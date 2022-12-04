package me.Femhack.features.modules.misc;

import me.Femhack.features.modules.Module;
import me.Femhack.util.Wrapper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.awt.*;
import java.net.URI;

public class AutoRickRoll
        extends Module {

    public AutoRickRoll() {
        super("AutoRickRoll", "Rickrolls you for the funny", Module.Category.MISC, true, false, false);
    }

    public void onUpdate(){
        if(Wrapper.mc.world == null || Wrapper.mc.player == null){
            this.disable();
        }
    }

    public void onEnable(){
        if(Wrapper.mc.world != null && Wrapper.getPlayer() != null) {
            try {
                Desktop.getDesktop().browse(URI.create("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
            } catch (Exception ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onClientDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.disable();
    }
}
