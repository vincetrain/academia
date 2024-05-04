package com.quizkit.api.model;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.google.common.hash.Hashing;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: This model is used to store the teacher information, including login credentials. Only a password hash retained.
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class Teacher {

    /**
     * Holds the teacher's name. 
     */
    public String teacherName;

    /**
     * Holds the teacher's email. 
     */
    public String teacherEmail;

    /**
     * Holds the teacher's password (as a hash). 
     */ 
    public String teacherPasswordHash;

    /**
     * Map of the quizzes that the teachers have under their account. 
     */
    public Map<String, String> quizzes = new HashMap<String, String> ();

    /**
     * This is an empty contructor for this model. This is required to deserialized the model from the firebase client. 
     */
    public Teacher() {}

    /**
     * Hashes the inputted password and compares it against the hashed password stored within Firebase.
     * 
     * @param password User inputted password.
     * @return True if the password hashes match and false if they do not. 
     */
    public boolean checkPassword (String password) {
        
        // Hashes inputted password, then compares it to the teacher password hash stored in firebase. Returns boolean. 
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString().equalsIgnoreCase(teacherPasswordHash);
    }

    /**
     * Creates a new teacher object. 
     * 
     * @param teacherName Name of the teacher.  
     * @param teacherEmail Email of the teacher. 
     * @param password Teacher's password. 
     */
    public Teacher(String teacherName, String teacherEmail, String password ) {
        
        // Adds the teacher's name to the Teacher object.
        this.teacherName = teacherName;

        // Adds the teacher's email to the Teacher object. 
        this.teacherEmail = teacherEmail;

        // Hashes the inputted password and adds it to the Teacher object. 
        this.teacherPasswordHash = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}