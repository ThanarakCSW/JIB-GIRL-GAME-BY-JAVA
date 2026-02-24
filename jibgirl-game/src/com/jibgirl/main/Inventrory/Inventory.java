package com.jibgirl.main.Inventrory;

import java.util.ArrayList;

public class Inventory {

    private ArrayList<String> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    // เพิ่มของเข้า Inventory
    public void addItem(String itemName) {
        items.add(itemName);
    }

    // แสดงรายการของทั้งหมด
    public String showItems() {

        if (items.isEmpty()) {
            return "📦 Inventory ว่างเปล่า";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("📦 ของใน Inventory:\n\n");

        for (int i = 0; i < items.size(); i++) {
            sb.append((i + 1)).append(". ")
              .append(items.get(i))
              .append("\n");
        }

        return sb.toString();
    }
}