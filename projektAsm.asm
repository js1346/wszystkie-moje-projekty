bits 64
global kody_polaczone           ;rdi-a0* rsi-*a1 i rdx-*ak, rcx-n, r8-k;
                            
section .text
kody_polaczone:            

call sprawdzenie              ;sprawdzamy, czy wynik to -2^63^n i umieszczamy 0/1 w r11

cmp    r11,1                  ;jezeli prawda bylo, ze a0 to -2^63 n zastosujemy specjalny algorytm
jne    negacja
push   rdi     
mov    rdi,rsi
call   przepisz               ;przepisujemy a1 do rdx
mov    r9,9223372036854775808 ;dodajemy 10000000.. do ax za posrednictwem r9
add    qword[rdx+8*rcx-8],r9  ;(nie wywoływalismy funkcji z waznym overflow wiec mozemy)
mov    r9,0                   ;w r9 przetrzymamy znak liczby
pop    rdi                     
jmp    koniec_dodawania


;sekcja negacji arytmetycznej liczby o rozmiarze n w adresie rdx
negacja:
call    przepisz                    ;przepisujemy a0 do ak
call    znotuj_rdx                  ;przeprowadzamy negacje)logicza a0
call    dodaj_jeden                 ;dodajemy jedynke(wynik sie zmiesci ze wzgledu na to ze a0=-2^63n)
call    dodajemy                    ;dodajemy a1+(-a0) w r9 mamy znak liczby

koniec_dodawania:

uczynienie_dodatnimi:           ;w r10 przetrzymamy informacje, czy wynik mnozenia jest dodatni, czy ujemny 0-dodatni
mov    r10, 0
cmp    r8,0                     ;poprawiamy liczby na dodatnie
jge    nie_negujemy_r8
mov    r10,18446744073709551615 ;same jedynki w r10 jezeli jest ujemny
neg    r8                       ;liczba w r8 jest w nkb
nie_negujemy_r8: 
cmp    r9,0                     ;sprawdzamy, czy w rdx liczba jest dodatnia, czy ujemna (po zawartosci r9)
je     nie_negujemy_rdx
xor    r10, 18446744073709551615 ;jezeli +/-, to wynik to 111 jezeli ++/-- to wynik to 0....
mov    r9,0                      ;w r9 trzymamy znak lliczby       
call   znotuj_rdx       
call   dodaj_jeden

nie_negujemy_rdx:
wykonujemy_mnozenie_unsigned:
push    rcx
push    rdx
push    rsi
push    r9                  ;r9 zostaje do uzycia, najbardziej znaczacy bit poprzedniej cyfry jest na szczycie stosu
mov     rsi,rdx             ; teraz zamiast na rdx bedziemy wykonywac operacje na rsi, by w rdx mógł byc overflow z mnozenia
mov     r9,0                ;r9 przechowuje overflow
petla_mnozenia:
cmp     rcx,0               
je      koniec_mnozenia
mov     rax,qword[rsi]      ;wsadzamy zawartosc z rsi do rax
mul     r8                  ;mnozymy przez stala w r8
add     rax,r9              ;dodajemy overflow z poprzedniego
adc     rdx,0               ;ewentualnie dodajemy do przeniesienia aktualnego 1 z poprzedniego dodawania
mov    r9,rdx               ;przerzucamy overflow do r9
mov    [rsi], rax           ;zawartosc przemnozona wrzucamy spowrotem do odp rejestru
dec    rcx                  ;zmniejszamy licznik
add    rsi,8                ;przesuwamy adres
jmp petla_mnozenia

koniec_mnozenia:
pop    rax                  ;do rax wsadzamy ostatnia liczbe
mul    r8                   ;mnozymy przez stalą
add    rax,r9               ;dodajemy poprzedni overflow
adc    rdx,0                ;dodajemy ewentualny teraźniejszy overflow

pop rsi
pop r8                     ;mozemy juz zgubic liczbe k,, dlatego zawartosc niegdysiejszego rejestru rdx trafi do r8
pop rcx                    ;w rdx bedziemy trzymali juz przygotowany wynik

cmp     r10, 0                             ;sprawdzamy, czy mnozeniemialo zwrocic wynik ujemny
je      nie_przepisujemy_wyniku_na_ujemny  ;jezeli nie to omijamy tą funkcję
not     rax                                ;negujemy bity poczatkowe
not     rdx
push    rdx                                ;wsadzamy rdx na stos, by funkcja negujaca i dodajaca jeden 
mov     rdx, r8                            ;mogla odwolac sie do wlasciwego adresu
call    znotuj_rdx                         ;notujemy reszte-calosc znotowana
call    dodaj_jeden                        ;dodajemy jeden zgodnie z algorytmem
pop     rdx                                ;przywracamy do rdx pierwotna czesc liczby
add     rax,r9                             ;dodajemy overflow do rax
adc     rdx,0                              ;dodajemy ewentualny overflow do rdx

nie_przepisujemy_wyniku_na_ujemny:         ;mamy juz poprawny wynik k*(a1-a0); teraz wystarczy dodac a0 do funkcji
push  rdx
mov   rdx, r8
call dodajemy
   
