package me.Femhack.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.Femhack.util.PlayerUtil;
import me.Femhack.util.Util;
import me.Femhack.util.WorldUtils;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class HoleUtil
        implements Util {
    public static final /* synthetic */ List<BlockPos> holeBlocks;
    public static final /* synthetic */ Vec3d[] awacityOffsets;

    public static Hole getHole(BlockPos blockPos, int n) {
        boolean bl = false;
        for (int i = 0; i < n; ++i) {
            if (WorldUtils.empty.contains((Object)WorldUtils.getBlock(blockPos.add(0, i + 1, 0)))) continue;
            bl = true;
        }
        if (bl) {
            return null;
        }
        if (WorldUtils.empty.contains((Object)WorldUtils.getBlock(blockPos)) && !WorldUtils.empty.contains((Object)WorldUtils.getBlock(blockPos.down()))) {
            EnumFacing blockPos222;
            if (!(!(WorldUtils.getBlock(blockPos.north()) instanceof BlockObsidian) && WorldUtils.getBlock(blockPos.north()) != Blocks.BEDROCK || !(WorldUtils.getBlock(blockPos.south()) instanceof BlockObsidian) && WorldUtils.getBlock(blockPos.south()) != Blocks.BEDROCK || !(WorldUtils.getBlock(blockPos.east()) instanceof BlockObsidian) && WorldUtils.getBlock(blockPos.east()) != Blocks.BEDROCK || !(WorldUtils.getBlock(blockPos.west()) instanceof BlockObsidian) && WorldUtils.getBlock(blockPos.west()) != Blocks.BEDROCK)) {
                if (WorldUtils.getBlock(blockPos.north()) instanceof BlockObsidian || WorldUtils.getBlock(blockPos.east()) instanceof BlockObsidian || WorldUtils.getBlock(blockPos.south()) instanceof BlockObsidian || WorldUtils.getBlock(blockPos.west()) instanceof BlockObsidian) {
                    return new SingleHole(blockPos, material.OBSIDIAN);
                }
                return new SingleHole(blockPos, material.BEDROCK);
            }
            BlockPos[] arrblockPos = new BlockPos[]{blockPos.west(), blockPos.north(), blockPos.east(), blockPos.south()};
            BlockPos blockPos3 = null;
            for (BlockPos blockPos2222 : arrblockPos) {
                if (!WorldUtils.empty.contains((Object)WorldUtils.getBlock(blockPos2222))) continue;
                blockPos3 = blockPos2222;
                break;
            }
            if (blockPos3 == null || WorldUtils.empty.contains((Object)WorldUtils.getBlock(blockPos3.down()))) {
                return null;
            }
            BlockPos[] arrblockPos2 = new BlockPos[]{blockPos3.west(), blockPos3.north(), blockPos3.east(), blockPos3.south()};
            int n2 = 0;
            int n3 = 0;
            blockPos222 = null;
            for (BlockPos blockPos4 : arrblockPos2) {
                if (blockPos4 == blockPos) continue;
                if (WorldUtils.getBlock(blockPos4) instanceof BlockObsidian) {
                    n3 = 1;
                    ++n2;
                }
                if (WorldUtils.getBlock(blockPos4) != Blocks.BEDROCK) continue;
                ++n2;
            }
            for (BlockPos blockPos2 : arrblockPos) {
                if (blockPos2 == blockPos3) continue;
                if (WorldUtils.getBlock(blockPos2) instanceof BlockObsidian) {
                    n3 = 1;
                    ++n2;
                }
                if (WorldUtils.getBlock(blockPos2) != Blocks.BEDROCK) continue;
                ++n2;
            }
            for (EnumFacing blockPos4 : EnumFacing.values()) {
                if (!blockPos.add(blockPos4.getXOffset(), blockPos4.getYOffset(), blockPos4.getZOffset()).equals((Object)blockPos3)) continue;
                blockPos222 = blockPos4;
            }
            if (n2 >= 6) {
                return new DoubleHole(blockPos, blockPos3, n3 != 0 ? material.OBSIDIAN : material.BEDROCK, (EnumFacing)blockPos222);
            }
        }
        return null;
    }

    public static ArrayList<Hole> holes(float f, int n) {
        ArrayList<Hole> arrayList = new ArrayList<Hole>();
        for (BlockPos blockPos : WorldUtils.getSphere(PlayerUtil.getPlayerPosFloored(), f, (int)f, false, true, 0)) {
            boolean bl;
            Hole hole = HoleUtil.getHole(blockPos, n);
            if (hole instanceof QuadHole) {
                bl = false;
                for (Hole hole2 : arrayList) {
                    if (!(hole2 instanceof QuadHole) || !((QuadHole)hole2).contains((QuadHole)hole)) continue;
                    bl = true;
                    break;
                }
                if (bl) continue;
            }
            if (hole instanceof DoubleHole) {
                bl = false;
                for (Hole hole2 : arrayList) {
                    if (!(hole2 instanceof DoubleHole) || !((DoubleHole)hole2).contains((DoubleHole)hole)) continue;
                    bl = true;
                    break;
                }
                if (bl) continue;
            }
            if (hole == null) continue;
            arrayList.add(hole);
        }
        return arrayList;
    }

    static {
        holeBlocks = Arrays.asList(new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1)});
        awacityOffsets = new Vec3d[]{new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0)};
    }

    public static class Hole {
        public /* synthetic */ type type;
        public /* synthetic */ material mat;

        public Hole(type type2, material material2) {
            this.type = type2;
            this.mat = material2;
        }
    }

    public static enum type {
        DOUBLE,
        SINGLE,
        QUAD;

    }

    public static enum material {
        BEDROCK,
        OBSIDIAN;

    }

    public static final class DoubleHole
            extends Hole {
        public /* synthetic */ EnumFacing dir;
        public /* synthetic */ BlockPos pos;
        public /* synthetic */ BlockPos pos1;

        public boolean contains(DoubleHole doubleHole) {
            return doubleHole.pos.equals((Object)this.pos) || doubleHole.pos.equals((Object)this.pos1) || doubleHole.pos1.equals((Object)this.pos) || doubleHole.pos1.equals((Object)this.pos1);
        }

        public boolean contains(BlockPos blockPos) {
            return this.pos == blockPos || this.pos1 == blockPos;
        }

        public DoubleHole(BlockPos blockPos, BlockPos blockPos2, material material2, EnumFacing enumFacing) {
            super(HoleUtil.type.DOUBLE, material2);
            this.pos = blockPos;
            this.pos1 = blockPos2;
            this.dir = enumFacing;
        }

        public boolean equals(DoubleHole doubleHole) {
            return !(!doubleHole.pos1.equals((Object)this.pos) && !doubleHole.pos1.equals((Object)this.pos1) || !doubleHole.pos.equals((Object)this.pos) && !doubleHole.pos.equals((Object)this.pos1));
        }
    }

    public static final class QuadHole
            extends Hole {
        public /* synthetic */ BlockPos pos3;
        public /* synthetic */ BlockPos pos;
        public /* synthetic */ EnumFacing dir;
        public /* synthetic */ BlockPos pos1;
        public /* synthetic */ BlockPos pos2;

        public boolean contains(BlockPos blockPos) {
            return this.pos == blockPos || this.pos1 == this.pos1 || this.pos2 == this.pos2 || this.pos3 == blockPos;
        }

        public QuadHole(BlockPos blockPos, BlockPos blockPos2, BlockPos blockPos3, BlockPos blockPos4, material material2, EnumFacing enumFacing) {
            super(HoleUtil.type.QUAD, material2);
            this.pos = blockPos;
            this.pos1 = blockPos2;
            this.pos2 = blockPos3;
            this.pos3 = blockPos4;
            this.dir = enumFacing;
        }

        public boolean equals(QuadHole quadHole) {
            return quadHole.pos3.equals((Object)this.pos) || quadHole.pos3.equals((Object)this.pos3) || quadHole.pos2.equals((Object)this.pos) || quadHole.pos2.equals((Object)this.pos2) || quadHole.pos1.equals((Object)this.pos) || quadHole.pos1.equals((Object)this.pos1) && (quadHole.pos.equals((Object)this.pos) || quadHole.pos.equals((Object)this.pos3) || quadHole.pos.equals((Object)this.pos2) || quadHole.pos.equals((Object)this.pos1));
        }

        public boolean contains(QuadHole quadHole) {
            return quadHole.pos.equals((Object)this.pos) || quadHole.pos.equals((Object)this.pos1) || quadHole.pos.equals((Object)this.pos2) || quadHole.pos3.equals((Object)this.pos) || quadHole.pos1.equals((Object)this.pos3);
        }
    }

    public static final class SingleHole
            extends Hole {
        public /* synthetic */ BlockPos pos;

        public SingleHole(BlockPos blockPos, material material2) {
            super(HoleUtil.type.SINGLE, material2);
            this.pos = blockPos;
        }
    }
}