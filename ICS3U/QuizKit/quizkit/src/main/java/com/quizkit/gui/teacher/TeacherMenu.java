package com.quizkit.gui.teacher;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
 
import com.quizkit.api.model.StudentQuiz;
import com.quizkit.api.model.Teacher;
import com.quizkit.api.sendgrid.EmailConnection;
import com.quizkit.api.services.StudentService;
import com.quizkit.api.services.TeacherService;
import com.quizkit.gui.teacher.Services.StudentResults;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Date: June 4 2021
 * Teacher: Mr. Ho
 * Description: Controller used for homepage.fxml
 */
public class TeacherMenu implements Initializable{

    Teacher teacher;    // initializes teacher object to later store teacher information
    String quizId;  // initializes quizId string to later store quizId for usage with firebase
    String quizName;    // Stores selected quiz name
    ArrayList<String> quizIdList;   // ArrayList containing quizIds from db
    ArrayList<String> quizNameList; // ArrayList containing quizNames from quizIdList

    // Main
    @FXML
    private Label fullName;
    @FXML
    private Button startButton;
    @FXML
    private Button exportButton;
    @FXML
    private Label studentLabel;
    @FXML
    private Button studentReload;

    // List View
    @FXML
    private ListView quizList;
    @FXML
    private Label error;

    // Class Table
    @FXML
    private TableView<StudentResults> classTable;
    @FXML
    private TableColumn<StudentResults, String> studentEmail;
    @FXML
    private TableColumn<StudentResults, String> studentScore;

    private ObservableList<StudentResults> studentData;

