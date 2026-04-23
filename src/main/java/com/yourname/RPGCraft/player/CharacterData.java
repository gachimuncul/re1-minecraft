package com.yourname.RPGCraft.player;

import com.yourname.RPGCraft.accessory.AccessoryInventory;
import com.yourname.RPGCraft.skilltree.SkillTreeState;
import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatType;

public class CharacterData {

    private final String name;
    private final String rank;

    private final StatContainer stats;
    private final ProgressionData progression;
    private final SkillTreeState skillTreeState;
    private final AccessoryInventory accessoryInventory;

    private final int currentHp;
    private final int maxHp;

    private final int currentMana;
    private final int maxMana;

    private final int currentStamina;
    private final int maxStamina;

    public CharacterData(
            String name,
            String rank,
            StatContainer stats,
            ProgressionData progression,
            SkillTreeState skillTreeState,
            AccessoryInventory accessoryInventory,
            int currentHp,
            int maxHp,
            int currentMana,
            int maxMana,
            int currentStamina,
            int maxStamina
    ) {
        this.name = name;
        this.rank = rank;
        this.stats = stats;
        this.progression = progression;
        this.skillTreeState = skillTreeState;
        this.accessoryInventory = accessoryInventory;
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

    public String getRank() {
        return rank;
    }

    public StatContainer getStats() {
        return stats;
    }

    public ProgressionData getProgression() {
        return progression;
    }

    public SkillTreeState getSkillTreeState() {
        return skillTreeState;
    }

    public AccessoryInventory getAccessoryInventory() {
        return accessoryInventory;
    }

    public int getLevel() {
        return progression.getLevel();
    }

    public int getExperience() {
        return progression.getExperience();
    }

    public int getRequiredExperience() {
        return LevelSystem.getRequiredExperienceForLevel(getLevel());
    }

    public int getPassivePoints() {
        return progression.getPassivePoints();
    }

    public int getStat(StatType statType) {
        return stats.getStat(statType);
    }

    public int getBaseStat(StatType statType) {
        return stats.getBaseStat(statType);
    }

    public int getFlatModifierStat(StatType statType) {
        return stats.getFlatModifierTotal(statType);
    }

    public int getPercentModifierStat(StatType statType) {
        return stats.getPercentModifierTotal(statType);
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
}