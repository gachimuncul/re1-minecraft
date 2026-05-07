package com.yourname.RPGCraft.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.yourname.RPGCraft.accessory.AccessoryItem;
import com.yourname.RPGCraft.accessory.AccessoryLevelData;
import com.yourname.RPGCraft.accessory.IrisSignetAbility;
import com.yourname.RPGCraft.accessory.LegendaryItemProgressionService;
import com.yourname.RPGCraft.item.ModItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class RPGCraftCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("rpgcraft_iris")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            IrisSignetAbility.activate(player);
                            return 1;
                        })
        );

        dispatcher.register(
                Commands.literal("rpgcraft_item_xp")
                        .then(Commands.literal("iris_signet")
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            int amount = IntegerArgumentType.getInteger(context, "amount");

                                            addItemExperience(player, ModItems.IRIS_SIGNET, amount);

                                            return 1;
                                        })
                                )
                        )
        );
    }

    private static void addItemExperience(ServerPlayer player, AccessoryItem item, int amount) {
        if (!LegendaryItemProgressionService.isUpgradeable(item)) {
            player.sendSystemMessage(Component.literal("§7This item cannot be upgraded."));
            return;
        }

        boolean leveledUp = LegendaryItemProgressionService.addExperience(player, item, amount);
        AccessoryLevelData data = LegendaryItemProgressionService.getProgressionData(player, item);

        if (data == null) {
            player.sendSystemMessage(Component.literal("§cNo progression data found."));
            return;
        }

        if (leveledUp) {
            player.sendSystemMessage(Component.literal(
                    "§9Iris Signet grew stronger. Level: §b" + data.getLevel() + "§9/" + data.getMaxLevel()
            ));
        } else {
            player.sendSystemMessage(Component.literal(
                    "§7Iris Signet gained §b" + amount + "§7 experience."
            ));
        }

        if (data.isMaxLevel()) {
            player.sendSystemMessage(Component.literal("§6Iris Signet is at maximum power."));
        } else {
            player.sendSystemMessage(Component.literal(
                    "§7Progress: §b" + data.getExperience() + "§7/§b" + data.getRequiredExperience()
            ));
        }
    }
}