package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.enums.AscensionLevel;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestamentCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public TestamentCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }
        
        if (args.length == 0) {
            showHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "status" -> showStatus(player);
            case "reunite" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /testament reunite <god>");
                    return true;
                }
                reuniteFragments(player, args[1]);
            }
            default -> showHelp(player);
        }
        
        return true;
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§6§l=== Fallen God Testament ===");
        player.sendMessage("§e/testament status §7- View your fragment progress");
        player.sendMessage("§e/testament reunite <god> §7- Reunite fragments at altar");
        player.sendMessage("§7Available gods: fallen, banishment, abyssal, sylvan, tempest, veil");
    }
    
    private void showStatus(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        AscensionLevel ascension = plugin.getAscensionManager().getPlayerAscensionLevel(player);
        
        player.sendMessage("§6§l=== Your Testament Progress ===");
        player.sendMessage("§7Ascension Level: " + ascension.getColoredName());
        player.sendMessage("§7Description: §f" + ascension.getDescription());
        player.sendMessage("§7Chests Opened: §f" + data.getChestsOpened());
        player.sendMessage("§7Total Fragments: §f" + data.getTotalFragments() + "§7/§f42");
        player.sendMessage("§7Completion: §f" + data.getCompletionPercentage() + "%");
        player.sendMessage("");
        
        for (GodType god : GodType.values()) {
            int fragmentCount = data.getFragments(god).size();
            String status = data.hasCompletedTestament(god) ? "§a✓ COMPLETED" : 
                           fragmentCount == 7 ? "§e⚡ READY" : "§7" + fragmentCount + "/7";
            
            player.sendMessage(god.getColor() + god.getDisplayName() + " §7- " + status);
            
            if (fragmentCount > 0 && !data.hasCompletedTestament(god)) {
                StringBuilder fragments = new StringBuilder("§8  Fragments: ");
                for (int i = 1; i <= 7; i++) {
                    if (data.hasFragment(god, i)) {
                        fragments.append("§a").append(i).append(" ");
                    } else {
                        fragments.append("§7").append(i).append(" ");
                    }
                }
                player.sendMessage(fragments.toString());
            }
        }
    }
    
    private void reuniteFragments(Player player, String godName) {
        GodType god = GodType.fromString(godName);
        if (god == null) {
            player.sendMessage("§cInvalid god name. Available: fallen, banishment, abyssal, sylvan, tempest, veil");
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        if (data.hasCompletedTestament(god)) {
            player.sendMessage("§cYou have already completed the " + god.getColoredName() + " §ctestament!");
            return;
        }
        
        if (!data.hasAllFragments(god)) {
            player.sendMessage("§cYou don't have all 7 fragments for the " + god.getColoredName() + "§c!");
            player.sendMessage("§7You have " + data.getFragments(god).size() + "/7 fragments.");
            return;
        }
        
        player.sendMessage("§6You have all fragments for the " + god.getColoredName() + "§6!");
        player.sendMessage("§7Find the " + god.getColoredName() + " §7altar and right-click it to complete the testament.");
        player.sendMessage("§7Use §e/datapack locate " + god.name().toLowerCase() + " §7to find the nearest altar.");
    }
}