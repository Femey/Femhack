package me.Femhack.features.modules.movement;

import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;

public class ElytraFly extends Module {
    public ElytraFly() {
        super("ElytraFly", "Elytrafly exploit", Module.Category.MOVEMENT, true, false,false );
    }

    public Setting<Float> speed = this.register(new Setting<Object>("Speed", 1.8f, 0f, 3f, "ElytraFlySpeed"));

    public void onUpdate(){
        if(mc.player.capabilities.isFlying || mc.player.isElytraFlying())
            mc.player.setSprinting(false);
        if (mc.player.capabilities.isFlying) {
            mc.player.setVelocity(0, 0, 0);
            mc.player.setPosition(mc.player.posX, mc.player.posY - 0.000050000002f, mc.player.posZ);
            mc.player.capabilities.setFlySpeed(this.speed.getValue());
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

    public void onDisable() {
        mc.player.capabilities.isFlying = false;
        mc.player.capabilities.setFlySpeed(0.05f);
        if (!mc.player.capabilities.isCreativeMode)
            mc.player.capabilities.allowFlying = false;
    }
}