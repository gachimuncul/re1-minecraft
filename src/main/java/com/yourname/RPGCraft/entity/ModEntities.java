package com.yourname.RPGCraft.entity;

import com.yourname.RPGCraft.RPGCraft;
import com.yourname.RPGCraft.entity.custom.IrisGhostStrayEntity;
import com.yourname.RPGCraft.entity.custom.IrisPiercingArrowEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final EntityType<IrisGhostStrayEntity> IRIS_GHOST_STRAY = register(
            "iris_ghost_stray",
            EntityType.Builder.of(IrisGhostStrayEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
    );

    private static <T extends net.minecraft.world.entity.Entity> EntityType<T> register(
            String name,
            EntityType.Builder<T> builder
    ) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(RPGCraft.MOD_ID, name)
        );

        EntityType<T> type = builder.build(key);

        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                key,
                type
        );
    }

    public static final EntityType<IrisPiercingArrowEntity> IRIS_PIERCING_ARROW = register(
            "iris_piercing_arrow",
            EntityType.Builder.<IrisPiercingArrowEntity>of(
                            IrisPiercingArrowEntity::new,
                            MobCategory.MISC
                    )
                    .sized(0.5F, 0.5F)
    );

    public static void initialize() {
    }
}