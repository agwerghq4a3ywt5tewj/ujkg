package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodFrenzyAbility extends WeaponAbility {
    
    public BloodFrenzyAbility() {
        super("Blood Frenzy", "Damage increases as health decreases", 0);
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        double healthPercent = player.getHealth() / player.getMaxHealth();
        
        if (healthPercent <= 0.25) { // Below 25% health
            // Massive damage boost
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
            player.sendMessage("§4§lBLOOD FRENZY! §cYour rage knows no bounds!");
        } else if (healthPercent <= 0.5) { // Below 50% health
            // Moderate damage boost
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
            player.sendMessage("§c§lBlood Frenzy! §7Your wounds fuel your fury.");
        }
    }
}