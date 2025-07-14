# Testament API Documentation

## Overview

The Fallen God Testament plugin provides a comprehensive API for external plugins to interact with the testament system. This allows for seamless integration and extension of the divine power mechanics.

## Getting Started

### Adding the Dependency

Add the Testament plugin as a dependency in your `plugin.yml`:

```yaml
depend: [FallenGodTestament]
```

### Accessing the API

```java
import com.fallengod.testament.api.TestamentAPI;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.enums.AscensionLevel;

// Check if a player has completed a testament
boolean hasCompleted = TestamentAPI.hasCompletedTestament(player.getUniqueId(), GodType.FALLEN);

// Get player's ascension level
AscensionLevel level = TestamentAPI.getAscensionLevel(player);
```

## API Methods

### Player Data Access

#### `getPlayerData(UUID playerId)`
Returns complete player progress data including fragments, completions, and statistics.

```java
PlayerData data = TestamentAPI.getPlayerData(player.getUniqueId());
int totalFragments = data.getTotalFragments();
Set<GodType> completed = data.getCompletedTestaments();
```

#### `hasCompletedTestament(UUID playerId, GodType god)`
Check if a player has completed a specific testament.

```java
if (TestamentAPI.hasCompletedTestament(playerId, GodType.FALLEN)) {
    // Player has mastered the Fallen God
    player.sendMessage("§5You wield the power of death itself!");
}
```

#### `getCompletedTestaments(UUID playerId)`
Get all completed testaments for a player.

```java
Set<GodType> completed = TestamentAPI.getCompletedTestaments(playerId);
player.sendMessage("§6You have mastered " + completed.size() + " divine testaments!");
```

### Ascension System

#### `getAscensionLevel(Player player)`
Get a player's current divine ascension level.

```java
AscensionLevel level = TestamentAPI.getAscensionLevel(player);
switch (level) {
    case MORTAL -> player.sendMessage("§7You are but mortal...");
    case GODLIKE -> player.sendMessage("§d§lYou have transcended mortality!");
}
```

### Fragment Management

#### `giveFragment(Player player, GodType god, int fragmentNumber)`
Give a specific fragment to a player.

```java
// Reward player with a rare fragment
TestamentAPI.giveFragment(player, GodType.VEIL, 7);
player.sendMessage("§8§lYou have found the final Veil fragment!");
```

### Testament Completion

#### `completeTestament(Player player, GodType god)`
Force complete a testament for a player (admin function).

```java
// Complete testament as quest reward
TestamentAPI.completeTestament(player, GodType.SYLVAN);
```

### God Conflicts

#### `areGodsInConflict(GodType god1, GodType god2)`
Check if two gods are opposing forces.

```java
if (TestamentAPI.areGodsInConflict(GodType.FALLEN, GodType.VEIL)) {
    player.sendMessage("§c⚠ These gods are eternal enemies!");
}
```

## Events

### TestamentCompletedEvent

Fired when a player completes a testament.

```java
@EventHandler
public void onTestamentCompleted(TestamentCompletedEvent event) {
    Player player = event.getPlayer();
    GodType god = event.getGod();
    int totalTestaments = event.getTotalTestaments();
    
    // Custom rewards or announcements
    if (totalTestaments >= 6) {
        Bukkit.broadcastMessage("§6" + player.getName() + " has achieved divine status!");
    }
    
    // Cancel the event to prevent default rewards
    // event.setCancelled(true);
}
```

### FragmentFoundEvent

Fired when a player finds a fragment.

```java
@EventHandler
public void onFragmentFound(FragmentFoundEvent event) {
    Player player = event.getPlayer();
    GodType god = event.getGod();
    int fragmentNumber = event.getFragmentNumber();
    FragmentSource source = event.getSource();
    
    // Special handling for rare fragments
    if (fragmentNumber == 7) {
        Bukkit.broadcastMessage("§6" + player.getName() + 
            " found the legendary " + god.getDisplayName() + " Fragment 7!");
    }
    
    // Custom fragment sources
    if (source == FragmentSource.OTHER) {
        player.sendMessage("§7This fragment feels different...");
    }
}
```

## Integration Examples

### Custom Quest Integration

```java
public class TestamentQuest {
    
    public void checkQuestCompletion(Player player) {
        Set<GodType> completed = TestamentAPI.getCompletedTestaments(player.getUniqueId());
        
        if (completed.size() >= 3) {
            // Unlock special quest
            giveQuestReward(player);
        }
    }
    
    public void giveQuestFragment(Player player, String questId) {
        // Give fragment as quest reward
        TestamentAPI.giveFragment(player, GodType.TIME, 5);
        player.sendMessage("§d§lQuest completed! Time fragment acquired!");
    }
}
```

### Economy Integration

```java
public class TestamentEconomy {
    
    public void sellFragment(Player player, GodType god, int fragmentNumber) {
        PlayerData data = TestamentAPI.getPlayerData(player.getUniqueId());
        
        if (data.hasFragment(god, fragmentNumber)) {
            // Calculate price based on rarity
            double price = calculateFragmentPrice(fragmentNumber);
            economy.depositPlayer(player, price);
            
            // Remove fragment (would need additional API method)
            player.sendMessage("§6Sold fragment for $" + price);
        }
    }
}
```

### Rank Integration

```java
public class TestamentRanks {
    
    public void updatePlayerRank(Player player) {
        AscensionLevel level = TestamentAPI.getAscensionLevel(player);
        
        switch (level) {
            case BLESSED -> setRank(player, "Blessed");
            case CHOSEN -> setRank(player, "Chosen");
            case DIVINE -> setRank(player, "Divine");
            case GODLIKE -> setRank(player, "Godlike");
        }
    }
}
```

## Best Practices

### Performance
- Cache API calls when possible
- Use events instead of polling for changes
- Avoid frequent player data access in loops

### Compatibility
- Always check if the Testament plugin is enabled
- Handle API exceptions gracefully
- Test with different ascension levels

### User Experience
- Provide clear feedback for testament-related actions
- Respect the plugin's theming and messaging style
- Consider cross-platform compatibility (Bedrock/Java)

## Advanced Usage

### Custom God Powers

```java
public class CustomGodPowers {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Check if player has Tempest God testament
        if (TestamentAPI.hasCompletedTestament(player.getUniqueId(), GodType.TEMPEST)) {
            // Custom storm powers
            if (player.isSneaking() && player.getLocation().getBlock().isLiquid()) {
                // Walk on water ability
                event.getTo().getBlock().setType(Material.FROSTED_ICE);
            }
        }
    }
}
```

### Testament Progression Tracking

```java
public class ProgressionTracker {
    
    public void trackProgress(Player player) {
        PlayerData data = TestamentAPI.getPlayerData(player.getUniqueId());
        
        // Log progression milestones
        int completion = data.getCompletionPercentage();
        if (completion >= 50 && !hasReachedMilestone(player, 50)) {
            Bukkit.broadcastMessage("§6" + player.getName() + " is halfway to divine ascension!");
            setMilestone(player, 50);
        }
    }
}
```

## Support

For additional API features or support:
- Check the plugin's GitHub repository
- Join the community Discord
- Submit feature requests for new API methods

The Testament API is designed to be extensible and will continue to grow based on community needs and feedback.