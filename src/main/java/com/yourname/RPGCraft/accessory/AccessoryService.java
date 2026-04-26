package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.stat.DerivedStatCalculator;
import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatModifier;

public class AccessoryService {

    public static boolean equipFirstAvailableSlot(AccessoryInventory inventory, StatContainer stats, AccessoryItem item) {
        AccessorySlotType slot = AccessorySlotResolver.findFreeSlot(inventory, item.getAccessoryType());

        if (slot == null) {
            return false;
        }

        inventory.set(slot, item);

        for (StatModifier modifier : item.getModifiers()) {
            stats.addModifier(modifier);
        }

        DerivedStatCalculator.applyDerivedStats(stats);
        return true;
    }

    public static void unequip(AccessoryInventory inventory, StatContainer stats, AccessorySlotType slot) {
        unequipAndReturn(inventory, stats, slot);
    }

    public static AccessoryItem unequipAndReturn(AccessoryInventory inventory, StatContainer stats, AccessorySlotType slot) {
        AccessoryItem removed = inventory.remove(slot);

        if (removed == null) {
            return null;
        }

        for (StatModifier modifier : removed.getModifiers()) {
            stats.removeModifiersBySource(modifier.getSource());
        }

        DerivedStatCalculator.applyDerivedStats(stats);
        return removed;
    }
}