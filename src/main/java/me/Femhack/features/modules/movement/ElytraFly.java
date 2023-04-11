package me.Femhack.features.modules.movement;

import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", "Elytrafly exploit", Module.Category.MOVEMENT, true, false,false );
    }

    public Setting<Float> Vspeed = this.register(new Setting<Object>("VerticalSpeed", 1.8f, 0f, 3f));
    public Setting<Float> Hspeed = this.register(new Setting<Object>("HorizontalSpeed", 1.8f, 0f, 3f));

    public static ElytraFly instance;
    public void onUpdate(){
        if (ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            mc.player.capabilities.isFlying = false;
            mc.player.capabilities.allowFlying = false;
        }
        if (ElytraFly.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
            if (mc.player.capabilities.isFlying || mc.player.isElytraFlying())
                mc.player.setSprinting(false);
            if (mc.player.capabilities.isFlying) {
                mc.player.setVelocity(0, 0, 0);
                mc.player.setPosition(mc.player.posX, mc.player.posY - 0.000050000002f, mc.player.posZ);
                if (mc.gameSettings.keyBindSneak.isKeyDown() || mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.capabilities.setFlySpeed(this.Vspeed.getValue());
                } else if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                    mc.player.capabilities.setFlySpeed(this.Hspeed.getValue());
                }
                mc.player.setSprinting(false);
            }

            if (mc.player.onGround) {
                mc.player.capabilities.allowFlying = false;
            }

            if (mc.player.isElytraFlying()) {
                mc.player.capabilities.setFlySpeed(.915f);
                mc.player.capabilities.isFlying = true;

                if (!mc.player.capabilities.isCreativeMode)
                    mc.player.capabilities.allowFlying = true;
            }
        }
    }

    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        if (!mc.player.capabilities.isCreativeMode)
            mc.player.capabilities.allowFlying = false;
    }
}