package me.Femhack.features.modules.troll;

import me.Femhack.features.command.Command;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;

public class SalhackDupe extends Module {

    public SalhackDupe() {
        super("SalhackDupe", "Perform SalC1 dupe automatically (Press esc to disable).", Module.Category.TROLL, true, false, false);
    }

    private final Setting<Boolean> shulkerOnly = this.register(new Setting<Boolean>("ShulkerOnly", true));
    private final Setting<Boolean> hitGround = this.register(new Setting<Boolean>("Ground", true));
    public Setting<Float> Delay = this.register(new Setting<Float>("Delay", 1f, 0f, 10f));

    private boolean doDrop = false; //When to drop items from entity.
    private boolean doChest = false; //When to add items to entity.
    private boolean doSneak = false; //Do a sneak.
    private boolean start = false; //When to start.
    private boolean finished = false; //When dupe is finished.
    private boolean grounded = false; //When to check if on ground.

    private int itemsToDupe;
    private int itemsMoved;
    private int itemsDropped;

    private GuiScreenHorseInventory l_Chest;
    private final Timer timer = new Timer(); //How long to wait.

    private boolean noBypass = false;

    @Override
    public void onEnable() {

        super.onEnable();

        timer.reset();

        start = true;
    }

    @Override
    public void onDisable() {

        super.onDisable();

        noBypass = false;
        doDrop = false;
        doChest = false;
        doSneak = false;
        start = false;
        finished = false;
        grounded = false;

        itemsToDupe = 0;
        itemsMoved = 0;
        itemsDropped = 0;

        timer.reset();
    }

