package com.quizkit.gui.teacher;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Date: June 4 2021
 * Teacher: Mr. Ho
 * Description: Controller for main.fxml for Teacher GUI
 * 
 * @author Vincent Tran
 */
public class TeacherMain extends Application{
    public static void main(String args[]) {
        launch(args);
    }

    /**
     * Starts program by setting stage to a scene and making visible
     * 
     * @param stage Gets stage from javafx to override
     * @throws Exception Throws exception if fxml or jpg does not exist.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML/main.fxml"));
        ImageView icon = new ImageView(getClass().getResource("Images/QuizKitLogo.JPG").toExternalForm());
        
        Scene scene = new Scene(root);
        stage.getIcons().add(icon.getImage());
        stage.setTitle("QuizKit: Teacher");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Switches to login scene
     * 
     * @param event Handles onAction from login button
     * @throws Exception Throws exception if fxml not found
     */
    @FXML
    private void loginAccount(ActionEvent event) throws Exception {
        event.consume();
        // Loads FXML file into Parent root variable
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/login.fxml"));
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
     * Switches to register scene
     * 
     * @param event Handles onAction from register button
     * @throws Exception Throws exception if fxml not found
     */
    @FXML
    private void registerAccount(ActionEvent event) throws Exception {
        event.consume();
        // Loads FXML file into Parent root variable
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/register.fxml"));
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
