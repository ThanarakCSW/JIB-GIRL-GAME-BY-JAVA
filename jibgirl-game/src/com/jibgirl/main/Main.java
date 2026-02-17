package com.jibgirl.main;

import com.jibgirl.model.*;
import com.jibgirl.controller.ChoiceManager;
import com.jibgirl.view.ConsoleMoneyUI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // --- 1. SETUP: เตรียมตัวละครและระบบ ---
        Scanner scanner = new Scanner(System.in);
        Player player = new Player("Pokpong", 1000); // เริ่มต้นมีเงิน 1,000 บาท
        ChoiceManager manager = new ChoiceManager();
        ConsoleMoneyUI ui = new ConsoleMoneyUI();

        // --- 2. CONTENT: สร้างสถานการณ์จำลอง ---
        Dialogue scene = new Dialogue("วันนี้วันเกิดนางเอก! คุณจะซื้ออะไรให้เธอ?");

        // ทางเลือกที่ 1: ของแพง (ได้ใจเยอะ)
        scene.addChoice(new Choice("ซื้อกระเป๋าแบรนด์เนม", 50, 2000, "กรี๊ดดด! รักที่ซู๊ดดด (แต่ตังค์คุณหมดนะ)"));

        // ทางเลือกที่ 2: ของราคาพอดีๆ
        scene.addChoice(new Choice("พาไปกินหมูกระทะ", 10, 500, "อร่อยจัง! ขอบคุณนะที่พามาเลี้ยง"));

        // ทางเลือกที่ 3: ไม่เสียตังค์ (แต่เสียใจ)
        scene.addChoice(new Choice("เดินเล่นในสวนสาธารณะ", -5, 0, "บรรยากาศดีนะ... แต่หิวข้าวอะ (เธอมองแรง)"));

        // --- 3. GAME LOOP: วนลูปเล่นไปเรื่อยๆ ---
        boolean isRunning = true;
        while (isRunning) {
            // แสดงสถานะการเงินปัจจุบัน
            ui.updateMoneyDisplay(player);
            System.out.println("❤️ ค่าความรักปัจจุบัน: " + player.getAffection());
            System.out.println("------------------------------------------------");

            // แสดงคำถาม
            System.out.println("สถานการณ์: " + scene.getQuestion());

            // แสดงตัวเลือก
            int i = 1;
            for (Choice c : scene.getChoices()) {
                System.out.println("[" + i + "] " + c.getText() + " (ราคา: " + c.getCost() + " บาท)");
                i++;
            }
            System.out.println("[0] ออกจากเกม");
            System.out.print(">> เลือกข้อไหนดีครับ? : ");

            // รับค่าจากคีย์บอร์ด
            int input = scanner.nextInt();

            if (input == 0) {
                isRunning = false; // จบเกม
            } else if (input > 0 && input <= scene.getChoices().size()) {

                // ดึงตัวเลือกที่ user กด
                Choice selectedChoice = scene.getChoices().get(input - 1);

                // *** จุดสำคัญที่สุด! *** // ส่งให้ ChoiceManager จัดการหักเงินและเพิ่มความรัก
                manager.selectChoice(player, selectedChoice);

                // (Optional) แอบเติมเงินให้ตัวเองหน่อย เผื่อเงินหมดแล้วอยากเทสต่อ
                // player.earnMoney(2000);

            } else {
                System.out.println("❌ กดผิดครับ! เลือกใหม่นะ");
            }

            System.out.println("\nกด Enter เพื่อไปต่อ...");
            try {
                System.in.read();
            } catch (Exception e) {
            } // หยุดรอให้กด Enter
        }

        System.out.println("จบเกมครับ! เจอกันใหม่ Sprint หน้า");
        scanner.close();
    }
}