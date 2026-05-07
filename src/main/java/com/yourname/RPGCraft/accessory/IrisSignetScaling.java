package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.stat.StatModifier;
import com.yourname.RPGCraft.stat.StatModifierType;
import com.yourname.RPGCraft.stat.StatType;

import java.util.List;

public class IrisSignetScaling {

    public static List<StatModifier> getStatModifiersForLevel(int level) {
        return switch (level) {
            case 1 -> List.of(
                    new StatModifier(StatType.STRENGTH, StatModifierType.PERCENT, 15, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.VITALITY, StatModifierType.FLAT, 4, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.MAX_HP, StatModifierType.PERCENT, 10, "rpgcraft:iris_signet")
            );
            case 2 -> List.of(
                    new StatModifier(StatType.STRENGTH, StatModifierType.PERCENT, 17, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.VITALITY, StatModifierType.FLAT, 5, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.MAX_HP, StatModifierType.PERCENT, 11, "rpgcraft:iris_signet")
            );
            case 3 -> List.of(
                    new StatModifier(StatType.STRENGTH, StatModifierType.PERCENT, 19, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.VITALITY, StatModifierType.FLAT, 6, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.MAX_HP, StatModifierType.PERCENT, 12, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.AGILITY, StatModifierType.FLAT, 2, "rpgcraft:iris_signet_random_bonus")
            );
            case 4 -> List.of(
                    new StatModifier(StatType.STRENGTH, StatModifierType.PERCENT, 21, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.VITALITY, StatModifierType.FLAT, 7, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.MAX_HP, StatModifierType.PERCENT, 13, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.AGILITY, StatModifierType.FLAT, 2, "rpgcraft:iris_signet_random_bonus")
            );
            case 5 -> List.of(
                    new StatModifier(StatType.STRENGTH, StatModifierType.PERCENT, 25, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.VITALITY, StatModifierType.FLAT, 9, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.MAX_HP, StatModifierType.PERCENT, 16, "rpgcraft:iris_signet"),
                    new StatModifier(StatType.AGILITY, StatModifierType.FLAT, 3, "rpgcraft:iris_signet_random_bonus")
            );
            default -> getStatModifiersForLevel(1);
        };
    }

    public static boolean hasPiercingArrows(int level) {
        return level >= 2;
    }

    public static boolean hasRandomBonusStat(int level) {
        return level >= 3;
    }

    public static boolean hasWarriorBerserk(int level) {
        return level >= 4;
    }

    public static boolean hasSummonEmpowerment(int level) {
        return level >= 5;
    }

    public static int getGhostDamageBonus(int level) {
        int bonus = level * 2;

        if (hasSummonEmpowerment(level)) {
            bonus += 6;
        }

        return bonus;
    }

    public static int getGhostHealthBonus(int level) {
        int bonus = level * 5;

        if (hasSummonEmpowerment(level)) {
            bonus += 20;
        }

        return bonus;
    }

    public static int getPiercingLevel(int level) {
        return level >= 2 ? 2 : 0;
    }
}