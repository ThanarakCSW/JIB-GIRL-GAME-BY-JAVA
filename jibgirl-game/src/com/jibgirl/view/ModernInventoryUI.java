package com.jibgirl.view;

import com.jibgirl.main.Inventrory.Inventory;
import com.jibgirl.model.Player;
import com.jibgirl.utils.UIUtils.*;
import com.jibgirl.utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ModernInventoryUI extends JFrame {

    private Inventory inventory;
    private Player player;
    private GameGui parentGui;
    private JPanel mainPanel;
    private static final Map<String, String> ITEM_ICONS = new HashMap<>();
    private static final Map<String, Integer> ITEM_PRICES = new HashMap<>();

    static {
        ITEM_ICONS.put("ชุดพู่กัน", "🖌️");
        ITEM_ICONS.put("สมุดสเก็ตช์", "📒");
        ITEM_ICONS.put("สีน้ำพรีเมียม", "🎨");
        ITEM_ICONS.put("โกโก้ร้อน", "☕");
        ITEM_ICONS.put("ชานมไข่มุก", "🧋");
        ITEM_ICONS.put("ดอกทานตะวัน", "🌻");
        ITEM_ICONS.put("ดอยทิวลิป", "🌷");
        ITEM_ICONS.put("รองเท้าบาส", "👟");
        ITEM_ICONS.put("กล้องโพลาฯ", "📸");

        ITEM_PRICES.put("ชุดพู่กัน", 150);
        ITEM_PRICES.put("สมุดสเก็ตช์", 80);
        ITEM_PRICES.put("สีน้ำพรีเมียม", 250);
        ITEM_PRICES.put("โกโก้ร้อน", 45);
        ITEM_PRICES.put("ชานมไข่มุก", 60);
        ITEM_PRICES.put("ดอกทานตะวัน", 120);
        ITEM_PRICES.put("ดอยทิวลิป", 150);
        ITEM_PRICES.put("รองเท้าบาส", 500);
        ITEM_PRICES.put("กล้องโพลาฯ", 800);
    }

    public ModernInventoryUI(Inventory inventory, Player player, GameGui parentGui) {
        this.inventory = inventory;
        this.player = player;
        this.parentGui = parentGui;

        setTitle("🎒 My Cute Inventory - กระเป๋าของฉัน");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(UIUtils.PASTEL_PURPLE);

        JLabel titleLabel = new JLabel("My Items ✨", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(new Color(100, 50, 150));
        titleLabel.setBorder(new EmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        mainPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(0, 40, 40, 40));

        refreshInventory();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        PremiumButton closeBtn = new PremiumButton("BACK 🏠");
        closeBtn.setCute(true);
        closeBtn.setPreferredSize(new Dimension(150, 50));
        closeBtn.addActionListener(e -> dispose());

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 30, 0));
        footer.add(closeBtn);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void refreshInventory() {
        mainPanel.removeAll();
        java.util.List<String> items = inventory.getItems();

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("Inventory is empty... 📦", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
            emptyLabel.setForeground(new Color(150, 150, 200));
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(emptyLabel);
        } else {
            mainPanel.setLayout(new GridLayout(0, 3, 20, 20));
            for (String itemName : items) {
                mainPanel.add(createItemCard(itemName));
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createItemCard(String name) {
        CuteCard card = new CuteCard(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 10, 15, 10));

        String icon = ITEM_ICONS.getOrDefault(name, "📦");
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 45));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        nameLabel.setForeground(new Color(100, 50, 100));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        PremiumButton useBtn = new PremiumButton("GIVE 💝");
        useBtn.setCute(true);
        useBtn.setPreferredSize(new Dimension(100, 35));
        useBtn.setMaximumSize(new Dimension(100, 35));
        useBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        useBtn.addActionListener(e -> {
            inventory.removeItem(name);

            // Calculate bonus based on price scaling
            int price = ITEM_PRICES.getOrDefault(name, 0);
            int bonus = 10; // Default for < 50
            if (price >= 500) {
                bonus = 30;
            } else if (price >= 100) { // Will cover 100-150 and up to 499
                bonus = 20;
            } else if (price >= 50) { // Covers 50-99
                bonus = 15;
            }

            player.addAffection(bonus);
            if (parentGui != null)
                parentGui.refreshStatus();
            JOptionPane.showMessageDialog(this, "คุณมอบ " + name + " ให้เธอ! ❤️\nความรักเพิ่มขึ้น +" + bonus, "Kawaii!",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshInventory();
        });

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(useBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }
}
