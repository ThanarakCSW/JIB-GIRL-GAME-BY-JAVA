package com.jibgirl.view;

import javax.swing.*;
import java.awt.*;
import com.jibgirl.utils.UIUtils.*;
import com.jibgirl.utils.UIUtils;

public class StartScreen extends JFrame {

    public StartScreen() {
        setTitle("💖 Jib Girl Game - Cute Edition 💖");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kawaii Background
        BackgroundPanel background = new BackgroundPanel("/com/jibgirl/asset/start.jpg");
        background.setLayout(new BorderLayout());

        // ===== Title Panel =====
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("✨ JIB GIRL ✨", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 72));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("~ Premium Dating Sim ~", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Tahoma", Font.ITALIC, 24));
        subtitleLabel.setForeground(UIUtils.PASTEL_PINK);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(titleLabel);
        centerPanel.add(subtitleLabel);

        titlePanel.add(centerPanel);
        background.add(titlePanel, BorderLayout.CENTER);

        // ===== Bottom Panel =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));

        PremiumButton startButton = new PremiumButton("เริ่มต้นความรักกันเถอะ! 💞✨");
        startButton.setCute(true);
        startButton.setFont(new Font("Tahoma", Font.BOLD, 28));
        startButton.setPreferredSize(new Dimension(400, 80));

        startButton.addActionListener(e -> {
            new CharacterSelectionScreen();
            dispose();
        });

        bottomPanel.add(startButton);
        background.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(background);
        setVisible(true);
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
                // Cute gradient overlay
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 182, 193, 100), 0, getHeight(),
                        new Color(224, 187, 228, 150));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        }
    }
}
