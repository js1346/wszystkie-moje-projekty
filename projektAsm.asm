bits 64
global kody_polaczone       ;rdi-a0* rsi-*a1 i rdx-*ak, rcx-n, r8-k;
                            
section .text
kody_polaczone:            


;szukamy a1-a0, jezeli a0 to -2^63n, to a1-a0=a1+2^63n, czyli do rdx przenosimy a1, a do ostatniego bitu ostatniej cyfry dodajemy 1
;a znak bitu przetrzymywany w RAX to bedzie zero 
;w przeciwnym wypadku mozemy dokonac negacji i bit znaku wsadzic do rax

;sprawdzamy czy liczba a0 to -2^63n

sprawdzenie:
push   rdi                 ;odkladamy na stos
push   rcx  
mov    rax, 0              ;rax -T/F
sprawdzenie_petla:
cmp    rcx,1
jle    ostatni_obrot_sprawdzenia
cmp    qword[rdi], 0                   
jne     sprawdzenie_false
add    rdi,8
dec    rcx
jmp    sprawdzenie_petla
ostatni_obrot_sprawdzenia:
mov    r9,9223372036854775808 
cmp    qword[rdi], r9  ;sprawdzamy, czy ostatni wyraz to 10000000000
jne    sprawdzenie_false
mov    rax,1
jmp    end_sprawdzenie

sprawdzenie_false:
mov   rax, 0
end_sprawdzenie:
pop   rcx
pop   rdi
; zakonczenie sprawdzenia czy dana liczba to -2^63n

cmp    rax,1
jne    negacja
push   rdi
mov    rdi,rsi
call   przepisz     
mov    r9,9223372036854775808                           ;chcemy przepisac a1
add    qword[rdx+8*rcx-8],r9  ;dodajemy jedynke do wyrazenia z przodu
mov    rax,0
pop    rdi
jmp    koniec_dodawania


;sekcja negacji arytmetycznej liczby o rozmiarze n w adresie rdx
negacja:
call przepisz                    ;zaczynamy od przepisania liczby a0, by była dobra liczba w ak
push   rcx                  ;odkładamy na stos wartosc licznika
push   rdx                  ;odkładamy na stos adres liczby

negowanie_bitow:
cmp    rcx, 0                ;sprawdzamy, czy licznik juz doszedł do zera
jle    dodaj_jeden          ;jezeli przeszedł, negowanie bitów zakończone, mozemy przejsc do dodawania jedynki 
not    qword [rdx]          ;negujemy dany bit
dec    rcx                  ;zmniejszamy licznik pozostałych operacji
add    rdx, 8               ;zwiekszamy o 8 bajtów wskaźnik na rdi
jmp    negowanie_bitow      ;robimy nowy obrót petli

dodaj_jeden:                ;dodanie jeden jest bezpieczne i zachowuje rozmiar ze względu na założenie, ze w rdi nie ma 1000000000000
pop    rdx
pop    rcx
push   rcx                  ;odzyskujemy adres pierwszej kolumny
push   rdx
                  

petla_dodawania_jedynki:
cmp    rcx, 0
je    end_neg
dec    rcx
add    qword[rdx], 1        ;dodajemy jeden do liczby polozonej pod adresem rdx
jnc    end_neg                ;jezeli nie ma oferflow to nic nie musimy robic
add    rdx, 8               ;jezeli jest jeden to dodajemy do nastepnego adresu

jmp petla_dodawania_jedynki

end_neg:
pop rdx                     ;przywracamy liczby do odpowiednich rejestrow, aby stos był pusty
pop rcx

koniec_negacji:              ; nie wiem, czy takie użycie etykiety jest odpowiendnie

dodawanie:                  ;dodajemy zawartosc rdx zawartosci pod adresem rsi

push   rcx                  ;odkładamy na stos wartosc licznika
push   rdx                  ;odkładamy na stos adres liczby ak
push   rsi                  ;odkładamy na stos adres liczby a1

cmp    rcx, 0               ;cmp czysci carry flaga
jle    end 

dodawanie_bitow:
mov    rax,    qword[rsi]   ;dodajemy zawartosc 8 bitów pod rsi do zawartosci 8 bitów w rax
adc    qword[rdx], rax
lea    rsi,  [rsi+8]        ;przesuwamy oba wskaźniki o 8 bajtów
lea    rdx,  [rdx+8]                            
mov    rax,  1              ;w rax zostawimy znak overflowu(zeby kod byl krótszy, uzupełnimy go za kazdym razem)
jl     pomin_zmiane_rax     ;jezeli jl to SF != oF  to w pierwszym bicie ma byc jeden
mov    rax, 0

pomin_zmiane_rax:
dec    rcx                  ;zmniejszamy licznik pozostałych operacji
jnz    dodawanie_bitow      ;dec ustawia flage zf, a jnz ją zczytuje

end:
pop rsi                     ;przywracamy liczby do odpowiednich rejestrow, aby stos był pusty
pop rdx
pop rcx

koniec_dodawania:           ;uzyskalismy roznice a1-a0 w rejestrze rdx

uczynienie_dodatnimi:       ;w r10 przetrzymamy informacje, czy wynik mnozenia jest dodatni, czy ujemny 0-dodatni
mov    r10, 0
cmp    r8,0                 ;poprawiamy liczby na dodatnie
jge    nie_negujemy_r8
mov    r10,18446744073709551615 ;same jedynki w r10
neg    r8
nie_negujemy_r8: 
cmp    rax,0                    ;sprawdzamy, czy w rdx liczba jest dodatnia, czy ujemna (po zawartosci rax)
je     nie_negujemy_rdx
push   1
call   negacja

nie_negujemy_rdx:




ret
przepisz:                    ;przepisuje liczbe z rdi do rdx                           
push    rdx
push    rdi
push    rcx

przepisujemy_do_ak:
cmp     rcx,0
jle     end_przepisujemy_do_ak
mov     rax, qword[rdi]
mov     qword[rdx],rax
add     rdx,8
add     rdi,8
dec     rcx
jmp przepisujemy_do_ak

end_przepisujemy_do_ak:
pop     rcx
pop     rdi
pop     rdx
ret
;koniec przepisywania a0 pod adres ak, adres rdx wskazuje na koniec liczby ak
