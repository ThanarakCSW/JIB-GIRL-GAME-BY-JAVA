package com.jibgirl.controller;

/**
 * StaminaManager handles all stamina-related logic.
 * Manages stamina consumption and checks for stamina availability.
 */
public class StaminaManager {
    private int stamina;
    private static final int MAX_STAMINA = 100;
    private static final int MIN_STAMINA = 0;

    public StaminaManager() {
        this.stamina = MAX_STAMINA;
    }

    /**
     * Decrease stamina by the given amount
     * 
     * @param amount stamina cost to deduct
     * @return true if stamina was successfully deducted, false if stamina was
     *         insufficient
     */
    public boolean consumeStamina(int amount) {
        if (amount <= 0) {
            return true; // ถ้าค่าใช้เป็น 0 หรือติดลบ จะประสบความสำเร็จเสมอ
        }

        if (stamina >= amount) {
            stamina -= amount;
            return true;
        }
        return false; // สมดุลชีวิตไม่พอ
    }

    /**
     * Check if there's enough stamina without consuming it
     */
    public boolean hasEnoughStamina(int amount) {
        return stamina >= amount;
    }

    /**
     * Restore stamina to maximum (at end of day)
     */
    public void restoreStamina() {
        stamina = MAX_STAMINA;
    }

    /**
     * Set stamina to specific value
     */
    public void setStamina(int value) {
        stamina = Math.max(MIN_STAMINA, Math.min(MAX_STAMINA, value));
    }

    /**
     * Add stamina (up to max)
     */
    public void addStamina(int amount) {
        stamina = Math.min(MAX_STAMINA, stamina + amount);
    }

    /**
     * Get current stamina value
     */
    public int getStamina() {
        return stamina;
    }

    /**
     * Get max stamina
     */
    public int getMaxStamina() {
        return MAX_STAMINA;
    }

    /**
     * Get stamina as percentage (0-100)
     */
    public int getStaminaPercentage() {
        return (stamina * 100) / MAX_STAMINA;
    }

    /**
     * Check if stamina is critical (low)
     */
    public boolean isStaminaCritical() {
        return stamina <= 30;
    }

    /**
     * Check if stamina is depleted
     */
    public boolean isStaminaDepleted() {
        return stamina <= 0;
    }
}
