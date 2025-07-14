# Custom Textures Guide for Fallen God Testament

## 🎨 Overview
This guide provides detailed instructions for finding, downloading, and implementing custom textures for all testament items using Minecraft's resource pack system.

## 📋 Items Needing Custom Textures

### Core Testament Items
- **Heart of Fallen God** (Custom Model Data: 100001)
- **Veil of Nullification** (Custom Model Data: 100002)

### Fallen God Armor Set
- **Fallen Helmet** (Custom Model Data: 100010)
- **Fallen Chestplate** (Custom Model Data: 100011)
- **Fallen Leggings** (Custom Model Data: 100012)
- **Fallen Boots** (Custom Model Data: 100013)

### God Weapons
- **Banishment Sword** (Custom Model Data: 100020)
- **Banishment Axe** (Custom Model Data: 100021)
- **Banishment Pickaxe** (Custom Model Data: 100022)
- **Abyssal Trident** (Custom Model Data: 100030)
- **Sylvan Bow** (Custom Model Data: 100040)
- **Storm Elytra** (Custom Model Data: 100050)

### Future Expansion Weapons
- **Forge Mace** (Custom Model Data: 100060)
- **Void Blade** (Custom Model Data: 100061)
- **Chronos Staff** (Custom Model Data: 100062)
- **Crimson Blade** (Custom Model Data: 100063)
- **Resonance Crystal** (Custom Model Data: 100064)
- **Shadow Cloak** (Custom Model Data: 100065)

## 🔍 Where to Find Custom Textures

### 1. **Planet Minecraft** (planetminecraft.com)
**Search Terms:**
- "custom item textures"
- "fantasy weapon textures"
- "divine armor textures"
- "mythical items resource pack"

**Recommended Packs:**
- "Epic Weapons Texture Pack"
- "Fantasy Items Resource Pack"
- "Divine Artifacts Textures"
- "Mythical Weapons Collection"

### 2. **CurseForge** (curseforge.com/minecraft/texture-packs)
**Filter by:**
- Category: "Items"
- Minecraft Version: 1.21+
- Sort by: "Most Downloaded"

**Look for:**
- Weapon enhancement packs
- Fantasy-themed resource packs
- Custom item collections

### 3. **GitHub Repositories**
**Search for:**
- "minecraft custom item textures"
- "fantasy weapon textures minecraft"
- "custom model data textures"

### 4. **Texture Creation Tools**

#### **Free Options:**
- **GIMP** (gimp.org) - Professional image editor
- **Paint.NET** (getpaint.net) - User-friendly editor
- **Blockbench** (blockbench.net) - Minecraft model editor
- **Nova Skin** (novaskin.me) - Online Minecraft editor

#### **Paid Options:**
- **Photoshop** - Industry standard
- **Aseprite** - Pixel art specialist

## 🎨 Specific Texture Themes to Look For

### **Heart of Fallen God**
- Dark purple/black orb with glowing effects
- Skull or death motifs
- Pulsing or animated textures
- Gothic/dark fantasy style

### **Veil of Nullification**
- Ethereal, ghostly appearance
- Void-like darkness with stars
- Semi-transparent effects
- Reality-bending visual distortions

### **Fallen Armor Set**
- Dark, spiky netherite armor
- Undead/gothic themes
- Purple/black color scheme
- Soul fire accents

### **Banishment Weapons**
- Fire-themed designs
- Red/orange color schemes
- Demonic or infernal motifs
- Lava/magma textures

### **Abyssal Trident**
- Ocean/water themes
- Blue/teal color schemes
- Coral, seaweed, or sea creature motifs
- Flowing water effects

### **Sylvan Bow**
- Nature/forest themes
- Green/brown color schemes
- Wood, leaves, or vine textures
- Living wood appearance

### **Storm Elytra**
- Lightning/cloud themes
- Yellow/white/blue colors
- Electric or storm effects
- Feathered or ethereal wings

### **Future Expansion Weapons**
- **Forge Mace**: Molten metal, anvil themes, orange/red glow
- **Void Blade**: Pure black, void effects, reality tears
- **Chronos Staff**: Clock/time themes, purple/gold, gears
- **Crimson Blade**: Blood red, battle-worn, dark metal
- **Resonance Crystal**: Crystalline, rainbow prisms, sound waves
- **Shadow Cloak**: Dark fabric, shadow effects, stealth themes

## 📦 Resource Pack Implementation

