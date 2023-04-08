package me.Femhack.features.gui.components.items;

import me.Femhack.Femhack;
import me.Femhack.features.modules.client.ClickGui;
import me.Femhack.util.ColorUtil;
import me.Femhack.util.RenderUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import sun.security.krb5.internal.crypto.Des;

import java.awt.*;
import java.util.ArrayList;

public class Description
        extends Item {
    public static float descX = 690.0f;
    public static float descY = 20.0f;
    public static float descH = 25.0f;
    public static float descW = 250f;
    private final ArrayList<Item> items = new ArrayList();
    private int x2;
    private int y2;
    public boolean drag;
    public static boolean open;
    public static Description INSTANCE;

    public Description(String name, int x, int y, boolean open) {
        super(name);
        this.x = descX;
        this.y = descY;
        this.height = (int)descH;
        Description.open = open;
    }

    public static void deez(String desc, int color){
        Femhack.textManager.drawStringWithShadow(desc, descX + 3.0f, descY + 7.0f, color);
        Description.descW = Femhack.textManager.getStringWidth(desc) + 6f;
    }

    public void drawScreen(int mouseX, int mouseY) {
        this.drag(mouseX, mouseY);
        int tRed = 0;
        int tBlue = 0;
        int tGreen = 0;
        Color tColor = new Color(tRed, tGreen, tBlue, 119);
        int color2 = ColorUtil.toARGB(ClickGui.getInstance().topRed.getValue(), ClickGui.getInstance().topGreen.getValue(), ClickGui.getInstance().topBlue.getValue(), 255);
        if (open) {
            RenderUtil.drawRectGradient(descX, descY - 4.0f, descW, descH, tColor, tColor, tColor, tColor);
            RenderUtil.drawLine(descX+0.5f, descY - 3, descX+0.5f, descY+descH - 3, 1.5f, color2); //left line
            RenderUtil.drawLine(descX-0.5f + descW, descY - 3 , descX-0.5f + descW, descY+descH - 3, 1.5f, color2); //right line
            RenderUtil.drawLine(descX, descY + descH - 3, descX + descW, descY + descH - 3, 1.5f, color2); //Bottom line
        }
        Gui.drawRect((int)((int)descX), (int)((int)descY - 17), (int)((int)descX + descW), (int)((int)descY - 3), (int)(ClickGui.getInstance().rainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB() : color2));
        Femhack.textManager.drawStringWithShadow("Description", descX + 3.0f, descY - 14.0f, -1);
    }

    public void drag(int mouseX, int mouseY) {
        if (!this.drag) {
            return;
        }
        descX = this.x2 + mouseX;
        descY = this.y2 + mouseY;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = (int)(descX - (float)mouseX);
            this.y2 = (int)(descY - (float)mouseY);
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            open = !open;
            Item.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord((SoundEvent) SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return (float)mouseX >= descX && (float)mouseX <= descX + 250.0f && (float)mouseY <= descY - 3.0f && (float)mouseY >= descY - 17.0f;
    }
}