package dev.jkopecky.mythicalfoes.mobs;

import dev.jkopecky.mythicalfoes.MythicalFoes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
public class MobRegister {


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MythicalFoes.MODID);



    public static final RegistryObject<EntityType<ConjuredKnightEntity>> CONJURED_KNIGHT =
            ENTITY_TYPES.register("conjured_knight",
                    () -> EntityType.Builder.of(ConjuredKnightEntity::new, MobCategory.CREATURE)
                            .sized(1.9f, 3.5f)
                            .build(new ResourceLocation(MythicalFoes.MODID, "conjured_knight").toString()));

    public static final RegistryObject<EntityType<ConjuredHoundEntity>> CONJURED_HOUND =
            ENTITY_TYPES.register("conjured_hound",
                    () -> EntityType.Builder.of(ConjuredHoundEntity::new, MobCategory.CREATURE)
                            .sized(1.5f, 1.0f)
                            .build(new ResourceLocation(MythicalFoes.MODID, "conjured_hound").toString()));

    public static final RegistryObject<EntityType<MolynianKnightEntity>> MOLYNIAN_KNIGHT =
            ENTITY_TYPES.register("molynian_knight",
                    () -> EntityType.Builder.of(MolynianKnightEntity::new, MobCategory.MONSTER)
                            .sized(1.1f, 2.7f)
                            .build(new ResourceLocation(MythicalFoes.MODID, "molynian_knight").toString()));

    public static final RegistryObject<EntityType<MolynianVanguardEntity>> MOLYNIAN_VANGUARD =
            ENTITY_TYPES.register("molynian_vanguard",
                    () -> EntityType.Builder.of(MolynianVanguardEntity::new, MobCategory.MONSTER)
                            .sized(1.2f, 2.7f)
                            .build(new ResourceLocation(MythicalFoes.MODID, "molynian_vanguard").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
