package com.jibgirl.main.Shop;

import com.jibgirl.main.Inventrory.Inventory;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ShopUI extends JFrame {

    private Inventory inventory = new Inventory();

    public ShopUI() {

        setTitle("💖 My Dating Sim - ร้านค้า");
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(255, 240, 245));

        // ===== หมวดสินค้า =====
        JPanel categoryPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        categoryPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        categoryPanel.setBackground(new Color(255, 240, 245));

        categoryPanel.add(createCategoryButton("เครื่องเขียน"));
        categoryPanel.add(createCategoryButton("เครื่องดื่ม"));
        categoryPanel.add(createCategoryButton("ดอกไม้"));
        categoryPanel.add(createCategoryButton("อุปกรณ์กีฬา"));
        categoryPanel.add(createCategoryButton("อุปกรณ์ท่องเที่ยว"));

        add(categoryPanel, BorderLayout.CENTER);

        // ===== ปุ่มดู Inventory =====
        JButton viewInventory = new JButton("ดู Inventory 📦");
        viewInventory.setBackground(new Color(255, 105, 180));
        viewInventory.setForeground(Color.WHITE);
        viewInventory.setFocusPainted(false);
        viewInventory.setCursor(new Cursor(Cursor.HAND_CURSOR));

        viewInventory.addActionListener(e ->
                JOptionPane.showMessageDialog(this, inventory.showItems())
        );

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 240, 245));
        bottomPanel.add(viewInventory);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createCategoryButton(String title) {

        JButton button = new JButton(title);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setBackground(new Color(255, 182, 193));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> openItems(title));

        return button;
    }

    private void openItems(String category) {

        JDialog dialog = new JDialog(this, category, true);
        dialog.setSize(500, 550);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(255, 240, 245));

        JLabel titleLabel = new JLabel("หมวด : " + category, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setForeground(new Color(199, 21, 133));
        titleLabel.setBorder(new EmptyBorder(20, 10, 10, 10));

        dialog.add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 240, 245));
        panel.setBorder(new EmptyBorder(10, 30, 10, 30));

        if (category.equals("เครื่องเขียน")) {
            panel.add(createItem("ชุดพู่กันหัวละเอียด"));
            panel.add(createItem("สมุดสเก็ตช์ปกผ้า"));
            panel.add(createItem("สีน้ำเกรดศิลปิน"));
        }

        if (category.equals("เครื่องดื่ม")) {
            panel.add(createItem("โกโก้ร้อน"));
            panel.add(createItem("ชาเอิร์ลเกรย์"));
            panel.add(createItem("เกลือแร่เย็น ๆ"));
            panel.add(createItem("อเมริกาโน่เย็น"));
            panel.add(createItem("ชานมไข่มุก"));
            panel.add(createItem("น้ำผลไม้ปั่นสด"));
        }

        if (category.equals("ดอกไม้")) {
            panel.add(createItem("ดอกยิปโซ"));
            panel.add(createItem("ดอกทานตะวัน"));
            panel.add(createItem("ดอกทิวลิปสีชมพู"));
        }

        if (category.equals("อุปกรณ์กีฬา")) {
            panel.add(createItem("รองเท้าบาสรุ่นลิมิเต็ด"));
            panel.add(createItem("สนับเข่าแบบรัดกระชับ"));
            panel.add(createItem("ลูกบาสสำหรับแข่ง"));
        }

        if (category.equals("อุปกรณ์ท่องเที่ยว")) {
            panel.add(createItem("กล้องโพลารอยด์"));
            panel.add(createItem("สมุดบันทึกท่องเที่ยว"));
            panel.add(createItem("กระเป๋าผ้าแนวมินิมอล"));
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton close = new JButton("ปิดร้าน");
        close.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(255, 240, 245));
        bottom.add(close);

        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createItem(String name) {

        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setMaximumSize(new Dimension(400, 60));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 182, 193), 2));

        JLabel label = new JLabel("  " + name);
        label.setFont(new Font("Tahoma", Font.BOLD, 14));

        JButton buyButton = new JButton("ซื้อ");
        buyButton.setBackground(new Color(255, 182, 193));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);

        buyButton.addActionListener(e -> {
            inventory.addItem(name);
            JOptionPane.showMessageDialog(this,
                    name + " ถูกเพิ่มเข้า Inventory แล้ว 🛍️");
        });

        itemPanel.add(label, BorderLayout.WEST);
        itemPanel.add(buyButton, BorderLayout.EAST);

        return itemPanel;
    }

    public static void main(String[] args) {
        new ShopUI();
    }
}