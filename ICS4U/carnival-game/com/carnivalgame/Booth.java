/**
 * Class file containing all callable methods for carnival games.
 * 
 * @author Vincent Tran
 * @teacher E. Katsman
 * @course ICS4U
 */
package com.carnivalgame;

import java.util.Random;
import java.util.Scanner;

public class Booth {
    /**
     * Main choose-a-duck game method
     * 
     * @param reader
     * @param player
     */
    public static void chooseADuck(Scanner reader, Player player) {
        Random rand = new Random();
        String userInput;
        int duckWinner;
        
        do {
            HelperClass.printDuck();
            reader = new Scanner(System.in);
            userInput = reader.nextLine();

            duckWinner = rand.nextInt(6) + 1;   // Selects random correct duck

            // Checks if userInput is valid.
            if (0 < Integer.parseInt(userInput) && Integer.parseInt(userInput) < 7) {
                HelperClass.printFinalDucks(duckWinner);
                if ((""+duckWinner).equals(userInput)) {
                    player.increaseTicketNum(5);
                    System.out.println("\n\nCongratulations! You guessed correctly!\n"
                    .concat("You gained +5 tickets!\n")
                    .concat("Current ticket balance: " + player.getTicketNum()));
                } else {
                    System.out.println("\n\nSorry! You guessed the wrong duck. The correct duck was number " + duckWinner + ".");
                }
                System.out.println("Press ENTER to continue...");
                reader.nextLine();
            }
        } while (!userInput.equals("9"));
        return;
    }

    /**
     * Main Nim method to be run in main to play game
     * 
     * @param reader
     * @param player
     */
    public static void nim(Scanner reader, Player player) {
        String userInput = "";
        int[] piles;
        int turn;

        int cpuInput;
        int selected_sticks = 0;

        Random rand = new Random();

        // Loops Nim prompt while user still wishes to play
        while (1==1) {
            turn = 0;
            piles = HelperClass.generatePiles(rand);
            HelperClass.printMenu(piles);
            reader = new Scanner(System.in);
            userInput = reader.nextLine();

            if (userInput.equals("9")) {    // If user wants to exit, exit.
                break;
            }

            System.out.println("You are Player 1.");

            // Main game loop
            while ((piles[0] + piles[1] + piles[2]) > 1) {
                if (turn % 2 == 0) {
                    // User input
                    System.out.print("Please select what pile you wish to remove from: ");
                    reader = new Scanner(System.in);
                    userInput = reader.nextLine().toLowerCase();
                    
                    System.out.print("Enter the amount you wish to remove: ");
                    reader = new Scanner(System.in);
                    selected_sticks = reader.nextInt();

                    System.out.println();   // Prints empty line for spacing

                    // Changes values if userInput is valid
                    if (HelperClass.validateInput(userInput, selected_sticks, piles)) {
                        piles[(int)userInput.charAt(0) - 97] -= selected_sticks;
                    }
                    else {
                        turn--; // Decrements turn by 1 to reloop
                    }
                    
                }
                else {
                    cpuInput = rand.nextInt(3); // Generates a random number upto 3
                    // Determines if the randomly genned num's pile > 0
                    if (piles[cpuInput] > 0) {
                        // Removes upto piles[cpuInput]'s maximum value of sticks from selected pile.
                        selected_sticks = rand.nextInt(piles[cpuInput])+1;
                        System.out.println("Player 2 removes " + selected_sticks + " from " + (char)(97+cpuInput) + "\n");
                        piles[cpuInput] -= selected_sticks;
                    }
                    else {  // Decrements turn by 1 if the selected pile is empty / under zero
                        turn--;
                    }
                }
                HelperClass.printPile(piles);
                System.out.println();
                turn++;
            }

            // Winning statements
            if (turn%2 == 1) {
                player.increaseTicketNum(15);   // Increases ticket count
                System.out.println("Congratulations! Player 1 wins.\n"
                .concat("You gained +15 tickets!\n"
                .concat("Current ticket balance: " + player.getTicketNum() + "\n\n")));
            }
            else {
                System.out.println("Game over! Player 2 wins.");
            }
            System.out.println("Press ENTER to continue...");
            reader = new Scanner(System.in);
            reader.nextLine();
        }
        return;
    }

