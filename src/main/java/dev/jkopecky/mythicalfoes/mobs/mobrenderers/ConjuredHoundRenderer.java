package dev.jkopecky.mythicalfoes.mobs.mobrenderers;

import dev.jkopecky.mythicalfoes.geckomodels.ConjuredHoundGeoModel;
import dev.jkopecky.mythicalfoes.geckomodels.ConjuredKnightGeoModel;
import dev.jkopecky.mythicalfoes.mobs.ConjuredHoundEntity;
import dev.jkopecky.mythicalfoes.mobs.ConjuredKnightEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ConjuredHoundRenderer extends GeoEntityRenderer<ConjuredHoundEntity> {


    public ConjuredHoundRenderer(EntityRendererProvider.Context context) {
        super(context, new ConjuredHoundGeoModel());
    }
}
