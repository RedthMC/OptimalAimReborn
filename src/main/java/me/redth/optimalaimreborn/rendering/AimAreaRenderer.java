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
        if (entity == mc.player) return;
        if (entity.isDead()) return;
        if (entity.isInvisible()) return;
        if (!mc.player.isInRange(entity, config.distance)) return;

        Vec3d cameraPosRelToEntity = mc.player.getCameraPosVec(tickDelta).subtract(entity.getLerpedPos(tickDelta));
        Box boxAtOrigin = entity.getBoundingBox().offset(entity.getPos().negate());
        Vec3d closestPoint = pointClampedIntoBox(cameraPosRelToEntity, boxAtOrigin);
        boolean isWithinReach = closestPoint.isInRange(cameraPosRelToEntity, mc.player.isCreative() ? 5f : 3f);
        Box shrunkenTargetBox = boxAtOrigin.contract(0.1);
        Vec3d bestHitPos = pointClampedIntoBox(closestPoint, shrunkenTargetBox);
        Box bestHitArea = new Box(bestHitPos, bestHitPos).expand(0.1);
        Color color = Color.ofTransparent(isWithinReach ? config.reachableColor : config.color);
        fillBox(matrices, vertexConsumersProvider, bestHitArea, color);
    }

    private static Vec3d pointClampedIntoBox(Vec3d point, Box box) {
        double x = MathHelper.clamp(point.x, box.minX, box.maxX);
        double y = MathHelper.clamp(point.y, box.minY, box.maxY);
        double z = MathHelper.clamp(point.z, box.minZ, box.maxZ);
        return new Vec3d(x, y, z);
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
