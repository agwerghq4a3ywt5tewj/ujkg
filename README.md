# Fallen God Testament - Complete Minecraft Plugin

## 🎮 Overview
Epic Minecraft plugin where players collect 7 rare fragments from each of 12 fallen gods to unlock ultimate rewards and divine powers. The ultimate goal is achieving **Divine Convergence** - mastering all gods to become the supreme deity. Built for Paper 1.21.5+ with Java 21.

## ⚡ Quick Start

### 1. Build the Plugin
```bash
mvn clean package
```

### 2. Install Plugin
```bash
# Copy the compiled JAR to your server
cp target/testament-1.7.0.jar /path/to/server/plugins/
```

### 3. Install Datapack
```bash
# Copy datapack to each world
cp -r datapack/ /path/to/world/datapacks/fallengod_testament/
```

### 4. Start Server
```bash
# Restart server or reload
/reload
```

## 🏛️ The Twelve Gods & Their Rewards

### 🌟 **THE ULTIMATE GOAL: DIVINE CONVERGENCE**

When a player completes **ALL 12 TESTAMENTS**, they unlock the legendary **Convergence Nexus** - a magnificent shrine that automatically spawns and grants the ultimate rewards:

#### **Convergence Nexus Rewards:**
- **👑 Crown of Divine Convergence**: +20 Hearts (30 total), Complete damage immunity, All testament powers combined
- **⚡ Scepter of Omnipotence**: 1000 damage attacks, Reality manipulation abilities, All weapon powers
- **📖 Divine Codex of All Knowledge**: Complete lore of all gods, Divine power combinations, Reality manipulation techniques
- **🎯 Master of All Divinity Title**: The ultimate achievement, Creative flight, Infinite resources

**Special Features:**
- The Convergence Nexus spawns automatically when a player completes their 12th testament
- Server-wide announcements celebrate this legendary achievement
- **Activation**: Right-click the **Netherite Block** in the center of the nexus to achieve convergence
- Only one player can achieve convergence per server session
- Epic particle effects and sounds mark this historic moment
- The shrine removes itself after activation, leaving only a memorial beacon

### **Core Six Gods** (Fully Implemented)

#### 1. **Fallen God** - Ultimate Defense
- **Theme**: Death, undeath, ultimate protection
- **Reward**: **Heart of the Fallen God** + Netherite armor set with Silence trim
- **Power**: 25 total hearts + Strength, Regeneration, Resistance
- **Special**: **Heart Return System** - If the Heart is consumed as a totem, it automatically returns after 45 seconds
- **Biomes**: Swamps, Dark Forests, Deep Dark
- **Altar Center**: Crying Obsidian

#### 2. **Banishment God** - Ultimate Offense  
- **Theme**: Fire, exile, destruction
- **Reward**: Ultimate weapon and tool set (Sword, Axe, Pickaxe)
- **Power**: Overpowered enchanted weapons with fire aspects
- **Biomes**: Deserts, Badlands, Savannas
- **Altar Center**: Magma Block

#### 3. **Abyssal God** - Master of Depths
- **Theme**: Ocean depths, water mastery
- **Reward**: **Trident of the Endless Deep** + water abilities
- **Power**: Permanent Water Breathing, Dolphins Grace
- **Biomes**: All ocean types (including underwater)
- **Altar Center**: Dark Prismarine

#### 4. **Sylvan God** - Nature's Guardian
- **Theme**: Forests, nature, growth
- **Reward**: **Bow of the Ancient Forest** + nature powers
- **Power**: Permanent Regeneration II, Saturation
- **Biomes**: Forests, Jungles, Taigas
- **Altar Center**: Oak Log

#### 5. **Tempest God** - Storm Lord
- **Theme**: Sky, storms, lightning, flight
- **Reward**: **Wings of the Storm Lord** + storm powers
- **Power**: Enhanced elytra, Speed II, Jump Boost III
- **Biomes**: Mountains, Hills, Peaks
- **Altar Center**: Lightning Rod

