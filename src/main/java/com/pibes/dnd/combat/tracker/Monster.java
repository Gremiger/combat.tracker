package com.pibes.dnd.combat.tracker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Monster extends Combatant {


    public Monster(String name, int ac, int initiative, int health) {
        super(name, ac, initiative, health, "M");
    }


    @Override
    public String toString() {
        return "Monster{" +
                "name='" + getName() + '\'' +
                ", ac=" + getAc() +
                ", initiative=" + getInitiative() +
                ", health=" + getHealth() +
                ", temporalHealth=" + getTemporalHealth() +
                '}';
    }
}