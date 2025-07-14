package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WeaponAbilityListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public WeaponAbilityListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_AIR && 
            event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        
        // Check for weapon abilities based on custom model data
        if (item.getItemMeta().hasCustomModelData()) {
            int modelData = item.getItemMeta().getCustomModelData();
            
            switch (modelData) {
                case 100060 -> { // Mace of Divine Forging
                    plugin.getWeaponAbilityManager().activateAbility(player, "molten_strike", item);
                    event.setCancelled(true);
                }
                case 100061 -> { // Void Walker's Blade
                    plugin.getWeaponAbilityManager().activateAbility(player, "void_rip", item);
                    event.setCancelled(true);
                }
                case 100062 -> { // Chronos Staff
                    plugin.getWeaponAbilityManager().activateAbility(player, "time_dilation", item);
                    event.setCancelled(true);
                }
                case 100063 -> { // Crimson Blade
                    plugin.getWeaponAbilityManager().activateAbility(player, "blood_frenzy", item);
                    event.setCancelled(true);
                }
                case 100064 -> { // Resonance Crystal
                    if (player.isSneaking()) {
                        // Crystal Shield ability
                        player.sendMessage("§b§lCrystal Shield activated!");
                    } else {
                        // Ore Sense ability
                        plugin.getWeaponAbilityManager().activateAbility(player, "sonic_boom", item);
                    }
                    event.setCancelled(true);
                }
                case 100065 -> { // Shadow Mantle
                    plugin.getWeaponAbilityManager().activateAbility(player, "umbral_form", item);
                    event.setCancelled(true);
                }
            }
        }
        
        // Check for special testament items
        if (SpecialItems.isHeartOfFallenGod(item)) {
            player.sendMessage("§5§lHeart of the Fallen God §7pulses with divine power...");
            event.setCancelled(true);
        } else if (SpecialItems.isVeilOfNullification(item)) {
            player.sendMessage("§8§lVeil of Nullification §7warps reality around you...");
            event.setCancelled(true);
        }
    }
}