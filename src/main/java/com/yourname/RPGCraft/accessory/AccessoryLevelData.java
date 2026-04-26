package com.yourname.RPGCraft.accessory;

public class AccessoryLevelData {

    private int level;
    private int experience;

    public AccessoryLevelData(int level, int experience) {
        this.level = level;
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getRequiredExperience() {
        return 100 + (level - 1) * 75;
    }

    public void addExperience(int amount) {
        experience += amount;

        while (experience >= getRequiredExperience()) {
            experience -= getRequiredExperience();
            level++;
        }
    }
}