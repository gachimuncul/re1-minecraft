package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.stat.DerivedStatCalculator;
import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatModifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class AccessoryService {

    public static boolean equipFirstAvailableSlot(AccessoryInventory inventory, StatContainer stats, AccessoryItem item) {
        AccessorySlotType slot = AccessorySlotResolver.findFreeSlot(inventory, item.getAccessoryType());
        if (slot == null) {
            return false;
        }

        inventory.set(slot, item);
        applyModifiers(stats, item);
        DerivedStatCalculator.applyDerivedStats(stats);
        return true;
    }

    public static void unequip(AccessoryInventory inventory, StatContainer stats, AccessorySlotType slot) {
        AccessoryItem removed = inventory.remove(slot);
        if (removed == null) {
            return;
        }

        String source = getAccessorySourceId(removed);
        stats.removeModifiersBySource(source);
        DerivedStatCalculator.applyDerivedStats(stats);
    }

    private static void applyModifiers(StatContainer stats, AccessoryItem item) {
        for (StatModifier modifier : item.getModifiers()) {
            stats.addModifier(modifier);
        }
    }

    public static String getAccessorySourceId(AccessoryItem item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        return id == null ? "unknown_accessory" : id.toString();
    }
}