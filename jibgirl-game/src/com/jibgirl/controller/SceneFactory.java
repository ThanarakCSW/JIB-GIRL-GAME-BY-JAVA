package com.jibgirl.controller;

import com.jibgirl.model.Scene;
import com.jibgirl.model.Dialogue;
import com.jibgirl.model.Choice;

/**
 * SceneFactory creates and manages all scenes in the game.
 * Handles scene creation for different characters and scene types.
 */
public class SceneFactory {

        /**
         * Create a classroom confession scene for a specific character
         */
        public static Scene createClassroomConfession(String characterName) {
                Scene scene = new Scene(
                                "ห้องเรียน (สารภาพรัก)",
                                "ห้องเรียนเงียบๆ หลังเลิกเรียน พบแต่เธออย่างเดียว...",
                                characterName,
                                "CONFESSION");

                Dialogue confession = createConfessionDialogue(characterName);
                scene.setDialogue(confession);

                return scene;
        }

        /**
         * Create a Bad End scene when stamina is depleted but affection is too low
         */
        public static Scene createBadEndScene(String characterName) {
                Scene scene = new Scene(
                                "บ้าน (จบบริบูรณ์ - Bad End)",
                                "คุณกลับบ้านด้วยความเหนื่อยล้า... และความสัมพันธ์ที่จืดจาง",
                                characterName,
                                "BAD_END");

                Dialogue dialogue = new Dialogue(
                                "💔 " + characterName + ": \"เราคงเป็นได้แค่เพื่อนกันเท่านั้นแหละ...\"\n\n" +
                                                "คุณไม่ได้ใช้เวลามากพอที่จะทำให้เธอประทับใจ\n" +
                                                "สมดุลชีวิตของคุณหมดลง และเธอก็เดินจากไปทิ้งไว้เพียงความเงียบงัน...");

                // Choice to return to title
                dialogue.addChoice(new Choice(
                                "ฉันเข้าใจแล้ว... (กลับหน้าหลัก)",
                                0,
                                0,
                                "TITLE", // Use special keyword for GUI to handle
                                0));

                scene.setDialogue(dialogue);
                return scene;
        }

        /**
         * Create confession dialogue specific to each character
         */
        private static Dialogue createConfessionDialogue(String characterName) {
                Dialogue dialogue = new Dialogue("");

                switch (characterName.toLowerCase()) {
                        case "maprang":
                                dialogue = new Dialogue(
                                                "💕 มะราง: คุณเข้ามาหาฉัน... " +
                                                                "\n\nตลอดเวลาที่ผ่านมา ฉันจำไม่ได้ว่าตัวเองตกหลุมรักคุณได้ยังไง "
                                                                +
                                                                "\nแต่ทุกครั้งที่เห็นหน้าคุณ ใจฉันก็เต้นเร็วขึ้น... " +
                                                                "\n\nฉันรักคุณ 💖");
                                // Choice 1: ยอมรับความรัก
                                dialogue.addChoice(new Choice(
                                                "ฉันก็รักเธอ... 💕",
                                                50, // affectionImpact: +50
                                                0, // cost: 0
                                                "มะราง ยิ้มอย่างสุขสันต์และกอดคุณไว้แนบชิด... ♡",
                                                0 // staminaCost: 0 (confession is free)
                                ));
                                // Choice 2: ปฏิเสธ
                                dialogue.addChoice(new Choice(
                                                "ฉันขอโทษ... ฉันไม่รู้สึกแบบนั้น",
                                                -30, // affectionImpact: -30
                                                0, // cost: 0
                                                "มะราง ดวงตาเศร้าลง... เธอพยักหน้ายอมรับความเป็นจริง",
                                                0 // staminaCost: 0
                                ));
                                break;

                        case "ice":
                                dialogue = new Dialogue(
                                                "❄️ ไอศ์: คุณมาหาฉันเพื่อ... อะไร? " +
                                                                "\n\n(เธอหันหน้ามอง ดวงตาเต็มไปด้วยความหวาดระแหว่ง) " +
                                                                "\n\nตลอดแต่งเรียน ฉันซ่อนความรู้สึกของฉันไว้ " +
                                                                "แต่เมื่อได้รู้จักคุณ ทุกสิ่งก็เปลี่ยนไป... " +
                                                                "\n\nฉัน... รักคุณแล้ว ❄️💙");
                                // Choice 1: ยอมรับ
                                dialogue.addChoice(new Choice(
                                                "ฉันก็รักเธออีกด้วย 💙",
                                                50,
                                                0,
                                                "ไอศ์อาบแดดใจ ที่ครั้งแรกที่เธอยิ้มอย่างชัดเจน...",
                                                0));
                                // Choice 2: ปฏิเสธ
                                dialogue.addChoice(new Choice(
                                                "สงสารเธอจริงๆ แต่ฉันหลงใหลคนอื่น",
                                                -30,
                                                0,
                                                "ไอศ์กลับไปสภาพเดิม เยือกเย็นไม่พูดคำใดอีก...",
                                                0));
                                break;

                        case "kanom":
                                dialogue = new Dialogue(
                                                "🧡 แนม: วันนี้พบคุณตรงนี้จริง ๆ ด้วย! " +
                                                                "\n\n(เธอทำพฤติกรรมสดใส แต่บ้านใจสั่นเทพลั่ว) " +
                                                                "\n\nตั้งแต่ครั้งแรกที่คุณช่วยฉัน ฉันก็รู้สึกพิเศษกับคุณ "
                                                                +
                                                                "\nวันนี้ ฉนอยากให้คุณรู้... ฉันรักคุณค่ะ 🧡");
                                // Choice 1: ยอมรับ
                                dialogue.addChoice(new Choice(
                                                "ฉันก็รักเธอ 🧡",
                                                50,
                                                0,
                                                "แนมวิ่งมาโอบกอดคุณด้วยความสุขแสนใหญ่...",
                                                0));
                                // Choice 2: ปฏิเสธ
                                dialogue.addChoice(new Choice(
                                                "เสียใจนะ ฉันคิดว่าเราแค่เพื่อน",
                                                -30,
                                                0,
                                                "แนมกำลังใจหักแตก เธอพยายามยิ้มเพื่อปกปิดความเศร้า...",
                                                0));
                                break;

                        default:
                                dialogue = new Dialogue(
                                                "??? : ฉันมีสิ่งสำคัญที่อยากบอกคุณ... " +
                                                                "\n\nตลอดเวลาที่อยู่ด้วยกัน ฉันค่อยๆ หลงใหลคุณ " +
                                                                "\n\nฉันรักคุณ");
                                dialogue.addChoice(new Choice("ฉันก็รักเธอ", 50, 0, "เธอยิ้มด้วยความสุข", 0));
                                dialogue.addChoice(new Choice("ขอโทษนะ", -30, 0, "เธอเศร้าลง", 0));
                                break;
                }

                return dialogue;
        }

        /**
         * Create a normal scene for a specific day and character
         */
        public static Scene createNormalScene(String characterName, int day, String locationName,
                        String locationDescription) {
                Scene scene = new Scene(
                                locationName,
                                locationDescription,
                                characterName,
                                "NORMAL");

                // Dialogue จะถูกตั้งค่าโดย GameGui เพราะมันขึ้นอยู่กับ day และ choices ก่อนหน้า
                return scene;
        }
}
