// Throughout this project, the use of data structures are not permitted such as methods like .split and .toCharArray




import java.util.Scanner;
// More packages may be imported in the space below
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.IOException;


class CustomerSystem{
    public static void main(String[] args) throws IOException { // I use IOException instead of FileNotFound because IOException already includes FileNotFound, and I need it for my file writing.
        // Please do not edit any of these variables
        Scanner reader = new Scanner(System.in);
        String userInput, enterCustomerOption, generateCustomerOption, exitCondition;
        enterCustomerOption = "1";
        generateCustomerOption = "2";
        exitCondition = "9";

        // More variables for the main may be declared in the space below

        do{
            printMenu();                                    // Printing out the main menu
            userInput = reader.nextLine();                  // User selection from the menu

            if (userInput.equals(enterCustomerOption)){
                // Only the line below may be editted based on the parameter list and how you design the method return
		        // Any necessary variables may be added to this if section, but nowhere else in the code
                enterCustomerInfo(reader, userInput);    
            }
            else if (userInput.equals(generateCustomerOption)) {
                // Only the line below may be editted based on the parameter list and how you design the method return
                generateCustomerDataFile(reader);
            }
            else{
                System.out.println("Please type in a valid option (A number from 1-9)");
            }

        } while (!userInput.equals(exitCondition));         // Exits once the user types 
        
        reader.close();
        System.out.println("Program Terminated");
    }

    public static void printMenu(){
        System.out.println("Customer and Sales System\n"
        .concat("1. Enter Customer Information\n")
        .concat("2. Generate Customer data file\n")
        .concat("3. Report on total Sales (Not done in this part)\n")
        .concat("4. Check for fraud in sales data (Not done in this part)\n")
        .concat("9. Quit\n")
        .concat("Enter menu option (1-9)\n")
        );
    }
    
    /**
     * Lets the user enter information about a customer and writes it to a file of their choice.
     * 
     * @author Vincent Tran
     * @param reader
     * @param userInput
     * @throws IOException
     */
    public static void enterCustomerInfo(Scanner reader, String userInput) throws IOException{

        // Prompts user to input what file they wish to save into
        String fileDir = fileSelect();
        if (fileDir.equals("error")) {  // If error is returned, exit method.
            return;
        }

        String inputType;  // Used to determine what info customer needs to input later
        int counter = 0;    // Counter variable used to keep track of what inputType is needed
        boolean valid;  // Boolean variable that keeps track of input validity

        String uniqueID = generateID(fileDir);

        while (counter<4) {

            valid = true;  // Sets valid to 1 to enable concatenation of strings.

            // Because I can't use any data structures, I used an elif chain to emulate an array.
            // This is done to determine which piece of data the user needs to input by switching between prompts. More optimized(?) and easier to read + work with.

            if (counter == 0) { // Checks on counter
                inputType = "full name: ";  // Changes inputType to prompt for fullName
            }

            else if (counter == 1) {
                inputType = "city: ";
            }

            else if (counter == 2) {
                inputType = "postal code: ";
            }

            else {
                inputType = "credit card number: ";
            }
            

            System.out.println("Please enter the customer's " + inputType);
            reader = new Scanner(System.in);
            userInput = reader.nextLine();


            if (reInput(reader, userInput)) {  // Asks the user if the information they inputted is correct.

                if (counter == 2 && !validatePostalCode(userInput.replaceAll("\\s", "").toUpperCase())) { // Checks if the user is supposed to postal information. Also removes spaces and converts to uppercase to avoid errors
                    System.out.println("This postal code is invalid.");
                    valid = false;  // Tells the program that this input was invalid.
                }
                else if (counter == 3 && !validateCreditCard(userInput.replaceAll("\\s", ""))) {    // Checks if the user is supposed to credit information.
                    System.out.println("This credit card is invalid.");
                    valid = false;  // Tells the program that this input was invalid.
                }

                if (valid) {    // Checks if the input was valid.
                    uniqueID = uniqueID.concat(", " + userInput);  // Concatenates into customInfo string to be saved later
                    counter++;  // Tells program that value has been entered, proceeding to next inputType.
                }
            }

        }

        writeToFile(uniqueID, fileDir);
    }

    /*
     * Description: Checks if the user-inputted postal code is valid - meaning it fulfills the length requirement & is found in CSV file
     * 
     * @author - Murphy Lee
     * @param postCode - A String containing the user-inputted postal code
     * @throws FileNotFoundException - Exception raised when attempting to read CSV, thrown to enterCustomerInfo method
     * @return isExistingZip - a boolean whose value depends on if the zip code meets the requirements
     */
    public static boolean validatePostalCode(String postCode) throws FileNotFoundException {
        // Check if the inputted postal code is long enough - at least 3 characters
        boolean validLen = isLongEnough(postCode, 3);
        if (validLen == false) {
            return false;
        }

        // Store the first 3 digits of user-input to compare with CSV values - this is the postal code
        String zipCode = postCode.substring(0, 3);

        // Compare the 3 characters with the postal codes found in CSV file
        boolean isExistingZip = compareZip(zipCode);
        return isExistingZip;
    }

