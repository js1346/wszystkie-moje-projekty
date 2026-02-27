#include <iostream>
#include <algorithm>
#include <vector>

//bledny

using namespace std;


int bazy[1000005];
int paliwo[1000005];
pair <bool,bool> dobre[1000005];
vector < pair <int,int> > s;
int n;

void wczytaj_dane(){
    cin>>n;
    for(int i=0; i<n;i++) cin>>paliwo[i]>>bazy[i];
    for(int i=0; i<n;i++) {dobre[i].first=1; dobre[i].second=1;}
}

void wykonaj_badanie(){

        
    for(int i=0;i<2*n;i++){

        s.push_back({paliwo[i%n]-bazy[i%n],i%n});
        while (!s.empty() && s.back().first<0){
            if(s.size()>1) s[s.size()-2].first += s.back().first;
            dobre[s.back().second].first=false;
            s.pop_back();
        }
    }
    while(!s.empty()) s.pop_back();

    for(int i=n-1;i>=0;i--){

            s.push_back({paliwo[i%n]-bazy[(i-1+n)%n],i%n});
            while (!s.empty() && s.back().first<0){
                if(s.size()>1) s[s.size()-2].first += s.back().first;
                dobre[s.back().second].second=false;
                s.pop_back();
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