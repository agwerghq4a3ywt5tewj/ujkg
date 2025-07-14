package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.events.ConvergenceEvent;
import com.fallengod.testament.items.ConvergenceItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConvergenceManager {
    private final TestamentPlugin plugin;
    private final Map<UUID, Location> playerNexusLocations;
    private final Map<UUID, Boolean> convergenceAchieved;
    
    public ConvergenceManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.playerNexusLocations = new HashMap<>();
        this.convergenceAchieved = new HashMap<>();
    }
    
    public boolean hasAchievedConvergence(UUID playerId) {
        return convergenceAchieved.getOrDefault(playerId, false);
    }
    
    public boolean canAchieveConvergence(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        // Must have completed ALL 12 testaments
        if (data.getCompletedTestaments().size() < 12) {
            return false;
        }
        
        // Must have completed every single god
        for (GodType god : GodType.values()) {
            if (!data.hasCompletedTestament(god)) {
                return false;
            }
        }
        
        // Must not have already achieved convergence
        return !hasAchievedConvergence(player.getUniqueId());
    }
    
    public void checkForConvergenceEligibility(Player player) {
        if (canAchieveConvergence(player)) {
            spawnConvergenceNexus(player);
        }
    }
    
    private void spawnConvergenceNexus(Player player) {
        Location nexusLocation = findSuitableNexusLocation(player);
        
        // Create the Convergence Nexus structure
        createNexusStructure(nexusLocation);
        
        // Store the location
        playerNexusLocations.put(player.getUniqueId(), nexusLocation);
        
        // Announce the convergence opportunity
        announceConvergenceOpportunity(player, nexusLocation);
        
        plugin.getLogger().info("Convergence Nexus spawned for " + player.getName() + 
            " at " + nexusLocation.getBlockX() + ", " + nexusLocation.getBlockY() + ", " + nexusLocation.getBlockZ());
    }
    
    private Location findSuitableNexusLocation(Player player) {
        Location playerLoc = player.getLocation();
        
        // Find a suitable location 50-100 blocks away
        double angle = Math.random() * 2 * Math.PI;
        double distance = 50 + Math.random() * 50; // 50-100 blocks
        
        int x = (int) (playerLoc.getX() + Math.cos(angle) * distance);
        int z = (int) (playerLoc.getZ() + Math.sin(angle) * distance);
        int y = player.getWorld().getHighestBlockYAt(x, z) + 1;
        
        return new Location(player.getWorld(), x, y, z);
    }
    
    private void createNexusStructure(Location center) {
        // Create a magnificent 9x9 structure
        Material baseMaterial = Material.OBSIDIAN;
        Material pillarMaterial = Material.CRYING_OBSIDIAN;
        Material centerMaterial = Material.BEACON;
        
        // Create obsidian base (9x9)
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                Location loc = center.clone().add(x, 0, z);
                loc.getBlock().setType(baseMaterial);
            }
        }
        
        // Create inner platform (5x5) with crying obsidian
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Location loc = center.clone().add(x, 1, z);
                loc.getBlock().setType(pillarMaterial);
            }
        }
        
        // Create center beacon
        center.clone().add(0, 2, 0).getBlock().setType(centerMaterial);
        
        // Create pillars at corners
        for (int i = 0; i < 4; i++) {
            int x = (i % 2 == 0) ? -3 : 3;
            int z = (i < 2) ? -3 : 3;
            
            for (int y = 1; y <= 5; y++) {
                Location pillarLoc = center.clone().add(x, y, z);
                pillarLoc.getBlock().setType(pillarMaterial);
            }
            
            // Top with end rods
            center.clone().add(x, 6, z).getBlock().setType(Material.END_ROD);
        }
        
        // Create the convergence activation block (nether star block)
        center.clone().add(0, 1, 0).getBlock().setType(Material.NETHERITE_BLOCK);
    }
    
    private void announceConvergenceOpportunity(Player player, Location nexusLocation) {
        // Epic server-wide announcement
        plugin.getServer().broadcastMessage("");
        plugin.getServer().broadcastMessage("§6§l✦ ✦ ✦ DIVINE CONVERGENCE ✦ ✦ ✦");
        plugin.getServer().broadcastMessage("§d" + player.getName() + " has mastered ALL TWELVE FALLEN GODS!");
        plugin.getServer().broadcastMessage("§7The Convergence Nexus has manifested at:");
        plugin.getServer().broadcastMessage("§f" + nexusLocation.getBlockX() + ", " + nexusLocation.getBlockY() + ", " + nexusLocation.getBlockZ());
        plugin.getServer().broadcastMessage("§8\"When all gods unite under one will...\"");
        plugin.getServer().broadcastMessage("§8\"...reality itself bends to their command.\"");
        plugin.getServer().broadcastMessage("");
        
        // Special message to the player
        player.sendTitle("§6§lDIVINE CONVERGENCE", "§7The Nexus awaits your touch", 20, 100, 20);
        player.sendMessage("§6§l✦ You have achieved the impossible! All twelve gods bow to your will!");
        player.sendMessage("§7Travel to the Convergence Nexus and claim your ultimate reward...");
        
        // Epic effects at player location
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 2, 0), 100, 3, 3, 3, 0.1);
        player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation(), 50, 2, 2, 2, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 0.5f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 2.0f);
    }
    
    public boolean activateConvergenceNexus(Player player, Location nexusLocation) {
        if (!canAchieveConvergence(player)) {
            return false;
        }
        
        // Fire the convergence event
        ConvergenceEvent event = new ConvergenceEvent(player, nexusLocation);
        plugin.getServer().getPluginManager().callEvent(event);
        
        if (event.isCancelled()) {
            return false;
        }
        
        // Mark convergence as achieved
        convergenceAchieved.put(player.getUniqueId(), true);
        
        // Give ultimate rewards
        giveConvergenceRewards(player);
        
        // Epic completion effects
        createConvergenceCompletionEffects(player, nexusLocation);
        
        // Final announcement
        announceConvergenceCompletion(player);
        
        // Remove the nexus structure after a delay
        new BukkitRunnable() {
            @Override
            public void run() {
                removeNexusStructure(nexusLocation);
            }
        }.runTaskLater(plugin, 1200L); // 1 minute delay
        
        return true;
    }
    
    private void giveConvergenceRewards(Player player) {
        // Give the three ultimate items
        player.getInventory().addItem(ConvergenceItems.createNexusCrown());
        player.getInventory().addItem(ConvergenceItems.createOmnipotentScepter());
        player.getInventory().addItem(ConvergenceItems.createDivineCodex());
        
        // Apply ultimate effects
        player.setMaxHealth(40.0); // 20 hearts + 20 from crown = 40 total
        player.setHealth(40.0);
        
        // Grant creative flight
        player.setAllowFlight(true);
        player.setFlying(true);
        
        // Ultimate potion effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 9, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        
        player.sendMessage("§6§l✦ CONVERGENCE COMPLETE! ✦");
        player.sendMessage("§7You have transcended mortality itself!");
        player.sendMessage("§7You are now the §6§lMaster of All Divinity§7!");
    }
    
    private void createConvergenceCompletionEffects(Player player, Location nexusLocation) {
        // Massive particle explosion
        for (int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    nexusLocation.getWorld().spawnParticle(Particle.END_ROD, nexusLocation.add(0, 3, 0), 200, 5, 5, 5, 0.2);
                    nexusLocation.getWorld().spawnParticle(Particle.FIREWORK, nexusLocation, 100, 3, 3, 3, 0.1);
                    nexusLocation.getWorld().spawnParticle(Particle.ENCHANT, nexusLocation, 150, 4, 4, 4, 0.15);
                    nexusLocation.getWorld().spawnParticle(Particle.PORTAL, nexusLocation, 100, 2, 2, 2, 0.1);
                }
            }.runTaskLater(plugin, i * 10L);
        }
        
        // Epic sound sequence
        nexusLocation.getWorld().playSound(nexusLocation, Sound.ENTITY_ENDER_DRAGON_DEATH, 2.0f, 0.5f);
        nexusLocation.getWorld().playSound(nexusLocation, Sound.ENTITY_WITHER_SPAWN, 1.0f, 2.0f);
        nexusLocation.getWorld().playSound(nexusLocation, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 1.0f);
        nexusLocation.getWorld().playSound(nexusLocation, Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 0.8f);
    }
    
    private void announceConvergenceCompletion(Player player) {
        plugin.getServer().broadcastMessage("");
        plugin.getServer().broadcastMessage("§6§l✦ ✦ ✦ CONVERGENCE ACHIEVED ✦ ✦ ✦");
        plugin.getServer().broadcastMessage("§d" + player.getName() + " has become the §6§lMaster of All Divinity§d!");
        plugin.getServer().broadcastMessage("§7All twelve fallen gods now serve under one supreme will!");
        plugin.getServer().broadcastMessage("§8\"I am Alpha and Omega, the beginning and the end.\"");
        plugin.getServer().broadcastMessage("§8\"I am the master of life, death, and all between.\"");
        plugin.getServer().broadcastMessage("");
        
        // Log this historic achievement
        plugin.getLogger().info("CONVERGENCE ACHIEVED: " + player.getName() + " has mastered all twelve gods!");
    }
    
    private void removeNexusStructure(Location center) {
        // Remove the structure after convergence
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                for (int y = 0; y <= 6; y++) {
                    Location loc = center.clone().add(x, y, z);
                    if (loc.getBlock().getType() != Material.AIR) {
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }
        
        // Leave a small memorial
        center.getBlock().setType(Material.BEACON);
        center.clone().add(0, -1, 0).getBlock().setType(Material.NETHERITE_BLOCK);
    }
    
    public Location getPlayerNexusLocation(UUID playerId) {
        return playerNexusLocations.get(playerId);
    }
}