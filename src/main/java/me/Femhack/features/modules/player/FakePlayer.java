package me.Femhack.features.modules.player;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.Femhack.event.events.PacketEvent;
import me.Femhack.event.events.TotemPopEvent;
import me.Femhack.features.modules.*;
import me.Femhack.util.DamageUtil;
import me.Femhack.util.PositionforFPUtil;
import me.Femhack.util.Timer;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.*;
import me.Femhack.features.setting.*;
import com.mojang.authlib.*;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import java.util.*;

import net.minecraft.potion.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakePlayer extends Module {
    final private ItemStack[] armors = new ItemStack[]{
            new ItemStack(Items.DIAMOND_BOOTS),
            new ItemStack(Items.DIAMOND_LEGGINGS),
            new ItemStack(Items.DIAMOND_CHESTPLATE),
            new ItemStack(Items.DIAMOND_HELMET)
    };

    private final me.Femhack.util.Timer timer = new Timer();

    private static FakePlayer INSTANCE;

    public Setting<Integer> vulnerabilityTick = this.register(new Setting<>("Vulnerability Tick", 4, 0, 10));
    public Setting<Integer> resetHealth = this.register(new Setting<>("Reset Health", 10, 0, 36));
    public Setting<Integer> tickRegenVal = this.register(new Setting<>("Tick Regen", 4, 0, 30));
    public Setting<Integer> startHealth = this.register(new Setting<>("Start Health", 20, 0, 30));

    public Setting<String> nameFakePlayer = this.register(new Setting<String>("Name FakePlayer", "Femey"));
    private Setting<Boolean> copyInventory = this.register(new Setting<Boolean>("Copy Inventory", false));

    private Setting<Boolean> playerStacked = this.register(new Setting<Boolean>("Player Stacked", true, v -> !copyInventory.getValue()));
    private Setting<Boolean> onShift = this.register(new Setting<Boolean>("On Crouch", false));
    private Setting<Boolean> simulateDamage = this.register(new Setting<Boolean>("Simulate Damage", false));
    private Setting<Boolean> resistance = this.register(new Setting<Boolean>("Resistance", true));
    private Setting<Boolean> pop = this.register(new Setting<Boolean>("Pop", true));
    private Setting<Boolean> gapple = this.register(new Setting<Boolean>("Gapple", false));
    private Setting<Integer> gappleDelay = this.register(new Setting<Integer>("Gapple Delay", 500, 250, 1000));


    public FakePlayer() {
        super("FakePlayer", "Epic code", Module.Category.PLAYER, true, false, false);
    }

    public static FakePlayer getInstance() {
        if (FakePlayer.INSTANCE == null) {
            FakePlayer.INSTANCE = new FakePlayer();
        }
        return FakePlayer.INSTANCE;
    }


    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
            this.timer.reset();
        }
    }

    int incr;

    @Override
    public void onEnable() {
        this.timer.reset();
        incr = 0;
        beforePressed = false;
        if (mc.player == null || mc.player.isDead) {
            disable();
            return;
        }
        if (!onShift.getValue())
            spawnPlayer();
    }

    EntityOtherPlayerMP clonedPlayer = null;

    void spawnPlayer() {
        // Clone empty player
        clonedPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("c8ccd8c1-322c-48e2-bca6-c9f6159aa973"), nameFakePlayer.getValue() + incr));
        // Copy angles
        clonedPlayer.copyLocationAndAnglesFrom(mc.player);
        clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
        clonedPlayer.rotationYaw = mc.player.rotationYaw;
        clonedPlayer.rotationPitch = mc.player.rotationPitch;
        // set gameType
        clonedPlayer.setGameType(GameType.SURVIVAL);
        clonedPlayer.setHealth(startHealth.getValue());
        // Add entity id
        mc.world.addEntityToWorld((-1234 + incr), clonedPlayer);
        incr++;
        // Set invenotry
        if (copyInventory.getValue())
            clonedPlayer.inventory.copyInventory(mc.player.inventory);
        else
            // If enchants
            if (playerStacked.getValue()) {
                // Iterate
                for (int i = 0; i < 4; i++) {
                    // Create base
                    ItemStack item = armors[i];
                    // Add enchants
                    item.addEnchantment(
                            i == 3 ? Enchantments.BLAST_PROTECTION : Enchantments.PROTECTION,
                            4);
                    // Add it to the player
                    clonedPlayer.inventory.armorInventory.set(i, item);

                }
            }
        if (resistance.getValue())
            clonedPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(11), 123456789, 0));
        clonedPlayer.onEntityUpdate();
        listPlayers.add(new playerInfo(clonedPlayer.getName()));
    }

    boolean beforePressed;

    @Override
    public void onUpdate() {
        // OnShift add
        if (onShift.getValue() && mc.gameSettings.keyBindSneak.isPressed() && !beforePressed) {
            beforePressed = true;
            spawnPlayer();
        } else beforePressed = false;

        // Update tick explosion
        for (int i = 0; i < listPlayers.size(); i++) {
            if (listPlayers.get(i).update()) {
                int finalI = i;
                Optional<EntityPlayer> temp = mc.world.playerEntities.stream().filter(
                        e -> e.getName().equals(listPlayers.get(finalI).name)
                ).findAny();
                if (temp.isPresent())
                    if (temp.get().getHealth() < 20)
                        temp.get().setHealth(temp.get().getHealth() + 1);
            }
        }

        if (gapple.getValue() && this.timer.passedMs(gappleDelay.getValue()))
        {
            clonedPlayer.setAbsorptionAmount(16.0f);
            clonedPlayer.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
            clonedPlayer.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
            clonedPlayer.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
            clonedPlayer.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
            this.timer.reset();
        }
    }


    // Simple list of players for the pop
    ArrayList<playerInfo> listPlayers = new ArrayList<>();

    class playerInfo {
        final String name;
        int tickPop = -1;
        int tickRegen = 0;

        // We just set the new name
        public playerInfo(String name) {
            this.name = name;
        }

        // If update, we have to regen and decrease vulnerability tick
        boolean update() {
            if (tickPop != -1) {
                if (++tickPop >= vulnerabilityTick.getValue())
                    tickPop = -1;
            }
            if (++tickRegen >= tickRegenVal.getValue()) {
                tickRegen = 0;
                return true;
            } else return false;
        }

        boolean canPop() {
            return this.tickPop == -1;
        }
    }


    public void onDisable() {
        if (mc.world != null) {
            for (int i = 0; i < incr; i++) {
                mc.world.removeEntityFromWorld((-1234 + i));
            }
        }
        listPlayers.clear();
        positions.clear();
    }


    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        // Simple crystal damage
        if (simulateDamage.getValue()) {
            Packet<?> packet = event.getPacket();
            if (packet instanceof SPacketSoundEffect) {
                final SPacketSoundEffect packetSoundEffect = (SPacketSoundEffect) packet;
                if (packetSoundEffect.getCategory() == SoundCategory.BLOCKS && packetSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                        if (entity instanceof EntityEnderCrystal) {
                            if (entity.getDistanceSq(packetSoundEffect.getX(), packetSoundEffect.getY(), packetSoundEffect.getZ()) <= 36.0f) {
                                for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                                    // If the player is like we want to be
                                    if (entityPlayer.getName().split(nameFakePlayer.getValue()).length == 2) {

                                        Optional<playerInfo> temp = listPlayers.stream().filter(
                                                e -> e.name.equals(entityPlayer.getName())
                                        ).findAny();
                                        // If he is in wait, continue
                                        if (!temp.isPresent() || !temp.get().canPop())
                                            continue;

                                        // Calculate damage
                                        float damage = DamageUtil.calculateDamage(packetSoundEffect.getX(), packetSoundEffect.getY(), packetSoundEffect.getZ(), entityPlayer, false);
                                        if (damage > entityPlayer.getHealth()) {
                                            // If higher, new health and pop
                                            entityPlayer.setHealth(resetHealth.getValue());
                                            if (pop.getValue()) {
                                                mc.effectRenderer.emitParticleAtEntity(entityPlayer, EnumParticleTypes.TOTEM, 30);
                                                mc.world.playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
                                            }
                                            MinecraftForge.EVENT_BUS.post(new TotemPopEvent(entityPlayer));

                                            // Else, setHealth
                                        } else entityPlayer.setHealth(entityPlayer.getHealth() - damage);

                                        // Add vulnerability
                                        temp.get().tickPop = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    boolean wasRecording;
    protected final List<PositionforFPUtil> positions = new ArrayList<>();
    int index = 0;
    private int ticks;
}