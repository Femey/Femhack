package me.Femhack.features.modules.troll;

import me.Femhack.features.modules.Module;
import net.minecraft.network.play.client.CPacketChatMessage;


public class Coordleaker extends Module {
    public Coordleaker() {
        super("CoordExploit", "troll 69", Module.Category.TROLL, true, false, false);

    }

    @Override
    public
    void onUpdate(){
        if (fullNullCheck()) return;
        mc.player.connection.sendPacket(new CPacketChatMessage("My coords are: " + Math.floor(mc.player.posX) + ", " + Math.floor(mc.player.posY) + ", " + Math.floor(mc.player.posZ) + "! come and kill me."));

    }
}
