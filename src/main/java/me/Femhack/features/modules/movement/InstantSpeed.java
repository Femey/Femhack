package me.Femhack.features.modules.movement;

import me.Femhack.event.events.MoveEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class InstantSpeed extends Module {
    public InstantSpeed() {
        super("InstantSpeed", "For monkey <3", Category.MOVEMENT, true, false, false);
    }

    @SubscribeEvent
    public void listen(final MoveEvent event) {
        if (this.mc.player.isSneaking() || this.mc.player.isInWater() || this.mc.player.isInLava()) {
            return;
        }
        final MovementInput movementInput = this.mc.player.movementInput;
        float moveForward = movementInput.moveForward;
        float moveStrafe = movementInput.moveStrafe;
        float rotationYaw = this.mc.player.rotationYaw;
        if (moveForward == 0.0 && moveStrafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (moveForward != 0.0) {
                if (moveStrafe > 0.0) {
                    rotationYaw += ((moveForward > 0.0) ? -45 : 45);
                }
                else if (moveStrafe < 0.0) {
                    rotationYaw += ((moveForward > 0.0) ? 45 : -45);
                }
                moveStrafe = 0.0f;
                if (moveForward != 0.0f) {
                    moveForward = ((moveForward > 0.0) ? 1.0f : -1.0f);
                }
            }
            moveStrafe = ((moveStrafe == 0.0f) ? moveStrafe : ((moveStrafe > 0.0) ? 1.0f : -1.0f));
            event.setX(moveForward * this.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * this.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
            event.setZ(moveForward * this.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * this.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
        }
    }

    public double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (this.mc.player.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(1)))) {
            maxModifier *= 1.0 + 0.2 * (Objects.requireNonNull(this.mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
        }
        return maxModifier;
    }
}
