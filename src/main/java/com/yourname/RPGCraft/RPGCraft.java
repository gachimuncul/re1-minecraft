package com.yourname.RPGCraft;

import com.yourname.RPGCraft.command.RPGCraftCommands;
import com.yourname.RPGCraft.entity.ModEntities;
import com.yourname.RPGCraft.entity.custom.IrisGhostStrayEntity;
import com.yourname.RPGCraft.item.ModItemGroups;
import com.yourname.RPGCraft.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class RPGCraft implements ModInitializer {
    public static final String MOD_ID = "rpgcraft";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModItemGroups.initialize();
        ModEntities.initialize();

        FabricDefaultAttributeRegistry.register(
                ModEntities.IRIS_GHOST_STRAY,
                IrisGhostStrayEntity.createAttributes()
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            RPGCraftCommands.register(dispatcher);
        });

        System.out.println("RPGCraft initialized");
    }
}