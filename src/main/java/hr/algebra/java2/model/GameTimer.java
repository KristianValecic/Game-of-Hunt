package hr.algebra.java2.model;

import java.io.Serializable;

public class GameTimer {

    private static int hour = 0;
    private static int minute = 0;
    private static int startMinute = 0;
    private static int second = 0;
    private static int startSecond = 0;
    private static int matchMinutesMax = 2;
    //private static int matchSecondsMax = 0;
    private static int matchMinutesMin = 0;
    private static int matchSecondsMin = 5;

    private GameTimer(){}

    public static void setMatchTime(int minutes, int seconds) {
        GameTimer.minute = minutes;
        GameTimer.second = seconds;
        startMinute = minutes;
        startSecond = seconds;
    }

    public static String getCurrentTime(){
        return minute + ":" + second;
    }
    public static String getMatchStartTime(){
        return startMinute + ":" + startSecond;
    }

    public static String matchOver() {
        return "0:0";
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

    public static void resetTimer() {
        minute = startMinute;
        second = startSecond;
    }

    public static String formatTime(int matchMinutes, int matchSconds) {
        return matchMinutes+":"+matchSconds;
    }

    public static String maxMatchTime() {
        return matchMinutesMax+":"+matchSecondsMin;
    }
    public static String minMatchTime() {
        return matchMinutesMin+":"+matchSecondsMin;
    }
    

    public static int getCurrentMinues() {
        return minute;
    }

    public static int getCurrentSeconds() {
        return second;
    }
}
