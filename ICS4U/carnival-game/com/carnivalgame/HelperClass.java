package com.carnivalgame;

import java.util.Scanner;
import java.util.Random;

public class HelperClass {
    /**
     * Prints menu of ducks
     */
    public static void printDuck() {
        //introduction
        System.out.println("Welcome to pick-a-duck!\n" 
        .concat("The rules of the game are simple, there are six ducks in front of you.\n\n") 
        .concat("   _      _      _      _      _      _\n") 
        .concat(" <(.)__ <(.)__ <(.)__ <(.)__ <(.)__ <(.)__\n")
        .concat("  (_1_/  (_2_/  (_3_/  (_4_/  (_5_/  (_6_/\n\n")
        .concat("Your job is to guess which one has the star * inside.\n\n")
        .concat("Enter 1-6 to select, or 9 to exit.\n")); 
    }

    /**
     * Prints new row of ducks, displaying where the correct duck is.
     * 
     * @param x
     */
    public static void printFinalDucks(int x) {
        System.out.println("   _      _      _      _      _      _\n"
        .concat(" <(.)__ <(.)__ <(.)__ <(.)__ <(.)__ <(.)__")); // Prints top row of ducks
        // Prints bottom row of ducks
        for (int i = 0; i < 6; i++) {
            // If printing the correct duck, print unique duck bottom with asterisk
            if (i+1==x) {
                System.out.print("  (_*_/");
            }
            // Else print regular duck instead
            else {
                System.out.print("  (_"+(i+1)+"_/");
            }
        }
    }


    /**
     * Prints each pile of Nim
     * 
     * @param arr
     */
    public static void printPile(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print("Pile " + (char)(97+i) + ": ");
            // prints an asterisk for the listed number of values within each pile
            for (int j = 0; j < arr[i]; j++) {
                System.out.print("*");
            }
            System.out.print(" ("+arr[i]+")\n");  // prints total amount of asterisks in a pile for readability
        }
    }

    /**
     * Prints the main menu for Nim
     * 
     * @param piles
     */
    public static void printMenu(int[] piles) {
        System.out.println("Welcome to Nim!\n"
        .concat("The rules to this game are simple, you will be given 3 piles where you must continually remove sticks until there is no more than one left.\n")
        .concat("During your turn you are able to remove as many sticks as you desire, \n"));
        printPile(piles);
        System.out.println("Press ENTER to play, or 9 to exit\n");
    }

    /**
     * Generates and returns an array consisting of numbers between 1 and 10 for piles
     * 
     * @return
     */
    public static int[] generatePiles(Random rand) {
        rand = new Random();
        int[] arr = new int[3];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(10)+1;
        }
        return arr;
    }

    /**
     * Checks if the inputted information is correct, and returns t/f
     * 
     * @param userInput
     * @param selected_sticks
     * @param piles
     * @return
     */
    public static boolean validateInput(String userInput, int selected_sticks, int[] piles) {
        if ((96 < (int)userInput.charAt(0) && (int)userInput.charAt(0) <  100)) {   // Checks if userInput is within a-c inclusive
            if (0 < selected_sticks && selected_sticks <= piles[(int)userInput.charAt(0) - 97]) {   // Checks if userInput is not outside of boundary of piles[userInput]
                return true;    // Returns true if all checks pass
            }
        }
        // Returns false and prompts user if any check fails.
        System.out.println("You did not select a valid selection please try again.");
        return false;
    }



    /**
     * Prints menu and determines whether or not user wants to play.
     * Returns true if user wishes to play, else false.
     * 
     * @param reader
     * @param player
     * @return
     */
    public static boolean menuPrompt(Scanner reader, Player player) {
        String userString;
        System.out.println("Welcome to Ticket Re-roll!\n"
        .concat("The rules of this game are simple. Upon depositing x amount of tickets, you must guess what number will be chosen within 1-500 inclusive.\n")
        .concat("If you guess correctly, you will be given 10 times the amount deposited.\n\n")
        .concat("You have " + player.getTicketNum() + " tickets.\n")
        .concat("Press ENTER to play, or 9 to exit.\n"));
        reader = new Scanner(System.in);
        userString = reader.nextLine();
        if (!userString.equals("9")) {
            return true;
        }

        return false;
    }


    /**
     * Prints menu
     */
    public static void printMenu() {
        System.out.println("Welcome to Tic Tac Toe!\n"
        .concat("Your objective of this game is to score a line of 3 symbols before your opponent does.\n")
        .concat("Press ENTER to start or 9 to exit...\n"));
    }

    /**
     * Prints grid taken from 2d array gridArr
     * 
     * @param gridArr
     */
    public static void printGrid(int[][] gridArr) {
        System.out.println("a b c");
        for (int i = 0; i < gridArr.length; i++) {
            for (int j = 0; j < gridArr[i].length; j++) {
                System.out.print(gridArr[i][j]);
                if (j<gridArr[i].length-1) System.out.print("|");
            }
            System.out.print(" " + i);
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Checks if game has been won yet
     * 
     * @param gridArr
     * @return
     */
    public static boolean hasWon(int[][] gridArr) {
        // Checks for diagonal wins
        if (gridArr[1][1] > 0 && ((gridArr[0][0] == gridArr[1][1] && gridArr[1][1] == gridArr[2][2]) || (gridArr[0][2] == gridArr[1][1] && gridArr[1][1] == gridArr[2][0]))) {
            System.out.println("This happened");
            return true;
        }
        // Checks for vertical wins
        for (int i = 0; i<gridArr.length; i++) {
            if (gridArr[i][0] > 0 && (gridArr[i][0] == gridArr[i][1] && gridArr[i][1] == gridArr[i][2])) {
                return true;
            }
        }
        // Checks for horizontal wins
        for (int i = 0; i<gridArr.length; i++) {
            if (gridArr[0][i] > 0 && (gridArr[0][i] == gridArr[1][i] && gridArr[1][i] == gridArr[2][i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns t/f value based on whether or not a slot is empty/=0
     * 
     * @param gridArr
     * @return
     */
    public static boolean canContinue(int[][] gridArr) {
        for (int i = 0; i < gridArr.length; i++) {
            for (int j = 0; j < gridArr.length; j++) {
                if (gridArr[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the inputted information is correct, and returns t/f
     * 
     * @param letter
     * @param number
     * @param gridArr
     * @return
     */
    public static boolean validateColumn(String letter, int number, int[][] gridArr) {
        if ((96 < (int)letter.charAt(0) && (int)letter.charAt(0) <  100)) {   // Checks if userInput is within a-c inclusive
            if (number < 3) {
                if (gridArr[number][(int)letter.charAt(0)-97] == 0) {
                    return true;
                }
            }
        }
        // Returns false and prompts user if any check fails.
        System.out.println("You did not select a valid column, please try again.");
        return false;
    }
}
