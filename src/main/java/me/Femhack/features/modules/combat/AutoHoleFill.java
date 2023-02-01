package me.Femhack.features.modules.combat;

import java.util.ArrayList;
import me.Femhack.features.command.Command;
import me.Femhack.features.setting.Setting;
import me.Femhack.features.modules.Module;
import me.Femhack.util.HoleUtil;
import me.Femhack.util.InventoryUtil;
import me.Femhack.util.MathUtil;
import me.Femhack.util.Timer;
import me.Femhack.util.WorldUtils;
import me.Femhack.util.BlockUtil;
import me.Femhack.util.EntityUtil;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoHoleFill
        extends Module {
    public final /* synthetic */ Setting<Boolean> doubleHoles;
    public final /* synthetic */ Setting<Boolean> predict;
    public final /* synthetic */ Setting<Boolean> packet;
    public final /* synthetic */ Setting<Sensitivity> sensitivity;
    public final /* synthetic */ Setting<Boolean> smart;
    public final /* synthetic */ Setting<Integer> bps;
    public final /* synthetic */ Setting<Integer> range;
    public final /* synthetic */ Setting<Boolean> toggle;
    private final /* synthetic */ Setting<Boolean> disable;
    private final /* synthetic */ Timer offTimer;
    public final /* synthetic */ Setting<Integer> distance;
    private final /* synthetic */ Setting<Integer> disableTime;
    public final /* synthetic */ Setting<Boolean> rotate;
    public final /* synthetic */ Setting<Integer> validHoleHeight;
    public final /* synthetic */ Setting<Float> validPlayerRange;
    public final /* synthetic */ Setting<Integer> ticks;

    @Override
    public void onUpdate() {
        if (AutoHoleFill.nullCheck()) {
            return;
        }
        if (this.disable.getValue().booleanValue() && this.offTimer.passedMs(this.disableTime.getValue().intValue())) {
            Command.sendMessage("<HoleFill> Disable");
            this.disable();
        }
        int n = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int n2 = InventoryUtil.findHotbarBlock(BlockAnvil.class);
        int n3 = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        int n4 = -1;
        int n5 = AutoHoleFill.mc.player.inventory.currentItem;
        int n6 = 0;
        ArrayList<HoleUtil.Hole> arrayList = HoleUtil.holes(this.range.getValue().intValue(), this.validHoleHeight.getValue());
        if (this.smart.getValue().booleanValue()) {
            EntityPlayer entityPlayer = (EntityPlayer)EntityUtil.getTarget(true, false, false, false, false, this.validPlayerRange.getValue().floatValue(), EntityUtil.toMode("Closest"));
            if (entityPlayer == null) {
                return;
            }
            Vec3d hole = this.predict.getValue() != false ? entityPlayer.getPositionVector() : MathUtil.extrapolatePlayerPosition(entityPlayer, this.ticks.getValue());
            arrayList.removeIf(arg_0 -> this.help((Vec3d)hole, entityPlayer, arrayList, arg_0));
        }
        if (arrayList.isEmpty()) {
            return;
        }
        for (HoleUtil.Hole hole : arrayList) {
            Object object;
            if (this.smart.getValue().booleanValue()) {
                object = (EntityPlayer)EntityUtil.getTarget(true, false, false, false, false, 10.0, EntityUtil.toMode("Closest"));
                if (n6 >= this.bps.getValue()) continue;
                if (hole instanceof HoleUtil.SingleHole && !EntityUtil.isInHole((Entity)object) && WorldUtils.empty.contains((Object)WorldUtils.getBlock(((HoleUtil.SingleHole)hole).pos)) && (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1 || InventoryUtil.findHotbarBlock(BlockEnderChest.class) != -1) && !BlockUtil.isIntercepted(((HoleUtil.SingleHole)hole).pos)) {
                    if (this.sensitivity.getValue() == Sensitivity.Less) {
                        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                            AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                        } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                            AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n2));
                        } else {
                            if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                                return;
                            }
                            AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n3));
                        }
                    } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                        if (n != AutoHoleFill.mc.player.inventory.currentItem) {
                            n4 = AutoHoleFill.mc.player.inventory.currentItem;
                            AutoHoleFill.mc.player.inventory.currentItem = n;
                        }
                    } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                        if (n2 != AutoHoleFill.mc.player.inventory.currentItem) {
                            n4 = AutoHoleFill.mc.player.inventory.currentItem;
                            AutoHoleFill.mc.player.inventory.currentItem = n2;
                        }
                    } else {
                        if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                            return;
                        }
                        if (n3 != AutoHoleFill.mc.player.inventory.currentItem) {
                            n4 = AutoHoleFill.mc.player.inventory.currentItem;
                            AutoHoleFill.mc.player.inventory.currentItem = n3;
                        }
                    }
                    BlockUtil.placeBlockss(((HoleUtil.SingleHole)hole).pos, false, this.packet.getValue(), this.rotate.getValue());
                    if (this.sensitivity.getValue() == Sensitivity.Less) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n5));
                    } else if (this.sensitivity.getValue() == Sensitivity.Strong && n4 != -1) {
                        AutoHoleFill.mc.player.inventory.currentItem = n4;
                    }
                    ++n6;
                }
                if (n6 >= this.bps.getValue() || !(hole instanceof HoleUtil.DoubleHole) || !this.doubleHoles.getValue().booleanValue() || n6 >= this.bps.getValue()) continue;
                HoleUtil.DoubleHole doubleHole = (HoleUtil.DoubleHole)hole;
                if (!(!this.getDist(doubleHole.pos) || EntityUtil.isInHole((Entity)object) || BlockUtil.isInterceptedByOther(doubleHole.pos) || !WorldUtils.empty.contains((Object)WorldUtils.getBlock(doubleHole.pos)) || InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1 && InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1 || BlockUtil.isIntercepted(doubleHole.pos))) {
                    if (this.sensitivity.getValue() == Sensitivity.Less) {
                        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                            AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                        } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                            AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n2));
                        } else {
                            if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                                return;
                            }
                            AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n3));
                        }
                    } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                        if (n != AutoHoleFill.mc.player.inventory.currentItem) {
                            n4 = AutoHoleFill.mc.player.inventory.currentItem;
                            AutoHoleFill.mc.player.inventory.currentItem = n;
                        }
                    } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                        if (n2 != AutoHoleFill.mc.player.inventory.currentItem) {
                            n4 = AutoHoleFill.mc.player.inventory.currentItem;
                            AutoHoleFill.mc.player.inventory.currentItem = n2;
                        }
                    } else {
                        if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                            return;
                        }
                        if (n3 != AutoHoleFill.mc.player.inventory.currentItem) {
                            n4 = AutoHoleFill.mc.player.inventory.currentItem;
                            AutoHoleFill.mc.player.inventory.currentItem = n3;
                        }
                    }
                    BlockUtil.placeBlockss(doubleHole.pos, false, this.packet.getValue(), this.rotate.getValue());
                    if (this.sensitivity.getValue() == Sensitivity.Less) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n5));
                    } else if (this.sensitivity.getValue() == Sensitivity.Strong && n4 != -1) {
                        AutoHoleFill.mc.player.inventory.currentItem = n4;
                    }
                    ++n6;
                }
                if (n6 >= this.bps.getValue() || !this.getDist(doubleHole.pos1) || EntityUtil.isInHole((Entity)object) || BlockUtil.isInterceptedByOther(doubleHole.pos1) || !WorldUtils.empty.contains((Object)WorldUtils.getBlock(doubleHole.pos1)) || InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1 && InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1 || BlockUtil.isIntercepted(doubleHole.pos1)) continue;
                if (this.sensitivity.getValue() == Sensitivity.Less) {
                    if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                    } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n2));
                    } else {
                        if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                            return;
                        }
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n3));
                    }
                } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                    if (n != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n;
                    }
                } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                    if (n2 != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n2;
                    }
                } else {
                    if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                        return;
                    }
                    if (n3 != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n3;
                    }
                }
                BlockUtil.placeBlockss(doubleHole.pos1, false, this.packet.getValue(), this.rotate.getValue());
                if (this.sensitivity.getValue() == Sensitivity.Less) {
                    AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n5));
                } else if (this.sensitivity.getValue() == Sensitivity.Strong && n4 != -1) {
                    AutoHoleFill.mc.player.inventory.currentItem = n4;
                }
                ++n6;
                continue;
            }
            if (n6 >= this.bps.getValue()) continue;
            if (hole instanceof HoleUtil.SingleHole && WorldUtils.empty.contains((Object)WorldUtils.getBlock(((HoleUtil.SingleHole)hole).pos)) && !BlockUtil.isIntercepted(((HoleUtil.SingleHole)hole).pos)) {
                if (this.sensitivity.getValue() == Sensitivity.Less) {
                    if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                    } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n2));
                    } else {
                        if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                            return;
                        }
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n3));
                    }
                } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                    if (n != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n;
                    }
                } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                    if (n2 != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n2;
                    }
                } else {
                    if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                        return;
                    }
                    if (n3 != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n3;
                    }
                }
                BlockUtil.placeBlockss(((HoleUtil.SingleHole)hole).pos, false, this.packet.getValue(), this.rotate.getValue());
                if (this.sensitivity.getValue() == Sensitivity.Less) {
                    AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n5));
                } else if (this.sensitivity.getValue() == Sensitivity.Strong && n4 != -1) {
                    AutoHoleFill.mc.player.inventory.currentItem = n4;
                }
                ++n6;
            }
            if (n6 >= this.bps.getValue() || !(hole instanceof HoleUtil.DoubleHole) || !this.doubleHoles.getValue().booleanValue() || n6 >= this.bps.getValue()) continue;
            object = (HoleUtil.DoubleHole)hole;
            if (this.getDist(((HoleUtil.DoubleHole) object).pos) && !BlockUtil.isInterceptedByOther(((HoleUtil.DoubleHole) object).pos) && WorldUtils.empty.contains((Object)WorldUtils.getBlock(((HoleUtil.DoubleHole) object).pos)) && !BlockUtil.isIntercepted(((HoleUtil.DoubleHole) object).pos)) {
                if (this.sensitivity.getValue() == Sensitivity.Less) {
                    if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                    } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n2));
                    } else {
                        if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                            return;
                        }
                        AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n3));
                    }
                } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                    if (n != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n;
                    }
                } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                    if (n2 != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n2;
                    }
                } else {
                    if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                        return;
                    }
                    if (n3 != AutoHoleFill.mc.player.inventory.currentItem) {
                        n4 = AutoHoleFill.mc.player.inventory.currentItem;
                        AutoHoleFill.mc.player.inventory.currentItem = n3;
                    }
                }
                BlockUtil.placeBlockss(((HoleUtil.DoubleHole) object).pos, false, this.packet.getValue(), this.rotate.getValue());
                if (this.sensitivity.getValue() == Sensitivity.Less) {
                    AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n5));
                } else if (this.sensitivity.getValue() == Sensitivity.Strong && n4 != -1) {
                    AutoHoleFill.mc.player.inventory.currentItem = n4;
                }
                ++n6;
            }
            if (n6 >= this.bps.getValue() || !this.getDist(((HoleUtil.DoubleHole) object).pos1) || BlockUtil.isInterceptedByOther(((HoleUtil.DoubleHole) object).pos1) || !WorldUtils.empty.contains((Object)WorldUtils.getBlock(((HoleUtil.DoubleHole) object).pos1)) || BlockUtil.isIntercepted(((HoleUtil.DoubleHole) object).pos1)) continue;
            if (this.sensitivity.getValue() == Sensitivity.Less) {
                if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                    AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n));
                } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                    AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n2));
                } else {
                    if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                        return;
                    }
                    AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n3));
                }
            } else if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                if (n != AutoHoleFill.mc.player.inventory.currentItem) {
                    n4 = AutoHoleFill.mc.player.inventory.currentItem;
                    AutoHoleFill.mc.player.inventory.currentItem = n;
                }
            } else if (InventoryUtil.findHotbarBlock(BlockAnvil.class) != -1) {
                if (n2 != AutoHoleFill.mc.player.inventory.currentItem) {
                    n4 = AutoHoleFill.mc.player.inventory.currentItem;
                    AutoHoleFill.mc.player.inventory.currentItem = n2;
                }
            } else {
                if (InventoryUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                    return;
                }
                if (n3 != AutoHoleFill.mc.player.inventory.currentItem) {
                    n4 = AutoHoleFill.mc.player.inventory.currentItem;
                    AutoHoleFill.mc.player.inventory.currentItem = n3;
                }
            }
            BlockUtil.placeBlockss(((HoleUtil.DoubleHole) object).pos1, false, this.packet.getValue(), this.rotate.getValue());
            if (this.sensitivity.getValue() == Sensitivity.Less) {
                AutoHoleFill.mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(n5));
            } else if (this.sensitivity.getValue() == Sensitivity.Strong && n4 != -1) {
                AutoHoleFill.mc.player.inventory.currentItem = n4;
            }
            ++n6;
        }
        if (n6 != 0 || !arrayList.isEmpty() || !this.toggle.getValue().booleanValue()) {
            return;
        }
        Command.sendMessage("Finished Holefilling, disabling!");
        this.disable();
    }

    @Override
    public void onEnable() {
        if (AutoHoleFill.nullCheck()) {
            return;
        }
        this.offTimer.reset();
    }

    private boolean help(Vec3d vec3d, EntityPlayer entityPlayer, ArrayList arrayList, HoleUtil.Hole hole) {
        if (hole instanceof HoleUtil.SingleHole) {
            return vec3d.squareDistanceTo(new Vec3d((Vec3i)((HoleUtil.SingleHole)hole).pos).add(0.5, 0.5, 0.5)) >= (double)(this.distance.getValue() * this.distance.getValue());
        }
        HoleUtil.Hole hole2 = HoleUtil.getHole(EntityUtil.getEntityPosFloored((Entity)entityPlayer), 1);
        if (hole2 instanceof HoleUtil.DoubleHole && arrayList.contains(hole2)) {
            return true;
        }
        Vec3d vec3d2 = new Vec3d((Vec3i)((HoleUtil.DoubleHole)hole).pos);
        if (vec3d.squareDistanceTo(vec3d2.add(0.5, 0.5, 0.5)) >= (double)(this.distance.getValue() * this.distance.getValue())) {
            return true;
        }
        return vec3d.squareDistanceTo(new Vec3d((Vec3i)((HoleUtil.DoubleHole)hole).pos1).add(0.5, 0.5, 0.5)) >= (double)(this.distance.getValue() * this.distance.getValue());
    }

    private boolean getDist(BlockPos blockPos) {
        if (AutoHoleFill.nullCheck() || blockPos == null) {
            return false;
        }
        return blockPos.add(0.5, 0.5, 0.5).distanceSq(AutoHoleFill.mc.player.posX, AutoHoleFill.mc.player.posY + (double)AutoHoleFill.mc.player.eyeHeight, AutoHoleFill.mc.player.posZ) < Math.pow(this.range.getValue().intValue(), 2.0);
    }

    public AutoHoleFill() {
        super("AutoHoleFill", "Automatically Fills safe holes near the opponent for Crystal PvP", Module.Category.COMBAT, true, false, false);
        this.doubleHoles = this.register(new Setting<Boolean>("Fill Double Holes", true));
        this.sensitivity = this.register(new Setting<Sensitivity>("Sensitivity", Sensitivity.Less));
        this.validHoleHeight = this.register(new Setting<Integer>("Valid Hole Height", 2, 1, 5));
        this.bps = this.register(new Setting<Integer>("Blocks Per Tick", 3, 1, 8));
        this.range = this.register(new Setting<Integer>("Range", 5, 1, 10));
        this.validPlayerRange = this.register(new Setting<Float>("Valid Player Range", Float.valueOf(10.0f), Float.valueOf(0.1f), Float.valueOf(15.0f)));
        this.toggle = this.register(new Setting<Boolean>("Disables", false));
        this.packet = this.register(new Setting<Boolean>("Packet", false));
        this.rotate = this.register(new Setting<Boolean>("Rotate", true));
        this.smart = this.register(new Setting<Boolean>("Smart", true));
        this.distance = this.register(new Setting<Integer>("Smart Range", 2, 1, 5));
        this.predict = this.register(new Setting<Boolean>("Predict", true));
        this.ticks = this.register(new Setting<Integer>("Predict Delay", 3, 1, 5));
        this.disable = this.register(new Setting<Boolean>("Disable", false));
        this.disableTime = this.register(new Setting<Integer>("Ms/Disable", 200, 1, 250));
        this.offTimer = new Timer();
    }

    public static enum Sensitivity {
        Strong,
        Less;

    }
}