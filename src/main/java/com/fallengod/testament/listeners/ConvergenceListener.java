package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.managers.ConvergenceManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ConvergenceListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final ConvergenceManager convergenceManager;
    
    public ConvergenceListener(TestamentPlugin plugin, ConvergenceManager convergenceManager) {
        this.plugin = plugin;
        this.convergenceManager = convergenceManager;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        
        // Only process right-click events
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        // Check if player clicked the convergence activation block (nether star)
        if (block.getType() != Material.NETHERITE_BLOCK) {
            return;
        }
        
        plugin.getLogger().info("=== CONVERGENCE NEXUS INTERACTION ===");
        plugin.getLogger().info("Player: " + player.getName());
        plugin.getLogger().info("Block: " + block.getType());
        plugin.getLogger().info("Can achieve convergence: " + convergenceManager.canAchieveConvergence(player));
        
        // Check if this is a convergence nexus
        if (!convergenceManager.canAchieveConvergence(player)) {
            if (convergenceManager.hasAchievedConvergence(player.getUniqueId())) {
                player.sendMessage("§7You have already achieved divine convergence.");
            } else {
                player.sendMessage("§cYou must complete all 12 testaments to activate the Convergence Nexus.");
                player.sendMessage("§7Current progress: " + 
                    plugin.getPlayerDataManager().getPlayerData(player.getUniqueId()).getCompletedTestaments().size() + "/12");
            }
            return;
        }
        
        // Activate the convergence nexus
        boolean success = convergenceManager.activateConvergenceNexus(player, block.getLocation());
        
        if (success) {
            plugin.getLogger().info("Convergence successfully activated for " + player.getName());
        } else {
            plugin.getLogger().warning("Convergence activation failed for " + player.getName());
        }
        
        event.setCancelled(true);
    }
}