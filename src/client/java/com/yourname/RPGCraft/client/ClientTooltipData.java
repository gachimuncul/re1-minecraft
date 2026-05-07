package com.yourname.RPGCraft.client;

import com.yourname.RPGCraft.accessory.AccessoryLevelData;
import com.yourname.RPGCraft.accessory.IrisSignetData;
import net.minecraft.client.Minecraft;

public class ClientTooltipData {

    public static AccessoryLevelData getIrisSignetData() {
        if (Minecraft.getInstance().player == null) {
            return null;
        }

        return IrisSignetData.get(Minecraft.getInstance().player.getUUID());
    }
}