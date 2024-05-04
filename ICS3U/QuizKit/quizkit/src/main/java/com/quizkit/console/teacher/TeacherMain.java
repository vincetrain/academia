package com.quizkit.console.teacher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import com.quizkit.api.model.Quiz;
import com.quizkit.api.model.StudentQuiz;
import com.quizkit.api.model.Teacher;
import com.quizkit.api.sendgrid.EmailConnection;
import com.quizkit.api.services.*;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This is a console version of the Teacher quiz manager. It is used to test the API, and then to assist 
 * the team with developing the GUI version.
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class TeacherMain {

    /**
     * Teacher main. Shows a menu and allows the teacher to create a quiz, start a quiz, see marks and export.
     * 
     * @param args Command line arguments. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void main(String[] args) throws Exception {

        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

         // Login menu held in an array. 
        String[] loginMenu = {"Teacher Login", "Register Teacher", "Quit"};

         // Navigation menu held in an array. 
        String[] navMenu = {"Create a new quiz from CSV", "List all quizzes", "Import student list from CSV", "Start a quiz", "See quiz results", "Export quiz results", "Logout"};

        // Sets the Teacher object to null. 
        Teacher loggedInTeacher = null;

        // Do/while loop that repeats until the teacher logs-in or quits. This loop is the for the login menu.
        do {

            // Prints the menu for the teacher login and saves the input. 
            int loginResult = printMenu("quizKit Teacher Login", loginMenu);
            
            // If the result is 1 (the teacher wants to log in), then log in the teacher.
            if (loginResult == 1) {
                
                // Logs in a teacher using the loginTeacher function. 
                loggedInTeacher = loginTeacher();

                // If a loginis null, then it goes back to the do/while loop login. 
                if (loggedInTeacher == null)

                    // Continues back to the do/while login loop. 
                    continue;

                // Infinite loop until the teacher decides to log out. Then it returns to the login menu.
                for(;;) {

                    // Prints the main menu. 
                    int navResult = printMenu("quizKit Teacher Main Menu", navMenu);
                    
                    // If the teacher enters the first option (Create a new quiz from CSV), then create a new quiz from an inputted CSV. 
                    if (navResult == 1) {

                        // Teacher enters the title of their quiz. 
                        System.out.print("\nEnter the name of the Quiz: ");

                        // Gets the name of the quiz the teacher entered. 
                        String quizName = scanner.nextLine();

                        // Asks the teacher to enter the filename of the Quizzes CSV, and saves the file path to the filePath variable.  
                        String filePath = getFile("Enter filename of Quizzes CSV: ", "quizzes/physics.csv", false);

                        // Creates a new Quiz object from the inputted CSV path. 
                        Quiz quiz = QuizService.createQuizFromCSV(quizName, filePath);

                        // Adds a Quiz object to a teacher's account in Firebase.
                        TeacherService.addTeacherQuiz(loggedInTeacher.teacherEmail, quiz);

                        // refresh local object
                        loggedInTeacher = TeacherService.getTeacher(loggedInTeacher.teacherEmail);

                        // Prints that the quiz has successfully been loaded from the CSV and a file path of that CSV. 
                        System.out.println("Quiz successfully loaded from CSV " + filePath);
                    }

                    // If the teacher enters the second option (List all quizes), then list all the teacher's quizzes. 
                    else if (navResult == 2) {

                        // Calls the function printQuizList to print a list of the teacher's quizzes.
                        printQuizList("List of teacher's quizzes", loggedInTeacher);
                    }

                    // If the teacher enters the third option (Import student list from CSV), ask teacher what quiz they want to 
                    // add students to then ask for a CSV input and add the students. 
                    else if (navResult == 3) {

                        // Asks which quiz the teacher wants to add students to. 
                        String quizId = selectFromQuizList("Select a Quiz to add students to", loggedInTeacher);

                        // If the quiz ID exists, then get the student CSV file and add students to the Quiz ID. 
                        if (quizId != null) {

                            // Gets the filepath of the Student CSV file. 
                            String filePath = getFile("Enter filename of Students CSV: ", "studentlist/students.csv", false);

                            // Creates StudentQuiz objects from the students csv file. 
                            ArrayList<StudentQuiz> students = StudentService.createStudentsFromCSV(filePath);

                            // Adds students to the quiz. 
                            StudentService.addStudentsToQuiz(quizId, students);

                            // Prints that students were successfully loaded from the filepath. 
                            System.out.println("Students successfully loaded from " + filePath);
                        }
                    }

                    // If the teacher enters the fourth option (Start a quiz), it asks teachers to select a quiz to launch, then
                    // sends out unique quiz codes to students via email. 
                    else if (navResult == 4) {

                        // Asks teacher to select a quiz to launch. 
                        String quizId = selectFromQuizList("Select a Quiz to launch. Codes will be emailed to students", loggedInTeacher);

                        // If the quiz ID that the teacher wants to start isn't null, emaill all the students for the quiz. 
                        if (quizId != null) {

                            // Gets StudentQuiz objects from a quiz and puts it in an ArrayList.
                            ArrayList<StudentQuiz> studentQuizList = StudentService.getStudentsFromQuiz(quizId);

                            // Constructs a blank EmailConnection object. 
                            EmailConnection email = new EmailConnection();

                            // For every student in the ArrayList of students, send an email to them. 
                            for (StudentQuiz student : studentQuizList) {

                                // Prints out who the invitation is currently being sent to. 
                                System.out.println("Sending invitation to " + student.studentEmail + " ...");

                                // Sends quiz code emails to students using SendGrid. 
                                email.Send(student.studentName, student.studentEmail, student.studentQuizCode);
                            }
                        }
                    }

                    // If the teacher enters the fifth option (See quiz results), print the mark of every student. 
                    else if (navResult == 5) {

                        // Asks teacher to select a quiz to see marks for.  
                        String quizId = selectFromQuizList("Select a Quiz to see marks for ", loggedInTeacher);

                        // If there's a quiz ID, Iterate all the marks and dislpay the marks from the Map. 
                        if (quizId != null) {
                            
                            // Iterate all the marks and display the marks from the Map.
                            Map<String, Object> results = TeacherService.getAllMarks(quizId);
                            
                            // For every entry in the map of results, print the student's score. 
                            for (Map.Entry<String, Object> entry : results.entrySet()) {
                                
                                // If the student's score is -1, that means that their quiz is incomplete. 
                                if (String.valueOf(entry.getValue()).equals("-1"))
                                {
                                    // Prints that the student's quiz is incompelte. 
                                    System.out.println(entry.getKey() + " : Incomplete");

                                // Student has competed their quiz. 
                                } else 
                                {
                                    // Prints the student's quiz score. 
                                    System.out.println(entry.getKey() + " : " + entry.getValue());
                                }
                            }
                        }
                    }

                    // If the teacher enters the sixth option (Export quiz results), export the results of the quiz. 
                    else if (navResult == 6) {

                        // Asks teacher to select a quiz to export.  
                        String quizId = selectFromQuizList("Select a Quiz to export ", loggedInTeacher);

                        // If there's a quiz ID, export the marks. 
                        if (quizId != null) {

                            // Asks the teacher for where they want to export their marks. 
                            String filePath = getFile("Enter filename of Marks CSV: ", "marks.csv", true);

                            // Exports all marks (as a CSV) to the specified file path. 
                            TeacherService.exportAllMarks(quizId, filePath);
                        }
                    }

                    // If the teacher enters the seventh option (Logout), log out the teacher and return to the log in page. 
                    else if (navResult == 7) {

                        // Sets the Teacher object to null. 
                        loggedInTeacher = null;

                        // Prints that the teacher is logged out. 
                        System.out.println("This teacher is logged out");

                        // Break to return to the log in page. 
                        break;
                    }

                    // The user enterd an invalid menu input. 
                    else {

                        // Prints that the menu input is invalid. 
                        System.out.println ("Invalid menu input");
                    }
                }
            }

            // If they enter 2 (register teacher), then register the teacher. 
            else if (loginResult == 2) {
                
                // Registers a teacher. 
                registerTeacher();
            }

            // If they enter 3 (Quit), then exit the program. 
            else if (loginResult == 3) {

                // Thanks the teacher for using quizkit
                System.out.println ("Thank you for using quizKit!");

                // Exits the while loop. 
                break;
            }

            // If they don't enter an option on the menu, then their input is invalid.
            else {

                // Prints that the menu input is invalid. 
                System.out.println ("Invalid menu input");
            }

        // While the teacher isn't logged in, the loop repeats. 
        } while (loggedInTeacher == null);

        // If the scanner isn't null, then close it. 
        if (scanner != null)

            // Closes scanner. 
            scanner.close();
    }

    /**
     * Prints the list of quizzes in a teacher's account. 
     * 
     * @param message Title of the list.
     * @param teacher Object of teacher with quizzes in their account. 
     * @return ArrayList of quiz IDs. Null if a teacher did not add any quizzes to their account. 
     */
    public static ArrayList<String> printQuizList (String message, Teacher teacher) {

        // Prints the title of the list. 
        System.out.println("\n" + message);

        // Check if there are quizzes loaded. Prints a message if no quizzes have been loaded. 
        if (teacher.quizzes == null || teacher.quizzes.size() == 0) {

            // Prints that no quizzes have been added. 
            System.out.println("There are no quizzes added");

            // Returns null. 
            return null;
        }

        // Adds every quiz ID in a teacher's account to an ArrayList. 
        ArrayList<String> quizIdList = new ArrayList<String>(teacher.quizzes.keySet());

        // Iterate the quizzes by quizId. Display a list of quizzes and quiz names.
        for (int i = 0; i < quizIdList.size(); i++) {

            // Prints a list of quizzes. 
            System.out.println((i + 1) + ". " + teacher.quizzes.get(quizIdList.get(i)));
        }

        // return the list of quiz Ids.
        return quizIdList;
    }

    /**
     * Allows teachers to select a quiz that they want to start. 
     * 
     * @param message Title of the teacher's quiz list.
     * @param teacher Object of teacher with quizzes in their account. 
     * @return ID of the quiz that the teacher wants to get. 
     */
    public static String selectFromQuizList (String message, Teacher teacher) {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // get an ArrayList of quizIds and display a list of those quizes. 
        ArrayList<String> quizIdList = printQuizList(message, teacher);

        // Ensure there are at least one quiz.
        if (quizIdList == null)

            // If there isn't a quiz, return null. 
            return null;

        // If there are quizzes, add a cancel option at the end of the list. 
        else
            System.out.println("0. Cancel");

        // Infinite loop. Allow the user to select a quiz number from the printed list. Exit loop once a valid menu item is entered.
        for (;;) {

            // Asks user to select a quiz number.
            System.out.print("Select a Quiz number: ");

            // get the quiz number entered from the menu. Ensure the quiz number is within the range of the quizzes displayed.
            if (scanner.hasNextInt()) {

                // Gets the user's input and saves it to the variable input. 
                int input = scanner.nextInt();

                // If input is within the range of the quizzes displayed, then return it. 
                if(input >= 1 && input <= quizIdList.size())
                    
                    // valid menu option is entered.
                    return quizIdList.get(input-1);
                
                // else, if the input is 0 (cancel) then return null. 
                else if (input == 0)

                    // Cancel menu option is selected. Return null.
                    return null;
            }
        }
    }

    /**
     * Prints a menu and returns the input.  
     * 
     * @param menuTitle Title of the menu. 
     * @param menu Array of the items in the menu. 
     * @return The user's menu option input. 0 if the user has opted to cancel. 
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public static int printMenu(String menuTitle, String[] menu) throws IOException {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // print out all the menu items, and a menu item number.
        System.out.println ("\n----- " + menuTitle + " -----");
        
        // Print every menu item in the menu array. 
        for (int i = 0; i < menu.length; i++ ) {

            // Prints a line of the menu. 
            System.out.println((i+1) + ". " + menu[i]);
        }

        // Asks the user to enter a command. 
        System.out.print("Enter Command: ");

        // If there user enters a command, validate it and return it. 
        if (scanner.hasNextInt()) {

            // Takes the input of the user.
            int input = scanner.nextInt();

            // If the input is valid, then return the input. 
            if(input >= 1 && input <= menu.length)

                // valid menu option is entered.
                return input;
        }

        // user has opted to cancel.
        return 0;
    }

    /**
     * Registers a teacher in Firebase. 
     * 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void registerTeacher () throws Exception {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // Asks the teacher to enteer their name. 
        System.out.print("\nEnter the teacher's name: ");

        // Takes the teacher's name input. 
        String teacherName = scanner.nextLine();

        // Creates a new teacher email variable. 
        String teacherEmail;

        // Infinite loop to enter the teacher email, then verify if it already exits in Firebase. If it does, 
        // then loop. 
        for (;;) {

            // Asks the teacher to enter their email. 
            System.out.print("Enter the teacher's login email: ");

            // Takes the teacher's input. 
            teacherEmail = scanner.nextLine();

            // Check if teacher email exists. If it does not, then break. 
            if (!TeacherService.existsTeacher(teacherEmail))
                
                // If a teacher's email doesn't exist in firebase, break loop.  
                break;

            // Tells the teacher that their email already exists and to try again. 
            System.out.println("\nThat email already exists. Try again.");
        }

        // Asks the teacher to enter their password. 
        System.out.print("Enter the teacher's password: ");

        // Takes the input of their password. 
        String teacherPassword = scanner.nextLine();

        // Adds their information to the registerTeacher function in the TeacherService API. 
        TeacherService.registerTeacher(teacherName, teacherEmail, teacherPassword);

        // Prints that the teacher has successfully been registered. 
        System.out.println("\nTeacher successfully registered. Please log in.");
    }

    /**
     * Logs a teacher into Firebase. Returns the teacher object. 
     * 
     * @return Teacher object of the authenticated teacher. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static Teacher loginTeacher () throws Exception {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // Asks the teacher to enter their email. 
        System.out.print("\nTeacher's Login: ");

        // Gets their response. 
        String teacherEmail = scanner.nextLine();

        // Asks the teacher to enter their password. 
        System.out.print("Teacher's password: ");

        // Gets their response.
        String teacherPassword = scanner.nextLine();

        // Authenticates the teacher and assigns the Teacher object to loggedInTeacher. 
        Teacher loggedInTeacher = TeacherService.authenticateTeacher(teacherEmail, teacherPassword);

        // If the teacher's email and password aren't valid, then a message prints that the authentication failed. 
        if (loggedInTeacher == null)

            // Prints that authentication has failed. 
            System.out.println("\nAuthentication Failed");

        // Authentication has been successful
        else

            // Prints that authentication has been successful for the teacher's name. 
            System.out.println("\nAuthentication successful for " + loggedInTeacher.teacherName);

        // Return the teacher object. 
        return loggedInTeacher;
    }

    /**
     * Gets a filepath to read and gets a filepath to save a file (and asks users if they want to overwrite file if it 
     * already exists).
     * 
     * @param message Instructions for what the user needs to enter. 
     * @param defaultFile The default file that the user can use. 
     * @param forSaving True if the user is saving a file. False if the user is getting a file. 
     * @return Filepath of the file. 
     */
    public static String getFile (String message, String defaultFile, boolean forSaving) {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);
        
        // Blank filepath variable. 
        String filePath;

        // Infinite loop that asks users to enter the file path. 
        for (;;) {
            
            // prints instructions and a default file. 
            System.out.print (message + "[" + defaultFile + "]: ");

            // Gets the file path. 
            filePath = scanner.nextLine();

            // If the user clicked enter for the default file, set the file path to the default file. 
            if (filePath.equals("")) {

                // Sets the file path to the default file. 
                filePath = defaultFile;
            }

            // check if the file exists
            File file = new File(filePath);

            // If the file exists and it's for saving, then it alerts users. 
            if (file.exists() && forSaving) {

                // Tells users that the file already exists, and asks if they want to overwrite. 
                System.out.print (filePath + " exists already. Overwrite? (y/n) ");

                // Gets the input. 
                String overwrite = String.valueOf(scanner.nextLine().charAt(0));

                // If they want to overwrite, then return the file path. 
                if (overwrite.equalsIgnoreCase("y"))

                    // Returns file path. 
                    return filePath;
            }

            // Else, if the file exists (for reading), return the file path. 
            else if (file.exists())

                // Returns the file path. 
                return filePath;

            // If the file path that the user entered does not exist, then print that the file doesn't exist and loop again. 
            System.out.println(filePath + " does not exist. Try another filename.");
        }
    }
}