package com.animalsurvival.animals;

import java.util.Random;

public class Lion extends Animal{
    Random rand = new Random();

    String[] attackNames = {"Tackle", "Slash", "Bite", "Pounce"};

    final int[] maxAttackVals = {20, 13, 7, 3};
    int[] attackVals = {20, 13, 7, 3};

    /**
     * Constructor for Lion
     * 
     * @param name
     */
    public Lion(String name, String type) {
        super(name, type);
        
        super.attackNames = this.attackNames;
        super.maxAttackVals = this.maxAttackVals;
        super.attackVals = this.attackVals;

        // Randomizes stat integers for Lion
        str = rand.nextInt(21)+20;
    }

    @Override
    public int attack1() {
        if (attackVals[0] < 1) {
            System.out.println(name + " is too exhausted to use " + attackNames[0]);
            return -1;
        }

        System.out.println(name + " used " + attackNames[0]);
        int damage = (int)(4 + level/2 + ((str*0.1)*(level/2)));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        attackVals[0]--;
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack2() {
        if (attackVals[1] < 1) {
            System.out.println(name + " is too exhausted to use " + attackNames[1]);
            return -1;
        }

        System.out.println(name + " used " + attackNames[1]);
        int damage = (int)(7 + level/2 + ((str*0.1)*(level/2)));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        attackVals[1]--;
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack3() {
        if (attackVals[2] < 1) {
            System.out.println(name + " is too exhausted to use " + attackNames[2]);
            return -1;
        }

        System.out.println(name + " used " + attackNames[2]);
        int damage = (int)(9 + level/2 + ((str*0.1)*(level/2)));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.05)*min_dmg)
        attackVals[2]--;
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack4() {
        if (attackVals[3] < 1) {
            System.out.println(name + " is too exhausted to use " + attackNames[3]);
            return -1;
        }

        System.out.println(name + " used " + attackNames[3]);
        int damage = (int)(12 + level/2 + ((str*0.1)*(level/2)));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        attackVals[3]--;
        return damage;  // Returns damage int to be used
    }
}
