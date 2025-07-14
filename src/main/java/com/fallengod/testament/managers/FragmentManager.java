package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.items.FragmentItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class FragmentManager {
    private final TestamentPlugin plugin;
    private final Random random;
    
    public FragmentManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }
    
    public boolean shouldSpawnChestFragment(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        // Check minimum chests requirement
        int minChests = plugin.getConfigManager().getMinChestsForFragments();
        if (data.getChestsOpened() < minChests) {
            return false;
        }
        
        // Check cooldown
        int cooldownHours = plugin.getConfigManager().getChestCooldownHours();
        if (!data.canReceiveChestFragment(cooldownHours)) {
            return false;
        }
        
        // Check spawn chance
        double spawnChance = plugin.getConfigManager().getChestSpawnChance();
        return random.nextDouble() < spawnChance;
    }
    
    public boolean shouldSpawnMobFragment(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        // Check cooldown
        int cooldownHours = plugin.getConfigManager().getMobCooldownHours();
        if (!data.canReceiveMobFragment(cooldownHours)) {
            return false;
        }
        
        // Check spawn chance
        double spawnChance = plugin.getConfigManager().getMobDropChance();
        return random.nextDouble() < spawnChance;
    }
    
    public ItemStack generateRandomFragment() {
        GodType god = getRandomGod();
        int fragmentNumber = getWeightedRandomFragment();
        return FragmentItem.createFragment(god, fragmentNumber);
    }
    
    public ItemStack generateRandomFragment(GodType god) {
        int fragmentNumber = getWeightedRandomFragment();
        return FragmentItem.createFragment(god, fragmentNumber);
    }
    
    private GodType getRandomGod() {
        GodType[] gods = GodType.values();
        return gods[random.nextInt(gods.length)];
    }
    
    private int getWeightedRandomFragment() {
        int totalWeight = 0;
        for (int i = 1; i <= 7; i++) {
            totalWeight += plugin.getConfigManager().getFragmentWeight(i);
        }
        
        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;
        
        for (int i = 1; i <= 7; i++) {
            currentWeight += plugin.getConfigManager().getFragmentWeight(i);
            if (randomValue < currentWeight) {
                return i;
            }
        }
        
        return 1; // Fallback
    }
    
    public void giveFragmentToPlayer(Player player, ItemStack fragment) {
        if (player.getInventory().firstEmpty() == -1) {
            // Inventory full, drop at player location
            player.getWorld().dropItemNaturally(player.getLocation(), fragment);
            player.sendMessage("§6Your inventory is full! Fragment dropped at your feet.");
        } else {
            player.getInventory().addItem(fragment);
        }
    }
}