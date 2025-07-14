package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PhaseStrikeAbility extends WeaponAbility {
    
    public PhaseStrikeAbility() {
        super("Phase Strike", "Attacks ignore armor and shields", 0);
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        // This is a passive ability that modifies damage calculation
        player.sendMessage("§8§lPhase Strike: Your attacks now ignore armor!");
        
        // Give a visual effect to show the ability is active
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, true, false));
    }
}