package com.fallengod.testament.items;

import com.fallengod.testament.enums.GodType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class FragmentItem {
    
    public static final NamespacedKey GOD_KEY = new NamespacedKey("fallengod", "god_type");
    public static final NamespacedKey FRAGMENT_KEY = new NamespacedKey("fallengod", "fragment_number");
    
    public static ItemStack createFragment(GodType god, int fragmentNumber) {
        ItemStack item = new ItemStack(getFragmentMaterial(god));
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name
            meta.setDisplayName(god.getColor() + god.getDisplayName() + " Fragment " + fragmentNumber);
            
            // Set lore
            List<String> lore = Arrays.asList(
                "§7A fragment of divine power from the",
                god.getColor() + god.getDisplayName(),
                "§7Theme: §f" + god.getTheme(),
                "§7Title: §f" + god.getTitle(),
                "",
                "§6Fragment " + fragmentNumber + " of 7",
                "",
                "§7Collect all 7 fragments to unlock",
                "§7the testament of this fallen god.",
                "",
                "§8Divine Fragment"
            );
            meta.setLore(lore);
            
            // Set custom data
            meta.getPersistentDataContainer().set(GOD_KEY, PersistentDataType.STRING, god.name());
            meta.getPersistentDataContainer().set(FRAGMENT_KEY, PersistentDataType.INTEGER, fragmentNumber);
            
            // Make it glow
            meta.setEnchantmentGlintOverride(true);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static boolean isFragment(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(GOD_KEY, PersistentDataType.STRING) &&
               meta.getPersistentDataContainer().has(FRAGMENT_KEY, PersistentDataType.INTEGER);
    }
    
    public static GodType getFragmentGod(ItemStack item) {
        if (!isFragment(item)) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        String godName = meta.getPersistentDataContainer().get(GOD_KEY, PersistentDataType.STRING);
        return GodType.fromString(godName);
    }
    
    public static int getFragmentNumber(ItemStack item) {
        if (!isFragment(item)) {
            return -1;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(FRAGMENT_KEY, PersistentDataType.INTEGER);
    }
    
    private static Material getFragmentMaterial(GodType god) {
        return switch (god) {
            case FALLEN -> Material.NETHERITE_SCRAP;
            case BANISHMENT -> Material.BLAZE_POWDER;
            case ABYSSAL -> Material.PRISMARINE_SHARD;
            case SYLVAN -> Material.EMERALD;
            case TEMPEST -> Material.AMETHYST_SHARD;
            case VEIL -> Material.ECHO_SHARD;
            case FORGE -> Material.COPPER_INGOT;
            case VOID -> Material.OBSIDIAN;
            case TIME -> Material.CLOCK;
            case BLOOD -> Material.REDSTONE;
            case CRYSTAL -> Material.QUARTZ;
            case SHADOW -> Material.COAL;
        };
    }
}