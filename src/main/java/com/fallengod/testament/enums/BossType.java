package com.fallengod.testament.enums;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

public enum BossType {
    // Core Six Bosses
    FALLEN_WARDEN("Corrupted Soul Warden", "The guardian of death itself, risen from the depths", 
                  ChatColor.DARK_PURPLE, EntityType.WARDEN, 500.0, 25.0),
    BANISHMENT_BLAZE("Infernal Exile Lord", "A massive blaze wreathed in eternal flames", 
                     ChatColor.RED, EntityType.BLAZE, 400.0, 20.0),
    ABYSSAL_GUARDIAN("Leviathan of the Deep", "Ancient guardian of the ocean's darkest depths", 
                     ChatColor.DARK_AQUA, EntityType.ELDER_GUARDIAN, 450.0, 22.0),
    SYLVAN_RAVAGER("Corrupted Forest Titan", "Nature's wrath given form and fury", 
                   ChatColor.GREEN, EntityType.RAVAGER, 380.0, 18.0),
    TEMPEST_DRAGON("Storm Sovereign", "Master of lightning and thunder", 
                   ChatColor.YELLOW, EntityType.ENDER_DRAGON, 600.0, 30.0),
    VEIL_PHANTOM("Reality Wraith", "A being that exists between dimensions", 
                 ChatColor.DARK_GRAY, EntityType.PHANTOM, 350.0, 28.0),
    
    // Expansion Six Bosses (Harder)
    FORGE_GOLEM("Molten Creation Golem", "A massive construct of divine metal and fire", 
                ChatColor.GOLD, EntityType.IRON_GOLEM, 700.0, 35.0),
    VOID_STALKER("Harbinger of Nothingness", "A creature that devours reality itself", 
                 ChatColor.BLACK, EntityType.ENDERMAN, 650.0, 40.0),
    TIME_KEEPER("Chronos Incarnate", "The living embodiment of time's flow", 
                ChatColor.LIGHT_PURPLE, EntityType.SHULKER, 750.0, 32.0),
    BLOOD_BERSERKER("Crimson War Beast", "A beast fueled by eternal bloodlust", 
                    ChatColor.DARK_RED, EntityType.HOGLIN, 600.0, 45.0),
    CRYSTAL_RESONATOR("Harmonic Destroyer", "A crystalline entity of pure sonic energy", 
                      ChatColor.AQUA, EntityType.ALLAY, 550.0, 38.0),
    SHADOW_ASSASSIN("Umbral Death Stalker", "Master of darkness and silent death", 
                    ChatColor.GRAY, EntityType.VEX, 500.0, 50.0);
    
    private final String displayName;
    private final String description;
    private final ChatColor color;
    private final EntityType baseEntity;
    private final double maxHealth;
    private final double damage;
    
    BossType(String displayName, String description, ChatColor color, 
             EntityType baseEntity, double maxHealth, double damage) {
        this.displayName = displayName;
        this.description = description;
        this.color = color;
        this.baseEntity = baseEntity;
        this.maxHealth = maxHealth;
        this.damage = damage;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public EntityType getBaseEntity() {
        return baseEntity;
    }
    
    public double getMaxHealth() {
        return maxHealth;
    }
    
    public double getDamage() {
        return damage;
    }
    
    public String getColoredName() {
        return color + displayName + ChatColor.RESET;
    }
    
    public static BossType fromGod(com.fallengod.testament.enums.GodType god) {
        return switch (god) {
            case FALLEN -> FALLEN_WARDEN;
            case BANISHMENT -> BANISHMENT_BLAZE;
            case ABYSSAL -> ABYSSAL_GUARDIAN;
            case SYLVAN -> SYLVAN_RAVAGER;
            case TEMPEST -> TEMPEST_DRAGON;
            case VEIL -> VEIL_PHANTOM;
            case FORGE -> FORGE_GOLEM;
            case VOID -> VOID_STALKER;
            case TIME -> TIME_KEEPER;
            case BLOOD -> BLOOD_BERSERKER;
            case CRYSTAL -> CRYSTAL_RESONATOR;
            case SHADOW -> SHADOW_ASSASSIN;
        };
    }
}