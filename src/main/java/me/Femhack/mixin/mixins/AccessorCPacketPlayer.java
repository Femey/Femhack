package me.Femhack.mixin.mixins;

import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ CPacketPlayer.class })
public interface AccessorCPacketPlayer
{
    @Accessor("x")
    void setX(final double p0);

    @Accessor("y")
    void setY(final double p0);

    @Accessor("z")
    void setZ(final double p0);

    @Accessor("yaw")
    void setYaw(final float p0);

    @Accessor("pitch")
    void setPitch(final float p0);

    @Accessor("onGround")
    void setOnGround(final boolean p0);
}