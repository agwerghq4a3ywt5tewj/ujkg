package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.weapons.WeaponAbility;
import com.fallengod.testament.weapons.abilities.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeaponAbilityManager {
    private final TestamentPlugin plugin;
    private final Map<String, WeaponAbility> abilities;
    private final Map<UUID, Map<String, Long>> cooldowns;
    
    public WeaponAbilityManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.abilities = new HashMap<>();
        this.cooldowns = new HashMap<>();
        
        registerAbilities();
    }
    
    private void registerAbilities() {
        // Forge God abilities
        abilities.put("molten_strike", new MoltenStrikeAbility(plugin));
        
        // Void God abilities
        abilities.put("phase_strike", new PhaseStrikeAbility());
        abilities.put("void_rip", new VoidRipAbility());
        
        // Time God abilities
        abilities.put("time_dilation", new TimeDilationAbility());
        
        // Blood God abilities
        abilities.put("blood_frenzy", new BloodFrenzyAbility());
        
        // Crystal God abilities
        abilities.put("sonic_boom", new SonicBoomAbility());
        
        // Shadow God abilities
        abilities.put("umbral_form", new UmbralFormAbility());
    }
    
    public boolean activateAbility(Player player, String abilityName, ItemStack weapon) {
        WeaponAbility ability = abilities.get(abilityName);
        if (ability == null) {
            return false;
        }
        
        // Check cooldown
        if (isOnCooldown(player, abilityName)) {
            long remainingTime = getCooldownRemaining(player, abilityName);
            player.sendMessage("§c" + ability.getName() + " is on cooldown for " + remainingTime + " seconds.");
            return false;
        }
        
        // Activate ability
        ability.activate(player, weapon);
        
        // Set cooldown
        setCooldown(player, abilityName, ability.getCooldownSeconds());
        
        return true;
    }
    
    public boolean isOnCooldown(Player player, String abilityName) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return false;
        }
        
        Long cooldownEnd = playerCooldowns.get(abilityName);
        return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
    }
    
    private long getCooldownRemaining(Player player, String abilityName) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) {
            return 0;
        }
        
        Long cooldownEnd = playerCooldowns.get(abilityName);
        if (cooldownEnd == null) {
            return 0;
        }
        
        return Math.max(0, (cooldownEnd - System.currentTimeMillis()) / 1000);
    }
    
    public void setCooldown(Player player, String abilityName, int seconds) {
        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                 .put(abilityName, System.currentTimeMillis() + (seconds * 1000L));
    }
    
    public void clearCooldowns(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
}