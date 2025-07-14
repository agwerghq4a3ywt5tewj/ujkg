package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.ConvergenceItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConvergenceEffectListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Boolean> hasConvergenceItems;
    
    public ConvergenceEffectListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.hasConvergenceItems = new HashMap<>();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkConvergenceItems(event.getPlayer());
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        // Check if player has Crown of Divine Convergence
        if (hasNexusCrown(player)) {
            // Complete immunity to all damage
            event.setCancelled(true);
            
            // Show immunity message occasionally
            if (player.getTicksLived() % 100 == 0) {
                player.sendActionBar("§6✦ Divine Immunity ✦ §7No force can harm you");
            }
        }
    }
    
    private void checkConvergenceItems(Player player) {
        boolean hasItems = hasNexusCrown(player) || hasOmnipotentScepter(player) || hasDivineCodex(player);
        boolean previouslyHadItems = hasConvergenceItems.getOrDefault(player.getUniqueId(), false);
        
        if (hasItems && !previouslyHadItems) {
            applyConvergenceEffects(player);
            hasConvergenceItems.put(player.getUniqueId(), true);
        } else if (!hasItems && previouslyHadItems) {
            removeConvergenceEffects(player);
            hasConvergenceItems.put(player.getUniqueId(), false);
        }
    }
    
    private void applyConvergenceEffects(Player player) {
        // Set maximum health to 40 (20 hearts)
        player.setMaxHealth(40.0);
        player.setHealth(40.0);
        
        // Grant creative flight
        player.setAllowFlight(true);
        
        // Apply ultimate effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 9, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 4, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        
        player.sendMessage("§6§l✦ Divine Convergence effects activated! ✦");
        player.sendMessage("§7You wield the power of all twelve gods!");
    }
    
    private void removeConvergenceEffects(Player player) {
        // Reset health to normal
        player.setMaxHealth(20.0);
        if (player.getHealth() > 20.0) {
            player.setHealth(20.0);
        }
        
        // Remove flight if not in creative mode
        if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
        
        // Remove convergence effects
        player.removePotionEffect(PotionEffectType.STRENGTH);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.RESISTANCE);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        
        player.sendMessage("§7Divine Convergence effects removed.");
    }
    
    private boolean hasNexusCrown(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (ConvergenceItems.isNexusCrown(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasOmnipotentScepter(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (ConvergenceItems.isOmnipotentScepter(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasDivineCodex(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (ConvergenceItems.isDivineCodex(item)) {
                return true;
            }
        }
        return false;
    }
}