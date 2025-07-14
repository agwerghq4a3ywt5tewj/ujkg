package com.fallengod.testament.enums;

import org.bukkit.ChatColor;

public enum AscensionLevel {
    MORTAL(0, 2, "Mortal", ChatColor.WHITE, "Base gameplay"),
    BLESSED(1, 2, "Blessed", ChatColor.YELLOW, "Minor divine powers"),
    CHOSEN(3, 4, "Chosen", ChatColor.GOLD, "Significant abilities"),
    DIVINE(5, 6, "Divine", ChatColor.LIGHT_PURPLE, "Major reality manipulation"),
    GODLIKE(7, Integer.MAX_VALUE, "Godlike", ChatColor.DARK_PURPLE, "Ultimate cosmic powers");
    
    private final int minTestaments;
    private final int maxTestaments;
    private final String displayName;
    private final ChatColor color;
    private final String description;
    
    AscensionLevel(int minTestaments, int maxTestaments, String displayName, ChatColor color, String description) {
        this.minTestaments = minTestaments;
        this.maxTestaments = maxTestaments;
        this.displayName = displayName;
        this.color = color;
        this.description = description;
    }
    
    public int getMinTestaments() {
        return minTestaments;
    }
    
    public int getMaxTestaments() {
        return maxTestaments;
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
    
    public static AscensionLevel fromTestamentCount(int testamentCount) {
        for (AscensionLevel level : values()) {
            if (testamentCount >= level.minTestaments && testamentCount <= level.maxTestaments) {
                return level;
            }
        }
        return MORTAL;
    }
}