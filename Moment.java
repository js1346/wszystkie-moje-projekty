import java.lang.String ;

public class Moment {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;

    public Moment(int year, int month, int day, int hour, int minute,int second) {
        this.year = year;
        if (month > 0 && month < 13) this.month = month;
        else throw new IllegalArgumentException("Miesiac nalezy do przedzialu 1-12");

        if (this.month == 1 || this.month == 3 || this.month == 5 || this.month == 7 || this.month == 8 || this.month == 10 || this.month == 12) {
            if (day > 0 && day < 32) this.day = day;
            else throw new IllegalArgumentException("Ten miesiac nie moze miec wiecej niz 31 dni");
        } else {
            if (this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11) {
                if (day > 0 && day < 31) this.day = day;
                else throw new IllegalArgumentException("Ten miesiac nie moze miec wiecej niz 30 dni");
            }
            else{
            if (day > 0 && day < 29) this.day = day;
            else throw new IllegalArgumentException("Ten miesiac nie moze miec wiecej niz 28 dni");
            }
        }

        if (hour >= 0 && hour < 24) this.hour = hour;
        else throw new IllegalArgumentException("Godzina musi nalezec do przedzialu 0-24");

        if(minute >= 0 && minute < 60) this.minute=minute;
        else throw new IllegalArgumentException("Minuta musi nalezec do przedzialu 0-59");

        if(second >= 0 && second < 60) this.second=second;
        else throw new IllegalArgumentException("Sekunda musi nalezec do przedzialu 0-59");
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
    public int getSecond() {
        return month;
    }

    public String get(){
        return(year + "-" + month + "-" + day + "-" + hour + ":" + minute + ":"+second);
    }

    private int _isLater(Moment a){
        if(this.year>a.getYear()) return 2;
        if(this.year<a.getYear()) return 0;

        if(this.month>a.getMonth()) return 2;
        if(this.month<a.getMonth()) return 0;

        if(this.day>a.getDay()) return 2;
        if(this.day<a.getDay()) return 0;

        if(this.hour>a.getHour()) return 2;
        if(this.hour<a.getHour()) return 0;

        if(this.minute>a.getMinute()) return 2;
        if(this.minute<a.getMinute()) return 0;

        if(this.second>a.getSecond()) return 2;
        if(this.second<a.getSecond()) return 0;

        return 1;
    }

    public void isLater (Moment a){
        int b=_isLater(a);
        if(b == 2 ) System.out.println("ten moment jest pozniejszy");
        if(b == 0) System.out.println("ten moment jest wczesniejszy");
        if(b == 1) System.out.println("oba momenty sa identyczne");
    }

    public boolean isPrzestepny(){
        if(this.year % 4 != 0) return false;
        if(this.year % 100 != 0) return true;
        if(this.year % 400 != 0) return false;
        return true;
    }
    private long _subtract(Moment a, Moment b){
        long toReturn=0;
        long += (long)((a.getYear()-b.getYear())*);

    }

    public long  diffirence(Moment a){
        if(this._isLater(a) == 1) return 0;
        if(this._isLater(a) == 2) return _subtract(this,a);
        if(this._isLater(a) == 0) return _subtract(a,this);
    }
    public static void main(String[] args) {


    }
}
