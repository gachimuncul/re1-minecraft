package com.yourname.RPGCraft.client.gui;

import com.yourname.RPGCraft.client.ClientCharacterState;
import com.yourname.RPGCraft.player.CharacterData;
import com.yourname.RPGCraft.skilltree.*;
import com.yourname.RPGCraft.stat.DerivedStatCalculator;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class SkillTreeScreen extends Screen {

    private static final int NODE_RADIUS = 9;
    private static final double MIN_ZOOM = 0.65;
    private static final double MAX_ZOOM = 2.20;
    private static final double ZOOM_STEP = 0.12;

    private CharacterData data;
    private String hoveredNodeId;
    private String selectedNodeId = "start";

    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;
    private double zoom = 1.0;

    private boolean dragging = false;
    private double lastMouseX = 0;
    private double lastMouseY = 0;

    public SkillTreeScreen() {
        super(Component.literal("Skill Tree"));
    }

    @Override
    protected void init() {
        super.init();
        this.data = ClientCharacterState.getCharacterData();
    }

    // ===== ВАЖНО =====
    // Если у тебя в проекте уже заработала другая сигнатура mouseClicked / mouseDragged / mouseScrolled,
    // оставь СВОЮ сигнатуру, а тело методов возьми отсюда.

    @Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean doubleClick) {
        if (data == null) {
            return super.mouseClicked(event, doubleClick);
        }

        double mouseX = event.x();
        double mouseY = event.y();

        SkillNode clickedNode = getNodeAt(mouseX, mouseY);
        if (clickedNode != null) {
            selectedNodeId = clickedNode.getId();

            boolean unlocked = SkillTreeService.unlockNode(
                    clickedNode.getId(),
                    data.getSkillTreeState(),
                    data.getProgression(),
                    data.getStats()
            );

            if (unlocked) {
                DerivedStatCalculator.applyDerivedStats(data.getStats());
            }

            return true;
        }

        dragging = true;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        dragging = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (dragging) {
            cameraOffsetX += dx;
            cameraOffsetY += dy;
            lastMouseX = event.x();
            lastMouseY = event.y();
            return true;
        }

        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        double oldZoom = zoom;

        if (verticalAmount > 0) {
            zoom += ZOOM_STEP;
        } else if (verticalAmount < 0) {
            zoom -= ZOOM_STEP;
        }

        if (zoom < MIN_ZOOM) zoom = MIN_ZOOM;
        if (zoom > MAX_ZOOM) zoom = MAX_ZOOM;

        if (oldZoom != zoom) {
            double treeCenterX = getTreeCenterX();
            double treeCenterY = getTreeCenterY();

            double beforeX = (mouseX - treeCenterX - cameraOffsetX) / oldZoom;
            double beforeY = (mouseY - treeCenterY - cameraOffsetY) / oldZoom;

            double afterX = beforeX * zoom;
            double afterY = beforeY * zoom;

            cameraOffsetX += (beforeX * oldZoom) - afterX;
            cameraOffsetY += (beforeY * oldZoom) - afterY;

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private int getTreeCenterX() {
        return this.width / 2 - 90;
    }

    private int getTreeCenterY() {
        return this.height / 2;
    }

    private int getNodeScreenX(SkillNode node) {
        return (int) Math.round(getTreeCenterX() + cameraOffsetX + node.getX() * zoom);
    }

    private int getNodeScreenY(SkillNode node) {
        return (int) Math.round(getTreeCenterY() + cameraOffsetY + node.getY() * zoom);
    }

    private int getScaledRadius() {
        return Math.max(6, (int) Math.round(NODE_RADIUS * zoom));
    }

    private SkillNode getNodeAt(double mouseX, double mouseY) {
        int radius = getScaledRadius();

        for (SkillNode node : SkillTreeRegistry.NODES.values()) {
            int x = getNodeScreenX(node);
            int y = getNodeScreenY(node);

            double dx = mouseX - x;
            double dy = mouseY - y;

            if (dx * dx + dy * dy <= radius * radius) {
                return node;
            }
        }

        return null;
    }

    private int getNodeFillColor(SkillNode node) {
        if (data.getSkillTreeState().isUnlocked(node.getId())) {
            return switch (node.getType()) {
                case START -> 0xFFB8915E;
                case SMALL -> 0xFFB8915E;
                case NOTABLE -> 0xFFD6B06E;
                case KEYSTONE -> 0xFFE0C48A;
            };
        }

        if (SkillTreeService.canUnlockNode(node.getId(), data.getSkillTreeState(), data.getProgression())) {
            return 0xFF8FAA72;
        }

        return 0xFF5A5A5A;
    }

    private int getNodeBorderColor(SkillNode node) {
        if (node.getId().equals(hoveredNodeId)) {
            return 0xFFE6D7B8;
        }

        if (node.getId().equals(selectedNodeId)) {
            return 0xFFD8C9A8;
        }

        return 0xFF2A2017;
    }

    private void fillCircle(GuiGraphicsExtractor graphics, int cx, int cy, int radius, int color) {
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                if (dx * dx + dy * dy <= radius * radius) {
                    graphics.fill(cx + dx, cy + dy, cx + dx + 1, cy + dy + 1, color);
                }
            }
        }
    }

    private void fillRing(GuiGraphicsExtractor graphics, int cx, int cy, int outerRadius, int innerRadius, int color) {
        for (int dy = -outerRadius; dy <= outerRadius; dy++) {
            for (int dx = -outerRadius; dx <= outerRadius; dx++) {
                int dist = dx * dx + dy * dy;
                if (dist <= outerRadius * outerRadius && dist >= innerRadius * innerRadius) {
                    graphics.fill(cx + dx, cy + dy, cx + dx + 1, cy + dy + 1, color);
                }
            }
        }
    }

    private void drawNode(GuiGraphicsExtractor graphics, SkillNode node) {
        int x = getNodeScreenX(node);
        int y = getNodeScreenY(node);

        int radius = getScaledRadius();
        int fillColor = getNodeFillColor(node);
        int borderColor = getNodeBorderColor(node);

        fillCircle(graphics, x, y, radius + 2, borderColor);
        fillCircle(graphics, x, y, radius, 0xFF1F1712);
        fillCircle(graphics, x, y, Math.max(2, radius - 2), fillColor);

        int innerGlow = switch (node.getType()) {
            case START -> 0xFFE6D7B8;
            case SMALL -> 0xFFD8C9A8;
            case NOTABLE -> 0xFFE0C48A;
            case KEYSTONE -> 0xFFF0D9A8;
        };

        fillCircle(graphics, x, y, Math.max(2, radius / 3), innerGlow);

        if (node.getType() == SkillNodeType.NOTABLE) {
            fillRing(graphics, x, y, radius - 1, radius - 3, 0x662A2017);
        }

        if (node.getType() == SkillNodeType.KEYSTONE) {
            graphics.fill(x - 1, y - radius + 2, x + 1, y + radius - 1, 0xAA2A2017);
            graphics.fill(x - radius + 2, y - 1, x + radius - 1, y + 1, 0xAA2A2017);
        }
    }

    private void drawConnections(GuiGraphicsExtractor graphics) {
        for (SkillNode node : SkillTreeRegistry.NODES.values()) {
            int x1 = getNodeScreenX(node);
            int y1 = getNodeScreenY(node);

            for (String connectionId : node.getConnections()) {
                SkillNode other = SkillTreeRegistry.getNode(connectionId);
                if (other == null) continue;
                if (node.getId().compareTo(other.getId()) > 0) continue;

                int x2 = getNodeScreenX(other);
                int y2 = getNodeScreenY(other);

                int color = 0xFF4A3B2A;

                boolean firstUnlocked = data.getSkillTreeState().isUnlocked(node.getId());
                boolean secondUnlocked = data.getSkillTreeState().isUnlocked(other.getId());

                if (firstUnlocked && secondUnlocked) {
                    color = 0xFFB8915E;
                } else if (firstUnlocked || secondUnlocked) {
                    color = 0xFF7B6545;
                }

                drawLine(graphics, x1, y1, x2, y2, color);
            }
        }
    }

    private void drawLine(GuiGraphicsExtractor graphics, int x1, int y1, int x2, int y2, int color) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        if (steps == 0) {
            graphics.fill(x1, y1, x1 + 1, y1 + 1, color);
            return;
        }

        for (int i = 0; i <= steps; i++) {
            int x = x1 + dx * i / steps;
            int y = y1 + dy * i / steps;
            graphics.fill(x, y, x + 1, y + 1, color);
        }
    }

    private SkillNode getHoveredNode(int mouseX, int mouseY) {
        return getNodeAt(mouseX, mouseY);
    }

    private void drawInfoPanel(GuiGraphicsExtractor graphics, int x, int y, int w, int h) {
        graphics.fill(x, y, x + w, y + h, 0xC0282119);
        graphics.fill(x + 1, y + 1, x + w - 1, y + h - 1, 0xD0362C22);

        graphics.text(this.font, "Node", x + 10, y + 8, 0xFFE6D7B8, true);

        SkillNode node = SkillTreeRegistry.getNode(selectedNodeId);
        if (node == null) {
            graphics.text(this.font, "No node selected", x + 10, y + 28, 0xFFD8C9A8, false);
            return;
        }

        graphics.text(this.font, node.getTitle(), x + 10, y + 28, 0xFFD8C9A8, false);
        graphics.text(this.font, node.getDescription(), x + 10, y + 44, 0xFF9E8F73, false);

        graphics.text(this.font, "Type: " + formatNodeType(node.getType()), x + 10, y + 64, 0xFFB8A98A, false);

        boolean unlocked = data.getSkillTreeState().isUnlocked(node.getId());
        boolean canUnlock = SkillTreeService.canUnlockNode(node.getId(), data.getSkillTreeState(), data.getProgression());

        String stateText;
        int stateColor;

        if (unlocked) {
            stateText = "State: Unlocked";
            stateColor = 0xFFB8915E;
        } else if (canUnlock) {
            stateText = "State: Available";
            stateColor = 0xFF8FAA72;
        } else {
            stateText = "State: Locked";
            stateColor = 0xFF8C8C8C;
        }

        graphics.text(this.font, stateText, x + 10, y + 80, stateColor, false);
        graphics.text(this.font, "Cost: 1 point", x + 10, y + 96, 0xFFD8C9A8, false);
        graphics.text(this.font, "Links: " + node.getConnections().size(), x + 10, y + 112, 0xFFB8A98A, false);

        int lineY = y + 136;
        graphics.text(this.font, "Bonuses", x + 10, lineY, 0xFFE6D7B8, true);
        lineY += 16;

        if (node.getModifiers().isEmpty() && node.getPassiveEffects().isEmpty()) {
            graphics.text(this.font, "No bonuses", x + 10, lineY, 0xFF9E8F73, false);
            lineY += 14;
        } else {
            for (var modifier : node.getModifiers()) {
                String line = switch (modifier.getModifierType()) {
                    case FLAT -> formatStatName(modifier.getStatType()) + " +" + modifier.getValue();
                    case PERCENT -> formatStatName(modifier.getStatType()) + " +" + modifier.getValue() + "%";
                };

                graphics.text(this.font, line, x + 10, lineY, 0xFFD8C9A8, false);
                lineY += 14;
            }

            for (PassiveEffect effect : node.getPassiveEffects()) {
                graphics.text(this.font, formatPassiveEffect(effect), x + 10, lineY, 0xFFB8915E, false);
                lineY += 14;
            }
        }

        lineY += 8;
        graphics.text(this.font, "Active Effects", x + 10, lineY, 0xFFE6D7B8, true);
        lineY += 16;

        if (data.getSkillTreeState().getActiveEffects().isEmpty()) {
            graphics.text(this.font, "None", x + 10, lineY, 0xFF9E8F73, false);
        } else {
            for (PassiveEffect effect : data.getSkillTreeState().getActiveEffects()) {
                graphics.text(this.font, formatPassiveEffect(effect), x + 10, lineY, 0xFFD8C9A8, false);
                lineY += 14;

                if (lineY > y + h - 36) {
                    break;
                }
            }
        }

        graphics.text(this.font, "Wheel - zoom", x + 10, y + h - 42, 0xFF9E8F73, false);
        graphics.text(this.font, "Drag - move tree", x + 10, y + h - 30, 0xFF9E8F73, false);
        graphics.text(this.font, "R - reset tree", x + 10, y + h - 18, 0xFF9E8F73, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        if (data == null) {
            return;
        }

        hoveredNodeId = null;

        graphics.fill(0, 0, this.width, this.height, 0xD0100D0B);

        int panelX = 24;
        int panelY = 24;
        int panelW = this.width - 48;
        int panelH = this.height - 48;

        graphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xD0221B14);
        graphics.fill(panelX + 2, panelY + 2, panelX + panelW - 2, panelY + panelH - 2, 0xD030261D);

        graphics.text(this.font, "Passive Skill Tree", panelX + 12, panelY + 8, 0xFFE6D7B8, false);
        graphics.text(this.font, "Points: " + data.getPassivePoints(), panelX + panelW - 72, panelY + 8, 0xFFD8C9A8, false);

        int treeX = panelX + 12;
        int treeY = panelY + 28;
        int treeW = panelW - 180;
        int treeH = panelH - 40;

        int infoX = panelX + panelW - 156;
        int infoY = panelY + 28;
        int infoW = 144;
        int infoH = panelH - 40;

        graphics.fill(treeX, treeY, treeX + treeW, treeY + treeH, 0x80261E17);
        graphics.fill(infoX, infoY, infoX + infoW, infoY + infoH, 0x80261E17);

        drawConnections(graphics);

        for (SkillNode node : SkillTreeRegistry.NODES.values()) {
            drawNode(graphics, node);
        }

        SkillNode hovered = getHoveredNode(mouseX, mouseY);
        if (hovered != null) {
            hoveredNodeId = hovered.getId();
        }

        drawInfoPanel(graphics, infoX, infoY, infoW, infoH);

        graphics.text(this.font, "J - close", panelX + 12, panelY + panelH - 14, 0xFF9E8F73, false);
        graphics.text(this.font, "LMB node - unlock", panelX + 70, panelY + panelH - 14, 0xFF9E8F73, false);
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
    public boolean isPauseScreen() {
        return false;
    }

    private String formatNodeType(SkillNodeType type) {
        return switch (type) {
            case START -> "Start";
            case SMALL -> "Small";
            case NOTABLE -> "Notable";
            case KEYSTONE -> "Keystone";
        };
    }

    private String formatPassiveEffect(PassiveEffect effect) {
        return switch (effect.getType()) {
            case NONE -> "No special effect";
            case BLEED_ON_HIT -> "Hits apply Bleeding";
            case FIRE_DAMAGE_BOOST -> "Fire damage is increased";
            case MANA_ON_KILL -> "Restore mana on kill";
            case STAMINA_COST_REDUCTION -> "Reduced stamina costs";
        };
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (data == null) {
            return super.keyPressed(event);
        }

        if (event.key() == org.lwjgl.glfw.GLFW.GLFW_KEY_R) {
            SkillTreeService.resetTree(
                    data.getSkillTreeState(),
                    data.getProgression(),
                    data.getStats()
            );

            DerivedStatCalculator.applyDerivedStats(data.getStats());
            selectedNodeId = "start";
            return true;
        }

        return super.keyPressed(event);
    }
}