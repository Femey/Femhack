package me.Femhack.features.modules.troll;

import me.Femhack.features.modules.Module;
import net.minecraft.network.play.client.CPacketChatMessage;


public class Suicide extends Module {
    public Suicide() {
        super("Suicide", "for lazy ppl who cant type /kill", Module.Category.TROLL, true, false, false);

    }

    @Override
    public
    void onEnable() {
        if (fullNullCheck()) return;
                mc.player.connection.sendPacket(new CPacketChatMessage("/kill"));
    }
}

