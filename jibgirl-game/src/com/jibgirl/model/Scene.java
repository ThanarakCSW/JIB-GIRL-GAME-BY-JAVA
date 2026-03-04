package com.jibgirl.model;

/**
 * Scene represents a location/area in the game with dialogue and character
 * context.
 * Each scene has a name, description, and dialogue tree associated with it.
 */
public class Scene {
    private String name; // ชื่อฉาก เช่น "ห้องเรียน", "ลานชมวิว"
    private String description; // คำอธิบายลักษณะฉาก
    private String backgroundAsset; // พาธของรูปพื้นหลัง
    private Dialogue dialogue; // บทสนทนาของฉากนี้
    private String characterName; // ชื่อตัวละคร ที่อยู่ในฉากนี้
    private String sceneType; // ประเภท: "NORMAL", "CONFESSION", "SHOP"

    public Scene(String name, String description, String backgroundAsset,
            String characterName, String sceneType) {
        this.name = name;
        this.description = description;
        this.backgroundAsset = backgroundAsset;
        this.characterName = characterName;
        this.sceneType = sceneType;
        this.dialogue = null;
    }

    // Constructor without background asset
    public Scene(String name, String description, String characterName, String sceneType) {
        this(name, description, "", characterName, sceneType);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackgroundAsset() {
        return backgroundAsset;
    }

    public void setBackgroundAsset(String backgroundAsset) {
        this.backgroundAsset = backgroundAsset;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    /**
     * Check if this is a confession scene
     */
    public boolean isConfessionScene() {
        return "CONFESSION".equals(sceneType);
    }

    @Override
    public String toString() {
        return "Scene{" +
                "name='" + name + '\'' +
                ", character='" + characterName + '\'' +
                ", type='" + sceneType + '\'' +
                '}';
    }
}
