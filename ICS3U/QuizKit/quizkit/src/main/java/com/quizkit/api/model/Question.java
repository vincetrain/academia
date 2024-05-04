package com.quizkit.api.model;

import java.util.ArrayList;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This model is used to store questions that are asked to students. It's also used to parse each line of the 
 * question's csv within the constructor.  
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class Question {

    /**
     * Stores the question text that is asked to the student during the multiple choice quiz. 
     */
    public String questionText;

    /**
     * Stores the options that students can choose during a quiz (multiple choice options) in an array. 
     */
    public ArrayList <String> choices = new ArrayList <String>();

    /**
     * Stores the index of the first correct answer. 
     */
    public int answer1Index;

    /**
     * Teachers are able to enter two correct answers. If a teacher chooses to only enter one correct answer, 
     * then by default the second answer will not be linked to anything. 
     */
    public int answer2Index = -1;

    /**
     * This is an empty contructor for this model. This is required to deserialized the model from the firebase client.
     */
    private Question() {}

    /**
     * Takes a line from a .csv file containing a quiz, and returns a Question object. 
     * 
     * @param line This is a single line from the questions csv file. Used to initialize this object.
     * @return
     */
    public static Question createQuestion (String line) {
        
        // String used as an index. Since users enter a letter, it needs to get converted into an integer. 
        final String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // Creates a new Question object. 
        Question question = new Question();

        // Splits the line where there is a comma and puts each item in an array. 
        String[] items = line.split(",[ ]*");

        // Assigns the 0th index in the array to the question variable in the Question object. 
        question.questionText = items[0];

        // Adds the 1st index in the array to the choice array in the Question object. 
        question.choices.add(items[1]);

        // Adds the 2nd index in the array to the choice array in the Question object. 
        question.choices.add(items[2]);

        // Adds the 3rd index in the array to the choice array in the Question object. 
        question.choices.add(items[3]);

        // Adds the 4th index in the array to the choice array in the Question object. 
        question.choices.add(items[4]);

        // Assigns the first answer to the answer 1 index variable in the Question object. 
        question.answer1Index = alpha.indexOf(items[5].charAt(0));

        // Checks to see if there's a second answer in the line. 
        if (items.length == 7){

            // If there's a second answer, then add it to the answer 2 index variable in the Question object. 
            question.answer2Index = alpha.indexOf(items[6].charAt(0));
        }

        // Returns the Question object. 
        return question;
    }
}