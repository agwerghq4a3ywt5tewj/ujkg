package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.PlayerTitle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ToxicityListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Long> lastChatTime;
    private final Map<UUID, Integer> spamCount;
    private final Map<UUID, Integer> capsViolations;
    private final Map<UUID, Integer> toxicWordCount;
    private final List<String> toxicWords;
    
    public ToxicityListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.lastChatTime = new HashMap<>();
        this.spamCount = new HashMap<>();
        this.capsViolations = new HashMap<>();
        this.toxicWordCount = new HashMap<>();
        
        // Common toxic words/phrases to detect
        this.toxicWords = Arrays.asList(
            "noob", "trash", "garbage", "ez", "easy", "bad", "suck", "terrible",
            "idiot", "stupid", "dumb", "loser", "scrub", "git gud", "skill issue",
            "kys", "kill yourself", "uninstall", "delete game", "quit", "cancer",
            "retard", "autistic", "gay", "fag", "niger", "bitch", "fuck", "shit"
        );
        
        // Start cleanup task to reset counters periodically
        startCleanupTask();
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getTitleManager().addDeath(player.getUniqueId());
        
        // Debug message
        plugin.getLogger().info("Death recorded for " + player.getName() + 
            ". Total deaths: " + plugin.getTitleManager().getDeathCount(player.getUniqueId()));
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        String originalMessage = event.getMessage();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Debug message
        plugin.getLogger().info("Chat from " + player.getName() + ": " + originalMessage);
        
        // Check for spam
        Long lastChat = lastChatTime.get(playerId);
        if (lastChat != null && (currentTime - lastChat) < 1000) { // Less than 1 second
            int spam = spamCount.getOrDefault(playerId, 0) + 1;
            spamCount.put(playerId, spam);
            
            if (spam >= 3) { // Reduced threshold for faster detection
                plugin.getTitleManager().addToxicityPoint(playerId, "Spamming chat");
                spamCount.put(playerId, 0); // Reset spam count
                plugin.getLogger().info("Spam detected for " + player.getName());
            }
        } else {
            spamCount.put(playerId, 0); // Reset spam count if not spamming
        }
        
        lastChatTime.put(playerId, currentTime);
        
        // Check for toxic language
        for (String toxicWord : toxicWords) {
            if (message.contains(toxicWord)) {
                int toxicCount = toxicWordCount.getOrDefault(playerId, 0) + 1;
                toxicWordCount.put(playerId, toxicCount);
                plugin.getTitleManager().addToxicityPoint(playerId, "Toxic language: " + toxicWord);
                plugin.getLogger().info("Toxic word '" + toxicWord + "' detected from " + player.getName() + 
                    ". Count: " + toxicCount);
                break; // Only count one toxic word per message
            }
        }
        
        // Check for excessive caps
        if (originalMessage.length() > 5) { // Reduced minimum length
            long capsCount = originalMessage.chars().filter(Character::isUpperCase).count();
            double capsRatio = (double) capsCount / originalMessage.length();
            
            if (capsRatio > 0.6) { // Reduced threshold to 60% caps
                int violations = capsViolations.getOrDefault(playerId, 0) + 1;
                capsViolations.put(playerId, violations);
                
                plugin.getTitleManager().addToxicityPoint(playerId, "Excessive caps");
                plugin.getLogger().info("Excessive caps from " + player.getName() + 
                    ". Ratio: " + String.format("%.2f", capsRatio) + ". Violations: " + violations);
            }
        }
        
        // Immediate title check for testing
        checkForImmediateTitleUpdate(player);
    }
    
    private void checkForImmediateTitleUpdate(Player player) {
        UUID playerId = player.getUniqueId();
        int toxicity = plugin.getTitleManager().getToxicityScore(playerId);
        int deaths = plugin.getTitleManager().getDeathCount(playerId);
        
        // More aggressive title assignment for testing
        if (toxicity >= 3) { // Reduced from 10
            plugin.getTitleManager().setPlayerTitle(playerId, PlayerTitle.TOXIC);
            player.sendMessage("§c⚠ You have been marked as TOXIC due to your behavior!");
        }
        
        if (toxicity >= 5 || deaths >= 20 || (toxicity >= 2 && deaths >= 10)) { // Reduced thresholds
            plugin.getTitleManager().setPlayerTitle(playerId, PlayerTitle.FALLEN);
            player.sendMessage("§4⚠ You have been marked as FALLEN! Your soul is corrupted!");
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Check if player rage quit (quit within 30 seconds of dying)
        // This would require tracking death times, but for now we'll just clean up
        lastChatTime.remove(playerId);
        spamCount.remove(playerId);
        capsViolations.remove(playerId);
        toxicWordCount.remove(playerId);
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        String reason = event.getReason().toLowerCase();
        
        // If kicked for toxic behavior, add toxicity points
        if (reason.contains("toxic") || reason.contains("spam") || reason.contains("grief")) {
            plugin.getTitleManager().addToxicityPoint(player.getUniqueId(), "Kicked for: " + reason);
            plugin.getLogger().info("Kick-based toxicity point for " + player.getName() + ": " + reason);
        }
    }
    
    private void startCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Reset some counters every 5 minutes to prevent permanent penalties
                long fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000);
                
                lastChatTime.entrySet().removeIf(entry -> entry.getValue() < fiveMinutesAgo);
                
                // Reduce violation counts over time
                capsViolations.replaceAll((k, v) -> Math.max(0, v - 1));
                toxicWordCount.replaceAll((k, v) -> Math.max(0, v - 1));
                
                // Clean up empty entries
                capsViolations.entrySet().removeIf(entry -> entry.getValue() <= 0);
                toxicWordCount.entrySet().removeIf(entry -> entry.getValue() <= 0);
            }
        }.runTaskTimer(plugin, 6000L, 6000L); // Every 5 minutes
    }
}