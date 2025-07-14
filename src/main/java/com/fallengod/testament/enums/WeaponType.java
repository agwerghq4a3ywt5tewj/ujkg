package com.fallengod.testament.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum WeaponType {
    // Existing Testament Weapons
    HEART_OF_FALLEN_GOD("Heart of the Fallen God", ChatColor.DARK_PURPLE, Material.TOTEM_OF_UNDYING),
    VEIL_OF_NULLIFICATION("Veil of Nullification", ChatColor.DARK_GRAY, Material.PHANTOM_MEMBRANE),
    BANISHMENT_BLADE("Blade of Eternal Banishment", ChatColor.RED, Material.NETHERITE_SWORD),
    ABYSSAL_TRIDENT("Trident of the Endless Deep", ChatColor.DARK_AQUA, Material.TRIDENT),
    SYLVAN_BOW("Bow of the Ancient Forest", ChatColor.GREEN, Material.BOW),
    STORM_ELYTRA("Wings of the Storm Lord", ChatColor.YELLOW, Material.ELYTRA),
    
    // Future Expansion Weapons
    FORGE_MACE("Mace of Divine Forging", ChatColor.GOLD, Material.MACE),
    VOID_BLADE("Void Walker's Blade", ChatColor.BLACK, Material.NETHERITE_SWORD),
    CHRONOS_STAFF("Chronos Staff", ChatColor.LIGHT_PURPLE, Material.STICK),
    CRIMSON_BLADE("Crimson Blade", ChatColor.DARK_RED, Material.NETHERITE_SWORD),
    RESONANCE_CRYSTAL("Resonance Crystal", ChatColor.AQUA, Material.AMETHYST_SHARD),
    SHADOW_CLOAK("Shadow Cloak", ChatColor.GRAY, Material.LEATHER_CHESTPLATE);
    
    private final String displayName;
    private final ChatColor color;
    private final Material material;
    
    WeaponType(String displayName, ChatColor color, Material material) {
        this.displayName = displayName;
        this.color = color;
        this.material = material;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public String getColoredName() {
        return color + displayName + ChatColor.RESET;
    }
}