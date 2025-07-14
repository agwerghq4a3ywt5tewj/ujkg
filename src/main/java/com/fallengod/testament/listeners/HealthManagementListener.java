package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.SpecialItems;
import com.fallengod.testament.items.ConvergenceItems;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HealthManagementListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Boolean> hasHeartEffects;
    private final Map<UUID, Boolean> invincibilityActive;
    private final Map<UUID, Long> invincibilityCooldowns;
    private final Map<UUID, BukkitTask> invincibilityTasks;
    private final Map<UUID, BukkitTask> actionBarTasks;
    
    public HealthManagementListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.hasHeartEffects = new HashMap<>();
        this.invincibilityActive = new HashMap<>();
        this.invincibilityCooldowns = new HashMap<>();
        this.invincibilityTasks = new HashMap<>();
        this.actionBarTasks = new HashMap<>();
        
        // Start health checking task
        startHealthCheckTask();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkPlayerHealth(event.getPlayer());
        checkConvergenceHelmet(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        hasHeartEffects.remove(playerId);
        invincibilityActive.remove(playerId);
        invincibilityCooldowns.remove(playerId);
        
        // Cancel any running tasks
        BukkitTask invincibilityTask = invincibilityTasks.remove(playerId);
        if (invincibilityTask != null) {
            invincibilityTask.cancel();
        }
        
        BukkitTask actionBarTask = actionBarTasks.remove(playerId);
        if (actionBarTask != null) {
            actionBarTask.cancel();
        }
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        
        // Check if Heart of Fallen God was dropped
        if (SpecialItems.isHeartOfFallenGod(droppedItem)) {
            removeHeartEffects(player);
            player.sendMessage("§5§lHeart of Fallen God §7effects removed!");
        }
        
        // Check if Convergence Crown was dropped
        if (ConvergenceItems.isNexusCrown(droppedItem)) {
            cancelInvincibility(player);
            player.sendMessage("§6§lCrown of Divine Convergence §7removed!");
        }
    }
    
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        ItemStack pickedItem = event.getItem().getItemStack();
        
        // Check if Heart of Fallen God was picked up
        if (SpecialItems.isHeartOfFallenGod(pickedItem)) {
            // Check for Mace conflict
            if (hasMaceOfDivineForging(player)) {
                event.setCancelled(true);
                player.sendMessage("§c⚠ The Heart of Fallen God cannot coexist with the Mace of Divine Forging!");
                player.sendMessage("§7Drop the Mace first to pick up the Heart.");
                return;
            }
            
            // Schedule heart effects application after pickup
            new BukkitRunnable() {
                @Override
                public void run() {
                    applyHeartEffects(player);
                    player.sendMessage("§5§lHeart of Fallen God §7effects restored!");
                }
            }.runTaskLater(plugin, 1L);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }
        
        // Check for Heart and Mace conflict when moving items
        if (SpecialItems.isHeartOfFallenGod(clickedItem) && hasMaceOfDivineForging(player)) {
            event.setCancelled(true);
            player.sendMessage("§c⚠ The Heart of Fallen God cannot coexist with the Mace of Divine Forging!");
            return;
        }
        
        if (isMaceOfDivineForging(clickedItem) && hasHeartOfFallenGod(player)) {
            event.setCancelled(true);
            player.sendMessage("§c⚠ The Mace of Divine Forging cannot coexist with the Heart of Fallen God!");
            return;
        }
        
        // Check for Convergence Crown equip
        if (ConvergenceItems.isNexusCrown(clickedItem) && event.getSlot() == 39) { // Helmet slot
            new BukkitRunnable() {
                @Override
                public void run() {
                    checkConvergenceHelmet(player);
                }
            }.runTaskLater(plugin, 1L);
        }
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // If invincibility is active, prevent holding items
        if (invincibilityActive.getOrDefault(playerId, false)) {
            ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
            if (newItem != null && newItem.getType() != Material.AIR) {
                event.setCancelled(true);
                player.sendMessage("§c⚠ You cannot hold items during Divine Invincibility!");
                player.sendActionBar("§6✦ Divine Invincibility: Fists Only ✦");
            }
        }
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        
        UUID playerId = player.getUniqueId();
        
        // Check if player has invincibility active and is using fists
        if (invincibilityActive.getOrDefault(playerId, false)) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            
            if (heldItem == null || heldItem.getType() == Material.AIR) {
                // Massive fist damage during invincibility
                double originalDamage = event.getDamage();
                event.setDamage(originalDamage * 5.0); // 5x damage
                
                // Epic effects
                player.getWorld().spawnParticle(Particle.CRIT, event.getEntity().getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0.2);
                player.getWorld().spawnParticle(Particle.ENCHANT, event.getEntity().getLocation(), 20, 1, 1, 1, 0.1);
                player.getWorld().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 2.0f, 0.5f);
                
                player.sendActionBar("§6✦ DIVINE FIST: " + String.format("%.1f", event.getDamage()) + " damage ✦");
            } else {
                // Cancel attack if holding an item
                event.setCancelled(true);
                player.sendMessage("§c⚠ You can only attack with fists during Divine Invincibility!");
            }
        }
        
        // Check if target has invincibility
        if (event.getEntity() instanceof Player target) {
            UUID targetId = target.getUniqueId();
            if (invincibilityActive.getOrDefault(targetId, false)) {
                event.setCancelled(true);
                player.sendMessage("§6" + target.getName() + " is protected by Divine Invincibility!");
                
                // Show immunity effect
                target.getWorld().spawnParticle(Particle.ENCHANT, target.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }
    
    private void startHealthCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    checkPlayerHealth(player);
                    checkConvergenceHelmet(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 40L); // Every 2 seconds
    }
    
    private void checkPlayerHealth(Player player) {
        UUID playerId = player.getUniqueId();
        boolean currentlyHasHeart = hasHeartOfFallenGod(player);
        boolean previouslyHadEffects = hasHeartEffects.getOrDefault(playerId, false);
        
        if (currentlyHasHeart && !previouslyHadEffects) {
            // Check for Mace conflict
            if (hasMaceOfDivineForging(player)) {
                // Remove Heart from inventory
                removeHeartFromInventory(player);
                player.sendMessage("§c⚠ The Heart of Fallen God cannot coexist with the Mace of Divine Forging!");
                return;
            }
            
            applyHeartEffects(player);
            hasHeartEffects.put(playerId, true);
        } else if (!currentlyHasHeart && previouslyHadEffects) {
            removeHeartEffects(player);
            hasHeartEffects.put(playerId, false);
        }
    }
    
    private void checkConvergenceHelmet(Player player) {
        UUID playerId = player.getUniqueId();
        ItemStack helmet = player.getInventory().getHelmet();
        
        boolean hasHelmet = helmet != null && ConvergenceItems.isNexusCrown(helmet);
        boolean currentlyInvincible = invincibilityActive.getOrDefault(playerId, false);
        
        if (hasHelmet && !currentlyInvincible) {
            // Check cooldown
            long currentTime = System.currentTimeMillis();
            Long lastActivation = invincibilityCooldowns.get(playerId);
            
            if (lastActivation != null && (currentTime - lastActivation) < 300000) { // 5 minutes
                long remainingTime = 300000 - (currentTime - lastActivation);
                long remainingMinutes = remainingTime / 60000;
                long remainingSeconds = (remainingTime % 60000) / 1000;
                
                player.sendMessage("§c⚠ Divine Invincibility is on cooldown!");
                player.sendMessage("§7Time remaining: " + remainingMinutes + "m " + remainingSeconds + "s");
                return;
            }
            
            activateInvincibility(player);
        } else if (!hasHelmet && currentlyInvincible) {
            cancelInvincibility(player);
        }
    }
    
    private void activateInvincibility(Player player) {
        UUID playerId = player.getUniqueId();
        
        invincibilityActive.put(playerId, true);
        invincibilityCooldowns.put(playerId, System.currentTimeMillis());
        
        // Clear held item
        player.getInventory().setItemInMainHand(null);
        player.getInventory().setItemInOffHand(null);
        
        // Epic activation effects
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 100, 2, 2, 2, 0.2);
        player.getWorld().spawnParticle(Particle.FIREWORK, player.getLocation(), 50, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 1.5f);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 2.0f, 0.8f);
        
        // Messages
        player.sendTitle("§6§lDIVINE INVINCIBILITY", "§7You are untouchable for 2 minutes", 20, 60, 20);
        player.sendMessage("§6§l✦ Divine Invincibility Activated! ✦");
        player.sendMessage("§7• Complete immunity to all damage");
        player.sendMessage("§7• Fist attacks deal 5x damage");
        player.sendMessage("§7• Cannot hold items");
        player.sendMessage("§7• Duration: 2 minutes");
        
        // Server announcement
        plugin.getServer().broadcastMessage("§6⚡ " + player.getName() + " has activated Divine Invincibility! ⚡");
        
        // Start invincibility timer
        BukkitTask invincibilityTask = new BukkitRunnable() {
            @Override
            public void run() {
                cancelInvincibility(player);
                player.sendMessage("§6§lDivine Invincibility §7has ended.");
                player.sendTitle("§7Invincibility Ended", "§8You are mortal once again", 10, 40, 10);
            }
        }.runTaskLater(plugin, 2400L); // 2 minutes = 2400 ticks
        
        invincibilityTasks.put(playerId, invincibilityTask);
        
        // Start action bar countdown
        startActionBarCountdown(player);
    }
    
    private void cancelInvincibility(Player player) {
        UUID playerId = player.getUniqueId();
        
        invincibilityActive.put(playerId, false);
        
        // Cancel tasks
        BukkitTask invincibilityTask = invincibilityTasks.remove(playerId);
        if (invincibilityTask != null) {
            invincibilityTask.cancel();
        }
        
        BukkitTask actionBarTask = actionBarTasks.remove(playerId);
        if (actionBarTask != null) {
            actionBarTask.cancel();
        }
        
        // Effects
        player.getWorld().spawnParticle(Particle.SMOKE, player.getLocation().add(0, 1, 0), 30, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
    }
    
    private void startActionBarCountdown(Player player) {
        UUID playerId = player.getUniqueId();
        
        BukkitTask actionBarTask = new BukkitRunnable() {
            int timeLeft = 120; // 2 minutes in seconds
            
            @Override
            public void run() {
                if (!invincibilityActive.getOrDefault(playerId, false)) {
                    cancel();
                    return;
                }
                
                if (timeLeft <= 0) {
                    cancel();
                    return;
                }
                
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                
                String timeDisplay = String.format("%d:%02d", minutes, seconds);
                player.sendActionBar("§6✦ Divine Invincibility: " + timeDisplay + " ✦");
                
                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
        
        actionBarTasks.put(playerId, actionBarTask);
    }
    
    private void applyHeartEffects(Player player) {
        int extraHearts = plugin.getConfigManager().getHeartExtraHearts();
        player.setMaxHealth(20.0 + (extraHearts * 2.0));
        player.setHealth(player.getMaxHealth());
    }
    
    private void removeHeartEffects(Player player) {
        player.setMaxHealth(20.0);
        if (player.getHealth() > 20.0) {
            player.setHealth(20.0);
        }
    }
    
    private boolean hasHeartOfFallenGod(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isHeartOfFallenGod(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasMaceOfDivineForging(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (isMaceOfDivineForging(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isMaceOfDivineForging(ItemStack item) {
        return item != null && item.hasItemMeta() && 
               item.getItemMeta().hasCustomModelData() && 
               item.getItemMeta().getCustomModelData() == 100060;
    }
    
    private void removeHeartFromInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isHeartOfFallenGod(item)) {
                player.getInventory().remove(item);
                break;
            }
        }
    }
}