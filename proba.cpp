#include <iostream>
using namespace std;

int fac(int n){
	if(n==1 ) return n;
	int dozret=0;
	for(int i=0;i<n;i++) dozret += fac(n-1);
	return dozret;
}

int main(){
	int n;
	cin>>n;
	cout<<fac(n);
	return 0;
}
