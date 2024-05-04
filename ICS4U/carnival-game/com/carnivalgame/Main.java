package com.carnivalgame;

import java.util.Scanner;

public class Main {
	
	// prints out menu of the possible selection of carnival games that the player can choose from
	// calls method menu
	public static void menu(){
		System.out.println("----------------------------------------");
		System.out.println("game 1: chooseADuck  (type 1 to play) ");
		System.out.println("game 2: Nim          (type 2 to play) ");
		System.out.println("game 3: ticketReRoll (type 3 to play) ");
		System.out.println("game 4: ticTacToe    (type 4 to play) ");
		System.out.println("Exit:                (type 10 to end) ");
		System.out.println("----------------------------------------");
	}
	
	
	
	
    public static void main(String[] args) {
		// initilizaing object
    	Player p1 = new Player();
		// initilizaing scanner
        Scanner scan = new Scanner(System.in);

		// to double check if user wants to play in the carnaval and take user input
        System.out.println("If u want to enter the carnaval type 1: ");
        int option = scan.nextInt();
        
		// if user input is 1, then takes user information include name and age and stores them
        if(option == 1){
            System.out.println("what is your name?: ");
            String name = scan.nextLine();
            name = scan.nextLine();
            System.out.println("what is your age?: ");
            String age = scan.nextLine();
            age = scan.nextLine();
            System.out.println(" ");
            
			/**
			 * TODO: Call game methods in while loop
			 * params: Scanner obj, Player obj
			 */
            while(option == 1){
				// calls menu to show the player of the possible selection of games
            	menu();
            	int gameOption = scan.nextInt();
				// if user chose to play game 1, calls method in booth that accounts for game 1
            	if( gameOption == 1){
            		Booth.chooseADuck(scan, p1);
				// if user chose to play game 2, calls method in booth that accounts for game 2
            	} else if ( gameOption == 2){
            		Booth.nim(scan, p1);
				// if user chose to play game 3, calls method in booth that accounts for game 3
            	} else if ( gameOption == 3){
            		Booth.ticketReRoll(scan, p1);
				// if user chose to play game 4, calls method in booth that accounts for game 4
            	} else if ( gameOption == 4){
            		Booth.ticTacToe(scan, p1);
				// if user no longer wants to play any carnaval games, exit the game option and enter prize trading section
            	} else if(gameOption == 10){ 
					break;
				// anything user inputs that is not what system asked for will print invalid input and ask for reinput
				} else {
            		System.out.print("invald input");
            	}
            }
			// inform player that game has ended and show them their final ticket sum
            System.out.println("The game has ended");
            System.out.println("U have " + p1.getTicketNum() + " tickets");
			// allow them to cash in a ticket for a random prize
			System.out.println("To cash in a ticket type 1: ");
            System.out.println(" ");
            int ticketcount = p1.getTicketNum();
            int option2 = scan.nextInt();
			// a while loop that gives player a prize everything he/she cashes in a ticket
			// and allow he/she to exit when ever needed
            while(option2 == 1) {
            	System.out.println("You won the " + p1.getPrize());
            	System.out.println("To cash in another ticket type 1, to exit type 0; ");
            	option2 = scan.nextInt();
            	System.out.println(" ");
            	ticketcount = ticketcount - 1;
            	if(ticketcount < 1) {
            		option2 = 0;
            	}
       
            }
        } 
    }
    public static void printMenu() { 
    }
}
