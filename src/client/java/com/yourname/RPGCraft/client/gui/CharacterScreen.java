package com.yourname.RPGCraft.client.gui;

import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.player.CharacterDataProvider;
import com.yourname.RPGCraft.stat.StatType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CharacterScreen extends Screen {

    private static final int PANEL_WIDTH = 390;
    private static final int PANEL_HEIGHT = 250;

    // Цвета под тёмное фэнтези / средневековый вайб
    private static final int COLOR_OUTER = 0xD01A1410;
    private static final int COLOR_INNER = 0xE0262018;
    private static final int COLOR_HEADER = 0xFF3A2C1F;
    private static final int COLOR_SECTION = 0x9034261C;
    private static final int COLOR_LINE = 0xFF5A4631;

    private static final int COLOR_TITLE = 0xFFE6D7B8;
    private static final int COLOR_TEXT = 0xFFD8C9A8;
    private static final int COLOR_SUBTEXT = 0xFF9E8F73;

    private static final int COLOR_HP = 0xFF8FAA72;
    private static final int COLOR_MANA = 0xFF7F97A8;
    private static final int COLOR_STAMINA = 0xFFB8915E;

    public CharacterScreen() {
        super(Component.literal("Character Menu"));
    }

    @Override
    protected void init() {
        super.init();
    }

    private void drawSectionTitle(GuiGraphicsExtractor graphics, String text, int x, int y) {
        graphics.text(this.font, text, x, y, COLOR_TITLE, true);
    }

    private void drawLine(GuiGraphicsExtractor graphics, String text, int x, int y, int color) {
        graphics.text(this.font, text, x, y, color, false);
    }

    private String formatStatMainLine(String label, CharacterData data, StatType statType) {
        return label + ": " + data.getStat(statType);
    }

    private String formatStatDetailLine(CharacterData data, StatType statType) {
        int base = data.getBaseStat(statType);
        int flat = data.getFlatModifierStat(statType);
        int percent = data.getPercentModifierStat(statType);

        if (flat == 0 && percent == 0) {
            return "Base " + base;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("Base ").append(base);

        if (flat != 0) {
            builder.append("   ");
            builder.append(flat > 0 ? "+" : "-").append(Math.abs(flat));
        }

        if (percent != 0) {
            builder.append("   ");
            builder.append(percent > 0 ? "+" : "-").append(Math.abs(percent)).append("%");
        }

        return builder.toString();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        String playerName = Minecraft.getInstance().player != null
                ? Minecraft.getInstance().player.getName().getString()
                : "Unknown";

        CharacterData data = CharacterDataProvider.getTestData(playerName);

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        int leftX = panelX + 14;
        int middleX = panelX + 138;
        int rightX = panelX + 260;

        // ===== ОСНОВНОЙ ФОН =====
        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, COLOR_OUTER);
        graphics.fill(panelX + 2, panelY + 2, panelX + PANEL_WIDTH - 2, panelY + PANEL_HEIGHT - 2, COLOR_INNER);

        // Верхняя полоса
        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + 24, COLOR_HEADER);

        // Секции
        graphics.fill(panelX + 8, panelY + 34, panelX + 126, panelY + 198, COLOR_SECTION);
        graphics.fill(panelX + 132, panelY + 34, panelX + 254, panelY + 198, COLOR_SECTION);
        graphics.fill(panelX + 256, panelY + 34, panelX + PANEL_WIDTH - 8, panelY + 198, COLOR_SECTION);

        // Нижняя полоса
        graphics.fill(panelX + 8, panelY + 206, panelX + PANEL_WIDTH - 8, panelY + PANEL_HEIGHT - 8, COLOR_SECTION);

        // Разделители
        graphics.fill(panelX + 128, panelY + 34, panelX + 129, panelY + 198, COLOR_LINE);
        graphics.fill(panelX + 254, panelY + 34, panelX + 255, panelY + 198, COLOR_LINE);
        graphics.fill(panelX + 8, panelY + 30, panelX + PANEL_WIDTH - 8, panelY + 31, COLOR_LINE);
        graphics.fill(panelX + 8, panelY + 202, panelX + PANEL_WIDTH - 8, panelY + 203, COLOR_LINE);

        // ===== ЗАГОЛОВОК =====
        drawLine(graphics, "Character Record", panelX + 12, panelY + 8, COLOR_TITLE);
        drawLine(graphics, "RPGCraft", panelX + PANEL_WIDTH - 58, panelY + 8, COLOR_SUBTEXT);

        // ===== ЛЕВАЯ КОЛОНКА =====
        drawSectionTitle(graphics, "Character", leftX, panelY + 40);
        drawLine(graphics, "Name: " + data.getName(), leftX, panelY + 58, COLOR_TEXT);
        drawLine(graphics, "Level: " + data.getLevel(), leftX, panelY + 72, COLOR_TEXT);
        drawLine(graphics, "Rank: " + data.getRank(), leftX, panelY + 86, COLOR_TEXT);

        drawLine(graphics, "Class: None", leftX, panelY + 110, COLOR_SUBTEXT);
        drawLine(graphics, "Origin: Unknown", leftX, panelY + 124, COLOR_SUBTEXT);
        drawLine(graphics, "Faction: None", leftX, panelY + 138, COLOR_SUBTEXT);

        drawSectionTitle(graphics, "Condition", leftX, panelY + 162);
        drawLine(graphics, "Health: " + data.getCurrentHp() + "/" + data.getMaxHp(), leftX, panelY + 180, COLOR_HP);

        // ===== СРЕДНЯЯ КОЛОНКА =====
        drawSectionTitle(graphics, "Attributes", middleX, panelY + 40);

        drawLine(graphics, formatStatMainLine("Strength", data, StatType.STRENGTH), middleX, panelY + 58, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.STRENGTH), middleX, panelY + 68, COLOR_SUBTEXT);

        drawLine(graphics, formatStatMainLine("Agility", data, StatType.AGILITY), middleX, panelY + 88, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.AGILITY), middleX, panelY + 98, COLOR_SUBTEXT);

        drawLine(graphics, formatStatMainLine("Intellect", data, StatType.INTELLIGENCE), middleX, panelY + 118, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.INTELLIGENCE), middleX, panelY + 128, COLOR_SUBTEXT);

        drawLine(graphics, formatStatMainLine("Vitality", data, StatType.VITALITY), middleX, panelY + 148, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.VITALITY), middleX, panelY + 158, COLOR_SUBTEXT);

        // ===== ПРАВАЯ КОЛОНКА =====
        drawSectionTitle(graphics, "Derived", rightX, panelY + 40);

        drawLine(graphics, "Max Health: " + data.getStat(StatType.MAX_HP), rightX, panelY + 58, COLOR_HP);
        drawLine(graphics, "From Vitality", rightX, panelY + 68, COLOR_SUBTEXT);

        drawLine(graphics, "Max Mana: " + data.getStat(StatType.MAX_MANA), rightX, panelY + 88, COLOR_MANA);
        drawLine(graphics, "From Intellect", rightX, panelY + 98, COLOR_SUBTEXT);

        drawLine(graphics, "Max Stamina: " + data.getStat(StatType.MAX_STAMINA), rightX, panelY + 118, COLOR_STAMINA);
        drawLine(graphics, "From Agility", rightX, panelY + 128, COLOR_SUBTEXT);

        drawLine(graphics, "Mana: " + data.getCurrentMana() + "/" + data.getMaxMana(), rightX, panelY + 152, COLOR_MANA);
        drawLine(graphics, "Stamina: " + data.getCurrentStamina() + "/" + data.getMaxStamina(), rightX, panelY + 166, COLOR_STAMINA);

        // ===== НИЖНЯЯ СТРОКА =====
        drawLine(graphics, "Press K to close", panelX + 14, panelY + 216, COLOR_SUBTEXT);
        drawLine(graphics, "Prototype character ledger", panelX + PANEL_WIDTH - 126, panelY + 216, 0xFF7B6B55);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}