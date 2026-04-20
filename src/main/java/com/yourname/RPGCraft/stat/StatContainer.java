package com.yourname.RPGCraft.stat;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StatContainer {

    private final Map<StatType, Integer> baseStats = new EnumMap<>(StatType.class);
    private final List<StatModifier> modifiers = new ArrayList<>();

    public StatContainer() {
        for (StatType statType : StatType.values()) {
            baseStats.put(statType, 0);
        }
    }

    public void setStat(StatType statType, int value) {
        baseStats.put(statType, value);
    }

    public int getBaseStat(StatType statType) {
        return baseStats.getOrDefault(statType, 0);
    }

    public void addStat(StatType statType, int value) {
        baseStats.put(statType, getBaseStat(statType) + value);
    }

    public void addModifier(StatModifier modifier) {
        modifiers.add(modifier);
    }

    public void removeModifiersBySource(String source) {
        modifiers.removeIf(modifier -> modifier.getSource().equals(source));
    }

    public int getFlatModifierTotal(StatType statType) {
        int total = 0;

        for (StatModifier modifier : modifiers) {
            if (modifier.getStatType() == statType && modifier.getModifierType() == StatModifierType.FLAT) {
                total += modifier.getValue();
            }
        }

        return total;
    }

    public int getPercentModifierTotal(StatType statType) {
        int total = 0;

        for (StatModifier modifier : modifiers) {
            if (modifier.getStatType() == statType && modifier.getModifierType() == StatModifierType.PERCENT) {
                total += modifier.getValue();
            }
        }

        return total;
    }

    public int getStat(StatType statType) {
        int base = getBaseStat(statType);
        int flat = getFlatModifierTotal(statType);
        int percent = getPercentModifierTotal(statType);

        double result = (base + flat) * (1.0 + percent / 100.0);
        return (int) Math.round(result);
    }
}