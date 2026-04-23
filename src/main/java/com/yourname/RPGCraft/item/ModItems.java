package com.yourname.RPGCraft.item;

import com.yourname.RPGCraft.RPGCraft;
import com.yourname.RPGCraft.accessory.AccessoryItem;
import com.yourname.RPGCraft.accessory.AccessoryType;
import com.yourname.RPGCraft.stat.StatModifier;
import com.yourname.RPGCraft.stat.StatModifierType;
import com.yourname.RPGCraft.stat.StatType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public class ModItems {

    public static final AccessoryItem RING_OF_STRENGTH = register(
            "ring_of_strength",
            settings -> new AccessoryItem(
                    AccessoryType.RING,
                    List.of(
                            new StatModifier(StatType.STRENGTH, StatModifierType.FLAT, 3, "rpgcraft:ring_of_strength")
                    ),
                    settings
            ),
            new Item.Properties().stacksTo(1)
    );

    public static final AccessoryItem AMULET_OF_WISDOM = register(
            "amulet_of_wisdom",
            settings -> new AccessoryItem(
                    AccessoryType.AMULET,
                    List.of(
                            new StatModifier(StatType.INTELLIGENCE, StatModifierType.FLAT, 4, "rpgcraft:amulet_of_wisdom"),
                            new StatModifier(StatType.MAX_MANA, StatModifierType.PERCENT, 10, "rpgcraft:amulet_of_wisdom")
                    ),
                    settings
            ),
            new Item.Properties().stacksTo(1)
    );

    public static final AccessoryItem BRACELET_OF_VIGOR = register(
            "bracelet_of_vigor",
            settings -> new AccessoryItem(
                    AccessoryType.BRACELET,
                    List.of(
                            new StatModifier(StatType.VITALITY, StatModifierType.FLAT, 2, "rpgcraft:bracelet_of_vigor"),
                            new StatModifier(StatType.MAX_HP, StatModifierType.PERCENT, 5, "rpgcraft:bracelet_of_vigor")
                    ),
                    settings
            ),
            new Item.Properties().stacksTo(1)
    );

    private static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(RPGCraft.MOD_ID, name)
        );

        T item = itemFactory.apply(settings.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }

    public static void initialize() {
    }
}