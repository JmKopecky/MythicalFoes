package dev.jkopecky.mythicalfoes.mobs;

import dev.jkopecky.mythicalfoes.mobs.customgoals.ConjuredKnightEntityMeleeGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ConjuredKnightEntity extends PathfinderMob implements GeoEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.conjuredknight.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.conjuredknight.walk");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().then("animation.conjuredknight.attack", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    protected ConjuredKnightEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ATTACK_DAMAGE, 7.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2f)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.FOLLOW_RANGE, 48)
                .build();
    }


    @Override
    protected void registerGoals() {

        //attack goal overridden to support a delay for the attack animation
        this.goalSelector.addGoal(2, new ConjuredKnightEntityMeleeGoal(this, 1.2D, false));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 18.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "Idle", 5, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "Attack", 5, this::attackPredicate));
    }


    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(WALK_ANIM);
            return PlayState.CONTINUE;
        }

        tAnimationState.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }


    private <T extends GeoAnimatable> PlayState attackPredicate(AnimationState<T> tAnimationState) {
        if (this.swinging && tAnimationState.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            tAnimationState.getController().forceAnimationReset();
            tAnimationState.getController().setAnimation(ATTACK_ANIM);
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
