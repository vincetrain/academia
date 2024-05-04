package com.quizkit.gui.teacher;

import java.net.URL;
import java.util.ResourceBundle;

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
 * Description:
 * 
 * @author Vincent Tran
 */
public class RegisterController implements Initializable{

    // User input fields
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passField;
    @FXML
    private PasswordField repassField;

    // Error messages
    @FXML
    private Label errorLabel;

    /**
     * Validates entered password and email, then contacts firebase to store information
     * 
     * @param event Handles onAction for register button
     * @throws Exception Throwse exception if TeacherService breaks
     */
    @FXML
    private void register(ActionEvent event) throws Exception {
        event.consume();
        // Reading and storing of user input fields
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = passField.getText();

        // Runs checks, then registers information
        if (validateCredentials(name, email, pass)) {
            TeacherService.registerTeacher(name, email, pass);
            loadConfirmPage(event); // Loads the confirm page
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
     * Loads the confirm/registered page
     * 
     * @param event Used to get scene from event
     * @throws Exception Throws exception if fxml not found
     */
    private void loadConfirmPage(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/registered.fxml"));
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
     * Runs given credentials through a few checks, and returns boolean if valid.
     * 
     * @param name Handles inputted name
     * @param email Handles inputted email
     * @param pass Handles inputted passworsd
     * @return Returns boolean if checks pass
     * @throws Exception Throws exception if TeacherService breaks
     */
    private boolean validateCredentials(String name, String email, String pass) throws Exception{
        String[] emailDomain = email.split("@");  // Splits email domain with @ symbol
        String repass = repassField.getText();  // Reads password confirmation field and sets as String

        // Name Checks
        
        if (name.length() <1 || name == null) {
            error(0);
            return false;
        }

        // Email Checks

        if (emailDomain.length != 2) {  // Returns an error message + false if email is not formatted correctly
            error(1);
            return false;
        }
        else if (TeacherService.existsTeacher(email)) { // Calls @1rahman's TeacherService.existsTeacher() to check if email already exists in db
            error(2);
            return false;
        }

        // Password Checks

        if (!(pass.length() < 16 && pass.length() > 7 && stringAccepted(pass))) { // Returns an error message + false if password is not within length boundaries and follows acceptedChars
            error(3);
            return false;
        }
        else if (!pass.equals(repass)) {    // Returns an error message + false if password does not equal repass
            error(4);
            return false;
        }

        return true;    // Returns true with no error message if all checks pass
    }

    /**
     * Compares given string to acceptedChars and returns boolean if matches acceptedChars
     * 
     * @param string Handles inputted string that user wishes to check
     * @return Returns boolean if string is accepted or not
     */
    private boolean stringAccepted(String string) {
        String acceptedChars = "abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*";  // String containing all accepted characters
        // Iterates through the given string and compares index to acceptedChars
        for (int i = 0; i < string.length(); i++) {
            if (!acceptedChars.contains(String.valueOf(string.toLowerCase().charAt(i)))) {  // uses String.contains() to determine if password of character i is accepted
                return false;
            }
        }
        return true;
    }

    /**
     * Displays error message.
     * 
     * @param i Defines what prompt to use
     */
    private void error(int i) {
        // String array storing all error prompts
        String[] errorPrompts = {"Invalid name: You did not enter a name",
        "Invalid email: The entered email is invalid",
        "Invalid email: This email has already been used",
        "Invalid password: Must be inbetween 8-15 characters and can only contain A-Z, 0-9 or !@#$%^&*",
        "Invalid password: Confirm password does not match password"};

        errorLabel.setText(errorPrompts[i]);    // Sets errorLabel text to prompt
    }

    /**
     * Executes when fxml starts. Does literally nothing.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

}
