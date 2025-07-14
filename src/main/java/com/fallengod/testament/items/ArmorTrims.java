package com.fallengod.testament.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.Registry;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class ArmorTrims {
    
    private static final List<String> TRIM_MATERIALS = Arrays.asList(
        "amethyst", "copper", "diamond", "emerald", "gold", 
        "iron", "lapis", "netherite", "quartz", "redstone"
    );
    
    private static int currentTrimIndex = 0;
    
    public static void startTrimRotation(JavaPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                currentTrimIndex = (currentTrimIndex + 1) % TRIM_MATERIALS.size();
                
                // Update all fallen armor pieces for online players
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    updatePlayerArmorTrims(player.getInventory().getArmorContents());
                });
            }
        }.runTaskTimer(plugin, 0L, 1200L); // Every 60 seconds (1200 ticks)
    }
    
    public static void updatePlayerArmorTrims(ItemStack[] armorContents) {
        for (ItemStack armor : armorContents) {
            if (armor != null && isFallenArmor(armor)) {
                applyRotatingTrim(armor);
            }
        }
    }
    
    public static void applyRotatingTrim(ItemStack armor) {
        if (armor == null || !armor.hasItemMeta()) {
            return;
        }
        
        ItemMeta meta = armor.getItemMeta();
        if (!(meta instanceof ArmorMeta armorMeta)) {
            return;
        }
        
        try {
            // Get silence pattern
            TrimPattern silencePattern = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("silence"));
            if (silencePattern == null) {
                return;
            }
            
            // Get current trim material
            String materialName = TRIM_MATERIALS.get(currentTrimIndex);
            TrimMaterial trimMaterial = Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft(materialName));
            
            if (trimMaterial != null) {
                ArmorTrim trim = new ArmorTrim(trimMaterial, silencePattern);
                armorMeta.setTrim(trim);
                
                // Update lore to show current trim
                List<String> lore = armorMeta.getLore();
                if (lore != null) {
                    // Remove old trim info
                    lore.removeIf(line -> line.contains("Trim:"));
                    
                    // Add new trim info
                    lore.add("§7Trim: §f" + capitalizeFirst(materialName) + " Silence");
                    lore.add("§8Rotates every 60 seconds");
                    
                    armorMeta.setLore(lore);
                }
                
                armor.setItemMeta(armorMeta);
            }
        } catch (Exception e) {
            // Silently fail if trim system isn't available
        }
    }
    
    public static ItemStack createFallenHelmet() {
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§5§lHelmet of the Fallen God");
            meta.setLore(Arrays.asList(
                "§7Blessed by the Fallen God himself,",
                "§7this helmet grants divine protection.",
                "",
                "§5Set Bonus (4/4):§r",
                "§7• Complete Void Immunity",
                "§7• 50% Fatal Damage Resistance", 
                "§7• Undead Neutrality",
                "§7• Death Effect Immunity",
                "",
                "§8Divine Armor Piece"
            ));
            
            // Add enchantments
            meta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION, 7, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 10, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.RESPIRATION, 5, true);
            
            meta.setCustomModelData(100010);
            meta.setEnchantmentGlintOverride(true);
            
            // Mark as fallen armor
            meta.getPersistentDataContainer().set(SpecialItems.FALLEN_ARMOR_KEY, 
                org.bukkit.persistence.PersistentDataType.BOOLEAN, true);
            
            helmet.setItemMeta(meta);
        }
        
        // Apply initial trim
        applyRotatingTrim(helmet);
        
        return helmet;
    }
    
    public static ItemStack createFallenChestplate() {
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = chestplate.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§5§lChestplate of the Fallen God");
            meta.setLore(Arrays.asList(
                "§7The core protection of the fallen,",
                "§7radiating with dark divine energy.",
                "",
                "§5Set Bonus (4/4):§r",
                "§7• Complete Void Immunity",
                "§7• 50% Fatal Damage Resistance",
                "§7• Undead Neutrality", 
                "§7• Death Effect Immunity",
                "",
                "§8Divine Armor Piece"
            ));
            
            // Add enchantments
            meta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION, 7, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 10, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.THORNS, 5, true);
            
            meta.setCustomModelData(100011);
            meta.setEnchantmentGlintOverride(true);
            
            meta.getPersistentDataContainer().set(SpecialItems.FALLEN_ARMOR_KEY, 
                org.bukkit.persistence.PersistentDataType.BOOLEAN, true);
            
            chestplate.setItemMeta(meta);
        }
        
        applyRotatingTrim(chestplate);
        return chestplate;
    }
    
    public static ItemStack createFallenLeggings() {
        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta meta = leggings.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§5§lLeggings of the Fallen God");
            meta.setLore(Arrays.asList(
                "§7Leg protection infused with the",
                "§7power of death and undeath.",
                "",
                "§5Set Bonus (4/4):§r",
                "§7• Complete Void Immunity",
                "§7• 50% Fatal Damage Resistance",
                "§7• Undead Neutrality",
                "§7• Death Effect Immunity", 
                "",
                "§8Divine Armor Piece"
            ));
            
            // Add enchantments
            meta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION, 7, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 10, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.SWIFT_SNEAK, 3, true);
            
            meta.setCustomModelData(100012);
            meta.setEnchantmentGlintOverride(true);
            
            meta.getPersistentDataContainer().set(SpecialItems.FALLEN_ARMOR_KEY, 
                org.bukkit.persistence.PersistentDataType.BOOLEAN, true);
            
            leggings.setItemMeta(meta);
        }
        
        applyRotatingTrim(leggings);
        return leggings;
    }
    
    public static ItemStack createFallenBoots() {
        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta meta = boots.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§5§lBoots of the Fallen God");
            meta.setLore(Arrays.asList(
                "§7Footwear that treads between",
                "§7the realm of life and death.",
                "",
                "§5Set Bonus (4/4):§r",
                "§7• Complete Void Immunity",
                "§7• 50% Fatal Damage Resistance",
                "§7• Undead Neutrality",
                "§7• Death Effect Immunity",
                "",
                "§8Divine Armor Piece"
            ));
            
            // Add enchantments
            meta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION, 7, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 10, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.FEATHER_FALLING, 10, true);
            meta.addEnchant(org.bukkit.enchantments.Enchantment.DEPTH_STRIDER, 3, true);
            
            meta.setCustomModelData(100013);
            meta.setEnchantmentGlintOverride(true);
            
            meta.getPersistentDataContainer().set(SpecialItems.FALLEN_ARMOR_KEY, 
                org.bukkit.persistence.PersistentDataType.BOOLEAN, true);
            
            boots.setItemMeta(meta);
        }
        
        applyRotatingTrim(boots);
        return boots;
    }
    
    private static boolean isFallenArmor(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(SpecialItems.FALLEN_ARMOR_KEY, 
            org.bukkit.persistence.PersistentDataType.BOOLEAN);
    }
    
    private static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public static String getCurrentTrimMaterial() {
        return TRIM_MATERIALS.get(currentTrimIndex);
    }
}