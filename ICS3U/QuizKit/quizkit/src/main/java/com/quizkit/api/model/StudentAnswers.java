package com.quizkit.api.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This model is used to store the student answerers of a single quiz.
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class StudentAnswers {

    /**
     * Holds a map of student answers. The key is the question Id. The value is the answer Id (A=1, B=2, ...) the student provides. 
     */
    public Map<String, Integer> answers = new HashMap<String, Integer>();

    /**
     * This is an empty contructor for this model. This is required to deserialized the model from the firebase client.
     */
    public StudentAnswers() {}
}