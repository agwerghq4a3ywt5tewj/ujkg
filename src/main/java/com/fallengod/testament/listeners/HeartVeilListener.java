package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartVeilListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Boolean> testModeUsers;
    private final Map<UUID, Boolean> heartEffectsActive;
    private final Map<UUID, Boolean> veilEffectsActive;
    private final Map<UUID, Long> heartReturnTasks;
    private final Map<UUID, Boolean> heartWasPopped;
    
    public HeartVeilListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.testModeUsers = new HashMap<>();
        this.heartEffectsActive = new HashMap<>();
        this.veilEffectsActive = new HashMap<>();
        this.heartReturnTasks = new HashMap<>();
        this.heartWasPopped = new HashMap<>();
        
        // Start the effect checking task
        startEffectTask();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Check effects when player joins
        checkAndApplyEffects(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Check effects periodically when player moves
        if (event.getPlayer().getTicksLived() % 20 == 0) { // Every second
            checkAndApplyEffects(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // Check effects when player changes held item
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            checkAndApplyEffects(event.getPlayer());
        }, 1L);
    }
    
    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        // Check if the totem used was Heart of Fallen God
        ItemStack totemUsed = null;
        if (event.getHand() == org.bukkit.inventory.EquipmentSlot.HAND) {
            totemUsed = player.getInventory().getItemInMainHand();
        } else if (event.getHand() == org.bukkit.inventory.EquipmentSlot.OFF_HAND) {
            totemUsed = player.getInventory().getItemInOffHand();
        }
        
        if (totemUsed != null && SpecialItems.isHeartOfFallenGod(totemUsed)) {
            UUID playerId = player.getUniqueId();
            heartWasPopped.put(playerId, true);
            
            // Schedule heart return after 45 seconds
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                returnHeartToPlayer(player);
            }, 900L); // 45 seconds = 900 ticks
            
            player.sendMessage("§5§l❤ The Heart of the Fallen God has been consumed!");
            player.sendMessage("§7It will return to you in 45 seconds...");
            
            // Epic resurrection effects
            player.getWorld().spawnParticle(org.bukkit.Particle.SOUL, player.getLocation().add(0, 1, 0), 100, 2, 2, 2, 0.1);
            player.getWorld().spawnParticle(org.bukkit.Particle.SOUL_FIRE_FLAME, player.getLocation(), 50, 1, 1, 1, 0.05);
            player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_WITHER_SPAWN, 0.8f, 2.0f);
        }
    }
    
    private void returnHeartToPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Only return if heart was actually popped and player doesn't already have one
        if (!heartWasPopped.getOrDefault(playerId, false)) {
            return;
        }
        
        if (hasHeartOfFallenGod(player)) {
            player.sendMessage("§7You already have a Heart of the Fallen God.");
            heartWasPopped.put(playerId, false);
            return;
        }
        
        // Give heart back
        ItemStack heart = SpecialItems.createHeartOfFallenGod();
        if (player.getInventory().firstEmpty() == -1) {
            // Drop at player location if inventory is full
            player.getWorld().dropItemNaturally(player.getLocation(), heart);
            player.sendMessage("§5§l❤ The Heart of the Fallen God returns! (Dropped due to full inventory)");
        } else {
            player.getInventory().addItem(heart);
            player.sendMessage("§5§l❤ The Heart of the Fallen God returns to you!");
        }
        
        player.sendMessage("§7Death could not claim what is already yours...");
        
        // Epic return effects
        player.getWorld().spawnParticle(org.bukkit.Particle.SOUL, player.getLocation().add(0, 1, 0), 50, 1, 1, 1, 0.1);
        player.getWorld().spawnParticle(org.bukkit.Particle.ENCHANT, player.getLocation(), 30, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.BLOCK_BEACON_ACTIVATE, 0.5f, 2.0f);
        
        // Reset the popped status
        heartWasPopped.put(playerId, false);
        
        // Reapply heart effects if needed
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            checkAndApplyEffects(player);
        }, 5L);
    }
    
    private void startEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    checkAndApplyEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    private void checkAndApplyEffects(Player player) {
        boolean hasHeart = hasHeartOfFallenGod(player);
        boolean hasVeil = hasVeilOfNullification(player);
        boolean veilNullifies = hasVeil && isVeilNullifying(player);
        
        UUID playerId = player.getUniqueId();
        boolean currentHeartActive = heartEffectsActive.getOrDefault(playerId, false);
        boolean currentVeilActive = veilEffectsActive.getOrDefault(playerId, false);
        
        // Determine new states
        boolean shouldHaveHeartEffects = hasHeart && !veilNullifies;
        boolean shouldHaveVeilEffects = hasVeil;
        
        // Apply Heart effects
        if (shouldHaveHeartEffects && !currentHeartActive) {
            applyHeartEffects(player);
            heartEffectsActive.put(playerId, true);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                player.sendMessage("§5§l❤ Heart of Fallen God effects activated!");
                player.sendActionBar("§5❤ Heart Active | §7+15 Hearts, Strength, Regen, Resistance");
            }
        } else if (!shouldHaveHeartEffects && currentHeartActive) {
            removeHeartEffects(player);
            heartEffectsActive.put(playerId, false);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                if (veilNullifies) {
                    player.sendMessage("§8§l🌫 Veil nullifies Heart effects!");
                    player.sendActionBar("§8🌫 Veil Nullifying | §7Heart effects suppressed");
                } else {
                    player.sendMessage("§7Heart effects removed (no longer holding Heart)");
                    player.sendActionBar("§7No active testament effects");
                }
            }
        }
        
        // Apply Veil effects
        if (shouldHaveVeilEffects && !currentVeilActive) {
            applyVeilEffects(player);
            veilEffectsActive.put(playerId, true);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                player.sendMessage("§8§l🌫 Veil of Nullification effects activated!");
                if (hasHeart) {
                    player.sendActionBar("§8🌫 Veil Active | §7Nullifying Heart + Veil effects");
                } else {
                    player.sendActionBar("§8🌫 Veil Active | §7Slow Fall, Night Vision, Speed");
                }
            }
        } else if (!shouldHaveVeilEffects && currentVeilActive) {
            removeVeilEffects(player);
            veilEffectsActive.put(playerId, false);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                player.sendMessage("§7Veil effects removed (no longer holding Veil)");
                if (hasHeart) {
                    player.sendActionBar("§5❤ Heart Active | §7+15 Hearts, Strength, Regen, Resistance");
                } else {
                    player.sendActionBar("§7No active testament effects");
                }
            }
        }
    }
    
    private boolean hasHeartOfFallenGod(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isHeartOfFallenGod(item)) {
                // Check if player has Mace of Divine Forging - conflict prevention
                if (hasMaceOfDivineForging(player)) {
                    // Remove heart from inventory
                    player.getInventory().remove(item);
                    player.sendMessage("§c⚠ The Heart of Fallen God cannot coexist with the Mace of Divine Forging!");
                    player.sendMessage("§7The divine energies are incompatible...");
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean hasMaceOfDivineForging(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasCustomModelData() 
                && item.getItemMeta().getCustomModelData() == 100060) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasVeilOfNullification(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isVeilOfNullification(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isVeilNullifying(Player player) {
        if (!plugin.getConfigManager().isVeilEnabled()) {
            return false;
        }
        
        // Check if any nearby players have Heart effects that should be nullified
        double range = plugin.getConfigManager().getVeilRange();
        
        for (Player nearbyPlayer : player.getWorld().getPlayers()) {
            if (nearbyPlayer != player && 
                nearbyPlayer.getLocation().distance(player.getLocation()) <= range &&
                hasHeartOfFallenGod(nearbyPlayer)) {
                return true;
            }
        }
        
        // Also nullify own heart if player has both
        return hasHeartOfFallenGod(player);
    }
    
    private void applyHeartEffects(Player player) {
        if (!plugin.getConfigManager().isHeartEnabled()) {
            return;
        }
        
        // Set max health
        int extraHearts = plugin.getConfigManager().getHeartExtraHearts();
        player.setMaxHealth(20.0 + (extraHearts * 2.0));
        player.setHealth(player.getMaxHealth());
        
        // Apply potion effects
        int strengthLevel = plugin.getConfigManager().getHeartStrengthLevel();
        int regenLevel = plugin.getConfigManager().getHeartRegenerationLevel();
        int resistanceLevel = plugin.getConfigManager().getHeartResistanceLevel();
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthLevel - 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, regenLevel - 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, resistanceLevel - 1, false, false));
        
        // Schedule heart return after totem pop
        scheduleHeartReturn(player);
    }
    
    private void scheduleHeartReturn(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Cancel any existing return task
        Long existingTask = heartReturnTasks.get(playerId);
        if (existingTask != null) {
            plugin.getServer().getScheduler().cancelTask(existingTask.intValue());
        }
        
        // Schedule new return task for 45 seconds after totem pop
        int taskId = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // Check if player still doesn't have heart and had it popped
            if (!hasHeartOfFallenGod(player) && heartEffectsActive.getOrDefault(playerId, false)) {
                // Give heart back
                ItemStack heart = SpecialItems.createHeartOfFallenGod();
                player.getInventory().addItem(heart);
                player.sendMessage("§5§l❤ The Heart of the Fallen God returns to you!");
                player.sendMessage("§7Death could not claim what is already yours...");
                
                // Play dramatic return effects
                player.getWorld().playSound(player.getLocation(), org.bukkit.Sound.ENTITY_WITHER_SPAWN, 0.5f, 2.0f);
                player.getWorld().spawnParticle(org.bukkit.Particle.SOUL, player.getLocation().add(0, 1, 0), 50, 1, 1, 1, 0.1);
            }
            heartReturnTasks.remove(playerId);
        }, 900L).getTaskId(); // 45 seconds = 900 ticks
        
        heartReturnTasks.put(playerId, (long) taskId);
    }
    
    private void removeHeartEffects(Player player) {
        // Reset max health
        player.setMaxHealth(20.0);
        if (player.getHealth() > 20.0) {
            player.setHealth(20.0);
        }
        
        // Remove potion effects
        player.removePotionEffect(PotionEffectType.STRENGTH);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.RESISTANCE);
    }
    
    private void applyVeilEffects(Player player) {
        if (!plugin.getConfigManager().isVeilEnabled()) {
            return;
        }
        
        // Apply veil effects
        if (plugin.getConfigManager().getVeilSlowFalling()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false));
        }
        
        if (plugin.getConfigManager().getVeilNightVision()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
        
        int speedLevel = plugin.getConfigManager().getVeilSpeedLevel();
        if (speedLevel > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel - 1, false, false));
        }
    }
    
    private void removeVeilEffects(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SPEED);
    }
    
    public void setTestMode(UUID playerId, boolean enabled) {
        testModeUsers.put(playerId, enabled);
        
        if (!enabled) {
            // Clean up when disabling test mode
            heartEffectsActive.remove(playerId);
            veilEffectsActive.remove(playerId);
        }
    }
}