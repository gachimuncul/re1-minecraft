package com.yourname.RPGCraft.client.gui;

import com.yourname.RPGCraft.accessory.AccessoryItem;
import com.yourname.RPGCraft.accessory.AccessoryService;
import com.yourname.RPGCraft.accessory.AccessorySlotType;
import com.yourname.RPGCraft.client.ClientCharacterState;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.stat.StatType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class CharacterScreen extends Screen {

    private static final int PANEL_WIDTH = 390;
    private static final int PANEL_HEIGHT = 250;

    private enum CharacterTab {
        CHARACTER,
        ACCESSORIES
    }

    private CharacterTab currentTab = CharacterTab.CHARACTER;

    // Палитра
    private static final int COLOR_OUTER = 0xD014100C;
    private static final int COLOR_BORDER_DARK = 0xFF2A2017;
    private static final int COLOR_BORDER_LIGHT = 0xFF6C5438;

    private static final int COLOR_PARCHMENT_DARK = 0xE02A2218;
    private static final int COLOR_PARCHMENT_MID = 0xE0342A1E;
    private static final int COLOR_PARCHMENT_LIGHT = 0xC0453828;

    private static final int COLOR_HEADER = 0xFF3B2D20;
    private static final int COLOR_SECTION = 0xA035291D;
    private static final int COLOR_LINE = 0xFF5C4730;

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

    @Override
    public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_1) {
            currentTab = CharacterTab.CHARACTER;
            return true;
        }

        if (event.key() == GLFW.GLFW_KEY_2) {
            currentTab = CharacterTab.ACCESSORIES;
            return true;
        }

        return super.keyPressed(event);
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

    private void drawDecorativeCorner(GuiGraphicsExtractor graphics, int x, int y, boolean right, boolean bottom) {
        int hDir = right ? -1 : 1;
        int vDir = bottom ? -1 : 1;

        graphics.fill(x, y, x + hDir * 14, y + vDir, COLOR_BORDER_LIGHT);
        graphics.fill(x, y, x + hDir, y + vDir * 14, COLOR_BORDER_LIGHT);

        graphics.fill(x + hDir * 2, y + vDir * 2, x + hDir * 10, y + vDir * 3, COLOR_LINE);
        graphics.fill(x + hDir * 2, y + vDir * 2, x + hDir * 3, y + vDir * 10, COLOR_LINE);

        graphics.fill(x + hDir * 10, y + vDir * 2, x + hDir * 12, y + vDir * 4, COLOR_LINE);
        graphics.fill(x + hDir * 2, y + vDir * 10, x + hDir * 4, y + vDir * 12, COLOR_LINE);
    }

    private void drawOuterFrame(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2) {
        graphics.fill(x1, y1, x2, y1 + 1, COLOR_BORDER_DARK);
        graphics.fill(x1, y2 - 1, x2, y2, COLOR_BORDER_DARK);
        graphics.fill(x1, y1, x1 + 1, y2, COLOR_BORDER_DARK);
        graphics.fill(x2 - 1, y1, x2, y2, COLOR_BORDER_DARK);

        graphics.fill(x1 + 1, y1 + 1, x2 - 1, y1 + 2, COLOR_BORDER_LIGHT);
        graphics.fill(x1 + 1, y2 - 2, x2 - 1, y2 - 1, COLOR_BORDER_LIGHT);
        graphics.fill(x1 + 1, y1 + 1, x1 + 2, y2 - 1, COLOR_BORDER_LIGHT);
        graphics.fill(x2 - 2, y1 + 1, x2 - 1, y2 - 1, COLOR_BORDER_LIGHT);
    }

    private void drawParchmentBackground(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2) {
        graphics.fill(x1, y1, x2, y2, COLOR_PARCHMENT_DARK);
        graphics.fill(x1 + 6, y1 + 6, x2 - 6, y2 - 6, COLOR_PARCHMENT_MID);
        graphics.fill(x1 + 14, y1 + 14, x2 - 14, y2 - 14, COLOR_PARCHMENT_LIGHT);

        graphics.fill(x1 + 3, y1 + 3, x2 - 3, y1 + 10, 0x5020140E);
        graphics.fill(x1 + 3, y2 - 10, x2 - 3, y2 - 3, 0x5020140E);
        graphics.fill(x1 + 3, y1 + 3, x1 + 10, y2 - 3, 0x5020140E);
        graphics.fill(x2 - 10, y1 + 3, x2 - 3, y2 - 3, 0x5020140E);
    }

    private void drawTabHeader(GuiGraphicsExtractor graphics, int x, int y, String label, boolean active) {
        int bg = active ? 0xFF5A4631 : 0xAA35291D;
        int fg = active ? COLOR_TITLE : COLOR_SUBTEXT;

        graphics.fill(x, y, x + 84, y + 18, bg);
        graphics.fill(x + 1, y + 1, x + 83, y + 17, active ? 0xCC6C5438 : 0xAA2A2017);
        graphics.text(this.font, label, x + 10, y + 5, fg, false);
    }

    private void drawCharacterTab(GuiGraphicsExtractor graphics, CharacterData data, int panelX, int panelY) {
        int leftX = panelX + 14;
        int middleX = panelX + 138;
        int rightX = panelX + 260;

        graphics.fill(panelX + 8, panelY + 34, panelX + 126, panelY + 198, COLOR_SECTION);
        graphics.fill(panelX + 132, panelY + 34, panelX + 254, panelY + 198, COLOR_SECTION);
        graphics.fill(panelX + 256, panelY + 34, panelX + PANEL_WIDTH - 8, panelY + 198, COLOR_SECTION);
        graphics.fill(panelX + 8, panelY + 206, panelX + PANEL_WIDTH - 8, panelY + PANEL_HEIGHT - 8, COLOR_SECTION);

        graphics.fill(panelX + 128, panelY + 34, panelX + 129, panelY + 198, COLOR_LINE);
        graphics.fill(panelX + 254, panelY + 34, panelX + 255, panelY + 198, COLOR_LINE);
        graphics.fill(panelX + 8, panelY + 202, panelX + PANEL_WIDTH - 8, panelY + 203, COLOR_LINE);

        drawSectionTitle(graphics, "Character", leftX, panelY + 40);
        drawLine(graphics, "Name: " + data.getName(), leftX, panelY + 58, COLOR_TEXT);
        drawLine(graphics, "Level: " + data.getLevel(), leftX, panelY + 72, COLOR_TEXT);
        drawLine(graphics, "Exp: " + data.getExperience() + "/" + data.getRequiredExperience(), leftX, panelY + 86, COLOR_TEXT);
        drawLine(graphics, "Points: " + data.getPassivePoints(), leftX, panelY + 100, COLOR_TEXT);
        drawLine(graphics, "Rank: " + data.getRank(), leftX, panelY + 114, COLOR_TEXT);

        drawLine(graphics, "Class: None", leftX, panelY + 138, COLOR_SUBTEXT);
        drawLine(graphics, "Origin: Unknown", leftX, panelY + 152, COLOR_SUBTEXT);
        drawLine(graphics, "Faction: None", leftX, panelY + 166, COLOR_SUBTEXT);

        drawSectionTitle(graphics, "Condition", leftX, panelY + 186);
        drawLine(graphics, "Health: " + data.getCurrentHp() + "/" + data.getMaxHp(), leftX, panelY + 204, COLOR_HP);

        drawSectionTitle(graphics, "Attributes", middleX, panelY + 40);
        drawLine(graphics, formatStatMainLine("Strength", data, StatType.STRENGTH), middleX, panelY + 58, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.STRENGTH), middleX, panelY + 68, COLOR_SUBTEXT);

        drawLine(graphics, formatStatMainLine("Agility", data, StatType.AGILITY), middleX, panelY + 88, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.AGILITY), middleX, panelY + 98, COLOR_SUBTEXT);

        drawLine(graphics, formatStatMainLine("Intellect", data, StatType.INTELLIGENCE), middleX, panelY + 118, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.INTELLIGENCE), middleX, panelY + 128, COLOR_SUBTEXT);

        drawLine(graphics, formatStatMainLine("Vitality", data, StatType.VITALITY), middleX, panelY + 148, COLOR_TEXT);
        drawLine(graphics, formatStatDetailLine(data, StatType.VITALITY), middleX, panelY + 158, COLOR_SUBTEXT);

        drawSectionTitle(graphics, "Derived", rightX, panelY + 40);
        drawLine(graphics, "Max Health: " + data.getStat(StatType.MAX_HP), rightX, panelY + 58, COLOR_HP);
        drawLine(graphics, "From Vitality", rightX, panelY + 68, COLOR_SUBTEXT);

        drawLine(graphics, "Max Mana: " + data.getStat(StatType.MAX_MANA), rightX, panelY + 88, COLOR_MANA);
        drawLine(graphics, "From Intellect", rightX, panelY + 98, COLOR_SUBTEXT);

        drawLine(graphics, "Max Stamina: " + data.getStat(StatType.MAX_STAMINA), rightX, panelY + 118, COLOR_STAMINA);
        drawLine(graphics, "From Agility", rightX, panelY + 128, COLOR_SUBTEXT);

        drawLine(graphics, "Mana: " + data.getCurrentMana() + "/" + data.getMaxMana(), rightX, panelY + 152, COLOR_MANA);
        drawLine(graphics, "Stamina: " + data.getCurrentStamina() + "/" + data.getMaxStamina(), rightX, panelY + 166, COLOR_STAMINA);
    }

    private void drawAccessoryLine(GuiGraphicsExtractor graphics, String label, AccessoryItem item, int x, int y) {
        drawLine(graphics, label + ":", x, y, COLOR_TEXT);

        String value = item == null ? "Empty" : item.getName(item.getDefaultInstance()).getString();
        int color = item == null ? 0xFF8C8C8C : 0xFFB8915E;

        drawLine(graphics, value, x + 120, y, color);
    }

    private void drawAccessoriesTab(GuiGraphicsExtractor graphics, CharacterData data, int panelX, int panelY) {
        int contentX = panelX + 12;
        int contentY = panelY + 36;

        graphics.fill(panelX + 8, panelY + 34, panelX + PANEL_WIDTH - 8, panelY + 198, COLOR_SECTION);
        graphics.fill(panelX + 8, panelY + 206, panelX + PANEL_WIDTH - 8, panelY + PANEL_HEIGHT - 8, COLOR_SECTION);
        graphics.fill(panelX + 8, panelY + 202, panelX + PANEL_WIDTH - 8, panelY + 203, COLOR_LINE);

        drawSectionTitle(graphics, "Accessories", contentX, contentY);

        drawAccessoryLine(graphics, "Ring Left", data.getAccessoryInventory().get(AccessorySlotType.RING_LEFT), contentX, contentY + 22);
        drawAccessoryLine(graphics, "Ring Right", data.getAccessoryInventory().get(AccessorySlotType.RING_RIGHT), contentX, contentY + 38);
        drawAccessoryLine(graphics, "Amulet", data.getAccessoryInventory().get(AccessorySlotType.AMULET), contentX, contentY + 54);
        drawAccessoryLine(graphics, "Bracelet Left", data.getAccessoryInventory().get(AccessorySlotType.BRACELET_LEFT), contentX, contentY + 70);
        drawAccessoryLine(graphics, "Bracelet Right", data.getAccessoryInventory().get(AccessorySlotType.BRACELET_RIGHT), contentX, contentY + 86);
        drawAccessoryLine(graphics, "Charm", data.getAccessoryInventory().get(AccessorySlotType.CHARM), contentX, contentY + 102);

        drawSectionTitle(graphics, "Notes", contentX, contentY + 136);
        drawLine(graphics, "Accessories are separate from armor.", contentX, contentY + 154, COLOR_SUBTEXT);
        drawLine(graphics, "They grant passive bonuses and effects.", contentX, contentY + 168, COLOR_SUBTEXT);
    }

    private AccessorySlotType getAccessorySlotAt(int mouseX, int mouseY, int panelX, int panelY) {
        int contentX = panelX + 12;
        int contentY = panelY + 36;

        int startY = contentY + 22;
        int lineHeight = 16;

        if (mouseX < contentX || mouseX > contentX + 260) {
            return null;
        }

        if (mouseY >= startY && mouseY < startY + lineHeight) {
            return AccessorySlotType.RING_LEFT;
        }

        if (mouseY >= startY + 16 && mouseY < startY + 16 + lineHeight) {
            return AccessorySlotType.RING_RIGHT;
        }

        if (mouseY >= startY + 32 && mouseY < startY + 32 + lineHeight) {
            return AccessorySlotType.AMULET;
        }

        if (mouseY >= startY + 48 && mouseY < startY + 48 + lineHeight) {
            return AccessorySlotType.BRACELET_LEFT;
        }

        if (mouseY >= startY + 64 && mouseY < startY + 64 + lineHeight) {
            return AccessorySlotType.BRACELET_RIGHT;
        }

        if (mouseY >= startY + 80 && mouseY < startY + 80 + lineHeight) {
            return AccessorySlotType.CHARM;
        }

        return null;
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        if (currentTab != CharacterTab.ACCESSORIES) {
            return super.mouseClicked(event, doubleClick);
        }

        int mouseX = (int) event.x();
        int mouseY = (int) event.y();

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        AccessorySlotType slot = getAccessorySlotAt(mouseX, mouseY, panelX, panelY);

        if (slot == null) {
            return super.mouseClicked(event, doubleClick);
        }

        CharacterData data = ClientCharacterState.getCharacterData();

        AccessoryItem removed = AccessoryService.unequipAndReturn(
                data.getAccessoryInventory(),
                data.getStats(),
                slot
        );

        if (removed == null) {
            return true;
        }

        Player player = Minecraft.getInstance().player;

        if (player != null) {
            ItemStack stack = new ItemStack(removed);

            boolean added = player.getInventory().add(stack);

            if (!added) {
                player.drop(stack, false);
            }
        }

        return true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        CharacterData data = ClientCharacterState.getCharacterData();

        int panelX = (this.width - PANEL_WIDTH) / 2;
        int panelY = (this.height - PANEL_HEIGHT) / 2;

        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, COLOR_OUTER);
        drawParchmentBackground(graphics, panelX + 2, panelY + 2, panelX + PANEL_WIDTH - 2, panelY + PANEL_HEIGHT - 2);
        drawOuterFrame(graphics, panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT);

        drawDecorativeCorner(graphics, panelX + 6, panelY + 6, false, false);
        drawDecorativeCorner(graphics, panelX + PANEL_WIDTH - 6, panelY + 6, true, false);
        drawDecorativeCorner(graphics, panelX + 6, panelY + PANEL_HEIGHT - 6, false, true);
        drawDecorativeCorner(graphics, panelX + PANEL_WIDTH - 6, panelY + PANEL_HEIGHT - 6, true, true);

        graphics.fill(panelX + 3, panelY + 3, panelX + PANEL_WIDTH - 3, panelY + 24, COLOR_HEADER);

        drawLine(graphics, "Character Record", panelX + 12, panelY + 8, COLOR_TITLE);
        drawLine(graphics, "RPGCraft", panelX + PANEL_WIDTH - 58, panelY + 8, COLOR_SUBTEXT);

        drawTabHeader(graphics, panelX + 10, panelY + 26, "Character [1]", currentTab == CharacterTab.CHARACTER);
        drawTabHeader(graphics, panelX + 98, panelY + 26, "Accessories [2]", currentTab == CharacterTab.ACCESSORIES);

        if (currentTab == CharacterTab.CHARACTER) {
            drawCharacterTab(graphics, data, panelX, panelY);
        } else {
            drawAccessoriesTab(graphics, data, panelX, panelY);
        }

        drawLine(graphics, "Press K to close", panelX + 14, panelY + 216, COLOR_SUBTEXT);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}