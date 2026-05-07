package com.yourname.RPGCraft.entity.custom;

import com.yourname.RPGCraft.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class IrisPiercingArrowEntity extends Projectile {

    private final Set<UUID> hitEntities = new HashSet<>();

    private int lifeTicks = 0;
    private int maxLifeTicks = 20 * 8;

    private float customDamage = 6.0F;

    public IrisPiercingArrowEntity(
            EntityType<? extends IrisPiercingArrowEntity> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    public IrisPiercingArrowEntity(
            Level level,
            LivingEntity owner,
            float damage,
            int maxLifeTicks
    ) {
        super(ModEntities.IRIS_PIERCING_ARROW, level);

        this.setOwner(owner);

        this.customDamage = damage;
        this.maxLifeTicks = maxLifeTicks;
    }

    @Override
    public void tick() {

        super.baseTick();

        lifeTicks++;

        if (lifeTicks >= maxLifeTicks) {
            this.discard();
            return;
        }

        this.setPos(
                this.getX() + this.getDeltaMovement().x,
                this.getY() + this.getDeltaMovement().y,
                this.getZ() + this.getDeltaMovement().z
        );

        checkEntityCollisions();
    }

    private void checkEntityCollisions() {

        double hitRadius = 0.6D;

        for (LivingEntity entity : this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(hitRadius)
        )) {

            if (entity == this.getOwner()) {
                continue;
            }

            if (!entity.isAlive()) {
                continue;
            }

            UUID uuid = entity.getUUID();

            if (hitEntities.contains(uuid)) {
                continue;
            }

            hitEntities.add(uuid);

            entity.hurt(
                    this.damageSources().generic(),
                    customDamage
            );
        }
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
    }

    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag, net.minecraft.core.HolderLookup.Provider provider) {
    }
}