package com.jibgirl.controller;

import com.jibgirl.model.Choice;
import com.jibgirl.model.Player;

public class ChoiceManager {

    public void selectChoice(Player player, Choice choice) {
        System.out.println(">> ผู้เล่นเลือก: " + choice.getText());

        // --- แก้ไขตรงนี้ครับ (ใช้ spendMoney แบบใหม่) ---
        
        // เราใช้ if เพื่อถาม Player ว่า "มีเงินจ่ายไหม?"
        // ถ้าจ่ายได้ (True) -> จะหักเงินให้เลย แล้วทำข้างในปีกกา
        if (player.spendMoney(choice.getCost())) {
            
            // 1. จ่ายเงินสำเร็จ -> เพิ่มความรัก
            player.addAffection(choice.getAffectionImpact());
            
            // 2. แสดงผลลัพธ์
            System.out.println("Result: " + choice.getReaction());
            
        } else {
            // จ่ายเงินไม่สำเร็จ (False) -> เงินไม่พอ
            System.out.println("❌ เงินไม่พอครับ! (ขาดอีก " + (choice.getCost() - player.getMoney()) + " บาท)");
            System.out.println("Result: คุณไม่ได้ทำอะไรเลย เพราะไม่มีตังค์...");
        }
        
        System.out.println("------------------------------------------------");
    }
}