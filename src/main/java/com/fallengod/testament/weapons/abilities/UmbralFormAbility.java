package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class UmbralFormAbility extends WeaponAbility {
    
    public UmbralFormAbility() {
        super("Umbral Form", "Invisibility when motionless in darkness", 0);
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        // Check if player is in darkness (light level < 7)
        int lightLevel = player.getLocation().getBlock().getLightLevel();
        
        if (lightLevel < 7) {
            // Apply invisibility
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, true, false));
            player.sendMessage("§8§lUmbral Form! §7You fade into the shadows...");
            player.sendActionBar("§8👤 One with the darkness");
        } else {
            player.sendMessage("§7You need to be in darkness to use Umbral Form.");
        }
    }
}