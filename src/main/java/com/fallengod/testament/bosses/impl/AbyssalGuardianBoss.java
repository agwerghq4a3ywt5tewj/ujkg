package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AbyssalGuardianBoss extends GodBoss {
    
    public AbyssalGuardianBoss(TestamentPlugin plugin) {
        super(plugin, BossType.ABYSSAL_GUARDIAN, GodType.ABYSSAL);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof ElderGuardian guardian) {
            guardian.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0));
            guardian.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            guardian.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> tidalWave();
            case 2 -> whirlpool();
            case 3 -> pressureCrush();
        }
    }
    
    private void tidalWave() {
        Location center = entity.getLocation();
        
        // Create massive water surge
        for (int radius = 1; radius <= 20; radius++) {
            final int r = radius;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (double angle = 0; angle < 360; angle += 15) {
                    double x = center.getX() + r * Math.cos(Math.toRadians(angle));
                    double z = center.getZ() + r * Math.sin(Math.toRadians(angle));
                    Location waveLoc = new Location(center.getWorld(), x, center.getY(), z);
                    
                    // Create water blocks temporarily
                    if (waveLoc.getBlock().getType() == Material.AIR) {
                        waveLoc.getBlock().setType(Material.WATER);
                        
                        // Remove water after 5 seconds
                        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                            if (waveLoc.getBlock().getType() == Material.WATER) {
                                waveLoc.getBlock().setType(Material.AIR);
                            }
                        }, 100L);
                    }
                    
                    // Damage and knockback players
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(waveLoc) <= 3) {
                            player.damage(12.0);
                            player.setVelocity(player.getLocation().subtract(center).toVector().normalize().multiply(2));
                            player.sendMessage("§3§lTidal Wave! §7You are swept away by the crushing waters!");
                        }
                    }
                    
                    // Water particles
                    center.getWorld().spawnParticle(Particle.BUBBLE, waveLoc, 10, 1, 1, 1, 0.1);
                }
            }, radius * 2L);
        }
        
        entity.getWorld().playSound(center, Sound.AMBIENT_UNDERWATER_LOOP, 3.0f, 0.5f);
    }
    
    private void whirlpool() {
        Location center = entity.getLocation();
        
        // Create whirlpool effect
        for (int i = 0; i < 100; i++) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (double radius = 1; radius <= 15; radius += 2) {
                    for (double angle = 0; angle < 360; angle += 30) {
                        double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                        double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                        Location particleLoc = new Location(center.getWorld(), x, center.getY(), z);
                        
                        center.getWorld().spawnParticle(Particle.BUBBLE, particleLoc, 1);
                    }
                }
                
                // Pull players toward center
                for (Player player : entity.getWorld().getPlayers()) {
                    if (player.getLocation().distance(center) <= 15) {
                        org.bukkit.util.Vector pullVector = center.subtract(player.getLocation()).toVector().normalize().multiply(0.3);
                        player.setVelocity(pullVector);
                        
                        if (player.getLocation().distance(center) <= 3) {
                            player.damage(8.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));
                        }
                    }
                }
            }, i * 2L);
        }
        
        entity.getWorld().playSound(center, Sound.BLOCK_WATER_AMBIENT, 2.0f, 0.8f);
    }
    
    private void pressureCrush() {
        // Massive damage based on water depth
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 25) {
                Location playerLoc = player.getLocation();
                int waterDepth = 0;
                
                // Calculate water depth above player
                for (int y = playerLoc.getBlockY(); y <= playerLoc.getBlockY() + 20; y++) {
                    Location checkLoc = new Location(playerLoc.getWorld(), playerLoc.getX(), y, playerLoc.getZ());
                    if (checkLoc.getBlock().getType() == Material.WATER) {
                        waterDepth++;
                    }
                }
                
                double damage = Math.max(5, waterDepth * 2);
                player.damage(damage);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 3));
                
                player.sendMessage("§3§lPressure Crush! §7The weight of the ocean crushes you!");
                
                // Crushing particles
                player.getWorld().spawnParticle(Particle.BUBBLE, player.getLocation(), 30, 1, 1, 1, 0.2);
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 2.0f, 0.5f);
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        
        // Summon water elementals (guardians)
        for (int i = 0; i < 3; i++) {
            Location spawnLoc = entity.getLocation().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            spawnLoc.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.GUARDIAN);
        }
        
        entity.getWorld().spawnParticle(Particle.BUBBLE, entity.getLocation(), 100, 3, 3, 3, 0.2);
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        
        // Create permanent water zones
        Location center = entity.getLocation();
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                if (Math.random() < 0.3) {
                    Location waterLoc = center.clone().add(x, 0, z);
                    if (waterLoc.getBlock().getType() == Material.AIR) {
                        waterLoc.getBlock().setType(Material.WATER);
                    }
                }
            }
        }
        
        entity.getWorld().spawnParticle(Particle.DRIPPING_WATER, entity.getLocation(), 200, 5, 5, 5, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Epic water explosion
        entity.getWorld().spawnParticle(Particle.BUBBLE, loc, 200, 10, 10, 10, 0.5);
        entity.getWorld().spawnParticle(Particle.DRIPPING_WATER, loc, 100, 5, 5, 5, 0.3);
        entity.getWorld().playSound(loc, Sound.AMBIENT_UNDERWATER_EXIT, 3.0f, 0.5f);
        
        // Drop loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.PRISMARINE_CRYSTALS, 15));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.SEA_LANTERN, 10));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 2));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§3§lLEVIATHAN DEFEATED", "§7The depths have been conquered", 20, 80, 20);
            }
        }
    }
}