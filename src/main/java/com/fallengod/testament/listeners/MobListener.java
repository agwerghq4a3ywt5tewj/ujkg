package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class MobListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Set<EntityType> dangerousMobs;
    
    public MobListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.dangerousMobs = Set.of(
            EntityType.WITHER_SKELETON,
            EntityType.BLAZE,
            EntityType.GHAST,
            EntityType.ELDER_GUARDIAN,
            EntityType.WARDEN,
            EntityType.ENDER_DRAGON,
            EntityType.WITHER,
            EntityType.RAVAGER,
            EntityType.EVOKER,
            EntityType.VEX,
            EntityType.PIGLIN_BRUTE,
            EntityType.HOGLIN,
            EntityType.ZOGLIN
        );
    }
    
    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        
        Player player = event.getEntity().getKiller();
        
        // Only dangerous mobs can drop fragments
        if (!dangerousMobs.contains(event.getEntity().getType())) {
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        // Check if fragment should spawn
        if (plugin.getFragmentManager().shouldSpawnMobFragment(player)) {
            ItemStack fragment = plugin.getFragmentManager().generateRandomFragment();
            
            // Add fragment to drops
            event.getDrops().add(fragment);
            
            // Update cooldown
            data.setLastMobFragment(System.currentTimeMillis());
            
            // Notify player
            player.sendMessage("§6⚔ The " + event.getEntity().getType().name().toLowerCase().replace("_", " ") + 
                             " drops a divine fragment!");
            player.sendMessage("§7Your courage in battle has been rewarded...");
            
            plugin.getLogger().info("Fragment dropped by " + event.getEntity().getType() + 
                                  " for player " + player.getName());
        }
        
        // Save player data
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
    }
}