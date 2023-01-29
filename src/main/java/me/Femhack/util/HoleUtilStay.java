package me.Femhack.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HoleUtilStay {
    public static final /* synthetic */ List<BlockPos> holeBlocks;
    public static final /* synthetic */ Vec3d[] cityOffsets;
    private static /* synthetic */ Minecraft mc;

    static {
        holeBlocks = Arrays.asList(new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1)});
        mc = Minecraft.getMinecraft();
        cityOffsets = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0)};
    }

    public static BlockPos is2Hole(BlockPos blockPos) {
        if (HoleUtilStay.isHole(blockPos)) {
            return null;
        }
        BlockPos blockPos2 = blockPos;
        BlockPos blockPos3 = null;
        int n = 0;
        int n2 = 0;
        if (HoleUtilStay.mc.world.getBlockState(blockPos).getBlock() != Blocks.AIR) {
            return null;
        }
        for (BlockPos blockPos4 : holeBlocks) {
            if (HoleUtilStay.mc.world.getBlockState(blockPos2.add((Vec3i)blockPos4)).getBlock() != Blocks.AIR || blockPos2.add((Vec3i)blockPos4) == new BlockPos(blockPos4.getX(), blockPos4.getY() - 1, blockPos4.getZ())) continue;
            blockPos3 = blockPos2.add((Vec3i)blockPos4);
            ++n;
        }
        if (n == 1) {
            for (BlockPos blockPos4 : holeBlocks) {
                if (HoleUtilStay.mc.world.getBlockState(blockPos2.add((Vec3i)blockPos4)).getBlock() != Blocks.BEDROCK && HoleUtilStay.mc.world.getBlockState(blockPos2.add((Vec3i)blockPos4)).getBlock() != Blocks.OBSIDIAN) continue;
                ++n2;
            }
            for (BlockPos blockPos4 : holeBlocks) {
                if (HoleUtilStay.mc.world.getBlockState(blockPos3.add((Vec3i)blockPos4)).getBlock() != Blocks.BEDROCK && HoleUtilStay.mc.world.getBlockState(blockPos3.add((Vec3i)blockPos4)).getBlock() != Blocks.OBSIDIAN) continue;
                ++n2;
            }
        }
        if (n2 == 8) {
            return blockPos3;
        }
        return null;
    }

    public static Vec3d interpolateEntity(Entity entity) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getRenderPartialTicks(), entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getRenderPartialTicks(), entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getRenderPartialTicks());
    }

    public static boolean isInHole() {
        Vec3d vec3d = interpolateEntity((Entity)HoleUtilStay.mc.player);
        BlockPos blockPos = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
        int n = 0;
        for (BlockPos blockPos2 : holeBlocks) {
            if (!isHard(HoleUtilStay.mc.world.getBlockState(blockPos.add((Vec3i)blockPos2)).getBlock())) continue;
            ++n;
        }
        return n == 5;
    }

    public static HashMap<BlockOffset, BlockSafety> getUnsafeSides(BlockPos blockPos) {
        HashMap<BlockOffset, BlockSafety> hashMap = new HashMap<BlockOffset, BlockSafety>();
        BlockSafety blockSafety = HoleUtilStay.isBlockSafe(HoleUtilStay.mc.world.getBlockState(BlockOffset.DOWN.offset(blockPos)).getBlock());
        if (blockSafety != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.DOWN, blockSafety);
        }
        if ((blockSafety = HoleUtilStay.isBlockSafe(HoleUtilStay.mc.world.getBlockState(BlockOffset.NORTH.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.NORTH, blockSafety);
        }
        if ((blockSafety = HoleUtilStay.isBlockSafe(HoleUtilStay.mc.world.getBlockState(BlockOffset.SOUTH.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.SOUTH, blockSafety);
        }
        if ((blockSafety = HoleUtilStay.isBlockSafe(HoleUtilStay.mc.world.getBlockState(BlockOffset.EAST.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.EAST, blockSafety);
        }
        if ((blockSafety = HoleUtilStay.isBlockSafe(HoleUtilStay.mc.world.getBlockState(BlockOffset.WEST.offset(blockPos)).getBlock())) != BlockSafety.UNBREAKABLE) {
            hashMap.put(BlockOffset.WEST, blockSafety);
        }
        return hashMap;
    }

    public static boolean isHard(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK;
    }

    public static boolean isHole(BlockPos blockPos) {
        BlockPos blockPos2 = blockPos;
        int n = 0;
        for (BlockPos blockPos3 : holeBlocks) {
            if (!isHard(HoleUtilStay.mc.world.getBlockState(blockPos2.add((Vec3i)blockPos3)).getBlock())) continue;
            ++n;
        }
        return n == 5;
    }

    public static boolean is2securityHole(BlockPos blockPos) {
        if (HoleUtilStay.is2Hole(blockPos) == null) {
            return false;
        }
        BlockPos blockPos2 = blockPos;
        BlockPos blockPos3 = HoleUtilStay.is2Hole(blockPos);
        int n = 0;
        for (BlockPos blockPos4 : holeBlocks) {
            if (HoleUtilStay.mc.world.getBlockState(blockPos2.add((Vec3i)blockPos4)).getBlock() != Blocks.BEDROCK) continue;
            ++n;
        }
        for (BlockPos blockPos4 : holeBlocks) {
            if (HoleUtilStay.mc.world.getBlockState(blockPos3.add((Vec3i)blockPos4)).getBlock() != Blocks.BEDROCK) continue;
            ++n;
        }
        return n == 8;
    }

    public static BlockSafety isBlockSafe(Block block) {
        if (block == Blocks.BEDROCK) {
            return BlockSafety.UNBREAKABLE;
        }
        if (block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL) {
            return BlockSafety.RESISTANT;
        }
        return BlockSafety.BREAKABLE;
    }

    public static enum BlockSafety {
        UNBREAKABLE,
        RESISTANT,
        BREAKABLE;

    }

    public static enum BlockOffset {
        DOWN(0, -1, 0),
        UP(0, 1, 0),
        NORTH(0, 0, -1),
        EAST(1, 0, 0),
        SOUTH(0, 0, 1),
        WEST(-1, 0, 0);

        private final /* synthetic */ int y;
        private final /* synthetic */ int x;
        private final /* synthetic */ int z;

        public BlockPos left(BlockPos blockPos, int n) {
            return blockPos.add(this.z * n, 0, -this.x * n);
        }

        public BlockPos right(BlockPos blockPos, int n) {
            return blockPos.add(-this.z * n, 0, this.x * n);
        }

        private BlockOffset(int n2, int n3, int n4) {
            this.x = n2;
            this.y = n3;
            this.z = n4;
        }

        public BlockPos backward(BlockPos blockPos, int n) {
            return blockPos.add(-this.x * n, 0, -this.z * n);
        }

        public BlockPos forward(BlockPos blockPos, int n) {
            return blockPos.add(this.x * n, 0, this.z * n);
        }

        public BlockPos offset(BlockPos blockPos) {
            return blockPos.add(this.x, this.y, this.z);
        }
    }
}