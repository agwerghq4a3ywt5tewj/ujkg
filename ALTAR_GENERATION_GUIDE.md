# Altar Generation Troubleshooting Guide

## 🚨 Current Issue: Altars Not Generating

Your datapack has all the necessary files, but there are several potential issues preventing altar generation:

## 🔧 Step-by-Step Fix

### 1. **Check Datapack Installation**
```bash
# Verify datapack is in the correct location
ls /path/to/world/datapacks/fallengod_testament/

# Should show:
# - pack.mcmeta
# - data/fallengod/...
```

### 2. **Verify Datapack is Loaded**
In-game commands:
```
/datapack list
/datapack list enabled
```
You should see `[file/fallengod_testament]` in the enabled list.

### 3. **Force Reload Datapack**
```
/reload
```
Or:
```
/datapack disable "file/fallengod_testament"
/datapack enable "file/fallengod_testament"
```

### 4. **Check Structure Generation**
```
/locate structure fallengod:fallen_altar
/locate structure fallengod:banishment_altar
```

### 5. **Generate New Chunks**
**CRITICAL**: Altars only generate in **newly explored chunks**. Existing chunks will NOT have altars.

- Travel to unexplored areas (at least 1000+ blocks from spawn)
- Use `/tp @s ~ ~ ~1000` to get to new areas
- Explore new chunks to trigger generation

## 🎯 Quick Test Method

### Method 1: Use Plugin Commands
```
/datapack scan 2000
/datapack locate fallen
```

### Method 2: Manual Structure Placement
If generation still fails, you can manually place altar structures:
```
/place structure fallengod:fallen_altar ~ ~ ~
/place structure fallengod:banishment_altar ~50 ~ ~
```

## 🔍 Common Issues & Solutions

### Issue 1: Datapack Not Loading
**Symptoms**: `/datapack list` doesn't show your pack
**Solution**: 
- Check pack.mcmeta format
- Ensure correct folder structure
- Restart server

### Issue 2: Structures Not Registering
**Symptoms**: `/locate structure` says "Unknown structure"
**Solution**:
- Verify all JSON files are valid
- Check biome tags exist
- Ensure template pools reference correct structures

### Issue 3: Biome Restrictions
**Symptoms**: Altars only generate in specific biomes
**Current Settings**:
- Fallen: Swamps, Dark Forests, Deep Dark
- Banishment: Deserts, Badlands, Savannas  
- Abyssal: All ocean types
- Sylvan: Forests, Jungles, Taigas
- Tempest: Mountains, Hills, Peaks
- Veil: End dimension only

### Issue 4: Spacing Too Large
**Current Settings**: 32 chunks apart (512 blocks)
**Solution**: Reduce spacing in structure_set file

## 🎮 Testing Procedure

1. **Enable Creative Mode**: `/gamemode creative`
2. **Teleport to New Area**: `/tp @s 2000 100 2000`
3. **Check Biome**: `/locate biome minecraft:swamp`
4. **Force Generate**: Fly around to load new chunks
5. **Scan for Altars**: `/datapack scan 1000`

## 🛠️ Debug Commands

```bash
# Check if structures are registered
/locate structure fallengod:fallen_altar

# Scan for nearby altars
/datapack scan 2000

# Test altar interaction
/fragment testfragments fallen
# Then find/place crying_obsidian and right-click it
```

## 📊 Expected Generation Rates

With current settings:
- **Spacing**: 32 chunks (512 blocks apart)
- **Separation**: 8 chunks minimum between different types
- **Coverage**: ~1 altar per 1024x1024 block area

## 🎯 Quick Fix Commands

If you want immediate testing:
```
# Give yourself test fragments
/fragment testfragments fallen

# Place a test altar center block
/give @s crying_obsidian

# Place the block and right-click to test
```

## 🔄 Alternative: Manual Altar Building

If generation continues to fail, you can build altars manually using the patterns in the README.md file. Each altar follows a 7x7 base pattern with specific center blocks for each god.

## 📞 Still Not Working?

1. Check server console for errors during `/reload`
2. Verify Minecraft version compatibility (1.21.5+)
3. Test with a fresh world
4. Check if other datapacks are conflicting

The most likely issue is that you need to explore **new, ungenerated chunks** for the altars to appear!