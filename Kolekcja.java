// Kolekcja.java

import java.util.Iterator;

/**
 * Reprezentuje generyczną kolekcję elementów typu T (z powtórzeniami).
 * Obsługuje podstawowe operacje typu "dodaj", "usuń", "zawiera" etc.
 * Dodatkowo implementuje interfejs Iterable<T>, co pozwala iterować się po elementach kolekcji.
 * UWAGA: Zgodnie z https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html,
 *   iteracja nie musi gwarantować poprawności działania jeśli zawartość kolekcji zmieni się w trakcie iteracji.
 */
public class Kolekcja<T> implements Iterable<T> {
    private class Node{
        private T wartosc;
        private Node nastepny;
        private Node poprzedni;

        public T wartosc(){
            return this.wartosc;
        }
        public Node nastepny(){
            return this.nastepny;
        }
        public Node(){
            this.wartosc=null;
            this.nastepny=null;
            this.poprzedni=null;
        }
        public Node(T wartosc){
            this.wartosc=wartosc;
            this.poprzedni=null;
            this.nastepny=null;
        }

        public Node usun(){
            if(this.poprzedni == null && this.nastepny==null){
                return null;
            }
            if(this.poprzedni == null){
                this.nastepny.poprzedni=null;
                return this.nastepny;
            }
            if(this.nastepny==null){
                this.poprzedni.nastepny=null;
                return this.poprzedni;
            }
            this.poprzedni.nastepny = this.nastepny;
            this.nastepny.poprzedni = this.poprzedni;
            return this.nastepny;
        }
        public Node dodajPrzed(T wartosc){ 
            Node temp=new Node(wartosc);
            temp.nastepny=this;
            if(this.poprzedni != null){
                this.poprzedni.nastepny=temp;
            }
            temp.poprzedni=this.poprzedni;
            this.poprzedni=temp;
            return this;
        }
        public Node dodajZa(T wartosc){
            Node temp=new Node(wartosc);
            temp.poprzedni=this;
            if(this.nastepny != null){
                this.nastepny.poprzedni=temp;
            }
            temp.nastepny=this.nastepny;
            this.nastepny=temp;
            return this;
        }
        

    }
    private Node head;

    public Kolekcja() { 
        this.head=new Node();
        }

    // Podstawowe operacje na elementach.
    public void dodaj(T element) { 
        head.dodajZa(element); 
    }
    public boolean usuń(T element) {
        boolean czyJest=false;
        Node temp=head.nastepny();
        while(temp!=null){
            if(temp.wartosc().equals(element)){
                temp=temp.usun();
                czyJest=true;
            }
            else temp=temp.nastepny();
        }
        return czyJest;
    }
    public boolean zawiera(T element) { 
        boolean czyJest=false;
        Node temp=head.nastepny();
        while(temp!=null){
            if(temp.wartosc().equals(element)){
                czyJest=true;
            }
            temp=temp.nastepny();
        }
        return czyJest;
    }

    public int rozmiar() { 
    Node temp=head;
    int num=0;
        while(temp.nastepny()!=null){
            num++;
            temp=temp.nastepny();
        }
        return num;
    }
    public boolean pusta() {
        return this.rozmiar()>0;    
    }

    public void wyczyść() {
        Node temp=head.nastepny();
        while(temp!=null){
            temp=temp.usun();
        }
    }

    // Dodaj wszystkie elementy w podanych iterable
    public void dodajWszystkie(Iterable<T> S) {
        for(T element : S){
            this.dodaj(element);
        }
    }

    // Zwraca true wtedy i tylko wtedy gdy zawieram wszystkie elementy w S
    public boolean zawieraWszystkie(Kolekcja<T> S) { 
        boolean czyJest=false;
        boolean temp;
        for(T element : S){
            temp=this.zawiera(element);
            if(temp==true) czyJest=true;
        }
        return czyJest;
     }

    // Usuwa wszystkie elementy które nie występują w S
    public void zachowajWszystkie(Kolekcja<T> S) { 
        for(T element : this){
            if(!S.zawiera(element)){
                this.usuń(element);
            } 
        }
    }

    @Override
    public Iterator<T> iterator() { /* TODO */}

    @Override
    public String toString() { /* TODO */}
}
