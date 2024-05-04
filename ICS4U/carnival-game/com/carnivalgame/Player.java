package com.carnivalgame;
/**
 * Holds prize, money
 * Strength, stamina, luck
 * Win game = +1 ticket, run out of ticket = end.  Reset ticket
 */

// import random package
import java.util.Random;

class Player {
    // setting all instance variables which includes strength, stamina, luck, ticketNum and prize
    private int strength;
    private String name;
    private int age;
    private int stamina;
    private static int luck;
    private static int ticketNum;
    private static String prize;
    
    // intializing random
    private final Random rand = new Random();

    // constructor method, gives player name, age, strength, stamina, luck, ticket number and the prize
    // they would win
    public Player(){
        name = " ";
        age = 0;
        strength = setStrength();
        stamina = setStamina();
        luck = setLuck();
        ticketNum = 5;
        prize = getPrize();
    }
    
    /**
     * allows user to set age
     * is a modifier method
     * @param newAge set age of the user
     */
    public void setAge(int newAge){
        age = newAge;
    }
    
    /**
     * gets user age
     * is an accessor method
     * @return the age
     */
    public int getAge(){
        return age;
    }
    /**
     * is a modifier method
     * allow user to set their name
     * @param newName allow user to store name and change name to this name
     */
    public void setName(String newName){
        name = newName;
    }
    /**
     * is an accessor method
     * gets user name
     * @return name
     */
    public String getName(){
        return name;
    }
    /**
     * Randomlizes player strength
     * is a modifier method
     * @return player strenth
     */
    public int setStrength(){
        return strength = rand.nextInt(10); // the range is from 0 to 10
    }
    /**
     * is an accessor method
     * changes player strength
     * @param newStength stores new strength
     * @return the new strength
     */
    public int changeStength(int newStength){
        return strength = newStength;
    } 
    /**
     * is a modifier
     * randomlizes initial player stamina
     * @return player stamina
     */
    public int setStamina(){
        return stamina = rand.nextInt(10); // the range is from 0 to 10
    }
    /**
     * is an accessor method
     * changes player stamina
     * @param newStamina stores new stamina
     * @return returns the new stamina
     */
    public int changeStamina(int newStamina){
        return stamina = newStamina;
    } 
    /**
     * set luck for player
     * modifier method
     * @return player luck
     */
    public int setLuck(){
        // intializae max and min range for the luck
        int max = 3; 
        int min = 1;
        luck = rand.nextInt(max - min) + min; // give three possible lucks, good luck = 3, bad luck = 1 and average luck = 2
        return luck;
    }
    /**
     * accessor method
     * changes player luck
     * @param newLuck variable to change player luck
     * @return new player luck
     */
    public static int changeLuck(int newLuck){
        return luck = newLuck;
    } 
    /**
     * is an accessor method
     * get the user ticketNumber 
     * @return the ticketNumber
     */
    public static int getTicketNum(){
        return ticketNum;
    }
    /**
     * modifier method
     * increase ticket number based on main or booth
     * @param x the number by which how much the tickets will be added by
     * @return the new ticket num
     */
    public static void increaseTicketNum(int x){
        ticketNum += x;
    }
    /**
     * modifier method
     * decrease ticket number based on main or booth
     * @param x the number by which how much the tickets will be subtracted by
     * @return the new ticket num
     */
    public static void decreaseTicketNum(int x){
        ticketNum -= x;
    }
    /**
     * accessor method, determines user prize based on luck variable
     * @return prize
     */
    public static String getPrize(){ 

        double num = randDouble(100, 3.01);
        double range = luck; // change int luck to a double variable so it can be manipulated with double variable num
        
        // the number that determines what prize user gets
        num = num - range;

        // return the prize for the user
        if(num == 0.01){
            return prize = "Legendary Prize(Stuffed animal): Pig - Large";
        }
        else if(num > 0.01 && num < 1.00){
            return prize = "Epic Prize(Stuffed animal): Panda - medium sized";
        }
        else if(num >= 1.00 & num < 2.00){
            return prize = "Epic Prize(Stuffed animal): Pikachu - medium sized";
        }
        else if(num >= 2.00 && num < 3.00){
            return prize = "Rare Prize(Stuffed animal): Shark - tiny";
        }
        else if(num >= 3.00 && num < 4.00){
            return prize = "Rare Prize(Stuffed animal): Bird - tiny";
        }
        else if(num >= 4.00 && num < 5.00){
            return prize = "Rare Prize: Stuffed animal - Lizard - large";
        }
        else if(num >= 5 && num < 10){
            return prize = "Good Prize: Pencil Case";
        }
        else if(num >= 10 && num < 20){
            return prize = "Good Prize: Coloured Pencil";
        }
        else if(num >= 20 && num < 30){
            return prize = "Good Prize: Pencil Sharpener";
        }
        else if(num >= 30 && num < 40){
            return prize = "Normal Prize: Eraser";
        }
        else if(num >= 40 && num < 50){
            return prize = "Normal Prize: Pencil";
        }
        else if(num >= 50 && num < 60){
            return prize = "Normal Prize: 10 Marbles of different color";
        }
        else if(num >= 60 && num < 70){
            return prize = "Normal Prize: 5 Marbles of different color";
        }
        else{
            return prize = "Worst Prize: 1 white marble engraved with the word 'Failure'";
        }
    }
    /**
     * is a helper method
     * @param max sets the max of the range for the randomly generated number
     * @param min sets the min of the range for the randomly generated number
     * @return the randomly generated double number
     */
    public static double randDouble(double max, double min){
        Random rand = new Random(); 
        double d = min + (max - min) * rand.nextDouble(); 
        d = (int)(d * 100)/100.0;

        return d;
    } 
}
