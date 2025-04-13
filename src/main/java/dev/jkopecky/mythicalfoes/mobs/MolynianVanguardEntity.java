package dev.jkopecky.mythicalfoes.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class MolynianVanguardEntity extends Monster implements GeoEntity {

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.molynianvanguard.idle");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.molynianvanguard.walk");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().then("animation.molynianvanguard.attack", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


    protected MolynianVanguardEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.ATTACK_SPEED, 1f)
                .add(Attributes.MOVEMENT_SPEED, 0.19f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2f)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.FOLLOW_RANGE, 32)
                .build();
    }


    @SuppressWarnings("deprecation")
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData output = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        populateDefaultEquipmentSlots(new SingleThreadedRandomSource(new Random().nextLong(Long.MIN_VALUE, Long.MAX_VALUE)), pDifficulty);
        populateDefaultEquipmentEnchantments(new SingleThreadedRandomSource(new Random().nextLong(Long.MIN_VALUE, Long.MAX_VALUE)), pDifficulty);


        //handle spawning allies
        int toSpawn = new Random().nextInt(2,7);
        int x = Mth.floor(this.getX());
        int y = Mth.floor(this.getY());
        int z = Mth.floor(this.getZ());
        ServerLevel serverlevel = (ServerLevel)this.level();
        LivingEntity target = this.getTarget();
        for (int i = 0; i < toSpawn; i++) { //attempt to spawn allies
            MolynianKnightEntity knight = MobRegister.MOLYNIAN_KNIGHT.get().create(this.level());

            for(int l = 0; l < 50; ++l) {
                int x1 = x + Mth.nextInt(this.random, 3, 15) * Mth.nextInt(this.random, -1, 1);
                int y1 = y + Mth.nextInt(this.random, 3, 5) * Mth.nextInt(this.random, -1, 1);
                int z1 = z + Mth.nextInt(this.random, 3, 15) * Mth.nextInt(this.random, -1, 1);
                knight.setPos(x1, y1, z1);

                if (knight.checkSpawnObstruction(serverlevel)) {
                    if (!serverlevel.hasNearbyAlivePlayer(x1, y1, z1, 7.0F) && serverlevel.isUnobstructed(knight) && serverlevel.noCollision(knight) && !serverlevel.containsAnyLiquid(knight.getBoundingBox())) {
                        if (target != null) {
                            knight.setTarget(target);
                        }

                        knight.finalizeSpawn(serverlevel, serverlevel.getCurrentDifficultyAt(knight.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
                        serverlevel.addFreshEntityWithPassengers(knight);
                        break;
                    }
                }
            }
        }

        return output;
    }


    @Override
    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }



    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 18.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractSkeleton.class, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Zombie.class, false));
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
