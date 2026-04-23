package com.yourname.RPGCraft.skilltree;

import com.yourname.RPGCraft.stat.StatModifier;

import java.util.List;

public class SkillNode {

    private final String id;
    private final String title;
    private final String description;
    private final SkillNodeType type;
    private final int x;
    private final int y;
    private final List<String> connections;
    private final List<StatModifier> modifiers;
    private final List<PassiveEffect> passiveEffects;

    public SkillNode(
            String id,
            String title,
            String description,
            SkillNodeType type,
            int x,
            int y,
            List<String> connections,
            List<StatModifier> modifiers,
            List<PassiveEffect> passiveEffects
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.x = x;
        this.y = y;
        this.connections = connections;
        this.modifiers = modifiers;
        this.passiveEffects = passiveEffects;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public SkillNodeType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<String> getConnections() {
        return connections;
    }

    public List<StatModifier> getModifiers() {
        return modifiers;
    }

    public List<PassiveEffect> getPassiveEffects() {
        return passiveEffects;
    }
}