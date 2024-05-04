package com.animalsurvival;

import com.animalsurvival.animals.*;
import com.animalsurvival.monsters.*;
import com.animalsurvival.utility.*;


import java.util.Scanner;
import java.util.Random;

public final class App {

    private static Scanner reader;
    private static Random rand = new Random();

    private App() {
        // Does nothing
    }

    public static void main(String[] args) {
        Scanner reader;
        String userInput = "";
        // Main menu loop
        do {
            printMenu();
            System.out.print("Your choice: ");
            reader = new Scanner(System.in);
            userInput = reader.nextLine();

            if (userInput.equals("1")) {
                newGame(userInput);
            }

        } while (!userInput.equals("9"));

        reader.close();
        System.exit(0);
    }

    /**
     * Main game loop
     * 
     * @param animal
     */
    public static void game(Animal animal) {
        String userInput = "";

        game_loop: do {

            for (int i = 0; i < 3; i++) {
                printDayMenu(animal);;

                System.out.println("Phase " + (i+1) + "/3");

                System.out.print("Your choice: ");
                reader = new Scanner(System.in);
                userInput = reader.nextLine();
    
                switch (userInput) {
                    case "1":
                        scavengeFood(animal, userInput);    // Makes animal scavenge for food
                        break;
                    case "2":
                        scavengeWater(animal, userInput);   // Makes animal scavenge for water
                        break;
                    case "3":
                        rest(animal);   // Rest with current animal to replenish health
                        break;
                    case "9":
                        if (quitGame()) return; // Returns to main menu if user confirms that they want to quit the game, else decrements day phase so that menu reloops.
                        // We don't use break here so it automatically reloops by going to switch's default which reloops the menu
                    default:
                        i--;    // Decrements day phase so that menu reloops upon incorrect input
                }

                if (animal.getHealth() < 1) {
                    break game_loop;
                }
            }

            System.out.println("The day passes.\n"
            .concat("-3 Hunger\n")
            .concat("-1 Thirst"));

            // Decrements hunger by 3, thirst by 1 and resets animal's attack values to full before changing day
            animal.changeHunger(-3);
            animal.changeThirst(-1);
            animal.resetAttackValues();
            animal.changeDay(1);    // Changes the day by one

            // Makes user press enter to continue
            System.out.println("Press ENTER to continue..");
            reader.nextLine(); 
            
        } while (animal.getHealth() > 1);

        System.out.println("GAME OVER! " + animal.getName() + " has died.\n"
        .concat("Returning to main menu.\n"));
    }

    /**
     * Initializes new animal instance and calls main game method with new animal and day 0
     * 
     * @param userInput String containing userInput
     */
    public static void newGame(String userInput) {
        String name = getName();
        String achoice = "";
        Animal animal;

        do {
            System.out.println("\nSelect an animal\n"
            .concat("1) Lion\n")
            .concat("2) Wolf\n"));

            System.out.print("Your choice: ");
            reader = new Scanner(System.in);
            achoice = reader.nextLine();

        } while(!(achoice.equals("1") || achoice.equals("2")));

        if (achoice.equals("1")) {
            animal = new Lion(name, "LION");
        }
        else {
            animal = new Wolf(name, "WOLF");
        }

        game(animal);
    }

    /**
     * Decides whether or not to initiate combat between an animal and monster and gives a randomly generated amount of food
     * 
     * @param animal Animal object
     * @param userInput userInput String
     */
    public static void scavengeFood(Animal animal, String userInput) {
        int encounterChance = rand.nextInt(10);
        int hungerGained = 0;

        if (encounterChance > 1) {
            hungerGained = initCombat(animal, userInput);
        }
        else {
            hungerGained = rand.nextInt(10)+1;
            System.out.println("You came across what seems to be a deserted animal hideout. Luckily, there seems to be leftovers inside");
        }

        // Increases hunger if hungerGained > 0
        if (hungerGained > 0) {
            animal.changeHunger(hungerGained);
            // Makes user press enter to continue
            System.out.println("+" + hungerGained + " Hunger\n"
            .concat("Press ENTER to continue.."));
            reader.nextLine();
        }
    }

    /**
     * Decides whether or not to initiate combat between an animal and monster and gives a randomly generated amount of food
     * 
     * @param animal Animal object
     * @param userInput userInput String
     */
    public static void scavengeWater(Animal animal, String userInput) {
        int encounterChance = rand.nextInt(10);
        int thirstGained = 0;

        if (encounterChance > 2) {
            thirstGained = initCombat(animal, userInput);
        }
        else {
            thirstGained = rand.nextInt(6)+1;
            System.out.println("You venture towards the local freshwater lake. Luckily there seems to be no animals nearby.");
        }
        if (thirstGained > 0) {
            animal.changeThirst(thirstGained);
            // Makes user press enter to continue
            System.out.println("+" + thirstGained + " Thirst\n"
            .concat("Press ENTER to continue.."));
            reader.nextLine(); // Makes user press enter to continue
        }
    }

