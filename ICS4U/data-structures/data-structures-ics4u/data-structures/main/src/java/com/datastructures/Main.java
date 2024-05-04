package com.datastructures;

import com.datastructures.api.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        String userInput = "";

        String integerStack = "1";
        String countryList = "2";
        String exitCondition = "9";

        // Loops program until user wishes to exit
        do {
            printMenu();
            reader = new Scanner(System.in);
            userInput = reader.nextLine();

            if (userInput.equals(integerStack)) {
                integerStack(reader);
            }
            else if (userInput.equals(countryList)) {
                countryList(reader);
            }
        } while(!userInput.equals(exitCondition));
        
        reader.close();
        System.exit(0);
    }

    /**
     * Prints menu for the program.
     */
    public static void printMenu() {
        System.out.println("ICS4U Data Structures Assignment -- Vincent Tran\n"
                    .concat("1) Integer Stack\n")
                    .concat("2) Country List\n")
                    .concat("9) Exit\n"));
    }

    /**
     * Method handling integerStack functionality of the program
     */
    public static void integerStack(Scanner reader) {
        Stack integerStack = new Stack(10);
        Stack popNegativeStack;
        int userInt;

        // Loops 10 times for user to input 10 elements into stack
        for (int i = 1; i < 11; i++) {
            System.out.println("Please enter integer " + i + ":");
            userInt = reader.nextInt();
            integerStack.push(userInt);
        }

        System.out.println("\nOriginal Stack: " + integerStack.toString() + "\n");    // Prints integerStack as a String

        popNegativeStack = integerStack.popNegative();    // Extracts numbers into new stack until a negative is found
        System.out.println("New popNegative Stack: " + popNegativeStack.toString());    // Prints popNegativeStack as a String
        System.out.println("Modified Stack: " + integerStack.toString() + "\n");    // Prints integerStack as a String

        System.out.println("Sum of Stack elements until first negative: " + popNegativeStack.sum());  // Gets the sum of all elements in popNegativeStack
        System.out.println("Remaining elements of Stack: " + integerStack.size() + "\n");   // Prints the size of integerStack

    }

    /**
     * Method handling countryList functionality of the program
     */
    public static void countryList(Scanner reader) {
        LinkedList countryList = new LinkedList();
        String name;
        int population;

        // Loops 6 times to get 6 countries from user
        for (int i = 1; i < 7; i++) {
            // Gets user input for both country name and population
            System.out.println("Enter country "+ i + ":");
            reader = new Scanner(System.in);
            name = reader.nextLine();
        
            System.out.println("Enter population for " + name + ":");
            reader = new Scanner(System.in);
            population = reader.nextInt();

            countryList.addAtFront(name, population);   // Adds new country to front of list
        }

        // Prints original linked list
        System.out.println("\nYour original Linked List: " + countryList.toString());

        // Reverses and prints Linked List
        countryList.reverse();
        System.out.println("Your reversed Linked List: " + countryList.toString() + "\n");

        // Prints country with longest name and country with smalles population
        System.out.println("Longest name: " + countryList.longestName());
        System.out.println("Smallest Population: " + countryList.smallestPopulation() + "\n");
    }
}