package me.Femhack.features.modules.misc;

import io.netty.buffer.ByteBuf;
import me.Femhack.Femhack;
import me.Femhack.features.modules.Module;
import me.Femhack.features.modules.misc.truedura.CustomLayerBipedArmor;
import me.Femhack.features.modules.misc.truedura.CustomLayerElytra;
import me.Femhack.features.modules.misc.truedura.CustomRenderItem;
import me.Femhack.features.modules.misc.truedura.SpecialTagCompound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TrueDura extends Module {

    private Field playerRendererLayersField;
    private Field renderItemField;
    private Field itemRendererField;

    private RenderItem renderItemSave;
    private EntityRenderer entityRendererSave;

    
    public TrueDura() {
        super("TrueDura", "Display unbreakable items in red enchant, add real durability in tooltip", Category.MISC, true, false, false);

        this.renderItemSave = null;
        this.entityRendererSave = null;

        // Get access to the layers list from the player renderer
        Class<RenderLivingBase> renderLivingClass = RenderLivingBase.class;
        try {
            this.playerRendererLayersField = renderLivingClass.getDeclaredField("field_177097_h");
        } catch (NoSuchFieldException e) {
            try {
                this.playerRendererLayersField = renderLivingClass.getDeclaredField("layerRenderers");
            } catch (NoSuchFieldException e2) {
                throw new RuntimeException("FamilyFunPack error: no such field " + e2.getMessage() + " in class RenderLivingBase");
            }
        }
        this.playerRendererLayersField.setAccessible(true);

        // Get access to the item renderer fields
        Class<Minecraft> mcClass = Minecraft.class;
        try {
            try {
                this.renderItemField = mcClass.getDeclaredField("field_175621_X");
            } catch (NoSuchFieldException e) {
                this.renderItemField = mcClass.getDeclaredField("renderItem");
            }
            this.renderItemField.setAccessible(true);
            try {
                this.itemRendererField = mcClass.getDeclaredField("field_175620_Y");
            } catch (NoSuchFieldException e) {
                this.itemRendererField = mcClass.getDeclaredField("itemRenderer");
            }
            this.itemRendererField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("FamilyFunPack error: no such field " + e.getMessage() + " in class Minecraft");
        }
    }

    public void onEnable() {

        Minecraft client = Minecraft.getMinecraft();

        // Modify armor / elytra renderer to our own
        RenderManager renderManager = client.getRenderManager();
        RenderPlayer normal = renderManager.getSkinMap().get("default");
        RenderPlayer slim = renderManager.getSkinMap().get("slim");

        try {
            List<Object> layers_default = (List<Object>)this.playerRendererLayersField.get(normal);
            List<Object> layers_slim = (List<Object>)this.playerRendererLayersField.get(slim);

            // Set Armor renderers
            layers_default.set(0, new CustomLayerBipedArmor(normal));
            layers_slim.set(0, new CustomLayerBipedArmor(slim));

            // Set Elytra renderers
            layers_default.set(6, new CustomLayerElytra(normal));
            layers_slim.set(6, new CustomLayerElytra(slim));

        } catch (IllegalAccessException e) {
            throw new RuntimeException("FamilyFunPack error: " + e.getMessage());
        }

        // Init item renderer
        try {
            // Get new item renderer
            RenderItem newRenderItem = null;
            if(this.renderItemSave == null) {
                newRenderItem = new CustomRenderItem(client.getTextureManager(), client.getRenderItem().getItemModelMesher().getModelManager(), client.getItemColors());
                ((IReloadableResourceManager)(client.getResourceManager())).registerReloadListener(newRenderItem);
            } else {
                newRenderItem = this.renderItemSave;
            }

            // save old item renderer
            this.renderItemSave = (RenderItem)this.renderItemField.get(client);

            // Modify item renderer
            this.renderItemField.set(client, newRenderItem);
            this.itemRendererField.set(client, new ItemRenderer(client));

            // Get new entity renderer
            EntityRenderer newEntityRenderer = null;
            if(this.entityRendererSave == null) {
                newEntityRenderer = new EntityRenderer(client, client.getResourceManager());
                ((IReloadableResourceManager)(client.getResourceManager())).registerReloadListener(newEntityRenderer);
            } else {
                newEntityRenderer = this.entityRendererSave;
            }

            // save old entity renderer
            this.entityRendererSave = client.entityRenderer;

            // Update Item renderer in entity renderer (for first person item render)
            client.entityRenderer = newEntityRenderer;

        } catch (IllegalAccessException e) {
            throw new RuntimeException("FamilyFunPack error: " + e.getMessage());
        }

        // Replace old guiInGame
        client.ingameGUI = new GuiIngameForge(client);

        MinecraftForge.EVENT_BUS.register(this); // register to listen for tooltip events
    }

    public void onDisable() {
        Minecraft client = Minecraft.getMinecraft();

        // Reset armor / elytra renderers
        RenderManager renderManager = client.getRenderManager();
        RenderPlayer normal = renderManager.getSkinMap().get("default");
        RenderPlayer slim = renderManager.getSkinMap().get("slim");

        try {
            List<Object> layers_default = (List<Object>)this.playerRendererLayersField.get(normal);
            List<Object> layers_slim = (List<Object>)this.playerRendererLayersField.get(slim);

            // Set Armor renderers
            layers_default.set(0, new LayerBipedArmor(normal));
            layers_slim.set(0, new LayerBipedArmor(slim));

            // Set Elytra renderers
            layers_default.set(6, new LayerElytra(normal));
            layers_slim.set(6, new LayerElytra(slim));

        } catch (IllegalAccessException e) {
            throw new RuntimeException("FamilyFunPack error: " + e.getMessage());
        }

        // Reset item renderers
        try {
            RenderItem newRenderItem = this.renderItemSave;
            EntityRenderer newEntityRenderer = this.entityRendererSave;

            // save previous renderers
            this.renderItemSave = (RenderItem)this.renderItemField.get(client);
            this.entityRendererSave = client.entityRenderer;

            // Modify item renderer
            this.renderItemField.set(client, newRenderItem);
            this.itemRendererField.set(client, new ItemRenderer(client));

            // Update Item renderer in entity renderer (for first person item render)
            client.entityRenderer = newEntityRenderer;

        } catch (IllegalAccessException e) {
            throw new RuntimeException("FamilyFunPack error: " + e.getMessage());
        }

        // Reset guiInGame
        client.ingameGUI = new GuiIngameForge(client);

        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void itemToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        int max = stack.getMaxDamage();

        if(stack.isEmpty() || max <= 0) return;
        if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("Unbreakable")) return;

        List<String> toolTip = event.getToolTip();

        int damage;
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && tag instanceof SpecialTagCompound) {
            damage = ((SpecialTagCompound)tag).getTrueDamage();
        } else damage = stack.getItemDamage();

        long count = (long)max - (long)damage;

        TextFormatting color;
        if(damage < 0) color = TextFormatting.DARK_PURPLE;
        else if(damage > max) color = TextFormatting.DARK_RED;
        else color = TextFormatting.BLUE;

        toolTip.add("");
        toolTip.add(color + "Durability: " + count + " [Max: " + Long.toString(max) + "]" + TextFormatting.RESET);
    }

    public Packet<?> packetReceived(EnumPacketDirection direction, int id, Packet<?> packet, ByteBuf in) {
        switch(id) {
            case 20: // SPacketWindowItems
            {
                SPacketWindowItems packet_window = (SPacketWindowItems) packet;
                PacketBuffer buf = new PacketBuffer(in);
                buf.readerIndex(buf.readerIndex() + 4);
                for(ItemStack i : packet_window.getItemStacks()) {
                    if(buf.readShort() >= 0) {
                        buf.readerIndex(buf.readerIndex() + 1);
                        short true_damage = buf.readShort();
                        try {
                            if(true_damage < 0) {
                                i.setTagCompound(new SpecialTagCompound(buf.readCompoundTag(), true_damage));
                            } else buf.readCompoundTag();
                        } catch (IOException e) {
                            break;
                        }
                    }
                }
            }
            break;
            case 22: // SPacketSetSlot
            {
                SPacketSetSlot packet_slot = (SPacketSetSlot) packet;
                PacketBuffer buf = new PacketBuffer(in);
                buf.readerIndex(buf.readerIndex() + 4);
                if(buf.readShort() >= 0) {
                    buf.readerIndex(buf.readerIndex() + 1);
                    short real_damage = buf.readShort();
                    if(real_damage < 0) {
                        ItemStack stack = packet_slot.getStack();
                        stack.setTagCompound(new SpecialTagCompound(stack.getTagCompound(), real_damage));
                    }
                }
            }
            break;
            case 63: // SPacketEntityEquipment
            {
                SPacketEntityEquipment equipment = (SPacketEntityEquipment) packet;
                PacketBuffer buf = new PacketBuffer(in);
                buf.readerIndex(buf.readerIndex() + 3 + (int)Math.floor(Math.log(equipment.getEntityID()) / Math.log(128d)));
                if(buf.readShort() >= 0) {
                    buf.readerIndex(buf.readerIndex() + 1);
                    short real_damage = buf.readShort();
                    if(real_damage < 0) {
                        ItemStack stack = equipment.getItemStack();
                        stack.setTagCompound(new SpecialTagCompound(stack.getTagCompound(), real_damage));
                    }
                }
            }
            break;
        }
        return packet;
    }

    public boolean defaultState() {
        return true;
    }

}