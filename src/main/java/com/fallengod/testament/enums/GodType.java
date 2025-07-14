package com.fallengod.testament.enums;

import org.bukkit.ChatColor;

public enum GodType {
    // Original 6 Gods
    FALLEN("Fallen God", "Death, undeath, ultimate protection", "Guardian of the Afterlife", ChatColor.DARK_PURPLE),
    BANISHMENT("Banishment God", "Fire, exile, destruction", "Lord of Eternal Flames", ChatColor.RED),
    ABYSSAL("Abyssal God", "Ocean depths, water mastery", "Master of the Deep", ChatColor.DARK_AQUA),
    SYLVAN("Sylvan God", "Forests, nature, growth", "Guardian of the Wild", ChatColor.GREEN),
    TEMPEST("Tempest God", "Sky, storms, lightning, flight", "Lord of the Storms", ChatColor.YELLOW),
    VEIL("Veil God", "Reality manipulation, void magic", "Weaver of Reality", ChatColor.DARK_GRAY),
    
    // Future Expansion Gods
    FORGE("Forge God", "Smithing, crafting, creation, molten metal", "Master of Creation", ChatColor.GOLD),
    VOID("Void God", "Emptiness, teleportation, phase shifting", "Harbinger of Nothingness", ChatColor.BLACK),
    TIME("Time God", "Time manipulation, aging, temporal magic", "Chronos Incarnate", ChatColor.LIGHT_PURPLE),
    BLOOD("Blood God", "Combat, sacrifice, berserker rage", "Warrior's Patron", ChatColor.DARK_RED),
    CRYSTAL("Crystal God", "Crystals, sound, vibration, harmony", "Resonance Master", ChatColor.AQUA),
    SHADOW("Shadow God", "Stealth, darkness, assassination", "Master of Darkness", ChatColor.GRAY);
    
    private final String displayName;
    private final String theme;
    private final String title;
    private final ChatColor color;
    
    GodType(String displayName, String theme, String title, ChatColor color) {
        this.displayName = displayName;
        this.theme = theme;
        this.title = title;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getTheme() {
        return theme;
    }
    
    public String getTitle() {
        return title;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public String getColoredName() {
        return color + displayName + ChatColor.RESET;
    }
    
    public static GodType fromString(String name) {
        if (name == null) return null;
        
        for (GodType god : values()) {
            if (god.name().equalsIgnoreCase(name) || 
                god.displayName.equalsIgnoreCase(name)) {
                return god;
            }
        }
        return null;
    }
}