package me.Femhack.features.gui.components.items.buttons;

import me.Femhack.features.gui.FemhackGui;
import me.Femhack.features.gui.components.Component;
import me.Femhack.features.gui.components.items.Description;
import me.Femhack.features.gui.components.items.Item;
import me.Femhack.features.modules.client.ClickGui;
import me.Femhack.util.ColorUtil;
import me.Femhack.util.Util;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Bind;
import me.Femhack.features.setting.Setting;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton
        extends Button {
    private final Module module;
    private List<Item> items = new ArrayList<Item>();
    private boolean subOpen;
    private float progress;

    public ModuleButton(Module module) {
        super(module.getName());
        this.module = module;
        this.initSettings();
    }

    public void initSettings() {
        ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add(new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add(new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add(new StringButton(setting));
                }
                if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add(new Slider(setting));
                    continue;
                }
                if (!setting.isEnumSetting()) continue;
                newItems.add(new EnumButton(setting));
            }
        }
        newItems.add(new BindButton(this.module.getSettingByName("Keybind")));
        this.items = newItems;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float tempx;
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!this.items.isEmpty()) {
            if (this.subOpen) {
                float height = 1.0f;
                for (Item item : this.items) {
                    Component.counter1[0] = Component.counter1[0] + 1;
                    if (!item.isHidden()) {
                        item.setLocation(this.x + 1.0f, this.y + (height += 15.0f));
                        item.setHeight(15);
                        item.setWidth(this.width - 9);
                        item.drawScreen(mouseX, mouseY, partialTicks);
                    }
                    item.update();
                }
            }
        }
        if (this.getModule(mouseX, mouseY) != null && Description.open) {
            String s = this.getDescription(this.getModule(mouseX, mouseY));
            int color = ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), 255);
            Description.deez(s, color);
        }
        if (ClickGui.getInstance().arrow.getValue()) {
            if (!this.subOpen) {
                tempx = 0;
                progress = 0;
            } else {
                progress = 90;
                tempx = 6.5F;
            }
            //gear bit
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.color(197, 182, 179);
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/arrow.png"));
            GlStateManager.translate(this.x + this.width - 8.5F, this.y - FemhackGui.getClickGui().getTextOffset() - 2, 0.0F);
            GlStateManager.rotate(progress, 0.0F, 0.0F, 1.0F);
            Gui.drawScaledCustomSizeModalRect(-5, (int) (-5 - tempx), 0.0F, 0.0F, 16, 16, 16, 16, 16.0F, 16.0F);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public String getDescription(Module module) {
        return module.getDescription();
    }

    public Module getModule(int mouseX, int mouseY) {
        if (this.isHovering(mouseX, mouseY)) {
            return this.module;
        }
        return null;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
            if (this.subOpen) {
                for (Item item : this.items) {
                    if (item.isHidden()) continue;
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (Item item : this.items) {
                if (item.isHidden()) continue;
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }

    public Module getModule() {
        return this.module;
    }

    @Override
    public void toggle() {
        this.module.toggle();
    }

    @Override
    public boolean getState() {
        return this.module.isEnabled();
    }
}