#### 6. **Veil God** - Master of Dimensions
- **Theme**: Reality manipulation, void magic
- **Reward**: **Veil of Nullification** + dimension powers
- **Power**: Counters Heart of Fallen God, Night Vision, Speed
- **Biomes**: End dimension only
- **Altar Center**: End Portal Frame (with Eye)

### **Expansion Six Gods** (Ready for Activation)

#### 7. **Forge God** - Master of Creation
- **Theme**: Smithing, crafting, creation, molten metal
- **Reward**: **Mace of Divine Forging** + crafting mastery
- **Power**: Enhanced mace with Wind Burst, Forge Mastery ability
- **Biomes**: Mountains, Caves, Lava areas
- **Altar Center**: Anvil
- **Special**: Right-click to repair all items (30s cooldown), conflicts with Heart
- **Conflict**: Cannot coexist with Heart of Fallen God

#### 8. **Void God** - Harbinger of Nothingness
- **Theme**: Emptiness, teleportation, phase shifting
- **Reward**: **Void Walker's Blade** + teleportation abilities
- **Power**: Void Rip teleportation, Phase Strike ignores armor
- **Biomes**: End dimension, Deep caves
- **Altar Center**: Obsidian
- **Special**: Right-click to teleport 10 blocks forward (5s cooldown)

#### 9. **Time God** - Chronos Incarnate
- **Theme**: Time manipulation, aging, temporal magic
- **Reward**: **Chronos Staff** + time abilities
- **Power**: Time Dilation slows enemies, temporal mastery
- **Biomes**: Deep Dark, Dripstone Caves, Lush Caves
- **Altar Center**: Amethyst Cluster
- **Special**: Right-click for Time Dilation (25s cooldown)

#### 10. **Blood God** - Warrior's Patron
- **Theme**: Combat, sacrifice, berserker rage
- **Reward**: **Crimson Blade** + battle frenzy
- **Power**: Blood Frenzy (stronger when injured), Life Steal
- **Biomes**: Nether, dangerous areas
- **Altar Center**: Redstone Block
- **Special**: Damage increases as health decreases

#### 11. **Crystal God** - Resonance Master
- **Theme**: Crystals, sound, vibration, harmony
- **Reward**: **Resonance Crystal** + sonic abilities
- **Power**: Ore Sense, Crystal Shield, Sonic Boom
- **Biomes**: Geode caves, crystal formations
- **Altar Center**: Large Amethyst Cluster
- **Special**: Right-click for Ore Sense, Sneak for Crystal Shield

#### 12. **Shadow God** - Master of Darkness
- **Theme**: Stealth, darkness, assassination
- **Reward**: **Shadow Mantle** + stealth abilities
- **Power**: Umbral Form (invisible in darkness), Shadow Strike
- **Biomes**: Dark areas, caves, sculk biomes
- **Altar Center**: Sculk Catalyst
- **Special**: Stand still in darkness for invisibility

## ⚔️ Advanced Weapon Abilities

### **Active Abilities** (Right-click or Special Actions)
- **Forge Mastery**: Repair all items in inventory (Forge Mace)
- **Void Rip**: Teleport 10 blocks forward (Void Blade)
- **Time Dilation**: Slow all nearby enemies for 20 seconds (Chronos Staff)
- **Ore Sense**: Reveal ores within 20 blocks (Resonance Crystal)
- **Crystal Shield**: Resistance II + Absorption I (Resonance Crystal)
- **Sonic Boom**: Shatter blocks in cone shape (Resonance Crystal)

### **Passive Abilities**
- **Wind Burst**: Enhanced fall damage creates shockwave (Forge Mace)
- **Phase Strike**: Attacks ignore 50% armor (Void Blade)
- **Blood Frenzy**: Damage scales with missing health (Crimson Blade)
- **Life Steal**: Heal 2 hearts on successful attacks (Crimson Blade)
- **Umbral Form**: Invisibility when motionless in darkness (Shadow Mantle)
- **Shadow Strike**: 3x damage when attacking from behind (Shadow Mantle)

