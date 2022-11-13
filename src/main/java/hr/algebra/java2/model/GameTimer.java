package hr.algebra.java2.model;

import java.io.Serializable;

public class GameTimer implements Serializable{

    private  int hour = 0;
    private  int minute;
    private  int second;
    private  int pauseMinute;
    private  int pauseSecond;
    private  int puaseSecondDuration = 1;
    private static int startMinute = 0;
    private static int startSecond = 10;
    private static int matchMinutesMax = 3;
    private static int matchSecondsMax = 0;
    private static int matchMinutesMin = 0;
    private static int matchSecondsMin = 5;
    private  final int matchTimeIncrement = 5;
    private  boolean gameTimeState;
    private  boolean gamePauseState;
    public static final String MATCH_OVER = "0:0";
    public static final String MAX_MATCH_TIME = formatTime(matchMinutesMax, matchSecondsMax);
    public static final String MIN_MATCH_TIME = formatTime(matchMinutesMin, matchSecondsMin);
    public static final String DEFAULT_MATCH_START_TIME = formatTime(startMinute, startSecond);
    private static GameTimer gameTimerInstance = null;

    private GameTimer() {
        pauseSecond = puaseSecondDuration;

        minute = startMinute;
        second = startSecond;
    }

    public static GameTimer getInstance() {
        if (gameTimerInstance == null)
            gameTimerInstance = new GameTimer();

        return gameTimerInstance;
    }

    public void setGameTimer(GameTimer gameTimer) {

    }

    public void setMatchTime(int minutes, int seconds) {
        this.minute = minutes;
        this.second = seconds;
        this.startMinute = minutes;
        this.startSecond = seconds;
    }

    public String getCurrentTime() {
        return formatTime(minute, second);
    }

    public void countDownSecondPassed() {
        gameTimeState = true;
        gamePauseState = false;

        second--;
        if (second == -1) {
            minute--;
            second = 59;
            if (minute == -1) {
                hour--;
                minute = 59;
                if (hour == -1) {
                    hour = 23;
                    System.out.println("Match Over");
                }
            }
        }
    }

    public void countDownPauseSecondPassed() {
        gamePauseState = true;
        gameTimeState = false;

        pauseSecond--;
        if (pauseSecond == -1) {
            pauseMinute--;
            pauseSecond = 59;
            if (pauseMinute == -1) {
                pauseMinute = 59;
                System.out.println("Pause Over");
            }
        }
    }

    public void resetTimer() {
        pauseSecond = puaseSecondDuration;

        minute = startMinute;
        second = startSecond;
    }

    public static String formatTime(int matchMinutes, int matchSconds) {
        return matchMinutes + ":" + matchSconds;
    }

//    public static int getCurrentMintues() {
//        return minute;
//    }
//
//    public static int getCurrentSeconds() {
//        return second;
//    }

    public String getTime() {
        if (gamePauseState) {
            return formatTime(pauseMinute, pauseSecond);
        }
        return formatTime(minute, second);
    }

    public void subMatchTime() {
        if (startSecond == 0) {
            startMinute--;
            startSecond = 60 - matchTimeIncrement;
        } else {
            startSecond -= matchTimeIncrement;
        }
        second = startSecond;
        minute = startMinute;
    }

    public void addMatchTime() {
        startSecond += matchTimeIncrement;
        if (startSecond == 60) {
            startMinute++;
            startSecond = 0;
        }
        second = startSecond;
        minute = startMinute;
    }

    public boolean validateMinTime() {
        if (formatTime(minute, second).equals(GameTimer.MIN_MATCH_TIME)) {
            return true;
        }
        return false;
    }

    public boolean validateMaxTime() {
        if (formatTime(minute, second).equals(GameTimer.MAX_MATCH_TIME)) {
            return true;
        }
        return false;
    }

    public boolean isPauseOver() {
        if (pauseSecond == 0) {
            return true;
        }
        return false;
    }

//    public static GameTimer getTimeForSave() {
//        if (gameTimeState) {
//            formatTime(minute, second);
//        } else if (gamePauseState) {
//            formatTime(pauseMinute, pauseSecond);
//        }
//
//    }
}