    /**
     * Main method for ticketReRoll game
     * 
     * @param reader
     * @param player
     */
    public static void ticketReRoll(Scanner reader, Player player) {
        Random rand = new Random();
        int tickets = 0;
        int num;
        int userInput;

        // Loops while player still wants to play
        while (HelperClass.menuPrompt(reader, player)) { // Calls method menuPrompt to print menu and determine if user wants to play
            num = rand.nextInt(500)+1;  // Generates random number for target num

            // Gets userInput, loops while invalid.

            do {
                System.out.print("Deposit tickets: ");
                reader = new Scanner(System.in);
                tickets = reader.nextInt();
            } while(tickets > player.getTicketNum());

            player.decreaseTicketNum(tickets);

            do {
                System.out.print("Guess (1-500): ");
                reader = new Scanner(System.in);
                userInput = reader.nextInt();
            } while(1 > userInput || userInput > 500);

            // Adds tickets * 10 into player.ticketNum if player guesses correctly
            if (userInput == num) {
                System.out.println("Congratulations! You have guessed correctly, therefore you have gained " + (tickets*10) + "tickets\n");
                player.increaseTicketNum(tickets*10);
            }
            // Else does nothing and prompts user
            else {
                System.out.println("You guessed incorrectly. The target number was: " + num);
            }
            // Pauses program until player enters anything
            System.out.println("Press ENTER to continue...");
            reader = new Scanner(System.in);
            reader.nextLine();
        }
        return;
    }

    /**
     * Main method for Tic Tac Toe game
     * 
     * @param reader
     * @param player
     */
    public static void ticTacToe(Scanner reader, Player player) {
        Random rand = new Random();
        String userInput = "";
        int compChoice;
        int[][] gridArr;
        int row;
        int turn;

        boolean hasWon;
        while (1==1) {
            turn = 0;
            gridArr = new int[3][3];
            hasWon = false;

            HelperClass.printMenu();
            reader = new Scanner(System.in);
            userInput = reader.nextLine();

            if (userInput.equals("9")) {
                break;
            }

            System.out.println("You are player 1.");

            // Game loop while slots are empty
            while (HelperClass.canContinue(gridArr)) {
                HelperClass.printGrid(gridArr);   // Prints grid

                // Determines who's turn it is
                if (turn % 2 == 0) {
                    // User input
                    System.out.println("Please choose a letter column: ");
                    reader = new Scanner(System.in);
                    userInput = reader.nextLine().toLowerCase();

                    System.out.println("Please choose a number row: ");
                    reader = new Scanner(System.in);
                    row = reader.nextInt();

                    // Checks if input is valid and slot is not full
                    if (HelperClass.validateColumn(userInput, row, gridArr)) {
                        gridArr[row][(int)userInput.charAt(0)-97] = 1;  // Marks given slot with 1
                    }
                    else{   // If not valid, decrement turn
                        turn--;
                    }
                }
                // Cpu turn
                else {
                    // Randomly generates CPU choice
                    compChoice = rand.nextInt(3);
                    row = rand.nextInt(3);
                    // If selected slot is empty,
                    if (gridArr[row][compChoice] == 0) {
                        System.out.println("Player 2 selects (" + (char)(compChoice+97) + ", " + row + ")");
                        gridArr[row][compChoice] = 2;   // Mark selected slot as cpu
                    }
                    else {  // Else decrement turn
                        turn--;
                    }
                    
                }

                // Breaks loop if someone wins
                if (HelperClass.hasWon(gridArr)) {
                    hasWon = true;
                    break;
                }

                turn++;
            }
            HelperClass.printGrid(gridArr);
            if (hasWon == true) {
                if (turn%2 == 0) {
                    player.increaseTicketNum(15);   // Increases ticket count
                    System.out.println("Congratulations! Player 1 wins.\n"
                    .concat("You gained +15 tickets!\n"
                    .concat("Current ticket balance: " + player.getTicketNum() + "\n\n")));
                }
                else {
                    System.out.println("Game over! Player 2 wins.");
                }
            }
            else {
                System.out.println("You tied, no tickets were rewarded.");
            }
            // Pauses program
            System.out.println("Press ENTER to continue...");
            reader = new Scanner(System.in);
            reader.nextLine();
        }
    }
}

