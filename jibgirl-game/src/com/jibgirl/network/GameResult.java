package com.jibgirl.network;

import java.util.*;

/**
 * GameResult - Tracks final game results for multiplayer mode
 * Determines winner based on Affection + Stamina score
 */
public class GameResult {
    public static final int CHOICE_TIME_LIMIT_SECONDS = 30;

    private int playerId;
    private String playerName;
    private String characterName;
    private int finalAffection;
    private int finalStamina;
    private int finalScore; // Affection + Stamina
    private long finishTime;
    private boolean finished;

    public GameResult(int playerId, String playerName, String characterName,
            int affection, int stamina) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.characterName = characterName;
        this.finalAffection = affection;
        this.finalStamina = stamina;
        this.finalScore = affection + stamina;
        this.finishTime = System.currentTimeMillis();
        this.finished = true;
    }

    // Getters
    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public int getFinalAffection() {
        return finalAffection;
    }

    public int getFinalStamina() {
        return finalStamina;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * Format result for network transmission
     */
    @Override
    public String toString() {
        return playerId + "," + playerName + "," + characterName + "," +
                finalAffection + "," + finalStamina + "," + finalScore + "," + finishTime;
    }

    /**
     * Parse result from network string
     */
    public static GameResult fromString(String s) {
        String[] parts = s.split(",");
        if (parts.length < 7)
            return null;

        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String character = parts[2];
        int affection = Integer.parseInt(parts[3]);
        int stamina = Integer.parseInt(parts[4]);

        return new GameResult(id, name, character, affection, stamina);
    }

    /**
     * Determine winner among multiple results
     * Winner = highest (Affection + Stamina)
     */
    public static GameResult determineWinner(List<GameResult> results) {
        if (results == null || results.isEmpty())
            return null;

        GameResult winner = results.get(0);
        for (GameResult result : results) {
            if (result.getFinalScore() > winner.getFinalScore()) {
                winner = result;
            }
        }
        return winner;
    }

    /**
     * Rank all players
     */
    public static List<GameResult> rankPlayers(List<GameResult> results) {
        if (results == null || results.isEmpty())
            return new ArrayList<>();

        List<GameResult> ranked = new ArrayList<>(results);
        ranked.sort((a, b) -> Integer.compare(b.getFinalScore(), a.getFinalScore()));
        return ranked;
    }
}