    /**
     * Makes animal "rest" for a given amount of time, increasing HP and decreasing hunger and thirst.
     * 
     * @param animal Animal object
     */
    public static void rest(Animal animal) {
        int rest_time = -1;
        int health_regen;
        int base_health_regen = 0;
        int decrement_hunger;
        int decrement_thirst;

        // Gets an integer between 0 and 3, inclusive and then lets the animal rest and regenerate stats.
        do {
            try {
                System.out.println("How long do you wish to rest for?");
                System.out.print("Hours (0-3): ");
                reader = new Scanner(System.in);
                rest_time = reader.nextInt();

                if (rest_time < 0 || rest_time > 3) {
                    System.out.println("You did not enter a valid amount of time. Please try again.");
                }
            } catch (Exception InputMismatchException) {
                System.out.println("You did not enter an integer. Please try again.");
            }
        } while (rest_time < 0 || rest_time > 3);

        base_health_regen = (int)(animal.getMaxHealth()*0.1);
        health_regen = base_health_regen*rest_time;
        decrement_hunger = -1*(5*rest_time);
        decrement_thirst = -1*(3*rest_time);

        animal.changeHealth(health_regen);
        animal.changeHunger(decrement_hunger);
        animal.changeThirst(decrement_thirst);

        System.out.println("You took a nap.\n"
        .concat("+" + health_regen + " Health\n")
        .concat(decrement_hunger + " Hunger\n")
        .concat(decrement_thirst + " Thirst\n"));

        System.out.println("You are feeling refreshed. Attack values reset.");

        // Makes user press enter to continue
        System.out.println("Press ENTER to continue..");
        reader = new Scanner(System.in);
        reader.nextLine();
    }

    /**
     * Confirms with the user whether or not they wish to quit the game, then returns a boolean relevant to their choice
     * 
     * @return boolean containing t/f whether or not user wants to quit game
     */
    public static boolean quitGame() {
        System.out.println("Are you sure you want to quit the game? y/n");
        reader = new Scanner(System.in);
        String choice = reader.nextLine().toLowerCase();
        if (choice.equals("y")) {
            return true;
        }
        return false;
    }

    /**
     * Initiates combat between animal and monster.
     * 
     * @param animal Animal object
     * @param monster Monster object
     * @return Returns true or false on whether or not the battle was won
     */
    public static int initCombat(Animal animal, String userInput) {
        int turn = 0;
        int dmg = 0;
        int randExp = rand.nextInt(5) + 1;
        int randResource = rand.nextInt(9) + 7;

        Monster monster = CombatUtil.generateMonster(animal.getDay());
        System.out.println(animal.getName() + " encountered a wild " + monster.getName() + "!");
        // Loops while monster and animal are still alive
        while (animal.getHealth() > 0 && monster.getHealth() > 0) {
            // Animal turn to attack
            if (turn%2==0) {
                printCombatMenu(animal, monster);
                System.out.print("Your choice: ");
                reader = new Scanner(System.in);
                userInput = reader.nextLine();

                switch (userInput) {
                    case "1":
                        dmg = animal.attack1();
                        break;
                    case "2":
                        dmg = animal.attack2();
                        break;
                    case "3":
                        dmg = animal.attack3();
                        break;
                    case "4":
                        dmg = animal.attack4();
                        break;
                    default:
                        System.out.println("You did not select a valid attack. Try again.");
                        turn--;
                }

                if (dmg > 0) {
                    CombatUtil.attack(monster, dmg);
                }
            }
            // Monster turn to attack
            else {
                dmg = CombatUtil.randomAttack(monster);

                CombatUtil.attack(animal, dmg);
            }

            System.out.println("Press ENTER to continue..");
            reader.nextLine(); // Makes user press enter to continue

            turn++;
        }

        if (animal.getHealth() > 0) {
            System.out.println(monster.getName() + " was killed!\n"
            .concat("Gained +" + randExp + " exp!"));
            animal.increaseExp(randExp);
            return randResource;    // Returns randomly generated resource amount on battle victory
        }
        return 0;   // Returns 0 resource amount on battle loss
    }

    /**
     * Prints main menu for game
     */
    public static void printMenu() {
        System.out.println("Welcome to Animal Survival\n"
        .concat("1) New Game\n")
        .concat("9) Exit\n"));
    }

    /**
     * Prints menu for game
     * 
     * @param animal Animal object
     */    
    public static void printDayMenu(Animal animal) {
        System.out.println();
        System.out.println(animal.dayMenuToString());

        System.out.println("1) Scavenge for food (Hunger++)\n"
        .concat("2) Scavenge for water (Thirst++)\n")
        .concat("3) Rest (Health++, Hunger--, Thirst--)\n")
        .concat("9) Exit Game\n"));
    }

    /**
     * Prints a menu containing options for combat and information about animal and monster
     * 
     * @param animal Variable containing Animal object
     * @param monster Variable containing Monster object
     */
    public static void printCombatMenu(Animal animal, Monster monster) {
        System.out.println("\nYour HP: " + animal.getHealth() + "/" + animal.getMaxHealth());
        System.out.println(monster.toString());
        System.out.println(animal.attackMenuToString());
    }

    /**
     * Retrieves animal name from user and returns it
     * 
     * @param reader Scanner variable
     * @return
     */
    public static String getName() {
        String name = null;
        String choice = null;

        while (true) {
            System.out.println("\nPlease enter a name: ");
            reader = new Scanner(System.in);
            name = reader.nextLine();
            
            System.out.println("\nConfirm name as " + name + "? y/n");
            reader = new Scanner(System.in);
            choice = reader.nextLine().toLowerCase();
            if (choice.equals("y")) {
                break;
            }
        } 
        return name;
    }
}