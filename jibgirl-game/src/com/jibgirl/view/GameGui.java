package com.jibgirl.view;

import com.jibgirl.controller.ChoiceManager;
import com.jibgirl.controller.SceneFactory;
import com.jibgirl.controller.StaminaManager;
import com.jibgirl.main.Inventrory.Inventory;
import com.jibgirl.main.Shop.ShopUI;
import com.jibgirl.model.Choice;
import com.jibgirl.model.Dialogue;
import com.jibgirl.model.Player;
import com.jibgirl.model.Scene;
import com.jibgirl.network.GameClient;
import com.jibgirl.network.GameResult;
import com.jibgirl.network.GameServer;
import com.jibgirl.utils.UIUtils;
import com.jibgirl.utils.UIUtils.ModernPanel;
import com.jibgirl.utils.UIUtils.NeonProgressBar;
import com.jibgirl.utils.UIUtils.PremiumButton;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameGui extends JFrame {

    private Player player;
    private Dialogue currentScene;
    private ChoiceManager manager;
    private StaminaManager staminaManager;
    private GameClient client; // set when playing in multiplayer
    private Inventory inventory;
    private String characterKey;
    private int day = 1;
    private String gameState = "START";
    private boolean isShy = false; // Track if character is currently shy

    private JLabel moneyLabel;
    private NeonProgressBar affectionBar;
    private NeonProgressBar staminaBar;
    private JTextArea dialogueArea;
    private JPanel buttonPanel;
    private JLabel dayLabel;
    private JLabel timerLabel;
    private com.jibgirl.utils.QuestionTimer questionTimer;

    private static final Font MAIN_FONT = new Font("Tahoma", Font.BOLD, 18);
    private static final Font DIALOG_FONT = new Font("Tahoma", Font.BOLD, 22);

    public GameGui(String characterKey) {
        this.characterKey = characterKey;
        // เริ่มต้นเงิน 100 บาทตามระบบรายได้รายวัน
        this.player = new Player("เซนต์", 100);
        this.manager = new ChoiceManager();
        this.staminaManager = new StaminaManager();
        this.inventory = new Inventory();

        // multiplayer client is null in this constructor
        this.client = null;

        setTitle("💖 Jib Girl Game - Cute Edition 💖");
        setSize(1200, 900);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Kawaii Background
        BackgroundPanel bgPanel = new BackgroundPanel("/com/jibgirl/asset/bg.jpg");
        setContentPane(bgPanel);
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

        // Center: Stamina Bar
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel staminaLabelTop = new JLabel("⚡ Stamina", SwingConstants.CENTER);
        staminaLabelTop.setForeground(new Color(100, 100, 50));
        staminaLabelTop.setFont(new Font("Tahoma", Font.BOLD, 12));

        staminaBar = new NeonProgressBar();
        staminaBar.setStamina(true); // Pastel yellow for stamina
        staminaBar.setValue(staminaManager.getStamina());
        staminaBar.setPreferredSize(new Dimension(120, 10)); // [AESTHETIC] Reduced size

        centerPanel.add(staminaLabelTop, BorderLayout.NORTH);
        centerPanel.add(staminaBar, BorderLayout.CENTER);
        statusHeader.add(centerPanel, BorderLayout.CENTER);

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
        rightPanel.add(Box.createVerticalStrut(10));
        timerLabel = new JLabel("⏱️ 30", SwingConstants.CENTER);
        timerLabel.setForeground(new Color(80, 50, 100));
        timerLabel.setFont(MAIN_FONT);
        // only show timer in multiplayer mode
        timerLabel.setVisible(client != null);
        rightPanel.add(timerLabel);
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
        if (client != null) {
            startQuestionTimer();
        }
        setVisible(true);
    }

    /**
     * Multiplayer constructor - initializes GUI based on provided GameClient.
     * The characterKey is retrieved from the client's own PlayerState.
     */
    public GameGui(GameClient client) {
        // [FIX] Use targetCharacter from client (set by host) instead of local player
        // state
        String charKey = client.getTargetCharacter();
        if (charKey == null || charKey.equals("None")) {
            // Fallback to local state just in case, but targetCharacter should be dominant
            GameServer.PlayerState ps = client.getAllPlayers().get(client.getMyId());
            charKey = ps != null ? ps.character : "Maprang"; // Default to Maprang if all else fails
        }
        // call local constructor to build GUI
        this(charKey);
        // save client reference
        this.client = client;
        // listen for game-end broadcast and display results
        client.setOnGameEndListener(results -> {
            GameResult myRes = results.get(client.getMyId());
            if (myRes != null) {
                SwingUtilities.invokeLater(() -> {
                    new OnlineEndingScreen(myRes, List.copyOf(results.values()));
                    dispose();
                });
            }
        });
    }

    public void setBackgroundImage(String fileName) {
        if (getContentPane() instanceof BackgroundPanel) {
            ((BackgroundPanel) getContentPane()).setBackgroundImage(fileName);
        }
    }

    public void setCharacterImage(String fileName) {
        if (getContentPane() instanceof BackgroundPanel) {
            ((BackgroundPanel) getContentPane()).setCharacterImage(fileName);
        }
    }

    public void refreshStatus() {
        updateUI();
    }

    /**
     * Trigger the confession scene when stamina is depleted
     */
    private void triggerConfessionScene() {
        if (questionTimer != null)
            questionTimer.stop();
        isShy = true; // Always shy during confession
        setBackgroundImage("/com/jibgirl/asset/classroom.png"); // Confession is always in Classroom
        buttonPanel.removeAll();
        Scene confessionScene = SceneFactory.createClassroomConfession(characterKey);
        Dialogue confessionDialogue = confessionScene.getDialogue();

        dialogueArea.setText(confessionDialogue.getQuestion());
        // Set character to shy again just in case
        String charPrefix = characterKey.toLowerCase();
        setCharacterImage("/com/jibgirl/asset/" + charPrefix + "_shy.png");

        // Add confession choices
        for (Choice choice : confessionDialogue.getChoices()) {
            PremiumButton btn = new PremiumButton(choice.getText());
            btn.setCute(true);
            btn.setPreferredSize(new Dimension(240, 45));
            btn.setMaximumSize(new Dimension(240, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            btn.addActionListener(e -> {
                manager.selectChoice(player, choice, staminaManager);
                // After confession, show ending
                gameState = "ENDED";
                day = 6;
                showEnding();
            });

            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(10));
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void triggerBadEnd() {
        if (questionTimer != null)
            questionTimer.stop();
        setBackgroundImage("/com/jibgirl/asset/start.jpg"); // Return to home theme or similar
        buttonPanel.removeAll();
        Scene badEndScene = SceneFactory.createBadEndScene(characterKey);
        Dialogue dialogue = badEndScene.getDialogue();

        dialogueArea.setText(dialogue.getQuestion());
        // Clear character sprite for bad end? Or show normal/sad?
        // Showing normal as a "friendzone" look
        String charPrefix = characterKey.toLowerCase();
        setCharacterImage("/com/jibgirl/asset/" + charPrefix + "_normal.png");

        for (Choice choice : dialogue.getChoices()) {
            PremiumButton btn = new PremiumButton(choice.getText());
            btn.setCute(false); // Not cute anymore!
            btn.setChoiceStyle(true); // Red style
            btn.setPreferredSize(new Dimension(240, 45));
            btn.setMaximumSize(new Dimension(240, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            btn.addActionListener(e -> {
                // Return to selection screen
                new CharacterSelectionScreen();
                dispose();
            });

            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(10));
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void loadScene() {
        buttonPanel.removeAll();
        // Set character image - use shy if isShy is true, otherwise normal
        String charPrefix = characterKey.toLowerCase();
        String expression = isShy ? "_shy.png" : "_normal.png";
        setCharacterImage("/com/jibgirl/asset/" + charPrefix + expression);

        if (characterKey.equalsIgnoreCase("Maprang")) {
            loadMaprangRoute();
        } else if (characterKey.equalsIgnoreCase("Ice")) {
            loadIceRoute();
        } else if (characterKey.equalsIgnoreCase("Kanom")) {
            loadKanomRoute();
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

    private void loadKanomRoute() {
        switch (day) {
            case 1:
                runKanomDay1();
                break;
            case 2:
                runKanomDay2();
                break;
            case 3:
                runKanomDay3();
                break;
            case 4:
                runKanomDay4();
                break;
            case 5:
                runKanomDay5();
                break;
            default:
                showEnding();
        }
    }

    private void runDay1() {
        setBackgroundImage("/com/jibgirl/asset/artroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 1: ห้องศิลปะ --- 🖌️\n'มะปราง': \"หมายถึงแมว…หรือฉันคะ?\" เธอจ้องมองคุณด้วยความสงสัย");
            addChoice("คุณนั่นแหละครับ ✨", 10, 0, "D1_CHOICE1_A", 5);
            addChoice("หมายถึงแมวครับ 🐈", 3, 0, "D1_CHOICE1_B", 5);
            addChoice("พูดเล่นครับ 😋", -5, 0, "D1_CHOICE1_C", 5);
        } else if (gameState.equals("D1_CHOICE1_A")) {
            currentScene = new Dialogue("มะปรางหน้าแดง \"เขินนะคะ… คุณพูดแบบนี้บ่อยเหรอ?\"");
            addChoice("พูดเฉพาะคนพิเศษ 💖", 10, 0, "NEXT_DAY", 3);
            addChoice("ชมตามมารยาท ✨", 0, 0, "NEXT_DAY", 3);
            addChoice("ชมทุกคนแหละ 😜", -5, 0, "NEXT_DAY", 3);
        } else if (gameState.equals("D1_CHOICE1_B")) {
            currentScene = new Dialogue("มะปราง: \"อ๋อ แมวตัวนี้เองเหรอคะ ก็น่ารักดีค่ะ\"");
            addChoice("เหมือนเจ้าของเลย ✨", 10, 0, "NEXT_DAY", 3);
            addChoice("มันดูสบายดีนะ 🐾", 3, 0, "NEXT_DAY", 3);
            addChoice("ก็ธรรมดานะ 😐", -5, 0, "NEXT_DAY", 3);
        } else if (gameState.equals("D1_CHOICE1_C")) {
            currentScene = new Dialogue("บรรยากาศจืดสนิท มะปรางขมวดคิ้วเล็กน้อย");
            addChoice("ขอโทษนะ 🥺", 3, 0, "NEXT_DAY", 3);
            addChoice("ก็จริงนี่ 🙄", -10, 0, "NEXT_DAY", 3);
            addChoice("(เงียบไปเลย) 🤐", -5, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runDay2() {
        setBackgroundImage("/com/jibgirl/asset/classroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 2: ห้องเรียน --- 🌅\nมะปราง: \"ฉันอบอุ่นที่นี่ค่ะ มันทำให้ใจสงบดี\"");
            addChoice("ทำไมถึงชอบ? 🦋", 10, 0, "D2_A", 5);
            addChoice("สวยเหมือนเธอ ✨", 5, 0, "D2_A", 5);
            addChoice("เหมือนทุกวัน 😴", -5, 0, "D2_C", 5);
        } else if (gameState.equals("D2_A")) {
            currentScene = new Dialogue("เธอยิ้มและเริ่มเล่าเรื่องตอนเด็กให้คุณฟัง...");
            addChoice("เธอเก่งจัง 🌸", 10, 0, "NEXT_DAY", 5);
            addChoice("เธอน่ารักจัง 😊", 5, 0, "NEXT_DAY", 5);
            addChoice("คิดมากไปไหม? 🤨", -10, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("D2_C")) {
            currentScene = new Dialogue("เธอเงียบลงและก้มหน้ามองพื้น...");
            addChoice("ขอโทษนะ 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("พูดความจริง 😐", -5, 0, "NEXT_DAY", 5);
            addChoice("กลับกันเถอะ 🚶", -10, 0, "NEXT_DAY", 5);
        }
        updateUI();
    }

    private void runDay3() {
        setBackgroundImage("/com/jibgirl/asset/clubroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 3: ห้องชมรม --- 🎨\nมะปราง: \"ช่วยเลือกโทนสีหน่อยได้ไหมคะ ฉันลังเลจัง\"");
            addChoice("ช่วยเลือกเต็มที่ 🖌️", 10, 0, "D3_A", 15);
            addChoice("อะไรก็ได้ที่ชอบ ✨", 5, 0, "D3_A", 10);
            addChoice("ไม่รู้เรื่องหรอก 💤", -10, 0, "D3_C", 3);
        } else if (gameState.equals("D3_A")) {
            currentScene = new Dialogue("เธอประทับใจที่คุณใส่ใจรายละเอียดงานของเธอ");
            addChoice("จะอยู่ช่วยจนเสร็จ 💖", 10, 0, "NEXT_DAY", 20);
            addChoice("ช่วยก็สนุกดี 😊", 5, 0, "NEXT_DAY", 15);
            addChoice("เหนื่อยจัง 🥱", -5, 0, "NEXT_DAY", 3);
        } else if (gameState.equals("D3_C")) {
            currentScene = new Dialogue("เธอพยักหน้าเบา ๆ แล้วหันไปวาดต่อเงียบ ๆ");
            addChoice("ขอโทษ เดี๋ยวช่วย 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("ไปนั่งพัก 💤", -10, 0, "NEXT_DAY", 5);
            addChoice("ขอตัวก่อน 👋", -10, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runDay4() {
        setBackgroundImage("/com/jibgirl/asset/cafe.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 4: คาเฟ่ --- ☕\nมะปราง: \"ถ้าวันหนึ่งฉันวาดไม่เก่งแล้ว คุณยังจะอยู่ไหมคะ?\"");
            addChoice("ชอบที่ตัวตนเธอ 💖", 10, 0, "D4_A", 5);
            addChoice("ฝึกใหม่ด้วยกัน ✨", 5, 0, "D4_A", 5);
            addChoice("หาอย่างอื่นทำ 😐", -10, 0, "D4_C", 5);
        } else if (gameState.equals("D4_A")) {
            currentScene = new Dialogue("เธอนิ่งไปครู่หนึ่ง แล้วยิ้มออกมาด้วยความโล่งใจ");
            addChoice("จับมือเบา ๆ 🙏💖", 10, 0, "NEXT_DAY", 8);
            addChoice("ยิ้มให้อ่อนโยน 😊", 5, 0, "NEXT_DAY", 5);
            addChoice("หัวเราะแก้เขิน 😅", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("D4_C")) {
            currentScene = new Dialogue("บรรยากาศในคาเฟ่เริ่มอึดอัด...");
            addChoice("ล้อเล่นนะ 🥺", 3, 0, "NEXT_DAY", 5);
            addChoice("พูดตามจริง ✨", -10, 0, "NEXT_DAY", 5);
            addChoice("เปลี่ยนเรื่อง 😶", -5, 0, "NEXT_DAY", 5);
        }
        updateUI();
    }

    private void runDay5() {
        setBackgroundImage("/com/jibgirl/asset/basketball.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 5: สนามบาสเกตบอล --- 🏀\n'เซนต์': \"ผมว่าเราอยู่ด้วยกันแบบนี้ก็ดีนะ... ผมชอบเธอนะ มะปราง\"");
            addChoice("สารภาพจริงใจ 💎💖", 10, 0, "FINAL", 0);
            addChoice("สารภาพเขิน ๆ 😳💓", 5, 0, "FINAL", 0);
            addChoice("พูดติดตลก 🍭", -5, 0, "FINAL", 0);
        }
        updateUI();
    }

    private void showEnding() {
        if (questionTimer != null) {
            questionTimer.stop();
        }
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
        } else if (characterKey.equalsIgnoreCase("Kanom")) {
            if (score >= 40)
                endingText = "📸 HAPPY ENDING: \"งั้นเราไปเที่ยวด้วยกันทั้งชีวิตเลยไหม!\" 💖🏖️";
            else if (score >= 20)
                endingText = "✨ NORMAL ENDING: \"เพื่อนเที่ยวตลอดไปนะ!\" 🗺️🌸";
            else
                endingText = "💔 BAD ENDING: \"นายดูไม่อินกับโลกของฉันเลย...\" 👋";
        } else {
            endingText = "✨ จบการเดินทาง... คะแนนของคุณคือ: " + score;
        }

        dialogueArea.setText(endingText);
        // notify server if playing online
        if (client != null) {
            client.finishGame(player.getAffection(), staminaManager.getStamina(), characterKey);
        }
        PremiumButton exitBtn = new PremiumButton("BACK TO MAIN MENU 🏠✨");
        exitBtn.setCute(true);
        exitBtn.setPreferredSize(new Dimension(250, 60));
        exitBtn.addActionListener(e -> {
            new StartScreen();
            dispose();
        });
        buttonPanel.add(exitBtn);
    }

    /**
     * Initialize and start the per-question countdown timer for multiplayer mode.
     * This will update the timerLabel and handle expiration automatically.
     */
    private void startQuestionTimer() {
        if (questionTimer == null) {
            questionTimer = new com.jibgirl.utils.QuestionTimer();
            questionTimer.setOnTick(sec -> SwingUtilities.invokeLater(() -> {
                timerLabel.setText("⏱️ " + sec);
                if (questionTimer.isCritical()) {
                    timerLabel.setForeground(Color.RED);
                } else {
                    timerLabel.setForeground(new Color(80, 50, 100));
                }
            }));
            questionTimer.setOnTimeUp(() -> SwingUtilities.invokeLater(() -> {
                // disable choice buttons and progress
                for (Component comp : buttonPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        comp.setEnabled(false);
                    }
                }
                JOptionPane.showMessageDialog(this,
                        "เวลาหมด!", "TIME UP", JOptionPane.WARNING_MESSAGE);
                if (day < 5) {
                    day++;
                    gameState = "START";
                    isShy = false;
                    player.addMoney(100);
                    loadScene();
                } else {
                    day = 6;
                    gameState = "ENDED";
                    showEnding();
                }
                if (client != null) {
                    client.updateProgress(day, player.getAffection(), staminaManager.getStamina());
                }
            }));
        }
        questionTimer.reset();
        questionTimer.start();
    }

    private void addChoice(String text, int affect, int cost, String nextState) {
        addChoice(text, affect, cost, nextState, 5); // Default stamina cost = 5
    }

    private void addChoice(String text, int affect, int cost, String nextState, int staminaCost) {
        // [MOD] Fixed stamina costs based on choice order (Skip if cost is explicitly
        // 0, e.g. Day 5)
        int fixedStaminaCost;
        if (staminaCost == 0) {
            fixedStaminaCost = 0;
        } else {
            int choiceIndex = 0;
            for (Component comp : buttonPanel.getComponents()) {
                if (comp instanceof JButton) {
                    choiceIndex++;
                }
            }

            switch (choiceIndex) {
                case 0:
                    fixedStaminaCost = 20;
                    break;
                case 1:
                    fixedStaminaCost = 15;
                    break;
                case 2:
                    fixedStaminaCost = 10;
                    break;
                default:
                    fixedStaminaCost = 5;
                    break;
            }
        }

        Choice c = new Choice(text, affect, cost, "", fixedStaminaCost);
        PremiumButton btn = new PremiumButton(text);
        btn.setCute(true);
        btn.setPreferredSize(new Dimension(240, 45));
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Check if choice is affordable (money only - stamina exhaustion is handled by
        // logic)
        boolean hasMoney = player.canSpendMoney(cost);

        // Disable button if not enough money
        if (!hasMoney) {
            btn.setEnabled(false);
            btn.setToolTipText("ไม่มีเงิน");
        }

        btn.addActionListener(e -> {
            // stop the countdown regardless
            if (questionTimer != null)
                questionTimer.stop();
            // Try to execute choice
            boolean success = manager.selectChoice(player, c, staminaManager);

            if (success) {
                // If this is a "best" choice (affect >= 5), show shy expression
                if (affect >= 10) {
                    System.out.println("💖 คำตอบที่ยอดเยี่ยม! สลับเป็นหน้าเขิน...");
                    isShy = true;
                } else {
                    isShy = false; // Reset to normal if not a "best" choice
                }

                // Check if stamina depleted
                // [FIX] Skip hijack if it's Day 5, allowing final progression to ending screen
                if (staminaManager.isStaminaDepleted() && day < 5) {
                    if (player.getAffection() < 50) {
                        System.out.println("❌ สมดุลชีวิตหมด! แต่คะแนนความรักไม่ถึง... Bad End");
                        triggerBadEnd();
                    } else {
                        System.out.println("💤 สมดุลชีวิตหมด! บังคับเข้าห้องเรียน...");
                        triggerConfessionScene();
                    }
                } else {
                    // Normal progression
                    if (nextState.equals("NEXT_DAY")) {
                        day++;
                        gameState = "START";
                        isShy = false; // Always reset shy on new day
                        // give daily income (Stamina NOT restored anymore)
                        player.addMoney(100);
                        System.out.println("🎁 ได้รับรายได้ 100 บาท (เงินตอนนี้: " + player.getMoney() + ")");
                    } else if (nextState.equals("FINAL")) {
                        day = 6;
                        gameState = "ENDED";
                    } else {
                        gameState = nextState;
                    }
                    loadScene();
                    if (client != null) {
                        client.updateProgress(day, player.getAffection(), staminaManager.getStamina());
                        startQuestionTimer();
                    }
                }
            } else {
                // Choice failed - show warning
                String reason = "";
                if (!player.canSpendMoney(cost)) {
                    reason = "เงินไม่พอ";
                } else if (!staminaManager.hasEnoughStamina(staminaCost)) {
                    reason = "สมดุลชีวิตไม่พอ";
                }
                JOptionPane.showMessageDialog(this,
                        "ไม่สามารถเลือก: " + reason,
                        "ไม่สามารถ",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(btn);
        buttonPanel.add(Box.createVerticalStrut(10));
    }

    // ======================
    // ICE ROUTE DAYS
    // ======================

    private void runIceDay1() {
        setBackgroundImage("/com/jibgirl/asset/artroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 1: ห้องศิลปะ --- 🖌️\nไอซ์: \"นายมาวาดรูปที่นี่ด้วยเหรอ?\"");
            addChoice("แค่อยากอยู่ใกล้เธอ ✨", 10, 0, "ICE_D1_A", 10);
            addChoice("มาหาที่สงบ ๆ 🎨", 3, 0, "ICE_D1_B", 5);
            addChoice("วาดรูปไม่เป็นหรอก 😅", -5, 0, "ICE_D1_C", 5);
        } else if (gameState.equals("ICE_D1_A")) {
            currentScene = new Dialogue("ไอซ์หน้าแดงนิด ๆ... \"พูดอะไรน่ะ นายเนี่ย...\"");
            addChoice("ฉันไม่หนีอะไรที่สำคัญ ✨", 10, 0, "NEXT_DAY", 10);
            addChoice("ก็แค่สนุกดี 🏀", 3, 0, "NEXT_DAY", 10);
            addChoice("ก็ต้องชนะเธอสิ 🔥", -5, 0, "NEXT_DAY", 15);
        } else if (gameState.equals("ICE_D1_B")) {
            currentScene = new Dialogue("ไอซ์: \"กลัวแพ้เหรอ\"");
            addChoice("ไม่ได้กลัว แค่อยากคุยมากกว่า ✨", 5, 0, "NEXT_DAY", 5);
            addChoice("ก็ใช่... 🤐", -5, 0, "NEXT_DAY", 5);
            addChoice("แข่งครึ่งเดียวพอ 🏀", 3, 0, "NEXT_DAY", 8);
        } else if (gameState.equals("ICE_D1_C")) {
            currentScene = new Dialogue("ไอซ์: \"ถ้าไม่จริงจังก็อย่าพูดแบบนั้น\"");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY", 5);
            addChoice("ก็เรื่องจริง 🙄", -10, 0, "NEXT_DAY", 5);
            addChoice("(เงียบ) 🤐", -5, 0, "NEXT_DAY", 5);
        }
        updateUI();
    }

    private void runIceDay2() {
        setBackgroundImage("/com/jibgirl/asset/classroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 2: ห้องเรียน --- 📚\nไอซ์: \"นายช่วยติววิชานี้หน่อยสิ ชักจะงงแล้ว\"");
            addChoice("ติวให้เต็มที่ ✍️", 10, 0, "ICE_D2_A", 15);
            addChoice("บอกว่ายากเหมือนกัน 😅", 3, 0, "ICE_D2_B", 5);
            addChoice("แอบหลับ 💤", -5, 0, "ICE_D2_C", 3);
        } else if (gameState.equals("ICE_D2_A")) {
            currentScene = new Dialogue("เธอตั้งใจฟังที่คุณอธิบายมาก...");
            addChoice("เธอเก่งมาก 👍✨", 10, 0, "NEXT_DAY", 10);
            addChoice("เหนื่อยไหม? 🥤", 3, 0, "NEXT_DAY", 8);
            addChoice("ช้ากว่านี้ได้ไหม 😫", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("ICE_D2_B")) {
            currentScene = new Dialogue("เธอวิ่งไปมองคุณไป...");
            addChoice("ส่งน้ำให้ 🥤", 5, 0, "NEXT_DAY", 8);
            addChoice("เล่นมือถือ 📱", -5, 0, "NEXT_DAY", 3);
            addChoice("ตะโกนแซว 📣", 3, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("ICE_D2_C")) {
            currentScene = new Dialogue("เธอวิ่งต่อโดยไม่สนใจ...");
            addChoice("เดินตาม 🚶", 3, 0, "NEXT_DAY", 5);
            addChoice("กลับก่อน 👋", -5, 0, "NEXT_DAY", 3);
            addChoice("ตะโกนบ่น 😫", -10, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runIceDay3() {
        setBackgroundImage("/com/jibgirl/asset/clubroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 3: ห้องชมรม --- 🏀\nไอซ์: \"มาแข่งบาสกับฉันเถอะ!\"");
            addChoice("งั้นเริ่มเลย! 🏀🔥", 10, 0, "ICE_D3_A", 20);
            addChoice("ออมมือให้ 😜", 3, 0, "ICE_D3_B", 15);
            addChoice("ไม่เอา ขี้เกียจ 🙄", -5, 0, "ICE_D3_C", 3);
        } else if (gameState.equals("ICE_D3_A")) {
            currentScene = new Dialogue("แข่งกันเหงื่อซึม เธอสนุกมาก!");
            addChoice("อยู่ช่วยจนเสร็จ ✨", 10, 0, "NEXT_DAY", 20);
            addChoice("ช่วยบางส่วน 👍", 3, 0, "NEXT_DAY", 10);
            addChoice("บ่นว่าเสียเวลา 🙄", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("ICE_D3_B")) {
            currentScene = new Dialogue("บรรยากาศค่อนข้างเงียบ...");
            addChoice("ถามเพิ่ม? ❓", 5, 0, "NEXT_DAY", 5);
            addChoice("เงียบต่อไป... 🤐", -3, 0, "NEXT_DAY", 3);
            addChoice("เปลี่ยนเรื่อง 💬", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("ICE_D3_C")) {
            currentScene = new Dialogue("เธอขมวดคิ้วเล็กน้อย...");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("ยืนยันไม่ช่วย 🙅", -10, 0, "NEXT_DAY", 5);
            addChoice("ลุกออก 👋", -10, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runIceDay4() {
        setBackgroundImage("/com/jibgirl/asset/cafe.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 4: คาเฟ่ --- ☕\nไอซ์: \"บางทีฉันก็เหนื่อยนะ ที่ต้องเก่งตลอด...\"");
            addChoice("เธอไม่ต้องเก่งตลอดก็ได้ ✨", 10, 0, "ICE_D4_A", 5);
            addChoice("ก็เธอเลือกเอง 🙄", -5, 0, "ICE_D4_B", 5);
            addChoice("เงียบฟัง 🎧", 3, 0, "ICE_D4_C", 5);
        } else if (gameState.equals("ICE_D4_A")) {
            currentScene = new Dialogue("เธอมองคุณนานขึ้น...");
            addChoice("ฉันอยากเป็นที่พักให้เธอ 💖", 10, 0, "NEXT_DAY", 8);
            addChoice("ยิ้มให้ 😊", 3, 0, "NEXT_DAY", 5);
            addChoice("หัวเราะกลบ 😅", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("ICE_D4_B")) {
            currentScene = new Dialogue("ไอซ์ดูจะผิดหวังเล็กน้อย...");
            addChoice("แก้คำพูด 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("ยืนยันคำเดิม 🤐", -10, 0, "NEXT_DAY", 5);
            addChoice("เปลี่ยนเรื่อง 😶", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("ICE_D4_C")) {
            currentScene = new Dialogue("บรรยากาศอ่อนโยนขึ้น...");
            addChoice("จับมือเธอ 🤝💖", 10, 0, "NEXT_DAY", 8);
            addChoice("บอกว่าเข้าใจนะ ✨", 3, 0, "NEXT_DAY", 5);
            addChoice("ล้อเล่นนิดหน่อย 😋", -5, 0, "NEXT_DAY", 5);
        }
        updateUI();
    }

    private void runIceDay5() {
        setBackgroundImage("/com/jibgirl/asset/basketball.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 5: สนามบาสเกตบอล --- 🏀💖\n'เซนต์': \"ฉันอยากอยู่ข้างเธอเสมอ ไม่ว่าผลจะเป็นยังไง\"");
            // [FIX] Set stamina cost to 0 for Day 5 to prevent button disabling
            addChoice("จริงจังลึกซึ้ง 💎💖", 10, 0, "FINAL", 0);
            addChoice("พูดแบบเขิน ๆ 😳💓", 3, 0, "FINAL", 0);
            addChoice("พูดท้าทาย 🔥", -5, 0, "FINAL", 0);
        }
        updateUI();
    }

    // ======================
    // KANOM ROUTE DAYS
    // ======================

    private void runKanomDay1() {
        setBackgroundImage("/com/jibgirl/asset/artroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 1: ห้องศิลปะ --- 🖌️\nขนม: \"ช่วยระบายสีตรงนี้หน่อยสิ!\"");
            addChoice("ระบายสวยมาก ✨", 10, 0, "KANOM_D1_A", 10);
            addChoice("เละเทะไปหน่อย 😅", 3, 0, "KANOM_D1_B", 8);
            addChoice("ไม่ช่วยขี้เกียจ 🙄", -5, 0, "KANOM_D1_C", 5);
        } else if (gameState.equals("KANOM_D1_A")) {
            currentScene = new Dialogue("ขนม: \"ว้าว นายก็มีฝีมือนี่นา!\"");
            addChoice("เพราะคนในรูปน่ารัก ✨", 10, 0, "NEXT_DAY", 5);
            addChoice("ก็พอได้ 👍", 3, 0, "NEXT_DAY", 5);
            addChoice("กล้องดี 📸", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("KANOM_D1_B")) {
            currentScene = new Dialogue("ขนมพยักหน้าเบา ๆ...");
            addChoice("ชมเพิ่ม ✨", 5, 0, "NEXT_DAY", 5);
            addChoice("เงียบ 🤐", -3, 0, "NEXT_DAY", 3);
            addChoice("แซวแรง 😜", -5, 0, "NEXT_DAY", 3);
        } else if (gameState.equals("KANOM_D1_C")) {
            currentScene = new Dialogue("ขนมทำหน้ามุ่ยเล็กน้อย...");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("โทษเธอ 🙄", -10, 0, "NEXT_DAY", 3);
            addChoice("หัวเราะใส่ 😂", -5, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runKanomDay2() {
        setBackgroundImage("/com/jibgirl/asset/classroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 2: ห้องเรียน --- 📚\nขนม: \"ทำการบ้านให้เราหน่อยน้าาาา!\"");
            addChoice("ทำให้สิครับ ✨", 10, 0, "KANOM_D2_A", 15);
            addChoice("สอนให้ทำเองดีกว่า 👍", 5, 0, "KANOM_D2_A", 10);
            addChoice("ไม่ทำ! 🙄", -5, 0, "KANOM_D2_C", 3);
        } else if (gameState.equals("KANOM_D2_A")) {
            currentScene = new Dialogue("ขนมซาบซึ้งใจสุด ๆ พยายามตั้งใจเรียนไปพร้อมกับคุณ...");
            addChoice("อาสาไปด้วย 🚗💨", 10, 0, "NEXT_DAY", 20);
            addChoice("ช่วยวางแผน ✨", 5, 0, "NEXT_DAY", 10);
            addChoice("เปลี่ยนใจ 😶", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("KANOM_D2_B")) {
            currentScene = new Dialogue("ขนมเล่าแผนของเธอต่อ...");
            addChoice("ชมไอเดีย 👍", 3, 0, "NEXT_DAY", 5);
            addChoice("เล่นมือถือ 📱", -5, 0, "NEXT_DAY", 3);
            addChoice("ล้อเล่น 😋", 0, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("KANOM_D2_C")) {
            currentScene = new Dialogue("ขนมเงียบไป...");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("ยืนยันคำเดิม 🤐", -10, 0, "NEXT_DAY", 3);
            addChoice("เดินหนี 🚶", -10, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runKanomDay3() {
        setBackgroundImage("/com/jibgirl/asset/clubroom.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue("--- DAY 3: ห้องชมรม --- 🎒\nขนม: \"ไปหาของกินในตู้เย็นชมรมกัน!\"");
            addChoice("เอาสิ! หิวพอดี 🌭", 10, 0, "KANOM_D3_A", 5);
            addChoice("เดี๋ยวโดนว่านะ 🤐", 3, 0, "KANOM_D3_B", 5);
            addChoice("ไม่ไป 🙄", -5, 0, "KANOM_D3_C", 3);
        } else if (gameState.equals("KANOM_D3_A")) {
            currentScene = new Dialogue("เราแอบกินขนมด้วยกันอย่างสนุกสนาน...");
            addChoice("อยู่ปลอบจนยิ้ม 😊💖", 10, 0, "NEXT_DAY", 8);
            addChoice("เล่าเรื่องขำ ๆ 😂", 3, 0, "NEXT_DAY", 8);
            addChoice("รีบกลับ 👋", -5, 0, "NEXT_DAY", 3);
        } else if (gameState.equals("KANOM_D3_B")) {
            currentScene = new Dialogue("เธอยังดูไม่ค่อยสบายใจนัก...");
            addChoice("ช่วยคิดคอนเทนต์ 💡", 5, 0, "NEXT_DAY", 10);
            addChoice("เงียบ 🤐", -3, 0, "NEXT_DAY", 3);
            addChoice("เปลี่ยนเรื่อง 💬", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("KANOM_D3_C")) {
            currentScene = new Dialogue("ขนมหน้าเครียดกว่าเดิม...");
            addChoice("แก้คำพูด 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("ย้ำคำเดิม 🤐", -10, 0, "NEXT_DAY", 3);
            addChoice("ล้อแรง 😜", -10, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runKanomDay4() {
        setBackgroundImage("/com/jibgirl/asset/cafe.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 4: คาเฟ่ --- 🍰\nขนม: \"สตรอว์เบอร์รี่โทสต์ร้านนี้อร่อยมากเลย ลองกินดูสิ!\"");
            addChoice("ป้อนหน่อยสิครับ ✨😳", 10, 0, "KANOM_D4_A", 5);
            addChoice("กินเองก็ได้ 🍴", 3, 0, "KANOM_D4_B", 5);
            addChoice("ปกติ 😐", -5, 0, "KANOM_D4_C", 5);
        } else if (gameState.equals("KANOM_D4_A")) {
            currentScene = new Dialogue("ขนมหน้าแดงจัด... \"อึ๋ย... ก็ได้ ก้ากกกก\"");
            addChoice("จับมือ 🤝💖", 10, 0, "NEXT_DAY", 8);
            addChoice("ยิ้มเขิน 😊😳", 3, 0, "NEXT_DAY", 5);
            addChoice("เปลี่ยนเรื่อง 😶", -5, 0, "NEXT_DAY", 5);
        } else if (gameState.equals("KANOM_D4_B")) {
            currentScene = new Dialogue("ขนมยิ้มกว้าง...");
            addChoice("พูดเพิ่ม ✨", 5, 0, "NEXT_DAY", 5);
            addChoice("เงียบ 🤐", -3, 0, "NEXT_DAY", 3);
            addChoice("แซวแรง 😜", -5, 0, "NEXT_DAY", 3);
        } else if (gameState.equals("KANOM_D4_C")) {
            currentScene = new Dialogue("เธอดูเศร้าลงเล็กน้อย...");
            addChoice("ขอโทษ 🥺", 3, 0, "NEXT_DAY", 8);
            addChoice("ยืนยันคำเดิม 🤐", -10, 0, "NEXT_DAY", 3);
            addChoice("หัวเราะใส่ 😂", -5, 0, "NEXT_DAY", 3);
        }
        updateUI();
    }

    private void runKanomDay5() {
        setBackgroundImage("/com/jibgirl/asset/basketball.png");
        if (gameState.equals("START")) {
            currentScene = new Dialogue(
                    "--- DAY 5: สนามบาสเกตบอล --- 🏀✨\n'เซนต์': \"วันนี้มันเป็นวันที่ดีที่สุดเลย เพราะได้อยู่กับเธอ\"");
            addChoice("พูดตรง ๆ 💎💖", 10, 0, "FINAL", 0);
            addChoice("พูดเขิน ๆ 😳💓", 3, 0, "FINAL", 0);
            addChoice("พูดเล่น 🍬", -5, 0, "FINAL", 0);
        }
        updateUI();
    }

    private void updateUI() {
        dayLabel.setText("DAY " + day);
        moneyLabel.setText("💰 " + String.format("%,d", player.getMoney()));
        affectionBar.setValue(player.getAffection());

        // Update stamina bar with warning colors
        staminaBar.setValue(staminaManager.getStamina());

        // Color is handled by setStamina(true) and NeonProgressBar gradient

        dialogueArea.setText(currentScene.getQuestion());
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        private Image characterImage;

        public BackgroundPanel(String fileName) {
            setBackgroundImage(fileName);
        }

        public void setBackgroundImage(String fileName) {
            try {
                java.net.URL imgURL = getClass().getResource(fileName);
                if (imgURL != null) {
                    backgroundImage = new ImageIcon(imgURL).getImage();
                    repaint();
                }
            } catch (Exception e) {
            }
        }

        public void setCharacterImage(String fileName) {
            try {
                java.net.URL imgURL = getClass().getResource(fileName);
                if (imgURL != null) {
                    characterImage = new ImageIcon(imgURL).getImage();
                    repaint();
                } else {
                    characterImage = null;
                    repaint();
                }
            } catch (Exception e) {
                characterImage = null;
                repaint();
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

            if (characterImage != null) {
                // Draw character centered at the bottom
                int charWidth = 450; // Larger for gameplay
                int charHeight = 630;
                int x = (getWidth() - charWidth) / 2;
                int y = getHeight() - charHeight + 50; // Slightly lower
                g.drawImage(characterImage, x, y, charWidth, charHeight, this);
            }
        }
    }
}
