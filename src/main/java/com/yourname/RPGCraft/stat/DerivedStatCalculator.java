package com.yourname.RPGCraft.stat;

public class DerivedStatCalculator {

    public static void applyDerivedStats(StatContainer stats) {
        int vitality = stats.getStat(StatType.VITALITY);
        int intelligence = stats.getStat(StatType.INTELLIGENCE);
        int agility = stats.getStat(StatType.AGILITY);

        stats.setStat(StatType.MAX_HP, 20 + vitality * 5);
        stats.setStat(StatType.MAX_MANA, 10 + intelligence * 3);
        stats.setStat(StatType.MAX_STAMINA, 10 + agility * 4);
    }
}