package com.animalsurvival.monsters;

import java.util.Random;

public class Hyena extends Monster {
    Random rand = new Random();

    final String[] modifiers = {"Strong", "Agile", "Deadly"};

    int str;

    int min_dmg;

    /**
     * Hyena constructor
     * 
     * @param days days survived by player, used for difficulty scaling
     */
    public Hyena(int days) {
        super();
        int modifier = rand.nextInt(modifiers.length);
        name = modifiers[modifier] + " Hyena";
        min_dmg = 2+((int)(days*0.1));

        // Modifies each stat value to according modifier
        if (modifier == 1) {
            max_health = rand.nextInt(21)+days+10;
            str = rand.nextInt(21)+30;
        } 
        else if (modifier == 2) {
            max_health = rand.nextInt(11)+days+5;
            str = rand.nextInt(16)+25;
        }
        else {
            max_health = rand.nextInt(11)+days+20;
            str = rand.nextInt(11)+40;
        }

        health = max_health;
    }

    @Override
    public int attack1() {
        System.out.println(name + " used Slash!");
        int damage = (int)(2 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack2() {
        System.out.println(name + " used Bite!");
        int damage = (int)(5 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack3() {
        System.out.println(name + " used Claw!");
        int damage = (int)(9 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack4() {
        System.out.println(name + " used Pounce!");
        int damage = (int)(14 + min_dmg + ((str*0.1)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }
}
