package com.yourname.RPGCraft.skilltree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SkillTreeState {

    private final Set<String> unlockedNodes = new HashSet<>();
    private final List<PassiveEffect> activeEffects = new ArrayList<>();

    public SkillTreeState() {
        unlockedNodes.add("start");
    }

    public boolean isUnlocked(String nodeId) {
        return unlockedNodes.contains(nodeId);
    }

    public boolean unlock(String nodeId) {
        return unlockedNodes.add(nodeId);
    }

    public Set<String> getUnlockedNodes() {
        return unlockedNodes;
    }

    public List<PassiveEffect> getActiveEffects() {
        return activeEffects;
    }

    public void addPassiveEffect(PassiveEffect effect) {
        activeEffects.add(effect);
    }

    public int reset() {
        int spentPoints = Math.max(0, unlockedNodes.size() - 1);

        unlockedNodes.clear();
        unlockedNodes.add("start");

        activeEffects.clear();

        return spentPoints;
    }
}