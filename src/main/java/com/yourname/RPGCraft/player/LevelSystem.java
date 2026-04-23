package com.yourname.RPGCraft.player;

public class LevelSystem {

    public static int getRequiredExperienceForLevel(int level) {
        return 100 + (level - 1) * 50;
    }
}