### **Special Mechanics**
- **Heart Return**: Heart of Fallen God returns 45 seconds after totem pop
- **Divine Conflicts**: Heart and Forge Mace cannot coexist
- **Armor Trims**: Fallen armor features rotating Silence trim colors

## 🌟 Divine Ascension System

### **Ascension Levels**
- **Mortal** (0 testaments): Base gameplay
- **Blessed** (1-2 testaments): Luck I, minor divine favor
- **Chosen** (3-4 testaments): Luck II, Hero of Village, significant abilities
- **Divine** (5-6 testaments): Luck III, enhanced effects, reality manipulation
- **Godlike** (7+ testaments): Maximum effects, ultimate cosmic powers
- **CONVERGENCE** (12 testaments): **Master of All Divinity** - Transcends all limitations

### **Testament Conflicts**
Opposing god pairs create strategic choices:
- **Fallen vs Veil**: Death vs Reality (Heart/Veil nullification)
- **Banishment vs Abyssal**: Fire vs Water
- **Sylvan vs Tempest**: Nature vs Storm
- **Forge vs Void**: Creation vs Destruction
- **Time vs Shadow**: Light vs Dark
- **Blood vs Crystal**: Chaos vs Order

## 🎯 Fragment Collection System

### **Rarity & Distribution**
- **Fragment 1**: 30% chance (most common)
- **Fragment 2**: 20% chance
- **Fragment 3**: 15% chance
- **Fragment 4**: 13% chance
- **Fragment 5**: 10% chance
- **Fragment 6**: 7% chance
- **Fragment 7**: 5% chance (rarest)

### **Spawn Mechanics**
- **Chest Spawning**: 2% chance after opening 50+ chests
- **Mob Drops**: 0.1% from dangerous mobs (Wither Skeleton, Blaze, Elder Guardian, etc.)
- **Cooldowns**: 2 hours between chest fragments, 1 hour between mob drops
- **Biome Influence**: Higher chances in god-appropriate biomes

## 🏗️ Altar System

### **Natural Generation**
- **Spacing**: 32 chunks between altars (512 blocks)
- **Separation**: 8 chunks minimum between different types
- **Biome Specific**: Each god spawns only in thematic biomes
- **Structure**: 7x7 base with unique center blocks and decorations
- **Effects**: God-specific particles and sounds on completion

### **Manual Altar Building**
All altars follow this pattern:
```
Base Layer (7x7): God-specific material
Center Block: Unique interaction block per god
Walls: Themed walls around perimeter
Decorations: Atmospheric lighting and details
Beacon: Top beacon for visibility
```

### **Altar Protection**
- Grief protection system prevents unauthorized breaking
- Auto-regeneration option (configurable)
- Admin commands for manual creation and management

## 📋 Commands

### **Player Commands**
- `/testament status` - View fragment progress and ascension level
- `/testament reunite <god>` - Check if ready to complete testament
- `/godlex` - Get the divine knowledge compendium book
- `/godlex text` - Show progress in chat
- `/godlex god <name>` - Show specific god information

### **Admin Commands**
- `/fragment spawn <god> <number> <here>` - Spawn specific fragments
- `/fragment heart` - Give Heart of Fallen God
- `/fragment veil` - Give Veil of Nullification
- `/fragment giveall <god>` - Give all 7 fragments for testing
- `/fragment testfragments <god>` - Give fragments + show altar info
- `/fragment testmode <on|off>` - Toggle Heart/Veil testing
- `/fragment title <player> <title|reset>` - Manage player titles
- `/datapack scan [radius]` - Scan for nearby altars
- `/datapack locate <god>` - Find specific altar type
- `/altar create <god>` - Create altar at current location
- `/altar regenerate <god>` - Regenerate altar structure
- `/altar list` - List nearby altars
- `/altar protect <on|off>` - Toggle altar protection

## 🎮 Player Title System

### **Title Types**
- **None**: Default state
- **Fallen**: Toxic behavior + excessive deaths (permanent consequences)
- **Toxic**: Detected toxic chat/behavior
- **Cursed**: Divine punishment
- **Blessed**: Divine favor from gods
- **Champion**: Skilled warrior status
- **Legend**: Legendary achievements

