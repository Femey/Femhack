package me.Femhack.features.modules.troll;

import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;

public class AutoTolon
        extends Module {
    private static AutoTolon INSTANCE = new AutoTolon();
    public final Setting<Integer> timeout = this.register(new Setting<Integer>("Timeout", 5, 1, 10));
    public final Setting<Boolean> disablee = this.register(new Setting<Boolean>("Disable", true));
    public int disableThingy;

    public AutoTolon() {
        super("AutoTolon", "Tolon moment", Category.TROLL, true, false, false);
        this.setInstance();
    }

    public static AutoTolon getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoTolon();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (AutoTolon.nullCheck()) {
            return;
        }
        if (AutoTolon.INSTANCE.movingByKeys()) {
            this.disable();
            return;
        }
        if (AutoTolon.mc.world.getCollisionBoxes(AutoTolon.mc.player, AutoTolon.mc.player.getEntityBoundingBox().grow(0.01, 0, 0.01)).size() < 2) {
            AutoTolon.mc.player.setPosition(AutoTolon.roundToClosest(AutoTolon.mc.player.posX, Math.floor(AutoTolon.mc.player.posX) + 0.301, Math.floor(AutoTolon.mc.player.posX) + 0.699), AutoTolon.mc.player.posY, AutoTolon.roundToClosest(AutoTolon.mc.player.posZ, Math.floor(AutoTolon.mc.player.posZ) + 0.301, Math.floor(AutoTolon.mc.player.posZ) + 0.699));
        } else if (AutoTolon.mc.player.ticksExisted % this.timeout.getValue().intValue() == 0) {
            AutoTolon.mc.player.setPosition(AutoTolon.mc.player.posX + MathHelper.clamp(AutoTolon.roundToClosest(AutoTolon.mc.player.posX, Math.floor(AutoTolon.mc.player.posX) + 0.241, Math.floor(AutoTolon.mc.player.posX) + 0.759) - AutoTolon.mc.player.posX, -0.03, 0.03), AutoTolon.mc.player.posY, AutoTolon.mc.player.posZ + MathHelper.clamp(AutoTolon.roundToClosest(AutoTolon.mc.player.posZ, Math.floor(AutoTolon.mc.player.posZ) + 0.241, Math.floor(AutoTolon.mc.player.posZ) + 0.759) - AutoTolon.mc.player.posZ, -0.03, 0.03));
            AutoTolon.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(AutoTolon.mc.player.posX, AutoTolon.mc.player.posY, AutoTolon.mc.player.posZ, true));
            AutoTolon.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(AutoTolon.roundToClosest(AutoTolon.mc.player.posX, Math.floor(AutoTolon.mc.player.posX) + 0.23, Math.floor(AutoTolon.mc.player.posX) + 0.77), AutoTolon.mc.player.posY, AutoTolon.roundToClosest(AutoTolon.mc.player.posZ, Math.floor(AutoTolon.mc.player.posZ) + 0.23, Math.floor(AutoTolon.mc.player.posZ) + 0.77), true));
            if (this.disablee.getValue().booleanValue()) {
                disableThingy++;
            } else {
                disableThingy = 0;
            }
        }
        if (disableThingy >= 2 && this.disablee.getValue().booleanValue()) {
            disableThingy = 0;
            this.disable();
        }
    }

    private boolean movingByKeys() {
        return AutoTolon.mc.gameSettings.keyBindForward.isKeyDown() || AutoTolon.mc.gameSettings.keyBindBack.isKeyDown() || AutoTolon.mc.gameSettings.keyBindLeft.isKeyDown() || AutoTolon.mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static double roundToClosest(double num, double low, double high) {
        double d1 = num - low;
        double d2 = high - num;
        if (d2 > d1) {
            return low;
        } else {
            return high;
        }
    }
}