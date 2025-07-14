package com.fallengod.testament.weapons.abilities;

import com.fallengod.testament.weapons.WeaponAbility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class SonicBoomAbility extends WeaponAbility {
    
    public SonicBoomAbility() {
        super("Sonic Boom", "Shatters blocks in cone shape", 15);
    }
    
    @Override
    public void activate(Player player, ItemStack weapon) {
        Location playerLoc = player.getLocation();
        Vector direction = playerLoc.getDirection();
        
        // Create cone of destruction
        for (int distance = 1; distance <= 8; distance++) {
            for (int angle = -30; angle <= 30; angle += 10) {
                Vector rotated = rotateVector(direction, angle);
                Location targetLoc = playerLoc.clone().add(rotated.multiply(distance));
                
                Block block = targetLoc.getBlock();
                if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                    // Break block with particles
                    player.getWorld().spawnParticle(Particle.BLOCK, targetLoc, 10, block.getBlockData());
                    block.breakNaturally();
                }
            }
        }
        
        // Sound and visual effects
        player.getWorld().playSound(playerLoc, Sound.ENTITY_WARDEN_SONIC_BOOM, 2.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, playerLoc, 1);
        
        player.sendMessage("§b§lSonic Boom! §7Reality shatters before you.");
    }
    
    private Vector rotateVector(Vector vector, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        
        return new Vector(x, vector.getY(), z);
    }
}