package com.animalsurvival.monsters;

import java.util.Random;

public class Scorpion extends Monster {
    Random rand = new Random();

    final String[] modifiers = {"Giant", "Agile", "Deadly"};

    int str;

    int min_dmg;

    /**
     * Scorpion constructor
     * 
     * @param days days survived by player, used for difficulty scaling
     */
    public Scorpion(int days) {
        super();
        int modifier = rand.nextInt(modifiers.length);
        name = modifiers[modifier] + " Scorpion";
        min_dmg = 2+((int)(days*0.1));

        // Modifies each stat value to according modifier
        if (modifier == 1) {
            max_health = rand.nextInt(16)+days+10;
            str = rand.nextInt(11)+40;
        } 
        else if (modifier == 2) {
            max_health = rand.nextInt(11)+days+5;
            str = rand.nextInt(11)+20;
        }
        else {
            max_health = rand.nextInt(16)+days+20;
            str = rand.nextInt(21)+30;
        }

        health = max_health;
    }

    @Override
    public int attack1() {
        System.out.println(name + " used Poke!");
        int damage = (int)(3 + min_dmg + ((str*0.05)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack2() {
        System.out.println(name + " used Slash!");
        int damage = (int)(5 + min_dmg + ((str*0.05)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack3() {
        System.out.println(name + " used Pierce!");
        int damage = (int)(7 + min_dmg + ((str*0.05)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }

    @Override
    public int attack4() {
        System.out.println(name + " used Envelop!");
        int damage = (int)(10 + min_dmg + ((str*0.05)*min_dmg));    // Calculates the damage by using formula base_dmg + min_dmg + ((str*0.1)*min_dmg)
        return damage;  // Returns damage int to be used
    }
}