package com.jibgirl.view;

import javax.swing.*;

import java.awt.*;

public class StartScreen extends JFrame {

    public StartScreen() {

        setTitle("Jib Girl Game");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // โหลดรูปพื้นหลังจาก src/com/jibgirl/asset/start.jpg
        java.net.URL imgURL =
                getClass().getResource("/com/jibgirl/asset/start.jpg");

        if (imgURL == null) {
            System.out.println("❌ หาไฟล์ start.jpg ไม่เจอ");
        }

        ImageIcon bgIcon = new ImageIcon(imgURL);
        Image bgImage = bgIcon.getImage()
                .getScaledInstance(900, 600, Image.SCALE_SMOOTH);

        JLabel background = new JLabel(new ImageIcon(bgImage));
        background.setLayout(new BorderLayout());

        // ===== ปุ่มเริ่ม =====
        JButton startButton = new JButton("เริ่มเกม");
        startButton.setFont(new Font("Tahoma", Font.BOLD, 28));
        startButton.setPreferredSize(new Dimension(220, 80));

        startButton.addActionListener(e -> {
            new GameGui();
            dispose();
        });

        // ===== Panel ด้านล่าง =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        // ขยับปุ่มขึ้นจากขอบล่าง 80px
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 80, 0)
        );

        bottomPanel.add(startButton);

        background.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(background);
        setVisible(true);
    }
}
