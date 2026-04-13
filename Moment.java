import java.lang.String ;

public class Moment {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private static int nieprzestepne[]={31,28,31,30,31,30,31,31,30,31,30,31};
    private static int przestepne[]   ={31,29,31,30,31,30,31,31,30,31,30,31};

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
    public boolean isPrzestepny(){
        if(this.year % 4 != 0) return false;
        if(this.year % 100 != 0) return true;
        if(this.year % 400 != 0) return false;
        return true;
    }

    private long getNumberOfDaysInMonth(int a){
        long dayCount=0;
        int i=0;
        if(isPrzestepny()){
            while(a > 0){
                dayCount+=przestepne[i];
                a--;
                i++;
            }
        }
        else {
            while(a > 0){
                dayCount+=przestepne[i];
                a--;
                i++;
            }
        }
        return dayCount;
    }

    public long momentToSecondNumber(){
        int year=this.year-1;
        long toReturn=0;
        toReturn+=year/400*numberOfSecondsIn400Years;
        year%=400;
        toReturn+=year/100*numberOfSecondsIn100Years;
        year%=100;
        toReturn+=year/4*numberOfSecondsIn4Years;
        year%=4;
        toReturn+=year*numberOfSecondsIn1Year;
        toReturn+=((getNumberOfDaysInMonth(this.month)+this.day)*86400);
        toReturn+=(this.hour*3600+this.minute*60+this.second);//automatic conversion to long
        return toReturn;
    }
    private class Duplex{
        int first;
        long second;
        public Duplex(int first,long second){
            this.first=first;
            this.second=second;
        }
        
    }
    private Duplex secondToMonth(long secondCount,Moment moment){
        Duplex duplex=new Duplex(1,0);
        int i=0;
        while(secondCount > 0){
            if(moment.isPrzestepny()){
                secondCount-=przestepne[i]*86400;
                if(secondCount<0){
                    secondCount+=przestepne[i]*86400;
                    duplex.second=secondCount;
                    return duplex;
                }
                else{
                    duplex.first++;
                    i++;
                }
            }
            else if(!moment.isPrzestepny()){
                secondCount-=nieprzestepne[i]*86400;
                if(secondCount<0){
                    secondCount+=nieprzestepne[i]*86400;
                    duplex.second=secondCount;
                    return duplex;
                }
                else{
                    duplex.first++;
                    i++;
                }
            }
        }
        return duplex;
    }
    
    public Moment secondToMoment(long secondCount){
        Moment toReturn=new Moment(0,0,0,0,0,0);
        toReturn.year +=(int) 400*(secondCount/numberOfSecondsIn400Years);
        secondCount %= numberOfSecondsIn400Years;
        toReturn.year +=(int) 100*(secondCount/numberOfSecondsIn100Years);
        secondCount %=numberOfSecondsIn100Years;
        toReturn.year +=(int) 4*(secondCount/numberOfSecondsIn4Years);
        secondCount %= numberOfSecondsIn4Years;
        toReturn.year += (int)secondCount/numberOfSecondsIn1Year;
        secondCount %= numberOfSecondsIn1Year;
        
        if(secondCount == 0){
            return new Moment(toReturn.getYear(),12,31,23,59,59);
        }
        
        toReturn.year += 1;
        Duplex duplex=secondToMonth(secondCount,toReturn);
        toReturn.month=duplex.first;
        secondCount=duplex.second;
        
        toReturn.day +=(int) secondCount/86400;
        secondCount %=86400;
        toReturn.hour +=(int) secondCount/3600;
        secondCount %= 3600;
        toReturn.minute += (int)secondCount/60;
        secondCount %= 60;
        toReturn.second=(int)secondCount
        return toReturn;
    }
    /*private int _isLater(Moment a){
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


    private long _subtract(Moment a, Moment b){
        long toReturn=0;
        long += (long)((a.getYear()-b.getYear())*);

    }

    public long  diffirence(Moment a){
        if(this._isLater(a) == 1) return 0;
        if(this._isLater(a) == 2) return _subtract(this,a);
        if(this._isLater(a) == 0) return _subtract(a,this);
    }*/
    public static void main(String[] args) {


    }
}
