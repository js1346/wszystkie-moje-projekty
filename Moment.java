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
    private static long numberOfSecondsIn400Years=12622780800L;
    private static long numberOfSecondsIn100Years=3155673600L;
    private static long numberOfSecondsIn4Years=126230400L;
    private static long numberOfSecondsIn1Year=31536000L;


    private Moment(){}//sets empty spaces, for private purpose

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
            else if(this.isPrzestepny()){
                if (day > 0 && day <= 29) this.day = day;
                else throw new IllegalArgumentException("Ten miesiac nie moze miec wiecej niz 29 dni");
            }
            else{
                if (day > 0 && day <= 28) this.day = day;
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
        return second;
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
            while(a > 1){
                dayCount+=przestepne[i];
                a--;
                i++;
            }
        }
        else {
            while(a > 1){
                dayCount+=nieprzestepne[i];
                a--;
                i++;
            }
        }
        return dayCount;
    }

    private long momentToSecondNumber(){
        int year=this.year-1;
        long toReturn=0;
        toReturn+=year/400*numberOfSecondsIn400Years;
        year%=400;
        toReturn+=year/100*numberOfSecondsIn100Years;
        year%=100;
        toReturn+=year/4*numberOfSecondsIn4Years;
        year%=4;
        toReturn+=year*numberOfSecondsIn1Year;
        toReturn+=((getNumberOfDaysInMonth(this.month)+this.day-1)*86400);
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

    private Moment secondToMoment(long secondCount){
        Moment toReturn=new Moment();

        toReturn.year +=(int) 400*(secondCount/numberOfSecondsIn400Years);
        secondCount %= numberOfSecondsIn400Years;

        //in cycles is ending on last day of 399 year, first division will give 0; but second lines will ad 400 years,
        //therefore we need to let the programm get 399 years, and 400th year will be analised int secondToMonth function

        long c100=secondCount/numberOfSecondsIn100Years;
        if(c100 == 4) c100 = 3;
        toReturn.year +=(int) c100*100;
        secondCount -= c100 *  numberOfSecondsIn100Years;

        //here there is no such a problem, because ending of 100 year cycle is not przestepny.
        //therefore next operesions won't give extended results

        toReturn.year +=(int) 4*(secondCount/numberOfSecondsIn4Years);
        secondCount %= numberOfSecondsIn4Years;

        long c1=secondCount/numberOfSecondsIn1Year;
        if(c1 == 4) c1 = 3;
        toReturn.year += (int)c1;
        secondCount -=c1 * numberOfSecondsIn1Year;

        toReturn.year += 1;
        Duplex duplex=secondToMonth(secondCount,toReturn);
        toReturn.month=duplex.first;
        secondCount=duplex.second;

        toReturn.day +=(1+(int) secondCount/86400);
        secondCount %=86400;
        toReturn.hour +=(int) secondCount/3600;
        secondCount %= 3600;
        toReturn.minute += (int)secondCount/60;
        secondCount %= 60;
        toReturn.second=(int)secondCount;
        return toReturn;
    }

    public boolean isLater(Moment a){
        long b=this.momentToSecondNumber();
        long c=a.momentToSecondNumber();
        if(b > c) return true;
        return false;
    }

    private void rescribe(Moment a){
        this.year=a.getYear();
        this.month=a.getMonth();
        this.day=a.getDay();
        this.hour=a.getHour();
        this.minute=a.getMinute();
        this.second=a.getSecond();
    }

    private void addManySeconds(long a){
        long b=this.momentToSecondNumber();
        b += a;
        if(b < 0) System.out.println("bład");
        Moment c=secondToMoment(b);
        rescribe(c);
    }

    public void addYear(){
        addManySeconds(numberOfSecondsIn1Year);
    }

    public void addDay(){
        addManySeconds(86400);
    }

    public void addHour(){
        addManySeconds(3600);
    }

    public void subtractYear(){
        addManySeconds(-numberOfSecondsIn1Year);
    }

    public void subtractHour(){
        addManySeconds(-3600);
    }
    public void subtractDay(){
        addManySeconds(-86400);
    }
    public long diffirenceBetweenMoments(Moment b){
        return this.momentToSecondNumber()-b.momentToSecondNumber();
    }

    public static void main(String[] args) {

    }
}
