package com.jibgirl.Stamina;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaminaSystem extends JFrame {

    private int stamina = 100;
    private final int MAX_STAMINA = 100;

    private int day = 1;
    private final int MAX_DAY = 5;

    private JProgressBar staminaBar;
    private JLabel dayLabel;

    public StaminaSystem() {

        setTitle("Stamina + Day System");
        setSize(400, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== แสดงวัน =====
        dayLabel = new JLabel("Day : " + day + " / " + MAX_DAY, SwingConstants.CENTER);
        dayLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        dayLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // ===== หลอด Stamina =====
        staminaBar = new JProgressBar(0, MAX_STAMINA);
        staminaBar.setValue(stamina);
        staminaBar.setStringPainted(true);
        staminaBar.setForeground(Color.GREEN);
        staminaBar.setFont(new Font("Tahoma", Font.BOLD, 16));
        staminaBar.setBorder(new EmptyBorder(10, 20, 20, 20));

        // ===== ปุ่มใช้ Stamina =====
        JButton use20 = new JButton("ใช้ Stamina 20");
        use20.addActionListener(e -> useStamina(20));

        JButton use50 = new JButton("ใช้ Stamina 50");
        use50.addActionListener(e -> useStamina(50));

        // ===== ปุ่มจบวัน =====
        JButton nextDayButton = new JButton("จบวัน / ไปวันถัดไป");
        nextDayButton.addActionListener(e -> nextDay());

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 40, 20, 40));
        panel.add(use20);
        panel.add(use50);
        panel.add(nextDayButton);

        add(dayLabel, BorderLayout.NORTH);
        add(staminaBar, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void useStamina(int amount) {
        if (stamina - amount >= 0) {
            stamina -= amount;
            staminaBar.setValue(stamina);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Stamina ไม่พอ!",
                    "เตือน",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void nextDay() {

        if (day < MAX_DAY) {
            day++;

            // ✅ รี Stamina เฉพาะตอนจบวัน
            stamina = MAX_STAMINA;
            staminaBar.setValue(stamina);

            dayLabel.setText("Day : " + day + " / " + MAX_DAY);

        } else {
            JOptionPane.showMessageDialog(this,
                    "ครบ 5 วันแล้ว 💖\nจบเกม!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new StaminaSystem();
    }
}