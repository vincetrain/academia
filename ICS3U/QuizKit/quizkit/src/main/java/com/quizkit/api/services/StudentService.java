package com.quizkit.api.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import com.quizkit.api.model.*;
import com.quizkit.api.firebase.*;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: The Student service allows students to retreive their quizzes, and allows teachers to create students (objects) from a CSV, add students to a quiz, 
 * get Student (objects) from a quiz, and get the marks of a student. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class StudentService {

    /**
     * Gets the student's quiz from Firebase. 
     * 
     * @param studentCode Unique quiz code emailed to students. 
     * @param studentEmail Student's email address.
     * @return A StudentQuiz object of the student's quiz.
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static StudentQuiz getStudentQuiz (String studentCode, String studentEmail) throws Exception {
        
        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Sets the correct path (document) of the quiz that the student is able to compelte.  
        DocumentReference docRef = db.collection("students").document(studentCode);
        
        // Gets the Student quiz that's been saved. Kicks off a thread to hit API.  
        ApiFuture<DocumentSnapshot> future = docRef.get();

        // Waiting for the thread to complete, and assigning the value to document.
        DocumentSnapshot document = future.get();

        // If the document exists, then turn the document into a StudentQuiz object, authenticate the email, and return
        // the StudentQuiz object. 
        if (document.exists()) {
            
            // Turns the document into a StudentQuiz object. 
            StudentQuiz studentQuiz = document.toObject(StudentQuiz.class);

            // Authenticates the student's email.
            if (studentQuiz.studentEmail.equalsIgnoreCase(studentEmail)) { // authenticated
                
                // Return the StudentQuiz object once the email is authenticated. 
                return studentQuiz;
            }
        }

        // return null if the StudentQuiz doesn't exist or if the student email is invalid. 
        return null;
    }

    /**
     * Creates student objects from a CSV containing their name and email address. 
     * 
     * @param filePath The Student email/name CSV file path. 
     * @return ArrayList of StudentQuiz objects. 
     * @throws FileNotFoundException Signals that an attempt to open the file denoted by a specified pathname has failed.
     */
    public static ArrayList<StudentQuiz> createStudentsFromCSV (String filePath) throws FileNotFoundException {
        
        // New ArrayList that's the length of the number of StudentQuizzes. 
        ArrayList<StudentQuiz> students = new ArrayList<StudentQuiz> ();

        // Opens a new file reader and scanner to read the file. 
        Scanner scnr = new Scanner(new FileReader(filePath));

        // check if the file has at least one line. Assume it's the header. Read it, and throw it away.
        if (scnr.hasNextLine())
            scnr.nextLine();

        // While there's a line, split the line where the commas are and put them in an array of strings. Then, 
        // create a new StudentQuiz object and place the name and email address in their apropriate place. Add the 
        // StudentQuiz object to the students ArrayList. 
        while(scnr.hasNextLine()) {

            // Gets the current line of the file and assigns it to studentLine.  
            String studentLine = scnr.nextLine();

            // Splits the line where there is a comma and puts each item in an array.
            String[] items = studentLine.split(",[ ]*");

            // Takes the 0th and 1st index of the array and assigns it to the object. The information that is missing is
            // assigned null. 
            StudentQuiz student = new StudentQuiz(null, items[0], items[1], null);
            
            // Adds StudentQuiz objects to the students ArrayList. 
            students.add(student);
        }

        // Returns an ArrayList of StudentQuiz objects. 
        return students;
    }

    /**
     * Adds students to a quiz on Firebase. 
     * 
     * @param quizId The quiz ID of the quiz that the user wants to add students to. 
     * @param students ArrayList of StudentQuiz objects. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void addStudentsToQuiz (String quizId, ArrayList<StudentQuiz> students) throws Exception  {
        
        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // For every student, delete the dashes in their random UUID (unique quiz code), then place their code in their
        // document in Firebase. That code will be email to them in another method. 
        for (StudentQuiz student : students) {

            // Deletes dashes in the student's UUID (unique quiz code), and converts it to a string. 
            String studentQuizCode = UUID.randomUUID().toString().replace("-","").substring(0,8);

            // Sets the correct path (document) of the quiz that the student is able to compelte. 
            DocumentReference docRef = db.collection("students").document(studentQuizCode);

            // Creates a new StudentQuiz object using the attributes of the previous StudentQuiz object, and adds in 
            // a quiz code and a quiz ID. 
            StudentQuiz sq = new StudentQuiz(studentQuizCode, student.studentName, student.studentEmail, quizId);
            
            // Overwrites the previous StudentQuiz object with the new one. 
            docRef.set(sq).get();

            // Saves the student's email to the Quiz, and creates a blank StudentAnswers object. 
            AnswerService.saveAnswers(quizId, student.studentEmail, new StudentAnswers());
        }
    }

    /**
     * Gets StudentQuiz objects from a quiz. 
     * 
     * @param quizId ID of the quiz.
     * @return ArrayList of the StudentQuiz objects. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static ArrayList<StudentQuiz> getStudentsFromQuiz (String quizId) throws Exception  {
        
        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Searches the "students" collection to find the students who are assigned to the quiz ID.
        Query query = db.collection("students").whereEqualTo("quizId", quizId);

        // Querys the database and kicks off a thread to hit API. 
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        // Creates a new ArrayList of StudentQuiz objects. 
        ArrayList<StudentQuiz> students = new ArrayList<StudentQuiz>();

        // For the number of students, turn each document into a StudentQuiz object, then add it to the
        // ArrayList of StudentQuiz objects. 
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            
            // Turns the student document into a StudentQuiz object. 
            StudentQuiz studentQuiz = document.toObject(StudentQuiz.class);

            // adds the StudentQuiz objects into the ArrayList. 
            students.add(studentQuiz);
        }

        // Returns the ArrayList of the StudentQuiz object. 
        return students;
    }

    /**
     * Gets the mark of a specific student for a specific quiz. 
     * 
     * @param quizId The quiz that you would like to find the student's mark for. 
     * @param studentEmail The student's email address. 
     * @return A double of the student's score as a percentage. -1 is returned if the student did not complete the quiz. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static double getMarks(String quizId, String studentEmail) throws Exception {

        // An integer of the number of correct answers. 
        int correctAnswers = 0;

        // An integer of the number of total questions. 
        int totalQuestions = 0;

        // Gets the quiz document from Firebase and saves the Quiz object.
        Quiz quiz = QuizService.getQuiz(quizId);

        // Gets the student's answers for the quiz and puts it in a StudentAnswers object. 
        StudentAnswers studentAnswers = AnswerService.getAnswers(quizId, studentEmail);

        // If the Student didn't answer any of the questions (they have 0 answers), then return -1. 
        if (studentAnswers.answers.size() == 0) {
            
            // Returns -1 to let students know that the quiz isn't completed. 
            return -1;
        }

        // Loops through every student's answer to determine their grade. 
        for (Map.Entry<String,Integer> entry : studentAnswers.answers.entrySet()) {   // map is questionId, answerId (A=1, B=2, ...)
            
            // Adds 1 to the total of questions. 
            totalQuestions++;
            
            // Look up the question in the array by the question Id.
            Question question = quiz.questionList.get(Integer.valueOf(entry.getKey()));
            
            // Check if student answer is correct.
            if (question.answer1Index == entry.getValue() || question.answer2Index == entry.getValue()) {
                
                // Adds 1 to the number of correct answers. 
                correctAnswers++;
            }
        }

        // Returns a double of the student's score as a percentage. 
        return (double) correctAnswers / (double) totalQuestions;
    }
}