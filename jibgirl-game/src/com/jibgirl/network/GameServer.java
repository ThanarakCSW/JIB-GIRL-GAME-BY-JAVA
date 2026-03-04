package com.jibgirl.network;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class GameServer {
    private static final int PORT = 12345;
    private static final int MAX_PLAYERS = 3;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final Map<Integer, PlayerState> playerStates = new ConcurrentHashMap<>();
    private final Map<Integer, GameResult> finishedPlayers = new ConcurrentHashMap<>();
    private int idCounter = 0;
    private volatile boolean gameEnded = false;

    public void start() {
        System.out.println("✨ Jib Girl Game Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                if (clients.size() >= MAX_PLAYERS) {
                    System.out.println("❌ Server full. Rejecting connection.");
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println("ERROR:Server is full (Max 3 players)");
                    socket.close();
                    continue;
                }

                int playerId = ++idCounter;
                ClientHandler handler = new ClientHandler(socket, playerId);
                clients.add(handler);
                new Thread(handler).start();
                System.out.println("💖 Player " + playerId + " connected. Total: " + clients.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private synchronized void broadcastSync() {
        StringBuilder sb = new StringBuilder("SYNC:");
        for (PlayerState state : playerStates.values()) {
            sb.append(state.toString()).append(";");
        }
        broadcast(sb.toString());
    }

    private synchronized void broadcastGameEnded() {
        StringBuilder sb = new StringBuilder("GAME_ENDED:");
        List<GameResult> results = new ArrayList<>(finishedPlayers.values());
        for (GameResult result : results) {
            sb.append(result.toString()).append(";");
        }
        broadcast(sb.toString());
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final int playerId;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket, int playerId) {
            this.socket = socket;
            this.playerId = playerId;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("WELCOME:" + playerId);

                String input;
                while ((input = in.readLine()) != null) {
                    if (input.startsWith("JOIN:")) {
                        String name = input.substring(5);
                        playerStates.put(playerId, new PlayerState(playerId, name));
                        broadcastSync();
                    } else if (input.startsWith("SELECT:")) {
                        String character = input.substring(7);
                        PlayerState state = playerStates.get(playerId);
                        if (state != null) {
                            state.character = character;
                            broadcastSync();
                        }
                    } else if (input.startsWith("UPDATE:")) {
                        String[] parts = input.substring(7).split(":");
                        PlayerState state = playerStates.get(playerId);
                        if (state != null && parts.length >= 2) {
                            state.day = Integer.parseInt(parts[0]);
                            state.affection = Integer.parseInt(parts[1]);
                            broadcastSync();
                        }
                    } else if (input.equals("START_GAME")) {
                        broadcast("GAME_STARTED");
                    } else if (input.startsWith("FINISH_GAME:")) {
                        // Player finished: FINISH_GAME:affection:stamina:character
                        String[] parts = input.substring(12).split(":");
                        if (parts.length >= 3) {
                            int affection = Integer.parseInt(parts[0]);
                            int stamina = Integer.parseInt(parts[1]);
                            String character = parts[2];

                            PlayerState state = playerStates.get(playerId);
                            if (state != null) {
                                GameResult result = new GameResult(playerId, state.name, character, affection, stamina);
                                finishedPlayers.put(playerId, result);
                                System.out.println(
                                        "🎮 Player " + playerId + " finished with score: " + result.getFinalScore());

                                // Check if all players finished
                                if (finishedPlayers.size() >= playerStates.size()) {
                                    gameEnded = true;
                                    broadcastGameEnded();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("👋 Player " + playerId + " disconnected.");
            } finally {
                clients.remove(this);
                playerStates.remove(playerId);
                broadcastSync();
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }

    public static class PlayerState {
        public int id;
        public String name;
        public String character = "None";
        public int day = 1;
        public int affection = 0;

        public PlayerState(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id + "," + name + "," + character + "," + day + "," + affection;
        }

        public static PlayerState fromString(String s) {
            String[] p = s.split(",");
            PlayerState state = new PlayerState(Integer.parseInt(p[0]), p[1]);
            state.character = p[2];
            state.day = Integer.parseInt(p[3]);
            state.affection = Integer.parseInt(p[4]);
            return state;
        }
    }

    public static void main(String[] args) {
        new GameServer().start();
    }
}
