package me.Femhack.mixin.mixins;

import com.google.common.base.Predicate;
import me.Femhack.Femhack;
import me.Femhack.features.modules.misc.NoHitBox;
import me.Femhack.features.modules.render.Ambience;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.vecmath.Vector3f;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Mixin(value = {EntityRenderer.class})
public class MixinEntityRenderer {
    @Shadow
    @Final
    private int[] lightmapColors;

    @Redirect(method = {"getMouseOver"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(WorldClient worldClient, Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
        if (NoHitBox.getINSTANCE().isOn() && (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemPickaxe && NoHitBox.getINSTANCE().pickaxe.getValue() != false || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && NoHitBox.getINSTANCE().crystal.getValue() != false || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE && NoHitBox.getINSTANCE().gapple.getValue() != false || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.FLINT_AND_STEEL || Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == Items.TNT_MINECART)) {
            return new ArrayList<Entity>();
        }
        return worldClient.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Inject(method = "updateLightmap", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;updateDynamicTexture()V", shift = At.Shift.BEFORE ))
    private void updateTextureHook(float partialTicks, CallbackInfo ci) {
        Ambience Ambience = Femhack.moduleManager.getModuleByClass(Ambience.class);
        if (Ambience.isEnabled()) {
            for (int i = 0; i < this.lightmapColors.length; ++i) {
                Color ambientColor = new Color(Ambience.r.getValue(), Ambience.g.getValue(), Ambience.b.getValue(), Ambience.a.getValue());
                int alpha = ambientColor.getAlpha();
                float modifier = ( float ) alpha / 255.0f;
                int color = this.lightmapColors[ i ];
                int[] bgr = this.toRGBAArray(color);
                Vector3f values = new Vector3f(( float ) bgr[ 2 ] / 255.0f, ( float ) bgr[ 1 ] / 255.0f, ( float ) bgr[ 0 ] / 255.0f);
                Vector3f newValues = new Vector3f(( float ) ambientColor.getRed() / 255.0f, ( float ) ambientColor.getGreen() / 255.0f, ( float ) ambientColor.getBlue() / 255.0f);
                Vector3f finalValues = mix(values, newValues, modifier);
                int red = ( int ) (finalValues.x * 255.0f);
                int green = ( int ) (finalValues.y * 255.0f);
                int blue = ( int ) (finalValues.z * 255.0f);
                this.lightmapColors[ i ] = 0xFF000000 | red << 16 | green << 8 | blue;
            }
        }
    }

    private int[] toRGBAArray(int colorBuffer) {
        return new int[] { colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF };
    }

    private Vector3f mix(Vector3f first, Vector3f second, float factor) {
        return new Vector3f(first.x * (1.0f - factor) + second.x * factor, first.y * (1.0f - factor) + second.y * factor, first.z * (1.0f - factor) + first.z * factor);
    }
}

