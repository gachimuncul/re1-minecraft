package com.yourname.RPGCraft.accessory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IrisSignetData {

    private static final Map<UUID, AccessoryLevelData> DATA = new HashMap<>();

    public static AccessoryLevelData get(UUID playerId) {
        return DATA.computeIfAbsent(playerId, id -> new AccessoryLevelData(1, 0));
    }
}