    /*
    * Description: Checks if the user-inputted credit card is valid - meaning it fulfills the length requirement and passes the Luhn Algorithm
    * 
    * @author - Murphy Lee
    * @param creditNum - An int containing the user-inputted credit card number
    * @return isValid - A boolean whose value depends on if the user-input meets both requirements
    */
    public static boolean validateCreditCard(String creditNum){  

        // Check if the inputted credit card number is long enough - at least 9 characters
        boolean validLen = isLongEnough(creditNum, 9);
        if (validLen == false) {
            return false;
        }

        // Determine if the Luhn sum ends with 0, using the function
        boolean isValid = passesLuhnTest(creditNum); 

        return isValid;
    }

    /**
     * Generates a file that the user can later input information into.
     * 
     * @author Vincent Tran
     * @param reader
     * @throws IOException
     */
    public static void generateCustomerDataFile(Scanner reader) throws IOException {
        String fileDir; // The dir of the file
        String fileName;    // The name of the file

        // Asks user for file directory
        System.out.println("Please input the file directory you wish to save to. (Leave blank to save to program directory)");
        reader = new Scanner(System.in);
        fileDir = reader.nextLine();

        // Asks user for file name
        System.out.println("Please input the name of the file you wish to create or access. (Leave blank to save as customer_info.csv)");
        reader = new Scanner(System.in);
        fileName = reader.nextLine();

        // Get the path separator
        String separator = File.separator;

        // If file name is not given, set to default
        if (fileName.equals("")) {
            fileName = "customer_info";
        }

        fileName = fileName.concat(".csv");

        // If file directory is given, add to file name
        if (!fileDir.equals("")) {
            fileName = fileDir + separator + fileName;
        }
        // make directory
        new File(fileDir).mkdirs();

        // Make file to get path
        File name = new File(fileName);
        String absolute = name.getAbsolutePath();
      
        // Make file in the specified path
        File actualFile = new File(absolute);

        // Checking if the file exists, and creating a new file if it doesn't exist
        if (!actualFile.exists()) { // file.exists is a boolean. It checks if the boolean is false here and executes file creation.
            System.out.println("Created 1 file at " + actualFile);
            actualFile.createNewFile();
            writeToFile(absolute, "dataLocations.txt");
        }
        else {
            System.out.println("A file with the same name already exists in this directory.");
        }
    }

    /*******************************************************************
    *                        ADDITIONAL METHODS:                       *
    *******************************************************************/
    
    /**
     * Prompts the user of whether or not they meant to input something and triggers reinput if they didn't.
     * 
     * @author Vincent Tran
     * @param reader
     * @param userInput
     * @return needsInput
     */
    public static boolean reInput(Scanner reader, String userInput) {

        boolean needsInput = false;    // Initializes a Boolean variable, which will return to the program later to determine if re-input is required
        String userConfirm = " "; // Initializes userConfirm string variable

        // Asks user if input is correct
        System.out.println("Is '" + userInput + "' Correct? y/n");

        // While loop loops while needsInput is null
        int loop = 1;
        while (loop == 1) {
            reader = new Scanner(System.in);    // Calls reader for input
            userConfirm = reader.nextLine();  // Sets userConfirm as reader's input and lowercases for no case errors
            userConfirm = userConfirm.toLowerCase();

            if (userConfirm.equals("y")) {
                needsInput = true;  // Sets to true and kills loop
                loop = 0;
            }
            else if (userConfirm.equals("n")) {
                needsInput = false; // Sets to false and kills loop
                loop = 0;
            }
            else {
                System.out.println("You have not entered y/n. Please try again.");  // Prompts user that invalid input has been entered.
            }
        }
        // Returns needsInput
        return needsInput;
    }

