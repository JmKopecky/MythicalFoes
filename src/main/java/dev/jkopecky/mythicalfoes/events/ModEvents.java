package dev.jkopecky.mythicalfoes.events;

import dev.jkopecky.mythicalfoes.MythicalFoes;
import dev.jkopecky.mythicalfoes.mobs.ConjuredHoundEntity;
import dev.jkopecky.mythicalfoes.mobs.ConjuredKnightEntity;
import dev.jkopecky.mythicalfoes.mobs.MobRegister;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MythicalFoes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(MobRegister.CONJURED_KNIGHT.get(), ConjuredKnightEntity.setAttributes());
        event.put(MobRegister.CONJURED_HOUND.get(), ConjuredHoundEntity.setAttributes());
    }
}
