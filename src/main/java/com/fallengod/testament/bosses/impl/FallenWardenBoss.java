package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FallenWardenBoss extends GodBoss {
    
    public FallenWardenBoss(TestamentPlugin plugin) {
        super(plugin, BossType.FALLEN_WARDEN, GodType.FALLEN);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof Warden warden) {
            // Enhanced warden abilities
            warden.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            warden.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
            warden.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        Location loc = entity.getLocation();
        
        switch (phase) {
            case 1 -> soulDrain();
            case 2 -> deathPulse();
            case 3 -> necroticExplosion();
        }
    }
    
    private void soulDrain() {
        // Drain health from nearby players
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 15) {
                double damage = 4.0 * phase;
                player.damage(damage);
                entity.setHealth(Math.min(entity.getHealth() + damage, 
                    entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue()));
                
                // Visual effects
                player.getWorld().spawnParticle(Particle.SOUL, player.getLocation(), 10, 1, 1, 1, 0.1);
                player.sendMessage("§5§lSoul Drain! §7Your life force is being consumed!");
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 2.0f, 0.5f);
    }
    
    private void deathPulse() {
        // Create expanding ring of death
        for (int radius = 1; radius <= 20; radius++) {
            final int r = radius;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (double angle = 0; angle < 360; angle += 10) {
                    double x = entity.getLocation().getX() + r * Math.cos(Math.toRadians(angle));
                    double z = entity.getLocation().getZ() + r * Math.sin(Math.toRadians(angle));
                    Location particleLoc = new Location(entity.getWorld(), x, entity.getLocation().getY(), z);
                    
                    entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, particleLoc, 1);
                    
                    // Damage players in the ring
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(particleLoc) <= 2) {
                            player.damage(6.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                        }
                    }
                }
            }, radius * 2L);
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WITHER_SHOOT, 2.0f, 0.8f);
    }
    
    private void necroticExplosion() {
        // Massive explosion that spawns undead
        Location loc = entity.getLocation();
        
        // Main explosion
        entity.getWorld().spawnParticle(Particle.EXPLOSION, loc, 20, 5, 5, 5, 0.2);
        entity.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 0.5f);
        
        // Damage all nearby players
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 25) {
                double distance = player.getLocation().distance(loc);
                double damage = Math.max(1, 15 - distance);
                player.damage(damage);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 2));
            }
        }
        
        // Spawn undead minions
        for (int i = 0; i < 5; i++) {
            Location spawnLoc = loc.clone().add(
                (Math.random() - 0.5) * 20,
                0,
                (Math.random() - 0.5) * 20
            );
            spawnLoc.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.WITHER_SKELETON);
        }
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        entity.getWorld().spawnParticle(Particle.SOUL, entity.getLocation(), 100, 3, 3, 3, 0.2);
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        entity.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, entity.getLocation(), 200, 5, 5, 5, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Epic death effects
        entity.getWorld().spawnParticle(Particle.EXPLOSION, loc, 50, 10, 10, 10, 0.5);
        entity.getWorld().playSound(loc, Sound.ENTITY_WITHER_DEATH, 3.0f, 0.5f);
        
        // Drop special loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 3));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHERITE_INGOT, 5));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§5§lFALLEN WARDEN DEFEATED", "§7The guardian of death has fallen", 20, 80, 20);
                player.sendMessage("§6§l⚔ The Corrupted Soul Warden has been vanquished! ⚔");
            }
        }
    }
}