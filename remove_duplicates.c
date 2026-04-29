#include <stdio.h>
#include <stdlib.h>


extern int remove_duplicates(int *arr, int size);
//usuwamy podwojone elementy z tablicy posortowanej
int main(){
    int size;

    if (scanf("%d", &size) != 1 || size <= 0){
        printf("Błąd: Podano nieprawidłowy rozmiar tablicy.\n");
        return 1;
    }

    int *tab = (int *)malloc(size * sizeof(int));
    if (tab == NULL){
        printf("Błąd: Nie udało się zaalokować pamięci.\n");
        return 1;
    }


    for (int i = 0; i < size; i++){
        if (scanf("%d", &tab[i]) != 1){
            printf("Błąd: Nieprawidłowe dane na pozycji %d.\n", i);
            free(tab);
            return 1;
        }
    }

    printf("--- PRZED ---\n");
    printf("Rozmiar: %d\n", size);
    for (int i = 0; i < size; i++){
        printf("%d ", tab[i]);
    }
    printf("\n\n");
  
    int new_size = remove_duplicates(tab, size);

    printf("--- PO ---\n");
    printf("Nowy rozmiar: %d\n", new_size);
    for (int i = 0; i < new_size; i++){
        printf("%d ", tab[i]);
    }
    printf("\n");

    free(tab);
    return 0;
}
