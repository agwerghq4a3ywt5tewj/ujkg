package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.items.FragmentItem;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FragmentCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public FragmentCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fallengod.admin.spawn")) {
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "spawn" -> {
                if (args.length < 4) {
                    sender.sendMessage("§cUsage: /fragment spawn <god> <number> <chest|here>");
                    return true;
                }
                spawnFragment(sender, args[1], args[2], args[3]);
            }
            case "heart" -> giveHeart(sender);
            case "veil" -> giveVeil(sender);
            case "stats" -> showStats(sender);
            case "giveall" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /fragment giveall <god>");
                    return true;
                }
                giveAllFragments(sender, args[1]);
            }
            case "testmode" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /fragment testmode <on|off>");
                    return true;
                }
                toggleTestMode(sender, args[1]);
            }
            case "testfragments" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /fragment testfragments <god>");
                    return true;
                }
                giveTestFragments(sender, args[1]);
            }
            case "title" -> {
                if (args.length < 3) {
                    sender.sendMessage("§cUsage: /fragment title <player> <title|reset>");
                    return true;
                }
                managePlayerTitle(sender, args[1], args[2]);
            }
            case "convergence" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /fragment convergence <player>");
                    return true;
                }
                testConvergence(sender, args[1]);
            }
            default -> showHelp(sender);
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Fragment Admin Commands ===");
        sender.sendMessage("§e/fragment spawn <god> <number> <chest|here> §7- Spawn fragment");
        sender.sendMessage("§e/fragment heart §7- Give Heart of Fallen God");
        sender.sendMessage("§e/fragment veil §7- Give Veil of Nullification");
        sender.sendMessage("§e/fragment stats §7- View spawning statistics");
        sender.sendMessage("§e/fragment giveall <god> §7- Give all 7 fragments");
        sender.sendMessage("§e/fragment testmode <on|off> §7- Toggle veil test mode");
        sender.sendMessage("§e/fragment testfragments <god> §7- Give fragments and show altar info");
        sender.sendMessage("§e/fragment title <player> <title|reset> §7- Manage player titles");
        sender.sendMessage("§e/fragment convergence <player> §7- Test convergence system");
    }
    
    private void spawnFragment(CommandSender sender, String godName, String numberStr, String location) {
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        int number;
        try {
            number = Integer.parseInt(numberStr);
            if (number < 1 || number > 7) {
                sender.sendMessage("§cFragment number must be between 1 and 7.");
                return;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid fragment number.");
            return;
        }
        
        ItemStack fragment = FragmentItem.createFragment(god, number);
        
        if (location.equalsIgnoreCase("here")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cConsole cannot use 'here' location.");
                return;
            }
            player.getInventory().addItem(fragment);
            sender.sendMessage("§aGiven " + god.getColoredName() + " §aFragment " + number);
        } else if (location.equalsIgnoreCase("chest")) {
            // This would be implemented to spawn in nearby chests
            sender.sendMessage("§cChest spawning not yet implemented.");
        } else {
            sender.sendMessage("§cInvalid location. Use 'here' or 'chest'.");
        }
    }
    
    private void giveHeart(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cConsole cannot receive items.");
            return;
        }
        
        ItemStack heart = SpecialItems.createHeartOfFallenGod();
        player.getInventory().addItem(heart);
        sender.sendMessage("§aGiven Heart of the Fallen God");
    }
    
    private void giveVeil(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cConsole cannot receive items.");
            return;
        }
        
        ItemStack veil = SpecialItems.createVeilOfNullification();
        player.getInventory().addItem(veil);
        sender.sendMessage("§aGiven Veil of Nullification");
    }
    
    private void showStats(CommandSender sender) {
        sender.sendMessage("§6§l=== Fragment Statistics ===");
        
        int totalPlayers = 0;
        int totalFragments = 0;
        int totalCompletions = 0;
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
            totalPlayers++;
            totalFragments += data.getTotalFragments();
            totalCompletions += data.getCompletedTestaments().size();
        }
        
        sender.sendMessage("§7Online Players: §f" + totalPlayers);
        sender.sendMessage("§7Total Fragments: §f" + totalFragments);
        sender.sendMessage("§7Total Completions: §f" + totalCompletions);
        
        // Show fragment distribution
        sender.sendMessage("§7Fragment Weights:");
        for (int i = 1; i <= 7; i++) {
            int weight = plugin.getConfigManager().getFragmentWeight(i);
            sender.sendMessage("§7  Fragment " + i + ": §f" + weight + "%");
        }
    }
    
    private void giveAllFragments(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cConsole cannot receive items.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        for (int i = 1; i <= 7; i++) {
            ItemStack fragment = FragmentItem.createFragment(god, i);
            player.getInventory().addItem(fragment);
        }
        
        // Also add to player data for testing
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        for (int i = 1; i <= 7; i++) {
            data.addFragment(god, i);
        }
        
        // Save the data
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
        
        sender.sendMessage("§aGiven all 7 " + god.getColoredName() + " §afragments");
    }
    
    private void toggleTestMode(CommandSender sender, String mode) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cConsole cannot use test mode.");
            return;
        }
        
        boolean enable = mode.equalsIgnoreCase("on");
        
        if (enable) {
            // Give both items for testing
            ItemStack heart = SpecialItems.createHeartOfFallenGod();
            ItemStack veil = SpecialItems.createVeilOfNullification();
            player.getInventory().addItem(heart, veil);
            
            // Enable test mode in the listener
            plugin.getHeartVeilListener().setTestMode(player.getUniqueId(), true);
            
            sender.sendMessage("§6§lTest Mode Enabled!");
            sender.sendMessage("§7You now have both Heart and Veil items.");
            sender.sendMessage("§7§lHow Test Mode Works:");
            sender.sendMessage("§7• When you have both items: §8Veil nullifies Heart");
            sender.sendMessage("§7• Drop the Veil: §5Heart effects activate");
            sender.sendMessage("§7• Pick up the Veil: §8Heart effects get nullified");
            sender.sendMessage("§7• Watch your action bar and chat for status messages");
            sender.sendMessage("§7Use §e/fragment testmode off §7to disable.");
        } else {
            // Remove test items
            for (ItemStack item : player.getInventory().getContents()) {
                if (SpecialItems.isTestamentItem(item)) {
                    player.getInventory().remove(item);
                }
            }
            
            // Disable test mode in the listener
            plugin.getHeartVeilListener().setTestMode(player.getUniqueId(), false);
            
            sender.sendMessage("§6§lTest Mode Disabled!");
            sender.sendMessage("§7All test items removed.");
        }
    }
    
    private void giveTestFragments(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cConsole cannot receive items.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        // Give all fragments
        for (int i = 1; i <= 7; i++) {
            ItemStack fragment = FragmentItem.createFragment(god, i);
            player.getInventory().addItem(fragment);
        }
        
        // Also add to player data for testing
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        for (int i = 1; i <= 7; i++) {
            data.addFragment(god, i);
        }
        
        // Save the data
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
        
        // Show altar information
        Material centerBlock = getAltarCenterBlock(god);
        sender.sendMessage("§a§lTest Setup Complete!");
        sender.sendMessage("§7Given all 7 " + god.getColoredName() + " §7fragments");
        sender.sendMessage("§7Find an altar with center block: §e" + centerBlock.name());
        sender.sendMessage("§7Or place a " + centerBlock.name() + " block and right-click it");
        sender.sendMessage("§7Use §e/fragment spawn " + god.name().toLowerCase() + " 1 here §7for individual fragments");
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
    
    private void managePlayerTitle(CommandSender sender, String playerName, String titleName) {
        Player target = plugin.getServer().getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + playerName);
            return;
        }
        
        if (titleName.equalsIgnoreCase("reset")) {
            plugin.getTitleManager().resetToxicity(target.getUniqueId());
            plugin.getTitleManager().setPlayerTitle(target.getUniqueId(), 
                com.fallengod.testament.enums.PlayerTitle.NONE);
            sender.sendMessage("§aReset title and toxicity for " + target.getName());
            return;
        }
        
        try {
            com.fallengod.testament.enums.PlayerTitle title = 
                com.fallengod.testament.enums.PlayerTitle.valueOf(titleName.toUpperCase());
            plugin.getTitleManager().setPlayerTitle(target.getUniqueId(), title);
            sender.sendMessage("§aSet " + target.getName() + "'s title to " + title.getColoredName());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid title. Available: NONE, FALLEN, TOXIC, CURSED, BLESSED, CHAMPION, LEGEND");
        }
    }
    
    private void testConvergence(CommandSender sender, String playerName) {
        Player target = plugin.getServer().getPlayer(playerName);
        if (target == null) {
            sender.sendMessage("§cPlayer not found: " + playerName);
            return;
        }
        
        try {
            // Give all testaments for testing
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(target.getUniqueId());
            for (GodType god : GodType.values()) {
                data.completeTestament(god);
            }
            
            // Save data
            plugin.getPlayerDataManager().savePlayerData(target.getUniqueId());
            
            // Check for convergence
            plugin.getConvergenceManager().checkForConvergenceEligibility(target);
            
            sender.sendMessage("§aGiven all 12 testaments to " + target.getName());
            sender.sendMessage("§7Convergence Nexus should spawn automatically!");
            target.sendMessage("§6§lYou have been granted all divine testaments for testing!");
            target.sendMessage("§7Look for the Convergence Nexus to appear...");
        } catch (Exception e) {
            sender.sendMessage("§cError creating convergence nexus: " + e.getMessage());
            plugin.getLogger().severe("Convergence creation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}