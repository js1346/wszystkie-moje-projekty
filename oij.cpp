#include <iostream>
#include <vector>
#include <algorithm>
#include <bits/stdc++.h>

using namespace std;

int main ()
{ 
    int n; cin>>n;
    char p;
    vector <int> a(n);
    int i=0;
    while(i<n)
    {
        cin>>p;
        if(p=='O') a[i]++;
        else i++;
    }
    sort(a.begin(),a.end());
    for(int i=0;i<n;i++)
    {
        for(int j=0;j<a[i];j++) cout<<"O";
        cout<<endl;
    }

    return 0;
}