package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestamentDebugCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public TestamentDebugCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fallengod.admin.debug")) {
            sender.sendMessage("§cYou don't have permission to use debug commands.");
            return true;
        }
        
        if (args.length == 0) {
            showDebugHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "player" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /testament debug player <player>");
                    return true;
                }
                debugPlayer(sender, args[1]);
            }
            case "stats" -> debugStats(sender);
            case "reset" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /testament debug reset <player>");
                    return true;
                }
                resetPlayer(sender, args[1]);
            }
            case "conflicts" -> debugConflicts(sender);
            case "reload" -> reloadPlugin(sender);
            default -> showDebugHelp(sender);
        }
        
        return true;
    }
    
    private void showDebugHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Testament Debug Commands ===");
        sender.sendMessage("§e/testament debug player <player> §7- Show detailed player data");
        sender.sendMessage("§e/testament debug stats §7- Show server-wide statistics");
        sender.sendMessage("§e/testament debug reset <player> §7- Reset all player progress");
        sender.sendMessage("§e/testament debug conflicts §7- Show all god conflicts");
        sender.sendMessage("§e/testament debug reload §7- Reload plugin configuration");
    }
    
    private void debugPlayer(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + playerName);
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
        
        sender.sendMessage("§6§l=== Debug: " + target.getName() + " ===");
        sender.sendMessage("§7UUID: §f" + target.getUniqueId());
        sender.sendMessage("§7Chests Opened: §f" + data.getChestsOpened());
        sender.sendMessage("§7Total Fragments: §f" + data.getTotalFragments());
        sender.sendMessage("§7Completed Testaments: §f" + data.getCompletedTestaments().size());
        sender.sendMessage("§7Ascension Level: §f" + plugin.getAscensionManager().getPlayerAscensionLevel(target).getDisplayName());
        sender.sendMessage("§7Last Chest Fragment: §f" + formatTime(data.getLastChestFragment()));
        sender.sendMessage("§7Last Mob Fragment: §f" + formatTime(data.getLastMobFragment()));
        
        sender.sendMessage("§7Fragment Progress:");
        for (GodType god : GodType.values()) {
            int count = data.getFragments(god).size();
            String status = data.hasCompletedTestament(god) ? "§a✓" : count == 7 ? "§e⚡" : "§7" + count + "/7";
            sender.sendMessage("§7  " + god.getColoredName() + "§7: " + status);
        }
        
        // Show conflicts
        if (!data.getCompletedTestaments().isEmpty()) {
            sender.sendMessage("§7Potential Conflicts:");
            for (GodType completed : data.getCompletedTestaments()) {
                for (GodType god : GodType.values()) {
                    if (plugin.getAscensionManager().hasTestamentConflict(target, god) && god != completed) {
                        sender.sendMessage("§7  §c" + completed.getDisplayName() + " vs " + god.getDisplayName());
                    }
                }
            }
        }
    }
    
    private void debugStats(CommandSender sender) {
        sender.sendMessage("§6§l=== Server Testament Statistics ===");
        
        int totalPlayers = 0;
        int totalFragments = 0;
        int totalCompletions = 0;
        int[] godCompletions = new int[GodType.values().length];
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            totalPlayers++;
            totalFragments += data.getTotalFragments();
            totalCompletions += data.getCompletedTestaments().size();
            
            for (GodType god : data.getCompletedTestaments()) {
                godCompletions[god.ordinal()]++;
            }
        }
        
        sender.sendMessage("§7Online Players: §f" + totalPlayers);
        sender.sendMessage("§7Total Fragments Collected: §f" + totalFragments);
        sender.sendMessage("§7Total Testament Completions: §f" + totalCompletions);
        
        if (totalPlayers > 0) {
            sender.sendMessage("§7Average Fragments per Player: §f" + String.format("%.1f", (double) totalFragments / totalPlayers));
            sender.sendMessage("§7Average Completions per Player: §f" + String.format("%.1f", (double) totalCompletions / totalPlayers));
        }
        
        sender.sendMessage("§7Testament Popularity:");
        for (int i = 0; i < GodType.values().length; i++) {
            GodType god = GodType.values()[i];
            sender.sendMessage("§7  " + god.getColoredName() + "§7: §f" + godCompletions[i] + " completions");
        }
        
        // Config info
        sender.sendMessage("§7Configuration:");
        sender.sendMessage("§7  Chest Spawn Chance: §f" + (plugin.getConfigManager().getChestSpawnChance() * 100) + "%");
        sender.sendMessage("§7  Mob Drop Chance: §f" + (plugin.getConfigManager().getMobDropChance() * 100) + "%");
        sender.sendMessage("§7  Min Chests for Fragments: §f" + plugin.getConfigManager().getMinChestsForFragments());
    }
    
    private void resetPlayer(CommandSender sender, String playerName) {
        Player target = Bukkit.getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + playerName);
            return;
        }
        
        // Create new player data (effectively resetting everything)
        PlayerData newData = new PlayerData(target.getUniqueId());
        // Reset player data by saving new empty data
        plugin.getPlayerDataManager().savePlayerData(target.getUniqueId());
        
        // Reset titles and toxicity
        plugin.getTitleManager().resetToxicity(target.getUniqueId());
        plugin.getTitleManager().setPlayerTitle(target.getUniqueId(), 
            com.fallengod.testament.enums.PlayerTitle.NONE);
        
        // Remove all testament effects
        target.setMaxHealth(20.0);
        target.clearActivePotionEffects();
        
        sender.sendMessage("§aReset all progress for " + target.getName());
        target.sendMessage("§6Your testament progress has been reset by an administrator.");
    }
    
    private void debugConflicts(CommandSender sender) {
        sender.sendMessage("§6§l=== God Conflicts ===");
        sender.sendMessage("§7These god pairs are opposing forces:");
        sender.sendMessage("§5Fallen God §7vs §8Veil God §7(Death vs Reality)");
        sender.sendMessage("§cBanishment God §7vs §3Abyssal God §7(Fire vs Water)");
        sender.sendMessage("§aSylvan God §7vs §eTempest God §7(Nature vs Storm)");
        sender.sendMessage("§6Forge God §7vs §0Void God §7(Creation vs Destruction)");
        sender.sendMessage("§dTime God §7vs §8Shadow God §7(Light vs Dark)");
        sender.sendMessage("§4Blood God §7vs §bCrystal God §7(Chaos vs Order)");
    }
    
    private void reloadPlugin(CommandSender sender) {
        plugin.getConfigManager().reloadConfig();
        sender.sendMessage("§aPlugin configuration reloaded!");
    }
    
    private String formatTime(long timestamp) {
        if (timestamp == 0) return "Never";
        long diff = System.currentTimeMillis() - timestamp;
        long hours = diff / (1000 * 60 * 60);
        long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
        return hours + "h " + minutes + "m ago";
    }
}