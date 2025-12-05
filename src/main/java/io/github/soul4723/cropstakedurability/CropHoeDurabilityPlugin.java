package io.github.soul4723.cropstakedurability;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.HandlerList;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public final class CropHoeDurabilityPlugin extends JavaPlugin {

    private CropDurabilityListener cropListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadAndRegisterListener();
        getLogger().info("CropsTakeDurability enabled.");
    }

    @Override
    public void onDisable() {
        if (cropListener != null) {
            HandlerList.unregisterAll(cropListener);
            cropListener = null;
        }
        getLogger().info("CropsTakeDurability disabled.");
    }

    private void loadAndRegisterListener() {
        List<String> cropNames = getConfig().getStringList("crop-materials");

        EnumSet<Material> cropMaterials = cropNames.stream()
                .map(materialName -> {
                    try {
                        return Material.valueOf(materialName.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        getLogger().warning("Invalid material in config.yml: " + materialName);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Material.class)));

        if (cropMaterials.isEmpty()) {
            getLogger().warning("No valid crop materials configured. The plugin will not affect any blocks.");
        }

        if (cropListener != null) {
            HandlerList.unregisterAll(cropListener);
        }

        cropListener = new CropDurabilityListener(cropMaterials);
        getServer().getPluginManager().registerEvents(cropListener, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("cropsdurability")) {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("cropstakedurability.reload")) {
                sender.sendMessage("You do not have permission to use this command.");
                return true;
            }

            reloadConfig();
            loadAndRegisterListener();
            sender.sendMessage("CropsTakeDurability configuration reloaded.");
            return true;
        }

        sender.sendMessage("Usage: /" + label + " reload");
        return true;
    }
}
