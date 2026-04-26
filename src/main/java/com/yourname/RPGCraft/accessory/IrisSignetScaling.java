package com.yourname.RPGCraft.accessory;

public class IrisSignetScaling {

    public static int getBonusVitality(int level) {
        return 4 + level;
    }

    public static int getStrengthPercent(int level) {
        return 15 + level * 2;
    }

    public static int getMaxHpPercent(int level) {
        return 10 + level;
    }

    public static int getGhostDamageBonus(int level) {
        return level * 2;
    }

    public static int getGhostHealthBonus(int level) {
        return level * 5;
    }
}