ret


przepisz:                    ;przepisuje liczbe z rdi do rdx                           
push    rdx
push    rdi
push    rcx
push    rax

przepisujemy_do_ak:
cmp     rcx,0
jle     end_przepisujemy_do_ak
mov     rax, qword[rdi]        ;przepisujemy zawartosc za posrednictwem rejestru rax
mov     qword[rdx],rax
add     rdx,8
add     rdi,8                  ;zmniejszamy liczniki
dec     rcx
jmp przepisujemy_do_ak

end_przepisujemy_do_ak:
pop     rax
pop     rcx
pop     rdi
pop     rdx
ret



znotuj_rdx:             ;zmienia wszystkie bity w danej liczbie
                        ;zaczynamy od przepisania liczby a0, by była dobra liczba w ak
push   rcx                  ;odkładamy na stos wartosc licznika
push   rdx                  ;odkładamy na stos adres liczby

negowanie_bitow:
cmp    rcx, 0                ;sprawdzamy, czy licznik juz doszedł do zera
jle    end_neg          ;jezeli przeszedł, negowanie bitów zakończone, mozemy przejsc do dodawania jedynki 
not    qword [rdx]          ;negujemy dany bit
dec    rcx                  ;zmniejszamy licznik pozostałych operacji
add    rdx, 8               ;zwiekszamy o 8 bajtów wskaźnik na rdx
jmp    negowanie_bitow      ;robimy nowy obrót petli

end_neg:
pop rdx                     ;przywracamy liczby do odpowiednich rejestrow, aby stos był pusty
pop rcx
ret

dodaj_jeden:                ;bierze liczbe z rdx i dodaje jeden, overflow zapisuje w r9
push   rdx
push   rcx 
mov    r9,0            

petla_dodawania_jedynki:
cmp    rcx, 0
je     ostatnie_carry
dec    rcx                  ;zmniejszamy licznik
add    qword[rdx], 1        ;dodajemy jeden do liczby polozonej pod adresem rdx
jnc    end_neg_2             ;jezeli nie ma oferflow to mozemy zakonczyc funkcjie
add    rdx, 8               ;jezeli jest jeden to dodajemy do nastepnego adresu
jmp petla_dodawania_jedynki

ostatnie_carry:
mov    r9,1                ;jezeli nie wyszlismy przez end_neg_2 to znaczy, ze byl overflow
end_neg_2:
pop     rcx
pop     rdx
ret

dodajemy:                  ;dodajemy zawartosc rsi do liczby pod adresem rdx

push   rcx                  
push   rdx                 
push   rsi                  
push   rax

mov    r9,0                 ;r9 to bedzie rejestr zwracajacy overflow
cmp    rcx, 0               ;cmp czysci carry flaga
jle    end 

dodawanie_bitow:
mov    rax,    qword[rsi]   ;dodajemy zawartosc 8 bitów pod rsi do zawartosci 8 bitów w rax
adc    qword[rdx], rax      ;dodajemy do rdx zawartosc rax, przechowujac flage cf
lea    rsi,  [rsi+8]        ;przesuwamy oba wskaźniki o 8 bajtów
lea    rdx,  [rdx+8]                            
mov    r9,  1               ;w rax zostawimy znak overflowu(zeby kod byl krótszy, uzupełnimy go za kazdym razem)
jl     pomin_zmiane_rax     ;jezeli jl to SF != oF  to w pierwszym bicie ma byc jeden
mov    r9, 0                

pomin_zmiane_rax:
dec    rcx                  ;zmniejszamy licznik pozostałych operacji
jnz    dodawanie_bitow      ;dec ustawia flage zf, a jnz ją zczytuje

end:
pop rax
pop rsi                     ;przywracamy liczby do odpowiednich rejestrow, aby stos był pusty
pop rdx
pop rcx
ret

sprawdzenie:                        ;sprawdza, czy liczba w rdi to -2^63n i wynik umieszcza w r11
push   rdi                          ;odkladamy na stos
push   rcx  
push   r9
mov    r11, 0              

sprawdzenie_petla:
cmp    rcx,1                       ;ostatni obrót sprawdzenia jest inny
jle    ostatni_obrot_sprawdzenia
cmp    qword[rdi], 0               ;sprawdza, czy mało znaczące bity to zero 
jne    sprawdzenie_false           ;jezeli tak, to przechodzi do sprawdzanie_false
add    rdi,8                        
dec    rcx
jmp    sprawdzenie_petla

ostatni_obrot_sprawdzenia:
mov    r9,9223372036854775808 
cmp    qword[rdi], r9             ;sprawdzamy, czy ostatni wyraz to 10000000000
jne    sprawdzenie_false          ;jezeli nie bylo sprawdzanie false, to ustawiamy r11 na 1(true)

mov    r11,1                 
jmp    end_sprawdzenie

sprawdzenie_false:
mov   r11, 0
end_sprawdzenie:
pop   r9
pop   rcx
pop   rdi
ret 
