package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class TempestDragonBoss extends GodBoss {
    
    public TempestDragonBoss(TestamentPlugin plugin) {
        super(plugin, BossType.TEMPEST_DRAGON, GodType.TEMPEST);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof EnderDragon dragon) {
            dragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            dragon.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            dragon.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> lightningStorm();
            case 2 -> windShear();
            case 3 -> tornado();
        }
    }
    
    private void lightningStorm() {
        // Multiple lightning strikes across battlefield
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 40) {
                for (int i = 0; i < 5; i++) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        Location strikeLoc = player.getLocation().add(
                            (Math.random() - 0.5) * 20,
                            0,
                            (Math.random() - 0.5) * 20
                        );
                        
                        // Strike lightning
                        strikeLoc.getWorld().strikeLightning(strikeLoc);
                        
                        // Damage nearby players
                        for (Player nearbyPlayer : entity.getWorld().getPlayers()) {
                            if (nearbyPlayer.getLocation().distance(strikeLoc) <= 5) {
                                nearbyPlayer.damage(15.0);
                                nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));
                                nearbyPlayer.sendMessage("§e§lLightning Storm! §7Thunder and lightning rain down!");
                            }
                        }
                        
                    }, i * 20L);
                }
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3.0f, 1.0f);
    }
    
    private void windShear() {
        // Powerful knockback attacks
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 25) {
                // Calculate knockback direction
                Vector knockback = player.getLocation().subtract(entity.getLocation()).toVector().normalize();
                knockback.multiply(3.0); // Strong knockback
                knockback.setY(1.5); // Upward component
                
                player.setVelocity(knockback);
                player.damage(10.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1));
                player.sendMessage("§e§lWind Shear! §7Powerful winds hurl you through the air!");
                
                // Wind particles
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 20, 2, 2, 2, 0.2);
                player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, player.getLocation(), 10, 1, 1, 1, 0.1);
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ITEM_ELYTRA_FLYING, 2.0f, 0.5f);
    }
    
    private void tornado() {
        // Create moving vortex of destruction
        Location center = entity.getLocation();
        
        for (int duration = 0; duration < 200; duration++) { // 10 seconds
            final int tick = duration;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                // Move tornado in a pattern
                double angle = tick * 0.1;
                Location tornadoCenter = center.clone().add(
                    Math.cos(angle) * 10,
                    0,
                    Math.sin(angle) * 10
                );
                
                // Create tornado visual
                for (int height = 0; height <= 15; height++) {
                    for (double spiralAngle = 0; spiralAngle < 360; spiralAngle += 30) {
                        double radius = 3 - (height * 0.15); // Narrowing spiral
                        double x = tornadoCenter.getX() + radius * Math.cos(Math.toRadians(spiralAngle + tick * 10));
                        double z = tornadoCenter.getZ() + radius * Math.sin(Math.toRadians(spiralAngle + tick * 10));
                        
                        Location particleLoc = new Location(tornadoCenter.getWorld(), x, tornadoCenter.getY() + height, z);
                        tornadoCenter.getWorld().spawnParticle(Particle.CLOUD, particleLoc, 1);
                    }
                }
                
                // Damage and pull players
                for (Player player : entity.getWorld().getPlayers()) {
                    if (player.getLocation().distance(tornadoCenter) <= 8) {
                        // Pull toward tornado
                        Vector pullVector = tornadoCenter.subtract(player.getLocation()).toVector().normalize().multiply(0.5);
                        pullVector.setY(0.3); // Upward pull
                        player.setVelocity(pullVector);
                        
                        if (player.getLocation().distance(tornadoCenter) <= 3) {
                            player.damage(5.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                        }
                    }
                }
                
                // Sound effect
                if (tick % 20 == 0) {
                    tornadoCenter.getWorld().playSound(tornadoCenter, Sound.ENTITY_PHANTOM_FLAP, 1.0f, 0.5f);
                }
                
            }, duration);
        }
        
        // Announce tornado
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 50) {
                player.sendMessage("§e§lTornado! §7A devastating vortex tears across the battlefield!");
            }
        }
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
        
        // Fly higher and become more aggressive
        if (entity instanceof EnderDragon dragon) {
            // Dragon flies higher and more frequently
            Location highLoc = entity.getLocation().add(0, 20, 0);
            entity.teleport(highLoc);
        }
        
        entity.getWorld().spawnParticle(Particle.CLOUD, entity.getLocation(), 100, 5, 5, 5, 0.2);
        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getLocation(), 50, 3, 3, 3, 0.1);
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        
        // More frequent lightning and wind immunity
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                // Random lightning strikes
                if (Math.random() < 0.3) {
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(entity.getLocation()) <= 30) {
                            Location strikeLoc = player.getLocation().add(
                                (Math.random() - 0.5) * 10,
                                0,
                                (Math.random() - 0.5) * 10
                            );
                            strikeLoc.getWorld().strikeLightning(strikeLoc);
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L); // Every 2 seconds
        
        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, entity.getLocation(), 200, 8, 8, 8, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Epic storm death
        entity.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 200, 15, 15, 15, 0.5);
        entity.getWorld().spawnParticle(Particle.CLOUD, loc, 100, 10, 10, 10, 0.3);
        entity.getWorld().playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 3.0f, 0.5f);
        
        // Final lightning strikes
        for (int i = 0; i < 10; i++) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Location strikeLoc = loc.clone().add(
                    (Math.random() - 0.5) * 30,
                    0,
                    (Math.random() - 0.5) * 30
                );
                strikeLoc.getWorld().strikeLightning(strikeLoc);
            }, i * 5L);
        }
        
        // Drop storm loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(org.bukkit.Material.LIGHTNING_ROD, 5));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(org.bukkit.Material.ELYTRA, 1));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(org.bukkit.Material.NETHER_STAR, 3));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§e§lSTORM SOVEREIGN DEFEATED", "§7The tempest has been calmed", 20, 80, 20);
            }
        }
    }
}