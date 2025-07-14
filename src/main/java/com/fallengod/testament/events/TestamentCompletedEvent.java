package com.fallengod.testament.events;

import com.fallengod.testament.enums.GodType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired when a player completes a testament
 */
public class TestamentCompletedEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final GodType god;
    private final int totalTestaments;
    private boolean cancelled = false;
    
    public TestamentCompletedEvent(Player player, GodType god, int totalTestaments) {
        this.player = player;
        this.god = god;
        this.totalTestaments = totalTestaments;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public GodType getGod() {
        return god;
    }
    
    public int getTotalTestaments() {
        return totalTestaments;
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