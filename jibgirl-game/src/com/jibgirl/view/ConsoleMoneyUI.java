package com.jibgirl.view;

import com.jibgirl.model.Player;

public class ConsoleMoneyUI {

    // จำลองการอัปเดตหลอดเงินบนหน้าจอ
    public void updateMoneyDisplay(Player p) {
        System.out.println("\n=== 💳 K-Bank Account ===");
        System.out.println("Owner: " + p.getName()); // ต้องเพิ่ม method getName() ใน Player ด้วยนะครับ
        System.out.println("Balance: " + p.getMoney() + " THB");
        System.out.println("========================\n");
    }
}