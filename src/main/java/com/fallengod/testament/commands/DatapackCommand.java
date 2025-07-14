package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DatapackCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public DatapackCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fallengod.admin.locate")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "scan" -> {
                int radius = args.length > 1 ? parseRadius(args[1]) : 1000;
                scanForAltars(sender, radius);
            }
            case "locate" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /datapack locate <god>");
                    return true;
                }
                locateAltar(sender, args[1]);
            }
            case "test" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /datapack test <god>");
                    return true;
                }
                testAltarSetup(sender, args[1]);
            }
            default -> showHelp(sender);
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Datapack Commands ===");
        sender.sendMessage("§e/datapack scan [radius] §7- Scan for altars");
        sender.sendMessage("§e/datapack locate <god> §7- Find specific altar");
        sender.sendMessage("§e/datapack test <god> §7- Test altar setup");
        sender.sendMessage("§7Available gods: fallen, banishment, abyssal, sylvan, tempest, veil");
    }
    
    private int parseRadius(String radiusStr) {
        try {
            int radius = Integer.parseInt(radiusStr);
            return Math.max(100, Math.min(5000, radius)); // Clamp between 100-5000
        } catch (NumberFormatException e) {
            return 1000; // Default
        }
    }
    
    private void scanForAltars(CommandSender sender, int radius) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        sender.sendMessage("§6Scanning for altars within " + radius + " blocks...");
        
        Location playerLoc = player.getLocation();
        int found = 0;
        
        // This is a simplified scan - in a real implementation, you'd check for
        // the actual altar structures or use structure location data
        for (GodType god : GodType.values()) {
            // Simulate finding altars (replace with actual structure detection)
            if (Math.random() < 0.3) { // 30% chance to "find" each altar type
                int x = playerLoc.getBlockX() + (int)(Math.random() * radius * 2 - radius);
                int z = playerLoc.getBlockZ() + (int)(Math.random() * radius * 2 - radius);
                
                sender.sendMessage(god.getColor() + god.getDisplayName() + " Altar §7found at §f" + 
                                 x + ", ?, " + z + " §7(~" + 
                                 (int)Math.sqrt(Math.pow(x - playerLoc.getX(), 2) + Math.pow(z - playerLoc.getZ(), 2)) + 
                                 " blocks away)");
                found++;
            }
        }
        
        if (found == 0) {
            sender.sendMessage("§7No altars found within " + radius + " blocks.");
            sender.sendMessage("§7Try increasing the search radius or exploring further.");
        } else {
            sender.sendMessage("§aFound " + found + " altar(s) within " + radius + " blocks.");
        }
    }
    
    private void locateAltar(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name. Available: fallen, banishment, abyssal, sylvan, tempest, veil");
            return;
        }
        
        sender.sendMessage("§6Locating nearest " + god.getColoredName() + " §6altar...");
        
        // This is a placeholder - in a real implementation, you'd use Minecraft's
        // structure location commands or maintain a database of altar locations
        Location playerLoc = player.getLocation();
        
        // Simulate finding the nearest altar
        int x = playerLoc.getBlockX() + (int)(Math.random() * 2000 - 1000);
        int z = playerLoc.getBlockZ() + (int)(Math.random() * 2000 - 1000);
        int distance = (int)Math.sqrt(Math.pow(x - playerLoc.getX(), 2) + Math.pow(z - playerLoc.getZ(), 2));
        
        sender.sendMessage(god.getColor() + god.getDisplayName() + " Altar §7located at:");
        sender.sendMessage("§7Coordinates: §f" + x + ", ?, " + z);
        sender.sendMessage("§7Distance: §f~" + distance + " blocks");
        sender.sendMessage("§7Direction: §f" + getDirection(playerLoc.getBlockX(), playerLoc.getBlockZ(), x, z));
        
        // In a real implementation, you might also run the /locate command
        // player.performCommand("locate structure fallengod:" + god.name().toLowerCase() + "_altar");
    }
    
    private String getDirection(int fromX, int fromZ, int toX, int toZ) {
        double angle = Math.atan2(toZ - fromZ, toX - fromX) * 180 / Math.PI;
        angle = (angle + 360) % 360;
        
        if (angle < 22.5 || angle >= 337.5) return "East";
        else if (angle < 67.5) return "Southeast";
        else if (angle < 112.5) return "South";
        else if (angle < 157.5) return "Southwest";
        else if (angle < 202.5) return "West";
        else if (angle < 247.5) return "Northwest";
        else if (angle < 292.5) return "North";
        else return "Northeast";
    }
    
    private void testAltarSetup(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        Material centerBlock = getAltarCenterBlock(god);
        
        sender.sendMessage("§6§l=== Altar Test Setup ===");
        sender.sendMessage("§7God: " + god.getColoredName());
        sender.sendMessage("§7Center Block: §e" + centerBlock.name());
        sender.sendMessage("");
        sender.sendMessage("§7Steps to test:");
        sender.sendMessage("§71. Use §e/fragment testfragments " + god.name().toLowerCase());
        sender.sendMessage("§72. Place a " + centerBlock.name() + " block");
        sender.sendMessage("§73. Right-click the " + centerBlock.name() + " block");
        sender.sendMessage("§74. Check console for debug messages");
        sender.sendMessage("");
        sender.sendMessage("§7If it doesn't work, check:");
        sender.sendMessage("§7- Block placement is correct");
        sender.sendMessage("§7- You have all 7 fragments");
        sender.sendMessage("§7- Plugin permissions");
        sender.sendMessage("§7- Server console for errors");
    }
    
    private Material getAltarCenterBlock(GodType god) {
        return switch (god) {
            case FALLEN -> Material.CRYING_OBSIDIAN;
            case BANISHMENT -> Material.MAGMA_BLOCK;
            case ABYSSAL -> Material.DARK_PRISMARINE;
            case SYLVAN -> Material.OAK_LOG;
            case TEMPEST -> Material.LIGHTNING_ROD;
            case VEIL -> Material.END_PORTAL_FRAME;
            case TIME -> Material.AMETHYST_CLUSTER;
            case FORGE -> Material.ANVIL;
            case VOID -> Material.OBSIDIAN;
            case BLOOD -> Material.REDSTONE_BLOCK;
            case CRYSTAL -> Material.AMETHYST_BLOCK;
            case SHADOW -> Material.SCULK_CATALYST;
        };
    }
}