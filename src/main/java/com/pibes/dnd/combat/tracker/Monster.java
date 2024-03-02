package com.pibes.dnd.combat.tracker;

public class Monster {
    private String name;
    private int ac;
    private int initiative;
    private int health;

    //Constructor
    public Monster(String name, int ac, int initiative, int health) {
        this.name = name;
        this.ac = ac;
        this.initiative = initiative;
        this.health = health;
    }

    //Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getAC() {
        return ac;
    }

    public void setAC(int ac) {
        this.ac = ac;
    }
    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