    /**
     * Generates a unique customerID for a specifed file.
     * 
     * @author Vincent Tran
     * @param fileDir
     * @return strUniqueID
     * @throws FileNotFoundException
     */
    public static String generateID(String fileDir) throws FileNotFoundException {
        int uniqueID = 1;   // The unique ID that will be returned later for storage.
        String strUniqueID;
        String data;    // Used to check what each line contains
        File file = new File(fileDir); // Opens file for reading

        Scanner idFetcher;
        Scanner fileReader = new Scanner(file); // Reads from the file

        while (fileReader.hasNextLine()) {  // Reads until there are no more lines
            data = fileReader.nextLine();   // Reads line information
            idFetcher = new Scanner(data);
            
            // Using delimiter method to determine latest ID
            idFetcher.useDelimiter(",");
            uniqueID = idFetcher.nextInt();
            uniqueID++;
        }

        fileReader.close();

        strUniqueID = "" + uniqueID;    // converts to string so we can return this
        return strUniqueID;
    }

    /**
     * Prompts the user with a list of files and asks them to select an available file.
     * 
     * @author Vincent Tran
     * @return desiredFile
     * @throws FileNotFoundException
     */
    public static String fileSelect() throws FileNotFoundException {
        String desiredFile = "";

        String data;    // Used to check what each line contains
        int optionCount = 0;
        int optionChoice;
        int lineNum;

        System.out.println("Available files:");

        File file = new File("dataLocations.txt"); // Opens file for reading

        Scanner fileReader = new Scanner(file); // Reads from the file

        // Lists all file options to save into

        while (fileReader.hasNextLine()) {  // Reads until there are no more lines
            optionCount++;
            data = fileReader.nextLine();   // Reads line information
            System.out.println(optionCount + " " + data); // Displays available files to save into
        }
        fileReader.close();

        // Returns as error if there are no files available.

        if (optionCount < 1) {
            System.out.println("ERROR: No files available to save into. Please GENERATE A CUSTOMER DATA FILE.");
            return "error";
        }


        // Try and catch exception handling to kill any input mismatch possibilities.

        try {
            boolean loop = true;    // Tells program to loop following while loop. Used for reinput
            // While (loop) [haha get it] for re input.
            while (loop) {

                // Asks user to select what file they want
                if (optionCount > 1) {
                    System.out.println("What number file do you wish to store data into?");
                    Scanner userInput = new Scanner(System.in);
                    optionChoice = userInput.nextInt();
                }
                else {  // skips prompt if there is only 1 input
                    optionChoice = 1;
                }

                // Checks if file exists
                if (optionChoice > 0 && optionChoice <= optionCount) {
                    fileReader = new Scanner(file); // Reopens scanner
                    lineNum = 0;

                    // Uses lineNum to count how many lines have been read and compares it to what user has selected.
                    while (lineNum <= optionChoice && fileReader.hasNextLine()) {
                        desiredFile = fileReader.nextLine();   // Reads line information
                        lineNum++;
                    }
                    fileReader.close(); // Closes scanner
                    
                    loop = false;   // Kills loop
                }

                // If file does not exist, prompt user with error:
                else {
                    System.out.println("ERROR: You have selected a file that does not exist.");
                }
            }
        }
        catch (Exception inputMismatchException) {  // Catches if there is an invalid input.
            System.out.println("ERROR: You have entered an invalid input. Please try again.");
        }

        return desiredFile;
    
    }

