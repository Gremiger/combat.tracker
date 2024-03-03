package com.pibes.dnd.combat.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Combatant {
    private String name;
    private int ac;
    private int initiative;
    private int health;
}
