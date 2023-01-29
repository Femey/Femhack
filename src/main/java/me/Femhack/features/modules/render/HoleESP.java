package me.Femhack.features.modules.render;

import java.awt.Color;
import me.Femhack.event.events.Render3DEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.modules.client.ClickGui;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;

public class HoleESP
        extends Module {
    public /* synthetic */ Setting<Boolean> outline;
    public /* synthetic */ Setting<Boolean> renderOwn;
    private /* synthetic */ Setting<Integer> DcRed;
    public /* synthetic */ Setting<Boolean> doublehole;
    private final /* synthetic */ Setting<Integer> rangeY;
    private /* synthetic */ Setting<Integer> safeAlpha;
    private /* synthetic */ Setting<Integer> red;
    private /* synthetic */ Setting<Integer> safecAlpha;
    public /* synthetic */ Setting<Boolean> invertGradientBox;
    private /* synthetic */ Setting<Integer> doublealpha;
    private /* synthetic */ Setting<Integer> DcBlue;
    private /* synthetic */ Setting<Integer> cAlpha;
    private /* synthetic */ Setting<Integer> safeGreen;
    public /* synthetic */ Setting<Boolean> invertGradientOutline;
    private /* synthetic */ Setting<Integer> safecBlue;
    private /* synthetic */ int currentAlpha;
    public /* synthetic */ Setting<Double> height;
    private /* synthetic */ Setting<Integer> safecGreen;
    private /* synthetic */ Setting<Integer> safecRed;
    private /* synthetic */ Setting<Integer> cBlue;
    private static /* synthetic */ HoleESP INSTANCE;
    private /* synthetic */ Setting<Integer> alpha;
    private /* synthetic */ Setting<Integer> DcGreen;
    public /* synthetic */ Setting<Boolean> safeColor;
    public /* synthetic */ Setting<Boolean> fov;
    private /* synthetic */ Setting<Integer> safeBlue;
    private /* synthetic */ Setting<Integer> blue;
    private /* synthetic */ Setting<Integer> green;
    public /* synthetic */ Setting<Boolean> rainbow;
    public /* synthetic */ Setting<Boolean> gradientOutline;
    public /* synthetic */ Setting<Boolean> box;
    private /* synthetic */ Setting<Float> lineWidth;
    private /* synthetic */ Setting<Integer> doubleblue;
    private /* synthetic */ Setting<Integer> cRed;
    private /* synthetic */ Setting<Integer> safeRed;
    private /* synthetic */ Setting<Integer> boxAlpha;
    public /* synthetic */ Setting<Double> Dheight;
    private final /* synthetic */ Setting<Integer> range;
    private /* synthetic */ Setting<Integer> doublered;
    public /* synthetic */ Setting<Boolean> customOutline;
    private /* synthetic */ Setting<Integer> cGreen;
    private /* synthetic */ Setting<Integer> DcAlpha;
    private /* synthetic */ Setting<Integer> doublegreen;
    public /* synthetic */ Setting<Boolean> gradientBox;

    private void setInstance() {
        INSTANCE = this;
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    static {
        INSTANCE = new HoleESP();
    }

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Module.Category.RENDER, false, false, false);
        this.range = this.register(new Setting<Integer>("RangeX", 10, 0, 10));
        this.rangeY = this.register(new Setting<Integer>("RangeY", 6, 0, 10));
        this.renderOwn = this.register(new Setting<Boolean>("RenderOwn", true));
        this.fov = this.register(new Setting<Boolean>("InFov", false));
        this.box = this.register(new Setting<Boolean>("Box", true));
        this.doublehole = this.register(new Setting<Boolean>("Double Hole", true));
        this.rainbow = this.register(new Setting<Boolean>("Rainbow", false));
        this.gradientBox = this.register(new Setting<Object>("Gradient", Boolean.valueOf(false), object -> this.box.getValue()));
        this.invertGradientBox = this.register(new Setting<Object>("ReverseGradient", Boolean.FALSE, object -> this.gradientBox.getValue()));
        this.outline = this.register(new Setting<Boolean>("Outline", true));
        this.gradientOutline = this.register(new Setting<Object>("GradientOutline", Boolean.FALSE, object -> this.outline.getValue()));
        this.invertGradientOutline = this.register(new Setting<Object>("ReverseOutline", Boolean.FALSE, object -> this.gradientOutline.getValue()));
        this.height = this.register(new Setting<Double>("Height", 0.0, -2.0, 2.0));
        this.Dheight = this.register(new Setting<Double>("DoubleHole Height", -1.0, -2.0, 2.0));
        this.red = this.register(new Setting<Integer>("Red", 255, 0, 255));
        this.green = this.register(new Setting<Integer>("Green", 12, 0, 255));
        this.blue = this.register(new Setting<Integer>("Blue", 51, 0, 255));
        this.alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
        this.doublered = this.register(new Setting<Integer>("DoubleHoleRed", 51, 0, 255));
        this.doublegreen = this.register(new Setting<Integer>("DoubleHoleGreen", 12, 0, 255));
        this.doubleblue = this.register(new Setting<Integer>("DoubleHoleBlue", 168, 0, 255));
        this.doublealpha = this.register(new Setting<Integer>("DoubleHoleAlpha", 255, 0, 255));
        this.boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(70), Integer.valueOf(0), Integer.valueOf(255), object -> this.box.getValue()));
        this.lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), object -> this.outline.getValue()));
        this.safeColor = this.register(new Setting<Boolean>("BedrockColor", true));
        this.safeRed = this.register(new Setting<Object>("BedrockRed", Integer.valueOf(43), Integer.valueOf(0), Integer.valueOf(255), object -> this.safeColor.getValue()));
        this.safeGreen = this.register(new Setting<Object>("BedrockGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.safeColor.getValue()));
        this.safeBlue = this.register(new Setting<Object>("BedrockBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.safeColor.getValue()));
        this.safeAlpha = this.register(new Setting<Object>("BedrockAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.safeColor.getValue()));
        this.customOutline = this.register(new Setting<Object>("CustomLine", Boolean.FALSE, object -> this.outline.getValue()));
        this.cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.DcRed = this.register(new Setting<Object>("OL-DoubleRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.DcGreen = this.register(new Setting<Object>("OL-DoubleGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.DcBlue = this.register(new Setting<Object>("OL-DoubleBlue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.DcAlpha = this.register(new Setting<Object>("OL-DoubleAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false));
        this.safecRed = this.register(new Setting<Object>("OL-SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.safeColor.getValue() != false));
        this.safecGreen = this.register(new Setting<Object>("OL-SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.safeColor.getValue() != false));
        this.safecBlue = this.register(new Setting<Object>("OL-SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.safeColor.getValue() != false));
        this.safecAlpha = this.register(new Setting<Object>("OL-SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), object -> this.customOutline.getValue() != false && this.outline.getValue() != false && this.safeColor.getValue() != false));
        this.currentAlpha = 0;
        this.setInstance();
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent) {
        assert (HoleESP.mc.renderViewEntity != null);
        Vec3i vec3i = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int i = vec3i.getX() - this.range.getValue(); i < vec3i.getX() + this.range.getValue(); ++i) {
            for (int j = vec3i.getZ() - this.range.getValue(); j < vec3i.getZ() + this.range.getValue(); ++j) {
                for (int k = vec3i.getY() + this.rangeY.getValue(); k > vec3i.getY() - this.rangeY.getValue(); --k) {
                    BlockPos blockPos = new BlockPos(i, k, j);
                    if (this.doublehole.getValue().booleanValue()) {
                        if (HoleUtilStay.is2securityHole(blockPos) && (HoleESP.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ())).getBlock() == Blocks.AIR || HoleESP.mc.world.getBlockState(new BlockPos(HoleUtilStay.is2Hole(blockPos).getX(), HoleUtilStay.is2Hole(blockPos).getY() + 1, HoleUtilStay.is2Hole(blockPos).getZ())).getBlock() == Blocks.AIR) && HoleESP.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ())).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(new BlockPos(HoleUtilStay.is2Hole(blockPos).getX(), HoleUtilStay.is2Hole(blockPos).getY() - 1, HoleUtilStay.is2Hole(blockPos).getZ())).getBlock() == Blocks.BEDROCK) {
                            RenderUtil.drawBoxESP(blockPos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.doublered.getValue(), this.doublegreen.getValue(), this.doubleblue.getValue(), this.doublealpha.getValue()), this.customOutline.getValue(), new Color(this.DcRed.getValue(), this.DcGreen.getValue(), this.DcBlue.getValue(), this.DcAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.Dheight.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            RenderUtil.drawBoxESP(HoleUtilStay.is2Hole(blockPos), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.doublered.getValue(), this.doublegreen.getValue(), this.doubleblue.getValue(), this.doublealpha.getValue()), this.customOutline.getValue(), new Color(this.DcRed.getValue(), this.DcGreen.getValue(), this.DcBlue.getValue(), this.DcAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.Dheight.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            continue;
                        }
                        if (!(HoleUtilStay.is2Hole(blockPos) == null || HoleESP.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ())).getBlock() != Blocks.AIR && HoleESP.mc.world.getBlockState(new BlockPos(HoleUtilStay.is2Hole(blockPos).getX(), HoleUtilStay.is2Hole(blockPos).getY() + 1, HoleUtilStay.is2Hole(blockPos).getZ())).getBlock() != Blocks.AIR || HoleESP.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ())).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY() - 1, blockPos.getZ())).getBlock() != Blocks.OBSIDIAN || HoleESP.mc.world.getBlockState(new BlockPos(HoleUtilStay.is2Hole(blockPos).getX(), HoleUtilStay.is2Hole(blockPos).getY() - 1, HoleUtilStay.is2Hole(blockPos).getZ())).getBlock() != Blocks.BEDROCK && HoleESP.mc.world.getBlockState(new BlockPos(HoleUtilStay.is2Hole(blockPos).getX(), HoleUtilStay.is2Hole(blockPos).getY() - 1, HoleUtilStay.is2Hole(blockPos).getZ())).getBlock() != Blocks.OBSIDIAN)) {
                            RenderUtil.drawBoxESP(blockPos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.doublered.getValue(), this.doublegreen.getValue(), this.doubleblue.getValue(), this.doublealpha.getValue()), this.customOutline.getValue(), new Color(this.DcRed.getValue(), this.DcGreen.getValue(), this.DcBlue.getValue(), this.DcAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.Dheight.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            RenderUtil.drawBoxESP(HoleUtilStay.is2Hole(blockPos), this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.doublered.getValue(), this.doublegreen.getValue(), this.doubleblue.getValue(), this.doublealpha.getValue()), this.customOutline.getValue(), new Color(this.DcRed.getValue(), this.DcGreen.getValue(), this.DcBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.Dheight.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                            continue;
                        }
                    }
                    if (!HoleESP.mc.world.getBlockState(blockPos).getBlock().equals((Object)Blocks.AIR) || !HoleESP.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals((Object)Blocks.AIR) || !HoleESP.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals((Object)Blocks.AIR) || blockPos.equals((Object)new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !this.renderOwn.getValue().booleanValue() || !BlockUtil.isPosInFov(blockPos).booleanValue() && this.fov.getValue().booleanValue()) continue;
                    if (HoleESP.mc.world.getBlockState(blockPos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(blockPos.down()).getBlock() == Blocks.BEDROCK) {
                        RenderUtil.drawBoxESP(blockPos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.safeRed.getValue(), this.safeGreen.getValue(), this.safeBlue.getValue(), this.safeAlpha.getValue()), this.customOutline.getValue(), new Color(this.safecRed.getValue(), this.safecGreen.getValue(), this.safecBlue.getValue(), this.safecAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                        continue;
                    }
                    if (!BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(blockPos.down()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(blockPos.east()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(blockPos.west()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(blockPos.south()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(blockPos.north()).getBlock())) continue;
                    RenderUtil.drawBoxESP(blockPos, this.rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), true, this.height.getValue(), this.gradientBox.getValue(), this.gradientOutline.getValue(), this.invertGradientBox.getValue(), this.invertGradientOutline.getValue(), this.currentAlpha);
                }
            }
        }
    }
}