package com.jibgirl.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class UIUtils {

    // ===== Modern/Premium Colors =====
    public static final Color TEAL_DARK = new Color(0, 77, 64);
    public static final Color TEAL_LIGHT = new Color(0, 150, 136);
    public static final Color ACCENT_NEON = new Color(0, 255, 196);
    public static final Color GLASS_WHITE = new Color(255, 255, 255, 40);
    public static final Color GLASS_BORDER = new Color(255, 255, 255, 80);

    // ===== "Cute" Pastel Colors =====
    public static final Color PASTEL_PINK = new Color(255, 209, 220);
    public static final Color PASTEL_PURPLE = new Color(224, 187, 228);
    public static final Color PASTEL_MINT = new Color(191, 252, 198);
    public static final Color PASTEL_PEACH = new Color(255, 223, 186);
    public static final Color PASTEL_BLUE = new Color(174, 198, 255);
    public static final Color BORDER_SOFT = new Color(255, 255, 255, 150);

    public static class ModernPanel extends JPanel {
        private int round = 40;
        private Color bgColor = GLASS_WHITE;

        public ModernPanel(int round) {
            this.round = round;
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        public void setBackground(Color color) {
            this.bgColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), round, round));

            // Border
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(GLASS_BORDER);
            g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, round, round));

            g2.dispose();
        }
    }

    public static class CuteCard extends JPanel {
        private int round = 30;
        private Color color = Color.WHITE;
        private boolean isHovered = false;

        public CuteCard(Color color) {
            this.color = color;
            setOpaque(false);
            setLayout(new BorderLayout());
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isHovered) {
                g2.translate(0, -5); // Pop up effect
                // Shadow
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth(), getHeight(), round, round));
            }

            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), round, round));

            // Soft border
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(BORDER_SOFT);
            g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, round, round));

            g2.dispose();
        }
    }

    public static class PremiumButton extends JButton {
        private Color colorStart = TEAL_DARK;
        private Color colorEnd = TEAL_LIGHT;
        private boolean isHovered = false;
        private boolean isCute = false;

        public PremiumButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Tahoma", Font.BOLD, 16));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        public void setCute(boolean cute) {
            this.isCute = cute;
            if (cute) {
                this.colorStart = PASTEL_PINK;
                this.colorEnd = PASTEL_PURPLE;
                setForeground(new Color(100, 50, 50));
            } else {
                this.colorStart = TEAL_DARK;
                this.colorEnd = TEAL_LIGHT;
                setForeground(Color.WHITE);
            }
        }

        public void setChoiceStyle(boolean choice) {
            if (choice) {
                this.colorStart = new Color(255, 100, 100);
                this.colorEnd = new Color(255, 50, 50);
                setForeground(Color.WHITE);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient;
            if (isHovered) {
                if (isCute) {
                    gradient = new GradientPaint(0, 0, PASTEL_PURPLE, getWidth(), getHeight(), PASTEL_MINT);
                } else {
                    gradient = new GradientPaint(0, 0, TEAL_LIGHT, getWidth(), getHeight(), ACCENT_NEON);
                }
                g2.translate(0, -2);
            } else {
                gradient = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
            }

            g2.setPaint(gradient);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));

            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static class NeonProgressBar extends JProgressBar {
        private boolean isCute = false;

        public NeonProgressBar() {
            setOpaque(false);
            setBorderPainted(false);
            setForeground(ACCENT_NEON);
            setBackground(new Color(255, 255, 255, 20));
        }

        public void setCute(boolean cute) {
            this.isCute = cute;
            if (cute) {
                setForeground(PASTEL_PINK);
            } else {
                setForeground(ACCENT_NEON);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            double percent = getPercentComplete();
            int progressWidth = (int) (width * percent);

            // Track
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, width, height, height, height));

            // Progress Fill
            Paint paint;
            if (isCute) {
                paint = new GradientPaint(0, 0, PASTEL_PINK, progressWidth, 0, PASTEL_PURPLE);
            } else {
                paint = new GradientPaint(0, 0, TEAL_LIGHT, progressWidth, 0, ACCENT_NEON);
            }
            g2.setPaint(paint);
            g2.fill(new RoundRectangle2D.Double(0, 0, progressWidth, height, height, height));

            // Glow / Border
            g2.setColor(new Color(255, 255, 255, 100));
            if (!isCute) {
                g2.setColor(new Color(ACCENT_NEON.getRed(), ACCENT_NEON.getGreen(), ACCENT_NEON.getBlue(), 100));
            }
            g2.setStroke(new BasicStroke(3f));
            g2.draw(new RoundRectangle2D.Double(1, 1, progressWidth - 2, height - 2, height, height));

            g2.dispose();
        }
    }
}
