package me.Femhack.features.modules.movement;

import me.Femhack.features.setting.Setting;
import me.Femhack.Femhack;
import me.Femhack.event.events.MoveEvent;
import me.Femhack.event.events.PacketEvent;
import me.Femhack.event.events.UpdateWalkingPlayerEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.modules.player.Freecam;
import me.Femhack.util.EntityUtil;
import me.Femhack.util.Timer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Speed
        extends Module {
    private static Speed INSTANCE;
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NCP));
    private final Setting<Boolean> limiter = this.register(new Setting<Boolean>("SetGround", true));
    private final Setting<Boolean> bhop2 = this.register(new Setting<Boolean>("Hop", true));
    private final Setting<Boolean> limiter2 = this.register(new Setting<Boolean>("Bhop", false));
    private final Setting<Boolean> noLag = this.register(new Setting<Boolean>("NoLag", false));
    private final Setting<Integer> specialMoveSpeed = this.register(new Setting<Integer>("Speed", 100, 0, 150));
    private final Setting<Integer> potionSpeed = this.register(new Setting<Integer>("Speed1", 130, 0, 150));
    private final Setting<Integer> potionSpeed2 = this.register(new Setting<Integer>("Speed2", 125, 0, 150));
    private final Setting<Integer> dFactor = this.register(new Setting<Integer>("DFactor", 159, 100, 200));
    private final Setting<Integer> acceleration = this.register(new Setting<Integer>("Accel", 2149, 1000, 2500));
    private final Setting<Float> speedLimit = this.register(new Setting<Float>("SpeedLimit", Float.valueOf(35.0f), Float.valueOf(20.0f), Float.valueOf(60.0f)));
    private final Setting<Float> speedLimit2 = this.register(new Setting<Float>("SpeedLimit2", Float.valueOf(60.0f), Float.valueOf(20.0f), Float.valueOf(60.0f)));
    private final Setting<Integer> yOffset = this.register(new Setting<Integer>("YOffset", 400, 350, 500));
    private final Setting<Boolean> potion = this.register(new Setting<Boolean>("Potion", false));
    private final Setting<Boolean> wait = this.register(new Setting<Boolean>("Wait", true));
    private final Setting<Boolean> hopWait = this.register(new Setting<Boolean>("HopWait", true));
    private final Setting<Integer> startStage = this.register(new Setting<Integer>("Stage", 2, 0, 4));
    private final Setting<Boolean> setPos = this.register(new Setting<Boolean>("SetPos", true));
    private final Setting<Boolean> setNull = this.register(new Setting<Boolean>("SetNull", false));
    private final Setting<Integer> setGroundLimit = this.register(new Setting<Integer>("GroundLimit", 138, 0, 1000));
    private final Setting<Integer> groundFactor = this.register(new Setting<Integer>("GroundFactor", 13, 0, 50));
    private final Setting<Integer> step = this.register(new Setting<Object>("SetStep", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(2), v -> this.mode.getValue() == Mode.BHOP));
    private final Setting<Boolean> setGroundNoLag = this.register(new Setting<Boolean>("NoGroundLag", true));
    private int stage = 1;
    private double moveSpeed;
    private double lastDist;
    private int cooldownHops = 0;
    private boolean waitForGround = false;
    private final Timer timer = new Timer();
    private int hops = 0;

    public Speed() {
        super("Speed", "AirControl etc.", Module.Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    public static Speed getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speed();
        }
        return INSTANCE;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) amplifier;
        }
        return baseSpeed;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(value).setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    @Override
    public void onEnable() {
        if (!Speed.mc.player.onGround) {
            this.waitForGround = true;
        }
        this.hops = 0;
        this.timer.reset();
        this.moveSpeed = Speed.getBaseMoveSpeed();
    }

    @Override
    public void onDisable() {
        this.hops = 0;
        this.moveSpeed = 0.0;
        this.stage = this.startStage.getValue();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            this.lastDist = Math.sqrt((Speed.mc.player.posX - Speed.mc.player.prevPosX) * (Speed.mc.player.posX - Speed.mc.player.prevPosX) + (Speed.mc.player.posZ - Speed.mc.player.prevPosZ) * (Speed.mc.player.posZ - Speed.mc.player.prevPosZ));
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (event.getStage() != 0 || this.shouldReturn()) {
            return;
        }
        if (!Speed.mc.player.onGround) {
            if (this.wait.getValue().booleanValue() && this.waitForGround) {
                return;
            }
        } else {
            this.waitForGround = false;
        }
        if (this.mode.getValue() == Mode.NCP) {
            this.doNCP(event);
        } else if (this.mode.getValue() == Mode.BHOP) {
            float moveForward = Speed.mc.player.movementInput.moveForward;
            float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (this.step.getValue() == 1) {
                Speed.mc.player.stepHeight = 0.6f;
            }
            if (this.limiter2.getValue().booleanValue() && Speed.mc.player.onGround && Femhack.speedManager.getSpeedKpH() < (double) this.speedLimit2.getValue().floatValue()) {
                this.stage = 2;
            }
            if (this.limiter.getValue().booleanValue() && Speed.round(Speed.mc.player.posY - (double) ((int) Speed.mc.player.posY), 3) == Speed.round((double) this.setGroundLimit.getValue().intValue() / 1000.0, 3) && (!this.setGroundNoLag.getValue().booleanValue() || EntityUtil.isEntityMoving(Speed.mc.player))) {
                if (this.setNull.getValue().booleanValue()) {
                    Speed.mc.player.motionY = 0.0;
                } else {
                    Speed.mc.player.motionY -= (double) this.groundFactor.getValue().intValue() / 100.0;
                    event.setY(event.getY() - (double) this.groundFactor.getValue().intValue() / 100.0);
                    if (this.setPos.getValue().booleanValue()) {
                        Speed.mc.player.posY -= (double) this.groundFactor.getValue().intValue() / 100.0;
                    }
                }
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = (double) this.getMultiplier() * Speed.getBaseMoveSpeed() - 0.01;
            } else if (this.stage == 2 && EntityUtil.isMoving()) {
                this.stage = 3;
                Speed.mc.player.motionY = (double) this.yOffset.getValue().intValue() / 1000.0;
                event.setY((double) this.yOffset.getValue().intValue() / 1000.0);
                if (this.cooldownHops > 0) {
                    --this.cooldownHops;
                }
                ++this.hops;
                double accel = this.acceleration.getValue() == 2149 ? 2.149802 : (double) this.acceleration.getValue().intValue() / 1000.0;
                this.moveSpeed *= accel;
            } else if (this.stage == 3) {
                this.stage = 4;
                double difference = 0.66 * (this.lastDist - Speed.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            } else {
                if (Speed.mc.world.getCollisionBoxes(Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically && this.stage > 0) {
                    this.stage = this.bhop2.getValue() != false && Femhack.speedManager.getSpeedKpH() >= (double) this.speedLimit.getValue().floatValue() ? 0 : (Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / (double) this.dFactor.getValue().intValue();
            }
            this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
            if (this.hopWait.getValue().booleanValue() && this.limiter2.getValue().booleanValue() && this.hops < 2) {
                this.moveSpeed = EntityUtil.getMaxSpeed();
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            } else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += moveForward > 0.0f ? -45.0f : 45.0f;
                    moveStrafe = 0.0f;
                } else if (moveStrafe <= -1.0f) {
                    rotationYaw += moveForward > 0.0f ? 45.0f : -45.0f;
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                } else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                event.setX((double) moveForward * this.moveSpeed * motionX + (double) moveStrafe * this.moveSpeed * motionZ);
                event.setZ((double) moveForward * this.moveSpeed * motionZ - (double) moveStrafe * this.moveSpeed * motionX);
            }
            if (this.step.getValue() == 2) {
                Speed.mc.player.stepHeight = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                this.timer.reset();
                event.setX(0.0);
                event.setZ(0.0);
            }
        }
    }

    private void doNCP(MoveEvent event) {
        if (!this.limiter.getValue().booleanValue() && Speed.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if (Speed.mc.player.moveForward == 0.0f && Speed.mc.player.moveStrafing == 0.0f || !Speed.mc.player.onGround)
                    break;
                if (Speed.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    motionY += (float) (Speed.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }
                Speed.mc.player.motionY = motionY;
                event.setY(Speed.mc.player.motionY);
                this.moveSpeed *= 2.149;
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - Speed.getBaseMoveSpeed());
                break;
            }
            default: {
                if (Speed.mc.world.getCollisionBoxes(Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically && this.stage > 0) {
                    this.stage = this.bhop2.getValue() != false && Femhack.speedManager.getSpeedKpH() >= (double) this.speedLimit.getValue().floatValue() ? 0 : (Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, Speed.getBaseMoveSpeed());
        double forward = Speed.mc.player.movementInput.moveForward;
        double strafe = Speed.mc.player.movementInput.moveStrafe;
        double yaw = Speed.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else if (forward != 0.0 && strafe != 0.0) {
            forward *= Math.sin(0.7853981633974483);
            strafe *= Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
        event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        ++this.stage;
    }

    private float getMultiplier() {
        float baseSpeed = this.specialMoveSpeed.getValue().intValue();
        if (this.potion.getValue().booleanValue() && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            int amplifier = Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
            baseSpeed = amplifier >= 2 ? (float) this.potionSpeed2.getValue().intValue() : (float) this.potionSpeed.getValue().intValue();
        }
        return baseSpeed / 100.0f;
    }

    private boolean shouldReturn() {
        return Femhack.moduleManager.isModuleEnabled(String.valueOf(Freecam.class));
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && this.noLag.getValue().booleanValue()) {
            this.stage = this.mode.getValue() == Mode.BHOP && (this.limiter2.getValue() != false || this.bhop2.getValue() != false) ? 1 : 4;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() != Mode.NONE) {
            if (this.mode.getValue() == Mode.NCP) {
                return this.mode.currentEnumName().toUpperCase();
            }
            return this.mode.currentEnumName();
        }
        return null;
    }

    public enum Mode {
        NONE,
        NCP,
        BHOP

    }
}