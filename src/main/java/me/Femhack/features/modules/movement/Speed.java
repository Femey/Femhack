package me.Femhack.features.modules.movement;

import me.Femhack.Femhack;
import me.Femhack.features.modules.*;
import me.Femhack.features.setting.*;
import me.Femhack.mixin.mixins.AccessorCPacketPlayer;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.client.*;
import me.Femhack.event.events.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import net.minecraft.init.*;
import java.math.*;

public class Speed extends Module
{
    private Setting<mode> Mode = this.register(new Setting("Mode", mode.STRAFE));
    public Setting<Float> speed = this.register(new Setting("Vanlla Speed", 4.0f, 1.0f, 10.0f, v -> this.Mode.getValue() == mode.VANILLA));
    public Setting<Boolean> onGroundStrict = this.register(new Setting("OnGround Strict", true));
    public Setting<Boolean> water = this.register(new Setting("Water", true));
    public Setting<Boolean> autoSprint = this.register(new Setting("AutoSprint", true));
    public Setting<Boolean> timer = this.register(new Setting("Timer", true));
    public Setting<Boolean> timer2 = this.register(new Setting("Timer2", true));
    public Setting<Boolean> timer3 = this.register(new Setting("Timer3", true));
    public Setting<Float> speed1 = this.register(new Setting("Speed", 4.0f, 0.0f, 2.0f));
    public Setting<Float> speed2 = this.register(new Setting("Speed2", 4.0f, 0.0f, 2.0f));
    private boolean flip;
    private int rhh;
    private int stage;
    private double moveSpeed;
    private double distance;

    public Speed() {
        super("Speed", "crazy", Module.Category.MOVEMENT, true, false, false);
        this.flip = false;
        this.rhh = 0;
        this.stage = 0;
        this.moveSpeed = 0.0;
        this.distance = 0.0;
    }

