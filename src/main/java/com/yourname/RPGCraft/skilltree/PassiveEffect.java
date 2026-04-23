package com.yourname.RPGCraft.skilltree;

public class PassiveEffect {

    private final PassiveEffectType type;
    private final double value;
    private final String source;

    public PassiveEffect(PassiveEffectType type, double value, String source) {
        this.type = type;
        this.value = value;
        this.source = source;
    }

    public PassiveEffectType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public String getSource() {
        return source;
    }
}