package com.pibes.dnd.combat.tracker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Character extends Combatant {

    public Character(String name, int ac, int initiative, int health) {
        super(name, ac, initiative, health);
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + getName() + '\'' +
                ", ac=" + getAc() +
                ", initiative=" + getInitiative() +
                ", health=" + getHealth() +
                ", temporalHealth=" + getTemporalHealth()+
                '}';
    }
}