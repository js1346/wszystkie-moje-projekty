#include "zbior_ary.h"
#include <stdlib.h>
#include <stdio.h>
#include <limits.h>

#define inkri  if(i<a.siz) {aff=a.tab[i].ff;ass=a.tab[i].ss;} //skrot od inkrementuj i
#define inkrj  if(j<b.siz-1)  {j++;bff=b.tab[j].ff; bss=b.tab[j].ss;} //skrot od inkrementuj j

int max(int a, int b)
{
    if(a>b) return a;
    else return b;
}

int min(int a, int b)
{
    if(a>b) return b;
    else return a;
}

int q_glob;

int w_bez_mod_q(int a){
    int res = a%q_glob;
    if (res <0)
        res+=q_glob;
    return res;
}

resztazq* przepisz_kolret(resztazq *A)
{
    resztazq *X=(resztazq*)malloc(sizeof(resztazq));
    X->siz=A->siz;
    X->ktorareszta=A->ktorareszta;
    unsigned asiz=(unsigned int)A->siz;
    X->tab=(pii*)malloc(sizeof(pii)*asiz);
    for(int i=0; i<A->siz;i++){
        X->tab[i].ff=A->tab[i].ff;
        X->tab[i].ss=A->tab[i].ss;
    }
    return X;
}


zbior_ary ciag_arytmetyczny(int a,int q,int b) //generator zbiorow 
{
    q_glob=q;

    zbior_ary dozret; //skrot od do zreturnowania
    dozret.q=q_glob;
    dozret.siz=1;  
    dozret.kolret=(resztazq **)malloc(sizeof(resztazq*)); //alokujemy pamiec na 1 adres do tab z jednej reszty z q
    
    resztazq *res1=(resztazq*)malloc(sizeof(resztazq));
    res1->ktorareszta=w_bez_mod_q(a);//abyśmy mieli pewnosc, ze ktorareszta bel (0,q-1)
    //TODO: Overflow
    res1->siz=1;
    res1->tab=(pii *)malloc(sizeof(pii));   //alokujemy pamiec na 1 element(1 ciag aryt(aryq=1))
    res1->tab[0].ff=a;                     //pary<a,b> bo q jest stale
    res1->tab[0].ss=b;
    dozret.kolret[0]=res1;
      
    return dozret;
}

zbior_ary singleton(int a) //singleton to ciag o roznicy q i jednym elemencie 
{
    return ciag_arytmetyczny(a,q_glob,a);
}

resztazq* laczenie_tej_samej_reszty(resztazq a, resztazq b)
{
    resztazq *dozret=(resztazq*)malloc(sizeof(resztazq)); //alokujemy dozret poza funckją
    dozret->ktorareszta=a.ktorareszta;
    dozret->siz=a.siz+b.siz;
    unsigned dozretsiz2=(unsigned int)dozret->siz;//rzutowanie dozret siz na unsigned int
    dozret->tab=(pii *)malloc(sizeof(pii)*dozretsiz2);

    int i=0 , j=0;

    for(int licznik=0; licznik<dozret->siz; licznik++)
    {
       
        //if j=b.siz pobieramy z a
        //if i=a.siz pobieramy z b
        //if oba<b.siz pobieramy z mniejszego początku

        if(j==b.siz || (i < a.siz && a.tab[i].ff<b.tab[j].ff)) {
            dozret->tab[licznik]=a.tab[i];
            i++;
        }
        else{
            dozret->tab[licznik]=b.tab[j];
            j++;
        }
        //zauwazamy, ze nowa tablica bedzie posortowana dzieki a.ff<b.ff

        if(licznik>0){
            if((long long)dozret->tab[licznik].ff<=(long long)dozret->tab[licznik-1].ss+(long long)q_glob){
                dozret->tab[licznik-1].ss=max(dozret->tab[licznik].ss, dozret->tab[licznik-1].ss);
                licznik--;
                dozret->siz--;
                //jezeli dwa ciagi o tym samym modulo q sie pokrywaja, to trzeba je polaczzyc, a zatem zmniejszyc dozret.siz, 
                //ponadto zeby na tab[licznik] zaimplementowac nowy przedzial, to trzeba licnzik zmniejszyc o 1
            }
        }
    }
    //TODO
    unsigned ds=(unsigned int )dozret->siz;
    dozret->tab=(pii*)realloc(dozret->tab,ds*sizeof(pii));
    return dozret;
}

