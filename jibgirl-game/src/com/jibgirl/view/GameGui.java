package com.jibgirl.view;

import com.jibgirl.model.Player;
import com.jibgirl.model.Dialogue;
import com.jibgirl.model.Choice;
import com.jibgirl.controller.ChoiceManager;
import com.jibgirl.main.Shop.ShopUI;
import com.jibgirl.main.Inventrory.Inventory;
import com.jibgirl.utils.UIUtils.*;
import com.jibgirl.utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameGui extends JFrame {

    private Player player;
    private Dialogue currentScene;
    private ChoiceManager manager;
    private Inventory inventory;
    private String characterKey;
    private int day = 1;
    private String gameState = "START";

    private JLabel moneyLabel;
    private NeonProgressBar affectionBar;
    private JTextArea dialogueArea;
    private JPanel buttonPanel;
    private JLabel dayLabel;

    private static final Font MAIN_FONT = new Font("Tahoma", Font.BOLD, 18);
    private static final Font DIALOG_FONT = new Font("Tahoma", Font.BOLD, 22);

    public GameGui(String characterKey) {
        this.characterKey = characterKey;
        this.player = new Player("เซนต์", 1000);
        this.manager = new ChoiceManager();
        this.inventory = new Inventory();

        setTitle("💖 Jib Girl Game - Cute Edition 💖");
        setSize(1200, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kawaii Background
        setContentPane(new BackgroundPanel("/com/jibgirl/asset/bg.jpg"));
        getContentPane().setLayout(new BorderLayout());

        // ======================
        // NORTH PANEL (Header)
        // ======================
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);
        northPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Top Header: Relationship Bar (Matches mockup - top center)
        JPanel topHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topHeader.setOpaque(false);
        affectionBar = new NeonProgressBar();
        affectionBar.setCute(true);
        affectionBar.setValue(player.getAffection());
        affectionBar.setStringPainted(true);
        affectionBar.setString("Relationship");
        affectionBar.setPreferredSize(new Dimension(800, 30));
        topHeader.add(affectionBar);
        northPanel.add(topHeader, BorderLayout.NORTH);

        // Sub-Header: Money (WEST) and Day (EAST)
        JPanel statusHeader = new JPanel(new BorderLayout());
        statusHeader.setOpaque(false);
        statusHeader.setBorder(new EmptyBorder(10, 20, 0, 20));

        // West: Money & Shop
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        ModernPanel moneyContainer = new ModernPanel(20);
        moneyContainer.setBackground(UIUtils.PASTEL_PINK);
        moneyContainer.setPreferredSize(new Dimension(180, 70));
        moneyLabel = new JLabel("💰 1,000", SwingConstants.CENTER);
        moneyLabel.setForeground(new Color(100, 50, 50));
        moneyLabel.setFont(MAIN_FONT);
        moneyContainer.add(moneyLabel);

        PremiumButton shopBtn = new PremiumButton("SHOP");
        shopBtn.setCute(true);
        shopBtn.setPreferredSize(new Dimension(100, 50));
        shopBtn.addActionListener(e -> new ShopUI(player, inventory, this));

        leftPanel.add(moneyContainer);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(shopBtn);
        statusHeader.add(leftPanel, BorderLayout.WEST);

        // East: Day & Inventory
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        ModernPanel dayContainer = new ModernPanel(20);
        dayContainer.setBackground(UIUtils.PASTEL_PURPLE);
        dayContainer.setPreferredSize(new Dimension(120, 70));
        dayLabel = new JLabel("DAY 1", SwingConstants.CENTER);
        dayLabel.setForeground(new Color(80, 50, 100));
        dayLabel.setFont(MAIN_FONT);
        dayContainer.add(dayLabel);

        PremiumButton invBtn = new PremiumButton("Inventory");
        invBtn.setCute(true);
        invBtn.setPreferredSize(new Dimension(120, 50));
        invBtn.addActionListener(e -> new ModernInventoryUI(inventory, player, this));

        rightPanel.add(dayContainer);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(invBtn);
        statusHeader.add(rightPanel, BorderLayout.EAST);

        northPanel.add(statusHeader, BorderLayout.CENTER);
        getContentPane().add(northPanel, BorderLayout.NORTH);

        // ======================
        // CENTER/RIGHT PANEL (Choice Buttons)
        // ======================
        // Matches mockup: Choice box on the right side
        JPanel eastCenterPanel = new JPanel(new BorderLayout());
        eastCenterPanel.setOpaque(false);
        eastCenterPanel.setBorder(new EmptyBorder(0, 0, 20, 40));

        ModernPanel choiceContainer = new ModernPanel(40);
        choiceContainer.setBackground(new Color(255, 200, 220, 180)); // Soft Pastel Pink
        choiceContainer.setPreferredSize(new Dimension(280, 240)); // Shorter height to fit 3 choices
        choiceContainer.setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JScrollPane choiceScroll = new JScrollPane(buttonPanel);
        choiceScroll.setOpaque(false);
        choiceScroll.getViewport().setOpaque(false);
        choiceScroll.setBorder(null);
        choiceContainer.add(choiceScroll, BorderLayout.CENTER);

        eastCenterPanel.add(choiceContainer, BorderLayout.SOUTH);
        getContentPane().add(eastCenterPanel, BorderLayout.EAST);

        // ======================
        // SOUTH PANEL (Dialogue Area)
        // ======================
        // Matches mockup: Dialogue box at the bottom, full width
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(0, 20, 30, 20));

        ModernPanel dialogueContainer = new ModernPanel(40);
        dialogueContainer.setBackground(new Color(255, 255, 255, 200)); // Semi-transparent White
        dialogueContainer.setPreferredSize(new Dimension(getWidth(), 180));

        dialogueArea = new JTextArea();
        dialogueArea.setOpaque(false);
        dialogueArea.setEditable(false);
        dialogueArea.setForeground(new Color(100, 50, 50)); // Dark Brown text
        dialogueArea.setFont(DIALOG_FONT);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setPreferredSize(new Dimension(1100, 120));
        dialogueArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        dialogueContainer.add(dialogueArea);

        southPanel.add(dialogueContainer, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        loadScene();
        setVisible(true);
    }

    public void refreshStatus() {
        updateUI();
    }

    private void loadScene() {
        buttonPanel.removeAll();
        if (characterKey.equalsIgnoreCase("Maprang")) {
            loadMaprangRoute();
        } else if (characterKey.equalsIgnoreCase("Ice")) {
            loadIceRoute();
        } else {
            dialogueArea.setText("เนื้อเรื่องของ " + characterKey + " ยังไม่พร้อมใช้งานนะจ๊ะ ✨");
        }
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void loadMaprangRoute() {
        switch (day) {
            case 1:
                runDay1();
                break;
            case 2:
                runDay2();
                break;
            case 3:
                runDay3();
                break;
            case 4:
                runDay4();
                break;
            case 5:
                runDay5();
                break;
            default:
                showEnding();
        }
    }

    private void loadIceRoute() {
        switch (day) {
            case 1:
                runIceDay1();
                break;
            case 2:
                runIceDay2();
                break;
            case 3:
                runIceDay3();
                break;
            case 4:
                runIceDay4();
                break;
            case 5:
                runIceDay5();
                break;
            default:
                showEnding();
        }
    }

    private void runDay1() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 1: ห้องศิลปะ --- 🖌️\n'มะปราง': \"หมายถึงแมว…หรือฉันคะ?\" เธอจ้องมองคุณด้วยความสงสัย");
            addChoice("คุณนั่นแหละครับ ✨", 10, 0, "D1_CHOICE1_A");
            addChoice("หมายถึงแมวครับ 🐈", 3, 0, "D1_CHOICE1_B");
            addChoice("พูดเล่นครับ 😋", -5, 0, "D1_CHOICE1_C");
        } else if (gameState.equals("D1_CHOICE1_A")) {
            currentScene = new Dialogue("มะปรางหน้าแดง \"เขินนะคะ… คุณพูดแบบนี้บ่อยเหรอ?\"");
            addChoice("พูดเฉพาะคนพิเศษ 💖", 10, 0, "NEXT_DAY");
            addChoice("ชมตามมารยาท ✨", 0, 0, "NEXT_DAY");
            addChoice("ชมทุกคนแหละ 😜", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("D1_CHOICE1_B")) {
            currentScene = new Dialogue("มะปราง: \"อ๋อ แมวตัวนี้เองเหรอคะ ก็น่ารักดีค่ะ\"");
            addChoice("เหมือนเจ้าของเลย ✨", 10, 0, "NEXT_DAY");
            addChoice("มันดูสบายดีนะ 🐾", 3, 0, "NEXT_DAY");
            addChoice("ก็ธรรมดานะ 😐", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("D1_CHOICE1_C")) {
            currentScene = new Dialogue("บรรยากาศจืดสนิท มะปรางขมวดคิ้วเล็กน้อย");
            addChoice("ขอโทษนะ 🥺", 3, 0, "NEXT_DAY");
            addChoice("ก็จริงนี่ 🙄", -10, 0, "NEXT_DAY");
            addChoice("(เงียบไปเลย) 🤐", -5, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runDay2() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 2: ลานชมวิว --- 🌅\nมะปราง: \"ฉันชอบแสงแบบนี้ค่ะ มันทำให้ใจสงบดี\"");
            addChoice("ทำไมถึงชอบ? 🦋", 10, 0, "D2_A");
            addChoice("สวยเหมือนเธอ ✨", 5, 0, "D2_A");
            addChoice("เหมือนทุกวัน 😴", -5, 0, "D2_C");
        } else if (gameState.equals("D2_A")) {
            currentScene = new Dialogue("เธอยิ้มและเริ่มเล่าเรื่องตอนเด็กให้คุณฟัง...");
            addChoice("เธอเก่งจัง 🌸", 10, 0, "NEXT_DAY");
            addChoice("เธอน่ารักจัง 😊", 5, 0, "NEXT_DAY");
            addChoice("คิดมากไปไหม? 🤨", -10, 0, "NEXT_DAY");
        } else if (gameState.equals("D2_C")) {
            currentScene = new Dialogue("เธอเงียบลงและก้มหน้ามองพื้น...");
            addChoice("ขอโทษนะ 🥺", 3, 0, "NEXT_DAY");
            addChoice("พูดความจริง 😐", -5, 0, "NEXT_DAY");
            addChoice("กลับกันเถอะ 🚶", -10, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runDay3() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 3: เตรียมนิทรรศการ --- 🎨\nมะปราง: \"ช่วยเลือกโทนสีหน่อยได้ไหมคะ ฉันลังเลจัง\"");
            addChoice("ช่วยเลือกเต็มที่ 🖌️", 10, 0, "D3_A");
            addChoice("อะไรก็ได้ที่ชอบ ✨", 5, 0, "D3_A");
            addChoice("ไม่รู้เรื่องหรอก 💤", -10, 0, "D3_C");
        } else if (gameState.equals("D3_A")) {
            currentScene = new Dialogue("เธอประทับใจที่คุณใส่ใจรายละเอียดงานของเธอ");
            addChoice("จะอยู่ช่วยจนเสร็จ 💖", 10, 0, "NEXT_DAY");
            addChoice("ช่วยก็สนุกดี 😊", 5, 0, "NEXT_DAY");
            addChoice("เหนื่อยจัง 🥱", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("D3_C")) {
            currentScene = new Dialogue("เธอพยักหน้าเบา ๆ แล้วหันไปวาดต่อเงียบ ๆ");
            addChoice("ขอโทษ เดี๋ยวช่วย 🥺", 3, 0, "NEXT_DAY");
            addChoice("ไปนั่งพัก 💤", -10, 0, "NEXT_DAY");
            addChoice("ขอตัวก่อน 👋", -10, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runDay4() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 4: คาเฟ่ริมน้ำ --- ☕\nมะปราง: \"ถ้าวันหนึ่งฉันวาดไม่เก่งแล้ว คุณยังจะอยู่ไหมคะ?\"");
            addChoice("ชอบที่ตัวตนเธอ 💖", 10, 0, "D4_A");
            addChoice("ฝึกใหม่ด้วยกัน ✨", 5, 0, "D4_A");
            addChoice("หาอย่างอื่นทำ 😐", -10, 0, "D4_C");
        } else if (gameState.equals("D4_A")) {
            currentScene = new Dialogue("เธอนิ่งไปครู่หนึ่ง แล้วยิ้มออกมาด้วยความโล่งใจ");
            addChoice("จับมือเบา ๆ 🙏💖", 10, 0, "NEXT_DAY");
            addChoice("ยิ้มให้อ่อนโยน 😊", 5, 0, "NEXT_DAY");
            addChoice("หัวเราะแก้เขิน 😅", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("D4_C")) {
            currentScene = new Dialogue("บรรยากาศในคาเฟ่เริ่มอึดอัด...");
            addChoice("ล้อเล่นนะ 🥺", 3, 0, "NEXT_DAY");
            addChoice("พูดตามจริง ✨", -10, 0, "NEXT_DAY");
            addChoice("เปลี่ยนเรื่อง 😶", -5, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runDay5() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 5: การสารภาพรัก --- 💖\n'เซนต์': \"ผมว่าเราอยู่ด้วยกันแบบนี้ก็ดีนะ... ผมชอบเธอนะ มะปราง\"");
            addChoice("สารภาพจริงใจ 💎💖", 10, 0, "FINAL");
            addChoice("สารภาพเขิน ๆ 😳💓", 5, 0, "FINAL");
            addChoice("พูดติดตลก 🍭", -5, 0, "FINAL");
        }
        updateUI();
    }

    private void showEnding() {
        int score = player.getAffection();
        String endingText;

        if (characterKey.equalsIgnoreCase("Maprang")) {
            if (score >= 40)
                endingText = "🎉 HAPPY ENDING: \"งั้นอยู่ดูพระอาทิตย์ตกกับฉันไปเรื่อย ๆ นะคะ\" 🌅💖";
            else if (score > -20)
                endingText = "✨ NORMAL ENDING: \"คุณคือเพื่อนที่อบอุ่นที่สุดของฉัน\" 👫🌸";
            else
                endingText = "💔 BAD ENDING: \"เราคงเข้ากันไม่ได้ค่ะ\" 👋";
        } else if (characterKey.equalsIgnoreCase("Ice")) {
            if (score >= 40)
                endingText = "🏆 HAPPY ENDING: \"งั้นฉันยอมแพ้ให้นายครั้งหนึ่ง... เป็นคู่แข่งที่ดีที่สุดก็พอ\" 🏀💖";
            else
                endingText = "🏟️ NORMAL ENDING: \"นายนี่มัน... ยังไม่พร้อมจะเดินข้างฉันหรอกนะ\" 👋";
        } else {
            endingText = "✨ จบการเดินทาง... คะแนนของคุณคือ: " + score;
        }

        dialogueArea.setText(endingText);
        PremiumButton exitBtn = new PremiumButton("BACK TO MAIN MENU 🏠✨");
        exitBtn.setCute(true);
        exitBtn.setPreferredSize(new Dimension(250, 60));
        exitBtn.addActionListener(e -> {
            new StartScreen();
            dispose();
        });
        buttonPanel.add(exitBtn);
    }

    private void addChoice(String text, int affect, int cost, String nextState) {
        Choice c = new Choice(text, affect, cost, "");
        PremiumButton btn = new PremiumButton(text);
        btn.setCute(true); // Back to cute theme
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            manager.selectChoice(player, c);
            if (nextState.equals("NEXT_DAY")) {
                day++;
                gameState = "START";
            } else if (nextState.equals("FINAL")) {
                day = 6;
                gameState = "ENDED";
            } else {
                gameState = nextState;
            }
            loadScene();
        });
        buttonPanel.add(btn);
        buttonPanel.add(Box.createVerticalStrut(10));
    }

    // ======================
    // ICE ROUTE DAYS
    // ======================

    private void runIceDay1() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 1: สนามบาส --- 🏀\nไอซ์: \"จะจีบก็ต้องชนะฉันก่อน\"");
            addChoice("งั้นเริ่มเลย ✨", 10, 0, "ICE_D1_A");
            addChoice("ไม่แข่งได้ไหม 🥤", 0, 0, "ICE_D1_B");
            addChoice("ก็แค่บาสเอง 🏀", -5, 0, "ICE_D1_C");
        } else if (gameState.equals("ICE_D1_A")) {
            currentScene = new Dialogue("แข่งกันจริงจัง เหงื่อซึม... ไอซ์: \"ไม่หนีนี่นา\"");
            addChoice("ฉันไม่หนีอะไรที่สำคัญ ✨", 10, 0, "NEXT_DAY");
            addChoice("ก็แค่สนุกดี 🏀", 3, 0, "NEXT_DAY");
            addChoice("ก็ต้องชนะเธอสิ 🔥", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D1_B")) {
            currentScene = new Dialogue("ไอซ์: \"กลัวแพ้เหรอ\"");
            addChoice("ไม่ได้กลัว แค่อยากคุยมากกว่า ✨", 5, 0, "NEXT_DAY");
            addChoice("ก็ใช่... 🤐", -5, 0, "NEXT_DAY");
            addChoice("แข่งครึ่งเดียวพอ 🏀", 3, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D1_C")) {
            currentScene = new Dialogue("ไอซ์: \"ถ้าไม่จริงจังก็อย่าพูดแบบนั้น\"");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY");
            addChoice("ก็เรื่องจริง 🙄", -10, 0, "NEXT_DAY");
            addChoice("(เงียบ) 🤐", -5, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runIceDay2() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 2: ซ้อมวิ่งตอนเย็น --- 🏃\nไอซ์: \"จะวิ่งด้วยไหม\"");
            addChoice("วิ่งด้วย 🏃", 10, 0, "ICE_D2_A");
            addChoice("เชียร์อยู่ข้างสนาม 📣", 3, 0, "ICE_D2_B");
            addChoice("บ่นว่าเหนื่อย 😫", -5, 0, "ICE_D2_C");
        } else if (gameState.equals("ICE_D2_A")) {
            currentScene = new Dialogue("เธอชะลอความเร็วรอคุณ...");
            addChoice("เธอเก่งมาก 👍✨", 10, 0, "NEXT_DAY");
            addChoice("เหนื่อยไหม? 🥤", 3, 0, "NEXT_DAY");
            addChoice("ช้ากว่านี้ได้ไหม 😫", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D2_B")) {
            currentScene = new Dialogue("เธอวิ่งไปมองคุณไป...");
            addChoice("ส่งน้ำให้ 🥤", 5, 0, "NEXT_DAY");
            addChoice("เล่นมือถือ 📱", -5, 0, "NEXT_DAY");
            addChoice("ตะโกนแซว 📣", 3, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D2_C")) {
            currentScene = new Dialogue("เธอวิ่งต่อโดยไม่สนใจ...");
            addChoice("เดินตาม 🚶", 3, 0, "NEXT_DAY");
            addChoice("กลับก่อน 👋", -5, 0, "NEXT_DAY");
            addChoice("ตะโกนบ่น 😫", -10, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runIceDay3() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 3: โปรเจกต์เรียน --- 📚\nไอซ์: \"ช่วยดูแบบแปลนหน่อย\"");
            addChoice("วิเคราะห์จริงจัง 📐", 10, 0, "ICE_D3_A");
            addChoice("ฟังเฉย ๆ 🎧", 0, 0, "ICE_D3_B");
            addChoice("บอกว่ายากเกิน 😫", -5, 0, "ICE_D3_C");
        } else if (gameState.equals("ICE_D3_A")) {
            currentScene = new Dialogue("เธอเริ่มพึ่งคุณมากขึ้น...");
            addChoice("อยู่ช่วยจนเสร็จ ✨", 10, 0, "NEXT_DAY");
            addChoice("ช่วยบางส่วน 👍", 3, 0, "NEXT_DAY");
            addChoice("บ่นว่าเสียเวลา 🙄", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D3_B")) {
            currentScene = new Dialogue("บรรยากาศค่อนข้างเงียบ...");
            addChoice("ถามเพิ่ม? ❓", 5, 0, "NEXT_DAY");
            addChoice("เงียบต่อไป... 🤐", -3, 0, "NEXT_DAY");
            addChoice("เปลี่ยนเรื่อง 💬", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D3_C")) {
            currentScene = new Dialogue("เธอขมวดคิ้วเล็กน้อย...");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY");
            addChoice("ยืนยันไม่ช่วย 🙅", -10, 0, "NEXT_DAY");
            addChoice("ลุกออก 👋", -10, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runIceDay4() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 4: คาเฟ่เงียบ --- ☕\nไอซ์: \"บางทีฉันก็เหนื่อยนะ ที่ต้องเก่งตลอด...\"");
            addChoice("เธอไม่ต้องเก่งตลอดก็ได้ ✨", 10, 0, "ICE_D4_A");
            addChoice("ก็เธอเลือกเอง 🙄", -5, 0, "ICE_D4_B");
            addChoice("เงียบฟัง 🎧", 3, 0, "ICE_D4_C");
        } else if (gameState.equals("ICE_D4_A")) {
            currentScene = new Dialogue("เธอมองคุณนานขึ้น...");
            addChoice("ฉันอยากเป็นที่พักให้เธอ 💖", 10, 0, "NEXT_DAY");
            addChoice("ยิ้มให้ 😊", 3, 0, "NEXT_DAY");
            addChoice("หัวเราะกลบ 😅", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D4_B")) {
            currentScene = new Dialogue("ไอซ์ดูจะผิดหวังเล็กน้อย...");
            addChoice("แก้คำพูด 🥺", 3, 0, "NEXT_DAY");
            addChoice("ยืนยันคำเดิม 🤐", -10, 0, "NEXT_DAY");
            addChoice("เปลี่ยนเรื่อง 😶", -5, 0, "NEXT_DAY");
        } else if (gameState.equals("ICE_D4_C")) {
            currentScene = new Dialogue("บรรยากาศอ่อนโยนขึ้น...");
            addChoice("จับมือเธอ 🤝💖", 10, 0, "NEXT_DAY");
            addChoice("บอกว่าเข้าใจนะ ✨", 3, 0, "NEXT_DAY");
            addChoice("ล้อเล่นนิดหน่อย 😋", -5, 0, "NEXT_DAY");
        }
        updateUI();
    }

    private void runIceDay5() {
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 5: สารภาพ --- 🏀💖\n'เซนต์': \"ฉันไม่ได้อยากชนะเกม แต่อยากอยู่ข้างเธอ\"");
            addChoice("จริงจังลึกซึ้ง 💎💖", 10, 0, "FINAL");
            addChoice("พูดแบบเขิน ๆ 😳💓", 3, 0, "FINAL");
            addChoice("พูดท้าทาย 🔥", -5, 0, "FINAL");
        }
        updateUI();
    }

    private void updateUI() {
        dayLabel.setText("DAY " + day);
        moneyLabel.setText("💰 " + String.format("%,d", player.getMoney()));
        affectionBar.setValue(player.getAffection());
        dialogueArea.setText(currentScene.getQuestion());
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String fileName) {
            try {
                java.net.URL imgURL = getClass().getResource(fileName);
                if (imgURL != null)
                    backgroundImage = new ImageIcon(imgURL).getImage();
            } catch (Exception e) {
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(255, 255, 255, 40));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