    public void onEnable() {
        try {
            this.stage = 2;
            this.distance = 0.0;
            this.moveSpeed = this.getBaseMoveSpeed();
            Femhack.TICK_TIMER = 1.0f;
            if (this.autoSprint.getValue() && Speed.mc.player != null) {
                Speed.mc.player.setSprinting(false);
            }
        }
        catch (Exception ex) {}
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive e) {
        if (e.getPacket() instanceof SPacketPlayerPosLook && (this.Mode.getValue() == mode.STRAFE || this.Mode.getValue() == mode.ONGROUND)) {
            this.rhh = 6;
            if (this.Mode.getValue() == mode.ONGROUND) {
                this.stage = 2;
            }
            else {
                this.stage = 2;
                this.flip = false;
            }
            this.distance = 0.0;
            this.moveSpeed = this.getBaseMoveSpeed();
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send e) {
        if (e.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer)e.getPacket();
            if (this.Mode.getValue() == mode.ONGROUND && this.stage == 3) {
                ((AccessorCPacketPlayer)packet).setY(packet.getY(0.0) + 0.4);
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent e) {
        if (e.getStage() == 0) {
            final double d3 = Speed.mc.player.posX - Speed.mc.player.prevPosX;
            final double d4 = Speed.mc.player.posZ - Speed.mc.player.prevPosZ;
            this.distance = Math.sqrt(d3 * d3 + d4 * d4);
        }
    }

    @SubscribeEvent
    public void onMoveEvent(final MoveEvent event) {
        if (nullCheck()) {
            return;
        }
        if (Speed.mc.player.isElytraFlying() || Speed.mc.player.fallDistance >= 4.0f) {
            return;
        }
        if (!this.water.getValue() && (Speed.mc.player.isInWater() || Speed.mc.player.isInLava())) {
            return;
        }
        if (this.rhh > 0) {
            --this.rhh;
        }
        if (this.autoSprint.getValue()) {
            Speed.mc.player.setSprinting(true);
        }
        if (this.Mode.getValue() == mode.STRAFE) {
            if (this.timer.getValue()) {
                Femhack.TICK_TIMER = 1.08f;
            }
            if (this.round(Speed.mc.player.posY - (int)Speed.mc.player.posY, 3) == this.round(0.138, 3)) {
                final EntityPlayerSP player = Speed.mc.player;
                --player.motionY;
                event.motionY -= 0.09316090325960147;
            }
            if (this.stage == 2 && this.isMoving()) {
                if (Speed.mc.player.collidedVertically) {
                    event.motionY = 0.4;
                    Speed.mc.player.motionY = 0.3995;
                    this.flip = !this.flip;
                    Femhack.TICK_TIMER = 1.0f;
                    if (this.flip) {
                        this.moveSpeed *= 1.5499999523162842;
                    }
                    else {
                        this.moveSpeed *= 1.3949999809265137;
                    }
                }
            }
            else if (this.stage == 3 && this.isMoving()) {
                final double var = 0.66 * (this.distance - this.getBaseMoveSpeed());
                if (this.timer3.getValue()) {
                    this.moveSpeed = this.speed1.getValue();
                }
                else {
                    this.moveSpeed = this.distance - var;
                }
                if (this.timer.getValue()) {
                    if (this.flip) {
                        Femhack.TICK_TIMER = 1.125f;
                    }
                    else {
                        Femhack.TICK_TIMER = 1.0088f;
                    }
                }
            }
            else {
                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically) {
                    this.stage = 1;
                }
                this.moveSpeed = this.distance - this.distance / 159.0;
            }
            float val = 1.0f;
            val *= (float)this.getBaseMoveSpeed();
            this.moveSpeed = Math.max(this.moveSpeed, val);
            final float[] dir = this.getYaw(this.moveSpeed);
            event.motionX = dir[0];
            event.motionZ = dir[1];
            ++this.stage;
        }
        else if (this.Mode.getValue() == mode.ONGROUND) {
            if (Speed.mc.player.collidedHorizontally || !this.checkMove()) {
                Femhack.TICK_TIMER = 1.0f;
            }
            else {
                if (!this.onGroundStrict.getValue()) {
                    if (!Speed.mc.player.onGround) {
                        Femhack.TICK_TIMER = 1.0f;
                    }
                    else if (this.stage == 2) {
                        Femhack.TICK_TIMER = 1.0f;
                        if (this.rhh > 0) {
                            this.moveSpeed = this.getBaseMoveSpeed();
                        }
                        this.moveSpeed *= 2.149;
                        this.stage = 3;
                    }
                    else if (this.stage == 3) {
                        if (this.timer.getValue()) {
                            Femhack.TICK_TIMER = Math.max(1.0f + new Random().nextFloat(), 1.2f);
                        }
                        else {
                            Femhack.TICK_TIMER = 1.0f;
                        }
                        this.stage = 2;
                        final double var = 0.66 * (this.distance - this.getBaseMoveSpeed());
                        if (this.timer2.getValue()) {
                            this.moveSpeed = this.speed2.getValue();
                        }
                        else {
                            this.moveSpeed = this.distance - var;
                        }
                    }
                }
                this.setVanilaSpeed(event, this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed()));
            }
        }
        else if (this.Mode.getValue() == mode.GROUNDSTRAFE) {
            if (Speed.mc.player.collidedHorizontally || Speed.mc.player.movementInput.sneak) {
                return;
            }
            if (Speed.mc.player.isHandActive() && Speed.mc.player.getHeldItemMainhand().getItem() instanceof ItemFood) {
                return;
            }
            if (!this.checkMove()) {
                return;
            }
            if (Speed.mc.player.onGround) {
                if (Speed.mc.player.ticksExisted % 2 == 0) {
                    Femhack.TICK_TIMER = 1.0f;
                    this.stage = 2;
                }
                else {
                    if (this.timer.getValue()) {
                        Femhack.TICK_TIMER = 1.2f;
                    }
                    else {
                        Femhack.TICK_TIMER = 1.0f;
                    }
                    this.stage = 3;
                }
                this.moveSpeed = this.getBaseMoveSpeed();
            }
            else {
                Femhack.TICK_TIMER = 1.0f;
                this.stage = 0;
            }
            this.setVanilaSpeed(event, this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed()));
        }
        else if (this.Mode.getValue() == mode.VANILLA) {
            final double speedval = this.speed.getValue() / 5.0;
            this.setVanilaSpeed(event, speedval);
        }
    }

    public double getBaseMoveSpeed() {
        double d = 0.2873;
        if (Speed.mc.player != null && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            final int n = Speed.mc.player.getActivePotionEffect(MobEffects.SPEED).getAmplifier();
            d *= 1.0 + 0.2 * (n + 1);
        }
        return d;
    }

    public float[] setYaw(final float yaw, final double niggers) {
        float moveForward = Speed.mc.player.movementInput.moveForward;
        float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
        float rotationYaw = yaw;
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            final float[] ret = { 0.0f, 0.0f };
            return ret;
        }
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                moveStrafe = 0.0f;
            }
            else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        final double newX = moveForward * niggers * motionX + moveStrafe * niggers * motionZ;
        final double newZ = moveForward * niggers * motionZ - moveStrafe * niggers * motionX;
        final float[] ret2 = { (float)newX, (float)newZ };
        return ret2;
    }

    public float[] getYaw(final double niggers) {
        final float yaw = Speed.mc.player.prevRotationYaw + (Speed.mc.player.rotationYaw - Speed.mc.player.prevRotationYaw) * Speed.mc.getRenderPartialTicks();
        return this.setYaw(yaw, niggers);
    }

    public boolean isMoving() {
        return Speed.mc.player.movementInput.moveForward != 0.0f || Speed.mc.player.movementInput.moveStrafe != 0.0f;
    }

    public double round(final double value, final int places) {
        final BigDecimal b = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return b.doubleValue();
    }

    public boolean checkMove() {
        return Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f;
    }

    public void setVanilaSpeed(final MoveEvent event, final double speed) {
        float moveForward = Speed.mc.player.movementInput.moveForward;
        float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
        float rotationYaw = Speed.mc.player.rotationYaw;
        if (moveForward == 0.0f && moveStrafe == 0.0f) {
            event.motionX = 0.0;
            event.motionZ = 0.0;
            return;
        }
        if (moveForward != 0.0f) {
            if (moveStrafe >= 1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                moveStrafe = 0.0f;
            }
            else if (moveStrafe <= -1.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                moveStrafe = 0.0f;
            }
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
        final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
        final double newX = moveForward * speed * motionX + moveStrafe * speed * motionZ;
        final double newZ = moveForward * speed * motionZ - moveStrafe * speed * motionX;
        event.motionX = newX;
        event.motionZ = newZ;
    }

    public enum mode
    {
        STRAFE,
        ONGROUND,
        GROUNDSTRAFE,
        VANILLA;
    }
}