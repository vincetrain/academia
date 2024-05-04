package com.quizkit.gui.teacher;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.quizkit.api.model.Quiz;
import com.quizkit.api.model.Teacher;
import com.quizkit.api.model.StudentQuiz;
import com.quizkit.api.services.QuizService;
import com.quizkit.api.services.TeacherService;
import com.quizkit.api.services.StudentService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Date: June 4 2021
 * Teacher: Mr. Ho
 * Description: Controller for importFiles.fxml
 * 
 * @author Vincent Tran
 */
public class ImportController implements Initializable{

    @FXML
    private TextField quizName;
    @FXML
    private Label quizDir;
    @FXML
    private Label studentDir;
    @FXML
    private Button confirmButton;

    Teacher teacher;

    String quizString = null;   // String containing inputted quiz directory
    String studentString = null;    // String containing inputted student directory

    @FXML
    /**
     * Uploads both student and quiz files to db
     * 
     * @param event Handles onAction for confirm button
     * @throws Exception Throws exception if goBack() does not find fxml or if exception is raised in service methods
     */
    private void confirmImport(ActionEvent event) throws Exception{
        event.consume();
        String quizId;
        // Uploads quiz file
        Quiz quiz = QuizService.createQuizFromCSV(quizName.getText(), quizString);
        quizId = TeacherService.addTeacherQuiz(teacher.teacherEmail, quiz);
        // Uploads student file
        ArrayList<StudentQuiz> students = StudentService.createStudentsFromCSV(studentString);
        StudentService.addStudentsToQuiz(quizId, students);
        goBack(event);
    }

    /**
     * Loads previous page
     * 
     * @param event Handles onAction for cancel button
     * @throws Exception Throws exception in case goBack() does not find fxml
     */
    @FXML
    private void cancelImport(ActionEvent event) throws Exception {
        event.consume();
        goBack(event);
    }

    /**
     * Runs everytime a key is pressed in textfield and checks if empty
     * 
     * @param event Handles onKeyPressed in name textfield
     */
    @FXML
    private void readName(KeyEvent event) {
        event.consume();
        checkFile();
    }

    /**
     * opens file chooser for a quiz, and sets dir as a string and label
     * 
     * @param event Handles onAction for browse quiz button
     * @throws Exception Throws exception in case file returns invalid
     */
    @FXML
    private void browseQuiz(ActionEvent event) throws Exception {
        event.consume();
        quizString = chooseFile(event, 0);
        quizDir.setText(quizString);
        checkFile();
    }

    /**
     * opens a filechooser for a student file, and sets dir as string and label
     * 
     * @param event Handles onAction for browse button
     * @throws Exception Throws exception in case file returns invalid
     */
    @FXML
    private void browseStudent(ActionEvent event) throws Exception {
        event.consume();
        studentString = chooseFile(event, 1);
        studentDir.setText(studentString);
        checkFile();
    }

    /**
     * Uses fileChooser to make user select a file
     * 
     * @param event Used to get scene from event
     * @param reqFile Used to define which file is being located
     * @return Returns file directory
     * @throws IOException Throws IOException in case file is invalid
     */
    public static String chooseFile(ActionEvent event, int reqFile) throws IOException {
        // Make sure this method is called with either 0 or 1 as a param, depending on what file will be read.
        String[] prompts = {"Select Quiz CSV File", "Select Emails CSV file"};

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // Gets current stage

        FileChooser fileChooser = new FileChooser();    // Creates new fileChooser instance
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));    // Filters for .csv files

        fileChooser.setTitle(prompts[reqFile]); // Sets title to needed file prompt
        File file = fileChooser.showOpenDialog(stage); // Opens fileChooser and uses provided file as new File variable

        return String.valueOf(file);    // Returns file for later usage
    }

    /**
     * Loads previous scene (homepage)
     * 
     * @param event Handles onAction for cancel button
     * @throws Exception Throws exception in case fxml file is not found
     */
    private void goBack(ActionEvent event) throws Exception{
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

    /**
     * Checks if all information is inputted and enables buttons if true
     */
    private void checkFile() {
        if (quizString != null && studentString != null && !quizName.getText().isEmpty()) {  // Checks if fields 
            confirmButton.setDisable(false);
        }
        else {
            confirmButton.setDisable(true);
        }
    }

    /**
     * Runs when FXML is loaded and changes teacher object to match one inputted from LoginController
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            teacher = TeacherService.getTeacher(LoginController.teacherEmail); // gets teacher information
        } catch (Exception e) {}
    }

}