### **Toxicity Detection**
- **Chat Spam**: Messages sent within 1 second (5+ triggers toxicity)
- **Toxic Language**: Automatic detection of harmful words
- **Excessive Caps**: Messages with 70%+ capital letters
- **Death Tracking**: Excessive deaths contribute to Fallen status
- **Automatic Penalties**: Progressive system leading to "Fallen" status

### **Fallen Status Triggers**
- 10+ toxicity points, OR
- 50+ deaths, OR
- 5+ toxicity points AND 25+ deaths

## ⚔️ Strategic PvP Balance

### **Heart vs Veil System**
- **Heart of Fallen God**: Ultimate power (25 hearts + effects)
- **Heart Return**: Automatically returns 45 seconds after being consumed as a totem
- **Veil of Nullification**: Counters Heart within 16 blocks
- **Strategic Positioning**: Risk/reward gameplay
- **Test Mode**: `/fragment testmode on` for testing interactions

### **Divine Conflicts**
- **Heart vs Forge**: Cannot coexist in same inventory
- **Opposing Gods**: Penalties when mastering conflicting testaments
- **Strategic Choices**: Players must choose their divine path carefully

## 🔧 Configuration

Customize all settings in `config.yml`:
- Fragment spawn rates and cooldowns
- Heart of Fallen God effects and power levels
- Veil nullification range and effects
- God-specific biomes and altar blocks
- Ascension level requirements and effects
- Testament conflict penalties
- Cross-platform compatibility settings
- Weapon ability cooldowns and power levels
- Altar generation and protection settings

## 🏗️ Technical Implementation

### **Requirements**
- **Server**: Paper 1.21.5+
- **Java**: 21+
- **Maven**: 3.6+

### **Features**
- Async fragment spawning operations
- Persistent player data storage with YAML
- Real-time Heart/Veil interactions
- Natural altar generation via datapack
- Comprehensive admin tools and testing commands
- Performance optimizations with caching
- Weapon ability system with cooldowns
- Divine ascension progression tracking
- Public API for other plugins
- Cross-platform Bedrock/Geyser support
- God-specific particle and sound effects
- Armor trim system with rotating colors
- Conflict prevention and resolution

### **File Structure**
```
src/main/java/com/fallengod/testament/
├── TestamentPlugin.java          # Main plugin class
├── api/TestamentAPI.java         # Public API for other plugins
├── enums/                        # God types, ascension levels, titles
├── data/PlayerData.java          # Player progress tracking
├── managers/                     # Core system managers
├── items/                        # Fragment and special items
├── commands/                     # Command handlers
├── listeners/                    # Event listeners
├── rewards/RewardManager.java    # Testament rewards
├── weapons/                      # Weapon ability system
├── events/                       # Custom events
└── quests/LegendaryQuest.java    # Future quest system
```

## 🎮 Gameplay Flow

1. **Exploration Phase**: Players explore, open chests, fight dangerous mobs
2. **Collection Phase**: Gather 7 unique fragments for chosen god(s)
3. **Reunification Phase**: Locate appropriate altar and complete testament
4. **Ascension Phase**: Gain divine powers and strategic advantages
5. **Mastery Phase**: Complete multiple testaments for ultimate power
6. **Conflict Resolution**: Navigate divine conflicts and strategic choices
7. **CONVERGENCE PHASE**: Master all 12 gods to achieve ultimate divinity

## 🌟 End Game Content

### **Testament Completion Rewards**
- **Fallen God**: Ultimate survivability + Heart power + Silence armor
- **Banishment God**: Ultimate damage output with weapon set
- **Abyssal God**: Water world domination with trident
- **Sylvan God**: Nature harmony and archery mastery
- **Tempest God**: Sky mobility and storm power
- **Veil God**: Reality manipulation and Heart counter

### **Future Legendary Quests**
- **✅ The Convergence**: Unite all divine powers (12 testaments) - **IMPLEMENTED!**
- **The Sundering**: Choose one god, reject all others
- **The Balance**: Maintain equilibrium between opposing forces
- **The Transcendence**: Surpass the gods themselves

