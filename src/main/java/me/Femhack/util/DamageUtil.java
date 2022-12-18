package me.Femhack.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.block.Block;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class DamageUtil
        implements Util {
    public static int getRoundedDamage(ItemStack stack) {
        return (int) DamageUtil.getDamageInPercent(stack);
    }

    public static float getDamageInPercent(ItemStack stack) {
        return (float) (DamageUtil.getItemDamage(stack) / stack.getMaxDamage()) * 100.0f;
    }

    public static boolean isArmorLow(EntityPlayer player, int durability) {
        for (ItemStack piece : player.inventory.armorInventory) {
            if (piece == null) {
                return true;
            }
            if (DamageUtil.getItemDamage(piece) >= durability) continue;
            return true;
        }
        return false;
    }

    public static int getItemDamage(ItemStack stack) {
        return stack.getMaxDamage() - stack.getItemDamage();
    }

    public static boolean canTakeDamage(boolean suicide) {
        return !mc.player.capabilities.isCreativeMode && !suicide;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception exception) {
            // empty catch block
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (int) ((v * v + v) / 2.0 * 7.0 * (double) doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = DamageUtil.getBlastReduction((EntityLivingBase) entity, DamageUtil.getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float) finald;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, boolean ignoreTerrain) {
        float finalDamage = 1.0f;
        try {
            float doubleExplosionSize = 12.0F;
            double distancedSize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
            double blockDensity = ignoreTerrain ?
                    ignoreTerrainDecntiy(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox(), mc.world)
                    : entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
            double v = (1.0D - distancedSize) * blockDensity;
            float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));

            if (entity instanceof EntityLivingBase) {
                finalDamage = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
            }
        } catch (NullPointerException ignored) {
        }

        return finalDamage;
    }

    public static float ignoreTerrainDecntiy(Vec3d vec, AxisAlignedBB bb, World world) {
        double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D)
        {
            int j2 = 0;
            int k2 = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0))
            {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
                {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                    {
                        double d5 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                        double d6 = bb.minY + (bb.maxY - bb.minY) * (double)f1;
                        double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f2;
                        RayTraceResult result;

                        if ( (result = world.rayTraceBlocks(new Vec3d(d5 + d3, d6, d7 + d4), vec)) == null)
                        {
                            ++j2;
                        } else {
                            Block blockHit = BlockUtil.getBlock(result.getBlockPos());
                            if (blockHit.blockResistance < 600)
                                ++j2;
                        }

                        ++k2;
                    }
                }
            }

            return (float)j2 / (float)k2;
        }
        else
        {
            return 0.0F;
        }

    }

    public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int k = 0;
            try {
                k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            } catch (Exception exception) {
                // empty catch block
            }
            float f = MathHelper.clamp((float) k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    public static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(BlockPos pos, Entity entity) {
        return DamageUtil.calculateDamage((double) pos.getX() + 0.5, pos.getY() + 1, (double) pos.getZ() + 0.5, entity);
    }

    public static boolean canBreakWeakness(EntityPlayer player) {
        int strengthAmp = 0;
        PotionEffect effect = DamageUtil.mc.player.getActivePotionEffect(MobEffects.STRENGTH);
        if (effect != null) {
            strengthAmp = effect.getAmplifier();
        }
        return !DamageUtil.mc.player.isPotionActive(MobEffects.WEAKNESS) || strengthAmp >= 1 || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSpade;
    }

    public static boolean isNaked(EntityPlayer player) {
        for (ItemStack piece : player.inventory.armorInventory) {
            if (piece == null || piece.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public static float calculateDamage(Entity crystal, Entity entity) {
        return DamageUtil.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    public static int getCooldownByWeapon(EntityPlayer player) {
        Item item = player.getHeldItemMainhand().getItem();
        if (item instanceof ItemSword) {
            return 600;
        }
        if (item instanceof ItemPickaxe) {
            return 850;
        }
        if (item == Items.IRON_AXE) {
            return 1100;
        }
        if (item == Items.STONE_HOE) {
            return 500;
        }
        if (item == Items.IRON_HOE) {
            return 350;
        }
        if (item == Items.WOODEN_AXE || item == Items.STONE_AXE) {
            return 1250;
        }
        if (item instanceof ItemSpade || item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.WOODEN_HOE || item == Items.GOLDEN_HOE) {
            return 1000;
        }
        return 250;
    }
}

