package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VoidStalkerBoss extends GodBoss {
    
    public VoidStalkerBoss(TestamentPlugin plugin) {
        super(plugin, BossType.VOID_STALKER, GodType.VOID);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof Enderman enderman) {
            enderman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            enderman.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
            enderman.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
            enderman.setCustomName("§0§lHarbinger of Nothingness");
            enderman.setCustomNameVisible(true);
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> voidRip();
            case 2 -> realityErasure();
            case 3 -> nullZone();
        }
    }
    
    private void voidRip() {
        // Teleports players to dangerous locations
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 20) {
                // Find dangerous teleport location
                Location dangerLoc = findDangerousLocation(player);
                
                // Void rip effects at origin
                player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 1, 1, 1, 0.2);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 0.5f);
                
                // Teleport player
                player.teleport(dangerLoc);
                
                // Void rip effects at destination
                player.getWorld().spawnParticle(Particle.PORTAL, dangerLoc, 50, 1, 1, 1, 0.2);
                player.getWorld().playSound(dangerLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 1.5f);
                
                // Damage and disorient
                player.damage(10.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                player.sendMessage("§0§lVoid Rip! §7You are torn through the fabric of reality!");
                
                break; // Only target one player per use
            }
        }
    }
    
    private Location findDangerousLocation(Player player) {
        Location playerLoc = player.getLocation();
        
        // Try to find a location with hazards nearby
        for (int attempts = 0; attempts < 10; attempts++) {
            Location testLoc = playerLoc.clone().add(
                (Math.random() - 0.5) * 40,
                Math.random() * 20 - 10,
                (Math.random() - 0.5) * 40
            );
            
            // Ensure location is safe to teleport to (not in solid blocks)
            while (testLoc.getBlock().getType().isSolid() && testLoc.getY() < 256) {
                testLoc.add(0, 1, 0);
            }
            
            // Check if location has hazards nearby (lava, void, etc.)
            boolean hasDanger = false;
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    for (int z = -3; z <= 3; z++) {
                        Material block = testLoc.clone().add(x, y, z).getBlock().getType();
                        if (block == Material.LAVA || block == Material.FIRE || testLoc.getY() < 10) {
                            hasDanger = true;
                            break;
                        }
                    }
                }
            }
            
            if (hasDanger) {
                return testLoc;
            }
        }
        
        // Fallback: teleport high in the air
        return playerLoc.clone().add(0, 20, 0);
    }
    
    private void realityErasure() {
        // Removes blocks from the battlefield
        Location center = entity.getLocation();
        
        for (int i = 0; i < 20; i++) {
            Location eraseLoc = center.clone().add(
                (Math.random() - 0.5) * 30,
                Math.random() * 10 - 5,
                (Math.random() - 0.5) * 30
            );
            
            // Erase blocks in a small area
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Location blockLoc = eraseLoc.clone().add(x, y, z);
                        if (blockLoc.getBlock().getType() != Material.BEDROCK && 
                            blockLoc.getBlock().getType() != Material.AIR) {
                            
                            // Store original block for restoration
                            Material originalBlock = blockLoc.getBlock().getType();
                            blockLoc.getBlock().setType(Material.AIR);
                            
                            // Void particles
                            blockLoc.getWorld().spawnParticle(Particle.PORTAL, blockLoc, 5, 0.5, 0.5, 0.5, 0.1);
                            
                            // Restore block after 20 seconds
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                if (blockLoc.getBlock().getType() == Material.AIR) {
                                    blockLoc.getBlock().setType(originalBlock);
                                }
                            }, 400L);
                        }
                    }
                }
            }
        }
        
        entity.getWorld().playSound(center, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 0.3f);
        
        // Announce
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 35) {
                player.sendMessage("§0§lReality Erasure! §7The void consumes the battlefield!");
            }
        }
    }
    
    private void nullZone() {
        // Creates areas where abilities don't work
        Location center = entity.getLocation();
        
        for (int i = 0; i < 3; i++) {
            Location nullLoc = center.clone().add(
                (Math.random() - 0.5) * 25,
                0,
                (Math.random() - 0.5) * 25
            );
            
            // Create null zone marker
            for (int x = -3; x <= 3; x++) {
                for (int z = -3; z <= 3; z++) {
                    if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                        Location markerLoc = nullLoc.clone().add(x, 0, z);
                        if (markerLoc.getBlock().getType() == Material.AIR) {
                            markerLoc.getBlock().setType(Material.BLACK_CONCRETE);
                        }
                    }
                }
            }
            
            // Null zone effects for 25 seconds
            final Location finalNullLoc = nullLoc;
            for (int tick = 0; tick < 500; tick += 40) { // Every 2 seconds for 25 seconds
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    // Void particles
                    finalNullLoc.getWorld().spawnParticle(Particle.PORTAL, finalNullLoc.add(0, 1, 0), 15, 3, 1, 3, 0.1);
                    
                    // Disable abilities for players in null zone
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(finalNullLoc) <= 4) {
                            // Remove all potion effects
                            for (PotionEffectType effectType : PotionEffectType.values()) {
                                if (effectType != null) {
                                    player.removePotionEffect(effectType);
                                }
                            }
                            
                            // Apply null zone debuffs
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 2));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 60, 3));
                            
                            if (player.getTicksLived() % 40 == 0) { // Every 2 seconds
                                player.sendMessage("§0§lNull Zone! §7Your abilities are suppressed by the void!");
                            }
                        }
                    }
                }, tick);
            }
            
            // Remove null zone markers after 25 seconds
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (int x = -3; x <= 3; x++) {
                    for (int z = -3; z <= 3; z++) {
                        if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                            Location markerLoc = finalNullLoc.clone().add(x, 0, z);
                            if (markerLoc.getBlock().getType() == Material.BLACK_CONCRETE) {
                                markerLoc.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }
            }, 500L);
        }
        
        entity.getWorld().playSound(center, Sound.ENTITY_WITHER_AMBIENT, 2.0f, 0.5f);
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        
        // More frequent teleportation
        new org.bukkit.scheduler.BukkitRunnable() {
            int teleportCount = 0;
            
            @Override
            public void run() {
                if (!isAlive() || teleportCount >= 10) {
                    cancel();
                    return;
                }
                
                // Teleport to random location
                Location currentLoc = entity.getLocation();
                Location newLoc = currentLoc.clone().add(
                    (Math.random() - 0.5) * 40,
                    Math.random() * 15,
                    (Math.random() - 0.5) * 40
                );
                
                // Teleport effects
                entity.getWorld().spawnParticle(Particle.PORTAL, currentLoc, 30, 2, 2, 2, 0.2);
                entity.teleport(newLoc);
                entity.getWorld().spawnParticle(Particle.PORTAL, newLoc, 30, 2, 2, 2, 0.2);
                entity.getWorld().playSound(newLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.8f);
                
                teleportCount++;
            }
        }.runTaskTimer(plugin, 0L, 60L); // Every 3 seconds
        
        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 100, 5, 5, 5, 0.2);
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
        
        // Larger void zones and more frequent reality erasure
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                // Random reality erasure
                if (Math.random() < 0.3) {
                    Location eraseLoc = entity.getLocation().add(
                        (Math.random() - 0.5) * 35,
                        Math.random() * 10 - 5,
                        (Math.random() - 0.5) * 35
                    );
                    
                    // Erase larger area
                    for (int x = -2; x <= 2; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -2; z <= 2; z++) {
                                Location blockLoc = eraseLoc.clone().add(x, y, z);
                                if (blockLoc.getBlock().getType() != Material.BEDROCK && 
                                    blockLoc.getBlock().getType() != Material.AIR) {
                                    
                                    Material originalBlock = blockLoc.getBlock().getType();
                                    blockLoc.getBlock().setType(Material.AIR);
                                    blockLoc.getWorld().spawnParticle(Particle.PORTAL, blockLoc, 3, 0.3, 0.3, 0.3, 0.1);
                                    
                                    // Restore after 15 seconds
                                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                        if (blockLoc.getBlock().getType() == Material.AIR) {
                                            blockLoc.getBlock().setType(originalBlock);
                                        }
                                    }, 300L);
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 60L); // Every 3 seconds
        
        entity.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation(), 200, 8, 8, 8, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Void implosion
        entity.getWorld().spawnParticle(Particle.PORTAL, loc, 500, 25, 25, 25, 0.5);
        entity.getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_DEATH, 3.0f, 0.3f);
        entity.getWorld().playSound(loc, Sound.BLOCK_END_PORTAL_SPAWN, 2.0f, 0.5f);
        
        // Void implosion effect
        for (int radius = 30; radius >= 1; radius--) {
            final int r = radius;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (double angle = 0; angle < 360; angle += 20) {
                    double x = loc.getX() + r * Math.cos(Math.toRadians(angle));
                    double z = loc.getZ() + r * Math.sin(Math.toRadians(angle));
                    Location implosionLoc = new Location(loc.getWorld(), x, loc.getY(), z);
                    
                    loc.getWorld().spawnParticle(Particle.PORTAL, implosionLoc, 3);
                }
            }, (30 - radius) * 2L);
        }
        
        // Drop void loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.ENDER_PEARL, 20));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.OBSIDIAN, 32));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 3));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§0§lVOID STALKER DEFEATED", "§7Nothingness returns to the void", 20, 80, 20);
            }
        }
    }
}