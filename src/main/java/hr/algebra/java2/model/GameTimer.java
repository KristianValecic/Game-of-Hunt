package hr.algebra.java2.model;

public class GameTimer {

    private static int hour = 0;
    private static int minute = 0;
    private static int second = 0;

    private GameTimer(){}

//    public GameTimer(int minutes, int seconds) {
//        minute = minutes;
//        second = minutes;
//    }
//    public GameTimer(int hour, int minute, int second) {
//        this.hour = hour;
//        this.minute = minute;
//        this.second = second;
//    }

    public static void setMatchTime(int minutes, int seconds) {
        GameTimer.minute = minutes;
        GameTimer.second = seconds;
    }

//    public GameTimer(String currentTime) {
//        String[] time = currentTime.split(":");
//        hour = Integer.parseInt(time[0]);
//        minute = Integer.parseInt(time[1]);
//        second = Integer.parseInt(time[2]);
//    }

    public static String getCurrentTime(){
        return hour + ":" + minute + ":" + second;
    }

    public static String matchOver() {
        return "0:0:1";
    }

    public static void oneSecondPassed(){
        second++;
        if(second == 60){
            minute++;
            second = 0;
            if(minute == 60){
                hour++;
                minute = 0;
                if(hour == 24){
                    hour = 0;
                    System.out.println("Next day");
                }
            }
        }
    }
    public static void countDownSecondPassed(){
        second--;
        if(second == -1){
            minute--;
            second = 59;
            if(minute == -1){
                hour--;
                minute = 59;
                if(hour == -1){
                    hour = 23;
                    System.out.println("Match Over");
                }
            }
        }
    }
}
