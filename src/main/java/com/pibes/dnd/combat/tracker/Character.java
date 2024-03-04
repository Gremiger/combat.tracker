package com.pibes.dnd.combat.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Character extends Combatant {
    private int temporalHealth;

    public Character(String name, int ac, int initiative, int health) {
        super(name, ac, initiative, health);
        this.temporalHealth = health;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + getName() + '\'' +
                ", ac=" + getAc() +
                ", initiative=" + getInitiative() +
                ", health=" + getHealth() +
                ", temporalHealth=" + temporalHealth +
                '}';
    }
}