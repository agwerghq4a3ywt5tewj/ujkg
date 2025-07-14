package com.fallengod.testament.bosses.impl;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class BanishmentBlazeBoss extends GodBoss {
    
    public BanishmentBlazeBoss(TestamentPlugin plugin) {
        super(plugin, BossType.BANISHMENT_BLAZE, GodType.BANISHMENT);
    }
    
    @Override
    protected void setupBoss() {
        if (entity instanceof Blaze blaze) {
            blaze.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
            blaze.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
            blaze.setGlowing(true);
        }
    }
    
    @Override
    protected void useSpecialAbility() {
        switch (phase) {
            case 1 -> fireballBarrage();
            case 2 -> infernalRing();
            case 3 -> meteorStrike();
        }
    }
    
    private void fireballBarrage() {
        // Launch multiple fireballs at nearby players
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 30) {
                for (int i = 0; i < 5; i++) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        Vector direction = player.getLocation().subtract(entity.getLocation()).toVector().normalize();
                        Fireball fireball = entity.getWorld().spawn(entity.getLocation().add(0, 2, 0), Fireball.class);
                        fireball.setDirection(direction);
                        fireball.setYield(3.0f);
                        fireball.setShooter(entity);
                    }, i * 5L);
                }
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2.0f, 0.8f);
    }
    
    private void infernalRing() {
        // Create ring of fire around the boss
        Location center = entity.getLocation();
        
        for (int radius = 5; radius <= 15; radius += 5) {
            final int r = radius;
            for (double angle = 0; angle < 360; angle += 15) {
                double x = center.getX() + r * Math.cos(Math.toRadians(angle));
                double z = center.getZ() + r * Math.sin(Math.toRadians(angle));
                Location fireLoc = new Location(center.getWorld(), x, center.getY(), z);
                
                // Set fire blocks
                if (fireLoc.getBlock().getType() == Material.AIR || 
                    fireLoc.getBlock().getType().name().contains("GRASS")) {
                    fireLoc.getBlock().setType(Material.FIRE);
                }
                
                // Damage players
                for (Player player : entity.getWorld().getPlayers()) {
                    if (player.getLocation().distance(fireLoc) <= 2) {
                        player.damage(8.0);
                        player.setFireTicks(100);
                        player.sendMessage("§c§lInfernal Ring! §7You are caught in the flames!");
                    }
                }
                
                // Particles
                center.getWorld().spawnParticle(Particle.FLAME, fireLoc, 5, 0.5, 0.5, 0.5, 0.1);
            }
        }
        
        entity.getWorld().playSound(center, Sound.ITEM_FIRECHARGE_USE, 3.0f, 0.5f);
    }
    
    private void meteorStrike() {
        // Rain meteors from the sky
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 40) {
                for (int i = 0; i < 3; i++) {
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        Location target = player.getLocation().add(
                            (Math.random() - 0.5) * 10,
                            0,
                            (Math.random() - 0.5) * 10
                        );
                        
                        // Spawn fireball high above
                        Location spawnLoc = target.clone().add(0, 50, 0);
                        Fireball meteor = entity.getWorld().spawn(spawnLoc, Fireball.class);
                        meteor.setDirection(new Vector(0, -1, 0));
                        meteor.setYield(5.0f);
                        meteor.setShooter(entity);
                        
                        // Warning particles
                        target.getWorld().spawnParticle(Particle.FLAME, target, 20, 2, 0, 2, 0.1);
                        
                    }, i * 20L);
                }
            }
        }
        
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 2.0f, 0.5f);
    }
    
    @Override
    protected void enterPhase2() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        entity.getWorld().spawnParticle(Particle.FLAME, entity.getLocation(), 100, 3, 3, 3, 0.2);
        
        // Set nearby area on fire
        Location center = entity.getLocation();
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                Location fireLoc = center.clone().add(x, 0, z);
                if (fireLoc.getBlock().getType() == Material.AIR) {
                    fireLoc.getBlock().setType(Material.FIRE);
                }
            }
        }
    }
    
    @Override
    protected void enterPhase3() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        entity.getWorld().spawnParticle(Particle.LAVA, entity.getLocation(), 200, 5, 5, 5, 0.3);
        
        // Permanent fire aura
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                if (!isAlive()) {
                    cancel();
                    return;
                }
                
                for (Player player : entity.getWorld().getPlayers()) {
                    if (player.getLocation().distance(entity.getLocation()) <= 10) {
                        player.setFireTicks(60);
                        player.damage(2.0);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @Override
    protected void onDeath() {
        Location loc = entity.getLocation();
        
        // Massive explosion
        entity.getWorld().createExplosion(loc, 8.0f, false, false);
        entity.getWorld().spawnParticle(Particle.EXPLOSION, loc, 100, 15, 15, 15, 0.5);
        entity.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3.0f, 0.5f);
        
        // Drop loot
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.BLAZE_ROD, 10));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.FIRE_CHARGE, 20));
        loc.getWorld().dropItemNaturally(loc, new org.bukkit.inventory.ItemStack(Material.NETHER_STAR, 2));
        
        // Announce death
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(loc) <= 200) {
                player.sendTitle("§c§lINFERNAL EXILE DEFEATED", "§7The flames of banishment are extinguished", 20, 80, 20);
            }
        }
    }
}