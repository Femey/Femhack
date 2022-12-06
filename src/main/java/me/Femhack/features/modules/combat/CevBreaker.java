package me.Femhack.features.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.Vec3i;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.Femhack.util.BlockUtil;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import me.Femhack.features.command.Command;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import java.util.ArrayList;
import me.Femhack.features.setting.Setting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import me.Femhack.features.modules.Module;

public class CevBreaker extends Module
{
    private List<BlockPos> placeList;
    private boolean placing;
    private boolean placedCrystal;
    private boolean breaking;
    private boolean broke;
    private EntityPlayer _target;
    private BlockPos b_crystal;
    private BlockPos breakPos;
    private int attempts;
    private Setting<type> targetType;
    private Setting<mode> breakMode;
    private Setting<Boolean> rotate;
    private Setting<Integer> startDelay;
    private Setting<Integer> breakDelay;
    private Setting<Integer> crystalDelay;
    private Setting<Integer> breakAttempts;
    private int timer;

    public CevBreaker() {
        super("CevBreaker", "Attack Ceil", Category.COMBAT, true, false, false);
        this.placeList = new ArrayList<BlockPos>();
        this.placing = false;
        this.placedCrystal = false;
        this.breaking = false;
        this.broke = false;
        this._target = null;
        this.b_crystal = null;
        this.breakPos = null;
        this.attempts = 0;
        this.targetType = (Setting<type>)this.register(new Setting("Target", type.NEAREST));
        this.breakMode = (Setting<mode>)this.register(new Setting("Break Mode", mode.Vanilla));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", true));
        this.startDelay = (Setting<Integer>)this.register(new Setting("Start Delay", 1, 0, 10));
        this.breakDelay = (Setting<Integer>)this.register(new Setting("Break Delay", 1, 0, 10));
        this.crystalDelay = (Setting<Integer>)this.register(new Setting("Crystal Delay", 1, 0, 10));
        this.breakAttempts = (Setting<Integer>)this.register(new Setting("Break Attempts", 3, 0, 10));
        this.timer = 0;
    }

    @Override
    public void onEnable() {
        this.init();
    }

    private void init() {
        this.placeList = new ArrayList<BlockPos>();
        this._target = null;
        this.b_crystal = null;
        this.placedCrystal = false;
        this.placing = false;
        this.breaking = false;
        this.broke = false;
        this.timer = 0;
        this.attempts = 0;
    }

