package com.yourname.RPGCraft.accessory;

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

        ItemStack stack = user.getItemInHand(hand);

        if (!user.getAbilities().instabuild) {
            stack.shrink(1);
        }

        user.sendSystemMessage(Component.literal("Accessory equipped."));
        return InteractionResult.SUCCESS;
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
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        builder.accept(Component.literal(getTypeText())
                .withStyle(Style.EMPTY.withColor(0xFF9E8F73)));

        builder.accept(Component.literal("Rarity: " + rarity.getDisplayName())
                .withStyle(Style.EMPTY.withColor(rarity.getColor())));

        builder.accept(Component.literal(" "));

        if (modifiers.isEmpty()) {
            builder.accept(Component.literal("No bonuses")
                    .withStyle(Style.EMPTY.withColor(0xFF8C8C8C)));
        } else {
            for (StatModifier modifier : modifiers) {
                builder.accept(Component.literal(formatModifier(modifier))
                        .withStyle(Style.EMPTY.withColor(0xFFD8C9A8)));
            }
        }

        builder.accept(Component.literal(" "));
        builder.accept(Component.literal("Right-click to equip")
                .withStyle(Style.EMPTY.withColor(0xFFB8915E)));
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