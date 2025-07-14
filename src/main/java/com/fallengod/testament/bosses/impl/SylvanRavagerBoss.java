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
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SylvanRavagerBoss extends GodBoss {
    
    public SylvanRavagerBoss(TestamentPlugin plugin) {
        super(plugin, BossType.SYLVAN_RAVAGER, GodType.SYLVAN);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof Ravager ravager) {
            ravager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
            ravager.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            ravager.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> rootEntangle();
            case 2 -> thornVolley();
            case 3 -> naturesWrath();
        }
    }
    
    private void rootEntangle() {
        // Immobilize players with vine attacks
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 15) {
                // Create vine blocks around player
                Location playerLoc = player.getLocation();
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        Location vineLoc = playerLoc.clone().add(x, 0, z);
                        if (vineLoc.getBlock().getType() == Material.AIR) {
                            vineLoc.getBlock().setType(Material.VINE);
                            
                            // Remove vines after 10 seconds
                            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                                if (vineLoc.getBlock().getType() == Material.VINE) {
                                    vineLoc.getBlock().setType(Material.AIR);
                                }
                            }, 200L);
                        }
                    }
                }
                
                // Apply slowness and damage
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 4));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 200, -10)); // Negative jump boost
                player.damage(6.0);
                player.sendMessage("§a§lRoot Entangle! §7Nature's vines bind you!");
                
                // Nature particles
                player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation(), 20, 1, 1, 1, 0.1);
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_GRASS_BREAK, 2.0f, 0.5f);
    }
    
    private void thornVolley() {
        // Launch poisonous projectiles
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 25) {
                for (int i = 0; i < 3; i++) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        // Create thorn projectile effect
                        Location thornLoc = player.getLocation().add(
                            (Math.random() - 0.5) * 4,
                            Math.random() * 3 + 1,
                            (Math.random() - 0.5) * 4
                        );
                        
                        // Thorn damage
                        if (player.getLocation().distance(thornLoc) <= 2) {
                            player.damage(8.0);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1));
                            player.sendMessage("§a§lThorn Volley! §7Poisonous thorns pierce your flesh!");
                        }
                        
                        // Thorn particles
                        entity.getWorld().spawnParticle(Particle.COMPOSTER, thornLoc, 10, 0.5, 0.5, 0.5, 0.1);
                        entity.getWorld().spawnParticle(Particle.ITEM_CRACK, thornLoc, 5, 
                            new org.bukkit.inventory.ItemStack(Material.CACTUS));
                        
                    }, i * 10L);
                }
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ARROW_SHOOT, 2.0f, 0.8f);
    }
    
    private void naturesWrath() {
        // Summon hostile wolves and bears (polar bears)
        Location center = entity.getLocation();
        
        // Summon wolves
        for (int i = 0; i < 4; i++) {
            Location spawnLoc = center.clone().add(
                (Math.random() - 0.5) * 15,
                0,
                (Math.random() - 0.5) * 15
            );
            Wolf wolf = (Wolf) spawnLoc.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.WOLF);
            wolf.setAngry(true);
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        }
        
        // Summon bears (polar bears)
        for (int i = 0; i < 2; i++) {
            Location spawnLoc = center.clone().add(
                (Math.random() - 0.5) * 15,
                0,
                (Math.random() - 0.5) * 15
            );
            spawnLoc.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.POLAR_BEAR);
        }
        
        // Nature explosion
        entity.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, center, 100, 5, 5, 5, 0.2);
        entity.getWorld().spawnParticle(Particle.COMPOSTER, center, 50, 3, 3, 3, 0.1);
        entity.getWorld().playSound(center, Sound.ENTITY_WOLF_HOWL, 2.0f, 1.0f);
        
        // Announce
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(center) <= 30) {
                player.sendMessage("§a§lNature's Wrath! §7The wild answers the call!");
            }
        }
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        
        // Spawn more minions
        for (int i = 0; i < 2; i++) {
            Location spawnLoc = entity.getLocation().add(
                (Math.random() - 0.5) * 10,
                0,
                (Math.random() - 0.5) * 10
            );
            Wolf wolf = (Wolf) spawnLoc.getWorld().spawnEntity(spawnLoc, org.bukkit.entity.EntityType.WOLF);
            wolf.setAngry(true);
        }
        
        entity.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, entity.getLocation(), 100, 3, 3, 3, 0.2);
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 3));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        
        // Rapid health regeneration
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                double currentHealth = entity.getHealth();
                double maxHealth = entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue();
                
                if (currentHealth < maxHealth) {
                    entity.setHealth(Math.min(maxHealth, currentHealth + 10));
                    entity.getWorld().spawnParticle(Particle.HEART, entity.getLocation().add(0, 2, 0), 5, 1, 1, 1, 0.1);
                }
            }
        }.runTaskTimer(plugin, 0L, 40L); // Every 2 seconds
        
        entity.getWorld().spawnParticle(Particle.COMPOSTER, entity.getLocation(), 200, 5, 5, 5, 0.3);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Nature death effects
        entity.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, loc, 100, 10, 10, 10, 0.5);
        entity.getWorld().spawnParticle(Particle.COMPOSTER, loc, 200, 8, 8, 8, 0.3);
        entity.getWorld().playSound(loc, Sound.BLOCK_GRASS_BREAK, 3.0f, 0.5f);
        
        // Drop nature loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.EMERALD, 10));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.OAK_SAPLING, 20));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 2));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§a§lFOREST TITAN DEFEATED", "§7Nature's guardian has fallen", 20, 80, 20);
            }
        }
    }
}