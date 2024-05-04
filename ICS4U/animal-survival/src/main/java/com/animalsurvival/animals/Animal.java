package com.animalsurvival.animals;

import java.util.Random;

public class Animal {

    private Random rand = new Random();

    String[] attackNames = null;

    int[] maxAttackVals = null;
    int[] attackVals = null;

    int max_health;

    final int max_hunger = 50;
    final int max_thirst = 30;

    String name;
    String type;

    int health;
    int hunger;
    int thirst;

    int level;
    int exp;
    int req_exp;    

    int str;

    int day;

    /**
     * Animal Constructor
     * 
     * @param name user name for animal
     */
    public Animal(String name, String type) {
        this.name = name;
        this.type = type;
        max_health = 60;
        health = max_health;
        hunger = rand.nextInt((max_hunger-34))+35;
        thirst = rand.nextInt((max_thirst-14))+15;

        level = 1;
        exp = 0;
        req_exp = 10; 
        day = 0;
    }

    /**
     * Returns string value containing name of player animal
     * 
     * @return returns name String
     */
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /**
     * Returns integer value containing max health of player animal
     * 
     * @return returns max_health integer
     */
    public int getMaxHealth() {
        return max_health;
    }

    /**
     * Returns integer value containing max_hunger of player animal
     * 
     * @return returns max_hunger integer
     */
    public int getMaxHunger() {
        return max_hunger;
    }

    /**
     * Returns integer value containing max_thirst of player animal
     * 
     * @return returns max_thirst integer
     */
    public int getMaxThirst() {
        return max_thirst;
    }

    /**
     * Returns integer value containing health of player animal
     * 
     * @return returns health integer
     */
    public int getHealth() {
        return health;
    }

    /**
     * Returns integer value containing hunger of player animal
     * 
     * @return returns health integer
     */    
    public int getHunger() {
        return hunger;
    }

    /**
     * Returns integer value containing thirst of player animal
     * 
     * @return returns thirst integer
     */
    public int getThirst() {
        return thirst;
    }

    /**
     * Increments or decrements animal health by a given health_adjustment integer value if it does not go over max_health
     * <p>
     * Usage: animal.changeHealth(-45) or animal.changeHealth(45), etc.
     * 
     * @param health_adjustment integer containing value to change health by
     */
    public void changeHealth(int health_adjustment) {
        if (health + health_adjustment > max_health) {
            health = max_health;
            return;
        }
        health += health_adjustment;
    }

    /**
     * Increments or decrements animal hunger by a given hunger_adjustment integer value if it does not go over max_hunger
     * <p>
     * Usage: animal.changeHunger(-45) or animal.changeHunger(45), etc.
     * 
     * @param hunger_adjustment integer containing hunger to change value by
     */
    public void changeHunger(int hunger_adjustment) {
        if (hunger + hunger_adjustment >= max_hunger) {
            hunger = max_hunger;
            return;
        }
        hunger += hunger_adjustment;
        if (hunger < 1) {
            health = 0;
        }
    }

    /**
     * Increments or decrements animal thirst by a given thirst_adjustment integer value if it does not go over max_thirst
     * <p>
     * Usage: animal.changeThirst(-45) or animal.changeThirst(45), etc.
     * 
     * @param thirst_adjustment integer containing value to change thirst by
     */
    public void changeThirst(int thirst_adjustment) {
        if (thirst + thirst_adjustment >= max_thirst) {
            thirst = max_thirst;
            return;
        }
        thirst += thirst_adjustment;
        if (thirst < 1) {
            health = 0;
        }
    }

    /**
     * Increases exp variable of this.animal. If exp >= req_exp, levels up animal.
     * 
     * @param new_exp the new exp to be added onto the animal
     */
    public void increaseExp(int new_exp) {
        exp += new_exp;
        // Checks if animal is ready to level up
        if (exp >= req_exp) {
            level++;    // Increases level
            exp = req_exp+new_exp - req_exp;    // Sets exp to 0, or the remainder exp after levelling
            req_exp *= 1.5; // Multiplies req_exp by 1.5x
            System.out.println("Level up! " + name + " is now level " + level); // Prints that animal levelled up
            str += 1;
            max_health += 5;
            health = max_health;
        }
    }

    /**
     * Method that contains information about, and calculates the damage of Animal's attack1
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.05)*min_dmg)
     * 
     * @return damage to be dealt to monster
     */
    public int attack1() {
        System.out.println("Error: " + this.getClass() + " does not contain attack1() method");
        return 0;
    }

    /**
     * Method that contains information about, and calculates the damage of Animal's attack2
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.05)*min_dmg)
     * 
     * @return damage to be dealt to monster
     */
    public int attack2() {
        System.out.println("Error: " + this.getClass() + " not contain attack2() method");
        return 0;
    }

    /**
     * Method that contains information about, and calculates the damage of Animal's attack3
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.05)*min_dmg)
     * 
     * @return damage to be dealt to monster
     */
    public int attack3() {
        System.out.println("Error: " + this.getClass() + " does not contain attack3() method");
        return 0;
    }

    /**
     * Method that contains information about, and calculates the damage of Animal's attack4
     * <p>
     * Formula is base_dmg + min_dmg + ((str*0.05)*min_dmg)
     * 
     * @return damage to be dealt to monster
     */
    public int attack4() {
        System.out.println("Error: " + this.getClass() + " does not contain attack4() method");
        return 0;
    }

    /**
     * Resets attack values for animal.
     */
    public void resetAttackValues() {
        for (int i = 0; i < attackVals.length; i++) {
            attackVals[i] = maxAttackVals[i];
        }
    }

    /**
     * Returns animal day value
     * 
     * @return day integer
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Increments or decrements animal day by a given day_adjustment integer value
     * <p>
     * Usage: animal.changeDay(-1) or animal.changeDay(1), etc.
     */
    public void changeDay(int day_adjustment) {
        day+=day_adjustment;
    }

    @Override
    public String toString() {
        return (
            type + "\n" 
            + name + "\n"
            + health + "\n" 
            + max_health + "\n"
            + hunger + "\n" 
            + thirst + "\n" 
            + level + "\n" 
            + exp + "\n" 
            + req_exp + "\n" 
            + day + "\n"
            + str + "\n" 
        );
    }

    /**
     * Returns a string containing all information about all possible animal attack options.
     * 
     * @return String containing attack information
     */
    public String attackMenuToString() {
        return (
            "1) " + attackNames[0] + " " + attackVals[0] + "/" + maxAttackVals[0] + "\n"
            + "2) " + attackNames[1] + " " + attackVals[1] + "/" + maxAttackVals[1] + "\n"
            + "3) " + attackNames[2] + " " + attackVals[2] + "/" + maxAttackVals[2] + "\n"
            + "4) " + attackNames[3] + " " + attackVals[3] + "/" + maxAttackVals[3] + "\n"
        );
    }

    /**
     * Returns a string containing all relevant information about animal for day menu.
     * 
     * @return String containing day information and relevant information about animal
     */
    public String dayMenuToString() {
        return (
            "Name: " + name + " | Type: " + type + " | Day: " + day + "\n"
            + "Level " + level + " | Exp: " + exp + "/" + req_exp + "\n"
            + "Health: " + health + "/" + max_health + "\n"
            + "Hunger: " + hunger + "/" + max_hunger + "\n"
            + "Thirst: " + thirst + "/" + max_thirst
        );
    }


}