    /**
     * Gets inputted information and imports files to firebase
     * 
     * @author Vincent Tran
     * @param event Handles onAction for import quiz button
     * @throws Exception Throws exception if fxml not found
     */
    @FXML
    private void importQuiz(ActionEvent event) throws Exception {
        event.consume();
        // Loads FXML file into Parent root variable
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/importFiles.fxml"));
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
     * Sends email to all students if a quiz is selected. Taken directly from TeacherMain CLI.
     * 
     * @author Ibrahim Rahman
     * @param event Handles onAction for start quiz button
     * @throws Exception Throws exception if EmailConnection breaks
     */
    @FXML
    private void startQuiz(ActionEvent event) throws Exception {
        event.consume();
        if (quizId != null) {
            // email all students for the quiz
            ArrayList<StudentQuiz> studentQuizList = StudentService.getStudentsFromQuiz(quizId);
            EmailConnection email = new EmailConnection();
            for (StudentQuiz student : studentQuizList) {
                System.out.println("Sending invitation to " + student.studentEmail + " ...");
                email.Send(student.studentName, student.studentEmail, student.studentQuizCode);
            }
            messagePrompt(2);
        }
    }

    /**
     * Exports the quiz into a TeachAssist friendly format
     * 
     * @author Vincent Tran
     * @param event Handles onAction for export quiz button
     * @throws Exception Throws exception if directory returns invalid or TeacherService breaks
     */
    @FXML
    private void exportQuiz(ActionEvent event) throws Exception {
        event.consume();
        String dir = exportDirChooser(event);
        System.out.println(dir);
        int i = 0;  // Used to keep track if an error is raised
        if (dir != null) {  // If user selects a valid directory...
        TeacherService.exportAllMarks(quizId, (dir + "\\" + quizName + ".csv"));
        }
        else {  // Else, add i+1 to give error message in messagePrompt()
            i++;
        }
        messagePrompt(0+i); // Prompts either an error or that file has been exported
    }

    /**
     * Goes to main.fxml if cancel button was pressed.
     * 
     * @author Vincent Tran
     * @param event Handles onAction for logout button
     * @throws Exception Throws exception if fxml not found
     */
    @FXML
    private void logout(ActionEvent event) throws Exception {
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
     * Gets selected quizList item if clicked on. 
     * 
     * @author Vincent Tran
     * @param event Handles onMouseClicked for quizList listview
     * @throws Exception Throws exception if populateStudent breaks
     */
    @FXML 
    private void getQuiz(MouseEvent event) throws Exception {
        event.consume();
        quizName = String.valueOf(quizList.getSelectionModel().getSelectedItem());  // Sets quizName string to selected quiz
        quizId = quizIdList.get(quizNameList.indexOf(quizName));  // gets quiz ID from quizName string
        populateStudents(); // populates students marks table for quiz (shows up as none if none avaiable)
        toggleButtons();

    }

    /**
     * Calls populateStudents() to repopulate classTable
     * 
     * @author Vincent Tran
     * @param event Handles onAction for student list reload button
     * @throws Exception Throws exception if populateStudents breaks
     */
    @FXML
    private void reloadTable(ActionEvent event) throws Exception {
        populateStudents(); // reloads the table by repopulating quiz
    }

    /**
     * Populates Quiz ListView by reading quizIdList and getting + inputting quiz name
     * 
     * @author Vincent Tran
     */
    private void populateQuiz() {
        quizIdList = new ArrayList<String>(teacher.quizzes.keySet()); // ArrayList for quiz ids
        quizNameList = new ArrayList<String>(teacher.quizzes.values());
        if (quizIdList.size() > 0) {
            for (int i = 0; i < quizIdList.size(); i++) {
                quizList.getItems().add(teacher.quizzes.get(quizIdList.get(i)));
            }
        }
        else {
            error.setDisable(true);
        }
    }

    /*
     * Description: Shows all student data in a TableView for a given quiz
     * 
     * @author - Murphy Lee
     * @throws Exception - Thrown when accessing TeacherService methods
     * */
    private void populateStudents() throws Exception {
        ArrayList<StudentResults> studentResultsList = new ArrayList<StudentResults>();
        if (quizId != null) {
            // Enable TableView, labels, buttons
            classTable.setDisable(false);
            studentLabel.setDisable(false);
            studentReload.setDisable(false);
            
            // Store student marks in a HashMap (email, score)
            Map<String, Object> results = TeacherService.getAllMarks(quizId);

            // Loop through the hashmap, storing data into TableView
            for (Map.Entry<String, Object> entry : results.entrySet()) {
                // Create a StudentResults object and add it to ArrayList
                StudentResults studentResults = new StudentResults(entry.getKey(), entry.getValue());
                studentResultsList.add(studentResults);
            }
            // Store ArrayList contents into an ObservableList
            studentData = FXCollections.observableArrayList(studentResultsList);

            // Add the ObservableList to the TableView
            classTable.setItems(studentData);

            // Set cell columns for emails and marks
            studentEmail.setCellValueFactory(new PropertyValueFactory<StudentResults, String>("studentEmail"));
            studentEmail.setStyle("-fx-alignment: CENTER;");

            studentScore.setCellValueFactory(new PropertyValueFactory<StudentResults, String>("studentMark"));
            studentScore.setStyle("-fx-alignment: CENTER;");

            // Add columns to TableView
            classTable.getColumns().setAll(studentEmail, studentScore);
        }
    }

    /**
     * Toggles buttons' disable value
     * 
     * @author Vincent Tran
     */
    private void toggleButtons() {
        if (quizId != null) {
            startButton.setDisable(false);
            exportButton.setDisable(false);
        }
        else {
            startButton.setDisable(true);
            exportButton.setDisable(true);
        }
    }

    /**
     * Prompts the user with according message
     * 
     * @author Vincent Tran
     * @param option Defines what prompt to be used
     */
    private void messagePrompt(int option) {
        // String array storing prompts
        String[] prompts = {
            "Quiz successfully exported",
            "Error occured while exporting quiz.",
            "Quiz has started and emails have been sent."
        };
        Alert alert = new Alert(Alert.AlertType.INFORMATION);   // Creates a new informational alert box
        alert.setTitle(prompts[option]);    // Sets title to prompt
        alert.setHeaderText(prompts[option]);   // Sets header text to prompt
        alert.getDialogPane().setExpandableContent(new ScrollPane(new TextArea(prompts[option])));  // Sets text area to prompt
        alert.showAndWait();    // Disables functionality from previous scene until this is closed
    }

    /**
     * Uses DirChooser to select export directory
     * 
     * @author Vincent
     * @param event Used to get scene from event
     */
    private String exportDirChooser(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow(); // Gets current stage

        DirectoryChooser dirChooser = new DirectoryChooser();    // Creates new DirectoryChooser instance
        dirChooser.setTitle("Select Export Directory"); // Sets title
        File selectedDir = dirChooser.showDialog(stage);   // Opens DirectoryChooser on scene
        if (selectedDir != null) { // Returns string of directory if valid directory
            return String.valueOf(selectedDir);
        }
        return null;    // Returns null
    }

    /**
     * Runs when FXML is loaded and gets teacher + sets name
     * 
     * @author Vincent Tran
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Logins to teacher and displays teacher name as label
        try {
            teacher = TeacherService.getTeacher(LoginController.teacherEmail);
            fullName.setText(teacher.teacherName);
        } catch (Exception e) {
            fullName.setText("Error occurred while fetching teacher name");
        }
        
        populateQuiz(); // fetches quiz list

    }
}
