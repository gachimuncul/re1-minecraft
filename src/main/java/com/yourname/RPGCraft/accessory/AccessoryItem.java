package com.yourname.RPGCraft.accessory;

import com.yourname.RPGCraft.item.ModItems;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.PlayerCharacterDataManager;
import com.yourname.RPGCraft.stat.StatModifier;
import com.yourname.RPGCraft.stat.StatModifierType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

public class AccessoryItem extends Item {

    private final AccessoryType accessoryType;
    private final AccessoryRarity rarity;
    private final List<StatModifier> modifiers;

    public AccessoryItem(
            AccessoryType accessoryType,
            AccessoryRarity rarity,
            List<StatModifier> modifiers,
            Properties properties
    ) {
        super(properties);
        this.accessoryType = accessoryType;
        this.rarity = rarity;
        this.modifiers = modifiers;
    }

    public AccessoryType getAccessoryType() {
        return accessoryType;
    }

    public AccessoryRarity getRarity() {
        return rarity;
    }

    public List<StatModifier> getModifiers() {
        return modifiers;
    }

    @Override
    public InteractionResult use(Level level, Player user, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        CharacterData data = PlayerCharacterDataManager.get(user);

        boolean equipped = AccessoryService.equipFirstAvailableSlot(
                data.getAccessoryInventory(),
                data.getStats(),
                this
        );

        if (!equipped) {
            user.sendSystemMessage(Component.literal("No free accessory slot."));
            return InteractionResult.FAIL;
        }

        if (this == ModItems.IRIS_SIGNET) {
            IrisSignetProgressionService.refreshForPlayer(user);
        }

        ItemStack stack = user.getItemInHand(hand);

        if (!user.getAbilities().instabuild) {
            stack.shrink(1);
        }

        user.sendSystemMessage(Component.literal("Accessory equipped."));
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(
            ItemStack itemStack,
            TooltipContext context,
            TooltipDisplay display,
            Consumer<Component> builder,
            TooltipFlag tooltipFlag
    ) {
        builder.accept(Component.literal(getTypeText())
                .withStyle(Style.EMPTY.withColor(0xFF9E8F73)));

        builder.accept(Component.literal("Rarity: " + rarity.getDisplayName())
                .withStyle(Style.EMPTY.withColor(rarity.getColor())));

        if (this == ModItems.IRIS_SIGNET) {
            appendIrisProgressionTooltip(builder);
        }

        builder.accept(Component.literal(" "));

        List<StatModifier> tooltipModifiers = modifiers;

        if (this == ModItems.IRIS_SIGNET) {
            AccessoryLevelData data = getClientIrisDataSafe();
            int level = data == null ? 1 : data.getLevel();
            tooltipModifiers = IrisSignetScaling.getStatModifiersForLevel(level);
        }

        if (tooltipModifiers.isEmpty()) {
            builder.accept(Component.literal("No bonuses")
                    .withStyle(Style.EMPTY.withColor(0xFF8C8C8C)));
        } else {
            for (StatModifier modifier : tooltipModifiers) {
                builder.accept(Component.literal(formatModifier(modifier))
                        .withStyle(Style.EMPTY.withColor(0xFFD8C9A8)));
            }
        }
    }

    private void appendIrisProgressionTooltip(Consumer<Component> builder) {
        AccessoryLevelData data = getClientIrisDataSafe();

        if (data == null) {
            builder.accept(Component.literal("Level: ?/5")
                    .withStyle(Style.EMPTY.withColor(0xFF6FA8FF)));
            return;
        }

        builder.accept(Component.literal("Level: " + data.getLevel() + "/" + data.getMaxLevel())
                .withStyle(Style.EMPTY.withColor(0xFF6FA8FF)));

        if (data.isMaxLevel()) {
            builder.accept(Component.literal("Fully awakened")
                    .withStyle(Style.EMPTY.withColor(0xFFE0A84F)));
        } else {
            builder.accept(Component.literal("Exp: " + data.getExperience() + "/" + data.getRequiredExperience())
                    .withStyle(Style.EMPTY.withColor(0xFF9E8F73)));
        }
    }

    private AccessoryLevelData getClientIrisDataSafe() {
        try {
            Class<?> clazz = Class.forName("com.yourname.RPGCraft.client.ClientTooltipData");
            Object result = clazz.getMethod("getIrisSignetData").invoke(null);

            if (result instanceof AccessoryLevelData data) {
                return data;
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    private String getTypeText() {
        return switch (accessoryType) {
            case RING -> "Accessory · Ring";
            case AMULET -> "Accessory · Amulet";
            case BRACELET -> "Accessory · Bracelet";
            case CHARM -> "Accessory · Charm";
        };
    }

    private String formatModifier(StatModifier modifier) {
        String statName = formatStatName(modifier.getStatType());

        if (modifier.getModifierType() == StatModifierType.FLAT) {
            return modifier.getValue() >= 0
                    ? "+" + modifier.getValue() + " " + statName
                    : modifier.getValue() + " " + statName;
        }

        if (modifier.getModifierType() == StatModifierType.PERCENT) {
            return modifier.getValue() >= 0
                    ? "+" + modifier.getValue() + "% " + statName
                    : modifier.getValue() + "% " + statName;
        }

        return modifier.getValue() + " " + statName;
    }

    private String formatStatName(com.yourname.RPGCraft.stat.StatType statType) {
        return switch (statType) {
            case STRENGTH -> "Strength";
            case AGILITY -> "Agility";
            case INTELLIGENCE -> "Intellect";
            case VITALITY -> "Vitality";
            case MAX_HP -> "Max Health";
            case MAX_MANA -> "Max Mana";
            case MAX_STAMINA -> "Max Stamina";
        };
    }

    @Override
    public Component getName(ItemStack stack) {
        if (rarity == AccessoryRarity.LEGENDARY) {
            return super.getName(stack)
                    .copy()
                    .withStyle(Style.EMPTY
                            .withColor(rarity.getColor())
                            .withBold(true));
        }

        if (rarity == AccessoryRarity.EPIC) {
            return super.getName(stack)
                    .copy()
                    .withStyle(Style.EMPTY
                            .withColor(rarity.getColor())
                            .withItalic(true));
        }

        return super.getName(stack)
                .copy()
                .withStyle(Style.EMPTY.withColor(rarity.getColor()));
    }
}