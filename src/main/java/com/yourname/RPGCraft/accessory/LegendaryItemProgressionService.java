package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.item.ModItems;
import net.minecraft.world.entity.player.Player;

public class LegendaryItemProgressionService {

    public static boolean addExperience(Player player, AccessoryItem item, int amount) {
        if (item == ModItems.IRIS_SIGNET) {
            AccessoryLevelData data = IrisSignetData.get(player.getUUID());

            int oldLevel = data.getLevel();
            boolean leveledUp = data.addExperience(amount);
            int newLevel = data.getLevel();

            IrisSignetProgressionService.refreshForPlayer(player);

            return leveledUp || newLevel > oldLevel;
        }

        return false;
    }

    public static AccessoryLevelData getProgressionData(Player player, AccessoryItem item) {
        if (item == ModItems.IRIS_SIGNET) {
            return IrisSignetData.get(player.getUUID());
        }

        return null;
    }

    public static boolean isUpgradeable(AccessoryItem item) {
        return item == ModItems.IRIS_SIGNET;
    }
}