package com.jibgirl.model;

public class Choice {
    private String text; // ข้อความบนปุ่ม
    private int affectionImpact; // ผลต่อความรัก (+/-)
    private int cost; // ราคาที่ต้องจ่าย (ถ้ามี)
    private String reaction; // ข้อความตอบกลับเมื่อเลือก

    public Choice(String text, int affectionImpact, int cost, String reaction) {
        this.text = text;
        this.affectionImpact = affectionImpact;
        this.cost = cost;
        this.reaction = reaction;
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
}