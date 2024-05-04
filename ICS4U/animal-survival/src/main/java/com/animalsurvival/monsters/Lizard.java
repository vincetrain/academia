package com.animalsurvival.monsters;

import java.util.Random;

public class Lizard extends Monster {
    Random rand = new Random();

    final String[] modifiers = {"Giant", "Speedy", "Vicious"};

    int str;

    int min_dmg;

    /**
     * Lizard constructor
     * 
     * @param days days survived by player, used for difficulty scaling
     */
    public Lizard(int days) {
        super();
        int modifier = rand.nextInt(modifiers.length);
        name = modifiers[modifier] + " Lizard";
        min_dmg = 1+((int)(days*0.1));

        // Modifies each stat value to according modifier
        if (modifier == 1) {
            max_health = rand.nextInt(11)+days+5;
            str = rand.nextInt(11)+40;
        } 
        else if (modifier == 2) {
            max_health = rand.nextInt(9)+days+2;
            str = rand.nextInt(11)+10;
        }
        else {
            max_health = rand.nextInt(11)+days+20;
            str = rand.nextInt(16)+35;
        }

        health = max_health;
    }

    @Override
    public int attack1() {
        System.out.println(name + " used Tail Whip!");
        int damage = (int)(1 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack2() {
        System.out.println(name + " used Tackle!");
        int damage = (int)(3 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack3() {
        System.out.println(name + " used Claw!");
        int damage = (int)(6 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack4() {
        System.out.println(name + " used Bite!");
        int damage = (int)(8 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }
}
