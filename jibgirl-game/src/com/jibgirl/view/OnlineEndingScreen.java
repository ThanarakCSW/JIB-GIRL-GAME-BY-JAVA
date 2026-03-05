package com.jibgirl.view;

import com.jibgirl.network.GameResult;
import com.jibgirl.network.GameClient;
import com.jibgirl.utils.UIUtils.ModernPanel;
import com.jibgirl.utils.UIUtils.PremiumButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class OnlineEndingScreen extends JFrame {
    private boolean isWinner;
    private GameResult myResult;
    private List<GameResult> allResults;
    private GameResult winnerResult;
    private GameClient client;

    public OnlineEndingScreen(GameResult myResult, List<GameResult> allResults, GameClient client) {
        this.myResult = myResult;
        this.allResults = allResults;
        this.client = client;
        this.winnerResult = GameResult.determineWinner(allResults);
        // [FIX] Winner is anyone who has the same top score as the absolute winner
        this.isWinner = (myResult.getFinalScore() == winnerResult.getFinalScore());

        setTitle("ผลลัพธ์การจีบสาว");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background
        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // ===== HEADER =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER: The 3 Cards Layout =====
        JPanel centerPanel = createCenterCardsPanel();
        add(centerPanel, BorderLayout.CENTER);

        // ===== SOUTH: Action Button =====
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(0, 0, 50, 0));

        PremiumButton backBtn = new PremiumButton("กลับหน้าหลัก");
        backBtn.setCute(true); // [FIX] Align with theme
        backBtn.setPreferredSize(new Dimension(200, 60));
        backBtn.setFont(new Font("Tahoma", Font.BOLD, 20));
        backBtn.addActionListener(e -> {
            if (client != null) {
                client.disconnect();
            }
            new StartScreen();
            dispose();
        });
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(60, 20, 20, 20));

        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 72));

        if (isWinner) {
            titleLabel.setText("คุณมัดใจสาวคนนี้ได้แล้ว");
            titleLabel.setForeground(new Color(255, 180, 50)); // Orange/Yellow
        } else {
            titleLabel.setText("คุณคือไอ่ขี้แพ้");
            titleLabel.setForeground(new Color(255, 100, 100)); // Red
        }

        panel.add(titleLabel);
        return panel;
    }

    private JPanel createCenterCardsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 50, 0, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;

        List<GameResult> sortedResults = GameResult.rankPlayers(allResults);

        // Find who goes where
        GameResult topWinner = sortedResults.get(0);
        int topScore = topWinner.getFinalScore();

        List<GameResult> winners = new ArrayList<>();
        List<GameResult> losers = new ArrayList<>();

        for (GameResult r : sortedResults) {
            if (r.getFinalScore() == topScore) {
                winners.add(r);
            } else {
                losers.add(r);
            }
        }

        // Display logic (up to 3 cards)
        if (winners.size() >= 3) {
            // Unusual case: 3-way tie!
            panel.add(createResultCard("พระเอก (เสมอ)", winners.get(1), true), gbc);
            panel.add(createResultCard("พระเอก (เสมอ)", winners.get(0), true), gbc);
            panel.add(createResultCard("พระเอก (เสมอ)", winners.get(2), true), gbc);
        } else if (winners.size() == 2) {
            // 2-way tie
            panel.add(createResultCard("พวกขี้แพ้", losers.isEmpty() ? null : losers.get(0), false), gbc);
            panel.add(createResultCard("พระเอก (เสมอ)", winners.get(0), true), gbc);
            panel.add(createResultCard("พระเอก (เสมอ)", winners.get(1), true), gbc);
        } else {
            // Single winner
            if (!losers.isEmpty()) {
                panel.add(createResultCard("พวกขี้แพ้", losers.get(0), false), gbc);
            } else {
                panel.add(createEmptyCard(), gbc);
            }

            panel.add(createResultCard("พระเอก", winners.get(0), true), gbc);

            if (losers.size() > 1) {
                panel.add(createResultCard("พวกขี้แพ้", losers.get(1), false), gbc);
            } else {
                panel.add(createEmptyCard(), gbc);
            }
        }

        return panel;
    }

    private JPanel createResultCard(String title, GameResult result, boolean isWinnerCard) {
        if (result == null)
            return createEmptyCard();

        ModernPanel card = new ModernPanel(40);
        // [FIX] Winner card uses Golden theme, Loser card uses Purple
        if (isWinnerCard) {
            card.setBackground(new Color(255, 215, 0, 200)); // Golden/Gold
        } else {
            card.setBackground(new Color(224, 187, 228, 200)); // Purple pastel
        }
        card.setPreferredSize(new Dimension(280, 400));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        titleLabel.setForeground(isWinnerCard ? new Color(100, 70, 0) : Color.WHITE);
        card.add(titleLabel, BorderLayout.NORTH);

        // Content area for stats
        JPanel stats = new JPanel();
        stats.setLayout(new BoxLayout(stats, BoxLayout.Y_AXIS));
        stats.setOpaque(false);
        stats.add(Box.createVerticalGlue());

        JLabel nameLabel = new JLabel(result.getPlayerName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        // Use darker contrast for golden background
        nameLabel.setForeground(isWinnerCard ? new Color(80, 50, 0) : new Color(100, 50, 100));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats.add(nameLabel);

        stats.add(Box.createVerticalStrut(10));

        JLabel charLabel = new JLabel("(" + result.getCharacterName() + ")", SwingConstants.CENTER);
        charLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        charLabel.setForeground(new Color(150, 100, 180));
        charLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats.add(charLabel);

        stats.add(Box.createVerticalStrut(30));

        JLabel scoreLabel = new JLabel("SCORE: " + result.getFinalScore(), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        scoreLabel.setForeground(new Color(100, 50, 100));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stats.add(scoreLabel);

        stats.add(Box.createVerticalGlue());
        card.add(stats, BorderLayout.CENTER);

        return card;
    }

    private JPanel createEmptyCard() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(280, 380));
        return panel;
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Clean white background as per mockup
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
