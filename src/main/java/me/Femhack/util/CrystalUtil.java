package me.Femhack.util;

import me.Femhack.features.modules.combat.FemboyAura;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.util.List;
import java.util.stream.Collectors;

import static me.Femhack.util.BlockUtil.getSphere;
import static me.Femhack.util.DamageUtil.getBlastReduction;
import static me.Femhack.util.DamageUtil.getDamageMultiplied;
import static me.Femhack.util.EntityUtil.getPlayerPos;

public class CrystalUtil {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean canSeePos(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }


    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean oneDot15, boolean cc) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        final BlockPos final_boost = blockPos.add(0, 3, 0);
        try {
            if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if ((mc.world.getBlockState(boost).getBlock() != Blocks.AIR || (mc.world.getBlockState(boost2).getBlock() != Blocks.AIR && !oneDot15))) {
                return false;
            }
            if (!specialEntityCheck) {
                return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty();
            }
            for (final Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            for (final Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            for (final Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(final_boost))) {
                if (entity instanceof EntityEnderCrystal) {
                    return false;
                }
            }
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static List<BlockPos> possiblePlacePositions(final float placeRange, final boolean thirteen, final boolean specialEntityCheck, final boolean cc) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(getPlayerPos(mc.player), placeRange, (int) placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, thirteen, specialEntityCheck, cc)).collect(Collectors.toList()));
        return positions;
    }

    public static int getCrystalSlot() {
        int n = -1;
        if (Util.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            n = Util.mc.player.inventory.currentItem;
        }
        if (n == -1) {
            for (int i = 0; i < 9; ++i) {
                if (Util.mc.player.inventory.getStackInSlot(i).getItem() != Items.END_CRYSTAL) continue;
                n = i;
                break;
            }
        }
        return n;
    }

    public static int getSwordSlot() {
        int n = -1;
        if (Util.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
            n = Util.mc.player.inventory.currentItem;
        }
        if (n == -1) {
            for (int i = 0; i < 9; ++i) {
                if (Util.mc.player.inventory.getStackInSlot(i).getItem() != Items.DIAMOND_SWORD) continue;
                n = i;
                break;
            }
        }
        return n;
    }

    public static int ping() {
        if (mc.getConnection() == null) {
            return 50;
        }
        if (CrystalUtil.mc.player == null) {
            return 50;
        }
        try {
            return mc.getConnection().getPlayerInfo(CrystalUtil.mc.player.getUniqueID()).getResponseTime();
        }
        catch (NullPointerException nullPointerException) {
            return 50;
        }
    }

    public static boolean isVisible(Vec3d vec3d) {
        Vec3d vec3d2 = new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.getEntityBoundingBox().minY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ);
        return CrystalUtil.mc.world.rayTraceBlocks(vec3d2, vec3d) == null;
    }

    public static boolean rayTraceBreak(double d, double d2, double d3) {
        if (CrystalUtil.mc.world.rayTraceBlocks(new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ), new Vec3d(d, d2 + 1.8, d3), false, true, false) == null) {
            return true;
        }
        if (CrystalUtil.mc.world.rayTraceBlocks(new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ), new Vec3d(d, d2 + 1.5, d3), false, true, false) == null) {
            return true;
        }
        return CrystalUtil.mc.world.rayTraceBlocks(new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ), new Vec3d(d, d2, d3), false, true, false) == null;
    }

    public static float calculateDamage(BlockPos blockPos, Entity entity) {
        return CrystalUtil.calculateDamage((double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5, entity);
    }

    public static float calculateDamage(EntityEnderCrystal entityEnderCrystal, Entity entity) {
        return CrystalUtil.calculateDamage(entityEnderCrystal.posX, entityEnderCrystal.posY, entityEnderCrystal.posZ, entity);
    }

    public static
    float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int) ((v * v + v) / 2.0 * 7.0 * (double) doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(
                    mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float) finald;
    }

    public static
    float pumpkinWtf(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int) ((v * v + v) / 2.0 * 7.0 * (double) doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(
                    mc.world, null, posX, posY, posZ, 9f, false, true));
        }
        return (float) finald;
    }

    public static float calculateDamage(Vec3d vec3d, Entity entity) {
        return calculateDamage(vec3d.x, vec3d.y, vec3d.z, entity);
    }

    public static boolean rayTracePlace(BlockPos blockPos) {
        if (FemboyAura.getInstance().directionMode.getValue() != FemboyAura.DirectionMode.VANILLA) {
            double d = 0.45;
            double d2 = 0.05;
            double d3 = 0.95;
            Vec3d vec3d = new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.getEntityBoundingBox().minY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ);
            for (double d4 = d2; d4 <= d3; d4 += d) {
                for (double d5 = d2; d5 <= d3; d5 += d) {
                    for (double d6 = d2; d6 <= d3; d6 += d) {
                        Vec3d vec3d2 = new Vec3d((Vec3i)blockPos).add(d4, d5, d6);
                        double d7 = vec3d.distanceTo(vec3d2);
                        if (FemboyAura.getInstance().strictDirection.getValue().booleanValue() && d7 > (double) FemboyAura.getInstance().placeRange.getValue().floatValue()) continue;
                        double d8 = vec3d2.x - vec3d.x;
                        double d9 = vec3d2.y - vec3d.y;
                        double d10 = vec3d2.z - vec3d.z;
                        double d11 = MathHelper.sqrt((double)(d8 * d8 + d10 * d10));
                        double[] arrd = new double[]{MathHelper.wrapDegrees((float)((float)Math.toDegrees(Math.atan2(d10, d8)) - 90.0f)), MathHelper.wrapDegrees((float)((float)(-Math.toDegrees(Math.atan2(d9, d11)))))};
                        float f = MathHelper.cos((float)((float)(-arrd[0] * 0.01745329238474369 - 3.1415927410125732)));
                        float f2 = MathHelper.sin((float)((float)(-arrd[0] * 0.01745329238474369 - 3.1415927410125732)));
                        float f3 = -MathHelper.cos((float)((float)(-arrd[1] * 0.01745329238474369)));
                        float f4 = MathHelper.sin((float)((float)(-arrd[1] * 0.01745329238474369)));
                        Vec3d vec3d3 = new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
                        Vec3d vec3d4 = vec3d.add(vec3d3.x * d7, vec3d3.y * d7, vec3d3.z * d7);
                        RayTraceResult rayTraceResult = CrystalUtil.mc.world.rayTraceBlocks(vec3d, vec3d4, false, false, false);
                        if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK || !rayTraceResult.getBlockPos().equals((Object)blockPos)) continue;
                        return true;
                    }
                }
            }
            return false;
        }
        for (EnumFacing enumFacing : EnumFacing.values()) {
            RayTraceResult rayTraceResult;
            Vec3d vec3d = new Vec3d((double)blockPos.getX() + 0.5 + (double)enumFacing.getDirectionVec().getX() * 0.5, (double)blockPos.getY() + 0.5 + (double)enumFacing.getDirectionVec().getY() * 0.5, (double)blockPos.getZ() + 0.5 + (double)enumFacing.getDirectionVec().getZ() * 0.5);
            if (FemboyAura.getInstance().strictDirection.getValue().booleanValue() && CrystalUtil.mc.player.getPositionVector().add(0.0, (double)CrystalUtil.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d) > (double) FemboyAura.getInstance().placeRange.getValue().floatValue() || (rayTraceResult = CrystalUtil.mc.world.rayTraceBlocks(new Vec3d(CrystalUtil.mc.player.posX, CrystalUtil.mc.player.posY + (double)CrystalUtil.mc.player.getEyeHeight(), CrystalUtil.mc.player.posZ), vec3d, false, true, false)) == null || !rayTraceResult.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK) || !rayTraceResult.getBlockPos().equals((Object)blockPos)) continue;
            return true;
        }
        return false;
    }

}
