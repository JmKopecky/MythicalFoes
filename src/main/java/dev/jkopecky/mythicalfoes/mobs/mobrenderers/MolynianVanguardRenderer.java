package dev.jkopecky.mythicalfoes.mobs.mobrenderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.jkopecky.mythicalfoes.geckomodels.MolynianKnightGeoModel;
import dev.jkopecky.mythicalfoes.geckomodels.MolynianVanguardGeoModel;
import dev.jkopecky.mythicalfoes.mobs.MolynianKnightEntity;
import dev.jkopecky.mythicalfoes.mobs.MolynianVanguardEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class MolynianVanguardRenderer extends GeoEntityRenderer<MolynianVanguardEntity> {

    private static final String LEFT_HAND = "handLeft";
    private static final String RIGHT_HAND = "handRight";


    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;

    public MolynianVanguardRenderer(EntityRendererProvider.Context context) {
        super(context, new MolynianVanguardGeoModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));



        addRenderLayer(new BlockAndItemGeoLayer<>(this) {

            @Override
            protected @Nullable ItemStack getStackForBone(GeoBone bone, MolynianVanguardEntity animatable) {
                // Retrieve the items in the entity's hands for the relevant bone
                return switch (bone.getName()) {
                    case LEFT_HAND -> animatable.isLeftHanded() ?
                            MolynianVanguardRenderer.this.mainHandItem : MolynianVanguardRenderer.this.offhandItem;
                    case RIGHT_HAND -> animatable.isLeftHanded() ?
                            MolynianVanguardRenderer.this.offhandItem : MolynianVanguardRenderer.this.mainHandItem;
                    default -> null;
                };
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, MolynianVanguardEntity animatable) {
                // Apply the camera transform for the given hand
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    default -> ItemDisplayContext.NONE;
                };
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, MolynianVanguardEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == MolynianVanguardRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem)
                        poseStack.translate(0, 0.125, -0.25);

                    poseStack.scale(1.5f,1.5f,1.5f);
                }
                else if (stack == MolynianVanguardRenderer.this.offhandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    }

                    poseStack.scale(1.5f,1.5f,1.5f);
                }

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }


    @Override
    public void preRender(PoseStack poseStack, MolynianVanguardEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        this.mainHandItem = animatable.getMainHandItem();
        this.offhandItem = animatable.getOffhandItem();
    }
}
