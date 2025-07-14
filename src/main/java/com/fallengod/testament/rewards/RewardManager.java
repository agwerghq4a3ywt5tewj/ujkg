package com.fallengod.testament.rewards;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.Registry;
import org.bukkit.NamespacedKey;

import java.util.Arrays;

public class RewardManager {
    
    private final TestamentPlugin plugin;
    
    public RewardManager(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void giveTestamentReward(Player player, GodType god) {
        switch (god) {
            case FALLEN -> giveFallenReward(player);
            case BANISHMENT -> giveBanishmentReward(player);
            case ABYSSAL -> giveAbyssalReward(player);
            case SYLVAN -> giveSylvanReward(player);
            case TEMPEST -> giveTempestReward(player);
            case VEIL -> giveVeilReward(player);
            case FORGE -> giveForgeReward(player);
            case VOID -> giveVoidReward(player);
            case TIME -> giveTimeReward(player);
            case BLOOD -> giveBloodReward(player);
            case CRYSTAL -> giveCrystalReward(player);
            case SHADOW -> giveShadowReward(player);
        }
    }
    
    private void giveFallenReward(Player player) {
        // Give Heart of Fallen God
        ItemStack heart = SpecialItems.createHeartOfFallenGod();
        player.getInventory().addItem(heart);
        
        // Give Netherite armor set
        giveNetheriteArmorSet(player);
        
        player.sendMessage("§5§lYou have received the Heart of the Fallen God!");
        player.sendMessage("§7Ultimate defense is now yours...");
    }
    
    private void giveBanishmentReward(Player player) {
        // Give ultimate weapon set
        ItemStack sword = createUltimateWeapon();
        ItemStack axe = createUltimateAxe();
        ItemStack pickaxe = createUltimatePickaxe();
        
        player.getInventory().addItem(sword, axe, pickaxe);
        
        player.sendMessage("§c§lYou have received the Arsenal of Banishment!");
        player.sendMessage("§7Ultimate destruction is at your command...");
    }
    
    private void giveAbyssalReward(Player player) {
        // Give Trident of the Endless Deep
        ItemStack trident = createAbyssalTrident();
        player.getInventory().addItem(trident);
        
        // Give permanent water effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 0, false, false));
        
        player.sendMessage("§3§lYou have received the Trident of the Endless Deep!");
        player.sendMessage("§7The oceans bow to your will...");
    }
    
    private void giveSylvanReward(Player player) {
        // Give Bow of the Ancient Forest
        ItemStack bow = createSylvanBow();
        player.getInventory().addItem(bow);
        
        // Give permanent nature effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 0, false, false));
        
        player.sendMessage("§a§lYou have received the Bow of the Ancient Forest!");
        player.sendMessage("§7Nature's harmony flows through you...");
    }
    
    private void giveTempestReward(Player player) {
        // Give Wings of the Storm Lord (Enhanced Elytra)
        ItemStack elytra = createStormElytra();
        player.getInventory().addItem(elytra);
        
        // Give permanent sky effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2, false, false));
        
        player.sendMessage("§e§lYou have received the Wings of the Storm Lord!");
        player.sendMessage("§7The skies are your domain...");
    }
    
    private void giveVeilReward(Player player) {
        // Give Veil of Nullification
        ItemStack veil = SpecialItems.createVeilOfNullification();
        player.getInventory().addItem(veil);
        
        player.sendMessage("§8§lYou have received the Veil of Nullification!");
        player.sendMessage("§7Reality bends to your will...");
    }
    
    private void giveForgeReward(Player player) {
        ItemStack mace = createForgeMace();
        player.getInventory().addItem(mace);
        
        player.sendMessage("§6§lYou have received the Mace of Divine Forging!");
        player.sendMessage("§7Creation itself bows to your will...");
    }
    
    private void giveVoidReward(Player player) {
        ItemStack blade = createVoidBlade();
        player.getInventory().addItem(blade);
        
        player.sendMessage("§0§lYou have received the Void Walker's Blade!");
        player.sendMessage("§7Nothingness is your domain...");
    }
    
    private void giveTimeReward(Player player) {
        ItemStack staff = createChronosStaff();
        player.getInventory().addItem(staff);
        
        player.sendMessage("§d§lYou have received the Chronos Staff!");
        player.sendMessage("§7Time flows at your command...");
    }
    
    private void giveBloodReward(Player player) {
        ItemStack blade = createCrimsonBlade();
        player.getInventory().addItem(blade);
        
        player.sendMessage("§4§lYou have received the Crimson Blade!");
        player.sendMessage("§7Battle is your eternal dance...");
    }
    
    private void giveCrystalReward(Player player) {
        ItemStack crystal = createResonanceCrystal();
        player.getInventory().addItem(crystal);
        
        player.sendMessage("§b§lYou have received the Resonance Crystal!");
        player.sendMessage("§7Harmony resonates through your being...");
    }
    
    private void giveShadowReward(Player player) {
        ItemStack mantle = SpecialItems.createShadowMantle();
        player.getInventory().addItem(mantle);
        
        player.sendMessage("§8§lYou have received the Shadow Mantle!");
        player.sendMessage("§7Darkness embraces you as its own...");
    }
    
    private void giveNetheriteArmorSet(Player player) {
        ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
        
        // Enchant armor
        enchantArmor(helmet);
        enchantArmor(chestplate);
        enchantArmor(leggings);
        enchantArmor(boots);
        
        player.getInventory().addItem(helmet, chestplate, leggings, boots);
    }
    
    private void enchantArmor(ItemStack armor) {
        armor.addUnsafeEnchantment(Enchantment.PROTECTION, 5);
        armor.addUnsafeEnchantment(Enchantment.FIRE_PROTECTION, 5);
        armor.addUnsafeEnchantment(Enchantment.PROJECTILE_PROTECTION, 5);
        armor.addUnsafeEnchantment(Enchantment.BLAST_PROTECTION, 7);
        armor.addUnsafeEnchantment(Enchantment.UNBREAKING, 5);
        armor.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = armor.getItemMeta();
        if (meta != null) {
            meta.setLore(Arrays.asList("§5Blessed by the Fallen God", "§7Ultimate Protection"));
            meta.setCustomModelData(100010 + getArmorSlot(armor.getType()));
            
            // Add Silence armor trim with rotating colors
            try {
                TrimPattern silencePattern = Registry.TRIM_PATTERN.get(NamespacedKey.minecraft("silence"));
                if (silencePattern != null) {
                    // Use different trim materials for color variety
                    TrimMaterial trimMaterial = switch (getArmorSlot(armor.getType())) {
                        case 0 -> Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("amethyst")); // Helmet - Purple
                        case 1 -> Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("netherite")); // Chestplate - Dark
                        case 2 -> Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("diamond")); // Leggings - Blue
                        case 3 -> Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("emerald")); // Boots - Green
                        default -> Registry.TRIM_MATERIAL.get(NamespacedKey.minecraft("netherite"));
                    };
                    
                    if (trimMaterial != null && meta instanceof org.bukkit.inventory.meta.ArmorMeta armorMeta) {
                        ArmorTrim trim = new ArmorTrim(trimMaterial, silencePattern);
                        armorMeta.setTrim(trim);
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Could not apply armor trim: " + e.getMessage());
            }
            
            armor.setItemMeta(meta);
        }
    }
    
    private int getArmorSlot(Material armorType) {
        return switch (armorType) {
            case NETHERITE_HELMET -> 0;
            case NETHERITE_CHESTPLATE -> 1;
            case NETHERITE_LEGGINGS -> 2;
            case NETHERITE_BOOTS -> 3;
            default -> 0;
        };
    }
    
    private ItemStack createUltimateWeapon() {
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        sword.addUnsafeEnchantment(Enchantment.SHARPNESS, 10);
        sword.addUnsafeEnchantment(Enchantment.UNBREAKING, 7);
        sword.addUnsafeEnchantment(Enchantment.MENDING, 1);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        sword.addUnsafeEnchantment(Enchantment.LOOTING, 5);
        
        ItemMeta meta = sword.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lBlade of Eternal Banishment");
            meta.setLore(Arrays.asList(
                "§7Forged in the fires of exile,",
                "§7this blade knows no mercy.",
                "§c+Ultimate Destruction"
            ));
            meta.setCustomModelData(100020);
            meta.getPersistentDataContainer().set(SpecialItems.BANISHMENT_WEAPON_KEY, PersistentDataType.BOOLEAN, true);
            sword.setItemMeta(meta);
        }
        
        return sword;
    }
    
    private ItemStack createUltimateAxe() {
        ItemStack axe = new ItemStack(Material.NETHERITE_AXE);
        axe.addUnsafeEnchantment(Enchantment.SHARPNESS, 8);
        axe.addUnsafeEnchantment(Enchantment.EFFICIENCY, 7);
        axe.addUnsafeEnchantment(Enchantment.UNBREAKING, 7);
        axe.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = axe.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lAxe of Devastation");
            meta.setLore(Arrays.asList("§7Cleaves through anything", "§c+Ultimate Power"));
            meta.setCustomModelData(100021);
            meta.getPersistentDataContainer().set(SpecialItems.BANISHMENT_WEAPON_KEY, PersistentDataType.BOOLEAN, true);
            axe.setItemMeta(meta);
        }
        
        return axe;
    }
    
    private ItemStack createUltimatePickaxe() {
        ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        pickaxe.addUnsafeEnchantment(Enchantment.EFFICIENCY, 10);
        pickaxe.addUnsafeEnchantment(Enchantment.FORTUNE, 5);
        pickaxe.addUnsafeEnchantment(Enchantment.UNBREAKING, 7);
        pickaxe.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = pickaxe.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lPickaxe of Endless Mining");
            meta.setLore(Arrays.asList("§7Breaks the earth itself", "§c+Ultimate Efficiency"));
            meta.setCustomModelData(100022);
            meta.getPersistentDataContainer().set(SpecialItems.BANISHMENT_WEAPON_KEY, PersistentDataType.BOOLEAN, true);
            pickaxe.setItemMeta(meta);
        }
        
        return pickaxe;
    }
    
    private ItemStack createAbyssalTrident() {
        ItemStack trident = new ItemStack(Material.TRIDENT);
        trident.addUnsafeEnchantment(Enchantment.LOYALTY, 5);
        trident.addUnsafeEnchantment(Enchantment.CHANNELING, 1);
        trident.addUnsafeEnchantment(Enchantment.RIPTIDE, 5);
        trident.addUnsafeEnchantment(Enchantment.UNBREAKING, 7);
        trident.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = trident.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§3§lTrident of the Endless Deep");
            meta.setLore(Arrays.asList(
                "§7Commands the power of all oceans,",
                "§7storms, and depths unknown.",
                "§3+Water Mastery"
            ));
            meta.setCustomModelData(100030);
            meta.getPersistentDataContainer().set(SpecialItems.ABYSSAL_TRIDENT_KEY, PersistentDataType.BOOLEAN, true);
            trident.setItemMeta(meta);
        }
        
        return trident;
    }
    
    private ItemStack createSylvanBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.POWER, 7);
        bow.addUnsafeEnchantment(Enchantment.INFINITY, 1);
        bow.addUnsafeEnchantment(Enchantment.PUNCH, 5);
        bow.addUnsafeEnchantment(Enchantment.FLAME, 1);
        bow.addUnsafeEnchantment(Enchantment.UNBREAKING, 7);
        bow.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = bow.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§a§lBow of the Ancient Forest");
            meta.setLore(Arrays.asList(
                "§7Carved from the World Tree itself,",
                "§7blessed by nature's eternal spirit.",
                "§a+Nature's Harmony"
            ));
            meta.setCustomModelData(100040);
            meta.getPersistentDataContainer().set(SpecialItems.SYLVAN_BOW_KEY, PersistentDataType.BOOLEAN, true);
            bow.setItemMeta(meta);
        }
        
        return bow;
    }
    
    private ItemStack createStormElytra() {
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        elytra.addUnsafeEnchantment(Enchantment.UNBREAKING, 20);
        elytra.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = elytra.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e§lWings of the Storm Lord");
            meta.setLore(Arrays.asList(
                "§7Woven from lightning and clouds,",
                "§7grants dominion over the skies.",
                "§e+Storm Mastery"
            ));
            meta.getPersistentDataContainer().set(SpecialItems.STORM_ELYTRA_KEY, PersistentDataType.BOOLEAN, true);
            meta.setCustomModelData(100050);
            elytra.setItemMeta(meta);
        }
        
        return elytra;
    }
    
    private ItemStack createForgeMace() {
        ItemStack mace = new ItemStack(Material.MACE);
        mace.addUnsafeEnchantment(Enchantment.DENSITY, 10);
        mace.addUnsafeEnchantment(Enchantment.BREACH, 5);
        mace.addUnsafeEnchantment(Enchantment.WIND_BURST, 4);
        mace.addUnsafeEnchantment(Enchantment.SHARPNESS, 8);
        mace.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        mace.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
        mace.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = mace.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lMace of Divine Forging");
            meta.setLore(Arrays.asList(
                "§7Forged in the heart of creation itself,",
                "§7this mace shapes reality with each strike.",
                "§6+Wind Burst (Fall damage)", "§6+Forge Mastery (Right-click)", "§6+Enhanced Combat"
            ));
            meta.setCustomModelData(100060);
            mace.setItemMeta(meta);
        }
        
        return mace;
    }
    
    private ItemStack createVoidBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        blade.addUnsafeEnchantment(Enchantment.SHARPNESS, 8);
        blade.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
        blade.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = blade.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§0§lVoid Walker's Blade");
            meta.setLore(Arrays.asList(
                "§7Carved from the essence of nothingness,",
                "§7this blade cuts through reality itself.",
                "§8+Phase Strike", "§8+Void Rip", "§8+Dimensional Cut"
            ));
            meta.setCustomModelData(100061);
            blade.setItemMeta(meta);
        }
        
        return blade;
    }
    
    private ItemStack createChronosStaff() {
        ItemStack staff = new ItemStack(Material.STICK);
        staff.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
        staff.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = staff.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§d§lChronos Staff");
            meta.setLore(Arrays.asList(
                "§7Woven from the threads of time itself,",
                "§7this staff commands the flow of moments.",
                "§d+Time Dilation", "§d+Temporal Rewind", "§d+Age Acceleration"
            ));
            meta.setCustomModelData(100062);
            staff.setItemMeta(meta);
        }
        
        return staff;
    }
    
    private ItemStack createCrimsonBlade() {
        ItemStack blade = new ItemStack(Material.NETHERITE_SWORD);
        blade.addUnsafeEnchantment(Enchantment.SHARPNESS, 7);
        blade.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
        blade.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
        blade.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = blade.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§4§lCrimson Blade");
            meta.setLore(Arrays.asList(
                "§7Forged in the blood of fallen warriors,",
                "§7this blade thirsts for eternal combat.",
                "§4+Blood Frenzy", "§4+Life Steal", "§4+Berserker Mode"
            ));
            meta.setCustomModelData(100063);
            blade.setItemMeta(meta);
        }
        
        return blade;
    }
    
    private ItemStack createResonanceCrystal() {
        ItemStack crystal = new ItemStack(Material.AMETHYST_SHARD);
        crystal.addUnsafeEnchantment(Enchantment.UNBREAKING, 10);
        crystal.addUnsafeEnchantment(Enchantment.MENDING, 1);
        
        ItemMeta meta = crystal.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§b§lResonance Crystal");
            meta.setLore(Arrays.asList(
                "§7Crystallized from pure harmonic energy,",
                "§7this crystal resonates with all creation.",
                "§b+Ore Sense (Right-click)", "§b+Crystal Shield (Sneak)", "§b+Sonic Boom (Attack)"
            ));
            meta.setCustomModelData(100064);
            crystal.setItemMeta(meta);
        }
        
        return crystal;
    }
    
}