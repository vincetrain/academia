/*
 * Date: May 21, 2021
 * Author: Murphy Lee
 * Teacher: Mr. Ho
 * Description: Options menu page - Student can start test or view results
 * */
package com.quizkit.gui.student;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import com.quizkit.api.model.Quiz;
import com.quizkit.api.model.StudentQuiz;
import com.quizkit.api.services.QuizService;
import com.quizkit.api.services.StudentService;

/*
 * Description: Represents the Options Menu Controller of the FXML application, controls the options menu with events for certain actions
 * 
 * @author - Murphy Lee
 * */
public class OptionsMenuController {
    /*
     * Description: Contains the text used to welcome the user + their name
     * */
    @FXML
    private Label welcomeMessage;

    /*
     * Description: Empty label whose text can be sent if the user did something wrong
     * */
    @FXML
    private Label messageError;

    /*
     * Description: Label containing the users mark for the current quiz
     * */
    @FXML
    private Label userMark;
    
    /*
     * Description: Displays a welcome message according to the students name
     * 
     * @author - Murphy Lee
     * @param name - Name of the student
     * */
    public void greetStudent(String name) {
        // Fill the empty label with a welcome message to the student
        welcomeMessage.setText("Welcome " + name + "!");
    }

    /*
     * Description: Switches to the next scene - student takes the test
     * 
     * @author - Murphy Lee
     * @throws Exception - Thrown when accessing FireBase services
     * @param e - ActionEvent triggered when user clicks button
     * */
    public void goToTest(ActionEvent e) throws Exception {
        // Obtain the current user test
        StudentQuiz studentQuiz = LoginController.studentQuiz;
        double result = StudentService.getMarks(studentQuiz.quizId, studentQuiz.studentEmail);

        // If the users test is negative (incomplete), allow them to take the test
        if (result < 0) {
            // Store next Scene into a root node
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FillTest.fxml"));
            Parent root = loader.load();

            // Create new scene with root node and add it to the stage
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.show();
            
            // Call the alignText method to set display properties for the text (questions)
            FillTestController fillTestController = loader.getController();
            fillTestController.alignText();
        }
        else {
            messageError.setText("You have already completed this test");
        }
    }

    /*
     * Description: Switches to the next scene - student views their current marks
     * 
     * @author - Murphy Lee
     * @throws Exception - Thrown when accessing FireBase services
     * @param e - ActionEvent triggered when user clicks results button
     * */
    public void goToResults(ActionEvent e) throws Exception {
        // Get the studentQuiz from the controller and get results
        StudentQuiz studentQuiz = LoginController.studentQuiz;
        Quiz quiz = QuizService.getQuiz(studentQuiz.quizId);
        double result = StudentService.getMarks(studentQuiz.quizId, studentQuiz.studentEmail);
        
        // Move on to the results page if the student has a valid mark
        if (result >= 0) {
            String studentMark = String.valueOf(result * 100).concat("%");
            // Initialize the next FXML scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Results.fxml"));
            Parent root = loader.load();

            // Use an instance of the next scene controller to display the marks
            ResultsController resultsController = loader.getController();
            resultsController.displayResults(quiz.quizTitle, studentMark);

            // Switch to the next FXML scene
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            
            // Set the stage to the scene and show
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        }
        else {
            // Set the error message - user does not have current marks
            messageError.setText("You have not completed this test yet");
        }
    }

    /*
     * Description: Takes user back to the login page
     * 
     * @author - Murphy Lee
     * @throws IOException - Thrown when accessing Login.fxml
     * @param ActionEvent e - The event triggered when the user presses the logout button
     * */
    public void logout(ActionEvent e) throws IOException {
        // Store the login Scene into a root node
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Create new scene with root node and add it to the stage
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
