package com.jibgirl.model;

public class Choice {
    private String text; // ข้อความบนปุ่ม
    private int affectionImpact; // ผลต่อความรัก (+/-)
    private int cost; // ราคาที่ต้องจ่าย (ถ้ามี)
    private String reaction; // ข้อความตอบกลับเมื่อเลือก
    private int staminaCost; // ค่าใช้สมดุลชีวิต (stamina) ของการเลือกนี้

    /**
     * Constructor with stamina cost
     */
    public Choice(String text, int affectionImpact, int cost, String reaction, int staminaCost) {
        this.text = text;
        this.affectionImpact = affectionImpact;
        this.cost = cost;
        this.reaction = reaction;
        this.staminaCost = staminaCost;
    }

    /**
     * Constructor without stamina cost (backwards compatibility)
     * Default stamina cost = 5
     */
    public Choice(String text, int affectionImpact, int cost, String reaction) {
        this(text, affectionImpact, cost, reaction, 5);
    }

    // Getters
    public String getText() {
        return text;
    }

    public int getAffectionImpact() {
        return affectionImpact;
    }

    public int getCost() {
        return cost;
    }

    public String getReaction() {
        return reaction;
    }

    public int getStaminaCost() {
        return staminaCost;
    }

    // Setters
    public void setStaminaCost(int staminaCost) {
        this.staminaCost = staminaCost;
    }
}