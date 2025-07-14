package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ForgeGolemBoss extends GodBoss {
    
    public ForgeGolemBoss(TestamentPlugin plugin) {
        super(plugin, BossType.FORGE_GOLEM, GodType.FORGE);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof IronGolem golem) {
            golem.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
            golem.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
            golem.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
            golem.setCustomName("§6§lMolten Creation Golem");
            golem.setCustomNameVisible(true);
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> moltenHammer();
            case 2 -> forgeBlast();
            case 3 -> heatWave();
        }
    }
    
    private void moltenHammer() {
        // Creates lava pools on impact
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 15) {
                Location hammerLoc = player.getLocation();
                
                // Hammer impact effects
                entity.getWorld().spawnParticle(Particle.LAVA, hammerLoc, 30, 2, 1, 2, 0.1);
                entity.getWorld().spawnParticle(Particle.FLAME, hammerLoc, 50, 3, 1, 3, 0.2);
                entity.getWorld().playSound(hammerLoc, Sound.BLOCK_ANVIL_LAND, 2.0f, 0.5f);
                
                // Create lava pool
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        if (Math.abs(x) + Math.abs(z) <= 2) {
                            Location lavaLoc = hammerLoc.clone().add(x, -1, z);
                            if (lavaLoc.getBlock().getType() != Material.BEDROCK) {
                                lavaLoc.getBlock().setType(Material.LAVA);
                            }
                        }
                    }
                }
                
                // Damage player
                player.damage(18.0);
                player.setFireTicks(100);
                player.sendMessage("§6§lMolten Hammer! §7Lava erupts from the earth!");
                
                // Remove lava after 30 seconds
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    for (int x = -2; x <= 2; x++) {
                        for (int z = -2; z <= 2; z++) {
                            if (Math.abs(x) + Math.abs(z) <= 2) {
                                Location lavaLoc = hammerLoc.clone().add(x, -1, z);
                                if (lavaLoc.getBlock().getType() == Material.LAVA) {
                                    lavaLoc.getBlock().setType(Material.COBBLESTONE);
                                }
                            }
                        }
                    }
                }, 600L);
                
                break; // Only target one player per use
            }
        }
    }
    
    private void forgeBlast() {
        // Explosive metallic projectiles
        Location center = entity.getLocation();
        
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 30) {
                for (int i = 0; i < 3; i++) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        Location blastLoc = player.getLocation().add(
                            (Math.random() - 0.5) * 6,
                            Math.random() * 3,
                            (Math.random() - 0.5) * 6
                        );
                        
                        // Metallic explosion
                        blastLoc.getWorld().createExplosion(blastLoc, 3.0f, false, false);
                        blastLoc.getWorld().spawnParticle(Particle.ITEM_CRACK, blastLoc, 20, 
                            new org.bukkit.inventory.ItemStack(Material.IRON_INGOT));
                        blastLoc.getWorld().spawnParticle(Particle.FLAME, blastLoc, 15, 1, 1, 1, 0.1);
                        
                        // Damage nearby players
                        for (Player nearbyPlayer : entity.getWorld().getPlayers()) {
                            if (nearbyPlayer.getLocation().distance(blastLoc) <= 4) {
                                nearbyPlayer.damage(12.0);
                                nearbyPlayer.setFireTicks(80);
                                nearbyPlayer.sendMessage("§6§lForge Blast! §7Molten metal shrapnel tears through you!");
                            }
                        }
                        
                    }, i * 15L);
                }
            }
        }
        
        entity.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.8f);
    }
    
    private void heatWave() {
        // Area-wide fire damage and blindness
        Location center = entity.getLocation();
        
        // Expanding heat wave
        for (int radius = 1; radius <= 25; radius++) {
            final int r = radius;
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                for (double angle = 0; angle < 360; angle += 20) {
                    double x = center.getX() + r * Math.cos(Math.toRadians(angle));
                    double z = center.getZ() + r * Math.sin(Math.toRadians(angle));
                    Location heatLoc = new Location(center.getWorld(), x, center.getY(), z);
                    
                    // Heat particles
                    center.getWorld().spawnParticle(Particle.FLAME, heatLoc, 3, 0.5, 0.5, 0.5, 0.05);
                    center.getWorld().spawnParticle(Particle.SMOKE, heatLoc, 2, 0.3, 0.3, 0.3, 0.02);
                    
                    // Damage players in heat wave
                    for (Player player : entity.getWorld().getPlayers()) {
                        if (player.getLocation().distance(heatLoc) <= 2) {
                            player.damage(6.0);
                            player.setFireTicks(60);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                            player.sendMessage("§6§lHeat Wave! §7Intense heat overwhelms your senses!");
                        }
                    }
                }
            }, radius * 3L);
        }
        
        entity.getWorld().playSound(center, Sound.ITEM_FIRECHARGE_USE, 3.0f, 0.5f);
        
        // Announce
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 30) {
                player.sendMessage("§6§lHeat Wave! §7Scorching heat radiates from the forge!");
            }
        }
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        
        // Become larger and more imposing
        if (entity instanceof IronGolem golem) {
            // Visual size increase effect
            entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 100, 3, 3, 3, 0.2);
            entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 50, 2, 2, 2, 0.1);
        }
        
        // Set nearby area on fire
        Location center = entity.getLocation();
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                if (Math.random() < 0.3) {
                    Location fireLoc = center.clone().add(x, 0, z);
                    if (fireLoc.getBlock().getType() == Material.AIR) {
                        fireLoc.getBlock().setType(Material.FIRE);
                    }
                }
            }
        }
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
        
        // Heal from lava contact
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                Location entityLoc = entity.getLocation();
                
                // Check for nearby lava
                for (int x = -3; x <= 3; x++) {
                    for (int y = -2; y <= 2; y++) {
                        for (int z = -3; z <= 3; z++) {
                            Location checkLoc = entityLoc.clone().add(x, y, z);
                            if (checkLoc.getBlock().getType() == Material.LAVA) {
                                // Heal from lava
                                double currentHealth = entity.getHealth();
                                double maxHealth = entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
                                
                                if (currentHealth < maxHealth) {
                                    entity.setHealth(Math.min(maxHealth, currentHealth + 5));
                                    entity.getWorld().spawnParticle(Particle.HEART, entity.getLocation().add(0, 2, 0), 3, 1, 1, 1, 0.1);
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 40L); // Every 2 seconds
        
        entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 200, 5, 5, 5, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Massive forge explosion
        entity.getWorld().createExplosion(loc, 10.0f, false, false);
        entity.getWorld().spawnParticle(Particle.LAVA, loc, 200, 15, 15, 15, 0.5);
        entity.getWorld().spawnParticle(Particle.FLAME, loc, 300, 20, 20, 20, 0.3);
        entity.getWorld().playSound(loc, Sound.BLOCK_ANVIL_DESTROY, 3.0f, 0.5f);
        
        // Drop forge loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHERITE_INGOT, 8));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.ANVIL, 3));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 3));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§6§lFORGE GOLEM DEFEATED", "§7The fires of creation are extinguished", 20, 80, 20);
            }
        }
    }
}