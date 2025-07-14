package com.fallengod.testament.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired when the Convergence Nexus is activated
 * This represents the ultimate achievement in the Testament system
 */
public class ConvergenceEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final Location nexusLocation;
    private final long achievementTime;
    private boolean cancelled = false;
    
    public ConvergenceEvent(Player player, Location nexusLocation) {
        this.player = player;
        this.nexusLocation = nexusLocation;
        this.achievementTime = System.currentTimeMillis();
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Location getNexusLocation() {
        return nexusLocation;
    }
    
    public long getAchievementTime() {
        return achievementTime;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}