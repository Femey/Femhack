package me.Femhack.features.modules.combat;

import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.InventoryUtil;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;

public class MainHandTotem extends Module {
    public MainHandTotem() {
        super("MainHandTotem", "TheAtmoicPunk kys", Category.COMBAT, false, false, false);
    }

    public Setting<Float> minHealth = this.register(new Setting<Float>("MinHealth", 3.5f, 1f, 36f));
    public Setting<Boolean> autoSwitch = this.register(new Setting<Boolean>("AutoSwitch", true));
    public Setting<Float> minSwitch = this.register(new Setting<Float>("MinSwapHealth", 3.5f, 1f, 36f, v -> this.autoSwitch.getValue() == true));



    @Override
    public void onUpdate() {
        float cum = mc.player.getHealth();
        if (cum < minHealth.getValue()) {
            swapTotem();
        }
        if (autoSwitch.getValue() && cum < minSwitch.getValue()){
            InventoryUtil.switchToHotbarSlot(0, false);
        }
    }



    public void swapTotem() {
        if (MainHandTotem.mc.player.inventory.getStackInSlot(0).getItem().equals(Items.TOTEM_OF_UNDYING)) {
            return;
        }
        final int totem = InventoryUtil.findStackInventory(Items.TOTEM_OF_UNDYING);
        if (totem != -1 && totem != 0) {
            if (MainHandTotem.mc.currentScreen instanceof GuiHopper) {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem - 4, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
            else if (MainHandTotem.mc.currentScreen instanceof GuiDispenser) {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
            else if (MainHandTotem.mc.currentScreen != null && !(MainHandTotem.mc.currentScreen instanceof GuiInventory)) {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
            else {
                MainHandTotem.mc.playerController.windowClick(MainHandTotem.mc.player.inventoryContainer.windowId, totem, 0, ClickType.SWAP, (EntityPlayer)MainHandTotem.mc.player);
            }
        }
    }
}
