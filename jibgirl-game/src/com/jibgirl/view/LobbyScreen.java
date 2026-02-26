package com.jibgirl.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import com.jibgirl.network.GameClient;
import com.jibgirl.network.GameServer;
import com.jibgirl.utils.UIUtils.*;

public class LobbyScreen extends JFrame {
    private GameClient client;
    private JTextField nameField;
    private JTextField ipField;
    private JPanel playerListPanel;
    private PremiumButton startButton;
    private String myName = "";

    public LobbyScreen() {
        client = new GameClient();
        setTitle("💖 Multiplayer Lobby - Jib Girl Game 💖");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel background = new BackgroundPanel("/com/jibgirl/asset/start.jpg");
        background.setLayout(new BorderLayout());

        // ===== Connection Panel (North) =====
        JPanel connPanel = new JPanel(new FlowLayout());
        connPanel.setOpaque(false);
        connPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        nameField = new JTextField("Player", 10);
        ipField = new JTextField("localhost", 10);
        PremiumButton connectBtn = new PremiumButton("Join Server ✨");
        connectBtn.setCute(true);
        connectBtn.setPreferredSize(new Dimension(150, 40));

        connPanel.add(new JLabel("Name:"));
        connPanel.add(nameField);
        connPanel.add(new JLabel("IP:"));
        connPanel.add(ipField);
        connPanel.add(connectBtn);

        background.add(connPanel, BorderLayout.NORTH);

        // ===== Player List (Center) =====
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(playerListPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new EmptyBorder(20, 50, 20, 50));
        background.add(scroll, BorderLayout.CENTER);

        // ===== Actions (South) =====
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(0, 0, 50, 0));

        startButton = new PremiumButton("เริ่มเกม (เมื่อครบ 3 คน) 🚀");
        startButton.setCute(true);
        startButton.setPreferredSize(new Dimension(300, 60));
        startButton.setEnabled(false);
        southPanel.add(startButton);
        background.add(southPanel, BorderLayout.SOUTH);

        connectBtn.addActionListener(e -> connectToServer());
        startButton.addActionListener(e -> client.startGame());

        client.setOnSyncListener(this::updatePlayerList);
        client.setOnGameStartListener(() -> {
            SwingUtilities.invokeLater(() -> {
                new GameGui(client); // Modified GameGui to accept client
                dispose();
            });
        });

        setContentPane(background);
        setVisible(true);
    }

    private void connectToServer() {
        try {
            myName = nameField.getText();
            client.connect(ipField.getText(), 12345);
            client.join(myName);
            JOptionPane.showMessageDialog(this, "Connected! 🎉");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect: " + ex.getMessage());
        }
    }

    private void updatePlayerList(Map<Integer, GameServer.PlayerState> players) {
        SwingUtilities.invokeLater(() -> {
            playerListPanel.removeAll();
            for (GameServer.PlayerState p : players.values()) {
                ModernPanel panel = new ModernPanel(20);
                panel.setBackground(new Color(255, 255, 255, 180));
                panel.setLayout(new BorderLayout());
                panel.setMaximumSize(new Dimension(600, 60));
                panel.setBorder(new EmptyBorder(10, 20, 10, 20));

                JLabel nameLabel = new JLabel(p.name + (p.id == client.getMyId() ? " (You)" : ""));
                nameLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

                String charText = "Character: " + p.character;
                JLabel charLabel = new JLabel(charText);

                panel.add(nameLabel, BorderLayout.WEST);
                panel.add(charLabel, BorderLayout.EAST);

                // If it's me, allow character selection
                if (p.id == client.getMyId() && p.character.equals("None")) {
                    JComboBox<String> charBox = new JComboBox<>(new String[] { "None", "Maprang", "Kanom", "Ice" });
                    charBox.addActionListener(e -> client.selectCharacter((String) charBox.getSelectedItem()));
                    panel.add(charBox, BorderLayout.CENTER);
                }

                playerListPanel.add(panel);
                playerListPanel.add(Box.createVerticalStrut(10));
            }
            startButton.setEnabled(players.size() == 3);
            playerListPanel.revalidate();
            playerListPanel.repaint();
        });
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String fileName) {
            try {
                java.net.URL imgURL = getClass().getResource(fileName);
                if (imgURL != null)
                    backgroundImage = new ImageIcon(imgURL).getImage();
            } catch (Exception e) {
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
