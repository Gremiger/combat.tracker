package com.pibes.dnd.combat.tracker;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class Monster extends Combatant{
    public Monster(String name, int ac, int initiative, int health){
        //super(name, ac, initiative, health);
        this.setName(name);
        this.setAc(ac);
        this.setInitiative(initiative);
        this.setHealth(health);
    }
    @Override
    public String toString() {
        String var10000 = this.getName();
        return "Monster(name=" + var10000 + ", ac=" + this.getAc() + ", initiative=" + this.getInitiative() + ", health=" + this.getHealth() + ")";
    }
}