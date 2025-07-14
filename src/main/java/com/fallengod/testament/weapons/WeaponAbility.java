package com.fallengod.testament.weapons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class WeaponAbility {
    protected final String name;
    protected final String description;
    protected final int cooldownSeconds;
    
    public WeaponAbility(String name, String description, int cooldownSeconds) {
        this.name = name;
        this.description = description;
        this.cooldownSeconds = cooldownSeconds;
    }
    
    public abstract void activate(Player player, ItemStack weapon);
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }
}