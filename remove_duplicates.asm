global remove_duplicates        ;rdi-adres liczby na starej tablicy
                                ;rdx-adres liczby na "nowej" tablicy
                                ;rsi-ilosc pozostałych elementów
                                ;rax-ilosc roznych elementow
                                ;ecx-ostatnia liczba
                                ;r8- rejestr pomocniczy do przytrzymania zmiennje

section .text

remove_duplicates:

cmp    rsi, 1                    ; jezeli dlugosc tablicy mniejsza niz dwa, nie ma co sprawdzac
jle    ending_only_one           ; wtedy mozemy przejsc do konca funkcji

mov    rax,  1                   ;pierwszy element zawsze mozemy wziac, licznik jest ustawiony na jeden
mov    ecx,  dword[rdi]          ;wsadzamy do ecx zawartosc pierwszego wyrazu
add    rdi,  4                   ;skoro pierwszy adres juz przeanalizowalismy, przesuwamy adres o 4
mov    rdx,  rdi                 ;duplikujemy adres drugiej zmiennej(pierwsza jest wypelniona przez pierwszy element)
dec    rsi                       ;pierwszy element juz przeanalizowany

start_while:
cmp    rsi,  0
jle    ending_more_elements      ;jezeli przeanalizowalismy wszystkie elementy, to mozemy zakonczyc dział, programu

dec    rsi                       ;zmniejszamy rsi (licznik w petli)
cmp    ecx,  dword[rdi]          ;sprawdzamy, czy rsi jest równe nastepnemu wyrazowu
jne    adding_new_element        ;jesli nie, to idziemy do sekcji dodawania nowego elementy

add    rdi,   4                  ;jezeli sa takie same, to przesuwamy rdi dalej
jmp start_while                  ;nic wiecej nie robimy

adding_new_element:

mov       r8d,   dword [rdi]       ;przerzucamy do rejestru r8d zawartosc pod adresem rdi
mov dword[rdx], r8d                ;z rejestru r8 wyciagamy 4bajty i wsadzamy pod adres rdx
mov       rcx,  [rdx]             ;aktualizujemy ostatnia liczbe
add       rdi,   4                ;kolejny przeanalizowany, przesuwamy
add       rdx,   4                ;kolejny zapelniony- przesuwamy
inc       rax                     ;zwiekszamy licznik roznych elementow
jmp       start_while             ;kolejna iteracja petli

ending_only_one:
mov    rax, rsi
ret

ending_more_elements:
ret