zbior_ary suma(zbior_ary A, zbior_ary B)
{
    zbior_ary dozret;
    dozret.q=q_glob;
    dozret.siz=A.siz+B.siz;
    unsigned dozretsiz2=(unsigned int)dozret.siz;//rzutujemy dozret.siz na unsigned int
    dozret.kolret=(resztazq**)malloc(sizeof(resztazq*)*dozretsiz2);  //alokujemy tablice wskżników

    int i=0,j=0;
    for(int licznik=0;licznik<dozret.siz;licznik++)
    { 
        //jezeli j=b.siz to bierzemy z i
        //jezeli i=a.siz to bierzemy z j
        //jezeli j<bsiz i i<asiz bierzemy z mniejszeho
        //kkolejne elementy segregujemy po ich resztach z q

        if(j==B.siz || (i < A.siz && A.kolret[i]->ktorareszta<B.kolret[j]->ktorareszta)){
            dozret.kolret[licznik]=przepisz_kolret(A.kolret[i]);
            // trzeba zaalokować pamięć i zaalokować
            i++;
        }
        else{
            dozret.kolret[licznik]=przepisz_kolret(B.kolret[j]);
            //trzeba zaalokować pamieć
            j++;
        }
        //jezeli ktores dwa maja te qsama reszte z , to muszą być na pozycjach licznik, licznik-1
        //wtedy je łączymy funkcją laczenie_tej_samej_reszty, odejmujemy jeden od licznika i jeden od size'a tablicy
        if(licznik>0){
            if (dozret.kolret[licznik]->ktorareszta==dozret.kolret[licznik-1]->ktorareszta){
                /*wskaźnik*/dozret.kolret[licznik-1]=laczenie_tej_samej_reszty(*dozret.kolret[licznik],*dozret.kolret[licznik-1]);    
                dozret.siz--;//anal. do poprzedniego
                licznik--;
            }
        }
    }
      unsigned ds=(unsigned int )dozret.siz;
     dozret.kolret=(resztazq**)realloc(dozret.kolret,ds*sizeof(resztazq*));
    return dozret;
}

resztazq* roznica_tej_samej_reszty(resztazq a, resztazq b)
{
    resztazq *dozret=(resztazq*)malloc(sizeof(resztazq)); //alokujemy dozret poza tablicą
    dozret->ktorareszta=a.ktorareszta;
    dozret->siz=a.siz;
    int pomoc=a.siz+b.siz; //b moze podzielic przedzial z a n co najwyzej dwa przedziały,czyli co najwyzej jeden nowy
    unsigned dozretsiz2=(unsigned int)pomoc; //rzutujemy i alokujemy
    dozret->tab=(pii *)malloc(sizeof(pii)*(dozretsiz2));

   
    int j=0,i=0;
    int l=0; 
        int aff=a.tab[i].ff;
        int ass=a.tab[i].ss;
        int bff=b.tab[j].ff;
        int bss=b.tab[j].ss;
    while(i<a.siz)
    {
        if(aff<bff){
            if(ass<bff){

                dozret->tab[l].ff=aff; dozret->tab[l].ss=ass;
                l++;i++;
                inkri 
        
            }else if(ass>bss){

                dozret->tab[l].ff=aff; dozret->tab[l].ss=bff-q_glob;
                l++;
                aff=bss+q_glob;
                inkrj


            }else if(ass<=bss){
                dozret->tab[l].ff=aff; dozret->tab[l].ss=bff-q_glob;
                l++;
                i++;
                inkri
            }

        }else if(aff>=bff){
            if(ass<=bss){

                i++; inkri
                inkrj

            }else if(aff<=bss){

                if(bss+q_glob<=INT_MAX) aff=bss+q_glob;

                inkrj

            }else {

                inkrj

                else {

                    dozret->tab[l].ff=aff;dozret->tab[l].ss=ass;
                    l++;i++;
                    inkri

                }
            }
        }
    }
    dozret->siz=l;
      unsigned ds=(unsigned int )dozret->siz;
    dozret->tab=(pii*)realloc(dozret->tab,ds*sizeof(pii));

    return dozret;    
}

