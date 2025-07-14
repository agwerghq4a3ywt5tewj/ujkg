package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VeilPhantomBoss extends GodBoss {
    
    private int phaseShiftCooldown = 0;
    
    public VeilPhantomBoss(TestamentPlugin plugin) {
        super(plugin, BossType.VEIL_PHANTOM, GodType.VEIL);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof Phantom phantom) {
            phantom.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            phantom.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
            phantom.setSize(5); // Larger phantom
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> phaseShift();
            case 2 -> realityTear();
            case 3 -> dimensionalEcho();
        }
    }
    
    private void phaseShift() {
        if (phaseShiftCooldown > 0) {
            phaseShiftCooldown--;
            return;
        }
        
        // Become temporarily invulnerable
        entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 100, 10)); // Near invulnerability
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
        
        // Teleport to random location
        Location currentLoc = entity.getLocation();
        Location newLoc = currentLoc.clone().add(
            (Math.random() - 0.5) * 30,
            Math.random() * 10 + 5,
            (Math.random() - 0.5) * 30
        );
        
        // Phase out effects
        entity.getWorld().spawnParticle(Particle.PORTAL, currentLoc, 50, 2, 2, 2, 0.2);
        entity.getWorld().playSound(currentLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 0.5f);
        
        // Teleport
        entity.teleport(newLoc);
        
        // Phase in effects
        entity.getWorld().spawnParticle(Particle.PORTAL, newLoc, 50, 2, 2, 2, 0.2);
        entity.getWorld().playSound(newLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 1.5f);
        
        phaseShiftCooldown = 60; // 3 second cooldown
        
        // Announce
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(newLoc) <= 30) {
                player.sendMessage("§8§lPhase Shift! §7The wraith bends reality to escape!");
            }
        }
    }
    
    private void realityTear() {
        // Create void zones that deal continuous damage
        Location center = entity.getLocation();
        
        for (int i = 0; i < 5; i++) {
            Location tearLoc = center.clone().add(
                (Math.random() - 0.5) * 20,
                -2,
                (Math.random() - 0.5) * 20
            );
            
            // Create void zone
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    Location voidLoc = tearLoc.clone().add(x, 0, z);
                    if (voidLoc.getBlock().getType() != Material.BEDROCK) {
                        voidLoc.getBlock().setType(Material.BLACK_CONCRETE);
                    }
                }
            }
            
            // Damage zone for 15 seconds
            final Location finalTearLoc = tearLoc;
            for (int tick = 0; tick < 300; tick += 20) { // Every second for 15 seconds
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    // Void particles
                    finalTearLoc.getWorld().spawnParticle(Particle.PORTAL, finalTearLoc.add(0, 1, 0), 20, 2, 1, 2, 0.1);
                    
                    // Damage players in void zone
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(finalTearLoc) <= 3) {
                            player.damage(4.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
                            player.sendMessage("§8§lReality Tear! §7The void consumes your essence!");
                        }
                    }
                }, tick);
            }
            
            // Remove void zone after 15 seconds
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        Location voidLoc = finalTearLoc.clone().add(x, 0, z);
                        if (voidLoc.getBlock().getType() == Material.BLACK_CONCRETE) {
                            voidLoc.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }, 300L);
        }
        
        entity.getWorld().playSound(center, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 0.5f);
    }
    
    private void dimensionalEcho() {
        // Spawn phantom copies
        Location center = entity.getLocation();
        
        for (int i = 0; i < 3; i++) {
            Location echoLoc = center.clone().add(
                (Math.random() - 0.5) * 15,
                Math.random() * 5,
                (Math.random() - 0.5) * 15
            );
            
            // Spawn phantom echo
            Phantom echo = (Phantom) echoLoc.getWorld().spawnEntity(echoLoc, org.bukkit.entity.EntityType.PHANTOM);
            echo.setSize(3);
            echo.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
            echo.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            echo.setCustomName("§8Dimensional Echo");
            echo.setCustomNameVisible(true);
            
            // Echo spawning effects
            echoLoc.getWorld().spawnParticle(Particle.END_ROD, echoLoc, 30, 1, 1, 1, 0.1);
            
            // Remove echo after 30 seconds
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!echo.isDead()) {
                    echo.getWorld().spawnParticle(Particle.PORTAL, echo.getLocation(), 20, 1, 1, 1, 0.1);
                    echo.remove();
                }
            }, 600L);
        }
        
        entity.getWorld().playSound(center, Sound.ENTITY_PHANTOM_AMBIENT, 2.0f, 0.8f);
        
        // Announce
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 30) {
                player.sendMessage("§8§lDimensional Echo! §7Reality fractures into multiple phantoms!");
            }
        }
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        
        // More frequent phasing
        phaseShiftCooldown = 0;
        
        // Reality distortion effects
        Location center = entity.getLocation();
        for (int radius = 5; radius <= 20; radius += 5) {
            for (double angle = 0; angle < 360; angle += 45) {
                double x = center.getX() + radius * Math.cos(Math.toRadians(angle));
                double z = center.getZ() + radius * Math.sin(Math.toRadians(angle));
                Location distortLoc = new Location(center.getWorld(), x, center.getY(), z);
                
                center.getWorld().spawnParticle(Particle.PORTAL, distortLoc, 5, 0.5, 0.5, 0.5, 0.1);
            }
        }
        
        entity.getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 100, 5, 5, 5, 0.2);
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        
        // Constant reality distortion
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                // Random reality tears
                if (Math.random() < 0.2) {
                    Location tearLoc = entity.getLocation().add(
                        (Math.random() - 0.5) * 25,
                        -1,
                        (Math.random() - 0.5) * 25
                    );
                    
                    tearLoc.getWorld().spawnParticle(Particle.PORTAL, tearLoc, 10, 1, 1, 1, 0.1);
                    
                    // Mini void zone
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(tearLoc) <= 2) {
                            player.damage(3.0);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
        
        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 200, 8, 8, 8, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Reality collapse
        entity.getWorld().spawnParticle(Particle.PORTAL, loc, 300, 20, 20, 20, 0.5);
        entity.getWorld().spawnParticle(Particle.END_ROD, loc, 200, 15, 15, 15, 0.3);
        entity.getWorld().playSound(loc, Sound.BLOCK_END_PORTAL_SPAWN, 3.0f, 0.5f);
        
        // Reality implosion effect
        for (int radius = 20; radius >= 1; radius--) {
            final int r = radius;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (double angle = 0; angle < 360; angle += 30) {
                    double x = loc.getX() + r * Math.cos(Math.toRadians(angle));
                    double z = loc.getZ() + r * Math.sin(Math.toRadians(angle));
                    Location implosionLoc = new Location(loc.getWorld(), x, loc.getY(), z);
                    
                    loc.getWorld().spawnParticle(Particle.PORTAL, implosionLoc, 5);
                }
            }, (20 - radius) * 2L);
        }
        
        // Drop void loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.ENDER_PEARL, 10));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.PHANTOM_MEMBRANE, 15));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 2));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§8§lREALITY WRAITH DEFEATED", "§7The veil between worlds is restored", 20, 80, 20);
            }
        }
    }
}