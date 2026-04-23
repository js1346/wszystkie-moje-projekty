#include <stdio.h>
#include <stdlib.h>

// Deklarujemy Twoją funkcję z asemblera.
// Przyjmuje wskaźnik na int (int*) oraz rozmiar tablicy (int).
extern int findMax(int* array, int length);

int main() {
    int rozmiar = 0;
    scanf("%d",&rozmiar);
    int* liczby=malloc(sizeof(int)*rozmiar);

    for(int i=0;i<rozmiar;i++){
        scanf("%d",&liczby[i]);
    }
    
    int maksimum = findMax(liczby, rozmiar);
    
    printf("Najwieksza liczba w tablicy to: %d\n", maksimum);
    
    return 0;
}
