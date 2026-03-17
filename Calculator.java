import java.util.Scanner;

public class Calculator {

    public static void main(String args[]) {
        System.out.println("=== Welcome to the calculator ===");
        System.out.println("Available operations (x,y are integers):");
        System.out.printf("%-18s %s%n", "- addition:", "x + y");
        System.out.printf("%-18s %s%n", "- subtraction:", "x - y");
        System.out.printf("%-18s %s%n", "- multiplication:", "x * y");

        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                String line = scanner.nextLine();
                String[] parts = line.trim().split(" ");

                if (parts.length != 3) {
                    throw new IllegalArgumentException("Invalid input: Expected format is 'x (+/-/*) y'");
                }

                String x = parts[0];
                String op = parts[1];
                String y = parts[2];

                assertIsInteger(x);
                assertIsInteger(y);
                assertIsOperation(op);

                String result = "";

                if (op.equals("+")) {
                    result = add(x, y);
                } else if (op.equals("-")) {
                    result = subtract(x, y);
                } else if (op.equals("*")) {
                    result = multiply(x, y);
                }

                System.out.println(result);
            }
        } finally {
            scanner.close();
        }
    }

    static void assertIsInteger(String x) {
        if (x.isEmpty() || x.equals("-")) {
            throw new IllegalArgumentException("Invalid input: " + x + " is not an integer");
        }
        if (x.equals("0")) {
            return;
        }
        int i = 0;
        if (x.charAt(0) == '-') {
            i = 1;
        }
        boolean foundNonZero = false;
        while (i < x.length()) {
            if (x.charAt(i) == '0' && !foundNonZero) {
                throw new IllegalArgumentException("Invalid input: " + x + " has leading zeroes");
            }
            if (x.charAt(i) < '0' || x.charAt(i) > '9') {
                throw new IllegalArgumentException("Invalid input: " + x + " is not an integer");
            }
            if (x.charAt(i) != '0') {
                foundNonZero = true;
            }
            i++;
        }
    }

    static void assertIsOperation(String op) {
        if (!op.equals("+") && !op.equals("-") && !op.equals("*")) {
            throw new IllegalArgumentException("Invalid input: " + op + " is not in {+,-,*}");
        }
    }

    static int max(int a,int b) {
        if (a > b) return a;
        else return b;
    }

    static String reverse(String z){
        String dozret=new String();
        for(int i=z.length()-1;i>=0;i--){
            dozret=dozret.concat(String.valueOf(z.charAt(i)));
        }
        return dozret;
    }
    //we assume x,y>=0
    static String _add(String x, String y){
        String z = x;

        if(x.length()<y.length()) {
            x = y;
            y = z;
        }
        z=new String();

        int xct=x.length()-1;
        int yct=y.length()-1;
        int help=0;

        while(yct >= 0){
            help+=(int)y.charAt(yct)+(int)x.charAt(xct)-2*'0';
            z=z.concat(String.valueOf((char)(help%10+48)));
            help/=10;
            xct--;yct--;
        }
        while(xct>=0){
            help+=(int)x.charAt(xct)-'0';
            z=z.concat(String.valueOf((char)(help%10+48)));
            help/=10;
            xct--;
        }
        while(help>0){
            z=z.concat(String.valueOf((char)(help%10+48)));
            help/=10;
        }


        return reverse(z);

    }

    //we assume x,y>=0 and x>=y
    static String _subtract(String x, String y){
        String dozret=new String();
        int borrowed=0;
        int head;
        int yct=y.length()-1;

        for(int i=x.length()-1;i>=0;i--){
            if(yct>=0) head=(int)(x.charAt(i)-'0')-(int)(y.charAt(yct)-'0')-borrowed;
            else head=(int)(x.charAt(i)-'0')-borrowed;

            if(head<0){
                head+=10;
                borrowed=1;
            }
            else borrowed=0;

            dozret=dozret.concat(String.valueOf(head));
            yct--;
        }

        return reverse(dozret);
    }


    static boolean isAbsBigger(String x, String y){
        if(x.length()>y.length()) return true;
        if(x.length()<y.length()) return false;
        for(int i=0;i<x.length();i++){
            if((int)x.charAt(i)>(int)y.charAt(i)) return true;
            if((int)x.charAt(i)<(int)y.charAt(i)) return false;
        }
        return true;
    }

    static boolean isBigger(String x, String y){
        if(x.charAt(0) != '-' && y.charAt(0) == '-' ) return true;
        if(x.charAt(0) == '-' && y.charAt(0) != '-' ) return false;
        if(x.charAt(0) != '-' && y.charAt(0) != '-' ) return isAbsBigger(x,y);
        if (x.charAt(0) == '-' && y.charAt(0) == '-' ) return !isAbsBigger(x.substring(1),y.substring(1));
        return true;
    }

    /*returns codification depending on conditions:
    1) both are geq 0 we can make normal addition
    2) both are leq 0 we can make normal addition ending with minus
    3)abs(x)>abs(y) and x-y>0
    4)abs(x)>abs(y) and x-y<0
    5)abs(y)>abs(x) and y-x>0
    6)abs(y)>abx(x) and y-x<0*/

    static int makeCode(String x, String y){
        if(x.charAt(0) != '-' && y.charAt(0) != '-' ) return 1;
        if(x.charAt(0) == '-' && y.charAt(0) == '-' ) return 2;

        if (isAbsBigger(x,y)==true){
            if (isBigger(x,y)==true) return 3;
            else return 4;
        }
        else{
            if (isBigger(x,y)==true) return 5;
            else return 6;
        }
    }

    static String add(String x, String y) {
        String z="-";
        int help=makeCode(x,y);
        if (help == 1) return _add(x,y);
        if (help == 2) return z.concat(_add(x.substring(1),y.substring(1)));
        if (help == 3) return _subtract(x,y.substring(1));
        if (help == 4) return z.concat(_subtract(x.substring(1),y));
        if (help == 5) return z.concat( _subtract(y.substring(1),x));
        if (help == 6) return _subtract(y,x.substring(1));
        return " ";
    }

    static String subtract(String x, String y) {

        if(y.charAt(0) == '-') {
            y=y.substring(1);
            return add(x,y);
        }
        else {
            String z="-";
            z=z.concat(y);
            return add(x, z);
        }
    }

    static String multiply(String x, String y) {
        String[] dict=new String[10];
        dict[0]="0";
        dict[1]=x;
        for(int i=2;i<=9;i++){
            dict[i]=add(x,dict[i-1]);
        }

        String dozret="0";
        String help=new String();


        for(int i=y.length()-1;i>=0;i--){
            String zeroes=new String();
            for(int j=y.length()-1-i;j>0;j--) zeroes=zeroes.concat(String.valueOf(0));

            help=dict[(int)(y.charAt(i)-'0')].concat(zeroes);
            dozret=add(dozret,help);
        }


        return dozret;
    }
}
