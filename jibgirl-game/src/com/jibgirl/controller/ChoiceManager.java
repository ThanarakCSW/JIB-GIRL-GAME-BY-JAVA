package com.jibgirl.controller;

import com.jibgirl.model.Choice;
import com.jibgirl.model.Player;

public class ChoiceManager {

    /**
     * Select a choice and apply its effects to the player
     * Checks both money and stamina requirements
     * 
     * @param player         the player making the choice
     * @param choice         the selected choice
     * @param staminaManager the stamina manager for this player
     * @return true if choice was successfully executed, false otherwise
     */
    public boolean selectChoice(Player player, Choice choice, StaminaManager staminaManager) {
        System.out.println(">> ผู้เล่นเลือก: " + choice.getText());

        // 1. ตรวจสอบเงินก่อน
        if (!player.canSpendMoney(choice.getCost())) {
            System.out.println("❌ เงินไม่พอครับ! (ขาดอีก " + (choice.getCost() - player.getMoney()) + " บาท)");
            System.out.println("Result: คุณไม่ได้ทำอะไรเลย เพราะไม่มีตังค์...");
            System.out.println("------------------------------------------------");
            return false;
        }

        // 2. Strict stamina check
        if (!staminaManager.hasEnoughStamina(choice.getStaminaCost())) {
            System.out.println(
                    "❌ สมดุลชีวิตไม่พอครับ! (ขาดอีก " + (choice.getStaminaCost() - staminaManager.getStamina()) + "%)");
            return false;
        }

        // 3. ทั้งสองอย่างพอ -> หักเงินและสมดุลชีวิต
        player.spendMoney(choice.getCost());
        staminaManager.consumeStamina(choice.getStaminaCost());

        // 4. เพิ่มความรัก
        player.addAffection(choice.getAffectionImpact());

        // 5. แสดงผลลัพธ์
        System.out.println("Result: " + choice.getReaction());
        System.out.println("💰 เงิน: " + player.getMoney() + " | 💖 ความรัก: " +
                player.getAffection() + " | ⚡ สมดุล: " + staminaManager.getStamina());
        System.out.println("------------------------------------------------");
        return true;
    }

    /**
     * Backward compatible method for old code (assumes default stamina manager)
     */
    @Deprecated
    public void selectChoice(Player player, Choice choice) {
        StaminaManager tempManager = new StaminaManager();
        selectChoice(player, choice, tempManager);
    }
}