    @Override
    public void onTick() {
        final int pix = this.findItem(Items.DIAMOND_PICKAXE);
        final int crystal = this.findItem(Items.END_CRYSTAL);
        final int obby = this.findMaterials(Blocks.OBSIDIAN);
        if (pix == -1 || crystal == -1 || obby == -1) {
            Command.sendMessage("Missing Materials! disabling....");
            this.disable();
            return;
        }
        if (this._target == null) {
            if (this.targetType.getValue() == type.NEAREST) {
                this._target = (EntityPlayer)CevBreaker.mc.world.playerEntities.stream().filter(p -> p.entityId != CevBreaker.mc.player.entityId).min(Comparator.comparing(p -> p.getDistance((Entity)CevBreaker.mc.player))).orElse(null);
            }
            if (this._target == null) {
                this.disable();
                return;
            }
        }
        if (this.placeList.size() == 0 && !this.placing) {
            this.searchSpace();
            if (this.placeList.size() == 0) {
                Command.sendMessage("Not found space! disabling...");
                this.disable();
                return;
            }
        }
        if (!this.placedCrystal) {
            if (this.timer < this.startDelay.getValue()) {
                ++this.timer;
                return;
            }
            this.timer = 0;
            this.doPlace(obby, crystal);
        }
        else if (!this.breaking) {
            if (this.timer < this.breakDelay.getValue()) {
                ++this.timer;
                return;
            }
            this.timer = 0;
            if (this.breakMode.getValue() == mode.Vanilla) {
                CevBreaker.mc.player.inventory.currentItem = pix;
                CevBreaker.mc.playerController.updateController();
                CevBreaker.mc.player.swingArm(EnumHand.MAIN_HAND);
                CevBreaker.mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
            }
            else {
                CevBreaker.mc.player.swingArm(EnumHand.MAIN_HAND);
                CevBreaker.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
                CevBreaker.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.breakPos, EnumFacing.DOWN));
            }
            this.breaking = true;
        }
        else if (this.breaking && !this.broke) {
            if (this.getBlock(this.breakPos) == Blocks.AIR) {
                this.broke = true;
            }
        }
        else if (this.broke) {
            if (this.timer < this.crystalDelay.getValue()) {
                ++this.timer;
                return;
            }
            this.timer = 0;
            final Entity bcrystal = (Entity)CevBreaker.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal).min(Comparator.comparing(c -> c.getDistance((Entity)this._target))).orElse(null);
            if (bcrystal == null) {
                if (this.attempts < this.breakAttempts.getValue()) {
                    ++this.attempts;
                    return;
                }
                Command.sendMessage("Not found crystal! retrying...");
                this.placedCrystal = false;
                this.placeList.add(this.breakPos);
                this.breaking = false;
                this.broke = false;
                this.attempts = 0;
            }
            else {
                CevBreaker.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(bcrystal));
                this.placedCrystal = false;
                this.placeList.add(this.breakPos);
                this.breaking = false;
                this.broke = false;
                this.attempts = 0;
            }
        }
    }

    private void doPlace(final int obby, final int crystal) {
        this.placing = true;
        if (this.placeList.size() != 0) {
            final int oldslot = CevBreaker.mc.player.inventory.currentItem;
            CevBreaker.mc.player.inventory.currentItem = obby;
            CevBreaker.mc.playerController.updateController();
            BlockUtil.placeBlock(this.placeList.get(0), EnumHand.MAIN_HAND, this.rotate.getValue(), false, false);
            this.placeList.remove(0);
            CevBreaker.mc.player.inventory.currentItem = oldslot;
        }
        else if (!this.placedCrystal) {
            final int oldslot = CevBreaker.mc.player.inventory.currentItem;
            if (crystal != 999) {
                CevBreaker.mc.player.inventory.currentItem = crystal;
            }
            CevBreaker.mc.playerController.updateController();
            CevBreaker.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.b_crystal, EnumFacing.UP, (CevBreaker.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            CevBreaker.mc.player.inventory.currentItem = oldslot;
            this.placedCrystal = true;
        }
    }

    private void searchSpace() {
        final BlockPos ppos = CevBreaker.mc.player.getPosition();
        final BlockPos tpos = new BlockPos(this._target.posX, this._target.posY, this._target.posZ);
        this.placeList = new ArrayList<BlockPos>();
        final BlockPos[] offset = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
        if (this.getBlock(new BlockPos(tpos.getX(), tpos.getY() + 3, tpos.getZ())) != Blocks.AIR || this.getBlock(new BlockPos(tpos.getX(), tpos.getY() + 4, tpos.getZ())) != Blocks.AIR) {
            return;
        }
        final List<BlockPos> posList = new ArrayList<BlockPos>();
        for (int i = 0; i < offset.length; ++i) {
            final BlockPos offsetPos = tpos.add((Vec3i)offset[i]);
            final Block block = this.getBlock(offsetPos);
            if (block != Blocks.AIR && !(block instanceof BlockLiquid)) {
                posList.add(offsetPos);
            }
        }
        final BlockPos base = posList.stream().max(Comparator.comparing(b -> this._target.getDistance((double)b.getX(), (double)b.getY(), (double)b.getZ()))).orElse(null);
        if (base == null) {
            return;
        }
        this.placeList.add(base);
        this.placeList.add(base.add(0, 1, 0));
        this.placeList.add(base.add(0, 2, 0));
        this.placeList.add(tpos.add(0, 2, 0));
        this.breakPos = tpos.add(0, 2, 0);
        this.b_crystal = tpos.add(0, 2, 0);
    }

    private int findMaterials(final Block b) {
        for (int i = 0; i < 9; ++i) {
            if (CevBreaker.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock)CevBreaker.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == b) {
                return i;
            }
        }
        return -1;
    }

    private int findItem(final Item item) {
        if (item == Items.END_CRYSTAL && CevBreaker.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return 999;
        }
        for (int i = 0; i < 9; ++i) {
            if (CevBreaker.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    private Block getBlock(final BlockPos b) {
        return CevBreaker.mc.world.getBlockState(b).getBlock();
    }

    public enum type
    {
        NEAREST,
        LOOKING;
    }

    public enum mode
    {
        Vanilla,
        Packet;
    }
}