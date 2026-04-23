package com.yourname.RPGCraft.skilltree;

import com.yourname.RPGCraft.player.ProgressionData;
import com.yourname.RPGCraft.stat.StatContainer;
import com.yourname.RPGCraft.stat.StatModifier;

public class SkillTreeService {

    public static boolean canUnlockNode(String nodeId, SkillTreeState state, ProgressionData progression) {
        SkillNode node = SkillTreeRegistry.getNode(nodeId);

        if (node == null) {
            return false;
        }

        if (state.isUnlocked(nodeId)) {
            return false;
        }

        if (progression.getPassivePoints() <= 0) {
            return false;
        }

        if (node.getType() == SkillNodeType.START) {
            return false;
        }

        for (String connection : node.getConnections()) {
            if (state.isUnlocked(connection)) {
                return true;
            }
        }

        return false;
    }

    public static boolean unlockNode(String nodeId, SkillTreeState state, ProgressionData progression, StatContainer stats) {
        if (!canUnlockNode(nodeId, state, progression)) {
            return false;
        }

        SkillNode node = SkillTreeRegistry.getNode(nodeId);
        if (node == null) {
            return false;
        }

        if (!progression.spendPassivePoint()) {
            return false;
        }

        state.unlock(nodeId);

        for (StatModifier modifier : node.getModifiers()) {
            stats.addModifier(modifier);
        }

        for (PassiveEffect effect : node.getPassiveEffects()) {
            state.addPassiveEffect(effect);
        }

        return true;
    }
}