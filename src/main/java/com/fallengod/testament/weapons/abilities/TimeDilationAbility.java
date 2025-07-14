package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TimeDilationAbility extends WeaponAbility {
    
    public TimeDilationAbility() {
        super("Time Dilation", "Slows nearby enemies for 10 seconds", 30);
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        double radius = 10.0;
        
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
                // Apply slowness effect
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 3));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 2));
            }
        }
        
        player.sendMessage("§d§lTime Dilation! §7Time slows around you...");
        player.sendActionBar("§d⏰ Time flows at your command");
    }
}