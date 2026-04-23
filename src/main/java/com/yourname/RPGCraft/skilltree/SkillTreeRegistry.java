package com.yourname.RPGCraft.skilltree;

import com.yourname.RPGCraft.stat.StatModifier;
import com.yourname.RPGCraft.stat.StatModifierType;
import com.yourname.RPGCraft.stat.StatType;

import java.util.List;
import java.util.Map;

public class SkillTreeRegistry {

    public static final Map<String, SkillNode> NODES = Map.of(
            "start", new SkillNode(
                    "start",
                    "Origin",
                    "The beginning of your path.",
                    SkillNodeType.START,
                    0,
                    0,
                    List.of("strength_small", "vitality_small"),
                    List.of(),
                    List.of()
            ),

            "strength_small", new SkillNode(
                    "strength_small",
                    "Strong Arms",
                    "+2 Strength",
                    SkillNodeType.SMALL,
                    80,
                    -20,
                    List.of("start", "strength_notable"),
                    List.of(
                            new StatModifier(
                                    StatType.STRENGTH,
                                    StatModifierType.FLAT,
                                    2,
                                    "skill_tree_strength_small"
                            )
                    ),
                    List.of()
            ),

            "vitality_small", new SkillNode(
                    "vitality_small",
                    "Hardy",
                    "+2 Vitality",
                    SkillNodeType.SMALL,
                    80,
                    20,
                    List.of("start", "blood_instinct"),
                    List.of(
                            new StatModifier(
                                    StatType.VITALITY,
                                    StatModifierType.FLAT,
                                    2,
                                    "skill_tree_vitality_small"
                            )
                    ),
                    List.of()
            ),

            "strength_notable", new SkillNode(
                    "strength_notable",
                    "Warrior's Growth",
                    "+10% Strength",
                    SkillNodeType.NOTABLE,
                    160,
                    -20,
                    List.of("strength_small"),
                    List.of(
                            new StatModifier(
                                    StatType.STRENGTH,
                                    StatModifierType.PERCENT,
                                    10,
                                    "skill_tree_strength_notable"
                            )
                    ),
                    List.of()
            ),

            "blood_instinct", new SkillNode(
                    "blood_instinct",
                    "Blood Instinct",
                    "Hits apply bleeding.",
                    SkillNodeType.KEYSTONE,
                    160,
                    20,
                    List.of("vitality_small"),
                    List.of(),
                    List.of(
                            new PassiveEffect(
                                    PassiveEffectType.BLEED_ON_HIT,
                                    1.0,
                                    "skill_tree_blood_instinct"
                            )
                    )
            )
    );

    public static SkillNode getNode(String id) {
        return NODES.get(id);
    }
}