package com.jibgirl.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.jibgirl.utils.UIUtils;
import static com.jibgirl.utils.UIUtils.*;

public class CharacterSelectionScreen extends JFrame {

    public CharacterSelectionScreen() {
        setTitle("เลือกตัวละคร - Jib Girl Game Cute Edition");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kawaii Background
        setContentPane(new BackgroundPanel("/com/jibgirl/asset/start.jpg"));
        getContentPane().setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("เลือกตัวละครที่คุณต้องการจีบ", SwingConstants.CENTER);
        titleLabel.setFont(UIUtils.getBalancedFont(Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel selectionPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(new EmptyBorder(0, 50, 20, 50));

        selectionPanel.add(createCharacterCard("มะปราง (Maprang)", "Maprang"));
        selectionPanel.add(createCharacterCard("ขนม (Kanom)", "Kanom"));
        selectionPanel.add(createCharacterCard("ไอซ์ (Ice)", "Ice"));

        add(selectionPanel, BorderLayout.CENTER);

        // ===== SOUTH: Back Button =====
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        PremiumButton backBtn = new PremiumButton("กลับหน้าหลัก");
        backBtn.setCute(true);
        backBtn.setPreferredSize(new Dimension(200, 60));
        backBtn.addActionListener(e -> {
            new StartScreen();
            dispose();
        });
        southPanel.add(backBtn);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createCharacterCard(String name, String charKey) {
        ModernPanel card = new ModernPanel(40);
        card.setBackground(new Color(255, 255, 255, 200)); // More opaque white for cute look
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(UIUtils.getBalancedFont(Font.BOLD, 22));
        nameLabel.setForeground(new Color(100, 50, 50));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(30, 0, 20, 0));

        // Character Sprite instead of Placeholder
        String spritePath = "/com/jibgirl/asset/" + charKey.toLowerCase() + "_normal.png";

        JLabel characterSprite = new JLabel();
        try {
            java.net.URL imgURL = getClass().getResource(spritePath);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(200, 280, Image.SCALE_SMOOTH);
                characterSprite.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
        }
        characterSprite.setAlignmentX(Component.CENTER_ALIGNMENT);

        PremiumButton selectButton = new PremiumButton("เลือกคนนี้แหละ!");
        selectButton.setCute(true);
        selectButton.setPreferredSize(new Dimension(220, 50));
        selectButton.setMaximumSize(new Dimension(220, 50));
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.addActionListener(e -> {
            new GameGui(charKey);
            dispose();
        });

        card.add(nameLabel);
        card.add(Box.createVerticalGlue());
        card.add(characterSprite);
        card.add(Box.createVerticalGlue());
        card.add(selectButton);
        card.add(Box.createVerticalStrut(30));

        return card;
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
                g.setColor(new Color(255, 200, 220, 60)); // Pastel pink overlay
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
