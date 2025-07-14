package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.GodType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BossCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public BossCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fallengod.admin.boss")) {
            sender.sendMessage("§cYou don't have permission to use boss commands.");
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "spawn" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /boss spawn <god>");
                    return true;
                }
                spawnBoss(sender, args[1]);
            }
            case "list" -> listBosses(sender);
            case "kill" -> killAllBosses(sender);
            case "info" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /boss info <god>");
                    return true;
                }
                showBossInfo(sender, args[1]);
            }
            default -> showHelp(sender);
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Boss Admin Commands ===");
        sender.sendMessage("§e/boss spawn <god> §7- Spawn a god boss");
        sender.sendMessage("§e/boss list §7- List active bosses");
        sender.sendMessage("§e/boss kill §7- Kill all active bosses");
        sender.sendMessage("§e/boss info <god> §7- Show boss information");
    }
    
    private void spawnBoss(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        boolean success = plugin.getBossManager().spawnBoss(god, player.getLocation());
        
        if (success) {
            sender.sendMessage("§aSpawned " + god.getColoredName() + " §aboss at your location!");
        } else {
            sender.sendMessage("§cFailed to spawn boss. Check console for errors.");
        }
    }
    
    private void listBosses(CommandSender sender) {
        var activeBosses = plugin.getBossManager().getActiveBosses();
        
        if (activeBosses.isEmpty()) {
            sender.sendMessage("§7No active bosses.");
            return;
        }
        
        sender.sendMessage("§6§l=== Active Bosses ===");
        activeBosses.values().forEach(boss -> {
            var entity = boss.getEntity();
            var loc = entity.getLocation();
            double healthPercent = (entity.getHealth() / entity.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue()) * 100;
            
            sender.sendMessage(boss.getBossType().getColoredName() + " §7at " +
                loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() +
                " §7(§c" + String.format("%.1f", healthPercent) + "% HP§7)");
        });
    }
    
    private void killAllBosses(CommandSender sender) {
        var activeBosses = plugin.getBossManager().getActiveBosses();
        
        if (activeBosses.isEmpty()) {
            sender.sendMessage("§7No active bosses to kill.");
            return;
        }
        
        int count = 0;
        for (var boss : activeBosses.values()) {
            boss.getEntity().setHealth(0);
            count++;
        }
        
        sender.sendMessage("§aKilled " + count + " active bosses.");
    }
    
    private void showBossInfo(CommandSender sender, String godName) {
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        var bossType = com.fallengod.testament.enums.BossType.fromGod(god);
        
        sender.sendMessage("§6§l=== " + bossType.getDisplayName() + " ===");
        sender.sendMessage("§7Description: §f" + bossType.getDescription());
        sender.sendMessage("§7Base Entity: §f" + bossType.getBaseEntity().name());
        sender.sendMessage("§7Max Health: §f" + bossType.getMaxHealth());
        sender.sendMessage("§7Damage: §f" + bossType.getDamage());
        sender.sendMessage("§7Difficulty: §f" + getDifficultyRating(bossType));
    }
    
    private String getDifficultyRating(com.fallengod.testament.enums.BossType bossType) {
        double totalPower = bossType.getMaxHealth() + (bossType.getDamage() * 10);
        
        if (totalPower < 600) return "§aEasy";
        else if (totalPower < 800) return "§eMedium";
        else if (totalPower < 1000) return "§6Hard";
        else if (totalPower < 1200) return "§cVery Hard";
        else return "§4§lEXTREME";
    }
}