## 🚀 Getting Started

1. **Install** the plugin and datapack
2. **Explore** to find fragments (start with chests)
3. **Collect** 7 fragments from your chosen god
4. **Locate** the appropriate altar using `/datapack locate <god>`
5. **Complete** your first testament by right-clicking the altar center
6. **Ascend** through divine power levels
7. **Master** multiple gods for ultimate abilities
8. **Navigate** divine conflicts strategically
9. **ACHIEVE CONVERGENCE** by completing all 12 testaments for ultimate power!
10. **Activate the Nexus** by right-clicking the Netherite Block when it spawns!

## 🎯 Design Philosophy

1. **Rarity Creates Value**: Low drop rates make fragments precious
2. **Exploration Rewards**: Dangerous areas have better fragment chances
3. **Strategic Depth**: Testament conflicts create tactical decisions
4. **Long-term Engagement**: Multiple testaments provide extended goals
5. **Server Community**: Epic announcements create shared experiences
6. **Balanced Power**: Each god offers unique advantages without being overpowered
7. **Conflict Resolution**: Divine powers have meaningful trade-offs
8. **Immersive Experience**: Rich effects and feedback enhance gameplay
9. **Ultimate Achievement**: Divine Convergence provides the ultimate long-term goal
10. **Epic Moments**: Server-wide celebrations for legendary achievements

## 🔮 Future Additions & Roadmap

### **Planned Major Features**

#### **🏰 Guild Testament System**
- **Guild Convergence**: Multiple players can work together to achieve collective divine power
- **Shared Nexus**: Guild-wide convergence nexus that requires coordination between members
- **Guild Conflicts**: Inter-guild warfare over divine territories and fragment sources
- **Collective Rewards**: Unique items and abilities only available through guild cooperation

#### **🌍 World Events & Divine Interventions**
- **Fallen God Manifestations**: Rare world events where gods physically appear
- **Divine Storms**: Weather events that boost fragment spawn rates in specific biomes
- **Convergence Rifts**: Temporary portals that appear after convergence achievements
- **Testament Wars**: Server-wide events where players compete for divine favor

#### **⚔️ Advanced Combat & PvP**
- **Divine Arenas**: Special battlegrounds where testament powers are enhanced
- **Convergence Duels**: 1v1 combat between convergence achievers with reality-bending abilities
- **Testament Tournaments**: Organized competitions with unique rewards
- **Divine Siege Weapons**: Massive weapons powered by multiple testament energies

#### **🎭 Roleplay & Lore Expansion**
- **God Avatars**: Players can temporarily become avatars of their mastered gods
- **Divine Quests**: Complex multi-stage quests that reveal god backstories
- **Testament Prophecies**: Dynamic storylines that change based on server actions
- **Fallen God Resurrection**: Ultimate endgame content to bring gods back to life

#### **🔧 Technical Enhancements**
- **Web Dashboard**: Online tracking of server-wide testament progress and statistics
- **Mobile Companion App**: Fragment tracking and altar location mapping
- **Advanced Analytics**: Detailed metrics on fragment spawn rates and player progression
- **Cross-Server Integration**: Testament progress that carries across multiple servers

#### **🎨 Customization & Content**
- **Custom God Creator**: Tools for server admins to create their own gods and testaments
- **Testament Modifiers**: Seasonal events that change fragment requirements or rewards
- **Dynamic Altars**: Altars that change appearance based on server events
- **Player-Created Shrines**: Allow players to build their own worship sites

#### **🌟 Post-Convergence Content**
- **Transcendence Levels**: Progression beyond convergence with cosmic powers
- **Reality Shaping**: Convergence achievers can modify world generation
- **Divine Dimensions**: Access to god-specific pocket dimensions
- **Omnipotence Challenges**: Ultimate trials that test mastery of all divine powers

#### **🤝 Community Features**
- **Testament Mentorship**: Experienced players can guide newcomers
- **Fragment Trading Market**: Secure trading system for fragments between players
- **Divine Councils**: Player governance systems for servers with multiple convergence achievers
- **Testament Festivals**: Seasonal celebrations with special rewards and activities

