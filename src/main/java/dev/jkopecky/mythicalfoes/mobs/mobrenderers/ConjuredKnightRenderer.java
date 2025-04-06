package dev.jkopecky.mythicalfoes.mobs.mobrenderers;

import dev.jkopecky.mythicalfoes.geckomodels.ConjuredKnightGeoModel;
import dev.jkopecky.mythicalfoes.mobs.ConjuredKnightEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ConjuredKnightRenderer extends GeoEntityRenderer<ConjuredKnightEntity> {


    public ConjuredKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new ConjuredKnightGeoModel());
    }
}
