package com.yourname.RPGCraft.client;

import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.CharacterDataProvider;
import net.minecraft.client.Minecraft;

public class ClientCharacterState {

    private static CharacterData cachedData;

    public static CharacterData getCharacterData() {
        if (cachedData == null) {
            String playerName = Minecraft.getInstance().player != null
                    ? Minecraft.getInstance().player.getName().getString()
                    : "Unknown";

            cachedData = CharacterDataProvider.getTestData(playerName);
        }

        return cachedData;
    }

    public static void reset() {
        cachedData = null;
    }
}