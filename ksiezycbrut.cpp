#include <iostream>
#include <algorithm>

using namespace std;

int bazy[1000005];
int paliwo[1000005];
pair <bool,bool> dobre[1000005];
int n;

void wczytaj_dane(){
    cin>>n;
    for(int i=0; i<n;i++) cin>>paliwo[i]>>bazy[i];
    for(int i=0; i<n;i++) {dobre[i].first=1; dobre[i].second=1;}
}
void wykonaj_badanie(){

    for(int i=0;i<n;i++){

        int licznik=0;   
        
        for(int j=0;j<n;j++){

            licznik+=paliwo[(i+j)%n];
            licznik-=bazy[(i+j)%n];
            if(licznik<0) dobre[i].first=0;
        }

        licznik=0;

        for(int j=0;j<n;j++){
            licznik+=paliwo[(i-j+n)%n];
            licznik-=bazy[(i-j-1+n)%n];
            if(licznik<0) dobre[i].second=0;
        }
    }
}

void wypisz(){

    for(int i=0;i<n;i++){
        
        if(dobre[i].first==1||dobre[i].second==1) cout<<"TAK";
        else cout<<"NIE";
        cout<<endl;
    }
}

int main(){
    wczytaj_dane();
    wykonaj_badanie();
    wypisz();
    return 0;
}