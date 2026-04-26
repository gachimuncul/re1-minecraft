package com.yourname.RPGCraft.client;

import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.CharacterDataProvider;
import com.yourname.RPGCraft.player.PlayerCharacterDataManager;
import net.minecraft.client.Minecraft;

public class ClientCharacterState {

    private static CharacterData cachedFallbackData;

    public static CharacterData getCharacterData() {
        Minecraft client = Minecraft.getInstance();

        if (client.player != null) {
            return PlayerCharacterDataManager.get(client.player);
        }

        if (cachedFallbackData == null) {
            cachedFallbackData = CharacterDataProvider.getTestData("Unknown");
        }

        return cachedFallbackData;
    }

    public static void reset() {
        cachedFallbackData = null;

        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            PlayerCharacterDataManager.reset(client.player);
        }
    }
}