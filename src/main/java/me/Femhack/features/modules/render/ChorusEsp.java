package me.Femhack.features.modules.render;

import me.Femhack.event.events.ChorusEvent;
import me.Femhack.event.events.Render3DEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.mixin.mixins.accessors.IRenderManager;
import me.Femhack.util.RenderUtil;
import me.Femhack.util.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ChorusEsp extends Module {

    private final Timer timer;

    public ChorusEsp() {
        super("ChorusEsp", "Shows where people chorus (chorus delay counter)", Category.RENDER, true, false, false);
        timer = new Timer();

    }

    public static ChorusEsp instance;

    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Integer> time = this.register(new Setting<Integer>("Duration", 500, 50, 5000));
    public Setting<Boolean> circle = this.register(new Setting<Boolean>("MovingCircle", false));

    private float x;
    private float y;
    private float z;

    @SubscribeEvent
    public void onChorus(final ChorusEvent event) {
        this.x = (float) event.getChorusX();
        this.y = (float) event.getChorusY();
        this.z = (float) event.getChorusZ();
        this.timer.reset();
    }

    public void onUpdate(){
        prevCircleStep = circleStep;
        circleStep += 0.1f;
    }

    private float prevCircleStep, circleStep;

    public static double absSinAnimation(double input) {
        return Math.abs(1 + Math.sin(input)) / 2;
    }


    @Override
    public void onRender3D(Render3DEvent render3DEvent){
        if (!this.timer.passedMs(this.time.getValue()) && circle.getValue() == false) {
            RenderUtil.drawCircle(this.x, this.y, this.z, 0.4f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
        if (!this.timer.passedMs(this.time.getValue()) && circle.getValue()) {
            double cs = prevCircleStep + (circleStep - prevCircleStep) * mc.getRenderPartialTicks();
            double prevSinAnim = absSinAnimation(cs - 0.15f);
            double sinAnim = absSinAnimation(cs);
            double x = this.x + (this.x - this.x) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
            double y = this.y + (this.y - this.y) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosY() + prevSinAnim * 1.8f;
            double z = this.z + (this.z - this.z) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
            double nextY = this.y + (this.y - this.y) * mc.getRenderPartialTicks() - ((IRenderManager) mc.getRenderManager()).getRenderPosY() + sinAnim * 1.8f;

            GL11.glPushMatrix();

            boolean cullface = GL11.glIsEnabled(GL11.GL_CULL_FACE);
            boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
            boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
            boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);


            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);

            GL11.glShadeModel(GL11.GL_SMOOTH);

            GL11.glBegin(GL11.GL_QUAD_STRIP);
            for (int i = 0; i <= 360; i++) {
                Color clr = new Color(red.getValue(), green.getValue(), blue.getValue());
                GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 0.6F);
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * 0.6 * 0.8, nextY, z + Math.sin(Math.toRadians(i)) * 0.6 * 0.8);

                GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 0.01F);
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * 0.6 * 0.8, y, z + Math.sin(Math.toRadians(i)) * 0.6 * 0.8);
            }

            GL11.glEnd();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            for (int i = 0; i <= 360; i++) {
                Color clr = new Color(red.getValue(), green.getValue(), blue.getValue());
                GL11.glColor4f(clr.getRed() / 255f, clr.getGreen() / 255f, clr.getBlue() / 255f, 1F);
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * 0.6 * 0.8, nextY, z + Math.sin(Math.toRadians(i)) * 0.6 * 0.8);
            }
            GL11.glEnd();

            if (!cullface)
                GL11.glDisable(GL11.GL_LINE_SMOOTH);

            if (texture)
                GL11.glEnable(GL11.GL_TEXTURE_2D);


            if (depth)
                GL11.glEnable(GL11.GL_DEPTH_TEST);

            GL11.glShadeModel(GL11.GL_FLAT);

            if (!blend)
                GL11.glDisable(GL11.GL_BLEND);
            if (cullface)
                GL11.glEnable(GL11.GL_CULL_FACE);
            if (alpha)
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glPopMatrix();
            GlStateManager.resetColor();
        }

    }
}