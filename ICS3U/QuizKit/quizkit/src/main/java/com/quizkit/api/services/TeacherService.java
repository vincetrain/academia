package com.quizkit.api.services;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.quizkit.api.model.*;
import com.quizkit.api.firebase.*;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: The Teacher service can register a teacher, authenticate a teacher (to access their account), get a teacher object,
 * add a quiz to a teacher's account, get the marks of all students in a quiz, and export student marks.  
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class TeacherService {

    /**
     * Registers a teacher in the Firebase database. 
     * 
     * @param teacherName Name of the teacher.
     * @param teacherEmail Teacher's email address. 
     * @param password Teacher's password. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void registerTeacher (String teacherName, String teacherEmail, String password) throws Exception {

        // If a teacher's email already exists in Firebase, throw an exception. 
        if (existsTeacher(teacherEmail)) {

            // Throws a new exception that a teacher already exists.
            throw new Exception ("Teacher already exists");
        }

        // Creates a new Teacher object with the teacher's name, their email, and their password. 
        Teacher teacher = new Teacher(teacherName, teacherEmail, password);

        // Saves a teacher in the Firebase database. 
        saveTeacher(teacher);
    }

    /**
     * Authenicate teachers when they log in. 
     * 
     * @param teacherEmail Teacher's email address. 
     * @param password Teacher's hashed password. 
     * @return Teacher object. Returns null if the teacher is not authenticated. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static Teacher authenticateTeacher (String teacherEmail, String password) throws Exception {
        
        // Calls Firebase to get a Teacher object for a specific teacher based on their email. 
        Teacher teacher = getTeacher(teacherEmail);

        // If there's a teacher, check their hashed password against the hashed password stored in the Firebase. 
        if (teacher != null) {

            // If the teacher's hashed password is equal to the hashed password in Firebase, then return the Teacher object. 
            if (teacher.checkPassword(password)) {
                
                // Return the teacher object. 
                return teacher;
            }
        }

        // Return null if the teacher is not authenticated. 
        return null;
    }

    /**
     * Gets the teacher object. 
     * 
     * @param teacherEmail Teacher's email address. 
     * @return Teacher object. Returns null when the teacher does not exist. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static Teacher getTeacher (String teacherEmail) throws Exception {
        
        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Sets the correct path (document) of the teacher. 
        DocumentReference docRef = db.collection("teachers").document(teacherEmail);

        // Gets the Student quiz that's been saved. Kicks off a thread to hit API.  
        ApiFuture<DocumentSnapshot> future = docRef.get();

        // Waiting for the thread to complete, and assigning the value to document.
        DocumentSnapshot document = future.get();

        // If the teacher document exists, then get convert the document to a Teacher object and return it. 
        if (document.exists()) {
            
            // Converts the teacher document to a Teacher object. 
            Teacher teacher = document.toObject(Teacher.class); {
                
                // Returns the teacher object. 
                return teacher;
            }
        }

        // Returns null when the teacher does not exist. 
        return null;
    }

    /**
     * Adds a Quiz object to a teacher's account in Firebase. 
     * 
     * @param teacherEmail Teacher's Email Address. 
     * @param quiz Quiz object containing the quiz. 
     * @return The quiz ID of the quiz. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static String addTeacherQuiz (String teacherEmail, Quiz quiz) throws Exception {
        
        // Adds a quiz to firebase and gets the quiz ID returned. 
        String quizId = QuizService.addQuiz(quiz);

        // Gets the teacher object from firebase. 
        Teacher teacher = getTeacher(teacherEmail);

        // Puts the quiz ID and quiz title in the teacher's "account". 
        teacher.quizzes.put(quizId, quiz.quizTitle);

        // Pushes the changes to the teacher's account to firebase. 
        saveTeacher(teacher);

        // Returns the quiz ID of the quiz. 
        return quizId;
    }

    /**
     * Saves new changes made to a Teacher object. 
     * 
     * @param teacher
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void saveTeacher (Teacher teacher) throws Exception {
        
        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();
        
        // Sets the correct path (document) of the teacher. 
        DocumentReference docRef = db.collection("teachers").document(teacher.teacherEmail);
        
        // Overwrites the document referred to by docRef (overwrites the teacher document in Firebase).
        docRef.set(teacher).get();
    }

    /**
     * Uses the teacher email to determine if the teacher exists in Firebase. 
     * 
     * @param teacherEmail Teacher's Email address. 
     * @return Returns whether or not a teacher exits. Returns false if the teacher does not exist.
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static boolean existsTeacher (String teacherEmail) throws Exception {

        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Sets the correct path (document) of the teacher. 
        DocumentReference docRef = db.collection("teachers").document(teacherEmail);

        // Gets the Student quiz that's been saved. Kicks off a thread to hit API.  
        ApiFuture<DocumentSnapshot> future = docRef.get();

        // Waiting for the thread to complete, and assigning the value to document.
        DocumentSnapshot document = future.get();

        // Returns whether or not a teacher exits. Returns false if the teacher does not exist.
        return document.exists();
    }


    /**
     * Gets all student marks in a quiz. 
     * 
     * @param quizId ID of the quiz. 
     * @return Map of 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static Map<String, Object> getAllMarks (String quizId) throws Exception {

        // Creates a new hashmap containing the results. 
        Map<String, Object> results = new HashMap<String, Object>();

        // Number of correct answers. 
        int correctAnswers = 0;

        // Total number of questions. 
        int totalQuestions = 0;

        // Gets the Quiz object from Firebase using the getQuiz method. 
        Quiz quiz = QuizService.getQuiz(quizId);

        // Establishes a connection with the Firebase database.
        Firestore db = QuizBaseConnection.GetConnection();

        // Gets all the answers from the quiz using the quiz ID. Kicks off a thread to hit API. 
        ApiFuture<QuerySnapshot> future = db.collection("quizzes").document(quizId).collection("answers").get();

        // Waiting for thread to complete, adds all the documents to a list. 
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        // For every document (list of a student's answers) in the list of documents, deserialize the document into a StudentAnswers object, 
        // and determine the grades. 
        for (QueryDocumentSnapshot document : documents) {

            // Deserializes student answers into a StudentAnswer object. 
            StudentAnswers studentAnswers = document.toObject(StudentAnswers.class);

            // Sets the total questions to 0.
            totalQuestions = 0;

            // Sets the correct answers to 0.
            correctAnswers = 0;

            // If the student didn't answer any questions, assign a score of -1. 
            if (studentAnswers.answers.size() == 0) {

                // Put their score as -1. 
                results.put(document.getId(), -1);

                // Goes to the next document.
                continue;
            }

            // Goes through every student answer, increments the total questions, determines whether or not the student's answers are correct, 
            // and increments their correct answers if they are. 
            for (Map.Entry<String,Integer> entry : studentAnswers.answers.entrySet()) {   // map is questionId, answerId
                
                // Add 1 to the total number of questions.
                totalQuestions++;
                
                // look up the question in the array by the question Id.
                Question question = quiz.questionList.get(Integer.valueOf(entry.getKey()));
               
                // check if student answer is correct.
                if (question.answer1Index == entry.getValue() || question.answer2Index == entry.getValue()) {
                    
                    // Add 1 to the number of correct answers. 
                    correctAnswers++;
                }
            }

            // The mark is calculated by dividing the correct answers by the total number of questions and multiplying by 100.
            // The mark is a double. 
            double mark = (double) correctAnswers / (double) totalQuestions * 100;

            // Enter score into hashmap. 
            results.put(document.getId(), mark);
        }
        
        // Return the results Map with a student email and result. 
        return results;
    }

    /**
     * Exports the mark of all students. 
     * 
     * @param quizId ID of the quiz.
     * @param filePath Path of the file that the CSV will be saved to. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void exportAllMarks (String quizId, String filePath) throws Exception {
        
        // Gets the Quiz object based on quiz ID from firebase. 
        Quiz quiz = QuizService.getQuiz(quizId);

        // Gets all the marks from firebase for a specific quiz. 
        Map<String, Object> results = getAllMarks(quizId);

        // Opens the printwriter to the specified file path. 
        try (PrintWriter printWriter = new PrintWriter(filePath)) {

            // Iterate the map for all the entries. 
            for (Map.Entry<String, Object> entry : results.entrySet()) {

                // If a student did not complete the quiz, print that. Otherwise, print the score. 
                if (String.valueOf(entry.getValue()).equals("-1")) {

                    // Prints the quiz title, student email, and incomplete. 
                    printWriter.println(quiz.quizTitle + ", " + entry.getKey() + ", " + "Incomplete");
                
                // Student completed a quiz. 
                } else {

                    // Prints the quiz titile, student email, and the student's score as a percentage. 
                    printWriter.println(quiz.quizTitle + ", " + entry.getKey() + ", " + entry.getValue() + "%");
                }            
            }
        }
    }
}