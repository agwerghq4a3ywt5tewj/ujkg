package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class BossListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public BossListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Track damage dealt to bosses
        if (event.getDamager() instanceof Player player) {
            GodBoss boss = plugin.getBossManager().getBoss(event.getEntity().getUniqueId());
            if (boss != null) {
                boss.addPlayerDamage(player, (int) event.getFinalDamage());
                
                // Show boss health
                double healthPercent = (boss.getEntity().getHealth() / 
                    boss.getEntity().getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue()) * 100;
                
                player.sendActionBar(boss.getBossType().getColoredName() + " §7- §c" + 
                    String.format("%.1f", healthPercent) + "% HP");
            }
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        GodBoss boss = plugin.getBossManager().getBoss(event.getEntity().getUniqueId());
        if (boss != null) {
            // Boss death is handled by the boss itself
            plugin.getBossManager().removeBoss(event.getEntity().getUniqueId());
            
            // Clear default drops
            event.getDrops().clear();
            event.setDroppedExp(500); // Massive XP reward
        }
    }
}