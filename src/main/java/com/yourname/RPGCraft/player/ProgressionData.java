package com.yourname.RPGCraft.player;

public class ProgressionData {

    private int level;
    private int experience;
    private int passivePoints;

    public ProgressionData(int level, int experience, int passivePoints) {
        this.level = level;
        this.experience = experience;
        this.passivePoints = passivePoints;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getPassivePoints() {
        return passivePoints;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setPassivePoints(int passivePoints) {
        this.passivePoints = passivePoints;
    }

    public void addPassivePoints(int amount) {
        this.passivePoints += amount;
    }

    public boolean spendPassivePoint() {
        if (passivePoints <= 0) {
            return false;
        }

        passivePoints--;
        return true;
    }
}