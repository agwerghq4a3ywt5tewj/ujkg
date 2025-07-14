package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemEffectListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Boolean> hasAbyssalTrident;
    private final Map<UUID, Boolean> hasSylvanBow;
    private final Map<UUID, Boolean> hasStormElytra;
    private final Map<UUID, Boolean> hasShadowCloak;
    private final Map<UUID, Boolean> hasShadowMantle;
    
    public ItemEffectListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.hasAbyssalTrident = new HashMap<>();
        this.hasSylvanBow = new HashMap<>();
        this.hasStormElytra = new HashMap<>();
        this.hasShadowCloak = new HashMap<>();
        this.hasShadowMantle = new HashMap<>();
        
        // Start checking task
        startItemCheckTask();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkAndApplyItemEffects(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        hasAbyssalTrident.remove(playerId);
        hasSylvanBow.remove(playerId);
        hasStormElytra.remove(playerId);
        hasShadowCloak.remove(playerId);
        hasShadowMantle.remove(playerId);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            // Check effects after inventory change
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                checkAndApplyItemEffects(player);
            }, 1L);
        }
    }
    
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        // Check effects after item drop
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            checkAndApplyItemEffects(event.getPlayer());
        }, 1L);
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // Check effects when changing held item
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            checkAndApplyItemEffects(event.getPlayer());
        }, 1L);
    }
    
    private void startItemCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    checkAndApplyItemEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 40L); // Every 2 seconds
    }
    
    private void checkAndApplyItemEffects(Player player) {
        UUID playerId = player.getUniqueId();
        
        boolean currentlyHasAbyssal = hasAbyssalTrident.getOrDefault(playerId, false);
        boolean currentlyHasSylvan = hasSylvanBow.getOrDefault(playerId, false);
        boolean currentlyHasStorm = hasStormElytra.getOrDefault(playerId, false);
        boolean currentlyHasShadow = hasShadowCloak.getOrDefault(playerId, false);
        boolean currentlyHasMantle = hasShadowMantle.getOrDefault(playerId, false);
        
        boolean nowHasAbyssal = hasAbyssalTridentInInventory(player);
        boolean nowHasSylvan = hasSylvanBowInInventory(player);
        boolean nowHasStorm = hasStormElytraInInventory(player);
        boolean nowHasShadow = hasShadowCloakInInventory(player);
        boolean nowHasMantle = hasShadowMantleInInventory(player);
        
        // Abyssal Trident effects
        if (nowHasAbyssal && !currentlyHasAbyssal) {
            applyAbyssalEffects(player);
            hasAbyssalTrident.put(playerId, true);
        } else if (!nowHasAbyssal && currentlyHasAbyssal) {
            removeAbyssalEffects(player);
            hasAbyssalTrident.put(playerId, false);
        }
        
        // Sylvan Bow effects
        if (nowHasSylvan && !currentlyHasSylvan) {
            applySylvanEffects(player);
            hasSylvanBow.put(playerId, true);
        } else if (!nowHasSylvan && currentlyHasSylvan) {
            removeSylvanEffects(player);
            hasSylvanBow.put(playerId, false);
        }
        
        // Storm Elytra effects
        if (nowHasStorm && !currentlyHasStorm) {
            applyStormEffects(player);
            hasStormElytra.put(playerId, true);
        } else if (!nowHasStorm && currentlyHasStorm) {
            removeStormEffects(player);
            hasStormElytra.put(playerId, false);
        }
        
        // Shadow Cloak effects (old item) - just track for umbral form
        if (nowHasShadow && !currentlyHasShadow) {
            hasShadowCloak.put(playerId, true);
            player.sendMessage("§8§lShadow Cloak equipped! §7Stand still in darkness to become invisible.");
        } else if (!nowHasShadow && currentlyHasShadow) {
            hasShadowCloak.put(playerId, false);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
        
        // Shadow Mantle effects (new item) - enhanced shadow abilities
        if (nowHasMantle && !currentlyHasMantle) {
            hasShadowMantle.put(playerId, true);
            player.sendMessage("§8§lShadow Mantle equipped! §7Enhanced shadow abilities unlocked.");
            player.sendMessage("§7• Stand still in darkness for Umbral Form");
            player.sendMessage("§7• Attack from behind for Shadow Strike");
        } else if (!nowHasMantle && currentlyHasMantle) {
            hasShadowMantle.put(playerId, false);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }
    }
    
    private boolean hasAbyssalTridentInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isAbyssalTrident(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasSylvanBowInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isSylvanBow(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasStormElytraInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isStormElytra(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasShadowCloakInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isShadowCloak(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasShadowMantleInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isShadowMantle(item)) {
                return true;
            }
        }
        return false;
    }
    
    private void applyAbyssalEffects(Player player) {
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, false, false));
    }
    
    private void removeAbyssalEffects(Player player) {
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
    }
    
    private void applySylvanEffects(Player player) {
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false));
    }
    
    private void removeSylvanEffects(Player player) {
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.SATURATION);
    }
    
    private void applyStormEffects(Player player) {
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2, false, false));
    }
    
    private void removeStormEffects(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
    }
}