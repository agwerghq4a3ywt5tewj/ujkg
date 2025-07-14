package com.fallengod.testament.quests;

import com.fallengod.testament.enums.GodType;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Set;

public enum LegendaryQuest {
    THE_CONVERGENCE("The Convergence", ChatColor.GOLD, 
                   "Unite all divine powers under one banner",
                   List.of("Complete all 12 testaments", "Achieve perfect balance", "Transcend mortality"),
                   Set.of(GodType.values())),
    
    THE_SUNDERING("The Sundering", ChatColor.RED,
                 "Choose one god and reject all others",
                 List.of("Complete exactly 1 testament", "Resist all other divine influences", "Achieve pure devotion"),
                 Set.of()),
    
    THE_BALANCE("The Balance", ChatColor.BLUE,
               "Maintain equilibrium between opposing forces",
               List.of("Complete exactly 6 testaments", "Balance opposing gods", "Achieve harmony"),
               Set.of()),
    
    THE_TRANSCENDENCE("The Transcendence", ChatColor.LIGHT_PURPLE,
                     "Surpass the gods themselves",
                     List.of("Complete all testaments", "Defeat a god in combat", "Achieve true divinity"),
                     Set.of(GodType.values()));
    
    private final String name;
    private final ChatColor color;
    private final String description;
    private final List<String> objectives;
    private final Set<GodType> requiredGods;
    
    LegendaryQuest(String name, ChatColor color, String description, List<String> objectives, Set<GodType> requiredGods) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.objectives = objectives;
        this.requiredGods = requiredGods;
    }
    
    public String getName() {
        return name;
    }
    
    public ChatColor getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<String> getObjectives() {
        return objectives;
    }
    
    public Set<GodType> getRequiredGods() {
        return requiredGods;
    }
    
    public String getColoredName() {
        return color + name + ChatColor.RESET;
    }
}