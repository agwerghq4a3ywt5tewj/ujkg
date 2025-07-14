package com.fallengod.testament.events;

import com.fallengod.testament.enums.GodType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event fired when a player finds a fragment
 */
public class FragmentFoundEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    
    private final Player player;
    private final GodType god;
    private final int fragmentNumber;
    private final ItemStack fragment;
    private final FragmentSource source;
    private boolean cancelled = false;
    
    public enum FragmentSource {
        CHEST, MOB_DROP, ADMIN_COMMAND, OTHER
    }
    
    public FragmentFoundEvent(Player player, GodType god, int fragmentNumber, ItemStack fragment, FragmentSource source) {
        this.player = player;
        this.god = god;
        this.fragmentNumber = fragmentNumber;
        this.fragment = fragment;
        this.source = source;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public GodType getGod() {
        return god;
    }
    
    public int getFragmentNumber() {
        return fragmentNumber;
    }
    
    public ItemStack getFragment() {
        return fragment;
    }
    
    public FragmentSource getSource() {
        return source;
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