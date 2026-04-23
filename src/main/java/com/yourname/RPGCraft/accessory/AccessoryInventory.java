package com.yourname.RPGCraft.accessory;

import java.util.EnumMap;
import java.util.Map;

public class AccessoryInventory {

    private final Map<AccessorySlotType, AccessoryItem> equipped = new EnumMap<>(AccessorySlotType.class);

    public AccessoryItem get(AccessorySlotType slot) {
        return equipped.get(slot);
    }

    public void set(AccessorySlotType slot, AccessoryItem item) {
        equipped.put(slot, item);
    }

    public AccessoryItem remove(AccessorySlotType slot) {
        return equipped.remove(slot);
    }

    public Map<AccessorySlotType, AccessoryItem> getAll() {
        return equipped;
    }
}