package com.jibgirl.main;

import com.jibgirl.model.*;
import com.jibgirl.controller.ChoiceManager;
import java.util.Scanner; // ตัวช่วยรับค่าจากคีย์บอร์ด

public class Main {
    public static void main(String[] args) {
        // --- 1. SETUP ข้อมูล (เหมือนเดิม) ---
        Scanner scanner = new Scanner(System.in);
        Player hero = new Player("Pokpong", 1000);
        ChoiceManager manager = new ChoiceManager();

        // สร้างคำถามจำลอง
        Dialogue scene = new Dialogue("นางเอกมองหน้าคุณแล้วถามว่า 'เย็นนี้ว่างไหม?'");
        scene.addChoice(new Choice("ว่างสิ ไปกินข้าวกัน", 10, 500, "เย้! งั้นเจอกันร้านเดิมนะ"));
        scene.addChoice(new Choice("ไม่ว่าง ต้องปั่นโค้ด", -5, 0, "อ๋อ... งั้นไม่กวนนะ (เธอทำหน้าเศร้า)"));

        // --- 2. GAME LOOP (จำลอง GUI) ---
        while (true) {
            System.out.println("\n=================================");
            System.out.println("สถานะ: เงิน " + hero.getMoney() + " | ความรัก " + hero.getAffection());
            System.out.println("เหตุการณ์: " + scene.getQuestion());
            System.out.println("เลือกคำตอบ (กดเลข):");

            // วนลูปแสดง Choice ออกมา
            for (int i = 0; i < scene.getChoices().size(); i++) {
                System.out.println("[" + (i + 1) + "] " + scene.getChoices().get(i).getText());
            }
            System.out.println("[0] ออกจากเกม");

            // --- 3. INPUT (แทนการกดปุ่ม) ---
            System.out.print(">> ");
            int input = scanner.nextInt();

            if (input == 0)
                break; // ออกจากลูป

            // ตรวจสอบว่ากดเลขถูกไหม
            if (input > 0 && input <= scene.getChoices().size()) {
                // ดึง Choice ที่เลือกออกมา
                Choice selected = scene.getChoices().get(input - 1);

                // *** ตรงนี้คือหัวใจสำคัญ ***
                // เราเรียกใช้ Logic เดิมที่เขียนไว้ ไม่ว่าจะเป็น Console หรือ GUI
                // ก็เรียกบรรทัดนี้เหมือนกัน!
                manager.selectChoice(hero, selected);

            } else {
                System.out.println("เลือกผิดข้อครับ!");
            }
        }
        scanner.close();
    }
}