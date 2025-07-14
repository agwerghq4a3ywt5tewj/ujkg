package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.items.FragmentItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AltarListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public AltarListener(TestamentPlugin plugin) {
        this.plugin = plugin;
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
        
        // Enhanced debug logging
        plugin.getLogger().info("=== ALTAR INTERACTION DEBUG ===");
        plugin.getLogger().info("Player: " + player.getName());
        plugin.getLogger().info("Action: " + event.getAction());
        plugin.getLogger().info("Block: " + block.getType());
        
        // Check if player clicked an altar center block
        GodType altarGod = getAltarType(block.getType());
        
        plugin.getLogger().info("Detected altar god: " + altarGod);
        
        if (altarGod == null) {
            plugin.getLogger().info("Not an altar block, ignoring");
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        // Debug fragment status
        plugin.getLogger().info("Player fragments for " + altarGod + ": " + data.getFragments(altarGod).size() + "/7");
        plugin.getLogger().info("Fragment numbers: " + data.getFragments(altarGod));
        plugin.getLogger().info("Has all fragments: " + data.hasAllFragments(altarGod));
        plugin.getLogger().info("Already completed: " + data.hasCompletedTestament(altarGod));
        
        // Check if player has all fragments for this god
        if (!data.hasAllFragments(altarGod)) {
            plugin.getLogger().info("Missing fragments - showing message to player");
            // Only show message if player has at least 1 fragment to avoid spam
            if (data.getFragments(altarGod).size() > 0) {
                player.sendMessage("§cYou need all 7 " + altarGod.getColoredName() + " §cfragments to complete this testament!");
                player.sendMessage("§7You have " + data.getFragments(altarGod).size() + "/7 fragments.");
            }
            return;
        }
        
        // Check if already completed
        if (data.hasCompletedTestament(altarGod)) {
            plugin.getLogger().info("Testament already completed");
            // Only show message occasionally to avoid spam
            if (player.getTicksLived() % 100 == 0) {
                player.sendMessage("§7You have already mastered the " + altarGod.getColoredName() + " §7testament.");
            }
            return;
        }
        
        plugin.getLogger().info("All checks passed - proceeding with testament completion");
        
        // Check for conflicts
        plugin.getAscensionManager().checkForConflicts(player, altarGod);
        
        // Remove fragments from inventory
        plugin.getLogger().info("Removing fragments from inventory");
        removeFragmentsFromInventory(player, altarGod);
        
        // Complete testament
        plugin.getLogger().info("Completing testament");
        data.removeAllFragments(altarGod);
        data.completeTestament(altarGod);
        
        // Give rewards
        plugin.getLogger().info("Giving rewards");
        plugin.getRewardManager().giveTestamentReward(player, altarGod);
        
        // Check for ascension
        plugin.getLogger().info("Checking for ascension");
        plugin.getAscensionManager().checkForAscension(player);
        
        // Check for convergence eligibility
        plugin.getLogger().info("Checking for convergence eligibility");
        plugin.getConvergenceManager().checkForConvergenceEligibility(player);
        
        // Announce completion
        plugin.getLogger().info("Announcing completion");
        announceCompletion(player, altarGod);
        
        // Add dramatic effects
        addAltarCompletionEffects(player, altarGod);
        
        // Save data
        plugin.getLogger().info("Saving player data");
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
        
        plugin.getLogger().info("=== TESTAMENT COMPLETION FINISHED ===");
        event.setCancelled(true);
    }
    
    private GodType getAltarType(Material centerBlock) {
        return switch (centerBlock) {
            case CRYING_OBSIDIAN -> GodType.FALLEN;
            case MAGMA_BLOCK -> GodType.BANISHMENT;
            case DARK_PRISMARINE -> GodType.ABYSSAL;
            case OAK_LOG -> GodType.SYLVAN;
            case LIGHTNING_ROD -> GodType.TEMPEST;
            case END_PORTAL_FRAME -> GodType.VEIL;
            case AMETHYST_CLUSTER -> GodType.TIME;
            case ANVIL -> GodType.FORGE;
            case OBSIDIAN -> GodType.VOID;
            case REDSTONE_BLOCK -> GodType.BLOOD;
            case AMETHYST_BLOCK -> GodType.CRYSTAL;
            case SCULK_CATALYST -> GodType.SHADOW;
            default -> null;
        };
    }
    
    private void removeFragmentsFromInventory(Player player, GodType god) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (FragmentItem.isFragment(item) && FragmentItem.getFragmentGod(item) == god) {
                player.getInventory().remove(item);
            }
        }
    }
    
    private void announceCompletion(Player player, GodType god) {
        String message = "§6§l⚡ " + player.getName() + " has completed the " + 
                        god.getColoredName() + " §6§lTestament! ⚡";
        
        // Broadcast to server
        plugin.getServer().broadcastMessage("");
        plugin.getServer().broadcastMessage(message);
        plugin.getServer().broadcastMessage("§7" + god.getTitle() + " has blessed " + player.getName() + "!");
        plugin.getServer().broadcastMessage("");
        
        // Special message to player
        player.sendTitle(god.getColor() + "TESTAMENT COMPLETE!", 
                        "§7You have been blessed by the " + god.getDisplayName(), 
                        10, 70, 20);
    }
    
    private void addAltarCompletionEffects(Player player, GodType god) {
        var location = player.getLocation();
        var world = player.getWorld();
        
        // God-specific particle effects
        switch (god) {
            case FALLEN -> {
                world.spawnParticle(Particle.SOUL, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, location, 50, 1, 1, 1, 0.05);
                world.playSound(location, Sound.ENTITY_WITHER_SPAWN, 0.8f, 0.5f);
            }
            case BANISHMENT -> {
                world.spawnParticle(Particle.FLAME, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.spawnParticle(Particle.LAVA, location, 30, 1, 1, 1, 0);
                world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 0.8f);
            }
            case ABYSSAL -> {
                world.spawnParticle(Particle.BUBBLE, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.spawnParticle(Particle.DRIPPING_WATER, location, 50, 1, 1, 1, 0);
                world.playSound(location, Sound.AMBIENT_UNDERWATER_ENTER, 1.0f, 1.2f);
            }
            case SYLVAN -> {
                world.spawnParticle(Particle.HAPPY_VILLAGER, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.spawnParticle(Particle.COMPOSTER, location, 50, 1, 1, 1, 0);
                world.playSound(location, Sound.BLOCK_GRASS_BREAK, 1.0f, 1.5f);
            }
            case TEMPEST -> {
                world.spawnParticle(Particle.CLOUD, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.spawnParticle(Particle.ELECTRIC_SPARK, location, 50, 1, 1, 1, 0.1);
                world.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.5f);
            }
            case VEIL -> {
                world.spawnParticle(Particle.PORTAL, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.spawnParticle(Particle.END_ROD, location, 50, 1, 1, 1, 0.05);
                world.playSound(location, Sound.BLOCK_END_PORTAL_SPAWN, 1.0f, 1.0f);
            }
            default -> {
                world.spawnParticle(Particle.ENCHANT, location.add(0, 1, 0), 100, 2, 2, 2, 0.1);
                world.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
        
        // Universal completion effects
        world.spawnParticle(Particle.FIREWORK, location, 20, 1, 1, 1, 0.1);
        world.playSound(location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
    }
}