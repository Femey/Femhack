package me.Femhack.features.modules.player;

import me.Femhack.features.modules.Module;
import me.Femhack.util.InventoryUtil;
import net.minecraft.item.ItemExpBottle;

public class FastPlace
        extends Module {
    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class)) {
            FastPlace.mc.rightClickDelayTimer = 0;
        }
    }
}

