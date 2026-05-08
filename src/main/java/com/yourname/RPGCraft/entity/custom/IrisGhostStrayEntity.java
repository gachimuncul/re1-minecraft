package com.yourname.RPGCraft.entity.custom;

import com.yourname.RPGCraft.accessory.IrisSignetScaling;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.skeleton.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public class IrisGhostStrayEntity extends Stray {

    private static final int LIFE_TIME_TICKS = 20 * 35;
    private static final double TARGET_RANGE = 18.0D;

    private UUID ownerUuid;
    private int ringLevel = 1;
    private int ageTicks = 0;
    private boolean darkBuffed = false;
    private IrisGhostRole role = IrisGhostRole.ARCHER;
    private boolean berserkActive = false;

    private static final double ARCHER_MIN_DISTANCE = 7.0D;
    private static final double ARCHER_IDEAL_DISTANCE = 11.0D;
    private static final double ARCHER_RETREAT_SPEED = 1.25D;

    private static final double WARRIOR_ATTACK_RANGE = 2.4D;
    private static final double WARRIOR_TOO_CLOSE_RANGE = 1.2D;
    private static final double WARRIOR_RETREAT_SPEED = 1.15D;
    private static final int WARRIOR_DANGER_ENEMY_COUNT = 3;



    public IrisGhostStrayEntity(EntityType<? extends Stray> entityType, Level level) {
        super(entityType, level);
        this.setCustomName(Component.literal("§9Fallen Warrior"));
        this.setGlowingTag(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Stray.createAttributes();
    }

    public void setup(Player owner, int ringLevel, IrisGhostRole role) {
        this.ownerUuid = owner.getUUID();
        this.ringLevel = ringLevel;
        this.role = role;

        this.setCustomName(Component.literal(
                role == IrisGhostRole.ARCHER
                        ? "§9Fallen Archer"
                        : "§9Fallen Bladed Warrior"
        ));

        this.setGlowingTag(true);
        this.setTarget(null);
        this.clearFire();

        equipByRole();
        applyLevelScaling();
        rebuildGoals();
    }

    private void equipByRole() {
        if (role == IrisGhostRole.ARCHER) {
            this.setItemInHand(
                    InteractionHand.MAIN_HAND,
                    new ItemStack(Items.BOW)
            );
            this.setItemInHand(
                    InteractionHand.OFF_HAND,
                    ItemStack.EMPTY
            );
            return;
        }

        this.setItemInHand(
                InteractionHand.MAIN_HAND,
                new ItemStack(Items.IRON_SWORD)
        );

        this.setItemInHand(
                InteractionHand.OFF_HAND,
                new ItemStack(Items.IRON_SWORD)
        );

        var damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(damage.getBaseValue() + 4.0D);
        }

        var speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(speed.getBaseValue() + 0.08D);
        }
    }

    private void rebuildGoals() {
        this.goalSelector.removeAllGoals(goal -> true);
        this.targetSelector.removeAllGoals(goal -> true);

        registerGoals();
    }

    private void applyLevelScaling() {
        double bonusHealth = IrisSignetScaling.getGhostHealthBonus(ringLevel);
        double newMaxHealth = this.getMaxHealth() + bonusHealth;

        var healthAttr = this.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(newMaxHealth);
        }

        this.setHealth((float) newMaxHealth);

        var damageAttr = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damageAttr != null) {
            damageAttr.setBaseValue(
                    damageAttr.getBaseValue() + IrisSignetScaling.getGhostDamageBonus(ringLevel)
            );
        }
    }

    @Override
    protected void registerGoals() {
        if (role == IrisGhostRole.ARCHER) {
            this.goalSelector.addGoal(
                    2,
                    new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F)
            );
        } else {
            this.goalSelector.addGoal(
                    2,
                    new MeleeAttackGoal(this, 1.25D, true)
            );
        }

        this.goalSelector.addGoal(
                7,
                new RandomStrollGoal(this, 0.8D)
        );

        this.goalSelector.addGoal(
                8,
                new LookAtPlayerGoal(this, Player.class, 8.0F)
        );
    }

    @Override
    public void tick() {
        super.tick();

        this.clearFire();

        if (role == IrisGhostRole.ARCHER) {
            handleStepUpRetreat();
        }

        if (!this.level().isClientSide()) {
            serverTickLogic();
        }
    }

    private void handleStepUpRetreat() {

        if (!this.horizontalCollision) {
            return;
        }

        LivingEntity target = this.getTarget();

        if (!canAttackTarget(target)) {
            return;
        }

        double moveX = this.getDeltaMovement().x;
        double moveZ = this.getDeltaMovement().z;

        if (Math.abs(moveX) < 0.001D && Math.abs(moveZ) < 0.001D) {
            return;
        }

        BlockPos frontPos = BlockPos.containing(
                this.getX() + Math.signum(moveX),
                this.getY(),
                this.getZ() + Math.signum(moveZ)
        );

        BlockPos upperFrontPos = frontPos.above();
        BlockPos upperTwoFrontPos = upperFrontPos.above();

        boolean lowerBlocked =
                !this.level().getBlockState(frontPos).isAir();

        boolean upperFree =
                this.level().getBlockState(upperFrontPos).isAir();

        boolean upperTwoFree =
                this.level().getBlockState(upperTwoFrontPos).isAir();

        // Это ступенька / холм высотой 1 блок
        if (lowerBlocked && upperFree && upperTwoFree) {

            this.getJumpControl().jump();

            return;
        }

        // Если стена выше — ищем новый retreat path
        RetreatPoint retreatPoint = findBestRetreatPoint(target);

        if (retreatPoint != null) {
            this.getNavigation().moveTo(
                    retreatPoint.x,
                    retreatPoint.y,
                    retreatPoint.z,
                    ARCHER_RETREAT_SPEED
            );
        }
    }

    private void serverTickLogic() {
        ageTicks++;

        if (ageTicks >= LIFE_TIME_TICKS) {
            this.discard();
            return;
        }

        if (role == IrisGhostRole.WARRIOR) {
            updateBerserk();
        }

        LivingEntity currentTarget = this.getTarget();

        if (!canAttackTarget(currentTarget)) {
            this.setTarget(null);
        }

        if (this.tickCount % 10 == 0) {
            LivingEntity target = findNearestHostile();

            if (target != null) {
                this.setTarget(target);
            } else {
                followOwner();
            }

            updateDarknessBuff();
        }

        if (role == IrisGhostRole.ARCHER) {
            handleArcherKiting();
        } else {
            handleWarriorSpacing();
        }
    }

    private void updateBerserk() {
        if (!IrisSignetScaling.hasWarriorBerserk(ringLevel)) {
            return;
        }

        if (berserkActive) {
            return;
        }

        float hpPercent = this.getHealth() / this.getMaxHealth();

        if (hpPercent <= 0.20F) {
            activateBerserk();
            berserkActive = true;
        }
    }

    private void activateBerserk() {
        this.setCustomName(Component.literal("§cIris' Frenzied Duelist"));

        var damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(damage.getBaseValue() + 6.0D);
        }

        var speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(speed.getBaseValue() + 0.12D);
        }

        this.setGlowingTag(true);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        if (role != IrisGhostRole.ARCHER) {
            super.performRangedAttack(target, velocity);
            return;
        }

        if (!IrisSignetScaling.hasPiercingArrows(ringLevel)) {
            super.performRangedAttack(target, velocity);
            return;
        }

        shootIrisPiercingArrow(target);
    }

    private void shootIrisPiercingArrow(LivingEntity target) {
        IrisPiercingArrowEntity arrow = new IrisPiercingArrowEntity(
                this.level(),
                this,
                IrisSignetScaling.getPiercingArrowDamage(ringLevel),
                IrisSignetScaling.getPiercingArrowLifeTicks(ringLevel)
        );

        double startX = this.getX();
        double startY = this.getEyeY() - 0.1D;
        double startZ = this.getZ();

        arrow.setPosRaw(startX, startY, startZ);

        double dx = target.getX() - startX;
        double dy = target.getEyeY() - startY;
        double dz = target.getZ() - startZ;

        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (length < 0.001D) {
            return;
        }

        dx /= length;
        dy /= length;
        dz /= length;

        double speed = 2.6D;

        arrow.setDeltaMovement(
                dx * speed,
                dy * speed,
                dz * speed
        );

        this.level().addFreshEntity(arrow);
    }

    private void handleWarriorSpacing() {
        LivingEntity target = this.getTarget();

        if (!canAttackTarget(target)) {
            return;
        }

        int nearbyEnemies = countNearbyEnemies(4.0D);

        if (nearbyEnemies >= WARRIOR_DANGER_ENEMY_COUNT) {
            RetreatPoint retreatPoint = findBestRetreatPoint(target);

            if (retreatPoint != null) {
                this.getNavigation().moveTo(
                        retreatPoint.x,
                        retreatPoint.y,
                        retreatPoint.z,
                        WARRIOR_RETREAT_SPEED
                );
            }

            return;
        }

        double distanceSqr = this.distanceToSqr(target);
        double attackRangeSqr = WARRIOR_ATTACK_RANGE * WARRIOR_ATTACK_RANGE;
        double tooCloseSqr = WARRIOR_TOO_CLOSE_RANGE * WARRIOR_TOO_CLOSE_RANGE;

        if (distanceSqr <= tooCloseSqr) {
            RetreatPoint retreatPoint = findBestRetreatPoint(target);

            if (retreatPoint != null) {
                this.getNavigation().moveTo(
                        retreatPoint.x,
                        retreatPoint.y,
                        retreatPoint.z,
                        WARRIOR_RETREAT_SPEED
                );
            }

            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            return;
        }

        if (distanceSqr <= attackRangeSqr) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            return;
        }

        this.getNavigation().moveTo(target, 1.1D);
    }

    private int countNearbyEnemies(double radius) {
        AABB area = this.getBoundingBox().inflate(radius);

        int count = 0;

        for (Monster mob : this.level().getEntitiesOfClass(Monster.class, area)) {
            if (!canAttackTarget(mob)) {
                continue;
            }

            count++;
        }

        return count;
    }

    private LivingEntity findNearestHostile() {
        AABB area = this.getBoundingBox().inflate(TARGET_RANGE);

        Monster best = null;
        double bestDistance = Double.MAX_VALUE;

        for (Monster mob : this.level().getEntitiesOfClass(Monster.class, area)) {
            if (!canAttackTarget(mob)) {
                continue;
            }

            double distance = this.distanceToSqr(mob);

            if (distance < bestDistance) {
                bestDistance = distance;
                best = mob;
            }
        }

        return best;
    }

    private boolean canAttackTarget(LivingEntity target) {
        if (target == null) {
            return false;
        }

        if (!target.isAlive()) {
            return false;
        }

        if (target == this) {
            return false;
        }

        if (target instanceof Player player) {
            return ownerUuid == null || !player.getUUID().equals(ownerUuid);
        }

        if (target instanceof IrisGhostStrayEntity) {
            return false;
        }

        if (!(target instanceof Monster)) {
            return false;
        }

        return this.distanceToSqr(target) <= TARGET_RANGE * TARGET_RANGE;
    }

    private void followOwner() {
        if (ownerUuid == null) {
            return;
        }

        Player owner = this.level().getPlayerByUUID(ownerUuid);

        if (owner == null) {
            return;
        }

        double distance = this.distanceToSqr(owner);

        if (distance > 256.0D) {
            this.snapTo(
                    owner.getX(),
                    owner.getY(),
                    owner.getZ(),
                    owner.getYRot(),
                    owner.getXRot()
            );
            return;
        }

        if (distance > 6.0D) {
            this.getNavigation().moveTo(owner, 1.05D);
        }
    }

    private void updateDarknessBuff() {
        boolean inDarkness = isInDarkness();

        if (inDarkness && !darkBuffed) {
            applyDarkBuff();
            darkBuffed = true;
        }

        if (!inDarkness && darkBuffed) {
            removeDarkBuff();
            darkBuffed = false;
        }
    }

    private boolean isInDarkness() {
        int light = this.level().getMaxLocalRawBrightness(
                BlockPos.containing(this.getX(), this.getY(), this.getZ())
        );

        return light <= 7;
    }

    private void applyDarkBuff() {
        var damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(damage.getBaseValue() + 2.0D + ringLevel);
        }

        var speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(speed.getBaseValue() + 0.04D);
        }
    }

    private void removeDarkBuff() {
        var damage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null) {
            damage.setBaseValue(Math.max(1.0D, damage.getBaseValue() - (2.0D + ringLevel)));
        }

        var speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null) {
            speed.setBaseValue(Math.max(0.1D, speed.getBaseValue() - 0.04D));
        }
    }

    private void handleArcherKiting() {
        LivingEntity target = this.getTarget();

        if (!canAttackTarget(target)) {
            return;
        }

        double distanceSqr = this.distanceToSqr(target);
        double minDistanceSqr = ARCHER_MIN_DISTANCE * ARCHER_MIN_DISTANCE;

        if (distanceSqr > minDistanceSqr) {
            return;
        }

        RetreatPoint retreatPoint = findBestRetreatPoint(target);

        if (retreatPoint == null) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 30.0F, 30.0F);
            return;
        }

        this.getNavigation().moveTo(
                retreatPoint.x,
                retreatPoint.y,
                retreatPoint.z,
                ARCHER_RETREAT_SPEED
        );

        this.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    private RetreatPoint findBestRetreatPoint(LivingEntity target) {
        double awayX = this.getX() - target.getX();
        double awayZ = this.getZ() - target.getZ();

        double length = Math.sqrt(awayX * awayX + awayZ * awayZ);

        if (length < 0.001D) {
            awayX = this.getRandom().nextDouble() - 0.5D;
            awayZ = this.getRandom().nextDouble() - 0.5D;
            length = Math.sqrt(awayX * awayX + awayZ * awayZ);
        }

        awayX /= length;
        awayZ /= length;

        // Перпендикуляр — для ухода вбок
        double sideX = -awayZ;
        double sideZ = awayX;

        double[][] directions = new double[][]{
                // назад
                {awayX, awayZ},

                // назад-влево / назад-вправо
                {awayX + sideX * 0.7D, awayZ + sideZ * 0.7D},
                {awayX - sideX * 0.7D, awayZ - sideZ * 0.7D},

                // чисто вбок
                {sideX, sideZ},
                {-sideX, -sideZ},

                // короткий отход назад
                {awayX * 0.55D, awayZ * 0.55D}
        };

        RetreatPoint best = null;
        double bestScore = -999999.0D;

        for (double[] direction : directions) {
            RetreatPoint point = evaluateRetreatDirection(target, direction[0], direction[1]);

            if (point == null) {
                continue;
            }

            if (point.score > bestScore) {
                bestScore = point.score;
                best = point;
            }
        }

        return best;
    }

    private RetreatPoint evaluateRetreatDirection(LivingEntity target, double dirX, double dirZ) {
        double length = Math.sqrt(dirX * dirX + dirZ * dirZ);

        if (length < 0.001D) {
            return null;
        }

        dirX /= length;
        dirZ /= length;

        double[] distances = new double[]{
                ARCHER_IDEAL_DISTANCE,
                ARCHER_IDEAL_DISTANCE * 0.75D,
                ARCHER_IDEAL_DISTANCE * 0.5D
        };

        for (double distance : distances) {
            double x = this.getX() + dirX * distance;
            double z = this.getZ() + dirZ * distance;
            double y = this.getY();

            BlockPos blockPos = BlockPos.containing(x, y, z);

            if (!isRetreatPositionSafe(blockPos)) {
                continue;
            }

            var path = this.getNavigation().createPath(x, y, z, 0);

            if (path == null) {
                continue;
            }

            double distanceFromTarget = target.distanceToSqr(x, y, z);
            double distanceFromSelf = this.distanceToSqr(x, y, z);

            double score = distanceFromTarget;

            // Не хотим слишком далёкие и странные точки
            score -= distanceFromSelf * 0.15D;

            // Бонус за более длинный отход
            score += distance * 0.4D;

            return new RetreatPoint(x, y, z, score);
        }

        return null;
    }

    private boolean isRetreatPositionSafe(BlockPos pos) {
        BlockPos feet = pos;
        BlockPos head = pos.above();
        BlockPos ground = pos.below();

        boolean feetFree = this.level().getBlockState(feet).isAir();
        boolean headFree = this.level().getBlockState(head).isAir();
        boolean groundSolid = !this.level().getBlockState(ground).isAir();

        return feetFree && headFree && groundSolid;
    }

    private static class RetreatPoint {
        private final double x;
        private final double y;
        private final double z;
        private final double score;

        private RetreatPoint(double x, double y, double z, double score) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.score = score;
        }
    }

    public boolean isSunBurnTick() {
        return false;
    }

    public boolean removeWhenFarAway(double distanceSquared) {
        return false;
    }

    public boolean canAttack(LivingEntity target) {
        return canAttackTarget(target) && super.canAttack(target);
    }

}