package me.Femhack.features.modules.movement;

import me.Femhack.Femhack;
import me.Femhack.features.command.Command;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.EntityUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class AutoWasp extends Module {
    public AutoWasp() {
        super("AutoWasp", "Auto wasps people", Category.MOVEMENT,true, false, false);
    }

    Setting<Float> range = register(new Setting<>("Range",100f, 0f, 200f));

    @Override
    public void onTick() {
        if (mc.player.isElytraFlying() || mc.player.capabilities.isFlying) {
            EntityPlayer entityPlayer = (EntityPlayer) EntityUtil.getTarget(true, false, false, false, false, this.range.getValue().floatValue(), EntityUtil.toMode("Closest"));
            if (entityPlayer == null) {
                return;
            }

            if (!entityPlayer.isElytraFlying()) {
                Command.sendMessage("Target isnt elytra flying");
                return;
            }

            Femhack.rotationManager.lookAtEntity(entityPlayer);
            if (entityPlayer.posX + 1 < mc.player.posX || entityPlayer.posZ + 1 < mc.player.posZ || entityPlayer.posX - 1 > mc.player.posX || entityPlayer.posZ - 1 > mc.player.posZ){
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
            }

            if (entityPlayer.posY - 2 > mc.player.posY){
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
            } else if (entityPlayer.posY + 2 < mc.player.posY) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
            }
        }
    }

    @Override
    public void onEnable(){
        Command.sendMessage("Make sure you are using creative elytrafly");
    }

    @Override
    public void onDisable(){
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
    }
}
