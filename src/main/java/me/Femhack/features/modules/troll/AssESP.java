package me.Femhack.features.modules.troll;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.Femhack.event.events.Render3DEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.modules.combat.AutoCrystal;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Sphere;

public
class AssESP
        extends Module {
    public AssESP( ) {
        super ( "AssEsp" , "Want to have gay sex with your friend? Your solution is here!" , Module.Category.TROLL , true , false , false );
    }
    private final Setting<Boolean> Rainbow = this.register(new Setting<Object>("Rainbow", true));
    private final Setting<Integer> Red = this.register(new Setting<Object>("Red", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), v -> this.Rainbow.getValue() != true));
    private final Setting<Integer> Green = this.register(new Setting<Object>("Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.Rainbow.getValue() != true));
    private final Setting<Integer> Blue = this.register(new Setting<Object>("Blue", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), v -> this.Rainbow.getValue() != true));
    private final Setting<Integer> Alpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(40), Integer.valueOf(0), Integer.valueOf(255), v -> this.Rainbow.getValue() != true));

    @Override
    public
    void onRender3D ( Render3DEvent render3DEvent ) {
        for (Object e : mc.world.loadedEntityList) {
            if ( ! ( e instanceof EntityPlayer ) ) continue;
            RenderManager renderManager = Minecraft.getMinecraft ( ).getRenderManager ( );
            EntityPlayer entityPlayer = (EntityPlayer) e;
            double d = entityPlayer.lastTickPosX + ( entityPlayer.posX - entityPlayer.lastTickPosX ) * (double) mc.timer.renderPartialTicks;
            double d2 = d - renderManager.renderPosX;
            double d3 = entityPlayer.lastTickPosY + ( entityPlayer.posY - entityPlayer.lastTickPosY ) * (double) mc.timer.renderPartialTicks;
            double d4 = d3 - renderManager.renderPosY;
            double d5 = entityPlayer.lastTickPosZ + ( entityPlayer.posZ - entityPlayer.lastTickPosZ ) * (double) mc.timer.renderPartialTicks;
            double d6 = d5 - renderManager.renderPosZ;
            GL11.glPushMatrix ( );
            RenderHelper.disableStandardItemLighting ( );
            this.esp ( entityPlayer , d2 , d4 , d6 );
            RenderHelper.enableStandardItemLighting ( );
            GL11.glPopMatrix ( );
        }
    }

    public
    void esp ( EntityPlayer entityPlayer , double d , double d2 , double d3 ) {
        GL11.glDisable ( 2896 );
        GL11.glDisable ( 3553 );
        GL11.glEnable ( 3042 );
        GL11.glBlendFunc ( 770 , 771 );
        GL11.glDisable ( 2929 );
        GL11.glEnable ( 2848 );
        GL11.glDepthMask ( true );
        GL11.glLineWidth ( 1.0f );
        GL11.glTranslated ( d , d2 , d3 );
        GL11.glRotatef ( - entityPlayer.rotationYaw , 0.0f , entityPlayer.height , 0.0f );
        GL11.glTranslated ( - d , - d2 , - d3 );
        GL11.glTranslated ( d , d2 + (double) ( entityPlayer.height / 2.0f ) - (double) 0.225f , d3 );
        if (Rainbow.getValue()){
            GL11.glColor4f ( (ColorUtil.rainbow(50).getRed() / 255.0f) , (ColorUtil.rainbow(50).getGreen() / 255.0f) , (ColorUtil.rainbow(50).getBlue() / 255.0f) , 1.0f );
        } else {
            GL11.glColor4f ( (Red.getValue() / 255.0f) , (Green.getValue() / 255.0f) , (Blue.getValue() / 255.0f) , Alpha.getValue() / 255.0f);
        }
        GL11.glRotated ( ( entityPlayer.isSneaking ( ) ? 35 : 0) , 1.0f, 0.0, 0);
        Sphere sphere = new Sphere ( );
        GL11.glTranslated ( -0.15 , 0.0 , -0.2);
        sphere.setDrawStyle ( 100013 );
        sphere.draw ( 0.20f , 10 , 20 );
        GL11.glTranslated ( 0.35000000149011612 , 0.0 , 0.0 );
        Sphere sphere3 = new Sphere ( );
        sphere3.setDrawStyle ( 100013 );
        sphere3.draw ( 0.20f , 15 , 20 );
        GL11.glDepthMask ( true );
        GL11.glDisable ( 2848 );
        GL11.glEnable ( 2929 );
        GL11.glDisable ( 3042 );
        GL11.glEnable ( 2896 );
        GL11.glEnable ( 3553 );
    }
}