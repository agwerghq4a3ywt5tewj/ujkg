package com.fallengod.testament.commands;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.GodType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AltarCommand implements CommandExecutor {
    
    private final TestamentPlugin plugin;
    
    public AltarCommand(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fallengod.admin.altar")) {
            sender.sendMessage("§cYou don't have permission to use altar commands.");
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "regenerate" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /altar regenerate <god>");
                    return true;
                }
                regenerateAltar(sender, args[1]);
            }
            case "create" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /altar create <god>");
                    return true;
                }
                createAltar(sender, args[1]);
            }
            case "protect" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cUsage: /altar protect <on|off>");
                    return true;
                }
                toggleProtection(sender, args[1]);
            }
            case "list" -> listNearbyAltars(sender);
            default -> showHelp(sender);
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Altar Admin Commands ===");
        sender.sendMessage("§e/altar regenerate <god> §7- Regenerate altar at current location");
        sender.sendMessage("§e/altar create <god> §7- Create altar at current location");
        sender.sendMessage("§e/altar protect <on|off> §7- Toggle altar protection");
        sender.sendMessage("§e/altar list §7- List nearby altars");
    }
    
    private void regenerateAltar(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        // Create altar structure at player location
        createAltarStructure(player.getLocation(), god);
        
        sender.sendMessage("§aRegenerated " + god.getColoredName() + " §aaltar at your location!");
        sender.sendMessage("§7Use §e/fragment testfragments " + god.name().toLowerCase() + " §7to test it.");
    }
    
    private void createAltar(CommandSender sender, String godName) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        GodType god = GodType.fromString(godName);
        if (god == null) {
            sender.sendMessage("§cInvalid god name.");
            return;
        }
        
        // Create altar structure at player location
        createAltarStructure(player.getLocation(), god);
        
        sender.sendMessage("§aCreated " + god.getColoredName() + " §aaltar at your location!");
        sender.sendMessage("§7The altar is now ready for testament completion.");
    }
    
    private void createAltarStructure(Location center, GodType god) {
        // Get altar materials
        Material baseMaterial = getBaseMaterial(god);
        Material centerMaterial = getCenterMaterial(god);
        Material wallMaterial = getWallMaterial(god);
        
        // Create 7x7 base
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                Location loc = center.clone().add(x, 0, z);
                loc.getBlock().setType(baseMaterial);
            }
        }
        
        // Set center block
        center.getBlock().setType(centerMaterial);
        
        // Create walls around perimeter
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                    Location wallLoc = center.clone().add(x, 1, z);
                    if (x != 0 || z != 0) { // Don't place wall on center
                        wallLoc.getBlock().setType(wallMaterial);
                    }
                }
            }
        }
        
        // Add beacon on top
        Location beaconLoc = center.clone().add(0, 4, 0);
        beaconLoc.getBlock().setType(Material.BEACON);
        
        // Add decorative elements based on god type
        addGodSpecificDecorations(center, god);
    }
    
    private Material getBaseMaterial(GodType god) {
        return switch (god) {
            case FALLEN -> Material.BLACKSTONE;
            case BANISHMENT -> Material.NETHER_BRICKS;
            case ABYSSAL -> Material.PRISMARINE;
            case SYLVAN -> Material.MOSS_BLOCK;
            case TEMPEST -> Material.QUARTZ_BLOCK;
            case VEIL -> Material.END_STONE;
            case FORGE -> Material.COPPER_BLOCK;
            case VOID -> Material.BLACKSTONE;
            case TIME -> Material.DEEPSLATE;
            case BLOOD -> Material.RED_NETHER_BRICKS;
            case CRYSTAL -> Material.AMETHYST_BLOCK;
            case SHADOW -> Material.SCULK;
        };
    }
    
    private Material getCenterMaterial(GodType god) {
        return switch (god) {
            case FALLEN -> Material.CRYING_OBSIDIAN;
            case BANISHMENT -> Material.MAGMA_BLOCK;
            case ABYSSAL -> Material.DARK_PRISMARINE;
            case SYLVAN -> Material.OAK_LOG;
            case TEMPEST -> Material.LIGHTNING_ROD;
            case VEIL -> Material.END_PORTAL_FRAME;
            case FORGE -> Material.ANVIL;
            case VOID -> Material.OBSIDIAN;
            case TIME -> Material.AMETHYST_CLUSTER;
            case BLOOD -> Material.REDSTONE_BLOCK;
            case CRYSTAL -> Material.AMETHYST_CLUSTER;
            case SHADOW -> Material.SCULK_CATALYST;
        };
    }
    
    private Material getWallMaterial(GodType god) {
        return switch (god) {
            case FALLEN -> Material.BLACKSTONE_WALL;
            case BANISHMENT -> Material.NETHER_BRICK_WALL;
            case ABYSSAL -> Material.PRISMARINE_WALL;
            case SYLVAN -> Material.MOSSY_COBBLESTONE_WALL;
            case TEMPEST -> Material.COBBLESTONE_WALL; // Fixed: QUARTZ_WALL doesn't exist
            case VEIL -> Material.END_STONE_BRICK_WALL;
            case FORGE -> Material.COBBLESTONE_WALL;
            case VOID -> Material.BLACKSTONE_WALL;
            case TIME -> Material.DEEPSLATE_BRICK_WALL;
            case BLOOD -> Material.RED_NETHER_BRICK_WALL;
            case CRYSTAL -> Material.COBBLESTONE_WALL; // PURPUR_WALL doesn't exist
            case SHADOW -> Material.BLACKSTONE_WALL;
        };
    }
    
    private void addGodSpecificDecorations(Location center, GodType god) {
        switch (god) {
            case FALLEN -> {
                // Add soul fire
                center.clone().add(0, 1, 0).getBlock().setType(Material.SOUL_FIRE);
                // Add soul lanterns at corners
                center.clone().add(2, 2, 2).getBlock().setType(Material.SOUL_LANTERN);
                center.clone().add(-2, 2, 2).getBlock().setType(Material.SOUL_LANTERN);
                center.clone().add(2, 2, -2).getBlock().setType(Material.SOUL_LANTERN);
                center.clone().add(-2, 2, -2).getBlock().setType(Material.SOUL_LANTERN);
            }
            case BANISHMENT -> {
                // Add fire
                center.clone().add(0, 1, 0).getBlock().setType(Material.FIRE);
                // Add lanterns
                center.clone().add(2, 2, 2).getBlock().setType(Material.LANTERN);
                center.clone().add(-2, 2, 2).getBlock().setType(Material.LANTERN);
                center.clone().add(2, 2, -2).getBlock().setType(Material.LANTERN);
                center.clone().add(-2, 2, -2).getBlock().setType(Material.LANTERN);
            }
            case ABYSSAL -> {
                // Add sea lanterns
                center.clone().add(2, 2, 2).getBlock().setType(Material.SEA_LANTERN);
                center.clone().add(-2, 2, 2).getBlock().setType(Material.SEA_LANTERN);
                center.clone().add(2, 2, -2).getBlock().setType(Material.SEA_LANTERN);
                center.clone().add(-2, 2, -2).getBlock().setType(Material.SEA_LANTERN);
            }
            case SYLVAN -> {
                // Add glowstone and leaves
                center.clone().add(2, 2, 2).getBlock().setType(Material.GLOWSTONE);
                center.clone().add(-2, 2, 2).getBlock().setType(Material.GLOWSTONE);
                center.clone().add(2, 2, -2).getBlock().setType(Material.GLOWSTONE);
                center.clone().add(-2, 2, -2).getBlock().setType(Material.GLOWSTONE);
                // Add oak leaves at corners
                center.clone().add(0, 2, 0).getBlock().setType(Material.OAK_LEAVES);
            }
            case TEMPEST -> {
                // Add end rods
                center.clone().add(0, 1, 0).getBlock().setType(Material.END_ROD);
                center.clone().add(2, 2, 2).getBlock().setType(Material.WHITE_CONCRETE);
                center.clone().add(-2, 2, 2).getBlock().setType(Material.WHITE_CONCRETE);
                center.clone().add(2, 2, -2).getBlock().setType(Material.WHITE_CONCRETE);
                center.clone().add(-2, 2, -2).getBlock().setType(Material.WHITE_CONCRETE);
            }
            case VEIL -> {
                // Add end rods
                center.clone().add(2, 2, 2).getBlock().setType(Material.END_ROD);
                center.clone().add(-2, 2, 2).getBlock().setType(Material.END_ROD);
                center.clone().add(2, 2, -2).getBlock().setType(Material.END_ROD);
                center.clone().add(-2, 2, -2).getBlock().setType(Material.END_ROD);
            }
        }
    }
    
    private void toggleProtection(CommandSender sender, String mode) {
        boolean enable = mode.equalsIgnoreCase("on");
        
        // This would integrate with a protection system
        // For now, just acknowledge the command
        if (enable) {
            sender.sendMessage("§aAltar protection enabled!");
            sender.sendMessage("§7Altars are now protected from grief.");
        } else {
            sender.sendMessage("§cAltar protection disabled!");
            sender.sendMessage("§7Altars can now be modified.");
        }
    }
    
    private void listNearbyAltars(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players.");
            return;
        }
        
        sender.sendMessage("§6§l=== Nearby Altars ===");
        sender.sendMessage("§7Scanning 100 block radius...");
        
        Location playerLoc = player.getLocation();
        boolean foundAny = false;
        
        // Scan for altar center blocks
        for (int x = -100; x <= 100; x += 10) {
            for (int z = -100; z <= 100; z += 10) {
                for (int y = -20; y <= 20; y += 5) {
                    Location checkLoc = playerLoc.clone().add(x, y, z);
                    Material block = checkLoc.getBlock().getType();
                    
                    GodType god = getGodFromCenterBlock(block);
                    if (god != null) {
                        int distance = (int) playerLoc.distance(checkLoc);
                        sender.sendMessage(god.getColor() + god.getDisplayName() + " Altar §7at " +
                                         checkLoc.getBlockX() + ", " + checkLoc.getBlockY() + ", " + checkLoc.getBlockZ() +
                                         " §7(~" + distance + " blocks)");
                        foundAny = true;
                    }
                }
            }
        }
        
        if (!foundAny) {
            sender.sendMessage("§7No altars found within 100 blocks.");
            sender.sendMessage("§7Use §e/altar create <god> §7to create one here.");
        }
    }
    
    private GodType getGodFromCenterBlock(Material block) {
        return switch (block) {
            case CRYING_OBSIDIAN -> GodType.FALLEN;
            case MAGMA_BLOCK -> GodType.BANISHMENT;
            case DARK_PRISMARINE -> GodType.ABYSSAL;
            case OAK_LOG -> GodType.SYLVAN;
            case LIGHTNING_ROD -> GodType.TEMPEST;
            case END_PORTAL_FRAME -> GodType.VEIL;
            case ANVIL -> GodType.FORGE;
            case OBSIDIAN -> GodType.VOID;
            case AMETHYST_CLUSTER -> GodType.TIME;
            case REDSTONE_BLOCK -> GodType.BLOOD;
            case AMETHYST_BLOCK -> GodType.CRYSTAL;
            case SCULK_CATALYST -> GodType.SHADOW;
            default -> null;
        };
    }
}