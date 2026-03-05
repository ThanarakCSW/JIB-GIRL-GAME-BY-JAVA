package com.jibgirl.network;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class GameClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int myId;
    private final Map<Integer, GameServer.PlayerState> allPlayers = new HashMap<>();
    private final Map<Integer, GameResult> gameResults = new HashMap<>();
    private Consumer<Map<Integer, GameServer.PlayerState>> onSyncListener;
    private Consumer<Integer> onWelcomeListener;
    private Runnable onGameStartListener;
    private java.util.function.BiConsumer<Boolean, Map<Integer, GameResult>> onGameEndListener;
    private String targetCharacter = "None";

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    processMessage(line);
                }
            } catch (IOException e) {
                System.out.println("❌ Connection lost.");
            }
        }).start();
    }

    private void processMessage(String message) {
        if (message.startsWith("WELCOME:")) {
            myId = Integer.parseInt(message.substring(8));
            if (onWelcomeListener != null)
                onWelcomeListener.accept(myId);
        } else if (message.startsWith("SYNC:")) {
            String data = message.substring(5);
            allPlayers.clear();
            if (!data.isEmpty()) {
                String[] states = data.split(";");
                for (String s : states) {
                    if (!s.trim().isEmpty()) {
                        if (s.startsWith("TARGET-")) {
                            targetCharacter = s.substring(7);
                        } else {
                            GameServer.PlayerState state = GameServer.PlayerState.fromString(s);
                            allPlayers.put(state.id, state);
                        }
                    }
                }
            }
            if (onSyncListener != null)
                onSyncListener.accept(allPlayers);
        } else if (message.equals("GAME_STARTED")) {
            if (onGameStartListener != null)
                onGameStartListener.run();
        } else if (message.startsWith("GAME_ENDED:")) {
            String data = message.substring(11); // Skip "GAME_ENDED:"
            boolean isComplete = false;

            if (data.startsWith("COMPLETE:")) {
                isComplete = true;
                data = data.substring(9);
            } else if (data.startsWith("WAITING:")) {
                isComplete = false;
                data = data.substring(8);
            }

            gameResults.clear();
            if (!data.isEmpty()) {
                String[] resultStrings = data.split(";");
                for (String resultStr : resultStrings) {
                    if (!resultStr.trim().isEmpty()) {
                        GameResult result = GameResult.fromString(resultStr);
                        if (result != null) {
                            gameResults.put(result.getPlayerId(), result);
                        }
                    }
                }
            }
            if (onGameEndListener != null) {
                System.out.println(
                        "📡 Dispatching onGameEndListener: complete=" + isComplete + ", count=" + gameResults.size());
                onGameEndListener.accept(isComplete, gameResults);
            }
        }
    }

    public void join(String name) {
        if (out != null)
            out.println("JOIN:" + name);
    }

    public void selectCharacter(String character) {
        if (out != null)
            out.println("SELECT:" + character);
    }

    public void setTargetCharacter(String character) {
        if (out != null)
            out.println("SET_TARGET:" + character);
    }

    public String getTargetCharacter() {
        return targetCharacter;
    }

    public void updateProgress(int day, int affection, int stamina) {
        if (out != null)
            out.println("UPDATE:" + day + ":" + affection + ":" + stamina);
    }

    public void startGame() {
        if (out != null) {
            System.out.println("🎮 Sending START_GAME to server."); // Added logging
            out.println("START_GAME");
        }
    }

    public void finishGame(int affection, int stamina, String character) {
        if (out != null) {
            System.out.println("🎮 Sending FINISH_GAME to server: affection=" + affection + ", stamina=" + stamina
                    + ", character=" + character); // Added logging
            out.println("FINISH_GAME:" + affection + ":" + stamina + ":" + character);
        }
    }

    public void setOnSyncListener(Consumer<Map<Integer, GameServer.PlayerState>> listener) {
        this.onSyncListener = listener;
    }

    public void setOnWelcomeListener(Consumer<Integer> listener) {
        this.onWelcomeListener = listener;
    }

    public void setOnGameStartListener(Runnable listener) {
        this.onGameStartListener = listener;
    }

    public void setOnGameEndListener(java.util.function.BiConsumer<Boolean, Map<Integer, GameResult>> listener) {
        this.onGameEndListener = listener;
    }

    public int getMyId() {
        return myId;
    }

    public Map<Integer, GameServer.PlayerState> getAllPlayers() {
        return allPlayers;
    }

    public Map<Integer, GameResult> getGameResults() {
        return gameResults;
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out = null;
            in = null;
            socket = null;
            allPlayers.clear();
            gameResults.clear();
        }
    }

    public GameResult getMyResult() {
        return gameResults.get(myId);
    }
}
