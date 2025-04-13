package dev.jkopecky.mythicalfoes.events;

import dev.jkopecky.mythicalfoes.MythicalFoes;
import dev.jkopecky.mythicalfoes.mobs.*;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MythicalFoes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(MobRegister.CONJURED_KNIGHT.get(), ConjuredKnightEntity.setAttributes());
        event.put(MobRegister.CONJURED_HOUND.get(), ConjuredHoundEntity.setAttributes());
        event.put(MobRegister.MOLYNIAN_KNIGHT.get(), MolynianKnightEntity.setAttributes());
        event.put(MobRegister.MOLYNIAN_VANGUARD.get(), MolynianVanguardEntity.setAttributes());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(MobRegister.MOLYNIAN_KNIGHT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(MobRegister.MOLYNIAN_VANGUARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
