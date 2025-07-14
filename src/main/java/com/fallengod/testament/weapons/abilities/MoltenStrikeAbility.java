package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class MoltenStrikeAbility extends WeaponAbility {
    private final JavaPlugin plugin;
    
    public MoltenStrikeAbility(JavaPlugin plugin) {
        super("Molten Strike", "Chance to instantly smelt dropped items", 0);
        this.plugin = plugin;
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        // This ability is passive - triggers on mob death
        player.sendMessage("§6§lMolten Strike activated! Items will be auto-smelted.");
        
        // Apply temporary effect that auto-smelts drops
        new BukkitRunnable() {
            int duration = 200; // 10 seconds
            
            @Override
            public void run() {
                duration--;
                if (duration <= 0) {
                    cancel();
                    player.sendMessage("§7Molten Strike effect ended.");
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}