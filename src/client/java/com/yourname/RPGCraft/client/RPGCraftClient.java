package com.yourname.RPGCraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.yourname.RPGCraft.RPGCraft;
import com.yourname.RPGCraft.accessory.AccessoryEffectHandler;
import com.yourname.RPGCraft.client.gui.CharacterScreen;
import com.yourname.RPGCraft.client.gui.SkillTreeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class RPGCraftClient implements ClientModInitializer {

    private static KeyMapping openCharacterMenuKey;
    private static KeyMapping openSkillTreeKey;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(RPGCraft.MOD_ID, "general")
        );

        openCharacterMenuKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.rpgcraft.open_character_menu",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_K,
                        category
                )
        );

        openSkillTreeKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.rpgcraft.open_skill_tree",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_J,
                        category
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                AccessoryEffectHandler.tick(client.player);
            }

            while (openCharacterMenuKey.consumeClick()) {
                if (client.player == null) {
                    continue;
                }

                if (client.screen instanceof CharacterScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new CharacterScreen());
                }
            }

            while (openSkillTreeKey.consumeClick()) {
                if (client.player == null) {
                    continue;
                }

                if (client.screen instanceof SkillTreeScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new SkillTreeScreen());
                }
            }
        });
    }
}