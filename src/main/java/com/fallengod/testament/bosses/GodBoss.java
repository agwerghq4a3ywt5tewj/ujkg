package com.fallengod.testament.bosses;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GodBoss {
    protected final TestamentPlugin plugin;
    protected final BossType bossType;
    protected final GodType godType;
    protected LivingEntity entity;
    protected final Map<UUID, Integer> playerDamage;
    protected boolean isEnraged = false;
    protected int phase = 1;
    protected long lastAbilityUse = 0;
    
    public GodBoss(TestamentPlugin plugin, BossType bossType, GodType godType) {
        this.plugin = plugin;
        this.bossType = bossType;
        this.godType = godType;
        this.playerDamage = new HashMap<>();
    }
    
    public void spawn(Location location) {
        entity = (LivingEntity) location.getWorld().spawnEntity(location, bossType.getBaseEntity());
        
        // Set basic properties
        entity.setCustomName(bossType.getColoredName());
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(bossType.getMaxHealth());
        entity.setHealth(bossType.getMaxHealth());
        entity.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(bossType.getDamage());
        
        // Apply boss-specific setup
        setupBoss();
        
        // Start boss AI
        startBossAI();
        
        // Spawn effects
        spawnEffects();
        
        // Announce spawn
        announceSpawn(location);
    }
    
    protected abstract void setupBoss();
    protected abstract void useSpecialAbility();
    protected abstract void enterPhase2();
    protected abstract void enterPhase3();
    protected abstract void onDeath();
    
    private void startBossAI() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) {
                    onDeath();
                    cancel();
                    return;
                }
                
                updateBossLogic();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    private void updateBossLogic() {
        double healthPercent = entity.getHealth() / entity.getAttribute(Attribute.MAX_HEALTH).getValue();
        
        // Phase transitions
        if (healthPercent <= 0.33 && phase < 3) {
            phase = 3;
            enterPhase3();
            announcePhase("FINAL PHASE");
        } else if (healthPercent <= 0.66 && phase < 2) {
            phase = 2;
            enterPhase2();
            announcePhase("PHASE 2");
        }
        
        // Enrage at low health
        if (healthPercent <= 0.25 && !isEnraged) {
            isEnraged = true;
            enterEnrage();
        }
        
        // Use abilities
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAbilityUse >= getAbilityCooldown()) {
            useSpecialAbility();
            lastAbilityUse = currentTime;
        }
    }
    
    protected void enterEnrage() {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
        
        // Visual effects
        entity.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, entity.getLocation(), 50, 2, 2, 2, 0.1);
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2.0f, 0.5f);
        
        // Announce enrage
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 100) {
                player.sendTitle("§c§lENRAGED!", bossType.getColoredName() + " §cis furious!", 10, 40, 10);
            }
        }
    }
    
    protected void spawnEffects() {
        entity.getWorld().spawnParticle(Particle.EXPLOSION, entity.getLocation(), 10, 2, 2, 2, 0.1);
        entity.getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 50, 3, 3, 3, 0.2);
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2.0f, 0.8f);
    }
    
    protected void announceSpawn(Location location) {
        String message = "§4§l⚔ " + bossType.getColoredName() + " §4§lhas awakened! ⚔";
        
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= 200) {
                player.sendMessage("");
                player.sendMessage(message);
                player.sendMessage("§7" + bossType.getDescription());
                player.sendMessage("§7Location: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
                player.sendMessage("");
                
                player.sendTitle("§4§lBOSS AWAKENED", bossType.getColoredName(), 20, 60, 20);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
            }
        }
    }
    
    protected void announcePhase(String phaseName) {
        for (Player player : entity.getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 100) {
                player.sendTitle("§6§l" + phaseName, bossType.getColoredName(), 10, 40, 10);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.5f);
            }
        }
    }
    
    protected long getAbilityCooldown() {
        return switch (phase) {
            case 1 -> 5000; // 5 seconds
            case 2 -> 3000; // 3 seconds
            case 3 -> 2000; // 2 seconds
            default -> 5000;
        };
    }
    
    public void addPlayerDamage(Player player, int damage) {
        playerDamage.put(player.getUniqueId(), playerDamage.getOrDefault(player.getUniqueId(), 0) + damage);
    }
    
    public LivingEntity getEntity() {
        return entity;
    }
    
    public BossType getBossType() {
        return bossType;
    }
    
    public GodType getGodType() {
        return godType;
    }
    
    public boolean isAlive() {
        return entity != null && !entity.isDead();
    }
}