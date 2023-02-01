package me.Femhack.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.Femhack.util.TestUtil;
import me.Femhack.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class WorldUtils
        implements Util {
    public static final /* synthetic */ ArrayList<Block> empty;
    public static final /* synthetic */ List<Block> blackList;
    public static /* synthetic */ List<Block> emptyBlocks;

    public static EnumFacing getEnumFacing(boolean bl, BlockPos blockPos) {
        RayTraceResult rayTraceResult = WorldUtils.mc.world.rayTraceBlocks(new Vec3d(WorldUtils.mc.player.posX, WorldUtils.mc.player.posY + (double)WorldUtils.mc.player.getEyeHeight(), WorldUtils.mc.player.posZ), new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() - 0.5, (double)blockPos.getZ() + 0.5));
        if (blockPos.getY() == 255) {
            return EnumFacing.DOWN;
        }
        if (bl) {
            return rayTraceResult == null || rayTraceResult.sideHit == null ? EnumFacing.UP : rayTraceResult.sideHit;
        }
        return EnumFacing.UP;
    }

    public static boolean isWithin(double d, Vec3d vec3d, Vec3d vec3d2) {
        return vec3d.squareDistanceTo(vec3d2) <= d * d;
    }

    public static boolean canBeClicked(BlockPos blockPos) {
        return WorldUtils.mc.world.getBlockState(blockPos).getBlock().canCollideCheck(WorldUtils.mc.world.getBlockState(blockPos), false);
    }

    public static EnumFacing getPlaceableSide(BlockPos blockPos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            IBlockState iBlockState;
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (!WorldUtils.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(WorldUtils.mc.world.getBlockState(blockPos2), false) || (iBlockState = WorldUtils.mc.world.getBlockState(blockPos2)).getBlock().getMaterial(iBlockState).isReplaceable()) continue;
            return enumFacing;
        }
        return null;
    }

    public static void placeBlock(BlockPos blockPos, int n) {
        if (n == -1) {
            return;
        }
        int n2 = WorldUtils.mc.player.inventory.currentItem;
        WorldUtils.mc.player.inventory.currentItem = n;
        WorldUtils.placeBlock(blockPos);
        WorldUtils.mc.player.inventory.currentItem = n2;
    }

    public static double getRange(Vec3d vec3d) {
        return WorldUtils.mc.player.getPositionVector().add(0.0, (double)WorldUtils.mc.player.eyeHeight, 0.0).distanceTo(vec3d);
    }

    static {
        empty = new ArrayList<Block>(Arrays.asList(new Block[]{Blocks.AIR, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.FLOWING_WATER, Blocks.WATER}));
        blackList = Arrays.asList(new Block[]{Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE});
        emptyBlocks = Arrays.asList(new Block[]{Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE});
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float f, int n, boolean bl, boolean bl2, int n2) {
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        int n3 = blockPos.getX();
        int n4 = blockPos.getY();
        int n5 = blockPos.getZ();
        int n6 = n3 - (int)f;
        while ((float)n6 <= (float)n3 + f) {
            int n7 = n5 - (int)f;
            while ((float)n7 <= (float)n5 + f) {
                int n8 = bl2 ? n4 - (int)f : n4;
                while (true) {
                    float f2;
                    float f3 = n8;
                    float f4 = f2 = bl2 ? (float)n4 + f : (float)(n4 + n);
                    if (!(f3 < f2)) break;
                    double d = (n3 - n6) * (n3 - n6) + (n5 - n7) * (n5 - n7) + (bl2 ? (n4 - n8) * (n4 - n8) : 0);
                    if (!(!(d < (double)(f * f)) || bl && d < (double)((f - 1.0f) * (f - 1.0f)))) {
                        arrayList.add(new BlockPos(n6, n8 + n2, n7));
                    }
                    ++n8;
                }
                ++n7;
            }
            ++n6;
        }
        return arrayList;
    }

    public static Block getBlock(BlockPos blockPos) {
        return WorldUtils.mc.world.getBlockState(blockPos).getBlock();
    }

    private boolean place(BlockPos blockPos) {
        boolean bl = WorldUtils.mc.player.isSneaking();
        Block block = WorldUtils.mc.world.getBlockState(blockPos).getBlock();
        if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockFire)) {
            return false;
        }
        EnumFacing enumFacing = WorldUtils.getPlaceableSide(blockPos);
        if (enumFacing == null) {
            return false;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        if (!WorldUtils.canBeClicked(blockPos2)) {
            return false;
        }
        Vec3d vec3d = new Vec3d((Vec3i)blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block2 = WorldUtils.mc.world.getBlockState(blockPos2).getBlock();
        if (!bl && blackList.contains((Object)block2)) {
            WorldUtils.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)WorldUtils.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            bl = true;
        }
        WorldUtils.mc.playerController.processRightClickBlock(WorldUtils.mc.player, WorldUtils.mc.world, blockPos2, enumFacing2, vec3d, EnumHand.MAIN_HAND);
        WorldUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
        return true;
    }

    public static void openBlock(BlockPos blockPos) {
        EnumFacing[] arrenumFacing;
        for (EnumFacing enumFacing : arrenumFacing = EnumFacing.values()) {
            Block block = WorldUtils.mc.world.getBlockState(blockPos.offset(enumFacing)).getBlock();
            if (!emptyBlocks.contains((Object)block)) continue;
            WorldUtils.mc.playerController.processRightClickBlock(WorldUtils.mc.player, WorldUtils.mc.world, blockPos, enumFacing.getOpposite(), new Vec3d((Vec3i)blockPos), EnumHand.MAIN_HAND);
            return;
        }
    }

    public static boolean placeBlock(BlockPos blockPos) {
        if (TestUtil.isBlockEmpty(blockPos)) {
            EnumFacing[] arrenumFacing;
            for (EnumFacing enumFacing : arrenumFacing = EnumFacing.values()) {
                Block block = Util.mc.world.getBlockState(blockPos.offset(enumFacing)).getBlock();
                Vec3d vec3d = new Vec3d((double)blockPos.getX() + 0.5 + (double)enumFacing.getXOffset() * 0.5, (double)blockPos.getY() + 0.5 + (double)enumFacing.getYOffset() * 0.5, (double)blockPos.getZ() + 0.5 + (double)enumFacing.getZOffset() * 0.5);
                if (emptyBlocks.contains((Object)block) || !(Util.mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(vec3d) <= 4.25)) continue;
                float[] arrf = new float[]{Util.mc.player.rotationYaw, Util.mc.player.rotationPitch};
                if (TestUtil.rightclickableBlocks.contains((Object)block)) {
                    Util.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Util.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                }
                Util.mc.playerController.processRightClickBlock(Util.mc.player, Util.mc.world, blockPos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d((Vec3i)blockPos), EnumHand.MAIN_HAND);
                if (TestUtil.rightclickableBlocks.contains((Object)block)) {
                    Util.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Util.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                }
                Util.mc.player.swingArm(EnumHand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }

    public static boolean isOutside(double d, Vec3d vec3d, Vec3d vec3d2) {
        return vec3d.squareDistanceTo(vec3d2) > d * d;
    }
}