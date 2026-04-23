package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.stat.StatModifier;
import net.minecraft.world.item.Item;

import java.util.List;

public class AccessoryItem extends Item {

    private final AccessoryType accessoryType;
    private final List<StatModifier> modifiers;

    public AccessoryItem(
            AccessoryType accessoryType,
            List<StatModifier> modifiers,
            Properties properties
    ) {
        super(properties);
        this.accessoryType = accessoryType;
        this.modifiers = modifiers;
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public List<StatModifier> getModifiers() {
        return modifiers;
    }
}