package me.Femhack.features.modules.misc;

import me.Femhack.event.events.PacketEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OpenPacketCanceller extends Module {
    public OpenPacketCanceller() {
        super("OpenPacketCanceller", "Cancels the packets for opening storage blocks", Category.MISC, true, false, false);
    }

    Setting<Boolean> ender = register(new Setting<>("Ender Chests", false));
    Setting<Boolean> chest = register(new Setting<>("Chests", false));
    Setting<Boolean> trapped = register(new Setting<>("Trapped Chests", false));
    Setting<Boolean> dispenser = register(new Setting<>("Dispenser", false));
    Setting<Boolean> dropper = register(new Setting<>("Dropper", false));
    Setting<Boolean> anvil = register(new Setting<>("Anvils", false));

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send send) {
        CPacketPlayerTryUseItemOnBlock tryUseItemOnBlock;

        if (!(send.getPacket() instanceof CPacketPlayerTryUseItemOnBlock)) return;

        if (ender.getValue() && (OpenPacketCanceller.mc.world.getBlockState((tryUseItemOnBlock = send.getPacket()).getPos()).getBlock() == Blocks.ENDER_CHEST))
            send.setCanceled(true);

        if (chest.getValue() && (OpenPacketCanceller.mc.world.getBlockState((tryUseItemOnBlock = send.getPacket()).getPos()).getBlock() == Blocks.CHEST))
            send.setCanceled(true);

        if (trapped.getValue() && (OpenPacketCanceller.mc.world.getBlockState((tryUseItemOnBlock = send.getPacket()).getPos()).getBlock() == Blocks.TRAPPED_CHEST))
            send.setCanceled(true);

        if (dispenser.getValue() && (OpenPacketCanceller.mc.world.getBlockState((tryUseItemOnBlock = send.getPacket()).getPos()).getBlock() == Blocks.DISPENSER))
            send.setCanceled(true);

        if (dropper.getValue() && (OpenPacketCanceller.mc.world.getBlockState((tryUseItemOnBlock = send.getPacket()).getPos()).getBlock() == Blocks.DROPPER))
            send.setCanceled(true);

        if (anvil.getValue() && (OpenPacketCanceller.mc.world.getBlockState((tryUseItemOnBlock = send.getPacket()).getPos()).getBlock() == Blocks.ANVIL))
            send.setCanceled(true);

    }
}