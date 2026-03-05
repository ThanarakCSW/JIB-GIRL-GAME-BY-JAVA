package com.jibgirl.main;

import com.jibgirl.view.StartScreen;

public class Main {

    public static void main(String[] args) {

        // Set global fonts for popups and default UI
        com.jibgirl.utils.UIUtils.applyGlobalFont();

        // เปิดหน้าเริ่มเกม
        new StartScreen();

    }
}