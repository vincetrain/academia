package com.quizkit.api.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;

import com.quizkit.api.model.*;
import com.quizkit.api.firebase.*;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: The answer service allows students to save their quizzes to firebase and teachers to get their students' answers. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class AnswerService {

    /**
     * Saves answers of a student's quiz to Firebase, in a quizzes document. 
     * 
     * @param quizId ID of the quiz that the student is writing. 
     * @param studentEmail Email of the student. 
     * @param studentAnswers Map of student's answers. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void saveAnswers(String quizId, String studentEmail, StudentAnswers studentAnswers) throws Exception {
        
        // Establishes a connection with the Firebase Database. 
        Firestore db = QuizBaseConnection.GetConnection();

        // Sets the correct path (document) that the student answers need to be saved to. 
        DocumentReference docRef = db.collection("quizzes").document(quizId).collection("answers").document(studentEmail);

        // Adds the student answer to the document. 
        docRef.set(studentAnswers).get();

    }

    /**
     * Takes the student answers from Firebase and returns them to the caller. 
     * 
     * @param quizId ID of the quiz that the students are writing. 
     * @param studentEmail Email of the student. 
     * @return Returns the student answers object. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static StudentAnswers getAnswers(String quizId, String studentEmail) throws Exception {
        
        // Establishes a connection with the Firebase Database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Sets the correct path (document) that the student answers need to be saved to. 
        DocumentReference docRef = db.collection("quizzes").document(quizId).collection("answers").document(studentEmail);
        
        // Gets the answers that the student entered. Kicks off a thread to hit API.  
        ApiFuture<DocumentSnapshot> future = docRef.get();

        // Waiting for the thread to complete, and assigning the value to document. 
        DocumentSnapshot document = future.get();
        
        // If the document exists in Firebase, put the information in a Student Answers object then return it. 
        if (document.exists()) {

            // Puts the information in the Firebase document into a StudentAnswers object. 
            StudentAnswers studentAnswers = document.toObject(StudentAnswers.class);
            
            // Returns the student answers. 
            return studentAnswers;
        }

        // returns null when the document does not exist. 
        return null;
    }
}