package me.Femhack.event.events;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EventSchematicaPlaceBlockFull extends EventSchematicaPlaceBlock
{
    public boolean Result = true;
    public Item ItemStack;

    public EventSchematicaPlaceBlockFull(BlockPos p_Pos, Item itemStack)
    {
        super(p_Pos);
        itemStack = ItemStack;
    }

    public boolean GetResult()
    {
        return Result;
    }
}
