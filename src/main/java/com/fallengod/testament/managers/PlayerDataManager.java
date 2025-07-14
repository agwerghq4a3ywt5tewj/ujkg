package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final TestamentPlugin plugin;
    private final Map<UUID, PlayerData> playerDataCache;
    private final File dataFolder;
    
    public PlayerDataManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.playerDataCache = new HashMap<>();
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }
    
    public Map<UUID, PlayerData> getPlayerDataCache() {
        return playerDataCache;
    }
    
    public PlayerData getPlayerData(UUID playerId) {
        if (playerDataCache.containsKey(playerId)) {
            return playerDataCache.get(playerId);
        }
        
        PlayerData data = loadPlayerData(playerId);
        playerDataCache.put(playerId, data);
        return data;
    }
    
    private PlayerData loadPlayerData(UUID playerId) {
        File playerFile = new File(dataFolder, playerId.toString() + ".yml");
        
        if (!playerFile.exists()) {
            return new PlayerData(playerId);
        }
        
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        PlayerData data = new PlayerData(playerId);
        
        // Load fragments
        for (GodType god : GodType.values()) {
            String path = "fragments." + god.name().toLowerCase();
            if (config.contains(path)) {
                for (int fragment : config.getIntegerList(path)) {
                    data.addFragment(god, fragment);
                }
            }
        }
        
        // Load completed testaments
        for (String godName : config.getStringList("completed")) {
            GodType god = GodType.fromString(godName);
            if (god != null) {
                data.completeTestament(god);
            }
        }
        
        // Load statistics
        data.incrementChestsOpened(); // Reset to 0 then set
        for (int i = 0; i < config.getInt("chests_opened", 0) - 1; i++) {
            data.incrementChestsOpened();
        }
        
        data.setLastChestFragment(config.getLong("last_chest_fragment", 0));
        data.setLastMobFragment(config.getLong("last_mob_fragment", 0));
        
        return data;
    }
    
    public void savePlayerData(UUID playerId) {
        PlayerData data = playerDataCache.get(playerId);
        if (data == null) return;
        
        File playerFile = new File(dataFolder, playerId.toString() + ".yml");
        FileConfiguration config = new YamlConfiguration();
        
        // Save fragments
        for (GodType god : GodType.values()) {
            String path = "fragments." + god.name().toLowerCase();
            config.set(path, data.getFragments(god).stream().toList());
        }
        
        // Save completed testaments
        config.set("completed", data.getCompletedTestaments().stream()
                .map(god -> god.name().toLowerCase()).toList());
        
        // Save statistics
        config.set("chests_opened", data.getChestsOpened());
        config.set("last_chest_fragment", data.getLastChestFragment());
        config.set("last_mob_fragment", data.getLastMobFragment());
        
        try {
            config.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player data for " + playerId + ": " + e.getMessage());
        }
    }
    
    public void saveAllData() {
        for (UUID playerId : playerDataCache.keySet()) {
            savePlayerData(playerId);
        }
    }
    
    public void unloadPlayerData(UUID playerId) {
        savePlayerData(playerId);
        playerDataCache.remove(playerId);
    }
}