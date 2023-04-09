package me.Femhack.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import me.Femhack.Femhack;
import me.Femhack.event.events.PacketEvent;
import me.Femhack.event.events.Render3DEvent;
import me.Femhack.event.events.UpdateWalkingPlayerEvent;
import me.Femhack.features.command.Command;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Bind;
import me.Femhack.features.setting.Setting;
import me.Femhack.manager.RotationManager;
import me.Femhack.mixin.mixins.accessors.ICPacketUseEntity;
import me.Femhack.mixin.mixins.accessors.IEntityPlayerSP;
import me.Femhack.mixin.mixins.accessors.IRenderManager;
import me.Femhack.util.*;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class FemboyAura
        extends Module {
    private /* synthetic */ Vec3d bilateralVec;
    private /* synthetic */ AxisAlignedBB renderBB;
    public /* synthetic */ Setting<TimingMode> timingMode;
    public /* synthetic */ Setting<Boolean> instantdebug;
    public static /* synthetic */ List<String> bb2;
    private /* synthetic */ AtomicBoolean shouldRunThread;
    private final /* synthetic */ Setting<Integer> oBlue;
    public final /* synthetic */ Timer placeTimer;
    private final /* synthetic */ Setting<Integer> bRed;
    public /* synthetic */ Setting<Float> rainbowSaturationAA22;
    public /* synthetic */ Setting<Float> crystalRange;
    public /* synthetic */ Setting<SyncMode> syncMode;
    public /* synthetic */ Setting<Boolean> handleSequential1;
    public /* synthetic */ Setting<Boolean> debugG;
    public /* synthetic */ Setting<Integer> RTimerr;
    private final /* synthetic */ List<RenderPos> positions;
    public /* synthetic */ BlockPos cachePos;
    public /* synthetic */ Setting<Integer> attackFactor;
    public /* synthetic */ Setting<Boolean> check;
    public /* synthetic */ Setting<Float> breakRange;
    public /* synthetic */ Setting<Boolean> limit;
    public /* synthetic */ Setting<Float> suicideHealth;
    public /* synthetic */ Setting<Integer> yawTicks;
    public final /* synthetic */ Timer renderTimeoutTimer;
    public /* synthetic */ Setting<Boolean> protocol;
    private /* synthetic */ BlockPos lastRenderPos;
    public /* synthetic */ Setting<Boolean> terrainIgnore;
    public /* synthetic */ Setting<Float> placeSpeed;
    public /* synthetic */ Setting<Float> faceplaceHealth;
    private final /* synthetic */ Setting<Integer> oGreen;
    public /* synthetic */ Setting<Boolean> placeCrystal1;
    public /* synthetic */ int oldSlotCrystal;
    private static /* synthetic */ float test1;
    public /* synthetic */ BlockPos renderBreakingPos;
    private final /* synthetic */ Setting<Float> lineWidth;
    public /* synthetic */ Setting<RotationMode> rotationMode;
    public final /* synthetic */ Timer renderBreakingTimer;
    private /* synthetic */ BlockPos prevPlacePos;
    private /* synthetic */ Thread thread;
    public /* synthetic */ Setting<Boolean> outline;
    public /* synthetic */ Setting<Boolean> noGapSwitch;
    public /* synthetic */ Setting<Boolean> doinstantdebug;
    private /* synthetic */ BlockPos postPlacePos;
    private /* synthetic */ EntityEnderCrystal postBreakPos;
    private /* synthetic */ EntityPlayer renderTarget;
    private final /* synthetic */ Setting<Boolean> armorBreaker;
    public /* synthetic */ Setting<Float> breakSpeed;
    public final /* synthetic */ Timer breakTimer;
    private final /* synthetic */ Setting<Integer> bBlue;
    public /* synthetic */ boolean isPlacing;
    private final /* synthetic */ Setting<Integer> oAlpha;
    public /* synthetic */ Setting<Boolean> collision;
    public /* synthetic */ Setting<Boolean> noMineSwitch;
    public /* synthetic */ Setting<TargetingMode> targetingMode;
    public /* synthetic */ EntityEnderCrystal inhibitEntity;
    private /* synthetic */ float displayself;
    public /* synthetic */ Setting<Float> yawAngle;
    public /* synthetic */ Setting<Float> swapDelay;
    public /* synthetic */ int oldSlotSword;
    public /* synthetic */ Setting<Float> minPlaceDamage;
    public /* synthetic */ Setting<Float> security;
    public /* synthetic */ Setting<Boolean> box;
    public /* synthetic */ Setting<Float> mergeOffset;
    public final /* synthetic */ Timer swapTimer;
    public /* synthetic */ Timer rotationTimer;
    public /* synthetic */ Setting<Boolean> predictPops;
    private final /* synthetic */ Setting<Pages> setting;
    private final /* synthetic */ Setting<Integer> bAlpha;
    public /* synthetic */ Setting<Float> placeRange;
    public /* synthetic */ Setting<Integer> predictTicks;
    public /* synthetic */ Setting<Float> placeWallsRange;
    private /* synthetic */ AtomicBoolean lastBroken;
    public /* synthetic */ Setting<ACSwapMode> autoSwap;
    private /* synthetic */ Timer renderTargetTimer;
    public /* synthetic */ Setting<Float> breakWallsRange;
    private final /* synthetic */ Timer scatterTimer;
    public /* synthetic */ Setting<Boolean> strictDirection;
    public /* synthetic */ Setting<Boolean> liquids;
    public /* synthetic */ Setting<DirectionMode> directionMode;
    public /* synthetic */ Setting<Boolean> swing;
    public /* synthetic */ Setting<Float> maxSelfPlace;
    public /* synthetic */ AtomicBoolean tickRunning;
    public /* synthetic */ Setting<Float> rainbowBrightnessAA22;
    public final /* synthetic */ Setting<Bind> forceFaceplace;
    public final /* synthetic */ List<BlockPos> selfPlacePositions;
    public /* synthetic */ Setting<Float> disableUnderHealth;
    public final /* synthetic */ Timer linearTimer;
    private /* synthetic */ float timePassed;
    private final /* synthetic */ Setting<Integer> oRed;
    private final /* synthetic */ Setting<Integer> CRed;
    private final /* synthetic */ Setting<Integer> CGreen;
    private final /* synthetic */ Setting<Integer> CBlue;
    private final /* synthetic */ Setting<Float> circleStep1;
    private final /* synthetic */ Setting<Float> circleHeight;
    private final /* synthetic */ Setting<Boolean> circle;

    public /* synthetic */ Setting<Float> enemyRange;
    public /* synthetic */ Setting<Integer> delay;
    private /* synthetic */ RayTraceResult postResult;
    private /* synthetic */ int ticks;
    public final /* synthetic */ ConcurrentHashMap<BlockPos, Long> placeLocations;
    public /* synthetic */ Setting<Float> compromise;
    private final /* synthetic */ Setting<Float> depletion;
    public final /* synthetic */ ConcurrentHashMap<Integer, Long> breakLocations;
    private /* synthetic */ boolean foundDoublePop;
    public final /* synthetic */ Map<EntityPlayer, Timer> totemPops;
    public final /* synthetic */ Timer inhibitTimer;
    public /* synthetic */ Setting<ACAntiWeakness> antiWeakness;
    public /* synthetic */ Setting<YawStepMode> yawStep;
    public /* synthetic */ Setting<Boolean> fire;
    public /* synthetic */ Setting<Boolean> rightClickGap;
    public /* synthetic */ Vec3d rotationVector;
    private static /* synthetic */ float aboba;
    public /* synthetic */ Setting<Boolean> colorSync;
    public /* synthetic */ Setting<Boolean> abcc;
    public /* synthetic */ Setting<ConfirmMode> confirm;
    public /* synthetic */ Setting<Boolean> inhibit;
    public /* synthetic */ float renderDamage;
    public /* synthetic */ Setting<Boolean> breakCrystal1;
    private /* synthetic */ EnumFacing postFacing;
    private static /* synthetic */ FemboyAura INSTANCE;
    private final /* synthetic */ Setting<Integer> bGreen;
    public /* synthetic */ float[] rotations;
    public final /* synthetic */ Timer cacheTimer;
    public /* synthetic */ BlockPos renderBlock;

    @SubscribeEvent
    public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        if (updateWalkingPlayerEvent.getStage() == 0) {
            this.placeLocations.forEach((blockPos, l) -> {
                if (System.currentTimeMillis() - l > 1500L) {
                    this.placeLocations.remove(blockPos);
                }
            });
            --this.ticks;
            if (this.bilateralVec != null) {
                for (Entity entity : FemboyAura.mc.world.loadedEntityList) {
                    if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistance(this.bilateralVec.x, this.bilateralVec.y, this.bilateralVec.z) <= 6.0)) continue;
                    this.breakLocations.put(entity.getEntityId(), System.currentTimeMillis());
                }
                this.bilateralVec = null;
            }
            if (updateWalkingPlayerEvent.isCanceled()) {
                return;
            }
            this.postBreakPos = null;
            this.postPlacePos = null;
            this.postFacing = null;
            this.postResult = null;
            this.foundDoublePop = false;
            this.handleSequential();
            if (this.rotationMode.getValue() != RotationMode.OFF && !this.rotationTimer.passedMs(650L) && this.rotationVector != null) {
                if (this.rotationMode.getValue() == RotationMode.TRACK) {
                    this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                }
                if (this.yawAngle.getValue().floatValue() < 1.0f && this.yawStep.getValue() != YawStepMode.OFF && (this.postBreakPos != null || this.yawStep.getValue() == YawStepMode.FULL)) {
                    if (this.ticks > 0) {
                        this.rotations[0] = ((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw();
                        this.postBreakPos = null;
                        this.postPlacePos = null;
                    } else {
                        float f = MathHelper.wrapDegrees((float)(this.rotations[0] - ((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()));
                        if (Math.abs(f) > 180.0f * this.yawAngle.getValue().floatValue()) {
                            this.rotations[0] = ((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw() + f * (180.0f * this.yawAngle.getValue().floatValue() / Math.abs(f));
                            this.postBreakPos = null;
                            this.postPlacePos = null;
                            this.ticks = this.yawTicks.getValue();
                        }
                    }
                }
                SilentRotaionUtil.lookAtAngles(this.rotations[0], this.rotations[1]);
            }
        }
    }

    public static FemboyAura getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FemboyAura();
        }
        return INSTANCE;
    }

    public BlockPos getPostPlacePos() {
        return this.postPlacePos;
    }

    private boolean breakCrystal(EntityEnderCrystal entityEnderCrystal) {
        if (entityEnderCrystal != null) {
            if (this.breakCrystal1.getValue().booleanValue()) {
                Command.sendMessage("BreakCrystal");
            }
            if (this.antiWeakness.getValue() != ACAntiWeakness.OFF && FemboyAura.mc.player.isPotionActive(MobEffects.WEAKNESS) && !(FemboyAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !this.switchToSword()) {
                return false;
            }
            if (!this.swapTimer.passedMs((long)(this.swapDelay.getValue().floatValue() * 100.0f))) {
                return false;
            }
            FemboyAura.mc.playerController.attackEntity((EntityPlayer) FemboyAura.mc.player, (Entity)entityEnderCrystal);
            FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketAnimation(this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
            this.swingArmAfterBreaking(this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            if (this.oldSlotSword != -1 && FemboyAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
                FemboyAura.mc.player.inventory.currentItem = this.oldSlotSword;
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlotSword));
                this.oldSlotSword = -1;
            }
            if (this.syncMode.getValue() == SyncMode.MERGE) {
                this.placeTimer.reset();
            }
            if (this.syncMode.getValue() == SyncMode.STRICT) {
                this.lastBroken.set(true);
            }
            this.inhibitTimer.reset();
            this.inhibitEntity = entityEnderCrystal;
            this.renderBreakingPos = new BlockPos((Entity)entityEnderCrystal).down();
            this.renderBreakingTimer.reset();
            return true;
        }
        return false;
    }

    private BlockPos findPlacePosition(List<BlockPos> list, List<EntityPlayer> list2) {
        if (list2.isEmpty()) {
            return null;
        }
        float f = 0.5f;
        EntityPlayer entityPlayer = null;
        BlockPos blockPos = null;
        this.foundDoublePop = false;
        EntityPlayer entityPlayer2 = null;
        for (BlockPos blockPos2 : list) {
            float f2 = CrystalUtil.calculateDamage(blockPos2, (Entity) FemboyAura.mc.player);
            if (!((double)f2 + (double)this.suicideHealth.getValue().floatValue() < (double)(FemboyAura.mc.player.getHealth() + FemboyAura.mc.player.getAbsorptionAmount())) || !(f2 <= this.maxSelfPlace.getValue().floatValue())) continue;
            if (this.targetingMode.getValue() != TargetingMode.ALL) {
                entityPlayer2 = list2.get(0);
                if (entityPlayer2.getDistance((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5) > (double)this.crystalRange.getValue().floatValue()) continue;
                float f3 = CrystalUtil.calculateDamage(blockPos2, (Entity)entityPlayer2);
                if (this.isDoublePoppable(entityPlayer2, f3) && (blockPos == null || entityPlayer2.getDistanceSq(blockPos2) < entityPlayer2.getDistanceSq(blockPos))) {
                    entityPlayer = entityPlayer2;
                    f = f3;
                    blockPos = blockPos2;
                    this.foundDoublePop = true;
                    continue;
                }
                if (this.foundDoublePop || !(f3 > f) || !(f3 * this.compromise.getValue().floatValue() > f2) && !(f3 > entityPlayer2.getHealth() + entityPlayer2.getAbsorptionAmount()) || f3 < this.minPlaceDamage.getValue().floatValue() && entityPlayer2.getHealth() + entityPlayer2.getAbsorptionAmount() > this.faceplaceHealth.getValue().floatValue() && !this.forceFaceplace.getValue().isDown() && !this.shouldArmorBreak(entityPlayer2)) continue;
                f = f3;
                entityPlayer = entityPlayer2;
                blockPos = blockPos2;
                continue;
            }
            for (EntityPlayer entityPlayer3 : list2) {
                if (entityPlayer3.equals((Object)entityPlayer2) || entityPlayer3.getDistance((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.5, (double)blockPos2.getZ() + 0.5) > (double)this.crystalRange.getValue().floatValue()) continue;
                float f4 = CrystalUtil.calculateDamage(blockPos2, (Entity)entityPlayer3);
                if (this.isDoublePoppable(entityPlayer3, f4) && (blockPos == null || entityPlayer3.getDistanceSq(blockPos2) < entityPlayer3.getDistanceSq(blockPos))) {
                    entityPlayer = entityPlayer3;
                    f = f4;
                    blockPos = blockPos2;
                    this.foundDoublePop = true;
                    continue;
                }
                if (this.foundDoublePop || !(f4 > f) || !(f4 * this.compromise.getValue().floatValue() > f2) && !(f4 > entityPlayer3.getHealth() + entityPlayer3.getAbsorptionAmount()) || f4 < this.minPlaceDamage.getValue().floatValue() && entityPlayer3.getHealth() + entityPlayer3.getAbsorptionAmount() > this.faceplaceHealth.getValue().floatValue() && !this.forceFaceplace.getValue().isDown() && !this.shouldArmorBreak(entityPlayer3)) continue;
                f = f4;
                entityPlayer = entityPlayer3;
                blockPos = blockPos2;
            }
        }
        if (entityPlayer != null && blockPos != null) {
            this.renderTarget = entityPlayer;
            this.renderTargetTimer.reset();
        }
        if (blockPos != null) {
            this.renderBlock = blockPos;
            this.renderDamage = f;
        }
        this.cachePos = blockPos;
        this.cacheTimer.reset();
        return blockPos;
    }

    public static List<String> isGoodCrystal() {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            String string;
        }
        catch (Exception exception) {
            // empty catch block
        }
        return arrayList;
    }

    @Override
    public String getDisplayInfo() {
        test1 = this.renderTargetTimer.getPassedTimeMs() / 10L;
        if (this.renderTarget != null && !this.renderTargetTimer.passedMs(800L)) {
            return String.valueOf(new StringBuilder().append(this.renderTarget.getName()).append(" , ").append((int)test1).append("ms , ").append(Math.floor(this.renderDamage) == (double)this.renderDamage ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", Float.valueOf(this.renderDamage))).append(""));
        }
        return null;
    }

    public EnumFacing handlePlaceRotation(BlockPos blockPos) {
        if (blockPos == null || FemboyAura.mc.player == null) {
            return null;
        }
        EnumFacing enumFacing = null;
        if (this.directionMode.getValue() != DirectionMode.VANILLA) {
            double[] arrd;
            Vec3d vec3d;
            RayTraceResult rayTraceResult;
            Vec3d vec3d2;
            Vec3d vec3d3;
            float f;
            float f2;
            float f3;
            float f4;
            double[] arrd2;
            double d;
            double d2;
            double d3;
            double d4;
            double d5;
            Vec3d vec3d4;
            double d6;
            double d7;
            double d8;
            Vec3d vec3d5 = null;
            double[] arrd3 = null;
            double d9 = 0.45;
            double d10 = 0.05;
            double d11 = 0.95;
            Vec3d vec3d6 = new Vec3d(FemboyAura.mc.player.posX, FemboyAura.mc.player.getEntityBoundingBox().minY + (double) FemboyAura.mc.player.getEyeHeight(), FemboyAura.mc.player.posZ);
            for (d8 = d10; d8 <= d11; d8 += d9) {
                for (d7 = d10; d7 <= d11; d7 += d9) {
                    for (d6 = d10; d6 <= d11; d6 += d9) {
                        vec3d4 = new Vec3d((Vec3i)blockPos).add(d8, d7, d6);
                        d5 = vec3d6.distanceTo(vec3d4);
                        d4 = vec3d4.x - vec3d6.x;
                        d3 = vec3d4.y - vec3d6.y;
                        d2 = vec3d4.z - vec3d6.z;
                        d = MathHelper.sqrt((double)(d4 * d4 + d2 * d2));
                        arrd2 = new double[]{MathHelper.wrapDegrees((float)((float)Math.toDegrees(Math.atan2(d2, d4)) - 90.0f)), MathHelper.wrapDegrees((float)((float)(-Math.toDegrees(Math.atan2(d3, d)))))};
                        f4 = MathHelper.cos((float)((float)(-arrd2[0] * 0.01745329238474369 - 3.1415927410125732)));
                        f3 = MathHelper.sin((float)((float)(-arrd2[0] * 0.01745329238474369 - 3.1415927410125732)));
                        f2 = -MathHelper.cos((float)((float)(-arrd2[1] * 0.01745329238474369)));
                        f = MathHelper.sin((float)((float)(-arrd2[1] * 0.01745329238474369)));
                        vec3d3 = new Vec3d((double)(f3 * f2), (double)f, (double)(f4 * f2));
                        vec3d2 = vec3d6.add(vec3d3.x * d5, vec3d3.y * d5, vec3d3.z * d5);
                        rayTraceResult = FemboyAura.mc.world.rayTraceBlocks(vec3d6, vec3d2, false, true, false);
                        if (!(this.placeWallsRange.getValue().floatValue() >= this.placeRange.getValue().floatValue() || rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK && rayTraceResult.getBlockPos().equals((Object)blockPos))) continue;
                        vec3d = vec3d4;
                        arrd = arrd2;
                        if (this.strictDirection.getValue().booleanValue()) {
                            if (vec3d5 != null && arrd3 != null && (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK || enumFacing == null)) {
                                if (!(FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d) < FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d5))) continue;
                                vec3d5 = vec3d;
                                arrd3 = arrd;
                                if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                                enumFacing = rayTraceResult.sideHit;
                                this.postResult = rayTraceResult;
                                continue;
                            }
                            vec3d5 = vec3d;
                            arrd3 = arrd;
                            if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                            enumFacing = rayTraceResult.sideHit;
                            this.postResult = rayTraceResult;
                            continue;
                        }
                        if (vec3d5 != null && arrd3 != null && (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK || enumFacing == null)) {
                            if (!(Math.hypot(((arrd[0] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()) % 360.0 + 540.0) % 360.0 - 180.0, arrd[1] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedPitch()) < Math.hypot(((arrd3[0] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()) % 360.0 + 540.0) % 360.0 - 180.0, arrd3[1] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedPitch()))) continue;
                            vec3d5 = vec3d;
                            arrd3 = arrd;
                            if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                            enumFacing = rayTraceResult.sideHit;
                            this.postResult = rayTraceResult;
                            continue;
                        }
                        vec3d5 = vec3d;
                        arrd3 = arrd;
                        if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                        enumFacing = rayTraceResult.sideHit;
                        this.postResult = rayTraceResult;
                    }
                }
            }
            if (this.placeWallsRange.getValue().floatValue() < this.placeRange.getValue().floatValue() && this.directionMode.getValue() == DirectionMode.STRICT) {
                if (arrd3 != null && enumFacing != null) {
                    this.rotationTimer.reset();
                    this.rotationVector = vec3d5;
                    this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                    return enumFacing;
                }
                for (d8 = d10; d8 <= d11; d8 += d9) {
                    for (d7 = d10; d7 <= d11; d7 += d9) {
                        for (d6 = d10; d6 <= d11; d6 += d9) {
                            vec3d4 = new Vec3d((Vec3i)blockPos).add(d8, d7, d6);
                            d5 = vec3d6.distanceTo(vec3d4);
                            d4 = vec3d4.x - vec3d6.x;
                            d3 = vec3d4.y - vec3d6.y;
                            d2 = vec3d4.z - vec3d6.z;
                            d = MathHelper.sqrt((double)(d4 * d4 + d2 * d2));
                            arrd2 = new double[]{MathHelper.wrapDegrees((float)((float)Math.toDegrees(Math.atan2(d2, d4)) - 90.0f)), MathHelper.wrapDegrees((float)((float)(-Math.toDegrees(Math.atan2(d3, d)))))};
                            f4 = MathHelper.cos((float)((float)(-arrd2[0] * 0.01745329238474369 - 3.1415927410125732)));
                            f3 = MathHelper.sin((float)((float)(-arrd2[0] * 0.01745329238474369 - 3.1415927410125732)));
                            f2 = -MathHelper.cos((float)((float)(-arrd2[1] * 0.01745329238474369)));
                            f = MathHelper.sin((float)((float)(-arrd2[1] * 0.01745329238474369)));
                            vec3d3 = new Vec3d((double)(f3 * f2), (double)f, (double)(f4 * f2));
                            vec3d2 = vec3d6.add(vec3d3.x * d5, vec3d3.y * d5, vec3d3.z * d5);
                            rayTraceResult = FemboyAura.mc.world.rayTraceBlocks(vec3d6, vec3d2, false, true, true);
                            if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                            vec3d = vec3d4;
                            arrd = arrd2;
                            if (this.strictDirection.getValue().booleanValue()) {
                                if (vec3d5 != null && arrd3 != null && (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK || enumFacing == null)) {
                                    if (!(FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d) < FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d5))) continue;
                                    vec3d5 = vec3d;
                                    arrd3 = arrd;
                                    if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                                    enumFacing = rayTraceResult.sideHit;
                                    this.postResult = rayTraceResult;
                                    continue;
                                }
                                vec3d5 = vec3d;
                                arrd3 = arrd;
                                if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                                enumFacing = rayTraceResult.sideHit;
                                this.postResult = rayTraceResult;
                                continue;
                            }
                            if (vec3d5 != null && arrd3 != null && (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK || enumFacing == null)) {
                                if (!(Math.hypot(((arrd[0] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()) % 360.0 + 540.0) % 360.0 - 180.0, arrd[1] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedPitch()) < Math.hypot(((arrd3[0] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()) % 360.0 + 540.0) % 360.0 - 180.0, arrd3[1] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedPitch()))) continue;
                                vec3d5 = vec3d;
                                arrd3 = arrd;
                                if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                                enumFacing = rayTraceResult.sideHit;
                                this.postResult = rayTraceResult;
                                continue;
                            }
                            vec3d5 = vec3d;
                            arrd3 = arrd;
                            if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) continue;
                            enumFacing = rayTraceResult.sideHit;
                            this.postResult = rayTraceResult;
                        }
                    }
                }
            } else {
                if (arrd3 != null) {
                    this.rotationTimer.reset();
                    this.rotationVector = vec3d5;
                    this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                }
                if (enumFacing != null) {
                    return enumFacing;
                }
            }
        } else {
            Vec3d vec3d;
            EnumFacing enumFacing2 = null;
            Vec3d vec3d7 = null;
            for (EnumFacing enumFacing3 : EnumFacing.values()) {
                vec3d = new Vec3d((double)blockPos.getX() + 0.5 + (double)enumFacing3.getDirectionVec().getX() * 0.5, (double)blockPos.getY() + 0.5 + (double)enumFacing3.getDirectionVec().getY() * 0.5, (double)blockPos.getZ() + 0.5 + (double)enumFacing3.getDirectionVec().getZ() * 0.5);
                RayTraceResult rayTraceResult = FemboyAura.mc.world.rayTraceBlocks(new Vec3d(FemboyAura.mc.player.posX, FemboyAura.mc.player.posY + (double) FemboyAura.mc.player.getEyeHeight(), FemboyAura.mc.player.posZ), vec3d, false, true, false);
                if (rayTraceResult == null || !rayTraceResult.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK) || !rayTraceResult.getBlockPos().equals((Object)blockPos)) continue;
                if (this.strictDirection.getValue().booleanValue()) {
                    if (vec3d7 != null && !(FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d) < FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d7))) continue;
                    vec3d7 = vec3d;
                    enumFacing2 = enumFacing3;
                    this.postResult = rayTraceResult;
                    continue;
                }
                this.rotationTimer.reset();
                this.rotationVector = vec3d;
                this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                return enumFacing3;
            }
            if (enumFacing2 != null) {
                this.rotationTimer.reset();
                this.rotationVector = vec3d7;
                this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                return enumFacing2;
            }
            if (this.strictDirection.getValue().booleanValue()) {
                for (EnumFacing enumFacing3 : EnumFacing.values()) {
                    vec3d = new Vec3d((double)blockPos.getX() + 0.5 + (double)enumFacing3.getDirectionVec().getX() * 0.5, (double)blockPos.getY() + 0.5 + (double)enumFacing3.getDirectionVec().getY() * 0.5, (double)blockPos.getZ() + 0.5 + (double)enumFacing3.getDirectionVec().getZ() * 0.5);
                    if (vec3d7 != null && !(FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d) < FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d7))) continue;
                    vec3d7 = vec3d;
                    enumFacing2 = enumFacing3;
                }
                if (enumFacing2 != null) {
                    this.rotationTimer.reset();
                    this.rotationVector = vec3d7;
                    this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                    return enumFacing2;
                }
            }
        }
        if ((double)blockPos.getY() > FemboyAura.mc.player.posY + (double) FemboyAura.mc.player.getEyeHeight()) {
            this.rotationTimer.reset();
            this.rotationVector = new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5);
            this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
            return EnumFacing.DOWN;
        }
        this.rotationTimer.reset();
        this.rotationVector = new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5);
        this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
        return EnumFacing.UP;
    }

    private void handleSequential() {
        if (this.handleSequential1.getValue().booleanValue()) {
            Command.sendMessage("handleSequential");
        }
        if (FemboyAura.mc.player.getHealth() + FemboyAura.mc.player.getAbsorptionAmount() < this.disableUnderHealth.getValue().floatValue() || this.noGapSwitch.getValue() != false && FemboyAura.mc.player.getActiveItemStack().getItem() instanceof ItemFood || this.noMineSwitch.getValue().booleanValue() && FemboyAura.mc.playerController.getIsHittingBlock() && FemboyAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemTool) {
            this.rotationVector = null;
            return;
        }
        if (this.noGapSwitch.getValue().booleanValue() && this.rightClickGap.getValue().booleanValue() && FemboyAura.mc.gameSettings.keyBindUseItem.isKeyDown() && FemboyAura.mc.player.inventory.getCurrentItem().getItem() instanceof ItemEndCrystal) {
            int n = -1;
            for (int i = 0; i < 9; ++i) {
                if (FemboyAura.mc.player.inventory.getStackInSlot(i).getItem() != Items.GOLDEN_APPLE) continue;
                n = i;
                break;
            }
            if (n != -1 && n != FemboyAura.mc.player.inventory.currentItem) {
                FemboyAura.mc.player.inventory.currentItem = n;
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                return;
            }
        }
        if (!this.isOffhand() && !(FemboyAura.mc.player.inventory.getCurrentItem().getItem() instanceof ItemEndCrystal) && this.autoSwap.getValue() == ACSwapMode.OFF) {
            return;
        }
        List<EntityPlayer> list = this.getTargetsInRange();
        EntityEnderCrystal entityEnderCrystal = this.findCrystalTarget(list);
        int n = (int)Math.max(100.0f, (float)(CrystalUtil.ping() + 50) / 1.0f) + 150;
        if (entityEnderCrystal != null && this.breakTimer.passedMs((long)(1000.0f - this.breakSpeed.getValue().floatValue() * 50.0f)) && (entityEnderCrystal.ticksExisted >= this.delay.getValue() || this.timingMode.getValue() == TimingMode.NORMAL)) {
            this.postBreakPos = entityEnderCrystal;
            this.handleBreakRotation(this.postBreakPos.posX, this.postBreakPos.posY, this.postBreakPos.posZ);
        }
        if (entityEnderCrystal == null && (this.confirm.getValue() != ConfirmMode.FULL || this.inhibitEntity == null || (double)this.inhibitEntity.ticksExisted >= Math.floor(this.delay.getValue().intValue())) && (this.syncMode.getValue() != SyncMode.STRICT || this.breakTimer.passedMs((long)(950.0f - this.breakSpeed.getValue().floatValue() * 50.0f - (float)CrystalUtil.ping()))) && this.placeTimer.passedMs((long)(1000.0f - this.placeSpeed.getValue().floatValue() * 50.0f)) && (this.timingMode.getValue() == TimingMode.SEQUENTIAL || this.linearTimer.passedMs((long)((float)this.delay.getValue().intValue() * 5.0f)))) {
            BlockPos blockPos;
            if (this.confirm.getValue() != ConfirmMode.OFF && this.cachePos != null && !this.cacheTimer.passedMs(n + 100) && this.canPlaceCrystal(this.cachePos)) {
                this.postPlacePos = this.cachePos;
                this.postFacing = this.handlePlaceRotation(this.postPlacePos);
                this.lastBroken.set(false);
                return;
            }
            List<BlockPos> list2 = this.findCrystalBlocks();
            if (!list2.isEmpty() && (blockPos = this.findPlacePosition(list2, list)) != null) {
                this.postPlacePos = blockPos;
                this.postFacing = this.handlePlaceRotation(this.postPlacePos);
            }
        }
        this.lastBroken.set(false);
    }

    @Override
    public void onEnable() {
        this.postBreakPos = null;
        this.postPlacePos = null;
        this.postFacing = null;
        this.postResult = null;
        this.prevPlacePos = null;
        this.cachePos = null;
        this.bilateralVec = null;
        this.lastBroken.set(false);
        this.rotationVector = null;
        this.rotationTimer.reset();
        this.isPlacing = false;
        this.foundDoublePop = false;
        this.totemPops.clear();
        this.oldSlotCrystal = -1;
        this.oldSlotSword = -1;
    }

    public boolean canPlaceCrystal(BlockPos blockPos) {
        if (FemboyAura.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && FemboyAura.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }
        BlockPos blockPos2 = blockPos.add(0, 1, 0);
        if (!(FemboyAura.mc.world.getBlockState(blockPos2).getBlock() == Blocks.AIR || FemboyAura.mc.world.getBlockState(blockPos2).getBlock() == Blocks.FIRE && this.fire.getValue().booleanValue() || FemboyAura.mc.world.getBlockState(blockPos2).getBlock() instanceof BlockLiquid && this.liquids.getValue().booleanValue())) {
            return false;
        }
        BlockPos blockPos3 = blockPos.add(0, 2, 0);
        if (!(this.protocol.getValue().booleanValue() || FemboyAura.mc.world.getBlockState(blockPos3).getBlock() == Blocks.AIR || FemboyAura.mc.world.getBlockState(blockPos2).getBlock() instanceof BlockLiquid && this.liquids.getValue().booleanValue())) {
            return false;
        }
        if (this.check.getValue().booleanValue() && !CrystalUtil.rayTraceBreak((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5)) {
            Vec3d vec3d = new Vec3d((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 1.0, (double)blockPos.getZ() + 0.5);
            if (FemboyAura.mc.player.getPositionEyes(1.0f).distanceTo(vec3d) > (double)this.breakWallsRange.getValue().floatValue()) {
                return false;
            }
        }
        if (this.placeWallsRange.getValue().floatValue() < this.placeRange.getValue().floatValue()) {
            if (!CrystalUtil.rayTracePlace(blockPos)) {
                if (this.strictDirection.getValue().booleanValue()) {
                    boolean bl;
                    block26: {
                        Vec3d vec3d = FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0);
                        bl = false;
                        if (this.directionMode.getValue() == DirectionMode.VANILLA) {
                            for (EnumFacing enumFacing : EnumFacing.values()) {
                                Vec3d vec3d2 = new Vec3d((double)blockPos.getX() + 0.5 + (double)enumFacing.getDirectionVec().getX() * 0.5, (double)blockPos.getY() + 0.5 + (double)enumFacing.getDirectionVec().getY() * 0.5, (double)blockPos.getZ() + 0.5 + (double)enumFacing.getDirectionVec().getZ() * 0.5);
                                if (!(vec3d.distanceTo(vec3d2) <= (double)this.placeWallsRange.getValue().floatValue())) continue;
                                bl = true;
                                break;
                            }
                        } else {
                            double d = 0.45;
                            double d2 = 0.05;
                            double d3 = 0.95;
                            for (double d4 = d2; d4 <= d3; d4 += d) {
                                for (double d5 = d2; d5 <= d3; d5 += d) {
                                    for (double d6 = d2; d6 <= d3; d6 += d) {
                                        Vec3d vec3d3 = new Vec3d((Vec3i)blockPos).add(d4, d5, d6);
                                        double d7 = vec3d.distanceTo(vec3d3);
                                        if (!(d7 <= (double)this.placeWallsRange.getValue().floatValue())) continue;
                                        bl = true;
                                        break block26;
                                    }
                                }
                            }
                        }
                    }
                    if (!bl) {
                        return false;
                    }
                } else if ((double)blockPos.getY() > FemboyAura.mc.player.posY + (double) FemboyAura.mc.player.getEyeHeight() ? FemboyAura.mc.player.getDistance((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5) > (double)this.placeWallsRange.getValue().floatValue() : FemboyAura.mc.player.getDistance((double)blockPos.getX() + 0.5, (double)(blockPos.getY() + 1), (double)blockPos.getZ() + 0.5) > (double)this.placeWallsRange.getValue().floatValue()) {
                    return false;
                }
            }
        } else if (this.strictDirection.getValue().booleanValue()) {
            boolean bl;
            block27: {
                Vec3d vec3d = FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0);
                bl = false;
                if (this.directionMode.getValue() == DirectionMode.VANILLA) {
                    for (EnumFacing enumFacing : EnumFacing.values()) {
                        Vec3d vec3d4 = new Vec3d((double)blockPos.getX() + 0.5 + (double)enumFacing.getDirectionVec().getX() * 0.5, (double)blockPos.getY() + 0.5 + (double)enumFacing.getDirectionVec().getY() * 0.5, (double)blockPos.getZ() + 0.5 + (double)enumFacing.getDirectionVec().getZ() * 0.5);
                        if (!(vec3d.distanceTo(vec3d4) <= (double)this.placeRange.getValue().floatValue())) continue;
                        bl = true;
                        break;
                    }
                } else {
                    double d = 0.45;
                    double d8 = 0.05;
                    double d9 = 0.95;
                    for (double d10 = d8; d10 <= d9; d10 += d) {
                        for (double d11 = d8; d11 <= d9; d11 += d) {
                            for (double d12 = d8; d12 <= d9; d12 += d) {
                                Vec3d vec3d5 = new Vec3d((Vec3i)blockPos).add(d10, d11, d12);
                                double d13 = vec3d.distanceTo(vec3d5);
                                if (!(d13 <= (double)this.placeRange.getValue().floatValue())) continue;
                                bl = true;
                                break block27;
                            }
                        }
                    }
                }
            }
            if (!bl) {
                return false;
            }
        }
        return FemboyAura.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockPos2, blockPos3.add(1, 1, 1))).stream().filter(entity -> !this.breakLocations.containsKey(entity.getEntityId()) && (!(entity instanceof EntityEnderCrystal) || entity.ticksExisted > 20)).count() == 0L;
    }

    /*private List<EntityPlayer> getTargetsInRange() {
        List<EntityPlayer> list = FemboyAura.mc.world.playerEntities.stream().filter(entityPlayer -> entityPlayer != FemboyAura.mc.player && entityPlayer != mc.getRenderViewEntity()).filter(entityPlayer -> !entityPlayer.isDead).filter(entityPlayer -> !Femhack.friendManager.isFriend(entityPlayer.getName())).filter(entityPlayer -> entityPlayer.getHealth() > 0.0f).filter(entityPlayer -> FemboyAura.mc.player.getDistanceToEntity((Entity)entityPlayer) < this.enemyRange.getValue().floatValue()).sorted(Comparator.comparing(entityPlayer -> Float.valueOf(FemboyAura.mc.player.getDistanceToEntity((Entity)entityPlayer)))).collect(Collectors.toList());
        if (this.targetingMode.getValue() == TargetingMode.SMART) {
            List list2 = list.stream().filter(entityPlayer -> !BlockUtil.isHole(new BlockPos((Entity)entityPlayer)) && (FemboyAura.mc.world.getBlockState(new BlockPos((Entity)entityPlayer)).getBlock() == Blocks.AIR || FemboyAura.mc.world.getBlockState(new BlockPos((Entity)entityPlayer)).getBlock() == Blocks.WEB || FemboyAura.mc.world.getBlockState(new BlockPos((Entity)entityPlayer)).getBlock() instanceof BlockLiquid)).sorted(Comparator.comparing(entityPlayer -> Float.valueOf(FemboyAura.mc.player.getDistanceToEntity((Entity)entityPlayer)))).collect(Collectors.toList());
            if (list2.size() > 0) {
                list = list2;
            }
            if ((list2 = list.stream().filter(entityPlayer -> entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() < 10.0f).sorted(Comparator.comparing(entityPlayer -> Float.valueOf(FemboyAura.mc.player.getDistanceToEntity((Entity)entityPlayer)))).collect(Collectors.toList())).size() > 0) {
                list = list2;
            }
        }
        return list;
    }*/

    private List<EntityPlayer> getTargetsInRange() {
        List<EntityPlayer> list = (List<EntityPlayer>) FemboyAura.mc.world.playerEntities.stream().filter(entityPlayer -> entityPlayer != FemboyAura.mc.player && entityPlayer != FemboyAura.mc.getRenderViewEntity()).filter(e -> !e.isDead).filter(e -> !Femhack.friendManager.isFriend(e.getName())).filter(e -> e.getHealth() > 0.0f).filter(e -> FemboyAura.mc.player.getDistance(e) < this.enemyRange.getValue()).sorted(Comparator.comparing(e -> FemboyAura.mc.player.getDistance(e))).collect(Collectors.toList());
        if (this.targetingMode.getValue() == TargetingMode.SMART) {
            final boolean b = false;
            List<EntityPlayer> list2 = list.stream().filter(entityPlayer -> {
                if (!BlockUtil.isHole(new BlockPos(entityPlayer))) {
                    if (FemboyAura.mc.world.getBlockState(new BlockPos(entityPlayer)).getBlock() != Blocks.AIR) {
                        if (FemboyAura.mc.world.getBlockState(new BlockPos(entityPlayer)).getBlock() != Blocks.WEB) {
                            if (!(FemboyAura.mc.world.getBlockState(new BlockPos(entityPlayer)).getBlock() instanceof BlockLiquid)) {
                                return 0 != 0;
                            }
                        }
                    }
                    return b;
                }
                return b;
            }).sorted(Comparator.comparing(entityPlayer -> FemboyAura.mc.player.getDistance(entityPlayer))).collect(Collectors.toList());
            if (list2.size() > 0) {
                list = list2;
            }
            list2 = list.stream().filter(entityPlayer -> entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() < 10.0f).sorted(Comparator.comparing(entityPlayer -> FemboyAura.mc.player.getDistance(entityPlayer))).collect(Collectors.toList());
            if (list2.size() > 0) {
                list = list2;
            }
        }
        return list;
    }

    /*  private List<EntityPlayer> getTargetsInRange() {
        List<EntityPlayer> stream = (List<EntityPlayer>)NewAC.mc.world.playerEntities.stream().filter(e -> e != NewAC.mc.player && e != NewAC.mc.getRenderViewEntity()).filter(e -> !e.isDead).filter(e -> !Thunderhack.friendManager.isFriend(e.getName())).filter(e -> e.getHealth() > 0.0f).filter(e -> NewAC.mc.player.getDistance(e) < this.enemyRange.getValue()).sorted(Comparator.comparing(e -> NewAC.mc.player.getDistance(e))).collect(Collectors.toList());
        if (this.targetingMode.getValue() == TargetingMode.SMART) {
            final boolean b;
            List<EntityPlayer> safeStream = stream.stream().filter(e -> {
                if (!BlockUtils.isHole(new BlockPos(e))) {
                    if (NewAC.mc.world.getBlockState(new BlockPos(e)).getBlock() != Blocks.AIR) {
                        if (NewAC.mc.world.getBlockState(new BlockPos(e)).getBlock() != Blocks.WEB) {
                            if (!(NewAC.mc.world.getBlockState(new BlockPos(e)).getBlock() instanceof BlockLiquid)) {
                                return 0 != 0;
                            }
                        }
                    }
                    return b;
                }
                return b;
            }).sorted(Comparator.comparing(e -> NewAC.mc.player.getDistance(e))).collect((Collector<? super Object, ?, List<EntityPlayer>>)Collectors.toList());
            if (safeStream.size() > 0) {
                stream = safeStream;
            }
            safeStream = stream.stream().filter(e -> e.getHealth() + e.getAbsorptionAmount() < 10.0f).sorted(Comparator.comparing(e -> NewAC.mc.player.getDistance(e))).collect((Collector<? super Object, ?, List<EntityPlayer>>)Collectors.toList());
            if (safeStream.size() > 0) {
                stream = safeStream;
            }
        }
        return stream;
    } */

    private void setInstance() {
        INSTANCE = this;
    }

    public boolean setCrystalSlot() {
        if (this.isOffhand()) {
            return true;
        }
        int n = CrystalUtil.getCrystalSlot();
        if (n == -1) {
            return false;
        }
        if (FemboyAura.mc.player.inventory.currentItem != n) {
            FemboyAura.mc.player.inventory.currentItem = n;
            FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
        }
        return true;
    }

    @Override
    public void onUpdate() {
        if (circle.getValue()) {
            prevCircleStep = circleStep;
            circleStep += circleStep1.getValue();
        }
    }
    public static double absSinAnimation(double input) {
        return Math.abs(1 + Math.sin(input)) / 2;
    }

    private float prevCircleStep, circleStep;

    @SubscribeEvent
    @Override
    public void onRender3D(final Render3DEvent event) {
        Color color = new Color(this.bRed.getValue(), this.bGreen.getValue(), this.bBlue.getValue(), this.bAlpha.getValue());
        Color color2 = new Color(this.oRed.getValue(), this.oGreen.getValue(), this.oBlue.getValue(), this.oAlpha.getValue());
        if (FemboyAura.mc.world == null || FemboyAura.mc.player == null) {
            return;
        }
        if (this.box.getValue() && this.renderBlock != null) {
            if (this.renderTimeoutTimer.passedMs(1000L)) {
                return;
            }
            AxisAlignedBB axisAlignedBB = null;
            try {
                axisAlignedBB = FemboyAura.mc.world.getBlockState(this.renderBlock).getBoundingBox((IBlockAccess) FemboyAura.mc.world, this.renderBlock).offset(this.renderBlock);
            }
            catch (Exception ex) {}
            if (axisAlignedBB == null) {
                return;
            }
            try {
                RenderUtil.drawBoxESP(this.renderBlock, color, true, color2, this.lineWidth.getValue(), true, true, this.bAlpha.getValue(), false);
            }
            catch (Exception ex2) {}
        }
            AxisAlignedBB axisAlignedBB = null;
            try {
                axisAlignedBB = FemboyAura.mc.world.getBlockState(this.renderBreakingPos).getBoundingBox((IBlockAccess) FemboyAura.mc.world, this.renderBreakingPos).offset(this.renderBreakingPos);
            }
            catch (Exception ex3) {}
            if (axisAlignedBB == null) {
                return;
            }
            BlockRenderUtil.prepareGL();
            try {
                RenderUtil.drawBoxESP(this.renderBlock, color, true, color2, this.lineWidth.getValue(), true, true, this.bAlpha.getValue(), false);
            }
            catch (Exception ex4) {}
            BlockRenderUtil.releaseGL();
            GlStateManager.pushMatrix();
            BlockRenderUtil.prepareGL();
            if (circle.getValue()) {
                double cs = prevCircleStep + (circleStep - prevCircleStep) * mc.getRenderPartialTicks();
                double prevSinAnim = absSinAnimation(cs - circleHeight.getValue());
                double sinAnim = absSinAnimation(cs);
                EntityLivingBase entity = FemboyAura.getInstance().renderTarget;
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosY() + prevSinAnim * 1.8f;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
                double nextY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks() - ((IRenderManager)mc.getRenderManager()).getRenderPosY() + sinAnim * 1.8f;

                GL11.glPushMatrix();

                boolean cullface = GL11.glIsEnabled(GL11.GL_CULL_FACE);
                boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
                boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
                boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
                boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);


                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_ALPHA_TEST);

                GL11.glShadeModel(GL11.GL_SMOOTH);

                GL11.glBegin(GL11.GL_QUAD_STRIP);
                for (int i = 0; i <= 360; i++) {
                    Color clr = new Color(CRed.getValue(), CGreen.getValue(), CBlue.getValue());
                    GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 0.6F);
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * this.renderTarget.width * 0.8, nextY, z + Math.sin(Math.toRadians(i)) * this.renderTarget.width * 0.8);

                    GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 0.01F);
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * this.renderTarget.width * 0.8, y, z + Math.sin(Math.toRadians(i)) * this.renderTarget.width * 0.8);
                }

                GL11.glEnd();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glBegin(GL11.GL_LINE_LOOP);
                for (int i = 0; i <= 360; i++) {
                    Color clr = new Color(CRed.getValue(), CGreen.getValue(), CBlue.getValue());
                    GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 1F);
                    GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * this.renderTarget.width * 0.8, nextY, z + Math.sin(Math.toRadians(i)) * this.renderTarget.width * 0.8);
                }
                GL11.glEnd();

                if (!cullface)
                    GL11.glDisable(GL11.GL_LINE_SMOOTH);

                if (texture)
                    GL11.glEnable(GL11.GL_TEXTURE_2D);


                if (depth)
                    GL11.glEnable(GL11.GL_DEPTH_TEST);

                GL11.glShadeModel(GL11.GL_FLAT);

                if (!blend)
                    GL11.glDisable(GL11.GL_BLEND);
                if (cullface)
                    GL11.glEnable(GL11.GL_CULL_FACE);
                if (alpha)
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glPopMatrix();
                GlStateManager.resetColor();
                BlockRenderUtil.releaseGL();
                GlStateManager.popMatrix();
            }
    }

    static {
        INSTANCE = new FemboyAura();
        bb2 = new ArrayList<String>();
    }

    private int getSwingAnimTime(EntityLivingBase entityLivingBase) {
        if (entityLivingBase.isPotionActive(MobEffects.HASTE)) {
            return 6 - (1 + entityLivingBase.getActivePotionEffect(MobEffects.HASTE).getAmplifier());
        }
        return entityLivingBase.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + entityLivingBase.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
    }

    public void handleBreakRotation(double d, double d2, double d3) {
        if (this.rotationMode.getValue() != RotationMode.OFF) {
            if (this.rotationMode.getValue() == RotationMode.INTERACT && this.rotationVector != null && !this.rotationTimer.passedMs(650L)) {
                if (this.rotationVector.y < d2 - 0.1) {
                    this.rotationVector = new Vec3d(this.rotationVector.x, d2, this.rotationVector.z);
                }
                this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
                this.rotationTimer.reset();
                return;
            }
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(d - 1.0, d2, d3 - 1.0, d + 1.0, d2 + 2.0, d3 + 1.0);
            Vec3d vec3d = new Vec3d(FemboyAura.mc.player.posX, FemboyAura.mc.player.getEntityBoundingBox().minY + (double) FemboyAura.mc.player.getEyeHeight(), FemboyAura.mc.player.posZ);
            double d4 = 0.1;
            double d5 = 0.15;
            double d6 = 0.85;
            if (axisAlignedBB.intersects(FemboyAura.mc.player.getEntityBoundingBox())) {
                d5 = 0.4;
                d6 = 0.6;
                d4 = 0.05;
            }
            Vec3d vec3d2 = null;
            double[] arrd = null;
            boolean bl = false;
            for (double d7 = d5; d7 <= d6; d7 += d4) {
                for (double d8 = d5; d8 <= d6; d8 += d4) {
                    for (double d9 = d5; d9 <= d6; d9 += d4) {
                        Vec3d vec3d3 = new Vec3d(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * d7, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * d8, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * d9);
                        double d10 = vec3d3.x - vec3d.x;
                        double d11 = vec3d3.y - vec3d.y;
                        double d12 = vec3d3.z - vec3d.z;
                        double[] arrd2 = new double[]{MathHelper.wrapDegrees((float)((float)Math.toDegrees(Math.atan2(d12, d10)) - 90.0f)), MathHelper.wrapDegrees((float)((float)(-Math.toDegrees(Math.atan2(d11, Math.sqrt(d10 * d10 + d12 * d12))))))};
                        boolean bl2 = true;
                        if (this.directionMode.getValue() != DirectionMode.VANILLA && !CrystalUtil.isVisible(vec3d3)) {
                            bl2 = false;
                        }
                        if (this.strictDirection.getValue().booleanValue()) {
                            if (vec3d2 != null && arrd != null) {
                                if (!bl2 && bl || !(FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d3) < FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0).distanceTo(vec3d2))) continue;
                                vec3d2 = vec3d3;
                                arrd = arrd2;
                                continue;
                            }
                            vec3d2 = vec3d3;
                            arrd = arrd2;
                            bl = bl2;
                            continue;
                        }
                        if (vec3d2 != null && arrd != null) {
                            if (!bl2 && bl || !(Math.hypot(((arrd2[0] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()) % 360.0 + 540.0) % 360.0 - 180.0, arrd2[1] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedPitch()) < Math.hypot(((arrd[0] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedYaw()) % 360.0 + 540.0) % 360.0 - 180.0, arrd[1] - (double)((IEntityPlayerSP) FemboyAura.mc.player).getLastReportedPitch()))) continue;
                            vec3d2 = vec3d3;
                            arrd = arrd2;
                            continue;
                        }
                        vec3d2 = vec3d3;
                        arrd = arrd2;
                        bl = bl2;
                    }
                }
            }
            if (vec3d2 != null && arrd != null) {
                this.rotationTimer.reset();
                this.rotationVector = vec3d2;
                this.rotations = RotationManager.calculateAngle(FemboyAura.mc.player.getPositionEyes(1.0f), this.rotationVector);
            }
        }
    }

    private boolean shouldArmorBreak(EntityPlayer entityPlayer) {
        if (!this.armorBreaker.getValue().booleanValue()) {
            return false;
        }
        for (int i = 3; i >= 0; --i) {
            double d;
            ItemStack itemStack = (ItemStack)entityPlayer.inventory.armorInventory.get(i);
            if (itemStack == null || !((d = itemStack.getItem().getDurabilityForDisplay(itemStack)) > (double)this.depletion.getValue().floatValue())) continue;
            return true;
        }
        return false;
    }

    private void swingArmAfterBreaking(EnumHand enumHand) {
        if (!this.swing.getValue().booleanValue()) {
            return;
        }
        ItemStack itemStack = FemboyAura.mc.player.getHeldItem(enumHand);
        if (!itemStack.isEmpty() && itemStack.getItem().onEntitySwing((EntityLivingBase) FemboyAura.mc.player, itemStack)) {
            return;
        }
        if (!FemboyAura.mc.player.isSwingInProgress || FemboyAura.mc.player.swingProgressInt >= this.getSwingAnimTime((EntityLivingBase) FemboyAura.mc.player) / 2 || FemboyAura.mc.player.swingProgressInt < 0) {
            FemboyAura.mc.player.swingProgressInt = -1;
            FemboyAura.mc.player.isSwingInProgress = true;
            FemboyAura.mc.player.swingingHand = enumHand;
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPost(UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        aboba = this.mergeOffset.getValue().floatValue() / 10.0f;
        if (updateWalkingPlayerEvent.getStage() == 1) {
            if (this.postBreakPos != null) {
                if (this.breakCrystal(this.postBreakPos)) {
                    this.breakTimer.reset();
                    this.breakLocations.put(this.postBreakPos.getEntityId(), System.currentTimeMillis());
                    for (Entity entity : FemboyAura.mc.world.loadedEntityList) {
                        if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistance(this.postBreakPos.posX, this.postBreakPos.posY, this.postBreakPos.posZ) <= 6.0)) continue;
                        this.breakLocations.put(entity.getEntityId(), System.currentTimeMillis());
                    }
                    this.postBreakPos = null;
                    if (this.abcc.getValue().booleanValue() && this.postBreakPos == null) {
                        Command.sendMessage("postBreakPos Break Pos os Null ");
                    }
                    if (this.syncMode.getValue() == SyncMode.MERGE) {
                        this.runInstantThread();
                    }
                }
            } else if (this.postPlacePos != null) {
                if (!this.placeCrystal(this.postPlacePos, this.postFacing)) {
                    this.shouldRunThread.set(false);
                    this.postPlacePos = null;
                    return;
                }
                this.placeTimer.reset();
                this.postPlacePos = null;
            }
        }
    }

    public static void glBillboardDistanceScaled(float f, float f2, float f3, EntityPlayer entityPlayer, float f4) {
        FemboyAura.glBillboard(f, f2, f3);
        int n = (int)entityPlayer.getDistance((double)f, (double)f2, (double)f3);
        float f5 = (float)n / 2.0f / (2.0f + (2.0f - f4));
        if (f5 < 1.0f) {
            f5 = 1.0f;
        }
        GlStateManager.scale((float)f5, (float)f5, (float)f5);
    }

    public FemboyAura() {
        super("FemboyAura", "Best AutoCrystal", Module.Category.COMBAT, true, false, false);
        this.setting = this.register(new Setting<Pages>("Page", Pages.General));
        this.noMineSwitch = this.register(new Setting<Boolean>("NoMining", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.General));
        this.noGapSwitch = this.register(new Setting<Boolean>("NoGapping", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.General));
        this.rightClickGap = this.register(new Setting<Boolean>("RightClickGap", Boolean.FALSE, bl -> this.noGapSwitch.getValue() != false && this.setting.getValue() == Pages.General));
        this.timingMode = this.register(new Setting<TimingMode>("Timing", TimingMode.NORMAL, timingMode -> this.setting.getValue() == Pages.General));
        this.inhibit = this.register(new Setting<Boolean>("Inhibit", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.General));
        this.limit = this.register(new Setting<Boolean>("Limit", Boolean.valueOf(true), bl -> this.setting.getValue() == Pages.General));
        this.rotationMode = this.register(new Setting<RotationMode>("Rotate", RotationMode.TRACK, rotationMode -> this.setting.getValue() == Pages.General));
        this.yawStep = this.register(new Setting<YawStepMode>("YawStep", YawStepMode.OFF, yawStepMode -> this.setting.getValue() == Pages.General));
        this.yawAngle = this.register(new Setting<Float>("YawAngle", Float.valueOf(0.3f), Float.valueOf(0.1f), Float.valueOf(1.0f), f -> this.setting.getValue() == Pages.General));
        this.yawTicks = this.register(new Setting<Integer>("YawTicks", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(5), n -> this.setting.getValue() == Pages.General));
        this.strictDirection = this.register(new Setting<Boolean>("StrictDirection", Boolean.valueOf(true), bl -> this.setting.getValue() == Pages.General));
        this.swing = this.register(new Setting<Boolean>("Swing", Boolean.valueOf(true), bl -> this.setting.getValue() == Pages.General));
        this.syncMode = this.register(new Setting<SyncMode>("Sync", SyncMode.MERGE, syncMode -> this.setting.getValue() == Pages.General));
        this.mergeOffset = this.register(new Setting<Float>("MergeOffset", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(8.0f), f -> this.syncMode.getValue() == SyncMode.MERGE && this.setting.getValue() == Pages.General));
        this.enemyRange = this.register(new Setting<Float>("EnemyRange", Float.valueOf(8.0f), Float.valueOf(4.0f), Float.valueOf(15.0f), f -> this.setting.getValue() == Pages.General));
        this.crystalRange = this.register(new Setting<Float>("CrystalRange", Float.valueOf(6.0f), Float.valueOf(2.0f), Float.valueOf(12.0f), f -> this.setting.getValue() == Pages.General));
        this.disableUnderHealth = this.register(new Setting<Float>("DisableHealth", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), f -> this.setting.getValue() == Pages.General));
        this.placeRange = this.register(new Setting<Float>("PlaceRange", Float.valueOf(4.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), f -> this.setting.getValue() == Pages.Place));
        this.placeWallsRange = this.register(new Setting<Float>("PlaceWallsRange", Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), f -> this.setting.getValue() == Pages.Place));
        this.placeSpeed = this.register(new Setting<Float>("PlaceSpeed", Float.valueOf(20.0f), Float.valueOf(2.0f), Float.valueOf(20.0f), f -> this.setting.getValue() == Pages.Place));
        this.autoSwap = this.register(new Setting<ACSwapMode>("AutoSwap", ACSwapMode.OFF, aCSwapMode -> this.setting.getValue() == Pages.Place));
        this.swapDelay = this.register(new Setting<Float>("SwapDelay", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), f -> this.setting.getValue() == Pages.Place));
        this.check = this.register(new Setting<Boolean>("PlacementsCheck", Boolean.valueOf(true), bl -> this.setting.getValue() == Pages.Place));
        this.directionMode = this.register(new Setting<DirectionMode>("Interact", DirectionMode.NORMAL, directionMode -> this.setting.getValue() == Pages.Place));
        this.protocol = this.register(new Setting<Boolean>("1.13+ Place", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.Place));
        this.liquids = this.register(new Setting<Boolean>("PlaceInLiquids", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.Place));
        this.fire = this.register(new Setting<Boolean>("PlaceInFire", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.Place));
        this.breakRange = this.register(new Setting<Float>("BreakRange", Float.valueOf(4.3f), Float.valueOf(1.0f), Float.valueOf(6.0f), f -> this.setting.getValue() == Pages.Break));
        this.breakWallsRange = this.register(new Setting<Float>("BreakWalls", Float.valueOf(3.0f), Float.valueOf(1.0f), Float.valueOf(6.0f), f -> this.setting.getValue() == Pages.Break));
        this.attackFactor = this.register(new Setting<Integer>("AttackFactor", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(20), n -> this.setting.getValue() == Pages.Break));
        this.antiWeakness = this.register(new Setting<ACAntiWeakness>("AntiWeakness", ACAntiWeakness.OFF, aCAntiWeakness -> this.setting.getValue() == Pages.Break));
        this.breakSpeed = this.register(new Setting<Float>("BreakSpeed", Float.valueOf(20.0f), Float.valueOf(1.0f), Float.valueOf(20.0f), f -> this.setting.getValue() == Pages.Break));
        this.collision = this.register(new Setting<Boolean>("Collision", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.Break));
        this.predictTicks = this.register(new Setting<Integer>("PredictTicks", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(10), n -> this.setting.getValue() == Pages.Break));
        this.terrainIgnore = this.register(new Setting<Boolean>("TerrainTrace", Boolean.FALSE, bl -> this.setting.getValue() == Pages.Break));
        this.predictPops = this.register(new Setting<Boolean>("PredictPops", Boolean.FALSE, bl -> this.setting.getValue() == Pages.Break));
        this.confirm = this.register(new Setting<ConfirmMode>("Confirm", ConfirmMode.OFF, confirmMode -> this.setting.getValue() == Pages.Calculation));
        this.delay = this.register(new Setting<Integer>("TicksExisted", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(20), n -> this.setting.getValue() == Pages.Calculation));
        this.targetingMode = this.register(new Setting<TargetingMode>("Target", TargetingMode.ALL, targetingMode -> this.setting.getValue() == Pages.Calculation));
        this.security = this.register(new Setting<Float>("DamageBalance", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), f -> this.setting.getValue() == Pages.Calculation));
        this.compromise = this.register(new Setting<Float>("Compromise", Float.valueOf(1.0f), Float.valueOf(0.05f), Float.valueOf(2.0f), f -> this.setting.getValue() == Pages.Calculation));
        this.minPlaceDamage = this.register(new Setting<Float>("MinDamage", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(20.0f), f -> this.setting.getValue() == Pages.Calculation));
        this.maxSelfPlace = this.register(new Setting<Float>("MaxSelfDmg", Float.valueOf(12.0f), Float.valueOf(0.0f), Float.valueOf(20.0f), f -> this.setting.getValue() == Pages.Calculation));
        this.suicideHealth = this.register(new Setting<Float>("SuicideHealth", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), f -> this.setting.getValue() == Pages.Calculation));
        this.faceplaceHealth = this.register(new Setting<Float>("FaceplaceHealth", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(20.0f), f -> this.setting.getValue() == Pages.Calculation));
        this.forceFaceplace = this.register(new Setting<Bind>("Faceplace", new Bind(-1), bind -> this.setting.getValue() == Pages.Calculation));
        this.armorBreaker = this.register(new Setting<Boolean>("ArmorBreaker", Boolean.valueOf(true), bl -> this.setting.getValue() == Pages.Calculation));
        this.depletion = this.register(new Setting<Float>("ArmorDepletion", Float.valueOf(0.9f), Float.valueOf(0.1f), Float.valueOf(1.0f), f -> this.armorBreaker.getValue() != false && this.setting.getValue() == Pages.Calculation));
        this.colorSync = this.register(new Setting<Object>("ColorSync", Boolean.valueOf(true), object -> this.setting.getValue() == Pages.Render));
        this.box = this.register(new Setting<Object>("Box", Boolean.valueOf(true), object -> this.setting.getValue() == Pages.Render));
        this.bRed = this.register(new Setting<Object>("BoxRed", Integer.valueOf(64), Integer.valueOf(0), Integer.valueOf(255), object -> this.box.getValue() != false && this.setting.getValue() == Pages.Render));
        this.bGreen = this.register(new Setting<Object>("BoxGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.box.getValue() != false && this.setting.getValue() == Pages.Render));
        this.bBlue = this.register(new Setting<Object>("BoxBlue", Integer.valueOf(145), Integer.valueOf(0), Integer.valueOf(255), object -> this.box.getValue() != false && this.setting.getValue() == Pages.Render));
        this.bAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(105), Integer.valueOf(0), Integer.valueOf(255), object -> this.box.getValue() != false && this.setting.getValue() == Pages.Render));
        this.outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), object -> this.setting.getValue() == Pages.Render));
        this.oRed = this.register(new Setting<Object>("OutlineRed", Integer.valueOf(58), Integer.valueOf(0), Integer.valueOf(255), object -> this.outline.getValue() != false && this.setting.getValue() == Pages.Render));
        this.oGreen = this.register(new Setting<Object>("OutlineGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.outline.getValue() != false && this.setting.getValue() == Pages.Render));
        this.oBlue = this.register(new Setting<Object>("OutlineBlue", Integer.valueOf(145), Integer.valueOf(0), Integer.valueOf(255), object -> this.outline.getValue() != false && this.setting.getValue() == Pages.Render));
        this.oAlpha = this.register(new Setting<Object>("OutlineAlpha", Integer.valueOf(111), Integer.valueOf(0), Integer.valueOf(255), object -> this.outline.getValue() != false && this.setting.getValue() == Pages.Render));
        this.lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.8f), Float.valueOf(0.1f), Float.valueOf(5.0f), object -> this.outline.getValue() != false && this.setting.getValue() == Pages.Render));
        this.circle = this.register(new Setting<Object>("Circle", Boolean.valueOf(true), object -> this.setting.getValue() == Pages.Render));
        this.circleStep1 = this.register(new Setting("CircleSpeed", 0.15f, 0.1f, 1.0f, object -> circle.getValue() && setting.getValue() == Pages.Render));
        this.circleHeight = this.register(new Setting("CircleHeight", 0.15f, 0.1f, 1.0f, object -> circle.getValue() && setting.getValue() == Pages.Render));
        this.CRed = this.register(new Setting<Object>("CircleRed", Integer.valueOf(64), Integer.valueOf(0), Integer.valueOf(255), object -> this.circle.getValue() != false && this.setting.getValue() == Pages.Render));
        this.CGreen = this.register(new Setting<Object>("CircleGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.circle.getValue() != false && this.setting.getValue() == Pages.Render));
        this.CBlue = this.register(new Setting<Object>("CircleBlue", Integer.valueOf(145), Integer.valueOf(0), Integer.valueOf(255), object -> this.circle.getValue() != false && this.setting.getValue() == Pages.Render));
        this.debugG = this.register(new Setting<Boolean>("Debug", Boolean.valueOf(false), bl -> this.setting.getValue() == Pages.Development));
        this.handleSequential1 = this.register(new Setting<Boolean>("handleSequential", Boolean.valueOf(false), bl -> this.debugG.getValue() != false && this.setting.getValue() == Pages.Development));
        this.breakCrystal1 = this.register(new Setting<Boolean>("breakcrystal", Boolean.valueOf(false), bl -> this.debugG.getValue() != false && this.setting.getValue() == Pages.Development));
        this.placeCrystal1 = this.register(new Setting<Boolean>("placeCrystal", Boolean.valueOf(false), bl -> this.debugG.getValue() != false && this.setting.getValue() == Pages.Development));
        this.abcc = this.register(new Setting<Boolean>("Null Break Crystal", Boolean.valueOf(false), bl -> this.debugG.getValue() != false && this.setting.getValue() == Pages.Development));
        this.instantdebug = this.register(new Setting<Boolean>("(MergeSync)", Boolean.valueOf(false), bl -> this.debugG.getValue() != false && this.setting.getValue() == Pages.Development));
        this.doinstantdebug = this.register(new Setting<Boolean>("DoInstant", Boolean.valueOf(false), bl -> this.debugG.getValue() != false && this.setting.getValue() == Pages.Development));
        this.rotationVector = null;
        this.rotations = new float[]{0.0f, 0.0f};
        this.rotationTimer = new Timer();
        this.prevPlacePos = null;
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();
        this.swapTimer = new Timer();
        this.renderDamage = 0.0f;
        this.renderTimeoutTimer = new Timer();
        this.renderBreakingTimer = new Timer();
        this.isPlacing = false;
        this.placeLocations = new ConcurrentHashMap();
        this.breakLocations = new ConcurrentHashMap();
        this.totemPops = new ConcurrentHashMap<EntityPlayer, Timer>();
        this.selfPlacePositions = new CopyOnWriteArrayList<BlockPos>();
        this.tickRunning = new AtomicBoolean(false);
        this.linearTimer = new Timer();
        this.cacheTimer = new Timer();
        this.cachePos = null;
        this.inhibitTimer = new Timer();
        this.inhibitEntity = null;
        this.scatterTimer = new Timer();
        this.bilateralVec = null;
        this.shouldRunThread = new AtomicBoolean(false);
        this.lastBroken = new AtomicBoolean(false);
        this.renderTargetTimer = new Timer();
        this.foundDoublePop = false;
        this.displayself = 0.0f;
        this.oldSlotCrystal = -1;
        this.oldSlotSword = -1;
        this.positions = new ArrayList<RenderPos>();
        this.setInstance();
    }

    private void runInstantThread() {
        if (this.instantdebug.getValue().booleanValue()) {
            Command.sendMessage("(Merge)RunningInstantThread");
        }
        if (this.mergeOffset.getValue().floatValue() == 0.0f) {
            this.doInstant();
        } else {
            this.shouldRunThread.set(true);
            if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive()) {
                if (this.thread == null) {
                    this.thread = new Thread(InstantThread.getInstance(this));
                }
                if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                    this.thread = new Thread(InstantThread.getInstance(this));
                }
                if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                    try {
                        this.thread.start();
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        }
    }

    public boolean isOffhand() {
        return FemboyAura.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
    }

    private List<Entity> getCrystalInRange() {
        return FemboyAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(entity -> this.isValidCrystalTarget((EntityEnderCrystal)entity)).collect(Collectors.toList());
    }

    public boolean isMoving(EntityPlayer entityPlayer) {
        return (double)entityPlayer.moveForward != 0.0 || (double)entityPlayer.moveStrafing != 0.0;
    }

    private boolean isValidCrystalTarget(EntityEnderCrystal entityEnderCrystal) {
        if (FemboyAura.mc.player.getPositionEyes(1.0f).distanceTo(entityEnderCrystal.getPositionVector()) > (double)this.breakRange.getValue().floatValue()) {
            return false;
        }
        if (this.breakLocations.containsKey(entityEnderCrystal.getEntityId()) && this.limit.getValue().booleanValue()) {
            return false;
        }
        if (this.breakLocations.containsKey(entityEnderCrystal.getEntityId()) && entityEnderCrystal.ticksExisted > this.delay.getValue() + this.attackFactor.getValue()) {
            return false;
        }
        return !(CrystalUtil.calculateDamage(entityEnderCrystal, (Entity) FemboyAura.mc.player) + this.suicideHealth.getValue().floatValue() >= FemboyAura.mc.player.getHealth() + FemboyAura.mc.player.getAbsorptionAmount());
    }

    public boolean placeCrystal(BlockPos blockPos, EnumFacing enumFacing) {
        if (blockPos != null) {
            if (this.placeCrystal1.getValue().booleanValue()) {
                Command.sendMessage("placeCrystal");
            }
            if (this.autoSwap.getValue() != ACSwapMode.OFF && !this.hasCrystal()) {
                return false;
            }
            if (!this.isOffhand() && FemboyAura.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
                if (this.oldSlotCrystal != -1) {
                    FemboyAura.mc.player.inventory.currentItem = this.oldSlotCrystal;
                    FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlotCrystal));
                    this.oldSlotCrystal = -1;
                }
                return false;
            }
            if (FemboyAura.mc.world.getBlockState(blockPos.up()).getBlock() == Blocks.FIRE) {
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos.up(), EnumFacing.DOWN));
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos.up(), EnumFacing.DOWN));
                if (this.oldSlotCrystal != -1) {
                    FemboyAura.mc.player.inventory.currentItem = this.oldSlotCrystal;
                    FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlotCrystal));
                    this.oldSlotCrystal = -1;
                }
                return true;
            }
            this.isPlacing = true;
            if (this.postResult == null) {
                BlockUtil.rightClickBlock(blockPos, FemboyAura.mc.player.getPositionVector().add(0.0, (double) FemboyAura.mc.player.getEyeHeight(), 0.0), this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, enumFacing, true);
            } else {
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (float)this.postResult.hitVec.x, (float)this.postResult.hitVec.y, (float)this.postResult.hitVec.z));
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketAnimation(this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
            }
            if (this.foundDoublePop && this.renderTarget != null) {
                this.totemPops.put(this.renderTarget, new Timer());
            }
            this.isPlacing = false;
            this.placeLocations.put(blockPos, System.currentTimeMillis());
            if (this.security.getValue().floatValue() >= 0.5f) {
                this.selfPlacePositions.add(blockPos);
            }
            this.renderTimeoutTimer.reset();
            this.prevPlacePos = blockPos;
            if (this.oldSlotCrystal != -1) {
                FemboyAura.mc.player.inventory.currentItem = this.oldSlotCrystal;
                FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.oldSlotCrystal));
                this.oldSlotCrystal = -1;
            }
            return true;
        }
        return false;
    }

    public static void glBillboard(float f, float f2, float f3) {
        float f4 = 0.02666667f;
        GlStateManager.translate((double)((double)f - ((IRenderManager)mc.getRenderManager()).getRenderPosX()), (double)((double)f2 - ((IRenderManager)mc.getRenderManager()).getRenderPosY()), (double)((double)f3 - ((IRenderManager)mc.getRenderManager()).getRenderPosZ()));
        GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-Minecraft.getMinecraft().player.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)Minecraft.getMinecraft().player.rotationPitch, (float)(Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)(-f4), (float)(-f4), (float)f4);
    }

    private EntityEnderCrystal findCrystalTarget(List<EntityPlayer> list) {
        this.breakLocations.forEach((n, l) -> {
            if (System.currentTimeMillis() - l > 1000L) {
                this.breakLocations.remove(n);
            }
        });
        if (this.syncMode.getValue() == SyncMode.STRICT && !this.limit.getValue().booleanValue() && this.lastBroken.get()) {
            return null;
        }
        EntityEnderCrystal entityEnderCrystal = null;
        int n2 = (int)Math.max(100.0f, (float)(CrystalUtil.ping() + 50) / 1.0f) + 150;
        if (this.inhibit.getValue().booleanValue() && !this.limit.getValue().booleanValue() && !this.inhibitTimer.passedMs(n2) && this.inhibitEntity != null && FemboyAura.mc.world.getEntityByID(this.inhibitEntity.getEntityId()) != null && this.isValidCrystalTarget(this.inhibitEntity)) {
            entityEnderCrystal = this.inhibitEntity;
            return entityEnderCrystal;
        }
        List<Entity> list2 = this.getCrystalInRange();
        if (list2.isEmpty()) {
            return null;
        }
        if (this.security.getValue().floatValue() >= 1.0f) {
            double d = 0.5;
            for (Entity entity2 : list2) {
                if (!(entity2.getPositionVector().distanceTo(FemboyAura.mc.player.getPositionEyes(1.0f)) < (double)this.breakWallsRange.getValue().floatValue()) && !CrystalUtil.rayTraceBreak(entity2.posX, entity2.posY, entity2.posZ)) continue;
                EntityEnderCrystal entityEnderCrystal2 = (EntityEnderCrystal)entity2;
                double d2 = 0.0;
                for (EntityPlayer entityPlayer : list) {
                    double d3 = CrystalUtil.calculateDamage(entityEnderCrystal2, (Entity)entityPlayer);
                    d2 += d3;
                }
                double d4 = CrystalUtil.calculateDamage(entityEnderCrystal2, (Entity) FemboyAura.mc.player);
                if (d4 > d2 * (double)(this.security.getValue().floatValue() - 0.8f) && !this.selfPlacePositions.contains((Object)new BlockPos(entity2.posX, entity2.posY - 1.0, entity2.posZ)) || !(d2 > d)) continue;
                d = d2;
                entityEnderCrystal = entityEnderCrystal2;
            }
        } else {
            entityEnderCrystal = this.security.getValue().floatValue() >= 0.5f ? (EntityEnderCrystal)list2.stream().filter(entity -> this.selfPlacePositions.contains((Object)new BlockPos(entity.posX, entity.posY - 1.0, entity.posZ))).filter(entity -> entity.getPositionVector().distanceTo(FemboyAura.mc.player.getPositionEyes(1.0f)) < (double)this.breakWallsRange.getValue().floatValue() || CrystalUtil.rayTraceBreak(entity.posX, entity.posY, entity.posZ)).min(Comparator.comparing(entity -> FemboyAura.mc.player.getDistance(entity))).orElse(null) : (EntityEnderCrystal)list2.stream().filter(entity -> entity.getPositionVector().distanceTo(FemboyAura.mc.player.getPositionEyes(1.0f)) < (double)this.breakWallsRange.getValue().floatValue() || CrystalUtil.rayTraceBreak(entity.posX, entity.posY, entity.posZ)).min(Comparator.comparing(entity -> FemboyAura.mc.player.getDistance(entity))).orElse(null);
        }
        return entityEnderCrystal;
    }

    private double getDistance(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = d - d4;
        double d8 = d2 - d5;
        double d9 = d3 - d6;
        return Math.sqrt(d7 * d7 + d8 * d8 + d9 * d9);
    }

    private void doInstant() {
        BlockPos blockPos;
        List<BlockPos> list;
        if (this.doinstantdebug.getValue().booleanValue()) {
            Command.sendMessage("Doinstant");
        }
        if (this.confirm.getValue() != ConfirmMode.OFF && (this.confirm.getValue() != ConfirmMode.FULL || this.inhibitEntity == null || (double)this.inhibitEntity.ticksExisted >= Math.floor(this.delay.getValue().intValue()))) {
            int n = (int)Math.max(100.0f, (float)(CrystalUtil.ping() + 50) / 1.0f) + 150;
            if (this.cachePos != null && !this.cacheTimer.passedMs(n + 100) && this.canPlaceCrystal(this.cachePos)) {
                this.postPlacePos = this.cachePos;
                this.postFacing = this.handlePlaceRotation(this.postPlacePos);
                if (this.postPlacePos != null) {
                    if (!this.placeCrystal(this.postPlacePos, this.postFacing)) {
                        this.postPlacePos = null;
                        return;
                    }
                    this.placeTimer.reset();
                    this.postPlacePos = null;
                }
                return;
            }
        }
        if (!(list = this.findCrystalBlocks()).isEmpty() && (blockPos = this.findPlacePosition(list, this.getTargetsInRange())) != null) {
            this.postPlacePos = blockPos;
            this.postFacing = this.handlePlaceRotation(this.postPlacePos);
            if (this.postPlacePos != null) {
                if (!this.placeCrystal(this.postPlacePos, this.postFacing)) {
                    this.postPlacePos = null;
                    return;
                }
                this.placeTimer.reset();
                this.postPlacePos = null;
            }
        }
    }

    @Override
    public void onDisable() {
        this.lastRenderPos = null;
        this.positions.clear();
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive receive) {
        SPacketEntityStatus sPacketEntityStatus;
        if (receive.getPacket() instanceof SPacketSpawnObject) {
            SPacketSpawnObject sPacketSpawnObject = (SPacketSpawnObject)receive.getPacket();
            if (sPacketSpawnObject.getType() == 51) {
                this.placeLocations.forEach((blockPos, l) -> {
                    if (this.getDistance((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, sPacketSpawnObject.getX(), sPacketSpawnObject.getY() - 1.0, sPacketSpawnObject.getZ()) < 1.0) {
                        try {
                            this.placeLocations.remove(blockPos);
                            this.cachePos = null;
                            if (!this.limit.getValue().booleanValue() && this.inhibit.getValue().booleanValue()) {
                                this.scatterTimer.reset();
                            }
                        }
                        catch (ConcurrentModificationException concurrentModificationException) {
                            // empty catch block
                        }
                        if (this.timingMode.getValue() != TimingMode.NORMAL) {
                            return;
                        }
                        if (!this.swapTimer.passedMs((long)(this.swapDelay.getValue().floatValue() * 100.0f))) {
                            return;
                        }
                        if (this.tickRunning.get()) {
                            return;
                        }
                        if (FemboyAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                            return;
                        }
                        if (this.breakLocations.containsKey(sPacketSpawnObject.getEntityID())) {
                            return;
                        }
                        if (FemboyAura.mc.player.getHealth() + FemboyAura.mc.player.getAbsorptionAmount() < this.disableUnderHealth.getValue().floatValue() || this.noGapSwitch.getValue() != false && FemboyAura.mc.player.getActiveItemStack().getItem() instanceof ItemFood || this.noMineSwitch.getValue().booleanValue() && FemboyAura.mc.playerController.getIsHittingBlock() && FemboyAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemTool) {
                            this.rotationVector = null;
                            return;
                        }
                        Vec3d vec3d = new Vec3d(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ());
                        if (FemboyAura.mc.player.getPositionEyes(1.0f).distanceTo(vec3d) > (double)this.breakRange.getValue().floatValue()) {
                            return;
                        }
                        if (!this.breakTimer.passedMs((long)(1000.0f - this.breakSpeed.getValue().floatValue() * 50.0f))) {
                            return;
                        }
                        if (CrystalUtil.calculateDamage(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ(), (Entity) FemboyAura.mc.player) + this.suicideHealth.getValue().floatValue() >= FemboyAura.mc.player.getHealth() + FemboyAura.mc.player.getAbsorptionAmount()) {
                            return;
                        }
                        this.breakLocations.put(sPacketSpawnObject.getEntityID(), System.currentTimeMillis());
                        this.bilateralVec = new Vec3d(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ());
                        CPacketUseEntity cPacketUseEntity = new CPacketUseEntity();
                        ((ICPacketUseEntity)cPacketUseEntity).setEntityId(sPacketSpawnObject.getEntityID());
                        ((ICPacketUseEntity)cPacketUseEntity).setAction(CPacketUseEntity.Action.ATTACK);
                        FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketAnimation(this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
                        FemboyAura.mc.player.connection.sendPacket((Packet)cPacketUseEntity);
                        this.swingArmAfterBreaking(this.isOffhand() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                        this.renderBreakingPos = new BlockPos(sPacketSpawnObject.getX(), sPacketSpawnObject.getY() - 1.0, sPacketSpawnObject.getZ());
                        this.renderBreakingTimer.reset();
                        this.breakTimer.reset();
                        this.linearTimer.reset();
                        if (this.syncMode.getValue() == SyncMode.MERGE) {
                            this.placeTimer.reset();
                        }
                        if (this.syncMode.getValue() == SyncMode.STRICT) {
                            this.lastBroken.set(true);
                        }
                        if (this.syncMode.getValue() == SyncMode.MERGE) {
                            this.runInstantThread();
                        }
                    }
                });
            }
        } else if (receive.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect sPacketSoundEffect = (SPacketSoundEffect)receive.getPacket();
            if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                if (this.inhibitEntity != null && this.inhibitEntity.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) < 6.0) {
                    this.inhibitEntity = null;
                }
                if (this.security.getValue().floatValue() >= 0.5f) {
                    try {
                        this.selfPlacePositions.remove((Object)new BlockPos(sPacketSoundEffect.getX(), sPacketSoundEffect.getY() - 1.0, sPacketSoundEffect.getZ()));
                    }
                    catch (ConcurrentModificationException concurrentModificationException) {}
                }
            }
        } else if (receive.getPacket() instanceof SPacketEntityStatus && (sPacketEntityStatus = (SPacketEntityStatus)receive.getPacket()).getOpCode() == 35 && sPacketEntityStatus.getEntity((World) FemboyAura.mc.world) instanceof EntityPlayer) {
            this.totemPops.put((EntityPlayer)sPacketEntityStatus.getEntity((World) FemboyAura.mc.world), new Timer());
        }
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
                        BlockPos blockPos2 = new BlockPos(n6, n8 + n2, n7);
                        arrayList.add(blockPos2);
                    }
                    ++n8;
                }
                ++n7;
            }
            ++n6;
        }
        return arrayList;
    }

    @SubscribeEvent
    public void onChangeItem(PacketEvent.Send send) {
        if (send.getPacket() instanceof CPacketHeldItemChange) {
            this.swapTimer.reset();
        }
    }

    public EntityEnderCrystal getPostBreakPos() {
        return this.postBreakPos;
    }

    private boolean isDoublePoppable(EntityPlayer entityPlayer, float f) {
        if (this.predictPops.getValue().booleanValue() && entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= 2.0f && (double)f > (double)entityPlayer.getHealth() + (double)entityPlayer.getAbsorptionAmount() + 0.5 && f <= 4.0f) {
            Timer timer = this.totemPops.get((Object)entityPlayer);
            return timer == null || timer.passedMs(500L);
        }
        return false;
    }

    public boolean switchToSword() {
        int n = CrystalUtil.getSwordSlot();
        if (FemboyAura.mc.player.inventory.currentItem != n && n != -1) {
            if (this.antiWeakness.getValue() == ACAntiWeakness.SILENT) {
                this.oldSlotSword = FemboyAura.mc.player.inventory.currentItem;
            }
            FemboyAura.mc.player.inventory.currentItem = n;
            FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
        }
        return n != -1;
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList nonNullList = NonNullList.create();
        nonNullList.addAll((Collection) FemboyAura.getSphere(new BlockPos((Entity) FemboyAura.mc.player), this.strictDirection.getValue() != false ? this.placeRange.getValue().floatValue() + 2.0f : this.placeRange.getValue().floatValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        return nonNullList;
    }

    public boolean hasCrystal() {
        if (this.isOffhand()) {
            return true;
        }
        int n = CrystalUtil.getCrystalSlot();
        if (n == -1) {
            return false;
        }
        if (FemboyAura.mc.player.inventory.currentItem == n) {
            return true;
        }
        if (this.autoSwap.getValue() == ACSwapMode.SILENT) {
            this.oldSlotCrystal = FemboyAura.mc.player.inventory.currentItem;
        }
        FemboyAura.mc.player.inventory.currentItem = n;
        FemboyAura.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
        return true;
    }

    private static enum TimingMode {
        SEQUENTIAL,
        NORMAL;

    }

    private static enum YawStepMode {
        OFF,
        SEMI,
        FULL;

    }

    private static enum TargetingMode {
        ALL,
        SMART,
        NEAREST;

    }

    public static enum ACSwapMode {
        OFF,
        NORMAL,
        SILENT;

    }

    private static enum RotationMode {
        OFF,
        TRACK,
        INTERACT;

    }

    public static enum ConfirmMode {
        OFF,
        SEMI,
        FULL;

    }

    public static enum Pages {
        General,
        Place,
        Break,
        Calculation,
        Render,
        Development;

    }

    private static class InstantThread
            implements Runnable {
        private /* synthetic */ FemboyAura FemboyAura;
        private static /* synthetic */ InstantThread INSTANCE;

        private InstantThread() {
        }

        static InstantThread getInstance(FemboyAura femboyAura) {
            if (INSTANCE == null) {
                INSTANCE = new InstantThread();
                InstantThread.INSTANCE.FemboyAura = femboyAura;
            }
            return INSTANCE;
        }

        @Override
        public void run() {
            if (this.FemboyAura.shouldRunThread.get()) {
                try {
                    Thread.sleep((long)(aboba * 40.0f));
                }
                catch (InterruptedException interruptedException) {
                    this.FemboyAura.thread.interrupt();
                }
                if (!this.FemboyAura.shouldRunThread.get()) {
                    return;
                }
                this.FemboyAura.shouldRunThread.set(false);
                if (this.FemboyAura.tickRunning.get()) {
                    return;
                }
                this.FemboyAura.doInstant();
            }
        }
    }

    public static enum SyncMode {
        STRICT,
        MERGE;

    }

    private class RenderPos {
        private /* synthetic */ BlockPos renderPos;
        private /* synthetic */ float renderTime;

        public void setRenderTime(float f) {
            this.renderTime = f;
        }

        public BlockPos getPos() {
            return this.renderPos;
        }

        public void setPos(BlockPos blockPos) {
            this.renderPos = blockPos;
        }

        public RenderPos(BlockPos blockPos, float f) {
            this.renderPos = blockPos;
            this.renderTime = f;
        }

        public float getRenderTime() {
            return this.renderTime;
        }
    }

    public static enum DirectionMode {
        VANILLA,
        NORMAL,
        STRICT;

    }

    public static enum ACAntiWeakness {
        OFF,
        NORMAL,
        SILENT;

    }
}