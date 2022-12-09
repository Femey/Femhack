package me.Femhack.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.Femhack.Femhack;
import me.Femhack.event.events.ClientEvent;
import me.Femhack.event.events.Render2DEvent;
import me.Femhack.features.command.Command;
import me.Femhack.features.gui.FemhackGui;
import me.Femhack.features.modules.Module;
import me.Femhack.util.Util;
import me.Femhack.features.setting.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
        extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public static ResourceLocation logo = new ResourceLocation("textures/uwu.png");
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", "."));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = this.register(new Setting<Float>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Alpha", 180, 0, 255));
    public Setting<Integer> topRed = this.register(new Setting<Integer>("SecondRed", 255, 0, 255));
    public Setting<Integer> topGreen = this.register(new Setting<Integer>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = this.register(new Setting<Integer>("SecondBlue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = this.register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> this.rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = this.register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowHue = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowBrightness = this.register(new Setting<Object>("Brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<Float> rainbowSaturation = this.register(new Setting<Object>("Saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> this.rainbow.getValue()));
    public Setting<Boolean> imageLogo = this.register(new Setting<Object>("Image", false));
    public Setting<Integer> imageX = this.register(new Setting<Object>("ImageX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Integer> imageY = this.register(new Setting<Object>("ImageY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Integer> imageWidth = this.register(new Setting<Object>("ImageWidth", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Integer> imageHeight = this.register(new Setting<Object>("ImageHeight", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));

    public Setting<Mode> particleMode = this.register(new Setting<Object>("Particle", Mode.Normal));
    public Setting<Integer> pSpeed = this.register(new Setting<Object>("Particle Speed", 1, 15, 8, v -> this.particleMode.getValue() == Mode.Normal));
    public Setting<Float> pRed = this.register(new Setting<Float>("Particle Red", 255f, 0f, 255f, v -> this.particleMode.getValue() == Mode.Normal));
    public Setting<Float> pGreen = this.register(new Setting<Float>("Particle Green", 0f, 0f, 255f, v -> this.particleMode.getValue() == Mode.Normal));
    public Setting<Float> pBlue = this.register(new Setting<Float>("Particle Blue", 255f, 0f, 255f, v -> this.particleMode.getValue() == Mode.Normal));
    public Setting<Float> pAlpha = this.register(new Setting<Float>("Particle Alpha", 255f, 0f, 255f, v -> this.particleMode.getValue() == Mode.Normal));

    private FemhackGui click;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                Femhack.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + Femhack.commandManager.getPrefix());
            }
            Femhack.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        Util.mc.displayGuiScreen(FemhackGui.getClickGui());
    }

    @Override
    public void onLoad() {
        Femhack.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        Femhack.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof FemhackGui)) {
            this.disable();
        }
    }

    public void drawImageLogo() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        mc.getTextureManager().bindTexture(logo);
        FemhackGui.drawCompleteImage(ClickGui.getInstance().imageX.getValue(), ClickGui.getInstance().imageY.getValue(), ClickGui.getInstance().imageWidth.getValue(), ClickGui.getInstance().imageHeight.getValue());
        mc.getTextureManager().deleteTexture(logo);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (ClickGui.fullNullCheck()) {
            return;
        }
        if (this.imageLogo.getValue().booleanValue()) {
            drawImageLogo();
        }
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }

    public enum Mode {
        Normal,
        Snowing,
        None
    }
}

