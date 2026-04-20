package com.yourname.RPGCraft.player;

import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatType;

public class CharacterData {
    private final String name;
    private final int level;
    private final String rank;
    private final StatContainer stats;

    private final int currentHp;
    private final int maxHp;

    private final int currentMana;
    private final int maxMana;

    private final int currentStamina;
    private final int maxStamina;

    public CharacterData(
            String name,
            int level,
            String rank,
            StatContainer stats,
            int currentHp,
            int maxHp,
            int currentMana,
            int maxMana,
            int currentStamina,
            int maxStamina
    ) {
        this.name = name;
        this.level = level;
        this.rank = rank;
        this.stats = stats;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
        this.currentMana = currentMana;
        this.maxMana = maxMana;
        this.currentStamina = currentStamina;
        this.maxStamina = maxStamina;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getRank() {
        return rank;
    }

    public StatContainer getStats() {
        return stats;
    }

    public int getStat(StatType statType) {
        return stats.getStat(statType);
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getCurrentStamina() {
        return currentStamina;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public int getBaseStat(StatType statType) {
        return stats.getBaseStat(statType);
    }

    public int getPercentModifierStat(StatType statType) {
        return stats.getPercentModifierTotal(statType);
    }

    public int getFlatModifierStat(StatType statType) {
        return stats.getFlatModifierTotal(statType);
    }
}