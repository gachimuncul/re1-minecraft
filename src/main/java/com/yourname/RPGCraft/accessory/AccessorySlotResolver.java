package com.yourname.RPGCraft.accessory;

public class AccessorySlotResolver {

    public static AccessorySlotType findFreeSlot(AccessoryInventory inventory, AccessoryType type) {
        return switch (type) {
            case RING -> {
                if (inventory.get(AccessorySlotType.RING_LEFT) == null) yield AccessorySlotType.RING_LEFT;
                if (inventory.get(AccessorySlotType.RING_RIGHT) == null) yield AccessorySlotType.RING_RIGHT;
                yield null;
            }
            case AMULET -> inventory.get(AccessorySlotType.AMULET) == null ? AccessorySlotType.AMULET : null;
            case BRACELET -> {
                if (inventory.get(AccessorySlotType.BRACELET_LEFT) == null) yield AccessorySlotType.BRACELET_LEFT;
                if (inventory.get(AccessorySlotType.BRACELET_RIGHT) == null) yield AccessorySlotType.BRACELET_RIGHT;
                yield null;
            }
            case CHARM -> inventory.get(AccessorySlotType.CHARM) == null ? AccessorySlotType.CHARM : null;
        };
    }
}