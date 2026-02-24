package com.jibgirl.controller;

import com.jibgirl.model.*;
import java.util.Scanner;

public class MaprangRoute {
    private Player player;
    private ChoiceManager manager;
    private Scanner scanner;

    public MaprangRoute(Player player) {
        this.player = player;
        this.manager = new ChoiceManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== เริ่มต้นเส้นทาง: มะปราง ===");
        day1();
        day2();
        day3();
        day4();
        day5();
        showEnding();
    }

    private void day1() {
        System.out.println("\n--- DAY 1: ห้องศิลปะ ---");
        System.out.println("เซนต์: \"อันนี้สวยจัง\"");
        Dialogue d1 = new Dialogue("มะปราง: \"หมายถึงแมว…หรือฉันคะ?\"");
        d1.addChoice(new Choice("คุณนั่นแหละครับ", 10, 0, "มะปรางหน้าแดง \"เขินนะคะ…\""));
        d1.addChoice(new Choice("หมายถึงแมวครับ", 3, 0, "มะปราง: \"อ๋อ แมวตัวนี้เองเหรอคะ\""));
        d1.addChoice(new Choice("พูดเล่นครับ", -5, 0, "บรรยากาศจืดสนิท"));

        int choice1 = displayAndGetChoice(d1);
        if (choice1 == 1) { // A
            Dialogue d2a = new Dialogue("🔀 Choice 2A");
            d2a.addChoice(new Choice("พูดเฉพาะกับคนพิเศษ", 10, 0, "มะปราง: \"คุณพูดแบบนี้แล้วฉันทำตัวไม่ถูกเลยค่ะ…\" (เธอเริ่มเปิดใจ)"));
            d2a.addChoice(new Choice("ชมตามมารยาท", 0, 0, "\"อ๋อ แบบสุภาพสินะคะ\" (บรรยากาศกลาง ๆ)"));
            d2a.addChoice(new Choice("ก็ชมทุกคน", -5, 0, "เธอหลบตา (จบวันเร็ว)"));
            displayAndGetChoice(d2a);
        } else if (choice1 == 2) { // B
            Dialogue d2b = new Dialogue("🔀 Choice 2B");
            d2b.addChoice(new Choice("มันเหมือนเจ้าของเลย", 10, 0, "\"ฉันเหรอคะ?\" (ยิ้ม)"));
            d2b.addChoice(new Choice("มันดูสบายดี", 3, 0, "คุยทั่วไป"));
            d2b.addChoice(new Choice("ก็ธรรมดานะ", -5, 0, "เธอเงียบ"));
            displayAndGetChoice(d2b);
        } else if (choice1 == 3) { // C
            Dialogue d2c = new Dialogue("🔀 Choice 2C");
            d2c.addChoice(new Choice("ขอโทษนะ", 3, 0, "เธอยิ้มบาง ๆ พอคุยต่อได้"));
            d2c.addChoice(new Choice("ก็จริงนี่", -10, 0, "เธอเก็บของ (จบวันทันที)"));
            d2c.addChoice(new Choice("เงียบ", -5, 0, "จบบทเร็ว"));
            displayAndGetChoice(d2c);
        }
    }

