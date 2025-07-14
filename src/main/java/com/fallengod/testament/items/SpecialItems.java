package com.fallengod.testament.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class SpecialItems {
    
    public static final NamespacedKey HEART_KEY = new NamespacedKey("fallengod", "heart_of_fallen_god");
    public static final NamespacedKey VEIL_KEY = new NamespacedKey("fallengod", "veil_of_nullification");
    public static final NamespacedKey ABYSSAL_TRIDENT_KEY = new NamespacedKey("fallengod", "abyssal_trident");
    public static final NamespacedKey SYLVAN_BOW_KEY = new NamespacedKey("fallengod", "sylvan_bow");
    public static final NamespacedKey STORM_ELYTRA_KEY = new NamespacedKey("fallengod", "storm_elytra");
    public static final NamespacedKey BANISHMENT_WEAPON_KEY = new NamespacedKey("fallengod", "banishment_weapon");
    public static final NamespacedKey FALLEN_ARMOR_KEY = new NamespacedKey("fallengod", "fallen_armor");
    public static final NamespacedKey SHADOW_CLOAK_KEY = new NamespacedKey("fallengod", "shadow_cloak");
    public static final NamespacedKey SHADOW_MANTLE_KEY = new NamespacedKey("fallengod", "shadow_mantle");
    
    public static ItemStack createHeartOfFallenGod() {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§5§lHeart of the Fallen God");
            meta.setLore(Arrays.asList(
                "§7The ultimate power of death itself,",
                "§7granting unparalleled protection.",
                "",
                "§6Effects:",
                "§7• +15 Hearts (25 total)",
                "§7• Strength I",
                "§7• Regeneration II", 
                "§7• Resistance I",
                "",
                "§c⚠ Can be nullified by Veil of Nullification",
                "",
                "§8Testament Reward"
            ));
            
            meta.getPersistentDataContainer().set(HEART_KEY, PersistentDataType.BOOLEAN, true);
            meta.setEnchantmentGlintOverride(true);
            meta.addEnchant(Enchantment.UNBREAKING, 10, true);
            meta.setCustomModelData(100001);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static ItemStack createVeilOfNullification() {
        ItemStack item = new ItemStack(Material.PHANTOM_MEMBRANE);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§8§lVeil of Nullification");
            meta.setLore(Arrays.asList(
                "§7Manipulates reality itself to counter",
                "§7the Heart of the Fallen God's power.",
                "",
                "§6Effects:",
                "§7• Nullifies Heart within 16 blocks",
                "§7• Slow Falling",
                "§7• Night Vision",
                "§7• Speed I",
                "",
                "§a✓ Counters Heart of Fallen God",
                "",
                "§8Testament Reward"
            ));
            
            meta.getPersistentDataContainer().set(VEIL_KEY, PersistentDataType.BOOLEAN, true);
            meta.setEnchantmentGlintOverride(true);
            meta.addEnchant(Enchantment.UNBREAKING, 10, true);
            meta.setCustomModelData(100002);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static ItemStack createShadowMantle() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§8§lShadow Mantle");
            meta.setLore(Arrays.asList(
                "§7Woven from the void between worlds,",
                "§7this mantle grants mastery over shadows.",
                "",
                "§8+Umbral Form (Sneak in darkness)",
                "§8+Shadow Strike (Right-click)",
                "§8+Silent Movement",
                "",
                "§8Testament Reward"
            ));
            
            meta.getPersistentDataContainer().set(SHADOW_MANTLE_KEY, PersistentDataType.BOOLEAN, true);
            meta.setEnchantmentGlintOverride(true);
            meta.addEnchant(Enchantment.UNBREAKING, 10, true);
            meta.setCustomModelData(100065);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public static boolean isHeartOfFallenGod(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(HEART_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isVeilOfNullification(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(VEIL_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isAbyssalTrident(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(ABYSSAL_TRIDENT_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isSylvanBow(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(SYLVAN_BOW_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isStormElytra(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(STORM_ELYTRA_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isBanishmentWeapon(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(BANISHMENT_WEAPON_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isFallenArmor(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(FALLEN_ARMOR_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isShadowCloak(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(SHADOW_CLOAK_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isShadowMantle(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(SHADOW_MANTLE_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isTestamentItem(ItemStack item) {
        return isHeartOfFallenGod(item) || isVeilOfNullification(item) || 
               isAbyssalTrident(item) || isSylvanBow(item) || 
               isStormElytra(item) || isBanishmentWeapon(item) || 
               isFallenArmor(item) || isShadowCloak(item) || isShadowMantle(item);
    }
}