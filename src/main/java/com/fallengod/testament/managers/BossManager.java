package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.bosses.GodBoss;
import com.fallengod.testament.bosses.impl.*;
import com.fallengod.testament.enums.BossType;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossManager {
    private final TestamentPlugin plugin;
    private final Map<UUID, GodBoss> activeBosses;
    private final Map<GodType, Class<? extends GodBoss>> bossClasses;
    
    public BossManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.activeBosses = new HashMap<>();
        this.bossClasses = new HashMap<>();
        
        registerBosses();
    }
    
    private void registerBosses() {
        bossClasses.put(GodType.FALLEN, FallenWardenBoss.class);
        bossClasses.put(GodType.BANISHMENT, BanishmentBlazeBoss.class);
        bossClasses.put(GodType.ABYSSAL, AbyssalGuardianBoss.class);
        bossClasses.put(GodType.SYLVAN, SylvanRavagerBoss.class);
        bossClasses.put(GodType.TEMPEST, TempestDragonBoss.class);
        bossClasses.put(GodType.VEIL, VeilPhantomBoss.class);
        bossClasses.put(GodType.FORGE, ForgeGolemBoss.class);
        bossClasses.put(GodType.VOID, VoidStalkerBoss.class);
        // TODO: Add remaining expansion bosses (Time, Blood, Crystal, Shadow)
    }
    
    public boolean spawnBoss(GodType godType, Location location) {
        Class<? extends GodBoss> bossClass = bossClasses.get(godType);
        if (bossClass == null) {
            plugin.getLogger().warning("No boss class registered for god type: " + godType);
            return false;
        }
        
        try {
            GodBoss boss = bossClass.getDeclaredConstructor(TestamentPlugin.class).newInstance(plugin);
            boss.spawn(location);
            activeBosses.put(boss.getEntity().getUniqueId(), boss);
            
            plugin.getLogger().info("Spawned " + boss.getBossType().getDisplayName() + " at " + 
                location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
            
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to spawn boss for " + godType + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public GodBoss getBoss(UUID entityId) {
        return activeBosses.get(entityId);
    }
    
    public void removeBoss(UUID entityId) {
        activeBosses.remove(entityId);
    }
    
    public boolean isBoss(UUID entityId) {
        return activeBosses.containsKey(entityId);
    }
    
    public Map<UUID, GodBoss> getActiveBosses() {
        return new HashMap<>(activeBosses);
    }
    
    public void cleanupDeadBosses() {
        activeBosses.entrySet().removeIf(entry -> !entry.getValue().isAlive());
    }
}