    private void day2() {
        System.out.println("\n--- DAY 2: ลานชมวิว ---");
        Dialogue d1 = new Dialogue("มะปราง: \"ฉันชอบแสงแบบนี้ค่ะ\"");
        d1.addChoice(new Choice("ทำไมถึงชอบมัน", 10, 0, "เธอเล่าเรื่องตอนเด็ก"));
        d1.addChoice(new Choice("สวยดีนะ", 3, 0, "คุยทั่วไปเรื่องสีท้องฟ้า"));
        d1.addChoice(new Choice("ก็เหมือนทุกวัน", -5, 0, "เธอเงียบ"));

        int choice1 = displayAndGetChoice(d1);
        if (choice1 == 1) { // A
            Dialogue d2a = new Dialogue("🔀 Choice 2A");
            d2a.addChoice(new Choice("เธอเก่งนะที่รักษาความรู้สึกแบบนี้ไว้ได้", 10, 0, "เธอยิ้มจริงใจ (ใกล้ชิดขึ้น)"));
            d2a.addChoice(new Choice("ก็น่ารักดี", 3, 0, "ยิ้มตอบ"));
            d2a.addChoice(new Choice("คิดมากไปไหม", -10, 0, "เธอเงียบ (วันถัดไปบทสั้นลง)"));
            displayAndGetChoice(d2a);
        } else if (choice1 == 2) { // B
            Dialogue d2b = new Dialogue("Choice 2B");
            d2b.addChoice(new Choice("ชวนมาดูด้วยกันอีก", 5, 0, "เธอยิ้มรับ"));
            d2b.addChoice(new Choice("ฟังเฉย ๆ", 0, 0, "คุยกันเงียบๆ"));
            d2b.addChoice(new Choice("บอกเบื่อ", -5, 0, "บรรยากาศอึดอัด"));
            displayAndGetChoice(d2b);
        } else if (choice1 == 3) { // C
            Dialogue d2c = new Dialogue("Choice 2C");
            d2c.addChoice(new Choice("ขอโทษ", 3, 0, "เธอยิ้มบางๆ"));
            d2c.addChoice(new Choice("ไม่สนใจ", -5, 0, "เธอก้มหน้า"));
            d2c.addChoice(new Choice("เดินกลับ", -10, 0, "แยกย้ายทันที"));
            displayAndGetChoice(d2c);
        }
    }

    private void day3() {
        System.out.println("\n--- DAY 3: เตรียมนิทรรศการ ---");
        Dialogue d1 = new Dialogue("มะปราง: \"ช่วยเลือกโทนสีหน่อยได้ไหมคะ\"");
        d1.addChoice(new Choice("วิเคราะห์จริงจัง", 10, 0, "เธอประทับใจ"));
        d1.addChoice(new Choice("บอกว่าอะไรก็ได้", 0, 0, "เดี๋ยวนะ..."));
        d1.addChoice(new Choice("ไม่สนใจ", -10, 0, "เธอถอนหายใจ"));

        int choice1 = displayAndGetChoice(d1);
        if (choice1 == 1) { // A
            Dialogue d2a = new Dialogue("Choice 2A");
            d2a.addChoice(new Choice("อยู่ช่วยจนเสร็จ", 10, 0, "ทำงานด้วยกันอย่างมีความสุข"));
            d2a.addChoice(new Choice("ช่วยครึ่งเดียว", 3, 0, "ขอบคุณนะคะที่ช่วย"));
            d2a.addChoice(new Choice("บ่นว่าเหนื่อย", -5, 0, "ขอโทษที่รบกวนค่ะ"));
            displayAndGetChoice(d2a);
        } else if (choice1 == 2) { // B
            Dialogue d2b = new Dialogue("Choice 2B");
            d2b.addChoice(new Choice("พยายามฟังเธอ", 5, 0, "ดีขึ้นนิดหน่อย"));
            d2b.addChoice(new Choice("เล่นมือถือ", -5, 0, "เธอเงียบไป"));
            d2b.addChoice(new Choice("เปลี่ยนเรื่อง", -3, 0, "อืม... ค่ะ"));
            displayAndGetChoice(d2b);
        } else if (choice1 == 3) { // C
            Dialogue d2c = new Dialogue("Choice 2C");
            d2c.addChoice(new Choice("ขอโทษ", 3, 0, "ไม่เป็นไรค่ะ"));
            d2c.addChoice(new Choice("ยืนยันไม่ช่วย", -10, 0, "งั้นฉันทำเองค่ะ"));
            d2c.addChoice(new Choice("เดินออก", -10, 0, "เธอมองตามด้วยความผิดหวัง"));
            displayAndGetChoice(d2c);
        }
    }

