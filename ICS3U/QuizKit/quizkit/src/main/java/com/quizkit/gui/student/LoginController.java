/*
 * Date: May 21, 2021
 * Author: Murphy Lee
 * Teacher: Mr. Ho
 * Description: Student login page
 * */

package com.quizkit.gui.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Node;

import com.quizkit.api.model.StudentQuiz;

import com.quizkit.api.services.StudentService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * Description: Represents the Login Controller of the FXML application, controls the login page
 * 
 * @author - Murphy Lee
 * */
public class LoginController {
    /*
     * Description: The user-inputted meeting code from login page
     * */
    @FXML
    private TextField meetingCode;

    /*
     * Description: The user-inputted email from login page
     * */
    @FXML
    private TextField email; 

    /*
     * Description: Empty label that we can use to display error message
     * */
    @FXML
    private Label errorMessage;    

    /*
     * Description: Stores student-quiz data for other scenes to use
     * */
    public static StudentQuiz studentQuiz;  
    
    /*
     * Description: Checks if user-inputted email & meeting code match with the FireBase data
     * 
     * @author - Murphy Lee
     * @param e - The ActionEvent triggered when the user hits the submit button
     * @throws Exception - Raised when using FireBase to check student data
     * */
    public void loginValidation(ActionEvent e) throws Exception {
        errorMessage.setText("");   // So a previous error message won't show on the next try
        // Use the user-inputted email and meeting code input to return a quiz
        try {
            studentQuiz = StudentService.getStudentQuiz(meetingCode.getText(), email.getText());
        }
        // Thrown when user doesn't enter any data
        catch (IllegalArgumentException exception) {
            errorMessage.setText("Invalid student login");
        }
        // Check if a quiz was returned, and send error message if not
        if (studentQuiz == null) {
            // Fill the empty label with error message
            errorMessage.setText("Invalid student login");
        }
        else {
            String name = studentQuiz.studentName;
            
            // Store next FXML file into a root node 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OptionsMenu.fxml"));
            Parent root = loader.load();

            // Create instance of the next scene controller - Display name in new scene
            OptionsMenuController optionsMenuController = loader.getController();
            optionsMenuController.greetStudent(name);

            // Access current stage and make new scene
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            
            // Set the new scene in the stage
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        }
    }
}
