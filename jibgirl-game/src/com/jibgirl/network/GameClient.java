package com.jibgirl.network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;

public class GameClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int myId;
    private final Map<Integer, GameServer.PlayerState> allPlayers = new HashMap<>();
    private Consumer<Map<Integer, GameServer.PlayerState>> onSyncListener;
    private Consumer<Integer> onWelcomeListener;
    private Runnable onGameStartListener;

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
                        GameServer.PlayerState state = GameServer.PlayerState.fromString(s);
                        allPlayers.put(state.id, state);
                    }
                }
            }
            if (onSyncListener != null)
                onSyncListener.accept(allPlayers);
        } else if (message.equals("GAME_STARTED")) {
            if (onGameStartListener != null)
                onGameStartListener.run();
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

    public void updateProgress(int day, int affection) {
        if (out != null)
            out.println("UPDATE:" + day + ":" + affection);
    }

    public void startGame() {
        if (out != null)
            out.println("START_GAME");
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

    public int getMyId() {
        return myId;
    }

    public Map<Integer, GameServer.PlayerState> getAllPlayers() {
        return allPlayers;
    }
}
