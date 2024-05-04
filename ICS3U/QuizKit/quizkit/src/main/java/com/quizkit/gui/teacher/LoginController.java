package com.quizkit.gui.teacher;

import java.net.URL;
import java.util.ResourceBundle;

import com.quizkit.api.model.Teacher;
import com.quizkit.api.services.TeacherService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Date: June 4 2021
 * Teacher: Mr. Ho
 * Description: Controller for login.fxml
 * 
 * @author Vincent Tran
 */
public class LoginController implements Initializable{

    static String teacherEmail; // String containing teacher's email
    static String teacherPass;  // String containing teacher's password

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passField;
    @FXML
    private Label error;

    /**
     * Validates teacher's login by contacting FireBase. Uses @i1rahman's Teacher object.
     * If valid, goes to homepage.fxml
     * 
     * @param event Handles onAction for login button
     * @throws Exception Throws exception if fxml not found or TeacherService breaks
     */
    @FXML
    private void loginAuth(ActionEvent event) throws Exception {
        event.consume();

        // Calls TeacherService.authenicateTeacher with email and password to validate credentials 
        teacherEmail = emailField.getText();
        teacherPass = passField.getText();
        Teacher loggedInTeacher = TeacherService.authenticateTeacher(emailField.getText(), passField.getText());   

        if (loggedInTeacher == null) {  // Checks if credentials returned null (invalid)
            error.setVisible(true);
        }
        else {  // Else, (credentials returned valid):
            // Loads FXML file into Parent root variable
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/homepage.fxml"));
            Parent root = loader.load();
            // Gets current stage
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            // Creates new scene from root
            Scene scene = new Scene(root);
            // Sets stage to scene and makes visible
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        }

    }

    /**
     * Goes to main.fxml if cancel button was pressed.
     * 
     * @param event Handles onAction for cancel button
     * @throws Exception Throws exception if fxml not found
     */
    @FXML
    private void goBack(ActionEvent event) throws Exception {
        event.consume();
        // Loads FXML file into Parent root variable
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/main.fxml"));
        Parent root = loader.load();
        // Gets current stage
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        // Creates new scene from root
        Scene scene = new Scene(root);
        // Sets stage to scene and makes visible
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

    /**
     * Executes on startup of fxml file, but does literally nothing.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    
}
