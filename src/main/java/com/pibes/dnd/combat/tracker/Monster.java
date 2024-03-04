package com.pibes.dnd.combat.tracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Monster extends Combatant {
    private int temporalHealth;

    public Monster(String name, int ac, int initiative, int health) {
        super(name, ac, initiative, health);
        this.temporalHealth = health;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + getName() + '\'' +
                ", ac=" + getAc() +
                ", initiative=" + getInitiative() +
                ", health=" + getHealth() +
                ", temporalHealth=" + temporalHealth +
                '}';
    }
}