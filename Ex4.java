package ex4;
import java.util.Scanner;


public class Ex4 {
    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);

        int n;
        int m;
        String t1emp=new String();
        String t2emp=new String();

        n=scanner.nextInt();
        m=scanner.nextInt();
        Graph graph=new Graph(n);


        for(int i=0 ; i<m; i++){
            t1emp=scanner.next();
            t2emp=scanner.next();
            graph.addVertex(t1emp);
            graph.addVertex(t2emp);
            graph.addEdge(t1emp,t2emp);
        }

        String toReturn=graph.findTriangle();
        System.out.print(toReturn);

    }
}