    public void onUpdate() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) //toggle on escape
        {
            toggle();
            return;
        }

        if(finished) {
            finished = false;
            itemsMoved = 0;
            itemsDropped = 0;
            start = true; //redo dupe
            return;
        }

        if (!timer.passedMs((long) (Delay.getValue() * 100f)))
            return;

        timer.reset();

        if (doSneak) {
            if(!mc.player.isSneaking()) { //if sneak failed
                mc.gameSettings.keyBindSneak.pressed = true;
                return;
            }
            mc.gameSettings.keyBindSneak.pressed = false;  //stop sneaking on new tick
            doSneak = false;
            if(!hitGround.getValue()) {
                finished = true;
            } else {
                grounded = true;
            }
            return;
        }

        if(grounded && mc.player.onGround) { //helps with getting kicked for flying
            grounded = false;
            finished = true;
            return;
        }

        if (start && isEnabled()) {
            itemsToDupe = 0;
            itemsMoved = 0;

            Entity l_Entity = mc.world.loadedEntityList.stream()
                    .filter(this::isValidEntity)
                    .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                    .orElse(null);

            if (l_Entity instanceof AbstractChestHorse) {
                AbstractChestHorse l_entity = (AbstractChestHorse) l_Entity;

                if (!l_entity.hasChest()) {
                    int l_Slot = getChestInHotbar();

                    if (l_Slot != -1 && mc.player.inventory.currentItem != l_Slot) {
                        mc.player.inventory.currentItem = l_Slot;
                        mc.playerController.updateController();
                        mc.playerController.interactWithEntity(mc.player, l_entity, EnumHand.MAIN_HAND);
                    } else if (mc.player.inventory.currentItem != l_Slot) {
                        Command.sendMessage("No chests in hotbar, toggling...");
                        toggle();
                        return;
                    } else { //if chest is already in hand
                        mc.playerController.interactWithEntity(mc.player, l_entity, EnumHand.MAIN_HAND);
                    }

                }

                start = false;
                mc.playerController.interactWithEntity(mc.player, l_entity, EnumHand.MAIN_HAND); //ride entity
                mc.player.sendHorseInventory(); //open inventory
                doChest = true; //start next sequence

            }

        }

        if (doChest && !(mc.currentScreen instanceof GuiScreenHorseInventory)) { //check if we got kicked off entity
            doChest = false;
            start = true;
            return;
        }

        if (mc.currentScreen instanceof GuiScreenHorseInventory) {
            l_Chest = (GuiScreenHorseInventory) mc.currentScreen; //this next part is taken from chest stealer

            itemsToDupe = getItemsToDupe();

            for (int l_I = 2; l_I < l_Chest.horseInventory.getSizeInventory() + 1; ++l_I) {
                ItemStack l_Stack = l_Chest.horseInventory.getStackInSlot(l_I);

                if((itemsToDupe == 0 || itemsMoved == l_Chest.horseInventory.getSizeInventory() - 2) && doChest) { //itemsToDupe is for < donkey inventory slots, itemsMoved is for > donkey inventory slots
                    break; //break to execute code below
                } else if((itemsDropped >= itemsMoved) && doDrop) { //execute code below
                    break;
                }

                if ((l_Stack.isEmpty() || l_Stack.getItem() == Items.AIR) && doChest) {
                    HandleStoring(l_Chest.inventorySlots.windowId, l_Chest.horseInventory.getSizeInventory() - 9);
                    itemsToDupe--;
                    itemsMoved = getItemsInRidingEntity();
                    return;
                } else {
                    if(doChest) { //if items were already in entity inventory
                        continue;
                    }
                }

                if (shulkerOnly.getValue() && !(l_Stack.getItem() instanceof ItemShulkerBox))
                    continue;

                if (l_Stack.isEmpty())
                    continue;

                if (doDrop) {
                    if(canStore()) { //move to inventory first, then drop
                        mc.playerController.windowClick(mc.player.openContainer.windowId, l_I, 0, ClickType.QUICK_MOVE, mc.player);
                    } else {
                        mc.playerController.windowClick(l_Chest.inventorySlots.windowId, l_I, -999, ClickType.THROW, mc.player);
                    }
                    itemsDropped++;
                    return;
                }

            }

            if (doChest) {
                doChest = false;
                doDupe(); //break check
                return;
            }

            if(doDrop) {
                doDrop = false;
                mc.player.closeScreen();
                mc.gameSettings.keyBindSneak.pressed = true; //sending sneak packet messes with your connection
                doSneak = true;
            }

        }

    };


    @Override
    public void onLogout() {
        this.disable();
    }

    private boolean isValidEntity(Entity entity)
    {
        if (entity instanceof AbstractChestHorse) {
            AbstractChestHorse l_ChestHorse = (AbstractChestHorse) entity;

            return !l_ChestHorse.isChild() && l_ChestHorse.isTame();
        }

        return false;
    }

    private int getChestInHotbar()
    {
        for (int i = 0; i < 9; ++i)
        {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock)
            {
                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockChest)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    //Code from Chest Stealer.
    private void HandleStoring(int p_WindowId, int p_Slot)
    {
        for (int l_Y = 9; l_Y < mc.player.inventoryContainer.inventorySlots.size() - 1; ++l_Y)
        {
            ItemStack l_InvStack = mc.player.inventoryContainer.getSlot(l_Y).getStack();

            if (l_InvStack.isEmpty() || l_InvStack.getItem() == Items.AIR)
                continue;

            if (!(l_InvStack.getItem() instanceof ItemShulkerBox) && shulkerOnly.getValue())
                continue;

            mc.playerController.windowClick(p_WindowId, l_Y + p_Slot, 0, ClickType.QUICK_MOVE, mc.player);
            return;
        }

    }

    private void doDupe() {
        noBypass = true; //turn off mount bypass

        Entity l_Entity = mc.world.loadedEntityList.stream() //declaring this variable for the entire class causes NullPointerException
                .filter(this::isValidEntity)
                .min(Comparator.comparing(p_Entity -> mc.player.getDistance(p_Entity)))
                .orElse(null);

        if (l_Entity instanceof AbstractChestHorse) {
            mc.player.connection.sendPacket(new CPacketUseEntity(l_Entity, EnumHand.MAIN_HAND, l_Entity.getPositionVector())); //Packet to break chest.
            noBypass = false; //turn on mount bypass
            doDrop = true;
        }

    }

    private int getItemsToDupe() {
        int i = 0;

        for (int l_Y = 9; l_Y < mc.player.inventoryContainer.inventorySlots.size() - 1; ++l_Y)
        {
            ItemStack l_InvStack = mc.player.inventoryContainer.getSlot(l_Y).getStack();

            if (l_InvStack.isEmpty() || l_InvStack.getItem() == Items.AIR)
                continue;

            if (!(l_InvStack.getItem() instanceof ItemShulkerBox) && shulkerOnly.getValue())
                continue;

            i++;
        }

        if(i > l_Chest.horseInventory.getSizeInventory() - 1)
            i = l_Chest.horseInventory.getSizeInventory() - 1;

        return i;
    }

    private int getItemsInRidingEntity() {
        int i = 0;

        for(int l_I = 2; l_I < l_Chest.horseInventory.getSizeInventory() + 1; ++l_I)
        {
            ItemStack l_ItemStack = l_Chest.horseInventory.getStackInSlot(l_I);

            if(l_ItemStack.isEmpty() || l_ItemStack.getItem() == Items.AIR)
                continue;

            i++;
        }
        return i;
    }

    private boolean canStore() { //check for drop or steal
        for (int l_Y = 9; l_Y < mc.player.inventoryContainer.inventorySlots.size() - 1; ++l_Y)
        {
            ItemStack l_InvStack = mc.player.inventoryContainer.getSlot(l_Y).getStack();

            if (l_InvStack.isEmpty() || l_InvStack.getItem() == Items.AIR)
                return true;
        }
        return false;
    }

    public boolean ignoreMountBypass() { //tell mount bypass when to disable
        return noBypass;
    }


}