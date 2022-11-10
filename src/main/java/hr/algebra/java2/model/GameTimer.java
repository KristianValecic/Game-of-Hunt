package hr.algebra.java2.model;

import java.io.Serializable;

public class GameTimer {

    private static int hour = 0;
    private static int minute;
    private static int second;
    private static int startMinute = 0;
    private static int startSecond = 10;
    private static int matchMinutesMax = 3;
    private static int matchSecondsMax = 0;
    private static int matchMinutesMin = 0;
    private static int matchSecondsMin = 5;
    //private static int defaultStartTimeSeconds = 10;

    //    private static int matchMinutes = 0;
//    private static int matchSeconds = 10;
    private static final int matchTimeIncrement = 5;

    private GameTimer(){}

    public static void setMatchTime(int minutes, int seconds) {
        GameTimer.minute = minutes;
        GameTimer.second = seconds;
        startMinute = minutes;
        startSecond = seconds;
    }

    public static String getCurrentTime(){
        return formatTime(minute, second);
    }
//    public static String getMatchTime(){
//        return formatTime(minute, second);
//    }

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
        return matchMinutesMax+":"+matchSecondsMax;
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

    public static String getTime() {
        return formatTime(minute, second);
    }

    public static void subMatchTime() {
        if (startSecond == 0){
            startMinute--;
            startSecond = 60 - matchTimeIncrement;
        }else
        {
            startSecond-=matchTimeIncrement;
        }
        second = startSecond;
        minute  =startMinute;
    }
    public static void addMatchTime() {
        startSecond+=matchTimeIncrement;
        if (startSecond == 60){
            startMinute++;
            startSecond = 0;
        }
        second = startSecond;
        minute  =startMinute;
    }

    public static boolean validateMinTime() {
        if (formatTime(minute, second).equals(GameTimer.minMatchTime())){
            return true;
        }
        return false;
    }
    public static boolean validateMaxTime() {
        if (formatTime(minute, second).equals(GameTimer.maxMatchTime())){
            return true;
        }
        return false;
    }

    public static String getDefaultMatchStartTime() {
        return formatTime(startMinute, startSecond);
    }

}
