package com.quizkit.api.model;

import java.util.ArrayList;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This model is used to store the quiz and an array of all the questions of that quiz.
 *   
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class Quiz {
    
    /**
     * Holds the quiz title.
     */
    public String quizTitle;

    /**
     * Array list of all the Question objects. 
     */
    public ArrayList<Question> questionList = new ArrayList<Question>();

    /**
     * This is an empty contructor for this model. This is required to deserialized the model from the firebase client.
     */
    public Quiz () {}

    /** 
     * Initialize the quiz object with the quiz title.
     * @param quizTitle Holds the quiz title that is used to initialize this object.
     */
    public Quiz (String quizTitle) {
        this.quizTitle = quizTitle;
    }
}