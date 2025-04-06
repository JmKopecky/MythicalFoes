package dev.jkopecky.mythicalfoes.geckomodels;

import dev.jkopecky.mythicalfoes.MythicalFoes;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

@SuppressWarnings({"removal"})
public class ConjuredKnightGeoModel extends GeoModel {

    private final ResourceLocation model = new ResourceLocation(MythicalFoes.MODID, "geo/conjuredknight.geo.json");
    private final ResourceLocation texture = new ResourceLocation(MythicalFoes.MODID, "textures/entity/conjured_knight_texture.png");
    private final ResourceLocation animations = new ResourceLocation(MythicalFoes.MODID, "animations/conjuredknight/model.animation.json");

    @Override
    public ResourceLocation getModelResource(GeoAnimatable geoAnimatable) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable geoAnimatable) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable geoAnimatable) {
        return this.animations;
    }
}
