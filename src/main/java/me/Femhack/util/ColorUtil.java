package me.Femhack.util;

import me.Femhack.features.modules.client.ClickGui;

import java.awt.*;

public class ColorUtil {
    public static int toARGB(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int toRGBA(int r, int g, int b) {
        return ColorUtil.toRGBA(r, g, b, 255);
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static int toRGBA(float r, float g, float b, float a) {
        return ColorUtil.toRGBA((int) (r * 255.0f), (int) (g * 255.0f), (int) (b * 255.0f), (int) (a * 255.0f));
    }

    public static Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState %= 360.0) / 360.0), ClickGui.getInstance().rainbowSaturation.getValue().floatValue() / 255.0f, ClickGui.getInstance().rainbowBrightness.getValue().floatValue() / 255.0f);
    }

    public static Color invert(Color color) {
        int n;
        int n2;
        int n3 = color.getAlpha();
        int n4 = 255 - color.getRed();
        if (n4 + (n2 = 255 - color.getGreen()) + (n = 255 - color.getBlue()) > 740 || n4 + n2 + n < 20) {
            return new Color(255, 255, 40, n3);
        }
        return new Color(n4, n2, n, n3);
    }

    public static int toRGBA(float[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA(colors[0], colors[1], colors[2], colors[3]);
    }

    public static int toRGBA(double[] colors) {
        if (colors.length != 4) {
            throw new IllegalArgumentException("colors[] must have a length of 4!");
        }
        return ColorUtil.toRGBA((float) colors[0], (float) colors[1], (float) colors[2], (float) colors[3]);
    }

    public static int toRGBA(Color color) {
        return ColorUtil.toRGBA(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}