### 1. **Create Resource Pack Structure**
```
testament_textures/
├── pack.mcmeta
├── assets/
│   └── minecraft/
│       ├── models/
│       │   └── item/
│       │       ├── totem_of_undying.json
│       │       ├── phantom_membrane.json
│       │       ├── netherite_helmet.json
│       │       ├── netherite_sword.json
│       │       ├── trident.json
│       │       ├── bow.json
│       │       ├── elytra.json
│       │       └── mace.json
│       └── textures/
│           └── item/
│               ├── heart_of_fallen_god.png
│               ├── veil_of_nullification.png
│               ├── fallen_helmet.png
│               ├── banishment_sword.png
│               ├── abyssal_trident.png
│               ├── sylvan_bow.png
│               ├── storm_elytra.png
│               └── forge_mace.png
```

### 2. **pack.mcmeta Example**
```json
{
  "pack": {
    "pack_format": 48,
    "description": "§6Fallen God Testament §7Custom Textures\n§8Divine weapons and artifacts"
  }
}
```

### 3. **Model File Examples**

#### **Heart of Fallen God (totem_of_undying.json)**
```json
{
  "parent": "item/generated",
  "textures": {
    "layer0": "item/totem_of_undying"
  },
  "overrides": [
    {
      "predicate": {
        "custom_model_data": 100001
      },
      "model": "item/heart_of_fallen_god"
    }
  ]
}
```

#### **Custom Model (heart_of_fallen_god.json)**
```json
{
  "parent": "item/generated",
  "textures": {
    "layer0": "item/heart_of_fallen_god"
  }
}
```

## 🎯 Texture Requirements

### **Technical Specifications**
- **Resolution**: 16x16 or 32x32 pixels (higher for detailed items)
- **Format**: PNG with transparency support
- **Color Depth**: 32-bit RGBA
- **Animation**: Supported via .mcmeta files

### **Design Guidelines**
- **Consistency**: Maintain similar art style across all items
- **Readability**: Ensure items are recognizable at small sizes
- **Theme Matching**: Align with each god's theme and lore
- **Contrast**: Ensure good visibility against various backgrounds

## 🔧 Implementation Steps

### 1. **Download/Create Textures**
- Find suitable textures from recommended sources
- Edit them to match your vision using image editors
- Ensure proper resolution and format

### 2. **Set Up Resource Pack**
- Create the folder structure shown above
- Add pack.mcmeta with correct pack format
- Place texture files in correct locations

### 3. **Create Model Files**
- Copy base item model files
- Add custom model data overrides
- Reference your custom textures

### 4. **Test Implementation**
- Install resource pack in Minecraft
- Use `/fragment` commands to spawn items
- Verify textures appear correctly
- Test in different lighting conditions

### 5. **Distribute to Players**
- Upload resource pack to file hosting service
- Provide download link to server players
- Include installation instructions

## 🎨 Recommended Texture Artists

### **Popular Minecraft Texture Artists:**
- **Vattic** - Creator of Faithful texture pack
- **Compliance Team** - High-quality texture updates
- **Jappa** - Official Minecraft texture artist style
- **Steelfeathers** - Fantasy-themed textures

### **Commission Options:**
- **Fiverr** - Affordable custom textures ($5-50)
- **DeviantArt** - Professional artists
- **Reddit r/HungryArtists** - Commission marketplace
- **Discord Servers** - Minecraft texture communities

## 🎮 Advanced Features

### **Animated Textures**
Create .mcmeta files for animated effects:
```json
{
  "animation": {
    "frametime": 4,
    "frames": [0, 1, 2, 3, 2, 1]
  }
}
```

### **Emissive Textures**
Add glowing effects with _e suffix:
- `heart_of_fallen_god.png` (base texture)
- `heart_of_fallen_god_e.png` (emissive overlay)

### **3D Models**
Use Blockbench to create full 3D models instead of flat textures for ultimate visual impact.

## 📊 Performance Considerations

- **Texture Size**: Larger textures use more memory
- **Animation**: Animated textures impact performance
- **Quantity**: Too many custom textures can slow loading
- **Optimization**: Use texture atlases for efficiency

## 🎯 Quick Start Checklist

- [ ] Choose texture source (Planet Minecraft, CurseForge, etc.)
- [ ] Download or create textures for core items first
- [ ] Set up basic resource pack structure
- [ ] Test with Heart of Fallen God and Veil of Nullification
- [ ] Expand to other items gradually
- [ ] Share with server community
- [ ] Gather feedback and iterate

## 🔗 Useful Links

- **Minecraft Wiki - Resource Packs**: minecraft.wiki/w/Resource_pack
- **Custom Model Data Guide**: minecraft.wiki/w/Tutorials/Models
- **Blockbench Tutorials**: blockbench.net/wiki/
- **Texture Pack Guidelines**: minecraft.wiki/w/Tutorials/Creating_a_resource_pack

This guide provides everything needed to implement stunning custom textures for your Fallen God Testament items. Start with the core items and expand from there!