package me.Femhack.util;

import me.Femhack.mixin.mixins.accessors.IEntityPlayerSP;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class SilentRotaionUtil {
    private /* synthetic */ float yawi;

    public static void update(float f, float f2) {
        boolean bl;
        boolean bl2 = Util.mc.player.isSprinting();
        if (bl2 != ((IEntityPlayerSP)Util.mc.player).getServerSprintState()) {
            if (bl2) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Util.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                Util.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Util.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
            }
            ((IEntityPlayerSP)Util.mc.player).setServerSprintState(bl2);
        }
        if ((bl = Util.mc.player.isSneaking()) != ((IEntityPlayerSP)Util.mc.player).getServerSneakState()) {
            if (bl) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Util.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                Util.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Util.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            ((IEntityPlayerSP)Util.mc.player).setServerSneakState(bl);
        }
        if (Util.mc.player == Util.mc.getRenderViewEntity()) {
            AxisAlignedBB axisAlignedBB = Util.mc.player.getEntityBoundingBox();
            double d = Util.mc.player.posX - ((IEntityPlayerSP)Util.mc.player).getLastReportedPosX();
            double d2 = axisAlignedBB.minY - ((IEntityPlayerSP)Util.mc.player).getLastReportedPosY();
            double d3 = Util.mc.player.posZ - ((IEntityPlayerSP)Util.mc.player).getLastReportedPosZ();
            double d4 = f - ((IEntityPlayerSP)Util.mc.player).getLastReportedYaw();
            double d5 = f2 - ((IEntityPlayerSP)Util.mc.player).getLastReportedPitch();
            ((IEntityPlayerSP)Util.mc.player).setPositionUpdateTicks(((IEntityPlayerSP)Util.mc.player).getPositionUpdateTicks() + 1);
            boolean bl3 = d * d + d2 * d2 + d3 * d3 > 9.0E-4 || ((IEntityPlayerSP)Util.mc.player).getPositionUpdateTicks() >= 20;
            boolean bl4 = d4 != 0.0 || d5 != 0.0;
            boolean bl5 = bl4;
            if (Util.mc.player.isRiding()) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(Util.mc.player.motionX, -999.0, Util.mc.player.motionZ, f, f2, Util.mc.player.onGround));
                bl3 = false;
            } else if (bl3 && bl4) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(Util.mc.player.posX, axisAlignedBB.minY, Util.mc.player.posZ, f, f2, Util.mc.player.onGround));
            } else if (bl3) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Util.mc.player.posX, axisAlignedBB.minY, Util.mc.player.posZ, Util.mc.player.onGround));
            } else if (bl4) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(f, f2, Util.mc.player.onGround));
            } else if (((IEntityPlayerSP)Util.mc.player).getPrevOnGround() != Util.mc.player.onGround) {
                Util.mc.player.connection.sendPacket((Packet)new CPacketPlayer(Util.mc.player.onGround));
            }
            if (bl3) {
                ((IEntityPlayerSP)Util.mc.player).setLastReportedPosX(Util.mc.player.posX);
                ((IEntityPlayerSP)Util.mc.player).setLastReportedPosY(axisAlignedBB.minY);
                ((IEntityPlayerSP)Util.mc.player).setLastReportedPosZ(Util.mc.player.posZ);
                ((IEntityPlayerSP)Util.mc.player).setPositionUpdateTicks(0);
            }
            if (bl4) {
                ((IEntityPlayerSP)Util.mc.player).setLastReportedYaw(f);
                ((IEntityPlayerSP)Util.mc.player).setLastReportedPitch(f2);
            }
            ((IEntityPlayerSP)Util.mc.player).setPrevOnGround(Util.mc.player.onGround);
            ((IEntityPlayerSP)Util.mc.player).setAutoJumpEnabled(Util.mc.gameSettings.autoJump);
        }
    }

    public static void lookAtEntity(Entity entity) {
        float[] arrf = SilentRotaionUtil.calcAngle(Util.mc.player.getPositionEyes(Util.mc.getRenderPartialTicks()), entity.getPositionEyes(Util.mc.getRenderPartialTicks()));
        SilentRotaionUtil.lookAtAngles(arrf[0], arrf[1]);
    }

    public void setYaw(float f) {
        this.yawi = f;
        Minecraft.getMinecraft();
        Util.mc.player.rotationYawHead = f;
        Minecraft.getMinecraft();
        Util.mc.player.rotationYawHead = f;
    }

    public static void setPlayerRotations(float f, float f2) {
        Util.mc.player.rotationYaw = f;
        Util.mc.player.rotationYawHead = f;
        Util.mc.player.rotationPitch = f2;
    }

    public static void lookAtXYZ(float f, float f2, float f3) {
        Vec3d vec3d = new Vec3d((double)f, (double)f2, (double)f3);
        float[] arrf = SilentRotaionUtil.calcAngle(Util.mc.player.getPositionEyes(Util.mc.getRenderPartialTicks()), vec3d);
        SilentRotaionUtil.setPlayerRotations(arrf[0], arrf[1]);
        Util.mc.player.rotationYawHead = arrf[0];
    }

    public static void lookAtBlock(BlockPos blockPos) {
        float[] arrf = SilentRotaionUtil.calcAngle(Util.mc.player.getPositionEyes(Util.mc.getRenderPartialTicks()), new Vec3d((Vec3i)blockPos));
        SilentRotaionUtil.setPlayerRotations(arrf[0], arrf[1]);
        Util.mc.player.renderYawOffset = arrf[0];
        Util.mc.player.rotationYawHead = arrf[0];
    }

    public static float[] calcAngle(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = (vec3d2.y - vec3d.y) * -1.0;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = MathHelper.sqrt((double)(d * d + d3 * d3));
        return new float[]{(float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(d3, d)) - 90.0)), (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(d2, d4)))};
    }

    public static void lookAtAngles(float f, float f2) {
        SilentRotaionUtil.setPlayerRotations(f, f2);
        Util.mc.player.rotationYawHead = f;
    }

    public static void lookAtVector(Vec3d vec3d) {
        float[] arrf = SilentRotaionUtil.calcAngle(Util.mc.player.getPositionEyes(Util.mc.getRenderPartialTicks()), vec3d);
        SilentRotaionUtil.setPlayerRotations(arrf[0], arrf[1]);
        Util.mc.player.rotationYawHead = arrf[0];
    }

    public static float[] calculateAngle(Vec3d vec3d, Vec3d vec3d2) {
        double d = vec3d2.x - vec3d.x;
        double d2 = (vec3d2.y - vec3d.y) * -1.0;
        double d3 = vec3d2.z - vec3d.z;
        double d4 = MathHelper.sqrt((double)(d * d + d3 * d3));
        float f = (float)MathHelper.wrapDegrees((double)(Math.toDegrees(Math.atan2(d3, d)) - 90.0));
        float f2 = (float)MathHelper.wrapDegrees((double)Math.toDegrees(Math.atan2(d2, d4)));
        if (f2 > 90.0f) {
            f2 = 90.0f;
        } else if (f2 < -90.0f) {
            f2 = -90.0f;
        }
        return new float[]{f, f2};
    }
}