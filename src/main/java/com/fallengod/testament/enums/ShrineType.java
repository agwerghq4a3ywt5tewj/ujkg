package com.fallengod.testament.enums;

import org.bukkit.ChatColor;

public enum ShrineType {
    CONVERGENCE_NEXUS("Convergence Nexus", "The ultimate shrine of divine convergence", "Master of All Divinity", ChatColor.GOLD);
    
    private final String displayName;
    private final String description;
    private final String title;
    private final ChatColor color;
    
    ShrineType(String displayName, String description, String title, ChatColor color) {
        this.displayName = displayName;
        this.description = description;
        this.title = title;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
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
}