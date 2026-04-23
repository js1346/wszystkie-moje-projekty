global findMax              ;rdi-adres poczatkowy tablicy liczb czterobitowych
                            ;esi-liczba elementow tablicy
                            ;rdx-aktualny adres
                            ;rcx-ostatni odpowiedni adres
section .text

findMax:

mov    rdx, rdi             ;wsadzany rdi do rdx 
movsxd rcx, esi             ;do rcx wsadzamy liczbe elementow               
lea    rcx, [rdx+4*rcx-4]   ;adres poczatku+4*iloscelementow-4*1element(ostatni indeks ma numer n-1)
mov    eax, dword [rdi]     ;pozyskujemy pierwsze minimum poprzez wsadzenie do rejestru wynikowego zawartosci pierwszej komorki tablicy
add    rdx, 4               ;przesuwamy rdi o 4 adresy dalej

start_while: 
cmp    rcx, rdx             ;porównujemy, czy jestesmy jeszcze w zasiegu tablicy
jl     end_while            ;jesli jestesmy za tablica, konczymy program
cmp    eax, [rdx]           ;porownujemy, czy zawartosc tablicy na danym elemencie jest wieksza niz dotychczasowy wynik
jge    checked              ;jezeli eax>[rdx], to idziemy od razu do checked(pomijamy nastepna linijke)
mov    eax, [rdx]           ;zamieniamy mniejszy rejest z wjekszym

checked:
add    rdx, 4              ; przesuwamy rdi o 4 adresy dalej

jmp    start_while          ;koniec petli start-while


end_while:
ret 
