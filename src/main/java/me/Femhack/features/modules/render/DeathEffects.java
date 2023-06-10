package me.Femhack.features.modules.render;

import me.Femhack.features.modules.Module;
import me.Femhack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class DeathEffects extends Module {

    private static DeathEffects INSTANCE = new DeathEffects();

    public DeathEffects() {
        super("DeathEffects", "BUSSS", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static DeathEffects getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DeathEffects();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    Timer timer = new Timer();

    EntityPlayer person;


    public void onDeath(EntityPlayer player) {
        if (DeathEffects.INSTANCE.isEnabled() && (timer.passedMs(200) || player != person) && player != mc.player) {
            person = player;
            final EntityLightningBolt bolt = new EntityLightningBolt(DeathEffects.mc.world, player.posX, player.posY, player.posZ, true);
            DeathEffects.mc.world.spawnEntity((Entity)bolt);
            final ResourceLocation rl = new ResourceLocation("minecraft", "entity.lightning.thunder");
            final ResourceLocation rl2 = new ResourceLocation("minecraft", "entity.lightning.impact");
            final SoundEvent sound = new SoundEvent(rl);
            final SoundEvent sound2 = new SoundEvent(rl2);
            DeathEffects.mc.world.playSound(DeathEffects.mc.player, new BlockPos(player.posX, player.posY, player.posZ), sound, SoundCategory.WEATHER, Float.intBitsToFloat(Float.floatToIntBits(5.1964397f) ^ 0x7F26493C), Float.intBitsToFloat(Float.floatToIntBits(13.678651f) ^ 0x7EDADBC1));
            DeathEffects.mc.world.playSound(DeathEffects.mc.player, new BlockPos(player.posX, player.posY, player.posZ), sound2, SoundCategory.WEATHER, Float.intBitsToFloat(Float.floatToIntBits(7.520148f) ^ 0x7F70A50D), Float.intBitsToFloat(Float.floatToIntBits(5.243988f) ^ 0x7F27CEC0));
            timer.reset();
        }
    }
}
