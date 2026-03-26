import java.util.Scanner;

public class Ex4 {
    public static void main(String[] args) {

        Scanner scanner=new Scanner(System.in);
        scanner.useDelimiter(" ");
        scanner.useDelimiter("\n" + "");
        int n;
        int m;

        n=scanner.nextInt();
        m=scanner.nextInt();
        System.out.print(n + " " + m);

        for(int i=0 ; i<n; i++){
            System.out.print(n + " " + m);
        }

    }
}
