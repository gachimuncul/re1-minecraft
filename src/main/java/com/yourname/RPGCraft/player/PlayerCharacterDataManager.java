package com.yourname.RPGCraft.player;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCharacterDataManager {

    private static final Map<UUID, CharacterData> DATA = new HashMap<>();

    public static CharacterData get(Player player) {
        return DATA.computeIfAbsent(
                player.getUUID(),
                uuid -> CharacterDataProvider.getTestData(player.getName().getString())
        );
    }

    public static void reset(Player player) {
        DATA.remove(player.getUUID());
    }
}