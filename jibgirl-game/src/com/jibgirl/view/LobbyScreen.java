package com.jibgirl.view;

import com.jibgirl.network.GameClient;
import com.jibgirl.network.GameServer;
import com.jibgirl.utils.UIUtils;
import com.jibgirl.utils.UIUtils.ModernPanel;
import com.jibgirl.utils.UIUtils.PremiumButton;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LobbyScreen extends JFrame {
    private GameClient client;
    private JTextField nameField;
    private JTextField ipField;
    private JPanel playerListPanel;
    private PremiumButton startButton;
    private String myName = "";
    private JPanel mainContainer;
    private CardLayout cardLayout;
    private JLabel targetCharLabel;
    private boolean isHost = false;
    private boolean isTransitioning = false;

    public LobbyScreen() {
        client = new GameClient();
        setTitle("Multiplayer Lobby - Jib Girl Game");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setOpaque(false);

        BackgroundPanel background = new BackgroundPanel("/com/jibgirl/asset/start.jpg");
        background.setLayout(new BorderLayout());

        // Create different panels
        createModeSelectionPanel();
        createCharacterSelectionPanel();
        createLobbyPanel();

        background.add(mainContainer, BorderLayout.CENTER);
        setContentPane(background);

        client.setOnSyncListener(this::updatePlayerList);
        client.setOnGameStartListener(() -> {
            SwingUtilities.invokeLater(() -> {
                if (isTransitioning)
                    return;
                isTransitioning = true;
                new GameGui(client);
                dispose();
            });
        });

        setVisible(true);
    }

    @Override
    public void dispose() {
        if (client != null && !isTransitioning) {
            client.disconnect();
        }
        super.dispose();
    }

    private void createModeSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        ModernPanel container = new ModernPanel(40);
        container.setBackground(new Color(255, 255, 255, 180));
        container.setPreferredSize(new Dimension(500, 400));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("โหมดออนไลน์", SwingConstants.CENTER);
        title.setFont(UIUtils.getBalancedFont(Font.BOLD, 32));
        title.setForeground(new Color(100, 50, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = new JTextField("Player");
        nameField.setFont(UIUtils.getBalancedFont(Font.PLAIN, 20));
        nameField.setMaximumSize(new Dimension(300, 50));

        ipField = new JTextField("localhost");
        ipField.setFont(UIUtils.getBalancedFont(Font.PLAIN, 20));
        ipField.setMaximumSize(new Dimension(300, 50));

        PremiumButton hostBtn = new PremiumButton("สร้างห้องใหม่ (Host)");
        hostBtn.setCute(true);
        hostBtn.setMaximumSize(new Dimension(300, 60));
        hostBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        hostBtn.addActionListener(e -> {
            isHost = true;
            startHost();
        });

        PremiumButton joinBtn = new PremiumButton("เข้าร่วมห้อง (Join)");
        joinBtn.setCute(true); // [FIX] Align with theme
        joinBtn.setMaximumSize(new Dimension(300, 60));
        joinBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinBtn.addActionListener(e -> {
            isHost = false;
            joinServer();
        });

        container.add(title);
        container.add(Box.createVerticalStrut(30));
        container.add(new JLabel("ชื่อผู้เล่น:"));
        container.add(nameField);
        container.add(Box.createVerticalStrut(10));
        container.add(new JLabel("Server IP: (สำหรับคนจอย)"));
        container.add(ipField);
        container.add(Box.createVerticalStrut(30));
        container.add(hostBtn);
        container.add(Box.createVerticalStrut(10));
        container.add(joinBtn);
        container.add(Box.createVerticalStrut(10));

        PremiumButton backBtn = new PremiumButton("กลับไปหน้าหลัก");
        backBtn.setCute(true); // [FIX] Align with theme
        backBtn.setMaximumSize(new Dimension(300, 60));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> {
            new StartScreen();
            dispose();
        });
        container.add(backBtn);

        panel.add(container);
        mainContainer.add(panel, "MODE");
    }

    private void createCharacterSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("เลือกตัวละครที่จะจีบ (Host Only)", SwingConstants.CENTER);
        title.setFont(UIUtils.getBalancedFont(Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(1, 3, 20, 0));
        grid.setOpaque(false);

        grid.add(createCharCard("Maprang", "มะปราง"));
        grid.add(createCharCard("Kanom", "ขนม"));
        grid.add(createCharCard("Ice", "ไอซ์"));

        panel.add(grid, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        PremiumButton backBtn = new PremiumButton("กลับ");
        backBtn.setCute(true); // [FIX] Align with theme
        backBtn.setPreferredSize(new Dimension(150, 50));
        backBtn.addActionListener(e -> {
            cardLayout.show(mainContainer, "MODE");
            isHost = false;
        });
        southPanel.add(backBtn);
        panel.add(southPanel, BorderLayout.SOUTH);

        mainContainer.add(panel, "CHAR_SELECT");
    }

    private JPanel createCharCard(String key, String name) {
        ModernPanel card = new ModernPanel(30);
        card.setBackground(new Color(255, 255, 255, 220));
        card.setLayout(new BorderLayout());

        JLabel sprite = new JLabel();
        String path = "/com/jibgirl/asset/" + key.toLowerCase() + "_normal.png";
        java.net.URL url = getClass().getResource(path);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
            sprite.setIcon(new ImageIcon(img));
        }
        sprite.setHorizontalAlignment(SwingConstants.CENTER);

        PremiumButton btn = new PremiumButton(name);
        btn.setCute(true);
        btn.addActionListener(e -> {
            client.setTargetCharacter(key);
            cardLayout.show(mainContainer, "LOBBY");
        });

        card.add(sprite, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);
        return card;
    }

    private void createLobbyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        ModernPanel header = new ModernPanel(20);
        header.setBackground(new Color(255, 255, 255, 200));
        header.setPreferredSize(new Dimension(800, 80));
        header.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));

        targetCharLabel = new JLabel("Target: None");
        targetCharLabel.setFont(UIUtils.getBalancedFont(Font.BOLD, 24));
        targetCharLabel.setForeground(new Color(150, 50, 100));
        header.add(targetCharLabel);

        panel.add(header, BorderLayout.NORTH);

        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setOpaque(false);
        JScrollPane scroll = new JScrollPane(playerListPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new EmptyBorder(20, 0, 20, 0));
        panel.add(scroll, BorderLayout.CENTER);

        startButton = new PremiumButton("เริ่มเกม!");
        startButton.setCute(true);
        startButton.setPreferredSize(new Dimension(300, 70));
        startButton.setEnabled(false);
        startButton.addActionListener(e -> client.startGame());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        south.setOpaque(false);

        PremiumButton exitBtn = new PremiumButton("ออกจากห้อง");
        exitBtn.setCute(true); // [FIX] Align with theme
        exitBtn.setPreferredSize(new Dimension(200, 70));
        exitBtn.addActionListener(e -> {
            client.disconnect();
            cardLayout.show(mainContainer, "MODE");
            isHost = false; // Reset host status
        });

        south.add(exitBtn);
        south.add(startButton);
        panel.add(south, BorderLayout.SOUTH);

        mainContainer.add(panel, "LOBBY");
    }

    private void startHost() {
        new Thread(() -> {
            new GameServer().start();
        }).start();

        Timer timer = new Timer(500, e -> {
            joinServer();
            if (client.getMyId() != 0) {
                ((Timer) e.getSource()).stop();
                cardLayout.show(mainContainer, "CHAR_SELECT");
            }
        });
        timer.start();
    }

    private void joinServer() {
        try {
            myName = nameField.getText();
            client.connect(ipField.getText(), 12345);
            client.join(myName);
            if (!isHost) {
                cardLayout.show(mainContainer, "LOBBY");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage());
        }
    }

    private void updatePlayerList(Map<Integer, GameServer.PlayerState> players) {
        SwingUtilities.invokeLater(() -> {
            if (client == null || targetCharLabel == null || playerListPanel == null)
                return;
            playerListPanel.removeAll();
            String target = client.getTargetCharacter();
            targetCharLabel.setText("เป้าหมายการจีบ: " + (target == null ? "None" : target));

            for (GameServer.PlayerState p : players.values()) {
                ModernPanel panel = new ModernPanel(20);
                panel.setBackground(new Color(255, 255, 255, 180));
                panel.setLayout(new BorderLayout());
                panel.setMaximumSize(new Dimension(700, 60));
                panel.setBorder(new EmptyBorder(10, 20, 10, 20));

                JLabel nameLabel = new JLabel(p.name + (p.id == client.getMyId() ? " (คุณ)" : ""));
                nameLabel.setFont(UIUtils.getBalancedFont(Font.BOLD, 18));
                panel.add(nameLabel, BorderLayout.WEST);

                JLabel statusLabel = new JLabel(p.character.equals("None") ? "⏳ กำลังเลือก..." : "✅ พร้อมแล้ว");
                statusLabel.setForeground(p.character.equals("None") ? Color.GRAY : new Color(0, 150, 0));
                panel.add(statusLabel, BorderLayout.EAST);

                playerListPanel.add(panel);
                playerListPanel.add(Box.createVerticalStrut(10));
            }

            if (isHost) {
                startButton.setEnabled(players.size() >= 1 && !client.getTargetCharacter().equals("None"));
            } else {
                startButton.setText("รอกลุ่มหัวหน้าเริ่มเกม...");
                startButton.setEnabled(false);
            }

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
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 182, 193, 100), 0, getHeight(),
                        new Color(180, 150, 255, 150)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        }
    }
}
