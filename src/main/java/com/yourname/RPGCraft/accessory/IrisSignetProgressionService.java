package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.item.ModItems;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.PlayerCharacterDataManager;
import com.yourname.RPGCraft.stat.DerivedStatCalculator;
import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatModifier;
import net.minecraft.world.entity.player.Player;

public class IrisSignetProgressionService {

    public static final String IRIS_SIGNET_SOURCE = "rpgcraft:iris_signet";
    public static final String IRIS_SIGNET_RANDOM_SOURCE = "rpgcraft:iris_signet_random_bonus";

    public static void refreshForPlayer(Player player) {
        CharacterData data = PlayerCharacterDataManager.get(player);

        boolean hasIrisSignet = data.getAccessoryInventory().getAll().stream()
                .anyMatch(item -> item == ModItems.IRIS_SIGNET);

        if (!hasIrisSignet) {
            removeOldModifiers(data.getStats());
            DerivedStatCalculator.applyDerivedStats(data.getStats());
            return;
        }

        int ringLevel = IrisSignetData.get(player.getUUID()).getLevel();

        refreshModifiers(data.getStats(), ringLevel);
    }

    public static void refreshModifiers(StatContainer stats, int ringLevel) {
        removeOldModifiers(stats);

        for (StatModifier modifier : IrisSignetScaling.getStatModifiersForLevel(ringLevel)) {
            stats.addModifier(modifier);
        }

        DerivedStatCalculator.applyDerivedStats(stats);
    }

    public static void removeOldModifiers(StatContainer stats) {
        stats.removeModifiersBySource(IRIS_SIGNET_SOURCE);
        stats.removeModifiersBySource(IRIS_SIGNET_RANDOM_SOURCE);
    }
}