package com.jibgirl.utils;

import javax.swing.*;

/**
 * QuestionTimer - Countdown timer for multiple choice questions
 * 30 second limit per question for multiplayer mode
 */
public class QuestionTimer {
    private static final int QUESTION_TIME_SECONDS = 30;
    private Timer timer;
    private int secondsRemaining;
    private Runnable onTimeUp;
    private TickListener onTickListener; // Called every second with remaining seconds

    @FunctionalInterface
    public interface TickListener {
        void onTick(int secondsRemaining);
    }

    public QuestionTimer() {
        this.secondsRemaining = QUESTION_TIME_SECONDS;
    }

    /**
     * Start the countdown timer
     */
    public void start() {
        secondsRemaining = QUESTION_TIME_SECONDS;
        timer = new Timer(1000, e -> {
            secondsRemaining--;
            if (onTickListener != null) {
                onTickListener.onTick(secondsRemaining);
            }
            if (secondsRemaining <= 0) {
                stop();
                if (onTimeUp != null) {
                    onTimeUp.run();
                }
            }
        });
        timer.start();
    }

    /**
     * Stop the timer
     */
    public void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * Get remaining seconds
     */
    public int getSecondsRemaining() {
        return Math.max(0, secondsRemaining);
    }

    /**
     * Check if time is up
     */
    public boolean isTimeUp() {
        return secondsRemaining <= 0;
    }

    /**
     * Set callback when time runs out
     */
    public void setOnTimeUp(Runnable callback) {
        this.onTimeUp = callback;
    }

    /**
     * Set callback for each tick (every second)
     */
    public void setOnTick(TickListener callback) {
        this.onTickListener = callback;
    }

    /**
     * Reset timer
     */
    public void reset() {
        stop();
        secondsRemaining = QUESTION_TIME_SECONDS;
    }

    /**
     * Get remaining time as percentage (0-100)
     */
    public int getPercentage() {
        return (getSecondsRemaining() * 100) / QUESTION_TIME_SECONDS;
    }

    /**
     * Check if time is critical (less than 5 seconds)
     */
    public boolean isCritical() {
        return getSecondsRemaining() < 5;
    }
}
