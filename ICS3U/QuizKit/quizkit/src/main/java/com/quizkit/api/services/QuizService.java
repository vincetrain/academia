package com.quizkit.api.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.UUID;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import com.quizkit.api.model.*;
import com.quizkit.api.firebase.*;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This Quiz service allows teachers to create a quiz, add a quiz to Firebase, delete a quiz from Firebase, and get
 * a quiz from Firebase. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class QuizService {

    /**
     * Gets the quiz document from Firebase deserializes it into a Quiz object. 
     * 
     * @param quizId Identification string of the quiz.
     * @return A Quiz object containing the quiz. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static Quiz getQuiz(String quizId) throws Exception {
        
        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Sets the correct path (document) that the quizzes need to be retrived from. 
        DocumentReference docRef = db.collection("quizzes").document(quizId);

        // Gets the quiz that's been saved. Kicks off a thread to hit API.  
        ApiFuture<DocumentSnapshot> future = docRef.get();

        // Waiting for the thread to complete, and assigning the value to document.
        DocumentSnapshot document = future.get();
       
        // If the document exists, then it turns the document into an object and returns it. 
        if (document.exists()) {

            // Turns the quiz document into a Quiz object. 
            Quiz quiz = document.toObject(Quiz.class);

            // Returns the Quiz object. 
            return quiz;
        }

        // Returns null when the document does not exist. 
        return null;
    }

    /**
     * Creates a Quiz object from an inputted CSV. 
     * 
     * @param quizName What the name of the quiz will be. 
     * @param filePath Path to the CSV file. 
     * @return an ID of the quiz that was created. 
     * @throws FileNotFoundException Signals that an attempt to open the file denoted by a specified pathname has failed.
     */
    public static Quiz createQuizFromCSV (String quizName, String filePath) throws FileNotFoundException {
        
        // Creates a new Quiz object with and adds the quizname to the object. 
        Quiz quiz = new Quiz(quizName);

        // Opens a new file reader and scanner to read the file. 
        Scanner scnr = new Scanner(new FileReader(filePath));

        // Reads the first line (header) and discards it. 
        if (scnr.hasNextLine())
            // Reads a line of the file. 
            scnr.nextLine();

        // While there's another line, get the line from the document and pass it into the createQuestion method to 
        // create a Question object, then add it to a question list in the Quiz object. 
        while(scnr.hasNextLine()) {
            // Assigns the current document line to the String questionLine
            String questionLine = scnr.nextLine();

            // The method createQuestion creates a new Question object. 
            Question question = Question.createQuestion(questionLine);

            // Adds the Question object to a question list within the Quiz object. 
            quiz.questionList.add(question);
        }

        // Returns a Quiz object. 
        return quiz;
    }

    /**
     * Adds a Quiz object to firebase. 
     * 
     * @param quiz A quiz object created from the createQuizFromCSV method. 
     * @return The quiz ID for the quiz that was added. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static String addQuiz(Quiz quiz) throws Exception {

        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Takes the UUID (A unique code generated, which is the quiz ID) and saves it to
        // the variable quizId. 
        String quizId = UUID.randomUUID().toString();

        // Sets the correct path (document) that the quiz needs to be saved to. 
        DocumentReference docRef = db.collection("quizzes").document(quizId);

        // Gets the quiz and saves it to the docRef path. 
        docRef.set(quiz).get();

        // Returns the quizID. 
        return quizId;
    }

    /**
     * Deletes a quiz from firebase.
     *  
     * @param quizId A string of the quiz ID of the quiz that's going to be deleted. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void deleteQuiz(String quizId) throws Exception {

        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Deletes the quiz. 
        db.collection("quizzes").document(quizId).delete().get();
    }
}