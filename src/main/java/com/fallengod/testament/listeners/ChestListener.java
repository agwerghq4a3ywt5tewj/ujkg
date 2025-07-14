package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class ChestListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public ChestListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }
        
        // Check for various container types that can spawn fragments
        if (event.getInventory().getType() != InventoryType.CHEST &&
            event.getInventory().getType() != InventoryType.BARREL &&
            event.getInventory().getType() != InventoryType.SHULKER_BOX &&
            event.getInventory().getType() != InventoryType.ENDER_CHEST) {
            return;
        }
        
        // Don't spawn fragments in player inventories or crafting tables
        if (event.getInventory().getHolder() instanceof Player) {
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        data.incrementChestsOpened();
        
        // Check if fragment should spawn
        if (plugin.getFragmentManager().shouldSpawnChestFragment(player)) {
            ItemStack fragment = plugin.getFragmentManager().generateRandomFragment();
            
            // Try to add fragment to chest inventory
            var leftover = event.getInventory().addItem(fragment);
            
            // If chest is full, drop at player location
            if (!leftover.isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), fragment);
                player.sendMessage("§6✨ A divine fragment appears! (Chest was full, dropped at your feet)");
            } else {
                player.sendMessage("§6✨ A divine fragment materializes in the chest!");
            }
            
            // Update cooldown
            data.setLastChestFragment(System.currentTimeMillis());
            
            player.sendMessage("§7The gods have blessed your exploration...");
            
            plugin.getLogger().info("Fragment spawned in chest for player " + player.getName());
        }
        
        // Save player data
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
    }
}