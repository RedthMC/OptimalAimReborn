package me.redth.optimalaimreborn.rendering;

import me.shedaniel.math.Color;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static me.redth.optimalaimreborn.OptimalAimReborn.config;
import static me.redth.optimalaimreborn.OptimalAimReborn.mc;

public class AimAreaRenderer extends FeatureRenderer<LivingEntity, EntityModel<LivingEntity>> {
    @SuppressWarnings("unchecked")
    public AimAreaRenderer(FeatureRendererContext<?, ?> context) {
        super((FeatureRendererContext<LivingEntity, EntityModel<LivingEntity>>) context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!config.enabled) return;
        if (mc.player == null) return;
        if (entity.isDead()) return;
        if (mc.player.squaredDistanceTo(entity) > config.distance * config.distance) return;
        Box targetBox = entity.getBoundingBox();
        Vec3d eyePosition = new Vec3d(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());
        Vec3d bestHitPosition = pointClampedIntoBox(eyePosition, targetBox);
        bestHitPosition.add(-entity.getX(), -entity.getY(), -entity.getZ());
        Box bestHitBox = new Box(bestHitPosition, bestHitPosition).expand(0.2);
        fillBox(matrices,vertexConsumers, bestHitBox, Color.ofTransparent(config.color));
    }

    public static Vec3d pointClampedIntoBox(Vec3d point, Box box) {
        double newX = MathHelper.clamp(point.x, box.minX, box.maxX);
        double newY = MathHelper.clamp(point.y, box.minY, box.maxY);
        double newZ = MathHelper.clamp(point.z, box.minZ, box.maxZ);
        return new Vec3d(newX, newY, newZ);
    }

    public static void fillBox(MatrixStack matrices,VertexConsumerProvider vertexConsumers, Box box, Color color) {
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer = tessellator.getBuffer();
//        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        WorldRenderer.renderFilledBox(
            matrices, vertexConsumers.getBuffer(RenderLayer.getDebugFilledBox()),
            box.minX, box.minY, box.minZ,
            box.maxX, box.maxY, box.maxZ,
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f,
            color.getAlpha() / 255f
        );
//        tessellator.draw();
    }
}
