package com.yourname.RPGCraft.player;

import com.yourname.RPGCraft.accessory.AccessoryInventory;
import com.yourname.RPGCraft.accessory.AccessoryItem;
import com.yourname.RPGCraft.accessory.AccessoryService;
import com.yourname.RPGCraft.item.ModItems;
import com.yourname.RPGCraft.skilltree.SkillTreeService;
import com.yourname.RPGCraft.skilltree.SkillTreeState;
import com.yourname.RPGCraft.stat.DerivedStatCalculator;
import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatModifier;
import com.yourname.RPGCraft.stat.StatModifierType;
import com.yourname.RPGCraft.stat.StatType;

public class CharacterDataProvider {

    public static CharacterData getTestData(String playerName) {
        StatContainer stats = new StatContainer();

        // Базовые статы
        stats.setStat(StatType.STRENGTH, 5);
        stats.setStat(StatType.AGILITY, 5);
        stats.setStat(StatType.INTELLIGENCE, 5);
        stats.setStat(StatType.VITALITY, 5);

        // Временные тестовые внешние модификаторы
        stats.addModifier(new StatModifier(
                StatType.STRENGTH,
                StatModifierType.FLAT,
                2,
                "starter_ring"
        ));

        stats.addModifier(new StatModifier(
                StatType.VITALITY,
                StatModifierType.FLAT,
                3,
                "blessing"
        ));

        stats.addModifier(new StatModifier(
                StatType.STRENGTH,
                StatModifierType.PERCENT,
                20,
                "warrior_aura"
        ));

        // Прокачка
        ProgressionData progression = new ProgressionData(3, 40, 2);

        // Дерево навыков
        SkillTreeState skillTreeState = new SkillTreeState();

        SkillTreeService.unlockNode("strength_small", skillTreeState, progression, stats);
        SkillTreeService.unlockNode("strength_notable", skillTreeState, progression, stats);

        // Аксессуары
        AccessoryInventory accessoryInventory = new AccessoryInventory();

        AccessoryService.equipFirstAvailableSlot(
                accessoryInventory,
                stats,
                (AccessoryItem) ModItems.RING_OF_STRENGTH
        );

        AccessoryService.equipFirstAvailableSlot(
                accessoryInventory,
                stats,
                (AccessoryItem) ModItems.AMULET_OF_WISDOM
        );

        AccessoryService.equipFirstAvailableSlot(
                accessoryInventory,
                stats,
                (AccessoryItem) ModItems.BRACELET_OF_VIGOR
        );

        // Производные характеристики
        DerivedStatCalculator.applyDerivedStats(stats);

        int maxHp = stats.getStat(StatType.MAX_HP);
        int maxMana = stats.getStat(StatType.MAX_MANA);
        int maxStamina = stats.getStat(StatType.MAX_STAMINA);

        return new CharacterData(
                playerName,
                "Wanderer",
                stats,
                progression,
                skillTreeState,
                accessoryInventory,
                maxHp,
                maxHp,
                maxMana,
                maxMana,
                maxStamina,
                maxStamina
        );
    }
}