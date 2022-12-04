package me.Femhack.util;

import me.Femhack.manager.FileManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;

import javax.annotation.Nullable;

public class Wrapper {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static volatile Wrapper INSTANCE = new Wrapper();

    public static FileManager fileManager;

    @Nullable
    public static EntityPlayerSP getPlayer(){
        return mc.player;
    }

    @Nullable
    public static WorldClient getWorld(){
        return mc.world;
    }

    public static FontRenderer getFontRenderer(){
        return mc.fontRenderer;
    }

    public void sendPacket(Packet packet) {
        this.getPlayer().connection.sendPacket(packet);
    }

    public static FileManager getFileManager() {
        if (Wrapper.fileManager == null) {
            Wrapper.fileManager = new FileManager();
        }
        return Wrapper.fileManager;
    }
}
