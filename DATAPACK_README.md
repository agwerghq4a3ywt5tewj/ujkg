# Fallen God Testament - Datapack

## 🏛️ Complete Altar Generation System

This datapack provides natural generation of all six Fallen God altars throughout your Minecraft world. Each altar is uniquely themed and spawns in appropriate biomes.

## 🎯 The Six Altars

### 1. **Fallen Altar** - Ultimate Defense
- **Design**: Blackstone platform with crying obsidian center
- **Features**: Soul fire, soul lanterns, blackstone walls
- **Biomes**: Swamps, Dark Forests, Deep Dark
- **Theme**: Death and undeath

### 2. **Banishment Altar** - Ultimate Offense
- **Design**: Nether brick platform with magma block center
- **Features**: Fire, lanterns, nether brick walls
- **Biomes**: Deserts, Badlands, Savannas
- **Theme**: Fire and exile

### 3. **Abyssal Altar** - Master of Depths
- **Design**: Prismarine platform with dark prismarine center
- **Features**: Sea lanterns, prismarine walls
- **Biomes**: All ocean types
- **Theme**: Ocean depths

### 4. **Sylvan Altar** - Nature's Guardian
- **Design**: Moss block platform with oak log center
- **Features**: Glowstone, oak leaves, mossy walls
- **Biomes**: Forests, Jungles, Taigas
- **Theme**: Nature and growth

### 5. **Tempest Altar** - Storm Lord
- **Design**: Quartz platform with lightning rod center
- **Features**: End rods, white concrete, quartz walls
- **Biomes**: Mountains, Hills, Peaks
- **Theme**: Sky and storms

### 6. **Veil Altar** - Master of Dimensions
- **Design**: End stone platform with end portal frame center
- **Features**: End rods, end stone brick walls
- **Biomes**: End dimension
- **Theme**: Reality manipulation

## 📦 Installation

### 1. Copy Datapack
```bash
# Copy to each world you want altars in
cp -r datapack/ /path/to/world/datapacks/fallengod_testament/
```

### 2. Reload World
```bash
# In-game command
/reload
```

### 3. Generate New Chunks
Altars will only generate in newly explored chunks. Existing chunks will not have altars.

## ⚙️ Generation Settings

### Spacing Configuration
- **Spacing**: 32 chunks between altars
- **Separation**: 8 chunks minimum between different altar types
- **Salt**: Unique values prevent clustering

### Biome Distribution
Each altar type spawns only in thematically appropriate biomes:

- **Fallen**: Dark, spooky biomes
- **Banishment**: Hot, harsh biomes  
- **Abyssal**: Ocean biomes (including ocean floor)
- **Sylvan**: Forest and nature biomes
- **Tempest**: Mountain and high-altitude biomes
- **Veil**: End dimension only

## 🔍 Finding Altars

### In-Game Commands
```bash
# Locate specific altar type
/locate structure fallengod:fallen_altar
/locate structure fallengod:banishment_altar
/locate structure fallengod:abyssal_altar
/locate structure fallengod:sylvan_altar
/locate structure fallengod:tempest_altar
/locate structure fallengod:veil_altar
```

### Plugin Commands
If using the Fallen God Testament plugin:
```bash
/datapack locate fallen
/datapack scan 1000
```

## 🗺️ Treasure Maps
Altars are included in:
- Eye of Ender location system
- Treasure map generation
- Structure location commands

## 🏗️ Structure Details

### Altar Components
Each altar features:
- **7x7 base platform** in themed materials
- **Center interaction block** (crying obsidian, magma block, etc.)
- **Decorative walls** around the perimeter
- **Themed lighting** (soul fire, lanterns, etc.)
- **Beacon** at the top for visibility
- **Atmospheric details** (leaves, end rods, etc.)

### Interaction System
- Right-click the center block with all 7 fragments
- Plugin detects the altar type automatically
- Fragments are consumed and rewards granted
- Epic effects and server announcements

## 🎮 Gameplay Integration

### Exploration Incentive
- Altars are rare and spread out
- Encourages long-distance exploration
- Different biomes offer different god types
- Creates natural progression paths

### Strategic Locations
- Fallen altars in dangerous areas (Deep Dark)
- Abyssal altars underwater (diving required)
- Tempest altars on mountain peaks (climbing required)
- Veil altars in the End (late-game access)

## 🔧 Customization

### Modifying Generation
Edit structure set files to change:
- Spacing between altars
- Biome restrictions
- Generation frequency

### Adding Custom Altars
1. Create new .nbt structure file
2. Add structure definition JSON
3. Create template pool
4. Add to structure set
5. Define biome tags

## 📊 Performance

### Optimizations
- Efficient structure generation
- Minimal world impact
- Balanced spacing prevents lag
- Compatible with other datapacks

### Resource Usage
- Small file size (~50KB total)
- Fast loading and generation
- No ongoing performance impact
- Works with existing worlds

## 🐛 Troubleshooting

### Common Issues
1. **Altars not generating**: Ensure datapack is in correct folder and `/reload` was run
2. **Wrong biomes**: Check biome tag files for accuracy
3. **Too close together**: Verify spacing settings in structure sets
4. **Plugin not detecting**: Ensure center blocks match plugin expectations

### Debug Commands
```bash
/locate structure fallengod:fallen_altar
/datapack list
/reload
```

## 🎯 Design Philosophy

### Natural Integration
- Altars feel like natural world features
- Biome-appropriate materials and themes
- Balanced distribution across world types
- Encourages exploration of diverse areas

### Thematic Consistency
- Each altar reflects its god's domain
- Materials match the god's power theme
- Lighting and atmosphere enhance immersion
- Center blocks align with plugin mechanics

This datapack transforms the world into an epic quest landscape where ancient altars await discovery, each holding the power to complete divine testaments!

## 📄 Compatibility

- **Minecraft Version**: 1.21.5+
- **Pack Format**: 48
- **Compatible with**: All other datapacks
- **Required**: Fallen God Testament plugin for full functionality