package me.Femhack.features.modules.client;

import me.Femhack.Femhack;
import me.Femhack.event.events.DeathEvent;
import me.Femhack.event.events.Render2DEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.modules.combat.FemboyAura;
import me.Femhack.util.ColorUtil;
import me.Femhack.util.Timer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class KillFeed extends Module {
    public KillFeed() {
        super("KillFeed (W.I.P)", "Shows a kill feed", Category.CLIENT, true, false, false);
    }



    Timer addDelay = new Timer();


    @SubscribeEvent
    public void onPlayerDeath(DeathEvent e){
        if (FemboyAura.getInstance().renderTarget != null && FemboyAura.getInstance().renderTarget == e.player && addDelay.passedMs(1000)){
            stayDelay.reset();
            addDelay.reset();
            alpha = 255;
            name = e.player.getName();
        }
    }

    String name = null;

    int alpha = 255;


    Timer stayDelay = new Timer();

    @Override
    public void onRender2D(Render2DEvent event) {
        if (mc.world == null || alpha == 0 || name == null){
            return;
        }
        int width = this.renderer.scaledWidth;
        int height = this.renderer.scaledHeight;
        this.renderer.drawString(name, (width - 2 - this.renderer.getStringWidth(name)), height/2f,ColorUtil.toRGBA(new Color(255, 255 ,255, alpha)) , false);
        Femhack.textManager.drawString(mc.player.getName(), width - 2 - Femhack.textManager.getStringWidth(name) - Femhack.textManager.getStringWidth(mc.player.getName()) - 23, height/2f, ColorUtil.toRGBA(new Color(255, 255 ,255, alpha)), false);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, alpha / 255F);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/items/end_crystal.png"));
        GlStateManager.translate(width - 22 - Femhack.textManager.getStringWidth(name), height/2f - 4, 0.0F);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0F, 0.0F, 16, 16, 16, 16, 16.0F, 16.0F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public void onUpdate() {
        if (stayDelay.passedMs(8000) && alpha > 0){
            alpha -= 5;
        }
    }
}
