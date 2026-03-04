package com.jibgirl.main.Shop;

import com.jibgirl.main.Inventrory.Inventory;
import com.jibgirl.model.Player;
import com.jibgirl.utils.UIUtils.*;
import com.jibgirl.utils.UIUtils;
import com.jibgirl.view.GameGui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ShopUI extends JFrame {

    private Inventory inventory;
    private Player player;
    private GameGui parentGui;

    public ShopUI(Player player, Inventory inventory, GameGui parentGui) {
        this.player = player;
        this.inventory = inventory;
        this.parentGui = parentGui;

        setTitle("🎀 Kawaii Shop - ร้านค้าน่ารัก 🎀");
        setSize(750, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Pastel Background
        getContentPane().setBackground(UIUtils.PASTEL_PINK);

        JLabel titleLabel = new JLabel("Welcome to My Cute Shop ✨", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
        titleLabel.setForeground(new Color(150, 50, 100));
        titleLabel.setBorder(new EmptyBorder(30, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Subtitle or Shop Balance
        JLabel balanceLabel = new JLabel("Your Balance: " + player.getMoney() + "฿", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(100, 50, 50));
        add(balanceLabel, BorderLayout.NORTH);

        // Re-adjusting layout to fit both title and balance
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(balanceLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Shop Content
        JPanel mainPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        // Adding Items
        mainPanel.add(createShopCard("ชุดพู่กัน", "🖌️", 150, balanceLabel));
        mainPanel.add(createShopCard("สมุดสเก็ตช์", "📒", 80, balanceLabel));
        mainPanel.add(createShopCard("สีน้ำพรีเมียม", "🎨", 250, balanceLabel));
        mainPanel.add(createShopCard("โกโก้ร้อน", "☕", 45, balanceLabel));
        mainPanel.add(createShopCard("ชานมไข่มุก", "🧋", 60, balanceLabel));
        mainPanel.add(createShopCard("ดอกทานตะวัน", "🌻", 120, balanceLabel));
        mainPanel.add(createShopCard("ดอยทิวลิป", "🌷", 150, balanceLabel));
        mainPanel.add(createShopCard("รองเท้าบาส", "👟", 500, balanceLabel));
        mainPanel.add(createShopCard("กล้องโพลาฯ", "📸", 500, balanceLabel));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(20, 0, 30, 0));

        PremiumButton closeBtn = new PremiumButton("ปิดร้านค้า ❌");
        closeBtn.setCute(true);
        closeBtn.setPreferredSize(new Dimension(150, 50));
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn);

        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createShopCard(String name, String icon, int price, JLabel balanceLabel) {
        CuteCard card = new CuteCard(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 10, 15, 10));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        nameLabel.setForeground(new Color(100, 50, 100));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Price: " + price + "฿", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        priceLabel.setForeground(new Color(255, 100, 150));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        PremiumButton buyBtn = new PremiumButton("BUY 🛒");
        buyBtn.setCute(true);
        buyBtn.setPreferredSize(new Dimension(100, 40));
        buyBtn.setMaximumSize(new Dimension(100, 40));
        buyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyBtn.addActionListener(e -> {
            if (player.getMoney() >= price) {
                player.spendMoney(price);
                inventory.addItem(name);
                balanceLabel.setText("Your Balance: " + player.getMoney() + "฿");
                if (parentGui != null)
                    parentGui.refreshStatus();
                JOptionPane.showMessageDialog(this, "ซื่อ " + name + " สำเร็จแล้วนะ! 🛍️✨", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "เงินไม่พอจ้า! ขาดอีก " + (price - player.getMoney()) + " บาท 🥺",
                        "Oops!", JOptionPane.WARNING_MESSAGE);
            }
        });

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(buyBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }
}