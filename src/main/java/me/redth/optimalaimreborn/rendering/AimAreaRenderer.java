package me.redth.optimalaimreborn.rendering;

import me.shedaniel.math.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static me.redth.optimalaimreborn.OptimalAimReborn.config;
import static me.redth.optimalaimreborn.OptimalAimReborn.mc;

public class AimAreaRenderer {
    public static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumersProvider, LivingEntity entity, float tickDelta) {
        if (!config.enabled) return;
        if (mc.player == null) return;
        if (entity.isDead()) return;
        if (mc.player.squaredDistanceTo(entity) > config.distance * config.distance) return;
        Box targetBox = entity.getBoundingBox().contract(0.1);
        Vec3d eyePosition = mc.player.getCameraPosVec(tickDelta);
        Vec3d bestHitPositionOffsetted = pointClampedIntoBox(eyePosition, targetBox).subtract(entity.getLerpedPos(tickDelta));
        Box bestHitAreaOffsetted = new Box(bestHitPositionOffsetted, bestHitPositionOffsetted).expand(0.1);
        fillBox(matrices, vertexConsumersProvider, bestHitAreaOffsetted, Color.ofTransparent(config.color));
    }

    private static Vec3d pointClampedIntoBox(Vec3d point, Box box) {
        double newX = MathHelper.clamp(point.x, box.minX, box.maxX);
        double newY = MathHelper.clamp(point.y, box.minY, box.maxY);
        double newZ = MathHelper.clamp(point.z, box.minZ, box.maxZ);
        return new Vec3d(newX, newY, newZ);
    }

    public static void fillBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Box box, Color color) {
        WorldRenderer.renderFilledBox(
            matrices,
            vertexConsumers.getBuffer(RenderLayer.getDebugFilledBox()),
            box.minX, box.minY, box.minZ,
            box.maxX, box.maxY, box.maxZ,
            color.getRed() / 255f,
            color.getGreen() / 255f,
            color.getBlue() / 255f,
            color.getAlpha() / 255f
        );
    }
}
