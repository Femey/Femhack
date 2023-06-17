package me.Femhack.features.modules.render;

import me.Femhack.event.events.Render3DEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViagraPlayers extends Module {
    public ViagraPlayers() {
        super("ViagraPlayers", "Stiffy people", Category.RENDER, true, false, false);
    }
    public Setting<Boolean> shit = register(new Setting<>("Steven Hawking", true));
    public Setting<Boolean> stiff = register(new Setting<>("Viagra", true));

    @Override
    public void onUpdate() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if(shit.getValue() && player != mc.player){
                player.setSneaking(true);
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(Render3DEvent e) {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (stiff.getValue() && player != mc.player) {
                player.limbSwing = 0;
                player.limbSwingAmount = 0;
            }
        }
    }

}