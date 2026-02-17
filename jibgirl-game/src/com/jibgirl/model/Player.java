package com.jibgirl.model;

public class Player {
    private String name;
    private int money;
    private int affection; // ค่าความรักนางเอก

    public Player(String name, int money) {
        this.name = name;
        this.money = money;
        this.affection = 0; // เริ่มต้นที่ 0
    }

    // Methods สำหรับปรับค่า (Encapsulation: ไม่ให้แก้ตัวแปรตรงๆ)
    public void addAffection(int amount) {
        this.affection += amount;
        System.out.println("DEBUG: ความรักเปลี่ยนไป " + amount + " (ปัจจุบัน: " + this.affection + ")");
    }

    public void decreaseMoney(int amount) {
        this.money -= amount;
        System.out.println("DEBUG: จ่ายเงินไป " + amount + " (เหลือ: " + this.money + ")");
    }

    public int getMoney() { return money; }
    public int getAffection() { return affection; }
}