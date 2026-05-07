package com.yourname.RPGCraft.accessory;

public class AccessoryLevelData {

    private static final int MAX_LEVEL = 5;

    private int level;
    private int experience;

    public AccessoryLevelData(int level, int experience) {
        this.level = Math.min(level, MAX_LEVEL);
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public boolean isMaxLevel() {
        return level >= MAX_LEVEL;
    }

    public int getRequiredExperience() {
        if (isMaxLevel()) {
            return 0;
        }

        return switch (level) {
            case 1 -> 150;
            case 2 -> 300;
            case 3 -> 600;
            case 4 -> 1000;
            default -> 0;
        };
    }

    public boolean addExperience(int amount) {
        if (isMaxLevel()) {
            return false;
        }

        experience += amount;
        boolean leveledUp = false;

        while (!isMaxLevel() && experience >= getRequiredExperience()) {
            experience -= getRequiredExperience();
            level++;
            leveledUp = true;
        }

        if (isMaxLevel()) {
            experience = 0;
        }

        return leveledUp;
    }
}