package me.Femhack.mixin.mixins.accessors;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = {EntityRenderer.class})
public interface IEntityRenderer {
    @Invoker(value = "orientCamera")
    void orientCam(float var1);

    @Invoker(value = "setupCameraTransform")
    void invokeSetupCameraTransform(float var1, int var2);

    @Accessor("rendererUpdateCount")
    int getRendererUpdateCount();

    @Accessor("rainXCoords")
    float[] getRainXCoords();

    @Accessor("rainYCoords")
    float[] getRainYCoords();
}
