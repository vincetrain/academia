package com.animalsurvival.monsters;

public class Monster {

    int max_health;
    int health;

    String name;

    /**
     * EMPTY CONSTRUCTOR FOR REUSEABILITY; USE MONSTER TYPE CONSTRUCTORS INSTEAD
     * <p>
     * An empty constructor is used here because Monster does not define any variable values, and is only used as a structure for all Monster types,
     * meaning we can universally call methods from each Monster type.
     */
    public Monster() {
        // EMPTY
    }

    /**
     * Returns String containing name of Monster
     * 
     * @return name String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns int containing max_health of Monster
     * 
     * @return max_health int
     */
    public int getMaxHealth() {
        return max_health;
    }

    /**
     * Returns int containing current Monster health
     * 
     * @return health int
     */
    public int getHealth() {
        return health;
    }

    /**
     * Increments or decrements monster health by a given health_adjustment integer value if it does not go over max_health
     * <p>
     * Usage: monster.changeHealth(-45) or monster.changeHealth(45), etc.
     * 
     * @param health_adjustment integer containing value to change health by
     */
    public void changeHealth(int health_adjustment) {
        health += health_adjustment;
    }

    /**
     * Method that contains information about, and calculates the damage of Monster's attack1
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.1)*min_dmg)
     * 
     * @return damage to be dealt to animal
     */
    public int attack1() {
        System.out.println("Error: " + this.getClass() + " does not contain attack1() method");
        return 0;
    }

    /**
     * Method that contains information about, and calculates the damage of Monster's attack2
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.1)*min_dmg)
     * 
     * @return damage to be dealt to animal
     */
    public int attack2() {
        System.out.println("Error: " + this.getClass() + " not contain attack2() method");
        return 0;
    }

    /**
     * Method that contains information about, and calculates the damage of Monster's attack3
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.1)*min_dmg)
     * 
     * @return damage to be dealt to animal
     */
    public int attack3() {
        System.out.println("Error: " + this.getClass() + " does not contain attack3() method");
        return 0;
    }

    /**
     * Method that contains information about, and calculates the damage of Monster's attack4
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.1)*min_dmg)
     * 
     * @return damage to be dealt to animal
     */
    public int attack4() {
        System.out.println("Error: " + this.getClass() + " does not contain attack4() method");
        return 0;
    }

    public String toString() {
        return (
            name + ": " + health + "/" + max_health
        );
    }
}
