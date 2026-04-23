package com.yourname.RPGCraft;

import com.yourname.RPGCraft.item.ModItemGroups;
import com.yourname.RPGCraft.item.ModItems;
import net.fabricmc.api.ModInitializer;

public class RPGCraft implements ModInitializer {
    public static final String MOD_ID = "rpgcraft";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModItemGroups.initialize();
        System.out.println("RPGCraft initialized");
    }
}