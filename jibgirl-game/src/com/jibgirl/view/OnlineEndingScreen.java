package com.jibgirl.view;

import com.jibgirl.network.GameResult;
import com.jibgirl.utils.UIUtils.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * OnlineEndingScreen - Displays final results in multiplayer mode
 * Shows winner/loser status with ranked leaderboard
 */
public class OnlineEndingScreen extends JFrame {
    private boolean isWinner;
    private GameResult myResult;
    private List<GameResult> allResults;
    private GameResult winnerResult;

    public OnlineEndingScreen(GameResult myResult, List<GameResult> allResults) {
        this.myResult = myResult;
        this.allResults = allResults;
        this.winnerResult = GameResult.determineWinner(allResults);
        this.isWinner = (myResult.getPlayerId() == winnerResult.getPlayerId());

        setTitle("🎮 เกมจบลง - " + (isWinner ? "ชนะ!" : "แพ้"));
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background
        setContentPane(new BackgroundPanel());
        getContentPane().setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER: Your Result Card =====
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // ===== SOUTH: Ranking Table =====
        JPanel southPanel = createRankingPanel();
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Header showing win/lose status
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 20, 20, 20));

        if (isWinner) {
            // Winner header
            JLabel titleLabel = new JLabel("🏆 คุณชนะ! 🏆");
            titleLabel.setFont(new Font("Tahoma", Font.BOLD, 48));
            titleLabel.setForeground(new Color(255, 215, 0)); // Gold
            panel.add(titleLabel);
        } else {
            // Loser header
            JLabel titleLabel = new JLabel("💔 คุณแพ้ 💔");
            titleLabel.setFont(new Font("Tahoma", Font.BOLD, 48));
            titleLabel.setForeground(new Color(200, 50, 50)); // Red
            panel.add(titleLabel);
        }

        return panel;
    }

    /**
     * Your final stats card
     */
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));

        // Title
        JLabel yourResultLabel = new JLabel("📊 ผลลัพธ์ของคุณ");
        yourResultLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        yourResultLabel.setForeground(new Color(100, 50, 100));

        // Stats card
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 3, 20, 20));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Player name
        ModernPanel nameCard = new ModernPanel(15);
        nameCard.setBackground(new Color(255, 200, 220, 220));
        JLabel nameLabel = new JLabel(myResult.getPlayerName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        nameLabel.setForeground(new Color(100, 50, 50));
        nameCard.add(nameLabel);

        // Character
        ModernPanel charCard = new ModernPanel(15);
        charCard.setBackground(new Color(220, 200, 255, 220));
        JLabel charLabel = new JLabel(myResult.getCharacterName(), SwingConstants.CENTER);
        charLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        charLabel.setForeground(new Color(80, 50, 100));
        charCard.add(charLabel);

        // Empty space
        JPanel emptyPanel1 = new JPanel();
        emptyPanel1.setOpaque(false);

        // Affection
        ModernPanel affectionCard = new ModernPanel(15);
        affectionCard.setBackground(new Color(255, 200, 220, 220)); // Pastel pink
        JLabel affectionTitle = new JLabel("💖 ความรัก");
        affectionTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
        affectionTitle.setForeground(new Color(100, 50, 50));
        JLabel affectionValue = new JLabel(String.valueOf(myResult.getFinalAffection()), SwingConstants.CENTER);
        affectionValue.setFont(new Font("Tahoma", Font.BOLD, 32));
        affectionValue.setForeground(new Color(200, 80, 100));
        affectionCard.setLayout(new BorderLayout());
        affectionCard.add(affectionTitle, BorderLayout.NORTH);
        affectionCard.add(affectionValue, BorderLayout.CENTER);

        // Stamina
        ModernPanel staminaCard = new ModernPanel(15);
        staminaCard.setBackground(new Color(200, 220, 255, 200));
        JLabel staminaTitle = new JLabel("⚡ สมดุลชีวิต");
        staminaTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
        staminaTitle.setForeground(new Color(50, 50, 150));
        JLabel staminaValue = new JLabel(String.valueOf(myResult.getFinalStamina()), SwingConstants.CENTER);
        staminaValue.setFont(new Font("Tahoma", Font.BOLD, 32));
        staminaValue.setForeground(new Color(50, 100, 200));
        staminaCard.setLayout(new BorderLayout());
        staminaCard.add(staminaTitle, BorderLayout.NORTH);
        staminaCard.add(staminaValue, BorderLayout.CENTER);

        // Total score
        ModernPanel scoreCard = new ModernPanel(15);
        scoreCard.setBackground(new Color(255, 240, 200, 220));
        JLabel scoreTitle = new JLabel("🎯 คะแนนรวม");
        scoreTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scoreTitle.setForeground(new Color(150, 100, 0));
        JLabel scoreValue = new JLabel(String.valueOf(myResult.getFinalScore()), SwingConstants.CENTER);
        scoreValue.setFont(new Font("Tahoma", Font.BOLD, 32));
        scoreValue.setForeground(new Color(200, 150, 0));
        scoreCard.setLayout(new BorderLayout());
        scoreCard.add(scoreTitle, BorderLayout.NORTH);
        scoreCard.add(scoreValue, BorderLayout.CENTER);

        statsPanel.add(nameCard);
        statsPanel.add(charCard);
        statsPanel.add(emptyPanel1);
        statsPanel.add(affectionCard);
        statsPanel.add(staminaCard);
        statsPanel.add(scoreCard);

        panel.add(yourResultLabel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Leaderboard showing all players' rankings
     */
    private JPanel createRankingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 40, 30, 40));

        // Title
        JLabel rankingTitle = new JLabel("🏅 อันดับผู้เล่น");
        rankingTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        rankingTitle.setForeground(new Color(100, 50, 100));

        // Ranking table
        JPanel rankingContent = new JPanel();
        rankingContent.setLayout(new BoxLayout(rankingContent, BoxLayout.Y_AXIS));
        rankingContent.setOpaque(false);
        rankingContent.setBorder(new EmptyBorder(15, 0, 0, 0));

        List<GameResult> ranked = GameResult.rankPlayers(allResults);
        for (int i = 0; i < ranked.size(); i++) {
            GameResult result = ranked.get(i);
            JPanel rankRow = createRankRow(i + 1, result);
            rankingContent.add(rankRow);
            if (i < ranked.size() - 1) {
                rankingContent.add(Box.createVerticalStrut(10));
            }
        }

        panel.add(rankingTitle, BorderLayout.NORTH);
        panel.add(rankingContent, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Single rank row in leaderboard
     */
    private JPanel createRankRow(int rank, GameResult result) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        // Rank badge
        ModernPanel rankBadge = new ModernPanel(10);
        rankBadge.setPreferredSize(new Dimension(60, 60));
        rankBadge.setBackground(getRankColor(rank));
        JLabel rankLabel = new JLabel(getRankIcon(rank) + " #" + rank, SwingConstants.CENTER);
        rankLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        rankLabel.setForeground(Color.WHITE);
        rankBadge.add(rankLabel);

        // Player info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(0, 15, 0, 15));

        JLabel nameLabel = new JLabel(result.getPlayerName() + " (" + result.getCharacterName() + ")");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        nameLabel.setForeground(new Color(80, 50, 80));

        JLabel statsLabel = new JLabel(
                String.format("💖 %d | ⚡ %d | 🎯 %d",
                        result.getFinalAffection(),
                        result.getFinalStamina(),
                        result.getFinalScore()));
        statsLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        statsLabel.setForeground(new Color(100, 100, 100));

        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(statsLabel, BorderLayout.SOUTH);

        row.add(rankBadge, BorderLayout.WEST);
        row.add(infoPanel, BorderLayout.CENTER);

        return row;
    }

    /**
     * Get medal icon based on rank
     */
    private String getRankIcon(int rank) {
        switch (rank) {
            case 1:
                return "🥇";
            case 2:
                return "🥈";
            case 3:
                return "🥉";
            default:
                return "⭐";
        }
    }

    /**
     * Get color based on rank
     */
    private Color getRankColor(int rank) {
        switch (rank) {
            case 1:
                return new Color(255, 215, 0); // Gold
            case 2:
                return new Color(192, 192, 192); // Silver
            case 3:
                return new Color(205, 127, 50); // Bronze
            default:
                return new Color(150, 150, 150); // Gray
        }
    }

    /**
     * Background panel
     */
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Gradient background
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 240, 250),
                    0, getHeight(), new Color(240, 220, 255));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
