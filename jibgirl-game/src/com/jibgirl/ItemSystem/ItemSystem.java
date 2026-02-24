package com.jibgirl.ItemSystem;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ItemSystem extends JFrame {

    // ===== สถานะเกม =====
    private int relationship = 50;
    private int money = 200;

    // ===== UI =====
    private JProgressBar relationshipBar;
    private JLabel moneyLabel;
    private JComboBox<String> characterSelector;
    private JTextArea inventoryArea;

    // ===== ระบบข้อมูล =====
    private Map<String, Integer> shopItems = new LinkedHashMap<>();
    private Map<String, Map<String, Integer>> characterPreference = new HashMap<>();
    private java.util.List<String> inventory = new ArrayList<>();

    public ItemSystem() {

        // ===== ตั้งค่า Frame =====
        setTitle("My Java Dating Sim");
        setSize(800, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // ===== ข้อมูลร้านค้า (ชื่อ → ราคา) =====
        shopItems.put("ดอกยิปโซ", 50);
        shopItems.put("ชานมไข่มุก", 30);
        shopItems.put("ชุดพู่กันหัวละเอียด", 70);
        shopItems.put("รองเท้าบาสรุ่นลิมิเต็ด", 100);

        // ===== ความชอบตัวละคร =====
        // ชื่อไอเทม → คะแนนที่ได้

        Map<String, Integer> alice = new HashMap<>();
        alice.put("ดอกยิปโซ", 30);
        alice.put("ชานมไข่มุก", 10);
        alice.put("ชุดพู่กันหัวละเอียด", 20);

        Map<String, Integer> bella = new HashMap<>();
        bella.put("รองเท้าบาสรุ่นลิมิเต็ด", 30);
        bella.put("ชานมไข่มุก", 5);
        bella.put("ดอกยิปโซ", 15);

        characterPreference.put("Alice", alice);
        characterPreference.put("Bella", bella);

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new GridLayout(3,1));

        relationshipBar = new JProgressBar(0,100);
        relationshipBar.setValue(relationship);
        relationshipBar.setStringPainted(true);
        relationshipBar.setString("Relationship: " + relationship);

        moneyLabel = new JLabel("Money: " + money, SwingConstants.CENTER);

        characterSelector = new JComboBox<>(new String[]{"Alice", "Bella"});

        topPanel.add(relationshipBar);
        topPanel.add(moneyLabel);
        topPanel.add(characterSelector);

        add(topPanel, BorderLayout.NORTH);

        // ===== Center Panel (ร้านค้า) =====
        JPanel shopPanel = new JPanel(new GridLayout(0,2,10,10));

        for (String item : shopItems.keySet()) {

            JButton buyButton = new JButton("ซื้อ: " + item + " (" + shopItems.get(item) + "฿)");

            buyButton.addActionListener(e -> buyItem(item));

            shopPanel.add(buyButton);
        }

        add(new JScrollPane(shopPanel), BorderLayout.CENTER);

        // ===== Right Panel (Inventory) =====
        JPanel rightPanel = new JPanel(new BorderLayout());

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);

        JButton useButton = new JButton("ใช้ไอเทมที่เลือก");
        useButton.addActionListener(e -> useSelectedItem());

        rightPanel.add(new JLabel("Inventory"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(inventoryArea), BorderLayout.CENTER);
        rightPanel.add(useButton, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        setVisible(true);
    }

    // ===== ซื้อไอเทม =====
    private void buyItem(String item) {

        int price = shopItems.get(item);

        if (money >= price) {
            money -= price;
            inventory.add(item);
            updateInventory();
            moneyLabel.setText("Money: " + money);

            JOptionPane.showMessageDialog(this, "ซื้อ " + item + " สำเร็จ");
        } else {
            JOptionPane.showMessageDialog(this, "เงินไม่พอ!");
        }
    }

    // ===== ใช้ไอเทม =====
    private void useSelectedItem() {

        String selected = inventoryArea.getSelectedText();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกไอเทมจาก Inventory");
            return;
        }

        String character = (String) characterSelector.getSelectedItem();
        Map<String, Integer> preference = characterPreference.get(character);

        int bonus = preference.getOrDefault(selected.trim(), 5);

        relationship += bonus;
        if (relationship > 100) relationship = 100;

        relationshipBar.setValue(relationship);
        relationshipBar.setString("Relationship: " + relationship);

        inventory.remove(selected.trim());
        updateInventory();

        JOptionPane.showMessageDialog(this,
                "ให้ " + character + "\nRelationship +" + bonus);
    }

    private void updateInventory() {

        StringBuilder sb = new StringBuilder();
        for (String item : inventory) {
            sb.append(item).append("\n");
        }
        inventoryArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ItemSystem::new);
    }
}