package com.jibgirl.model;

import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    private String question;
    private List<Choice> choices; // ใช้ ArrayList เก็บตัวเลือกได้ไม่จำกัด

    public Dialogue(String question) {
        this.question = question;
        this.choices = new ArrayList<>();
    }

    public void addChoice(Choice c) {
        this.choices.add(c);
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public String getQuestion() {
        return question;
    }
}