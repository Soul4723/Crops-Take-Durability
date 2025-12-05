package io.github.soul4723.cropstakedurability;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumSet;
import java.util.Random;

public class CropDurabilityListener implements Listener {

    private final EnumSet<Material> cropMaterials;
    private static final Random RANDOM = new Random();

    public CropDurabilityListener(EnumSet<Material> cropMaterials) {
        this.cropMaterials = cropMaterials;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("cropstakedurability.bypass")) {
            return;
        }

        Material blockType = event.getBlock().getType();
        if (!cropMaterials.contains(blockType)) {
            return;
        }

        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        damageItemIfApplicable(player, mainHand, true);
        damageItemIfApplicable(player, offHand, false);
    }

    private void damageItemIfApplicable(Player player, ItemStack item, boolean isMainHand) {
        if (item == null || item.getType().isAir()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable damageable)) {
            return;
        }

        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
        if (!shouldDamage(unbreakingLevel)) {
            return;
        }

        int currentDamage = damageable.getDamage();
        int maxDurability = item.getType().getMaxDurability();

        if (currentDamage + 1 >= maxDurability) {
            if (isMainHand) {
                player.getInventory().setItemInMainHand(null);
            } else {
                player.getInventory().setItemInOffHand(null);
            }
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
        } else {
            damageable.setDamage(currentDamage + 1);
            item.setItemMeta(meta);

            if (isMainHand) {
                player.getInventory().setItemInMainHand(item);
            } else {
                player.getInventory().setItemInOffHand(item);
            }
        }
    }

    private boolean shouldDamage(int unbreakingLevel) {
        if (unbreakingLevel <= 0) {
            return true;
        }
        return RANDOM.nextInt(unbreakingLevel + 1) == 0;
    }
}