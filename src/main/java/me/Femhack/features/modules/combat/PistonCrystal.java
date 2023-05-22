package me.Femhack.features.modules.combat;

import java.util.ArrayList;
import java.util.List;

import me.Femhack.event.events.PacketEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.manager.ModuleManager;
import me.Femhack.util.BlockUtil;
import me.Femhack.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PistonCrystal
        extends Module {
    private /* synthetic */ EntityPlayer closestTarget;
    private static /* synthetic */ PistonCrystal instance;
    private /* synthetic */ boolean hasMoved;
    private /* synthetic */ int oldSlot;
    public /* synthetic */ Setting<Integer> pistonDelay;
    private /* synthetic */ boolean firstRun;
    private /* synthetic */ int stuck;
    public /* synthetic */ Setting<Boolean> rotate;
    private /* synthetic */ int stage;
    public /* synthetic */ Setting<Double> enemyRange;
    private /* synthetic */ boolean noMaterials;
    public /* synthetic */ Setting<Integer> trapDelay;
    public /* synthetic */ Setting<Integer> startDelay;
    /* synthetic */ double[] coordsD;
    private static /* synthetic */ double yaw;
    public /* synthetic */ Setting<Integer> hitDelay;
    private /* synthetic */ structureTemp toPlace;
    public static /* synthetic */ ModuleManager moduleManager;
    private /* synthetic */ int delayTimeTicks;
    private /* synthetic */ int[] slot_mat;
    /* synthetic */ boolean broken;
    private static /* synthetic */ double pitch;
    public /* synthetic */ Setting<Boolean> blockPlayer;
    public /* synthetic */ Setting<Integer> blocksPerTick;
    /* synthetic */ boolean brokenRedstoneTorch;
    /* synthetic */ Double[][] sur_block;
    private static /* synthetic */ boolean isSpoofingAngles;
    /* synthetic */ boolean brokenCrystalBug;
    private /* synthetic */ boolean enoughSpace;
    /* synthetic */ int[][] disp_surblock;
    private /* synthetic */ boolean isSneaking;
    public /* synthetic */ Setting<Boolean> antiWeakness;
    private /* synthetic */ int[] delayTable;
    private /* synthetic */ boolean isHole;
    public /* synthetic */ Setting<BreakModes> breakMode;
    public /* synthetic */ Setting<Integer> crystalDelay;

    private boolean is_in_hole() {
        this.sur_block = new Double[][]{{this.closestTarget.posX + 1.0, this.closestTarget.posY, this.closestTarget.posZ}, {this.closestTarget.posX - 1.0, this.closestTarget.posY, this.closestTarget.posZ}, {this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ + 1.0}, {this.closestTarget.posX, this.closestTarget.posY, this.closestTarget.posZ - 1.0}};
        return !(this.get_block(this.sur_block[0][0], this.sur_block[0][1], this.sur_block[0][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[1][0], this.sur_block[1][1], this.sur_block[1][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[2][0], this.sur_block[2][1], this.sur_block[2][2]) instanceof BlockAir) && !(this.get_block(this.sur_block[3][0], this.sur_block[3][1], this.sur_block[3][2]) instanceof BlockAir);
    }

    @Override
    public void onUpdate() {
        if (PistonCrystal.mc.player == null) {
            this.disable();
            return;
        }
        if (this.firstRun) {
            this.closestTarget = EntityUtil.getTargetDouble(this.enemyRange.getValue());
            if (this.closestTarget == null) {
                return;
            }
            this.firstRun = false;
            if (this.getMaterialsSlot()) {
                if (this.is_in_hole()) {
                    this.enoughSpace = this.createStructure();
                } else {
                    this.isHole = false;
                }
            } else {
                this.noMaterials = true;
            }
        } else {
            if (this.delayTable == null) {
                return;
            }
            if (this.delayTimeTicks < this.delayTable[this.stage]) {
                ++this.delayTimeTicks;
                return;
            }
            this.delayTimeTicks = 0;
        }
        if (this.noMaterials || !this.isHole || !this.enoughSpace || this.hasMoved) {
            this.disable();
            return;
        }
        if (this.trapPlayer()) {
            if (this.stage == 1) {
                BlockPos blockPos = this.compactBlockPos(this.stage);
                this.placeBlock(blockPos, this.stage, this.toPlace.offsetX, this.toPlace.offsetZ);
                ++this.stage;
            } else if (this.stage == 2) {
                BlockPos blockPos = this.compactBlockPos(this.stage - 1);
                if (!(this.get_block(blockPos.getX(), blockPos.getY(), blockPos.getZ()) instanceof BlockPistonBase)) {
                    --this.stage;
                } else {
                    BlockPos blockPos2 = this.compactBlockPos(this.stage);
                    if (this.placeBlock(blockPos2, this.stage, this.toPlace.offsetX, this.toPlace.offsetZ)) {
                        ++this.stage;
                    }
                }
            } else if (this.stage == 3) {
                for (Entity entity : PistonCrystal.mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityEnderCrystal) || (int)entity.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)entity.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                    --this.stage;
                    break;
                }
                if (this.stage == 3) {
                    BlockPos blockPos = this.compactBlockPos(this.stage);
                    this.placeBlock(blockPos, this.stage, this.toPlace.offsetX, this.toPlace.offsetZ);
                    ++this.stage;
                }
            } else if (this.stage == 4) {
                this.breakCrystal();
            }
        }
    }

    public static PistonCrystal getInstance() {
        if (instance == null) {
            instance = new PistonCrystal();
        }
        return instance;
    }

    private boolean createStructure() {
        structureTemp structureTemp2 = new structureTemp(Double.MAX_VALUE, 0, null);
        int n = 0;
        int[] arrn = new int[]{(int)PistonCrystal.mc.player.posX, (int)PistonCrystal.mc.player.posY, (int)PistonCrystal.mc.player.posZ};
        int[] arrn2 = arrn;
        int[] arrn3 = arrn2;
        int[] arrn4 = arrn3;
        if ((double)arrn4[1] - this.closestTarget.posY > -1.0) {
            for (Double[] arrdouble : this.sur_block) {
                double d = 0.0;
                double[] arrd = new double[]{arrdouble[0], arrdouble[1] + 1.0, arrdouble[2]};
                BlockPos blockPos = new BlockPos(arrd[0], arrd[1], arrd[2]);
                double d2 = PistonCrystal.mc.player.getDistance(arrd[0], arrd[1], arrd[2]);
                if (d < structureTemp2.distance && (blockPos.getY() != arrn4[1] || arrn4[0] != blockPos.getX() || Math.abs(arrn4[2] - blockPos.getZ()) > 3 && arrn4[2] != blockPos.getZ() || Math.abs(arrn4[0] - blockPos.getX()) > 3)) {
                    arrdouble[1] = arrdouble[1] + 1.0;
                    if (this.get_block(arrd[0], arrd[1], arrd[2]) instanceof BlockAir) {
                        double[] arrd2 = new double[]{arrd[0] + (double)this.disp_surblock[n][0], arrd[1], arrd[2] + (double)this.disp_surblock[n][2]};
                        Block block = this.get_block(arrd2[0], arrd2[1], arrd2[2]);
                        if ((block instanceof BlockAir || block instanceof BlockPistonBase) && this.someoneInCoords(arrd2[0], arrd2[1], arrd2[2])) {
                            boolean bl;
                            boolean bl2 = false;
                            if (!this.rotate.getValue().booleanValue() || ((int)arrd2[0] == arrn4[0] ? this.closestTarget.posZ > PistonCrystal.mc.player.posZ != this.closestTarget.posZ > arrd2[2] || Math.abs((int)this.closestTarget.posZ - (int)PistonCrystal.mc.player.posZ) == 1 : (int)arrd2[2] != arrn4[2] || (this.closestTarget.posX > PistonCrystal.mc.player.posX != this.closestTarget.posX > arrd2[0] || Math.abs((int)this.closestTarget.posX - (int)PistonCrystal.mc.player.posX) == 1) && (Math.abs((int)this.closestTarget.posX - (int)PistonCrystal.mc.player.posX) <= 1 || arrd2[0] > this.closestTarget.posX != (double)arrn4[0] > this.closestTarget.posX))) {
                                bl2 = true;
                            }
                            if (bl = bl2) {
                                boolean bl3;
                                boolean bl4 = false;
                                if (!this.rotate.getValue().booleanValue() || (arrn4[0] == (int)this.closestTarget.posX || arrn4[2] == (int)this.closestTarget.posZ ? PistonCrystal.mc.player.getDistance(arrd[0], arrd[1], arrd[2]) <= 3.5 || arrn4[0] == (int)arrd[0] || arrn4[2] == (int)arrd[2] : arrn4[0] != (int)arrd2[0] || Math.abs((int)this.closestTarget.posZ - (int)PistonCrystal.mc.player.posZ) == 1 || arrn4[2] == (int)arrd2[2] && Math.abs((int)this.closestTarget.posZ - (int)PistonCrystal.mc.player.posZ) != 1)) {
                                    bl4 = true;
                                }
                                if (bl3 = bl4) {
                                    int[] object = null;
                                    for (int[] arrn5 : this.disp_surblock) {
                                        double[] arrd3 = new double[]{arrdouble[0] + (double)this.disp_surblock[n][0] + (double)arrn5[0], arrdouble[1], arrdouble[2] + (double)this.disp_surblock[n][2] + (double)arrn5[2]};
                                        int[] arrn6 = new int[]{(int)arrd3[0], (int)arrd3[1], (int)arrd3[2]};
                                        int[] arrn7 = new int[]{(int)arrd[0], (int)arrd[1], (int)arrd[2]};
                                        if (!(this.get_block(arrd3[0], arrd3[1], arrd3[2]) instanceof BlockAir) || arrn6[0] == arrn7[0] && arrn6[1] == arrn7[1] && arrn7[2] == arrn6[2] || !this.someoneInCoords(arrd3[0], arrd3[1], arrd3[2])) continue;
                                        object = arrn5;
                                        break;
                                    }
                                    if (object != null) {
                                        float f;
                                        float f2;
                                        float f3;
                                        ArrayList<Vec3d> arrayList = new ArrayList<Vec3d>();
                                        int n2 = 0;
                                        if (this.get_block(arrdouble[0] + (double)this.disp_surblock[n][0], arrdouble[1] - 1.0, arrdouble[2] + (double)this.disp_surblock[n][2]) instanceof BlockAir) {
                                            arrayList.add(new Vec3d((double)(this.disp_surblock[n][0] * 2), (double)this.disp_surblock[n][1], (double)(this.disp_surblock[n][2] * 2)));
                                            ++n2;
                                        }
                                        if (this.get_block(arrdouble[0] + (double)this.disp_surblock[n][0] + (double)object[0], arrdouble[1] - 1.0, arrdouble[2] + (double)this.disp_surblock[n][2] + (double)object[2]) instanceof BlockAir) {
                                            arrayList.add(new Vec3d((double)(this.disp_surblock[n][0] * 2 + object[0]), (double)this.disp_surblock[n][1], (double)(this.disp_surblock[n][2] * 2 + object[2])));
                                            ++n2;
                                        }
                                        arrayList.add(new Vec3d((double)(this.disp_surblock[n][0] * 2), (double)(this.disp_surblock[n][1] + 1), (double)(this.disp_surblock[n][2] * 2)));
                                        arrayList.add(new Vec3d((double)this.disp_surblock[n][0], (double)(this.disp_surblock[n][1] + 1), (double)this.disp_surblock[n][2]));
                                        arrayList.add(new Vec3d((double)(this.disp_surblock[n][0] * 2 + object[0]), (double)(this.disp_surblock[n][1] + 1), (double)(this.disp_surblock[n][2] * 2 + object[2])));
                                        if (this.disp_surblock[n][0] != 0) {
                                            f2 = f3 = this.rotate.getValue() != false ? (float)this.disp_surblock[n][0] / 2.0f : (float)this.disp_surblock[n][0];
                                            f = this.rotate.getValue().booleanValue() ? (PistonCrystal.mc.player.getDistanceSq(arrd2[0], arrd2[1], arrd2[2] + 0.5) > PistonCrystal.mc.player.getDistanceSq(arrd2[0], arrd2[1], arrd2[2] - 0.5) ? -0.5f : 0.5f) : (float)this.disp_surblock[n][2];
                                        } else {
                                            f2 = f = this.rotate.getValue() != false ? (float)this.disp_surblock[n][2] / 2.0f : (float)this.disp_surblock[n][2];
                                            f3 = this.rotate.getValue().booleanValue() ? (PistonCrystal.mc.player.getDistanceSq(arrd2[0] + 0.5, arrd2[1], arrd2[2]) > PistonCrystal.mc.player.getDistanceSq(arrd2[0] - 0.5, arrd2[1], arrd2[2]) ? -0.5f : 0.5f) : (float)this.disp_surblock[n][0];
                                        }
                                        structureTemp2.replaceValues(d2, n2, arrayList, -1, f3, f);
                                    }
                                }
                            }
                        }
                    }
                }
                ++n;
            }
            if (structureTemp2.to_place != null) {
                if (this.blockPlayer.getValue().booleanValue()) {
                    Vec3d vec3d = structureTemp2.to_place.get(structureTemp2.supportBlock + 1);
                    int[] arrn7 = new int[]{(int)(-vec3d.x), (int)vec3d.y, (int)(-vec3d.z)};
                    structureTemp2.to_place.add(0, new Vec3d(0.0, 2.0, 0.0));
                    structureTemp2.to_place.add(0, new Vec3d((double)arrn7[0], (double)(arrn7[1] + 1), (double)arrn7[2]));
                    structureTemp2.to_place.add(0, new Vec3d((double)arrn7[0], (double)arrn7[1], (double)arrn7[2]));
                    structureTemp2.supportBlock += 3;
                }
                this.toPlace = structureTemp2;
                return true;
            }
        }
        return false;
    }

    private static void setYawAndPitch(float f, float f2) {
        yaw = f;
        pitch = f2;
        isSpoofingAngles = true;
    }

    private void breakCrystal(Entity entity) {
        PistonCrystal.mc.playerController.attackEntity((EntityPlayer)PistonCrystal.mc.player, entity);
        PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    private boolean someoneInCoords(double d, double d2, double d3) {
        int n = (int)d;
        int n2 = (int)d2;
        int n3 = (int)d3;
        for (EntityPlayer entityPlayer : PistonCrystal.mc.world.playerEntities) {
            if ((int)entityPlayer.posX != n || (int)entityPlayer.posZ != n3 || (int)entityPlayer.posY < n2 - 1 || (int)entityPlayer.posY > n2 + 1) continue;
            return false;
        }
        return true;
    }

    public BlockPos compactBlockPos(int n) {
        BlockPos blockPos = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + n - 1));
        return new BlockPos(this.closestTarget.getPositionVector()).add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public void onDisable() {
        if (PistonCrystal.mc.player == null) {
            return;
        }
        if (this.isSneaking) {
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)PistonCrystal.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        if (this.oldSlot != PistonCrystal.mc.player.inventory.currentItem && this.oldSlot != -1) {
            PistonCrystal.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.noMaterials = false;
        this.firstRun = true;
    }

    private boolean getMaterialsSlot() {
        int n;
        if (PistonCrystal.mc.player.getHeldItemOffhand().getItem() instanceof ItemEndCrystal) {
            this.slot_mat[2] = 11;
        }
        for (n = 0; n < 9; ++n) {
            ItemStack object = PistonCrystal.mc.player.inventory.getStackInSlot(n);
            if (object == ItemStack.EMPTY) continue;
            if (object.getItem() instanceof ItemEndCrystal) {
                this.slot_mat[2] = n;
                continue;
            }
            if (this.antiWeakness.getValue().booleanValue() && object.getItem() instanceof ItemSword) {
                this.slot_mat[4] = n;
                continue;
            }
            if (!(object.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)object.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                this.slot_mat[0] = n;
                continue;
            }
            if (block instanceof BlockPistonBase) {
                this.slot_mat[1] = n;
                continue;
            }
            if (!(block instanceof BlockRedstoneTorch) && !block.translationKey.equals("blockRedstone")) continue;
            this.slot_mat[3] = n;
        }
        n = 0;
        for (int n2 : this.slot_mat) {
            if (n2 == -1) continue;
            ++n;
        }
        return n == 4 + (this.antiWeakness.getValue() != false ? 1 : 0);
    }

    private void lookAtPacket(double d, double d2, double d3, EntityPlayer entityPlayer) {
        double[] arrd = PistonCrystal.calculateLookAt(d, d2, d3, entityPlayer);
        PistonCrystal.setYawAndPitch((float)arrd[0], (float)arrd[1]);
    }

    @Override
    public void onEnable() {
        this.coordsD = new double[3];
        this.delayTable = new int[]{this.startDelay.getValue(), this.trapDelay.getValue(), this.pistonDelay.getValue(), this.crystalDelay.getValue(), this.hitDelay.getValue()};
        this.toPlace = new structureTemp(0.0, 0, null);
        boolean bl = true;
        this.firstRun = true;
        this.isHole = true;
        boolean bl2 = false;
        this.brokenRedstoneTorch = false;
        this.brokenCrystalBug = false;
        this.broken = false;
        this.hasMoved = false;
        this.slot_mat = new int[]{-1, -1, -1, -1, -1};
        boolean bl3 = false;
        this.stuck = 0;
        this.delayTimeTicks = 0;
        this.stage = 0;
        if (PistonCrystal.mc.player == null) {
            this.disable();
            return;
        }
        this.oldSlot = PistonCrystal.mc.player.inventory.currentItem;
    }

    private Block get_block(double d, double d2, double d3) {
        return PistonCrystal.mc.world.getBlockState(new BlockPos(d, d2, d3)).getBlock();
    }

    private void breakCrystalPiston(Entity entity) {
        if (this.antiWeakness.getValue().booleanValue()) {
            PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[4];
        }
        if (this.rotate.getValue().booleanValue()) {
            this.lookAtPacket(entity.posX, entity.posY, entity.posZ, (EntityPlayer)PistonCrystal.mc.player);
        }
        if (this.breakMode.getValue().equals((Object)BreakModes.swing)) {
            this.breakCrystal(entity);
            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
            PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        if (this.rotate.getValue().booleanValue()) {
            PistonCrystal.resetRotation();
        }
    }

    public PistonCrystal() {
        super("PistonCrystal", "Use Pistons and Crystals to pvp.", Module.Category.COMBAT, true, false, false);
        this.rotate = this.register(new Setting<Boolean>("Rotate", true));
        this.blockPlayer = this.register(new Setting<Boolean>("TrapPlayer", false));
        this.antiWeakness = this.register(new Setting<Boolean>("AntiWeakness", false));
        this.enemyRange = this.register(new Setting<Double>("Range", 6.0, 0.0, 6.0));
        this.blocksPerTick = this.register(new Setting<Integer>("BlocksPerTick", 4, 0, 20));
        this.startDelay = this.register(new Setting<Integer>("StartDelay", 0, 0, 20));
        this.trapDelay = this.register(new Setting<Integer>("TrapDelay", 1, 0, 20));
        this.pistonDelay = this.register(new Setting<Integer>("PistonDelay", 1, 0, 20));
        this.crystalDelay = this.register(new Setting<Integer>("CrystalDelay", 2, 0, 20));
        this.hitDelay = this.register(new Setting<Integer>("HitDelay", 6, 0, 20));
        this.breakMode = this.register(new Setting<BreakModes>("Break Mode", BreakModes.swing));
        this.isSneaking = false;
        this.firstRun = false;
        this.noMaterials = false;
        this.hasMoved = false;
        this.isHole = true;
        this.enoughSpace = true;
        this.oldSlot = -1;
        this.disp_surblock = new int[][]{{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
        this.stuck = 0;
        instance = this;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        Object t = send.getPacket();
        if (t instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)t).yaw = (float)yaw;
            ((CPacketPlayer)t).pitch = (float)pitch;
        }
    }

    private boolean placeBlock(BlockPos blockPos, int n, double d, double d2) {
        Block block = PistonCrystal.mc.world.getBlockState(blockPos).getBlock();
        EnumFacing enumFacing = BlockUtil.getPlaceableSide(blockPos);
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        if (enumFacing == null) {
            return false;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        if (!BlockUtil.canBeClicked(blockPos2)) {
            return false;
        }
        Vec3d vec3d = new Vec3d((Vec3i)blockPos2).add(0.5 + d, 1.0, 0.5 + d2).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block2 = PistonCrystal.mc.world.getBlockState(blockPos2).getBlock();
        if (PistonCrystal.mc.player.inventory.getStackInSlot(this.slot_mat[n]) != ItemStack.EMPTY) {
            if (PistonCrystal.mc.player.inventory.currentItem != this.slot_mat[n]) {
                int n2 = PistonCrystal.mc.player.inventory.currentItem = this.slot_mat[n] == 11 ? PistonCrystal.mc.player.inventory.currentItem : this.slot_mat[n];
            }
            if (!this.isSneaking && BlockUtil.blackList.contains((Object)block2) || BlockUtil.shulkerList.contains((Object)block2)) {
                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)PistonCrystal.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                this.isSneaking = true;
            }
            if (this.rotate.getValue().booleanValue() || n == 1) {
                Vec3d vec3d2 = vec3d;
                if (!this.rotate.getValue().booleanValue() && n == 1) {
                    vec3d2 = new Vec3d(PistonCrystal.mc.player.posX + d, PistonCrystal.mc.player.posY, PistonCrystal.mc.player.posZ + d2);
                }
                BlockUtil.faceVectorPacketInstant(vec3d2);
            }
            EnumHand enumHand = EnumHand.MAIN_HAND;
            if (this.slot_mat[n] == 11) {
                enumHand = EnumHand.OFF_HAND;
            }
            PistonCrystal.mc.playerController.processRightClickBlock(PistonCrystal.mc.player, PistonCrystal.mc.world, blockPos2, enumFacing2, vec3d, enumHand);
            PistonCrystal.mc.player.swingArm(enumHand);
            return true;
        }
        return false;
    }

    public static double[] calculateLookAt(double d, double d2, double d3, EntityPlayer entityPlayer) {
        double d4 = entityPlayer.posX - d;
        double d5 = entityPlayer.posY - d2;
        double d6 = entityPlayer.posZ - d3;
        double d7 = Math.sqrt(d4 * d4 + d5 * d5 + d6 * d6);
        double d8 = Math.asin(d5 /= d7);
        double d9 = Math.atan2(d6 /= d7, d4 /= d7);
        d8 = d8 * 180.0 / Math.PI;
        d9 = d9 * 180.0 / Math.PI;
        return new double[]{d9 += 90.0, d8};
    }

    private boolean trapPlayer() {
        int n = 0;
        int n2 = 0;
        if (this.toPlace.to_place.size() <= 0 || this.toPlace.supportBlock <= 0) {
            this.stage = this.stage == 0 ? 1 : this.stage;
            return true;
        }
        do {
            BlockPos blockPos = new BlockPos(this.toPlace.to_place.get(n));
            BlockPos blockPos2 = new BlockPos(this.closestTarget.getPositionVector()).add(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            if (this.placeBlock(blockPos2, 0, 0.0, 0.0)) {
                ++n2;
            }
            if (n2 != this.blocksPerTick.getValue()) continue;
            return false;
        } while (++n < this.toPlace.supportBlock);
        this.stage = this.stage == 0 ? 1 : this.stage;
        return true;
    }

    public void breakCrystal() {
        Object object = null;
        for (Object object2 : PistonCrystal.mc.world.loadedEntityList) {
            if (!(object2 instanceof EntityEnderCrystal) || (((Entity)object2).posX != (double)((int)((Entity)object2).posX) && (int)((Entity)object2).posX != (int)this.closestTarget.posX || (int)((double)((int)((Entity)object2).posX) - 0.1) != (int)this.closestTarget.posX && (int)((double)((int)((Entity)object2).posX) + 0.1) != (int)this.closestTarget.posX || (int)((Entity)object2).posZ != (int)this.closestTarget.posZ) && (((Entity)object2).posZ != (double)((int)((Entity)object2).posZ) && (int)((Entity)object2).posZ != (int)this.closestTarget.posZ || (int)((double)((int)((Entity)object2).posZ) - 0.1) != (int)this.closestTarget.posZ && (int)((double)((int)((Entity)object2).posZ) + 0.1) != (int)this.closestTarget.posZ || (int)((Entity)object2).posX != (int)this.closestTarget.posX)) continue;
            object = object2;
        }
        if (this.broken && object == null) {
            boolean bl = false;
            this.stuck = 0;
            this.stage = 0;
            this.broken = false;
        }
        if (object != null) {
            this.breakCrystalPiston((Entity)object);
            this.broken = true;
        } else if (++this.stuck >= 35) {
            boolean bl = false;
            for (Object object3 : PistonCrystal.mc.world.loadedEntityList) {
                if (!(object3 instanceof EntityEnderCrystal) || (int)((Entity)object3).posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)((Entity)object3).posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                bl = true;
                break;
            }
            if (!bl) {
                BlockPos object3;
                BlockPos object2;
                object2 = new BlockPos(this.toPlace.to_place.get(this.toPlace.supportBlock + 2));
                object3 = new BlockPos(this.closestTarget.getPositionVector()).add(object2.getX(), object2.getY(), object2.getZ());
                if (this.brokenRedstoneTorch && this.get_block(object3.getX(), object3.getY(), object3.getZ()) instanceof BlockAir) {
                    this.stage = 1;
                    this.brokenRedstoneTorch = false;
                } else {
                    EnumFacing enumFacing = BlockUtil.getPlaceableSide((BlockPos)object3);
                    if (enumFacing != null) {
                        if (this.rotate.getValue().booleanValue()) {
                            BlockPos blockPos = ((BlockPos) object3).offset(enumFacing);
                            EnumFacing enumFacing2 = enumFacing.getOpposite();
                            Vec3d vec3d = new Vec3d((Vec3i)blockPos).add(0.5, 1.0, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
                            BlockUtil.faceVectorPacketInstant(vec3d);
                        }
                        PistonCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                        PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, (BlockPos)object3, enumFacing));
                        PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, (BlockPos)object3, enumFacing));
                        this.brokenRedstoneTorch = true;
                    }
                }
            } else {
                boolean bl2 = false;
                for (Entity entity : PistonCrystal.mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityEnderCrystal) || (int)entity.posX != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).x || (int)entity.posZ != (int)this.toPlace.to_place.get((int)(this.toPlace.supportBlock + 1)).z) continue;
                    bl2 = true;
                    break;
                }
                boolean bl3 = false;
                this.stuck = 0;
                this.stage = 0;
                this.brokenCrystalBug = false;
                if (bl2) {
                    this.breakCrystalPiston(null);
                    this.brokenCrystalBug = true;
                }
            }
        }
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = PistonCrystal.mc.player.rotationYaw;
            pitch = PistonCrystal.mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    private static enum BreakModes {
        packet,
        swing;

    }

    static class structureTemp {
        public /* synthetic */ int direction;
        public /* synthetic */ double distance;
        public /* synthetic */ float offsetZ;
        public /* synthetic */ int supportBlock;
        public /* synthetic */ List<Vec3d> to_place;
        public /* synthetic */ float offsetX;

        public structureTemp(double d, int n, List<Vec3d> list) {
            this.distance = d;
            this.supportBlock = n;
            this.to_place = list;
            this.direction = -1;
        }

        public void replaceValues(double d, int n, List<Vec3d> list, int n2, float f, float f2) {
            this.distance = d;
            this.supportBlock = n;
            this.to_place = list;
            this.direction = n2;
            this.offsetX = f;
            this.offsetZ = f2;
        }
    }
}