    /**
     * Writes information to a specified file.
     * 
     * @author Vincent Tran
     * @param customInfo
     * @param fileDir
     * @throws IOException
     */
    public static void writeToFile(String customInfo, String fileDir) throws IOException {
        try {
            // Initializing writers
            File file = new File(fileDir);
            FileWriter fileWriter = new FileWriter(file,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter output = new PrintWriter(bufferedWriter);
    
            // Printing customer info into a new line in csv file
            output.print(customInfo);
            output.println("");
    
            // Closing the writer
            output.close(); 
    
            // Telling the user that the file has successfully been written to.
            System.out.println("Written to " + fileDir + ".");
            }
    
        catch (Exception IOException) { // I catch IOException instead of FileNotFoundException because IOException catches not only FileNotFound, but many other related exceptions too.
                System.out.println("An error has occured while writing to " + fileDir);
        }
    }
    
    /*
     * Description: Checks if the length of the inputted string matches/exceeds the specified length
     * 
     * @author - Murphy Lee
     * @param sequence - The String that needs to be validated
     * @param length - The minimum length that the String must be
     * @return isValid - Boolean whose value depends on whether the String is long enough
     * */
    public static boolean isLongEnough(String sequence, int length) {
        boolean isValid;   // Return value depends on validation

        // Store length of sequence in an int variable
        int sequenceLen = sequence.length();

        // Set boolean to true or false after comparing the user-input length to the recommended length
        if (sequenceLen >= length) {
            isValid = true;
        }
        else {
            isValid = false;
        }
        return isValid;
    } 

    /*
     * Description: Reverses a user-inputted String
     * 
     * @author - Murphy Lee
     * @param num - The String that needs to be reversed
     * @return reverse - The String whose characters have been reversed
     * */
    public static String reverseString(String text) {
        String reverse = "";  // Will store the reversed number
        String digit;         // Placeholder for the digits that will be added

        // Set a for loop that processes all digits - use the string length as a boundary
        for (int i = 1; i <= text.length(); i++) {
            // Extract each character in a String variable backwards - find index by subtracting i from the length
            digit = String.valueOf(text.charAt(text.length() - i));

            // Add new character to the reversed String variable
            reverse = reverse.concat(digit);
        }
        return reverse;
    }

    /*
     * Description: Checks if the user-inputted postal code is found in CSV file
     * 
     * @author - Murphy Lee
     * @param sequence - The String that needs to be validated
     * @throws FileNotFoundException - Exception raised when attempting to read CSV, thrown to ValidatePostalCode method
     * @return foundInFile - Boolean whose value depends on whether the String is found in the CSV file
     * */
    public static boolean compareZip(String postCode) throws FileNotFoundException {
        String line;                            // Will store each line of the file
        String code;                            // Will store postal code from each line
        boolean foundInFile = false;            // Will be set to true if the code is found in CSV
        String fileName = "postal_codes.csv";   // File name of the CSV

        // Create File instance of the CSV to read from
        File text = new File(fileName);

        // Create instances of Scanner
        Scanner reader = new Scanner(text, "ISO-8859-1");   // Will read text from CSV file in a different character set, to accept accented letters
        Scanner codeReader;                   // Will store each line from the CSV file

        // Read lines until there are no more
        while (reader.hasNextLine() == true) {
            // Read the next line in the file
            line = reader.nextLine();

            // Store line into seperate scanner - this way we can use the delimeter method
            codeReader = new Scanner(line);

            // Split line by using the "|" character as a delimiter - since "|" is a special character in Java, we need to escape it by using "\\"
            codeReader.useDelimiter("\\|");
            code = codeReader.next();

            // If the user-input matches the postal code, the found variable will be set to true
            if (postCode.equals(code)) {
                foundInFile = true;
            }
        }
        reader.close();
        return foundInFile;
    }

    /*
     * Description: Checks if the user-inputted credit card is valid
     * 
     * @author - Murphy Lee
     * @param creditNum - The user-inputted credit card
     * @return validNum - A boolean whose value depends on if the credit card has a valid Luhn-sum
     * */
    public static boolean passesLuhnTest(String creditNum) {
        int numLength = creditNum.length();   // Length of the credit card
        double digit;          // Digit to be added to the sums
        double digitDoubled;   // Digit that has been multiplied by 2
        double digit1;         // First digit of a double digit number
        double digit2;         // Second digit of a double digit number
        double numReverse;     // Stores the reversed credit card number as an int
        double sumOdd = 0;     // Sum of all the odd digits
        double sumEven = 0;    // Sum of all the even digits
        double sum;            // Contains the Luhn-sum

        // Don't accept String with "." - indicates a decimal number
        if (creditNum.indexOf(".") != -1) {
            return false;
        }

        // Reverse the credit card number - using the reverseString method
        String reversedNum = reverseString(creditNum);

        // Try to convert the number into a double
        try {
            numReverse = Double.parseDouble(reversedNum);
        }
        // If the a NumberFormatException error is thrown, return -1, signifying an error
        catch (NumberFormatException | NullPointerException e) {
            // This means the user input is invalid, and false can be returned
            return false;
        }

        // For loop that iterates over each digit of the number - use length of the String as a boundary
        for (int i = 0; i < numLength; i++) {
            // Find digit of number
            digit = numReverse % 10;

            // Find index of letter (not computer index - assuming first letter is 1) by subtracting counter from the length
            if ((numLength - i) % 2 != 0) {
                // For odd digits, we simply add to the odd pile sum
                sumOdd += digit;
            }
            // Otherwise, it is an even index
            else {
                // Multiply the digit by 2
                digitDoubled = digit * 2;
                // Determine if the number multiplied by 2 is single-digit
                if (Math.floor(digitDoubled) / 10 == 0) {
                    sumEven += digitDoubled;
                }
                // Otherwise, it is a double digit number and must be split
                else {
                    digit1 = Math.floor(digitDoubled / 10);    // The tens digit
                    digit2 = digitDoubled % 10;    // The ones digit
                    sumEven += (digit1 + digit2);
                }
            }
            // Take a digit off the credit card number and floor it - so it behaves similarly to an int
            numReverse = Math.floor(numReverse / 10);
        }
        sum = sumOdd + sumEven;

        // A boolean whose value is determined by whether the sum ends with a zero
        boolean validNum = (sum % 10 == 0);
        return validNum;
    }

}