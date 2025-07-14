package com.fallengod.testament.api;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.enums.AscensionLevel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

/**
 * Public API for the Fallen God Testament plugin
 * Allows other plugins to interact with the testament system
 */
public class TestamentAPI {
    
    private static TestamentPlugin plugin;
    
    public static void initialize(TestamentPlugin pluginInstance) {
        plugin = pluginInstance;
    }
    
    /**
     * Get a player's testament progress
     * @param playerId The player's UUID
     * @return PlayerData containing all progress information
     */
    public static PlayerData getPlayerData(UUID playerId) {
        return plugin.getPlayerDataManager().getPlayerData(playerId);
    }
    
    /**
     * Check if a player has completed a specific testament
     * @param playerId The player's UUID
     * @param god The god type to check
     * @return true if the testament is completed
     */
    public static boolean hasCompletedTestament(UUID playerId, GodType god) {
        return getPlayerData(playerId).hasCompletedTestament(god);
    }
    
    /**
     * Get all completed testaments for a player
     * @param playerId The player's UUID
     * @return Set of completed god types
     */
    public static Set<GodType> getCompletedTestaments(UUID playerId) {
        return getPlayerData(playerId).getCompletedTestaments();
    }
    
    /**
     * Get a player's current ascension level
     * @param player The player
     * @return The player's ascension level
     */
    public static AscensionLevel getAscensionLevel(Player player) {
        return plugin.getAscensionManager().getPlayerAscensionLevel(player);
    }
    
    /**
     * Give a fragment to a player
     * @param player The player
     * @param god The god type
     * @param fragmentNumber The fragment number (1-7)
     */
    public static void giveFragment(Player player, GodType god, int fragmentNumber) {
        ItemStack fragment = com.fallengod.testament.items.FragmentItem.createFragment(god, fragmentNumber);
        plugin.getFragmentManager().giveFragmentToPlayer(player, fragment);
        
        // Also add to player data
        PlayerData data = getPlayerData(player.getUniqueId());
        data.addFragment(god, fragmentNumber);
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
    }
    
    /**
     * Force complete a testament for a player
     * @param player The player
     * @param god The god type
     */
    public static void completeTestament(Player player, GodType god) {
        PlayerData data = getPlayerData(player.getUniqueId());
        data.completeTestament(god);
        plugin.getRewardManager().giveTestamentReward(player, god);
        plugin.getAscensionManager().checkForAscension(player);
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
    }
    
    /**
     * Check if two gods are in conflict
     * @param god1 First god
     * @param god2 Second god
     * @return true if the gods are opposing forces
     */
    public static boolean areGodsInConflict(GodType god1, GodType god2) {
        return plugin.getAscensionManager().hasTestamentConflict(null, god1) && 
               plugin.getAscensionManager().hasTestamentConflict(null, god2);
    }
    
    /**
     * Check if a player has achieved divine convergence
     * @param playerId The player's UUID
     * @return true if the player has mastered all gods
     */
    public static boolean hasAchievedConvergence(UUID playerId) {
        return plugin.getConvergenceManager().hasAchievedConvergence(playerId);
    }
    
    /**
     * Get the plugin instance (for advanced integrations)
     * @return The TestamentPlugin instance
     */
    public static TestamentPlugin getPlugin() {
        return plugin;
    }
}