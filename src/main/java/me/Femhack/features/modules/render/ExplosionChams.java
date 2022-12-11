package me.Femhack.features.modules.render;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.event.events.*;
import me.Femhack.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.lwjgl.opengl.GL11;

public class ExplosionChams
        extends Module {
    private static ExplosionChams INSTANCE = new ExplosionChams();
    private final Setting<Boolean> Rainbow = this.register(new Setting<Object>("Rainbow", true));
    private final Setting<Integer> Red = this.register(new Setting<Object>("Red", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting<Integer> Green = this.register(new Setting<Object>("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255)));
    private final Setting<Integer> Blue = this.register(new Setting<Object>("Blue", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255)));

    public static HashMap<UUID, Thingering> thingers;

    public ExplosionChams() {
        super("ExplosionChams", "ExplosionChams", Category.MISC, true, false, false);
        this.setInstance();
    }

    public static ExplosionChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ExplosionChams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        for (Entity entity : ExplosionChams.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal) || thingers.containsKey(entity.getUniqueID())) continue;
            thingers.put(entity.getUniqueID(), new Thingering(this, entity));
            ExplosionChams.thingers.get((Object)entity.getUniqueID()).starTime = System.currentTimeMillis();
        }
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void onRender3D(Render3DEvent eventRender3D) {
        if (ExplosionChams.mc.player == null || ExplosionChams.mc.world == null) {
            return;
        }
        for (Map.Entry<UUID, Thingering> entry : thingers.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue().starTime >= ((long)180056445 ^ 0xABB74A1L)) continue;
            float opacity = Float.intBitsToFloat(Float.floatToIntBits(1.2886874E38f) ^ 0x7EC1E66F);
            long time = System.currentTimeMillis();
            long duration = time - entry.getValue().starTime;
            if (duration < ((long)-1747803867 ^ 0xFFFFFFFF97D2A4F9L)) {
                opacity = Float.intBitsToFloat(Float.floatToIntBits(13.7902155f) ^ 0x7EDCA4B9) - (float)duration / Float.intBitsToFloat(Float.floatToIntBits(6.1687006E-4f) ^ 0x7E9A3573);
            }
            ExplosionChams.drawCircle(entry.getValue().entity, eventRender3D.getPartialTicks(), Double.longBitsToDouble(Double.doubleToLongBits(205.3116845075892) ^ 0x7F89A9F951C9D87FL), (float)(System.currentTimeMillis() - entry.getValue().starTime) / Float.intBitsToFloat(Float.floatToIntBits(0.025765074f) ^ 0x7E1B1147), opacity);
        }
    }

    public static void drawCircle(final Entity entity, final float partialTicks, final double rad, final float plusY, final float alpha) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(Float.intBitsToFloat(Float.floatToIntBits(0.8191538f) ^ 0x7F11B410));
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - ExplosionChams.mc.getRenderManager().viewerPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - ExplosionChams.mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - ExplosionChams.mc.getRenderManager().viewerPosZ;
        final double pix2 = Double.longBitsToDouble(Double.doubleToLongBits(0.12418750450734782) ^ 0x7FA6EB3BC22A7D2FL);
        for (int i = 0; i <= 90; ++i) {
            if (ExplosionChams.getInstance().Rainbow.getValue()){
                GL11.glColor4f ( (ColorUtil.rainbow(50).getRed() / 255.0f) , (ColorUtil.rainbow(50).getGreen() / 255.0f) , (ColorUtil.rainbow(50).getBlue() / 255.0f) , alpha);
            } else {
                GL11.glColor4f ( (ExplosionChams.getInstance().Red.getValue() / 255.0f) , (ExplosionChams.getInstance().Green.getValue() / 255.0f) , (ExplosionChams.getInstance().Blue.getValue() / 255.0f) , alpha);
            }
            GL11.glVertex3d(x + rad * Math.cos(i * Double.longBitsToDouble(Double.doubleToLongBits(0.038923223119235344) ^ 0x7FBACC45F0F011C7L) / Double.longBitsToDouble(Double.doubleToLongBits(0.010043755046771538) ^ 0x7FC211D1FBA3AC6BL)), y + plusY / Float.intBitsToFloat(Float.floatToIntBits(0.13022153f) ^ 0x7F2558CB), z + rad * Math.sin(i * Double.longBitsToDouble(Double.doubleToLongBits(0.012655047216797511) ^ 0x7F90CB18FB234FBFL) / Double.longBitsToDouble(Double.doubleToLongBits(0.00992417958121009) ^ 0x7FC2D320D5ED6BD3L)));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }


    public static void startSmooth() {
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    public static void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }

    static {
        thingers = new HashMap();
    }

    public class Thingering {
        public Entity entity;
        public long starTime;
        public ExplosionChams this$0;

        public Thingering(ExplosionChams this$0, Entity entity) {
            this.this$0 = this$0;
            this.entity = entity;
            this.starTime = (long)1417733199 ^ 0x5480E44FL;
        }
    }
}