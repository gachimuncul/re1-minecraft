package com.yourname.RPGCraft.accessory;

public enum AccessoryRarity {
    COMMON("Common", 0xFFB8A98A),
    UNCOMMON("Uncommon", 0xFF8FAA72),
    RARE("Rare", 0xFF7F97A8),
    EPIC("Epic", 0xFFB57EDC),
    LEGENDARY("Legendary", 0xFFE0A84F);

    private final String displayName;
    private final int color;

    AccessoryRarity(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }
}