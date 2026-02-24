package com.jibgirl.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.jibgirl.utils.UIUtils.*;
import com.jibgirl.utils.UIUtils;

public class CharacterSelectionScreen extends JFrame {

    public CharacterSelectionScreen() {
        setTitle("💖 เลือกตัวละคร - Jib Girl Game Cute Edition 💖");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kawaii Background
        setContentPane(new BackgroundPanel("/com/jibgirl/asset/start.jpg"));
        getContentPane().setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("✨ เลือกตัวละครที่คุณต้องการจีบ ✨", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(50, 0, 40, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel selectionPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(new EmptyBorder(0, 50, 80, 50));

        selectionPanel.add(createCharacterCard("มะปราง (Maprang) 🖌️", "Maprang"));
        selectionPanel.add(createCharacterCard("อลิซ (Alice) 🎀", "Alice"));
        selectionPanel.add(createCharacterCard("เบลล่า (Bella) 🏀", "Bella"));

        add(selectionPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createCharacterCard(String name, String charKey) {
        ModernPanel card = new ModernPanel(40);
        card.setBackground(new Color(255, 255, 255, 200)); // More opaque white for cute look
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        nameLabel.setForeground(new Color(100, 50, 50));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(new EmptyBorder(30, 0, 20, 0));

        // Image Placeholder (Pinkish box)
        ModernPanel imagePlaceholder = new ModernPanel(20);
        imagePlaceholder.setBackground(UIUtils.PASTEL_PINK);
        imagePlaceholder.setPreferredSize(new Dimension(200, 280));
        imagePlaceholder.setMaximumSize(new Dimension(200, 280));
        JLabel placeholderIcon = new JLabel("👑");
        placeholderIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        placeholderIcon.setForeground(new Color(255, 255, 255, 180));
        imagePlaceholder.add(placeholderIcon);
        imagePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        PremiumButton selectButton = new PremiumButton("เลือกคนนี้แหละ! 💞");
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
        card.add(imagePlaceholder);
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
