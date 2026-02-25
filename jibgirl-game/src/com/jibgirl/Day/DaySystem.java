import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DaySystem extends JFrame {

    private int day = 1;
    private final int MAX_DAY = 5;

    private boolean activityDone = false; // ตรวจสอบว่าทำกิจกรรมแล้วหรือยัง

    private JLabel dayLabel;
    private JLabel statusLabel;
    private JButton activityButton;
    private JButton backRoomButton;
    private JButton nextDayButton;

    public DaySystem() {

        setTitle("ระบบวัน (5 วัน)");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== แสดงวัน =====
        dayLabel = new JLabel("Day : " + day + " / " + MAX_DAY, SwingConstants.CENTER);
        dayLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        dayLabel.setBorder(new EmptyBorder(15, 0, 10, 0));

        // ===== สถานะ =====
        statusLabel = new JLabel("สถานะ: ยังไม่ได้ทำกิจกรรม", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // ===== ปุ่มทำกิจกรรม =====
        activityButton = new JButton("ทำกิจกรรมประจำวัน");
        activityButton.addActionListener(e -> doActivity());

        // ===== ปุ่มกลับห้อง =====
        backRoomButton = new JButton("กลับห้อง");
        backRoomButton.setEnabled(false);
        backRoomButton.addActionListener(e -> backToRoom());

        // ===== ปุ่มวันถัดไป =====
        nextDayButton = new JButton("ไปวันถัดไป");
        nextDayButton.setEnabled(false);
        nextDayButton.addActionListener(e -> nextDay());

        // ===== Panel ปุ่ม =====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 30, 10, 30));
        buttonPanel.add(activityButton);
        buttonPanel.add(backRoomButton);
        buttonPanel.add(nextDayButton);

        add(dayLabel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ===== ทำกิจกรรม =====
    private void doActivity() {
        activityDone = true;
        statusLabel.setText("สถานะ: ทำกิจกรรมแล้ว ✅");
        activityButton.setEnabled(false);
        backRoomButton.setEnabled(true);
    }

    // ===== กลับห้อง =====
    private void backToRoom() {
        if (activityDone) {
            statusLabel.setText("สถานะ: กลับถึงห้องแล้ว 🏠");
            nextDayButton.setEnabled(true);
            backRoomButton.setEnabled(false);
        }
    }

    // ===== ไปวันถัดไป =====
    private void nextDay() {

        if (day < MAX_DAY) {
            day++;
            activityDone = false;

            dayLabel.setText("Day : " + day + " / " + MAX_DAY);
            statusLabel.setText("สถานะ: ยังไม่ได้ทำกิจกรรม");

            activityButton.setEnabled(true);
            nextDayButton.setEnabled(false);

        } else {
            JOptionPane.showMessageDialog(this,
                    "ครบ 5 วันแล้ว 💖\nจบเกม!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);

            nextDayButton.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        new DaySystem();
    }
}