package com.jibgirl.view;

import com.jibgirl.model.Player;
import com.jibgirl.model.Dialogue;
import com.jibgirl.model.Choice;
import com.jibgirl.controller.ChoiceManager;

import javax.swing.*;
import java.awt.*;

public class GameGui extends JFrame {

    private Player player;
    private Dialogue scene;
    private ChoiceManager manager;

    private JLabel moneyLabel;
    private JProgressBar affectionBar;
    private JTextArea dialogueArea;
    private JPanel buttonPanel;

    public GameGui() {

        // ======================
        // สร้าง Model
        // ======================
        player = new Player("Pokpong", 1000);
        scene = new Dialogue("วันนี้วันเกิดนางเอก! คุณจะซื้ออะไรให้เธอ?");
        manager = new ChoiceManager();

        scene.addChoice(new Choice(
                "ซื้อกระเป๋าแบรนด์เนม",
                50,
                2000,
                "กรี๊ดดด! รักที่สุดเลย! 💕"
        ));

        scene.addChoice(new Choice(
                "พาไปกินหมูกระทะ",
                10,
                500,
                "อร่อยจัง! 😋"
        ));

        scene.addChoice(new Choice(
                "เดินเล่นในสวน",
                -5,
                0,
                "บรรยากาศดีนะ... 😒"
        ));

        // ======================
        // ตั้งค่าหน้าต่าง
        // ======================
        setTitle("Jib Girl Game");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ======================
        // TOP PANEL
        // ======================
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        moneyLabel = new JLabel("💰 เงิน: " + player.getMoney() + " บาท");
        moneyLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        affectionBar = new JProgressBar(0, 100);
        affectionBar.setValue(player.getAffection());
        affectionBar.setStringPainted(true);

        topPanel.add(moneyLabel);
        topPanel.add(affectionBar);

        add(topPanel, BorderLayout.NORTH);

        // ======================
        // CENTER (Dialogue)
        // ======================
        dialogueArea = new JTextArea(scene.getQuestion());
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 20));
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(dialogueArea);
        add(scrollPane, BorderLayout.CENTER);

        // ======================
        // BOTTOM (Choices)
        // ======================
        buttonPanel = new JPanel(new GridLayout(0, 1));

        loadChoices();

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadChoices() {

        buttonPanel.removeAll();

        for (Choice c : scene.getChoices()) {

            JButton btn = new JButton(
                    c.getText() + " (ราคา " + c.getCost() + " บาท)"
            );

            btn.addActionListener(e -> {

                manager.selectChoice(player, c);

                // อัปเดต UI
                moneyLabel.setText("💰 เงิน: " + player.getMoney() + " บาท");
                affectionBar.setValue(player.getAffection());
                dialogueArea.setText(c.getReaction());
            });

            buttonPanel.add(btn);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }
}
