package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.enums.AscensionLevel;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class GodlexCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public GodlexCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return true;
        }
        
        if (args.length == 0) {
            giveGodlexBook(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "book" -> giveGodlexBook(player);
            case "text" -> showGodlexText(player);
            case "god" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /godlex god <god_name>");
                    return true;
                }
                showGodInfo(player, args[1]);
            }
            default -> showHelp(player);
        }
        
        return true;
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§6§l=== Godlex Commands ===");
        player.sendMessage("§e/godlex §7- Get the Godlex book");
        player.sendMessage("§e/godlex book §7- Get the Godlex book");
        player.sendMessage("§e/godlex text §7- Show progress in chat");
        player.sendMessage("§e/godlex god <name> §7- Show specific god info");
    }
    
    private void giveGodlexBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        
        if (meta != null) {
            meta.setTitle("§6§lThe Godlex");
            meta.setAuthor("§5The Fallen Gods");
            
            List<String> pages = generateBookPages(player);
            meta.setPages(pages);
            
            book.setItemMeta(meta);
        }
        
        player.getInventory().addItem(book);
        player.sendMessage("§6§lThe Godlex §7has been added to your inventory!");
        player.sendMessage("§7Your personal guide to divine ascension...");
    }
    
    private List<String> generateBookPages(Player player) {
        List<String> pages = new ArrayList<>();
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        AscensionLevel ascension = plugin.getAscensionManager().getPlayerAscensionLevel(player);
        
        // Title Page
        pages.add("§6§l§nThe Godlex§r\n\n" +
                 "§8A Chronicle of Divine Power\n\n" +
                 "§7Player: §f" + player.getName() + "\n" +
                 "§7Ascension: " + ascension.getColoredName() + "\n" +
                 "§7Testaments: §f" + data.getCompletedTestaments().size() + "§7/§f12\n\n" +
                 "§8\"Power corrupts, but divine power... transforms.\"");
        
        // Progress Overview
        StringBuilder progressPage = new StringBuilder("§6§l§nYour Progress§r\n\n");
        progressPage.append("§7Fragments Collected: §f").append(data.getTotalFragments()).append("§7/§f84\n");
        progressPage.append("§7Completion: §f").append(data.getCompletionPercentage()).append("%\n");
        progressPage.append("§7Chests Opened: §f").append(data.getChestsOpened()).append("\n\n");
        
        progressPage.append("§6§lAscension Effects:§r\n");
        progressPage.append("§7").append(ascension.getDescription()).append("\n\n");
        
        if (!data.getCompletedTestaments().isEmpty()) {
            progressPage.append("§a§lCompleted:§r\n");
            for (GodType god : data.getCompletedTestaments()) {
                progressPage.append("§7• ").append(god.getColoredName()).append("\n");
            }
        }
        
        pages.add(progressPage.toString());
        
        // God Information Pages
        for (GodType god : GodType.values()) {
            StringBuilder godPage = new StringBuilder();
            godPage.append(god.getColor()).append("§l").append(god.getDisplayName()).append("§r\n");
            godPage.append("§8").append(god.getTitle()).append("\n\n");
            
            godPage.append("§7Theme: §f").append(god.getTheme()).append("\n\n");
            
            int fragmentCount = data.getFragments(god).size();
            if (data.hasCompletedTestament(god)) {
                godPage.append("§a§l✓ MASTERED§r\n");
                godPage.append("§7You have unlocked the full power of this divine testament.\n\n");
            } else if (fragmentCount == 7) {
                godPage.append("§e§l⚡ READY§r\n");
                godPage.append("§7Find the altar and complete your testament!\n\n");
            } else {
                godPage.append("§7Progress: §f").append(fragmentCount).append("§7/§f7\n");
                if (fragmentCount > 0) {
                    godPage.append("§7Fragments: ");
                    for (int i = 1; i <= 7; i++) {
                        if (data.hasFragment(god, i)) {
                            godPage.append("§a").append(i).append(" ");
                        } else {
                            godPage.append("§7").append(i).append(" ");
                        }
                    }
                    godPage.append("\n\n");
                }
            }
            
            // Add reward information
            godPage.append("§6§lReward:§r\n");
            godPage.append(getGodRewardDescription(god));
            
            pages.add(godPage.toString());
        }
        
        // Conflicts Page
        StringBuilder conflictsPage = new StringBuilder("§c§l§nDivine Conflicts§r\n\n");
        conflictsPage.append("§7Some gods are opposing forces. Mastering both may have consequences...\n\n");
        conflictsPage.append("§5Fallen §7vs §8Veil\n");
        conflictsPage.append("§cBanishment §7vs §3Abyssal\n");
        conflictsPage.append("§aSylvan §7vs §eTempest\n");
        conflictsPage.append("§6Forge §7vs §0Void\n");
        conflictsPage.append("§dTime §7vs §8Shadow\n");
        conflictsPage.append("§4Blood §7vs §bCrystal\n\n");
        conflictsPage.append("§8\"Balance is the key to true power.\"");
        
        pages.add(conflictsPage.toString());
        
        return pages;
    }
    
    private String getGodRewardDescription(GodType god) {
        return switch (god) {
            case FALLEN -> "§7Heart of Fallen God\n§7+15 Hearts, Ultimate Defense";
            case BANISHMENT -> "§7Ultimate Weapon Arsenal\n§7Overpowered Tools & Weapons";
            case ABYSSAL -> "§7Trident of Endless Deep\n§7Water Breathing, Ocean Mastery";
            case SYLVAN -> "§7Bow of Ancient Forest\n§7Nature Harmony, Regeneration";
            case TEMPEST -> "§7Wings of Storm Lord\n§7Enhanced Flight, Storm Power";
            case VEIL -> "§7Veil of Nullification\n§7Reality Manipulation, Heart Counter";
            case FORGE -> "§7Mace of Divine Forging\n§7Creation Mastery, Wind Burst";
            case VOID -> "§7Void Walker's Blade\n§7Teleportation, Phase Strike";
            case TIME -> "§7Chronos Staff\n§7Time Dilation, Temporal Control";
            case BLOOD -> "§7Crimson Blade\n§7Blood Frenzy, Life Steal";
            case CRYSTAL -> "§7Resonance Crystal\n§7Sonic Abilities, Ore Sense";
            case SHADOW -> "§7Shadow Mantle\n§7Stealth Mastery, Umbral Form";
        };
    }
    
    private void showGodlexText(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        AscensionLevel ascension = plugin.getAscensionManager().getPlayerAscensionLevel(player);
        
        player.sendMessage("§6§l=== THE GODLEX ===");
        player.sendMessage("§7Player: §f" + player.getName());
        player.sendMessage("§7Ascension Level: " + ascension.getColoredName());
        player.sendMessage("§7Testaments Completed: §f" + data.getCompletedTestaments().size() + "§7/§f12");
        player.sendMessage("§7Total Fragments: §f" + data.getTotalFragments() + "§7/§f84");
        player.sendMessage("§7Completion: §f" + data.getCompletionPercentage() + "%");
        player.sendMessage("");
        
        for (GodType god : GodType.values()) {
            int fragmentCount = data.getFragments(god).size();
            String status = data.hasCompletedTestament(god) ? "§a✓ MASTERED" : 
                           fragmentCount == 7 ? "§e⚡ READY" : "§7" + fragmentCount + "/7";
            
            player.sendMessage(god.getColor() + god.getDisplayName() + " §7- " + status);
        }
        
        player.sendMessage("");
        player.sendMessage("§7Use §e/godlex book §7for detailed information!");
    }
    
    private void showGodInfo(Player player, String godName) {
        GodType god = GodType.fromString(godName);
        if (god == null) {
            player.sendMessage("§cInvalid god name. Use /godlex to see all gods.");
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        player.sendMessage("§6§l=== " + god.getDisplayName() + " ===");
        player.sendMessage("§8" + god.getTitle());
        player.sendMessage("§7Theme: §f" + god.getTheme());
        player.sendMessage("");
        
        int fragmentCount = data.getFragments(god).size();
        if (data.hasCompletedTestament(god)) {
            player.sendMessage("§a§l✓ TESTAMENT MASTERED");
            player.sendMessage("§7You have unlocked the full power of this god.");
        } else if (fragmentCount == 7) {
            player.sendMessage("§e§l⚡ READY FOR COMPLETION");
            player.sendMessage("§7Find the altar and complete your testament!");
        } else {
            player.sendMessage("§7Progress: §f" + fragmentCount + "§7/§f7 fragments");
            if (fragmentCount > 0) {
                StringBuilder fragments = new StringBuilder("§7Collected: ");
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
        
        player.sendMessage("");
        player.sendMessage("§6Reward: §f" + getGodRewardDescription(god));
    }
}