#### **📱 Integration & Compatibility**
- **Discord Bot Integration**: Real-time testament progress and server announcements
- **Twitch Integration**: Stream-friendly features for content creators
- **VR Support**: Virtual reality compatibility for immersive divine experiences
- **Bedrock Parity**: Full feature compatibility with Minecraft Bedrock Edition

### **Community-Requested Features**
- **Testament Pets**: Companion creatures that grow stronger with testament completion
- **Divine Crafting**: Special recipes unlocked through testament mastery
- **Seasonal Gods**: Limited-time gods with unique themes and rewards
- **Testament Leaderboards**: Global rankings and achievement tracking
- **Divine Music**: Dynamic soundtrack that changes based on player's testament progress

### **Long-Term Vision**
The ultimate goal is to create the most comprehensive divine power progression system in Minecraft, where every player's journey feels unique and meaningful. Future updates will focus on:

1. **Expanding the Universe**: More gods, more testaments, more ways to achieve divine power
2. **Community Building**: Features that bring players together in pursuit of divine goals
3. **Endless Progression**: Content that provides meaningful advancement even after convergence
4. **Creative Expression**: Tools for players and admins to create their own divine content
5. **Cross-Platform Unity**: Seamless experience across all Minecraft platforms and versions

### **How to Contribute Ideas**
- Join our Discord community for discussions and suggestions
- Submit feature requests through GitHub issues
- Participate in beta testing for new features
- Share your testament stories and experiences
- Help with translations and documentation

## 📊 Performance & Compatibility

- **Minimal Impact**: Efficient caching and async operations
- **Thread Safe**: All implementations designed for server stability
- **Balanced Rates**: Spawn rates tuned for long-term engagement
- **Scalable**: Supports multiple players and extensive gameplay
- **Cross-Platform**: Bedrock/Geyser compatibility features
- **API Integration**: Public API for other plugins
- **Event System**: Custom events for external integration

## 🔍 Troubleshooting

### **Altars Not Generating**
- Ensure datapack is loaded: `/datapack list`
- Explore NEW chunks (altars don't generate in existing areas)
- Check biome requirements for each god type
- Use `/datapack scan 1000` to find nearby altars

### **Fragments Not Spawning**
- Open 50+ chests before fragments can spawn
- Check cooldown timers (2h chest, 1h mob)
- Fight dangerous mobs in appropriate biomes
- Verify plugin permissions

### **Testament Not Completing**
- Ensure you have all 7 fragments in inventory
- Right-click the correct center block for each god
- Check console for debug messages
- Use `/fragment testfragments <god>` for testing

### **Heart/Forge Conflict**
- Heart of Fallen God cannot coexist with Mace of Divine Forging
- One will be removed if both are obtained
- This is intentional divine conflict design

### **Title System Not Working**
- Titles update automatically based on behavior
- Use `/fragment title <player> <title>` to manually set
- Check toxicity score with debug commands
- Ensure proper permissions for title management

## 🔗 API Integration

The plugin provides a comprehensive API for other plugins:

```java
// Check if player completed a testament
boolean hasCompleted = TestamentAPI.hasCompletedTestament(playerId, GodType.FALLEN);

// Get player's ascension level
AscensionLevel level = TestamentAPI.getAscensionLevel(player);

// Give fragments programmatically
TestamentAPI.giveFragment(player, GodType.VEIL, 7);

// Listen for testament completion
@EventHandler
public void onTestamentCompleted(TestamentCompletedEvent event) {
    Player player = event.getPlayer();
    GodType god = event.getGod();
    // Custom logic here
}

// Listen for divine convergence
@EventHandler
public void onConvergence(ConvergenceEvent event) {
    Player player = event.getPlayer();
    Location nexusLocation = event.getNexusLocation();
    // Ultimate achievement logic here
}
```

See `TESTAMENT_API.md` for complete API documentation.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Transform your Minecraft server into an epic realm where players forge their own divine destinies through strategic choices, divine conflicts, and ultimate power!**