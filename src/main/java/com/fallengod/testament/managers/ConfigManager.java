package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final TestamentPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    
    public double getChestSpawnChance() {
        return config.getDouble("testament.fragments.chest_spawn_chance", 0.02);
    }
    
    public double getMobDropChance() {
        return config.getDouble("testament.fragments.mob_drop_chance", 0.001);
    }
    
    public int getMinChestsForFragments() {
        return config.getInt("testament.fragments.min_chests_for_fragments", 50);
    }
    
    public int getChestCooldownHours() {
        return config.getInt("testament.fragments.chest_cooldown_hours", 2);
    }
    
    public int getMobCooldownHours() {
        return config.getInt("testament.fragments.mob_cooldown_hours", 1);
    }
    
    public int getFragmentWeight(int fragmentNumber) {
        return config.getInt("testament.fragment_weights.fragment_" + fragmentNumber, 10);
    }
    
    public boolean isHeartEnabled() {
        return config.getBoolean("heart_of_fallen_god.enabled", true);
    }
    
    public int getHeartExtraHearts() {
        return config.getInt("heart_of_fallen_god.extra_hearts", 15);
    }
    
    public int getHeartStrengthLevel() {
        return config.getInt("heart_of_fallen_god.strength_level", 1);
    }
    
    public int getHeartRegenerationLevel() {
        return config.getInt("heart_of_fallen_god.regeneration_level", 2);
    }
    
    public int getHeartResistanceLevel() {
        return config.getInt("heart_of_fallen_god.resistance_level", 1);
    }
    
    public boolean isVeilEnabled() {
        return config.getBoolean("veil_nullification.enabled", true);
    }
    
    public double getVeilRange() {
        return config.getDouble("veil_nullification.range", 16.0);
    }
    
    public boolean getVeilSlowFalling() {
        return config.getBoolean("veil_nullification.veil_effects.slow_falling", true);
    }
    
    public boolean getVeilNightVision() {
        return config.getBoolean("veil_nullification.veil_effects.night_vision", true);
    }
    
    public int getVeilSpeedLevel() {
        return config.getInt("veil_nullification.veil_effects.speed_level", 1);
    }
}