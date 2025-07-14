package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VoidRipAbility extends WeaponAbility {
    
    public VoidRipAbility() {
        super("Void Rip", "Right-click to teleport 10 blocks forward", 5);
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        Location currentLoc = player.getLocation();
        Vector direction = currentLoc.getDirection().normalize();
        Location targetLoc = currentLoc.add(direction.multiply(10));
        
        // Ensure target location is safe
        if (targetLoc.getBlock().getType().isSolid()) {
            targetLoc.setY(targetLoc.getY() + 2);
        }
        
        // Create void particles at origin
        player.getWorld().spawnParticle(Particle.PORTAL, currentLoc, 50, 1, 1, 1, 0.1);
        
        // Teleport player
        player.teleport(targetLoc);
        
        // Create void particles at destination
        player.getWorld().spawnParticle(Particle.PORTAL, targetLoc, 50, 1, 1, 1, 0.1);
        
        // Play sound
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
        
        player.sendMessage("§8§lVoid Rip! §7You tear through reality itself.");
    }
}