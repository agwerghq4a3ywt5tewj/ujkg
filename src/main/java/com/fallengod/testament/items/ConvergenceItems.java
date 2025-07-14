package com.fallengod.testament.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ConvergenceItems {
    
    public static final NamespacedKey NEXUS_CROWN_KEY = new NamespacedKey("fallengod", "nexus_crown");
    public static final NamespacedKey OMNIPOTENT_SCEPTER_KEY = new NamespacedKey("fallengod", "omnipotent_scepter");
    public static final NamespacedKey DIVINE_CODEX_KEY = new NamespacedKey("fallengod", "divine_codex");
    
    public static ItemStack createNexusCrown() {
        ItemStack crown = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = crown.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6§l✦ Crown of Divine Convergence ✦");
            meta.setLore(Arrays.asList(
                "§7The ultimate symbol of divine mastery,",
                "§7worn only by those who have united",
                "§7all twelve fallen gods under one will.",
                "",
                "§6§lEffects:",
                "§7• All Testament Powers Combined",
                "§7• +20 Hearts (30 total)",
                "§7• Immunity to All Damage Types",
                "§7• Flight (Creative Mode)",
                "§7• Infinite Resources",
                "§7• Reality Manipulation",
                "",
                "§8§l\"I am become Death, destroyer of worlds.\"",
                "§8§l\"I am become Life, creator of realms.\"",
                "",
                "§6§lCONVERGENCE NEXUS REWARD"
            ));
            
            // Ultimate enchantments
            meta.addEnchant(Enchantment.PROTECTION, 10, true);
            meta.addEnchant(Enchantment.FIRE_PROTECTION, 10, true);
            meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 10, true);
            meta.addEnchant(Enchantment.BLAST_PROTECTION, 10, true);
            meta.addEnchant(Enchantment.UNBREAKING, 20, true);
            meta.addEnchant(Enchantment.MENDING, 5, true);
            meta.addEnchant(Enchantment.RESPIRATION, 10, true);
            meta.addEnchant(Enchantment.AQUA_AFFINITY, 5, true);
            
            meta.getPersistentDataContainer().set(NEXUS_CROWN_KEY, PersistentDataType.BOOLEAN, true);
            meta.setEnchantmentGlintOverride(true);
            meta.setCustomModelData(100100); // Special model data for crown
            
            crown.setItemMeta(meta);
        }
        
        return crown;
    }
    
    public static ItemStack createOmnipotentScepter() {
        ItemStack scepter = new ItemStack(Material.STICK);
        ItemMeta meta = scepter.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§d§l⚡ Scepter of Omnipotence ⚡");
            meta.setLore(Arrays.asList(
                "§7A weapon that transcends all others,",
                "§7forged from the essence of twelve gods.",
                "",
                "§d§lAbilities:",
                "§7• Right-click: Divine Judgment",
                "§7• Sneak + Right-click: Reality Warp",
                "§7• Attack: Omnislash (1000 damage)",
                "§7• Passive: All Weapon Abilities",
                "",
                "§8\"With this scepter, I command the very\"",
                "§8\"fabric of existence itself.\"",
                "",
                "§6§lCONVERGENCE NEXUS REWARD"
            ));
            
            // Ultimate weapon enchantments
            meta.addEnchant(Enchantment.SHARPNESS, 20, true);
            meta.addEnchant(Enchantment.UNBREAKING, 50, true);
            meta.addEnchant(Enchantment.MENDING, 10, true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 10, true);
            meta.addEnchant(Enchantment.KNOCKBACK, 5, true);
            meta.addEnchant(Enchantment.LOOTING, 10, true);
            
            meta.getPersistentDataContainer().set(OMNIPOTENT_SCEPTER_KEY, PersistentDataType.BOOLEAN, true);
            meta.setEnchantmentGlintOverride(true);
            meta.setCustomModelData(100101); // Special model data for scepter
            
            scepter.setItemMeta(meta);
        }
        
        return scepter;
    }
    
    public static ItemStack createDivineCodex() {
        ItemStack codex = new ItemStack(Material.WRITTEN_BOOK);
        org.bukkit.inventory.meta.BookMeta meta = (org.bukkit.inventory.meta.BookMeta) codex.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§b§l📖 Divine Codex of All Knowledge 📖");
            meta.setLore(Arrays.asList(
                "§7The complete knowledge of all twelve",
                "§7fallen gods, their powers, and the",
                "§7secrets of divine convergence.",
                "",
                "§b§lContains:",
                "§7• All God Lore and Histories",
                "§7• Testament Completion Guides",
                "§7• Divine Power Combinations",
                "§7• Reality Manipulation Techniques",
                "§7• The True Names of the Gods",
                "",
                "§8\"Knowledge is the ultimate power.\"",
                "",
                "§6§lCONVERGENCE NEXUS REWARD"
            ));
            
            meta.addEnchant(Enchantment.UNBREAKING, 10, true);
            meta.getPersistentDataContainer().set(DIVINE_CODEX_KEY, PersistentDataType.BOOLEAN, true);
            meta.setEnchantmentGlintOverride(true);
            meta.setCustomModelData(100102); // Special model data for codex
            
            // Add actual readable content
            meta.setTitle("§b§lDivine Codex");
            meta.setAuthor("§6The Twelve Gods");
            meta.setPages(generateCodexPages());
            
            codex.setItemMeta(meta);
        }
        
        return codex;
    }
    
    public static boolean isNexusCrown(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(NEXUS_CROWN_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isOmnipotentScepter(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(OMNIPOTENT_SCEPTER_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isDivineCodex(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(DIVINE_CODEX_KEY, PersistentDataType.BOOLEAN);
    }
    
    public static boolean isConvergenceItem(ItemStack item) {
        return isNexusCrown(item) || isOmnipotentScepter(item) || isDivineCodex(item);
    }
    
    private static List<String> generateCodexPages() {
        List<String> pages = new ArrayList<>();
        
        // Title Page
        pages.add("§6§l§nDivine Codex§r\n§8§lof All Knowledge§r\n\n" +
                 "§7Written by the §6Twelve Fallen Gods§7 themselves, this tome contains the ultimate secrets of divine power.\n\n" +
                 "§8\"When all gods unite under one will,\nreality itself bends to their command.\"\n\n" +
                 "§7- §dThe Convergence Prophecy");
        
        // God Lore Pages
        pages.add("§5§l§nFallen God§r\n§8Guardian of the Afterlife§r\n\n" +
                 "§7Once the protector of souls, the Fallen God was cast down for defying the natural order of death. His heart still beats with the power to grant immortality.\n\n" +
                 "§5Powers:§r\n§7• Death Immunity\n§7• Soul Manipulation\n§7• Undeath Mastery\n\n" +
                 "§8\"Death is but a doorway I refuse to open.\"");
        
        pages.add("§c§l§nBanishment God§r\n§8Lord of Eternal Flames§r\n\n" +
                 "§7Exiled for his destructive rage, the Banishment God commands the fires of exile and destruction. His weapons burn with the fury of a thousand suns.\n\n" +
                 "§cPowers:§r\n§7• Infernal Weapons\n§7• Exile Magic\n§7• Destruction Mastery\n\n" +
                 "§8\"Let all who oppose me burn in eternal flame.\"");
        
        pages.add("§3§l§nAbyssal God§r\n§8Master of the Deep§r\n\n" +
                 "§7Ruler of the deepest oceans, the Abyssal God controls all waters and the creatures within. His trident commands the very tides.\n\n" +
                 "§3Powers:§r\n§7• Ocean Dominion\n§7• Water Breathing\n§7• Tidal Control\n\n" +
                 "§8\"The depths hold secrets mortals cannot fathom.\"");
        
        pages.add("§a§l§nSylvan God§r\n§8Guardian of the Wild§r\n\n" +
                 "§7Protector of all natural life, the Sylvan God was banished for refusing to let nature bow to civilization. His bow never misses its mark.\n\n" +
                 "§aPowers:§r\n§7• Nature Harmony\n§7• Plant Growth\n§7• Animal Communication\n\n" +
                 "§8\"Nature will reclaim what was always hers.\"");
        
        pages.add("§e§l§nTempest God§r\n§8Lord of the Storms§r\n\n" +
                 "§7Master of sky and storm, the Tempest God was cast down for bringing endless hurricanes. His wings grant dominion over the heavens.\n\n" +
                 "§ePowers:§r\n§7• Storm Control\n§7• Lightning Mastery\n§7• Flight\n\n" +
                 "§8\"The skies rage with my eternal fury.\"");
        
        pages.add("§8§l§nVeil God§r\n§8Weaver of Reality§r\n\n" +
                 "§7The most mysterious of the fallen, the Veil God manipulates reality itself. His veil can nullify any power, even that of other gods.\n\n" +
                 "§8Powers:§r\n§7• Reality Manipulation\n§7• Power Nullification\n§7• Dimensional Travel\n\n" +
                 "§8\"Reality is but a suggestion I choose to ignore.\"");
        
        // Divine Techniques
        pages.add("§6§l§nDivine Techniques§r\n\n" +
                 "§7The following techniques can only be mastered by those who have achieved convergence:\n\n" +
                 "§6Reality Shaping:§7 Alter the physical world at will\n\n" +
                 "§6Divine Judgment:§7 Instantly defeat any mortal foe\n\n" +
                 "§6Omniscience:§7 Know all things within your domain\n\n" +
                 "§6Creation:§7 Bring new life into existence");
        
        // Convergence Secrets
        pages.add("§d§l§nThe Convergence§r\n\n" +
                 "§7When all twelve testaments are mastered, the Convergence Nexus manifests. Only one may achieve this ultimate power.\n\n" +
                 "§dThe Ritual:§r\n§71. Master all gods\n§72. Find the Nexus\n§73. Touch the Netherite Block\n§74. Ascend to divinity\n\n" +
                 "§8\"I am Alpha and Omega, the beginning and the end.\"");
        
        // Final Page
        pages.add("§6§l§nFinal Words§r\n\n" +
                 "§7You who read this have achieved the impossible. You are no longer bound by mortal limitations.\n\n" +
                 "§7Use this power wisely, for with great power comes the responsibility to shape reality itself.\n\n" +
                 "§6May your reign be eternal,§r\n§8§lThe Twelve Fallen Gods");
        
        return pages;
    }
}