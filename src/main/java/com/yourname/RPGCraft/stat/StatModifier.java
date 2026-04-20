package com.yourname.RPGCraft.stat;

public class StatModifier {

    private final StatType statType;
    private final StatModifierType modifierType;
    private final int value;
    private final String source;

    public StatModifier(StatType statType, StatModifierType modifierType, int value, String source) {
        this.statType = statType;
        this.modifierType = modifierType;
        this.value = value;
        this.source = source;
    }

    public StatType getStatType() {
        return statType;
    }

    public StatModifierType getModifierType() {
        return modifierType;
    }

    public int getValue() {
        return value;
    }

    public String getSource() {
        return source;
    }
}