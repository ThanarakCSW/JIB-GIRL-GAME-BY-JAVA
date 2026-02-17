package com.jibgirl.controller;

import com.jibgirl.model.Choice;
import com.jibgirl.model.Player;

public class ChoiceManager {

    // Method นี้คือ "สมอง" ของระบบเลือกตอบ
    public void selectChoice(Player player, Choice choice) {
        System.out.println(">> ผู้เล่นเลือก: " + choice.getText());

        // 1. ตรวจสอบเงื่อนไข (Validation)
        if (player.getMoney() < choice.getCost()) {
            System.out.println("Msg: เงินไม่พอครับ! (ต้องการ " + choice.getCost() + " บาท)");
            return; // จบการทำงาน ไม่หักเงิน ไม่เพิ่มความรัก
        }

        // 2. คำนวณผลลัพธ์ (Execution)
        if (choice.getCost() > 0) {
            player.decreaseMoney(choice.getCost());
        }

        player.addAffection(choice.getAffectionImpact());

        // 3. แสดง Reaction (Feedback)
        System.out.println("Girl says: " + choice.getReaction());
        System.out.println("------------------------------------------------");
    }
}