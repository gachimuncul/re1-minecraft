package com.yourname.RPGCraft.player;

import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatType;

public class ProgressionService {

    public static void addExperience(ProgressionData progression, StatContainer stats, int amount) {
        progression.setExperience(progression.getExperience() + amount);

        while (progression.getExperience() >= LevelSystem.getRequiredExperienceForLevel(progression.getLevel())) {
            int required = LevelSystem.getRequiredExperienceForLevel(progression.getLevel());
            progression.setExperience(progression.getExperience() - required);
            levelUp(progression, stats);
        }
    }

    private static void levelUp(ProgressionData progression, StatContainer stats) {
        progression.setLevel(progression.getLevel() + 1);
        progression.addPassivePoints(1);

        stats.addStat(StatType.STRENGTH, 1);
        stats.addStat(StatType.AGILITY, 1);
        stats.addStat(StatType.INTELLIGENCE, 1);
        stats.addStat(StatType.VITALITY, 1);
    }
}