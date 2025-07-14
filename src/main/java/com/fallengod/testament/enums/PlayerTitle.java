package com.fallengod.testament.enums;

import org.bukkit.ChatColor;

public enum PlayerTitle {
    NONE("", ChatColor.WHITE, "No special title"),
    FALLEN("Fallen", ChatColor.DARK_RED, "A player who has fallen from grace"),
    TOXIC("Toxic", ChatColor.RED, "A player with toxic behavior"),
    CURSED("Cursed", ChatColor.DARK_PURPLE, "A player cursed by the gods"),
    BLESSED("Blessed", ChatColor.GOLD, "A player blessed by divine favor"),
    CHAMPION("Champion", ChatColor.YELLOW, "A skilled warrior"),
    LEGEND("Legend", ChatColor.LIGHT_PURPLE, "A legendary player");
    
    private final String displayName;
    private final ChatColor color;
    private final String description;
    
    PlayerTitle(String displayName, ChatColor color, String description) {
        this.displayName = displayName;
        this.color = color;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getColoredName() {
        return color + displayName + ChatColor.RESET;
    }
    
    public String getPrefix() {
        if (displayName.isEmpty()) {
            return "";
        }
        return "[" + getColoredName() + "] ";
    }
}