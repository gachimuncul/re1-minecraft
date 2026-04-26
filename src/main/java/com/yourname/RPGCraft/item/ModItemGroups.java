package com.yourname.RPGCraft.item;

import com.yourname.RPGCraft.RPGCraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab RPGCRAFT_ACCESSORIES = register("rpgcraft_accessories");

    private static CreativeModeTab register(String name) {

        ResourceKey<CreativeModeTab> key = ResourceKey.create(
                Registries.CREATIVE_MODE_TAB,
                Identifier.fromNamespaceAndPath(RPGCraft.MOD_ID, name)
        );

        return Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                key,
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0) // ← ВОТ ЭТО
                        .title(Component.literal("RPGCraft Accessories"))
                        .icon(() -> new ItemStack(ModItems.RING_OF_STRENGTH))
                        .displayItems((parameters, output) -> {
                            output.accept(new ItemStack(ModItems.RING_OF_STRENGTH));
                            output.accept(new ItemStack(ModItems.AMULET_OF_WISDOM));
                            output.accept(new ItemStack(ModItems.BRACELET_OF_VIGOR));
                            output.accept(new ItemStack(ModItems.IRIS_SIGNET));
                        })
                        .build()
        );
    }

    public static void initialize() {}
}