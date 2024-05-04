/*
 * Date: May 21, 2021
 * Author: Murphy Lee
 * Teacher: Mr. Ho
 * Description: Student testing program that makes use of JavaFX scenes 
 * */

package com.quizkit.gui.student;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/*
 * Description: Represents the main class, runs the whole student program
 * 
 * @author - Murphy Lee
 * */
public class StudentMain extends Application {
    /*
     * Description: Main method that launches the application
     * 
     * @author - Murphy Lee
     * */
    public static void main(String[] args) {
        launch(args);
    }
    
    /*
     * Description:
     * 
     * @author: Murphy Lee
     * @throws IOException - Thrown when trying to access the FXML scene
     * @param stage - The window for the application
     * */
    @Override
    public void start(Stage stage) throws IOException {
        // Root node for storing contents of FXML scene
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        
        // Create scene and add it to the stage
        Scene scene = new Scene(root);

        // Add favicon logo to the top
        ImageView icon = new ImageView(getClass().getResource("QuizKitLogoFavicon.JPG").toExternalForm());
        stage.getIcons().add(icon.getImage());

        stage.setTitle("QuizKit Student");
        stage.setScene(scene);        // Adds current scene to window
        stage.setResizable(false);    // Keeps a fixed window size
        stage.centerOnScreen();
        stage.show();
    }
}
