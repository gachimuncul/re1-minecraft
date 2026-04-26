package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.item.ModItems;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.PlayerCharacterDataManager;
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
        int level = ringData.getLevel();

        player.sendSystemMessage(Component.literal("§9Iris opens the graves."));

        player.playSound(
                SoundEvents.SOUL_ESCAPE.value(),
                0.6f,
                0.35f
        );

        summonGhosts(player, level);
    }

    private static void summonGhosts(Player player, int ringLevel) {
        for (int i = 0; i < 2; i++) {

            net.minecraft.world.entity.monster.Stray stray =
                    new net.minecraft.world.entity.monster.Stray(
                            net.minecraft.world.entity.EntityType.STRAY,
                            player.level()
                    );

            stray.setPos(
                    player.getX() + (i == 0 ? 1.5D : -1.5D),
                    player.getY(),
                    player.getZ()
            );

            stray.setCustomName(Component.literal("§9Fallen Warrior"));
            stray.setCustomNameVisible(true);

            // ❄ Визуал (призрачность)
            stray.setGlowingTag(true);

            // 💀 Усиление от уровня кольца
            double bonusHealth = IrisSignetScaling.getGhostHealthBonus(ringLevel);
            double newMaxHealth = stray.getMaxHealth() + bonusHealth;

            var healthAttr = stray.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH);
            if (healthAttr != null) {
                healthAttr.setBaseValue(newMaxHealth);
            }

            stray.setHealth((float) newMaxHealth);

            var damageAttr = stray.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
            if (damageAttr != null) {
                damageAttr.setBaseValue(
                        damageAttr.getBaseValue() + IrisSignetScaling.getGhostDamageBonus(ringLevel)
                );
            }

            // 🧊 Частицы при появлении
            player.level().addParticle(
                    net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,
                    stray.getX(),
                    stray.getY() + 1.0D,
                    stray.getZ(),
                    0.0D,
                    0.05D,
                    0.0D
            );

            player.level().addFreshEntity(stray);
        }
    }
}