    private void day4() {
        System.out.println("\n--- DAY 4: คาเฟ่ ---");
        Dialogue d1 = new Dialogue("มะปราง: \"ถ้าวันหนึ่งฉันวาดไม่เก่งแล้วล่ะ\"");
        d1.addChoice(new Choice("ฉันชอบเธอ ไม่ใช่แค่รูป", 10, 0, "เธอเงียบไป (หน้าแดง)"));
        d1.addChoice(new Choice("ก็ฝึกใหม่สิ", 0, 0, "ก็จริงค่ะ..."));
        d1.addChoice(new Choice("ก็เลิกวาด", -10, 0, "ใจร้ายจัง"));

        int choice1 = displayAndGetChoice(d1);
        if (choice1 == 1) { // A
            Dialogue d2a = new Dialogue("Choice 2A");
            d2a.addChoice(new Choice("จับมือเธอเบา ๆ", 10, 0, "ความรู้สึกส่งสารถึงกัน"));
            d2a.addChoice(new Choice("ยิ้มให้", 3, 0, "ยิ้มตอบอย่างขวยเขิน"));
            d2a.addChoice(new Choice("หัวเราะกลบ", -5, 0, "ทำเป็นตลกไปได้"));
            displayAndGetChoice(d2a);
        } else if (choice1 == 2) { // B
            Dialogue d2b = new Dialogue("Choice 2B");
            d2b.addChoice(new Choice("อธิบายเพิ่ม", 5, 0, "เข้าใจแล้วค่ะ"));
            d2b.addChoice(new Choice("เปลี่ยนเรื่อง", -3, 0, "ค่ะ..."));
            d2b.addChoice(new Choice("ล้อเล่น", -5, 0, "ไม่ขำเลยนะคะ"));
            displayAndGetChoice(d2b);
        } else if (choice1 == 3) { // C
            Dialogue d2c = new Dialogue("Choice 2C");
            d2c.addChoice(new Choice("ขอโทษ", 3, 0, "อืม... ค่ะ"));
            d2c.addChoice(new Choice("ยืนยันคำเดิม", -10, 0, "เย็นชาจัง"));
            d2c.addChoice(new Choice("ลุกกลับ", -10, 0, "วันนี้พอก่อนเถอะค่ะ"));
            displayAndGetChoice(d2c);
        }
    }

    private void day5() {
        System.out.println("\n--- DAY 5: สารภาพรัก ---");
        System.out.println("เซนต์: \"อยู่กับเธอแล้วผมรู้สึกสงบ ผมชอบเธอ\"");
        Dialogue d1 = new Dialogue("Choice สุดท้าย");
        d1.addChoice(new Choice("พูดจริงใจลึก", 10, 0, "เธอมองตาคุณ"));
        d1.addChoice(new Choice("พูดเขิน ๆ", 3, 0, "เธอยิ้มที่เฉลยความในใจ"));
        d1.addChoice(new Choice("พูดติดตลก", -5, 0, "จริงจังหน่อยสิคะ..."));
        displayAndGetChoice(d1);
    }

    private void showEnding() {
        System.out.println("\n=== 🎬 ENDING มะปราง ===");
        int score = player.getAffection();
        System.out.println("คะแนนความรักสุทธิ: " + score);

        if (score >= 40) {
            System.out.println("มะปรางจับมือ");
            System.out.println("“งั้นอยู่ดูพระอาทิตย์ตกกับฉันไปเรื่อย ๆ นะคะ” (จบแบบ Love)");
        } else if (score > -20) {
            System.out.println("“คุณคือเพื่อนที่อบอุ่นที่สุดของฉัน” (จบแบบ Friend)");
        } else {
            System.out.println("“เราคงเข้ากันไม่ได้ค่ะ” (Game Over)");
        }
    }

    private int displayAndGetChoice(Dialogue d) {
        System.out.println(d.getQuestion());
        for (int i = 0; i < d.getChoices().size(); i++) {
            Choice c = d.getChoices().get(i);
            System.out.println((i + 1) + ". " + c.getText() + (c.getCost() > 0 ? " (ราคา: " + c.getCost() + ")" : ""));
        }

        int userChoice = 0;
        while (userChoice < 1 || userChoice > d.getChoices().size()) {
            System.out.print("เลือกข้อ: ");
            if (scanner.hasNextInt()) {
                userChoice = scanner.nextInt();
                if (userChoice < 1 || userChoice > d.getChoices().size()) {
                    System.out.println("กรุณาเลือก 1-" + d.getChoices().size());
                }
            } else {
                System.out.println("กรุณาใส่ตัวเลข");
                scanner.next();
            }
        }
        
        Choice selected = d.getChoices().get(userChoice - 1);
        manager.selectChoice(player, selected);
        return userChoice;
    }

    public static void main(String[] args) {
        Player testPlayer = new Player("เซนต์", 1000);
        MaprangRoute game = new MaprangRoute(testPlayer);
        game.start();
    }
}
