package com.jibgirl.main;

import com.jibgirl.model.*;
import com.jibgirl.controller.ChoiceManager;
import com.jibgirl.view.ConsoleMoneyUI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // --- 1. SETUP ---
        Scanner scanner = new Scanner(System.in);
        Player player = new Player("Pokpong", 1000);
        ChoiceManager manager = new ChoiceManager();
        ConsoleMoneyUI ui = new ConsoleMoneyUI();

        // --- 2. CONTENT ---
        Dialogue scene = new Dialogue("วันนี้วันเกิดนางเอก! คุณจะซื้ออะไรให้เธอ?");

        scene.addChoice(new Choice("ซื้อกระเป๋าแบรนด์เนม", 50, 2000,
                "กรี๊ดดด! รักที่สุดเลย! 💕 (แต่ตังค์คุณหมดนะ)"));

        scene.addChoice(new Choice("พาไปกินหมูกระทะ", 10, 500,
                "อร่อยจัง! ขอบคุณนะที่พามาเลี้ยง 😋"));

        scene.addChoice(new Choice("เดินเล่นในสวนสาธารณะ", -5, 0,
                "บรรยากาศดีนะ... แต่หิวข้าวอะ 😒"));

        // --- 3. GAME LOOP ---
        boolean isRunning = true;

        while (isRunning) {

            ui.updateMoneyDisplay(player);

            System.out.println("❤️ ระดับความรัก");
            displayRelationshipBar(player.getAffection());

            System.out.println("------------------------------------------------");
            System.out.println("สถานการณ์: " + scene.getQuestion());

            int i = 1;
            for (Choice c : scene.getChoices()) {
                System.out.println("[" + i + "] " + c.getText()
                        + " (ราคา: " + c.getCost() + " บาท)");
                i++;
            }

            System.out.println("[0] ออกจากเกม");
            System.out.print(">> เลือกข้อไหนดีครับ? : ");

            int input = scanner.nextInt();

            if (input == 0) {
                isRunning = false;
            } else if (input > 0 && input <= scene.getChoices().size()) {

                Choice selectedChoice =
                        scene.getChoices().get(input - 1);

                manager.selectChoice(player, selectedChoice);

            } else {
                System.out.println("❌ กดผิดครับ! เลือกใหม่นะ");
            }

            System.out.println("\nกด Enter เพื่อไปต่อ...");
            try {
                System.in.read();
            } catch (Exception e) {
            }
        }

        System.out.println("จบเกมครับ! เจอกันใหม่ Sprint หน้า");
        scanner.close();
    }


    // ===============================
    // 🔥 หลอด Relationship (0-100)
    // ===============================
    public static void displayRelationshipBar(int affection) {

        int totalBars = 20;
        int filledBars = (affection * totalBars) / 100;

        System.out.print("Relationship: [");

        for (int i = 0; i < totalBars; i++) {
            if (i < filledBars) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }

        System.out.println("] " + affection + "/100");
    }
}
