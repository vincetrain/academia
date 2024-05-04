/*
 * Date: May 21, 2021
 * Author: Murphy Lee
 * Teacher: Mr. Ho
 * Description: Displays student marks in their quizzes
 * */
package com.quizkit.gui.student;

import java.io.IOException;

import com.quizkit.api.model.StudentQuiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

/*
 * Description: Represents the Results Controller of the FXML application, controls the results page controls
 * 
 * @author - Murphy Lee
 * */
public class ResultsController {
    /*
     * Description: Label containing the name of the test that the student took
     * */
    @FXML
    private Label testName;    
    /*
     * Description: The mark that the student got
     * */
    @FXML
    private Label testMark; 

    /*
        * Description: Fills the labels with the student test names and marks
        * 
        * @author - Murphy Lee
        * @param name - The name of the test (given by teacher)
        * @param mark - The mark that the student got on the test
        * */
    public void displayResults(String name, String mark) {
        testName.setText(name);
        testMark.setText(mark);
    }

    /*
        * Description: Goes back to the options menu when the student is done looking at their mark
        * 
        * @author - Murphy Lee
        * @throws IOException - Thrown when trying to access the options menu scene
        * @param e - ActionEvent triggered when user selects button
        * */
    public void goBack(ActionEvent e) throws IOException {
        StudentQuiz studentQuiz = LoginController.studentQuiz;
        // Go back to options menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OptionsMenu.fxml"));
        Parent root = loader.load();

        // Create instance of the next scene controller - Display name in new scene
        OptionsMenuController optionsMenuController = loader.getController();
        optionsMenuController.greetStudent(studentQuiz.studentName);

        // Create new scene with root node and add it to the stage
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        
        // Go back to options menu 
        stage.setScene(scene);
        stage.setResizable(true);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
}
