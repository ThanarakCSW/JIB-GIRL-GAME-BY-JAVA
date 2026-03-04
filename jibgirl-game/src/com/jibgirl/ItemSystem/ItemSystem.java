package com.jibgirl.ItemSystem;

import com.jibgirl.Relationship.Relationship;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ItemSystem extends JFrame {

    private Relationship relationshipSystem = new Relationship(50);
    private int money = 200;

    private JProgressBar relationshipBar;
    private JLabel moneyLabel;
    private JComboBox<String> characterSelector;
    private JTextArea inventoryArea;

    private Map<String, Integer> shopItems = new LinkedHashMap<>();
    private Map<String, Map<String, Integer>> characterPreference = new HashMap<>();
    private java.util.List<String> inventory = new ArrayList<>();

    public ItemSystem() {

        setTitle("My Java Dating Sim");
        setSize(800, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // ===== ข้อมูลร้านค้า =====
        shopItems.put("ดอกยิปโซ", 50);
        shopItems.put("ชานมไข่มุก", 30);
        shopItems.put("ชุดพู่กันหัวละเอียด", 70);
        shopItems.put("รองเท้าบาสรุ่นลิมิเต็ด", 100);
        shopItems.put("สมุดสเก็ตช์ปกผ้า", 60);
        shopItems.put("สีน้ำเกรดศิลปิน", 80);
        shopItems.put("โกโก้ร้อน", 40);
        shopItems.put("ชาเอิร์ลเกรย์", 40);
        shopItems.put("สนับเข่าแบบรัดกระชับ", 50);
        shopItems.put("ลูกบาสหนังแท้สำหรับแข่ง", 90);
        shopItems.put("เกลือแร่เย็น ๆ", 25);
        shopItems.put("อเมริกาโน่เย็น", 35);
        shopItems.put("ดอกทานตะวัน", 45);
        shopItems.put("กล้องโพลารอยด์", 120);
        shopItems.put("สมุดบันทึกท่องเที่ยว", 70);
        shopItems.put("กระเป๋าสะพายผ้าแนวมินิมอล", 65);
        shopItems.put("น้ำผลไม้ปั่นสด", 30);
        shopItems.put("ดอกทิวลิปสีชมพู", 55);

        // ================= มะปราง =================
        Map<String, Integer> maprang = new HashMap<>();
        maprang.put("ชุดพู่กันหัวละเอียด", 15);
        maprang.put("สมุดสเก็ตช์ปกผ้า", 10);
        maprang.put("สีน้ำเกรดศิลปิน", 15);
        maprang.put("โกโก้ร้อน", 10);
        maprang.put("ชาเอิร์ลเกรย์", 10);
        maprang.put("ดอกยิปโซ", 20);

        // ================= ไอซ์ =================
        Map<String, Integer> ice = new HashMap<>();
        ice.put("รองเท้าบาสรุ่นลิมิเต็ด", 20);
        ice.put("สนับเข่าแบบรัดกระชับ", 10);
        ice.put("ลูกบาสหนังแท้สำหรับแข่ง", 15);
        ice.put("เกลือแร่เย็น ๆ", 10);
        ice.put("อเมริกาโน่เย็น", 5);
        ice.put("ดอกทานตะวัน", 15);

        // ================= ขนม =================
        Map<String, Integer> khanom = new HashMap<>();
        khanom.put("กล้องโพลารอยด์", 20);
        khanom.put("สมุดบันทึกท่องเที่ยว", 15);
        khanom.put("กระเป๋าสะพายผ้าแนวมินิมอล", 10);
        khanom.put("ชานมไข่มุก", 10);
        khanom.put("น้ำผลไม้ปั่นสด", 10);
        khanom.put("ดอกทิวลิปสีชมพู", 15);

        characterPreference.put("มะปราง", maprang);
        characterPreference.put("ไอซ์", ice);
        characterPreference.put("ขนม", khanom);

        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        relationshipBar = new JProgressBar(0, 100);
        relationshipBar.setValue(relationshipSystem.getValue());
        relationshipBar.setStringPainted(true);
        relationshipBar.setString("Relationship: " + relationshipSystem.getValue());

        moneyLabel = new JLabel("Money: " + money, SwingConstants.CENTER);

        characterSelector = new JComboBox<>(new String[] { "มะปราง", "ไอซ์", "ขนม" });

        topPanel.add(relationshipBar);
        topPanel.add(moneyLabel);
        topPanel.add(characterSelector);

        add(topPanel, BorderLayout.NORTH);

        JPanel shopPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        for (String item : shopItems.keySet()) {
            JButton buyButton = new JButton("ซื้อ: " + item + " (" + shopItems.get(item) + "฿)");
            buyButton.addActionListener(e -> buyItem(item));
            shopPanel.add(buyButton);
        }

        add(new JScrollPane(shopPanel), BorderLayout.CENTER);

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

    private void buyItem(String item) {
        int price = shopItems.get(item);
        if (money >= price) {
            money -= price;
            inventory.add(item);
            updateInventory();
            moneyLabel.setText("Money: " + money);
        } else {
            JOptionPane.showMessageDialog(this, "เงินไม่พอ!");
        }
    }

    private void useSelectedItem() {

        String selected = inventoryArea.getSelectedText();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกไอเทม");
            return;
        }

        String itemName = selected.trim();
        String character = (String) characterSelector.getSelectedItem();
        Map<String, Integer> preference = characterPreference.get(character);

        // ✅ ใช้คะแนนตายตัวตามตัวละคร
        int bonus = preference.getOrDefault(itemName, 0);

        relationshipSystem.update(bonus);

        relationshipBar.setValue(relationshipSystem.getValue());
        relationshipBar.setString("Relationship: " + relationshipSystem.getValue());

        inventory.remove(itemName);
        updateInventory();

        JOptionPane.showMessageDialog(this,
                "ให้ " + character +
                        "\nRelationship +" + bonus +
                        "\nปัจจุบัน: " + relationshipSystem.getValue());
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