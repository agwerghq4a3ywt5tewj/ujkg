package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.AscensionLevel;
import com.fallengod.testament.enums.GodType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.ChatColor;

import java.util.Set;

public class AscensionManager {
    private final TestamentPlugin plugin;
    
    public AscensionManager(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    public AscensionLevel getPlayerAscensionLevel(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        int testamentCount = data.getCompletedTestaments().size();
        return AscensionLevel.fromTestamentCount(testamentCount);
    }
    
    public void applyAscensionEffects(Player player) {
        AscensionLevel level = getPlayerAscensionLevel(player);
        
        // Remove existing ascension effects first
        removeAscensionEffects(player);
        
        switch (level) {
            case MORTAL -> {
                // No special effects
            }
            case BLESSED -> {
                // Minor divine powers
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 0, true, false));
                player.sendMessage("§e§lBLESSED ASCENSION! §7The gods smile upon you.");
            }
            case CHOSEN -> {
                // Significant abilities
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 1, true, false));
                player.sendMessage("§6§lCHOSEN ASCENSION! §7You are favored by divine forces.");
            }
            case DIVINE -> {
                // Major reality manipulation
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 2, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, Integer.MAX_VALUE, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 2, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 0, true, false));
                player.sendMessage("§d§lDIVINE ASCENSION! §7Reality bends to your will.");
            }
            case GODLIKE -> {
                // Ultimate cosmic powers
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 3, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 2, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, Integer.MAX_VALUE, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 4, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, true, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, true, false));
                player.sendMessage("§5§lGODLIKE ASCENSION! §7You have transcended mortal limitations.");
            }
        }
    }
    
    private void removeAscensionEffects(Player player) {
        // Remove ascension-specific effects
        player.removePotionEffect(PotionEffectType.LUCK);
        player.removePotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE);
        player.removePotionEffect(PotionEffectType.CONDUIT_POWER);
        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        // Note: Don't remove Strength/Regen/Resistance as they might come from other sources
    }
    
    public boolean hasTestamentConflict(Player player, GodType newGod) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        Set<GodType> completed = data.getCompletedTestaments();
        
        // Define conflicting god pairs
        return switch (newGod) {
            case FALLEN -> completed.contains(GodType.VEIL); // Death vs Reality
            case VEIL -> completed.contains(GodType.FALLEN); // Reality vs Death
            case BANISHMENT -> completed.contains(GodType.ABYSSAL); // Fire vs Water
            case ABYSSAL -> completed.contains(GodType.BANISHMENT); // Water vs Fire
            case SYLVAN -> completed.contains(GodType.TEMPEST); // Nature vs Storm
            case TEMPEST -> completed.contains(GodType.SYLVAN); // Storm vs Nature
            case FORGE -> completed.contains(GodType.VOID); // Creation vs Destruction
            case VOID -> completed.contains(GodType.FORGE); // Destruction vs Creation
            case TIME -> completed.contains(GodType.SHADOW); // Light vs Dark
            case SHADOW -> completed.contains(GodType.TIME); // Dark vs Light
            case BLOOD -> completed.contains(GodType.CRYSTAL); // Chaos vs Order
            case CRYSTAL -> completed.contains(GodType.BLOOD); // Order vs Chaos
        };
    }
    
    public String getConflictMessage(GodType god1, GodType god2) {
        return "§c⚠ Warning: The " + god1.getColoredName() + " §cand " + god2.getColoredName() + 
               " §care opposing forces! Completing both testaments may have unexpected consequences...";
    }
    
    public void checkForAscension(Player player) {
        AscensionLevel currentLevel = getPlayerAscensionLevel(player);
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        int testamentCount = data.getCompletedTestaments().size();
        
        // Check if player just reached a new ascension level
        if (testamentCount == currentLevel.getMinTestaments() && testamentCount > 0) {
            announceAscension(player, currentLevel);
            // Apply ascension effects immediately
            applyAscensionEffects(player);
            // Update player name color
            updatePlayerNameColor(player, currentLevel);
        }
    }
    
    public void checkForConflicts(Player player, GodType newGod) {
        if (hasTestamentConflict(player, newGod)) {
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            Set<GodType> completed = data.getCompletedTestaments();
            
            for (GodType god : completed) {
                if (isConflictingPair(newGod, god)) {
                    String conflictMsg = getConflictMessage(newGod, god);
                    player.sendMessage(conflictMsg);
                    plugin.getServer().broadcastMessage("§c⚠ " + player.getName() + 
                        " now wields conflicting divine powers! Reality trembles...");
                    
                    // Apply conflict penalties
                    applyConflictPenalty(player, newGod, god);
                    break;
                }
            }
        }
    }
    
    private void applyConflictPenalty(Player player, GodType god1, GodType god2) {
        String conflictKey = getConflictKey(god1, god2);
        
        switch (conflictKey) {
            case "fallen_vs_veil" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 1));
                player.sendMessage("§c⚠ Death and Reality clash within you!");
            }
            case "banishment_vs_abyssal" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 1));
                player.sendMessage("§c⚠ Fire and Water war in your soul!");
            }
            case "sylvan_vs_tempest" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 600, 1));
                player.sendMessage("§c⚠ Nature and Storm cannot coexist!");
            }
            case "forge_vs_void" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 600, 1));
                player.sendMessage("§c⚠ Creation and Destruction tear you apart!");
            }
            case "time_vs_shadow" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 1));
                player.sendMessage("§c⚠ Time and Shadow blind your perception!");
            }
            case "blood_vs_crystal" -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 1));
                player.sendMessage("§c⚠ Chaos and Order poison your essence!");
            }
        }
    }
    
    private String getConflictKey(GodType god1, GodType god2) {
        // Normalize the conflict key to ensure consistent ordering
        if ((god1 == GodType.FALLEN && god2 == GodType.VEIL) || (god1 == GodType.VEIL && god2 == GodType.FALLEN)) {
            return "fallen_vs_veil";
        } else if ((god1 == GodType.BANISHMENT && god2 == GodType.ABYSSAL) || (god1 == GodType.ABYSSAL && god2 == GodType.BANISHMENT)) {
            return "banishment_vs_abyssal";
        } else if ((god1 == GodType.SYLVAN && god2 == GodType.TEMPEST) || (god1 == GodType.TEMPEST && god2 == GodType.SYLVAN)) {
            return "sylvan_vs_tempest";
        } else if ((god1 == GodType.FORGE && god2 == GodType.VOID) || (god1 == GodType.VOID && god2 == GodType.FORGE)) {
            return "forge_vs_void";
        } else if ((god1 == GodType.TIME && god2 == GodType.SHADOW) || (god1 == GodType.SHADOW && god2 == GodType.TIME)) {
            return "time_vs_shadow";
        } else if ((god1 == GodType.BLOOD && god2 == GodType.CRYSTAL) || (god1 == GodType.CRYSTAL && god2 == GodType.BLOOD)) {
            return "blood_vs_crystal";
        }
        return "unknown";
    }
    
    private boolean isConflictingPair(GodType god1, GodType god2) {
        return (god1 == GodType.FALLEN && god2 == GodType.VEIL) ||
               (god1 == GodType.VEIL && god2 == GodType.FALLEN) ||
               (god1 == GodType.BANISHMENT && god2 == GodType.ABYSSAL) ||
               (god1 == GodType.ABYSSAL && god2 == GodType.BANISHMENT) ||
               (god1 == GodType.SYLVAN && god2 == GodType.TEMPEST) ||
               (god1 == GodType.TEMPEST && god2 == GodType.SYLVAN) ||
               (god1 == GodType.FORGE && god2 == GodType.VOID) ||
               (god1 == GodType.VOID && god2 == GodType.FORGE) ||
               (god1 == GodType.TIME && god2 == GodType.SHADOW) ||
               (god1 == GodType.SHADOW && god2 == GodType.TIME) ||
               (god1 == GodType.BLOOD && god2 == GodType.CRYSTAL) ||
               (god1 == GodType.CRYSTAL && god2 == GodType.BLOOD);
    }
    
    private void announceAscension(Player player, AscensionLevel level) {
        String message = "§6§l⚡ " + player.getName() + " has ascended to " + 
                        level.getColoredName() + " §6§lstatus! ⚡";
        
        // Broadcast ascension
        plugin.getServer().broadcastMessage("");
        plugin.getServer().broadcastMessage(message);
        plugin.getServer().broadcastMessage("§7" + level.getDescription());
        plugin.getServer().broadcastMessage("");
        
        // Special effects for the player
        player.sendTitle(level.getColor() + "ASCENSION!", 
                        "§7You have become " + level.getDisplayName(), 
                        10, 70, 20);
    }
    
    private void updatePlayerNameColor(Player player, AscensionLevel level) {
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        
        // Remove player from any existing ascension teams
        for (Team team : scoreboard.getTeams()) {
            if (team.getName().startsWith("ascension_")) {
                team.removeEntry(player.getName());
            }
        }
        
        // Create or get team for this ascension level
        String teamName = "ascension_" + level.name().toLowerCase();
        Team team = scoreboard.getTeam(teamName);
        
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setColor(level.getColor());
            team.setPrefix(level.getColor() + "");
        }
        
        // Add player to the team
        team.addEntry(player.getName());
        
        // Set the scoreboard for the player
        player.setScoreboard(scoreboard);
    }
}