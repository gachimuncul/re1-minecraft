package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.entity.ModEntities;
import com.yourname.RPGCraft.entity.custom.IrisGhostRole;
import com.yourname.RPGCraft.entity.custom.IrisGhostStrayEntity;
import com.yourname.RPGCraft.item.ModItems;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.PlayerCharacterDataManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class IrisSignetAbility {

    private static long lastUseTick = -99999;

    public static void activate(Player player) {
        CharacterData data = PlayerCharacterDataManager.get(player);

        boolean hasRing = data.getAccessoryInventory().getAll().stream()
                .anyMatch(item -> item == ModItems.IRIS_SIGNET);

        if (!hasRing) {
            player.sendSystemMessage(Component.literal("§7The ring is silent."));
            return;
        }

        if (player.tickCount - lastUseTick < 20 * 30) {
            player.sendSystemMessage(Component.literal("§7The dead do not answer so soon."));
            return;
        }

        lastUseTick = player.tickCount;

        AccessoryLevelData ringData = IrisSignetData.get(player.getUUID());
        int ringLevel = ringData.getLevel();

        player.sendSystemMessage(Component.literal("§9Iris opens the graves."));

        player.playSound(
                SoundEvents.SOUL_ESCAPE.value(),
                0.6f,
                0.35f
        );

        summonGhosts(player, ringLevel);
    }

    private static void summonGhosts(Player player, int ringLevel) {
        for (int i = 0; i < 2; i++) {
            IrisGhostStrayEntity ghost = new IrisGhostStrayEntity(
                    ModEntities.IRIS_GHOST_STRAY,
                    player.level()
            );

            double xOffset = i == 0 ? 1.5D : -1.5D;

            ghost.snapTo(
                    player.getX() + xOffset,
                    player.getY(),
                    player.getZ(),
                    player.getYRot(),
                    player.getXRot()
            );

            ghost.setup(
                    player,
                    ringLevel,
                    i == 0 ? IrisGhostRole.ARCHER : IrisGhostRole.WARRIOR
            );

            player.level().addFreshEntity(ghost);

            player.level().addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    ghost.getX(),
                    ghost.getY() + 1.0D,
                    ghost.getZ(),
                    0.0D,
                    0.05D,
                    0.0D
            );
        }
    }
}