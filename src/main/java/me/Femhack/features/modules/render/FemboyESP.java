package me.Femhack.features.modules.render;

import me.Femhack.features.command.Command;
import me.Femhack.features.setting.*;
import me.Femhack.util.*;
import me.Femhack.features.modules.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FemboyESP extends Module
{
    private final Setting<CachedImage> imageUrl;
    private final Setting<Boolean> noRenderPlayers = this.register(new Setting<Boolean>("NoRenderPlayers", false));
    private final Setting<Boolean> reload = this.register(new Setting<Boolean>("ReloadTexture", false));
    private ResourceLocation femboy;
    private ICamera camera;
    public static final EventBus EVENT_BUS = MinecraftForge.EVENT_BUS;

    public FemboyESP() {
        super("FemboyEsp", "draws cute femboys on people", Module.Category.RENDER, false, false, false);
        this.imageUrl = this.register(new Setting<CachedImage>("Image", CachedImage.ASTOLFO));
        this.camera = (ICamera)new Frustum();
        this.onLoad();
    }

    @Override
    public void onEnable() {
        FemboyESP.EVENT_BUS.register((Object)this);
    }

    @Override
    public void onDisable() {
        FemboyESP.EVENT_BUS.unregister((Object)this);
    }

    private <T> BufferedImage getImage(final T source, final ThrowingFunction<T, BufferedImage> readFunction) {
        try {
            return readFunction.apply(source);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    @Override
    public void onUpdate() {
        if (this.reload.getValue()) {
            this.femboy = null;
            this.onLoad();
            return;
        }
    }


    private boolean shouldDraw(final EntityLivingBase entity) {
        return !entity.equals((Object) FemboyESP.mc.player) && entity.getHealth() > 0.0f && EntityUtil.isPlayer((Entity)entity);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Text event) {
        if (this.femboy == null) {
            return;
        }
        final double d3 = FemboyESP.mc.player.lastTickPosX + (FemboyESP.mc.player.posX - FemboyESP.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = FemboyESP.mc.player.lastTickPosY + (FemboyESP.mc.player.posY - FemboyESP.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = FemboyESP.mc.player.lastTickPosZ + (FemboyESP.mc.player.posZ - FemboyESP.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        final List<EntityPlayer> players = new ArrayList<EntityPlayer>(FemboyESP.mc.world.playerEntities);
        players.sort(Comparator.comparing(entityPlayer -> FemboyESP.mc.player.getDistance((Entity)entityPlayer)).reversed());
        for (final EntityPlayer player : players) {
            if (player != FemboyESP.mc.getRenderViewEntity() && player.isEntityAlive() && this.camera.isBoundingBoxInFrustum(player.getEntityBoundingBox())) {
                final EntityLivingBase living = (EntityLivingBase)player;
                final Vec3d bottomVec = EntityUtil.getInterpolatedPos((Entity)living, event.getPartialTicks());
                final Vec3d topVec = bottomVec.add(new Vec3d(0.0, player.getRenderBoundingBox().maxY - player.posY, 0.0));
                final VectorUtils.ScreenPos top = VectorUtils._toScreen(topVec.x, topVec.y, topVec.z);
                final VectorUtils.ScreenPos bot = VectorUtils._toScreen(bottomVec.x, bottomVec.y, bottomVec.z);
                if (!top.isVisible && !bot.isVisible) {
                    continue;
                }
                final int height;
                final int width = height = bot.y - top.y;
                final int x = (int)(top.x - width / 1.8);
                final int y = top.y;
                FemboyESP.mc.renderEngine.bindTexture(this.femboy);
                GlStateManager.color(255.0f, 255.0f, 255.0f);
                Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, width, height, width, height, (float)width, (float)height);
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(final RenderPlayerEvent.Pre event) {
        if (this.noRenderPlayers.getValue() && !event.getEntity().equals((Object) FemboyESP.mc.player)) {
            event.setCanceled(true);
        }
    }


    public void onLoad() {
        BufferedImage image = null;
        try {
            if (this.getFile(this.imageUrl.getValue().getName()) != null) {
                image = this.getImage(this.getFile(this.imageUrl.getValue().getName()), ImageIO::read);
            }
            if (image == null) {
                Command.sendMessage("Failed to load image");
            }
            else {
                final DynamicTexture dynamicTexture = new DynamicTexture(image);
                dynamicTexture.loadTexture(FemboyESP.mc.getResourceManager());
                this.femboy = FemboyESP.mc.getTextureManager().getDynamicTextureLocation("Bucket" + this.imageUrl.getValue().name(), dynamicTexture);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream getFile(final String string) {
        return FemboyESP.class.getResourceAsStream(string);
    }

    private enum CachedImage
    {
        ASTOLFO("/images/astolfo.png"),
        ASTOLFO2("/images/astolfo2.png"),
        FELIX("/images/felix.png"),
        FEMBOY("/images/femboy.png");

        String name;

        private CachedImage(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @FunctionalInterface
    private interface ThrowingFunction<T, R>
    {
        R apply(final T p0) throws IOException;
    }



}