zbior_ary roznica(zbior_ary A, zbior_ary B)
{
    zbior_ary dozret;
    dozret.q=q_glob;
    dozret.siz=A.siz;
    int pomoc=A.siz;                //rzutowanie A.siz na unsigned int
    unsigned dozretsiz2=(unsigned int)pomoc;
    dozret.kolret=(resztazq**)malloc(sizeof(resztazq*)*dozretsiz2);//alokacja tablicy zniaczników
    //bedziemy do kolret zapisywac a i robic odejmowanie z b, jezeli beda mialy takie same q

    int j=0,licznik=0;
    if(B.siz==0)
        {
            return A;
        }

    for(int i=0;i<dozret.siz;i++)
    {
        while( A.kolret[i]->ktorareszta > B.kolret[j]->ktorareszta && j<B.siz-1 ) j++;

        if ( A.kolret[i]->ktorareszta == B.kolret[j]->ktorareszta  ){ //jesli reszta z a[i]=reszta b[j] to laczymy
            dozret.kolret[licznik]=roznica_tej_samej_reszty(*A.kolret[i],*B.kolret[j]); 
            if (dozret.kolret[licznik]->siz == 0) { //jak rozmiar powstałej funkcji wynosi zero, to tą resztę można usunąć całkowicie
                licznik--;
            }        
        }
        else dozret.kolret[licznik]=przepisz_kolret(A.kolret[i]); //jesli a<b, to nie ma b, które mogłoby zmniejszać tablice a[i], wiec dozret i[dozret.kolret]=a.kolret[i]
        //trzeba zrboić kopie a (najkepiej osobna funkcja)
        licznik++;
    }
    dozret.siz=licznik;
      unsigned ds=(unsigned int )dozret.siz;
    dozret.kolret=(resztazq**)realloc(dozret.kolret,ds*sizeof(resztazq*));
    return dozret;
}

zbior_ary iloczyn(zbior_ary A, zbior_ary B) //a il b = a-(a-b)
{
    zbior_ary dozret;
    zbior_ary pom=roznica(A,B);
    dozret=roznica(A,pom);
    free(pom.kolret);
    return dozret;
}



bool nal_do_mod (resztazq a, int x)
{
   // printf("x-%d ", x);
    int lk=0;
    int pk=a.siz;
    int m=0;
    while(lk<pk) 
    {   
        m=(lk+pk-1)/2;  
        if(a.tab[m].ff<=x && a.tab[m].ss>=x) return true; //jesli nalezy do przedzialu m, to true
        else if(a.tab[m].ss<x) lk=m+1; //jesli nie, to nalezy do wiekszego lub mniejszego
        else pk=m;
    }
 
    return false; //jesli nie nalezy do zadnego, to false
}

bool nalezy(zbior_ary A, int x)
{   
    if(A.siz==0) return false;
    int lk=0;
    int pk=A.siz;
    bool wyn=false;
    int m=0;
    int modulox=w_bez_mod_q(x);

    while(lk+1<pk){ //bin search, który dla odpowiedniego modulo da lk=pk
        m=(lk+pk)/2;
        //printf("%d \n", A.kolret[m]->ktorareszta);
        if (A.kolret[m]->ktorareszta<=modulox) lk=m;
        else pk=m;
    }
    if(A.kolret[lk]->ktorareszta==modulox) { //jezeli ktorareszta jest rowna modul x, mozemy sprawdzac, czy nalezy, jesli nie, to na pewno nie nalezy
       // printf("true   ");
        wyn=nal_do_mod(*A.kolret[lk],x);
    }else wyn=false;

    return wyn;    
}

unsigned moc(zbior_ary A)
{
    long long int dozret=0;
   
    
    for(int i=0;i<A.siz;i++)
    {//dla kazdej ktorejreszty dodajemy ilosc elementow w ktorejreszczie
        for(int j=0;j<A.kolret[i]->siz;j++)
        { //sumujemy po przedziałach 
            dozret += ((long long)(A.kolret[i]->tab[j].ss)-A.kolret[i]->tab[j].ff)/q_glob + 1;

        }
    }
    unsigned dozret2=(unsigned )dozret;//rzutowanko na unsigned int

return dozret2;

}

unsigned ary(zbior_ary A)
{
    int dozret=0;
   
    for(int i=0;i<A.siz;i++)
    {
        //dodajemy ilość ciagów arytmetycznych w kazdym przedziale
        dozret += A.kolret[i]->siz;
    }
    unsigned dozret2=(unsigned int)dozret; //rzutowwanko na unsigned int 

    return dozret2;
}