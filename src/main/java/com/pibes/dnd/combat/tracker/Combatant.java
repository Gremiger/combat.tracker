package com.pibes.dnd.combat.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Combatant {
    private static int nextId = 1;
    private int id;
    private String name;
    private int ac;
    private int initiative;
    private int health;
    private int temporalHealth;

    // Constructor without ID
    public Combatant(String name, int ac, int initiative, int health) {
        this.id = nextId++;
        this.name = name;
        this.ac = ac;
        this.initiative = initiative;
        this.health = health;
        this.temporalHealth = health;
    }
}
