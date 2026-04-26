package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.item.ModItems;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.PlayerCharacterDataManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class AccessoryEffectHandler {

    private static final RandomSource RANDOM = RandomSource.create();

    public static void tick(Player player) {
        if (player == null) {
            return;
        }

        CharacterData data = PlayerCharacterDataManager.get(player);

        if (data == null) {
            return;
        }

        AccessoryInventory inventory = data.getAccessoryInventory();

        for (AccessoryItem item : inventory.getAll()) {
            if (item == null) {
                continue;
            }

            if (item == ModItems.IRIS_SIGNET) {
                applyIrisSignetEffects(player);
            }
        }
    }

    private static void applyIrisSignetEffects(Player player) {
        boolean isClient = player.level().isClientSide();

        // Клиентские визуальные эффекты
        if (isClient) {
            spawnIrisParticles(player);
            playIrisWhispers(player);
            return;
        }

        // Серверные gameplay-эффекты
        applyIrisRegeneration(player);
    }

    private static void spawnIrisParticles(Player player) {
        if (RANDOM.nextFloat() > 0.25f) {
            return;
        }

        double x = player.getX() + (RANDOM.nextDouble() - 0.5D) * 0.8D;
        double y = player.getY() + 0.4D + RANDOM.nextDouble() * 1.2D;
        double z = player.getZ() + (RANDOM.nextDouble() - 0.5D) * 0.8D;

        double velocityX = (RANDOM.nextDouble() - 0.5D) * 0.01D;
        double velocityY = 0.015D + RANDOM.nextDouble() * 0.015D;
        double velocityZ = (RANDOM.nextDouble() - 0.5D) * 0.01D;

        player.level().addParticle(
                ParticleTypes.SMOKE,
                x,
                y,
                z,
                velocityX,
                velocityY,
                velocityZ
        );

        if (RANDOM.nextFloat() < 0.35f) {
            player.level().addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    x,
                    y,
                    z,
                    0.0D,
                    0.01D,
                    0.0D
            );
        }
    }

    private static void playIrisWhispers(Player player) {
        if (RANDOM.nextFloat() > 0.0025f) {
            return;
        }

        player.playSound(
                SoundEvents.SOUL_ESCAPE.value(),
                0.25f,
                0.45f + RANDOM.nextFloat() * 0.35f
        );
    }

    private static void applyIrisRegeneration(Player player) {
        if (player.tickCount % 60 != 0) {
            return;
        }

        if (player.getHealth() >= player.getMaxHealth()) {
            return;
        }

        player.heal(0.5f);
    }
}