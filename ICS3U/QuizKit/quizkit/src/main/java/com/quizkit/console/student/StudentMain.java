package com.quizkit.console.student;

import java.io.IOException;
import java.util.Scanner;

import com.quizkit.api.model.Question;
import com.quizkit.api.model.Quiz;
import com.quizkit.api.model.StudentAnswers;
import com.quizkit.api.model.StudentQuiz;
import com.quizkit.api.services.AnswerService;
import com.quizkit.api.services.QuizService;
import com.quizkit.api.services.StudentService;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This is a console version of the Student quiz. It is used to test the API, and then to assist 
 * the team with developing the GUI version.
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class StudentMain {

    /**
     * Student main. Shows a menu and allows the user to take a quiz and see their mark.
     * 
     * @param args Command line arguments.
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void main(String[] args) throws Exception {

        // Sets the StudentQuiz object to null. 
        StudentQuiz studentQuiz = null;

        // Logs in the student using the loginStudent method. Keeps repeating until studentQuiz isn't null. 
        do {
            // When a student's credential is validated, it returns the student object. 
            studentQuiz = loginStudent();
        
        // Repeats until the StudentQuiz object isn't null. 
        } while (studentQuiz == null);

        // Navigation menu held in an array. 
        String[] navMenu = {"Start the quiz", "Get Marks", "Quit"};

        // Infinite Loop. Exit the loop when the student quits.
        for (;;) {

            // Prints the menu of what the student can do. 
            int navResult = printMenu("quizKit Student Main Menu", navMenu);

            // If the student selects 1 (Start the quiz), then it starts the quiz, uploads the answers to firebase, retrieves the results, 
            // and prints them. 
            if (navResult == 1) {

                // Runs the quiz, allows students to input answers and sends the answers to Firebase. 
                runQuiz (studentQuiz);

                // Gets the student's mark from Firebase. Stores as a double. 
                double result = StudentService.getMarks(studentQuiz.quizId, studentQuiz.studentEmail);

                // Prints the student's results. 
                System.out.println(studentQuiz.studentName + "'s grade is " + result);
            }

            // If the student selects 2 (Get marks), the student either gets a message that they haven't completed the quiz yet or they 
            // get their grade. 
            else if (navResult == 2) {

                // Gets the student's mark from Firebase. Stores as a double. 
                double result = StudentService.getMarks(studentQuiz.quizId, studentQuiz.studentEmail);
                
                // If getMarks returns -1, that means that the student has not completed the quiz. 
                if (result == -1)

                    // Prints that the student has not completed the quiz. 
                    System.out.println(studentQuiz.studentName + "'s has not completed the quiz");
                
                // Student has completed the quiz. 
                else 

                    // Prints the student's grade. 
                    System.out.println(studentQuiz.studentName + "'s grade is " + result);
            }

            // If the student selects 3 (Quit) then it sets the StudentQuiz object as null (to log out the student) and 
            // prints a message that the student is logged out. Then, it breaks the program. 
            else if (navResult == 3) {

                // StudentQuiz object is set to null. 
                studentQuiz = null;

                // Prints a message that the student is logged out. 
                System.out.println("This student is logged out");
                
                // Breaks to stop the program. 
                break;
            }

            // An invalid menu input was detected. 
            else {

                // Prints that there is an invalid menu input. 
                System.out.println ("Invalid menu input");
            }
        }

    }


    /**
     * Runs the quiz from start to finish.
     * 
     * @param studentQuiz 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void runQuiz(StudentQuiz studentQuiz) throws Exception {

            // Gets the quiz document from Firebase and assigns a Quiz object to quiz.
            Quiz quiz = QuizService.getQuiz(studentQuiz.quizId);

            // Creates a blank StudentAnswers object. 
            StudentAnswers studentAnswers = new StudentAnswers();

            // Sets the question number to 0. Used later to increment. 
            int questionNumber = 0;

            // For every question in the list of questions, ask the question and put the answers in the StudentAnswers object. 
            for (Question question : quiz.questionList) {

                // Increment the question number.
                questionNumber++;

                // Ask a question to the student. 
                int answer = askQuestion(questionNumber, question);

                // Takes the student asnwers and puts it in the StudentAnswers object. 
                studentAnswers.answers.put(String.valueOf(questionNumber-1), answer);
            }

            // Saves the answers to Firebase using the quiz ID, student email, and the student's answers. 
            AnswerService.saveAnswers(studentQuiz.quizId, studentQuiz.studentEmail, studentAnswers);
    }


    /**
     * Log in a student to quizKit using Firebase. 
     * 
     * @return StudentQuiz object. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static StudentQuiz loginStudent () throws Exception {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // Asks for student's email. 
        System.out.print("\nStudent's Email: ");

        // Saves student email. 
        String studentEmail = scanner.nextLine();

        // Asks student for their quiz code. 
        System.out.print("Student's Quiz Code: ");

        // Saves student quiz code. 
        String studentQuizCode = scanner.nextLine();

        // Gets the student's quiz from firebase and deserializes it into a StudentQuiz object. 
        StudentQuiz studentQuiz = StudentService.getStudentQuiz(studentQuizCode, studentEmail);

        // If the StudentQuiz object is null, than the email or quiz code is invalid. 
        if (studentQuiz == null)

            // Prints that the email or quiz code is invalid. 
            System.out.println("\nInvalid Email or Quiz Code");
        
        // Prints welcome message when email and quiz code is validated. 
        else

            // Prints welcome to quizkit message with the student's name. 
            System.out.println("\nWelcome to quizKit " + studentQuiz.studentName);

        // Returns the StudentQuiz object. 
        return studentQuiz;
    }


    /**
     * Asks a question to the student and gets a response. Then returns the response to the caller.  
     * 
     * @param number Question number that the console will display. 
     * @param question Question that the student will be asked, passed in as a Question object. 
     * @return Returns an index of the student's answer. 
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public static int askQuestion(int number, Question question) throws IOException {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // Index of characters used as an index. 
        String charIndex = "ABCD";

        // Starts the student's answer index at -1 since that's the default for an incomplete question. 
        int answerIndex = -1;

        // Do while loop to ask students a question and get the answer. Loops until the question gets answered. 
        do {

            // Starts a new line and prints the question number passed in, plus the question text. 
            System.out.println ("\n" + number + ". " + question.questionText);

            // Loops through the possible answers. 
            for (int i = 0; i < question.choices.size(); i++ ) {

                // Tabs in a line, then puts the option's letter, plus the text of the choice. 
                System.out.println("\t(" + charIndex.charAt(i) + ") " + question.choices.get(i));
            }

            // Asks the user to enter their answer. 
            System.out.print("\tEnter Answer: ");

            // If they enter their answer, find the index of their answer. 
            if (scanner.hasNextLine()) {

                // Converts the first letter to an uppercase char. 
                char input = scanner.nextLine().toUpperCase().charAt(0);

                // Gets the index of the answer. 
                answerIndex = charIndex.indexOf(String.valueOf(input));
            }

        // While the answer index is under 0 (unanswered question), ask the question again. 
        } while (answerIndex < 0);

        // Return the index of the student's answer. 
        return answerIndex;

    }

    /**
     * Prints a menu. 
     * 
     * @param menuTitle Menu header.
     * @param menu Items in the menu. 
     * @return Student's input or a return of 0 if the student did not input anything. 
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public static int printMenu(String menuTitle, String[] menu) throws IOException {
        
        // Constructs a new Scanner that produces values scanned from the specified input stream.
        Scanner scanner = new Scanner(System.in);

        // Prints the menu header using the menu title passed in to the function. 
        System.out.println ("\n----- " + menuTitle + " -----");

        // Prints every item in the menu with a number in front of it. 
        for (int i = 0; i < menu.length; i++ ) {

            // Printing a line of the menu. 
            System.out.println((i+1) + ". " + menu[i]);
        }

        // Asks the user to enter their command. 
        System.out.print("Enter Command: ");

        // If the user enters a command and if the input is between 1 and the length of the menu, then
        // return their input. 
        if (scanner.hasNextInt()) {

            // Assigns the input to the variable input. 
            int input = scanner.nextInt();

            // If the input is 1 or greater and under the length of the menu, then return the input. 
            if(input >= 1 && input <= menu.length)

                // Returns the user's input.
                return input;
        }

        // Returns 0 when the user does not enter a valid input. 
        return 0;
    }
}