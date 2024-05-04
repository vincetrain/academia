/*
     * Date: May 26, 2021
     * Author: Murphy Lee
     * Teacher: Mr. Ho
     * Description: Page where student completes test
     * */
package com.quizkit.gui.student;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

import com.quizkit.api.model.Question;
import com.quizkit.api.model.Quiz;
import com.quizkit.api.model.StudentAnswers;
import com.quizkit.api.model.StudentQuiz;
import com.quizkit.api.services.AnswerService;
import com.quizkit.api.services.QuizService;

/*
 * Description: Represents the Testing Controller of the FXML application, controls the components of the student test page
 * 
 * @author - Murphy Lee
 * */
public class FillTestController implements Initializable {
    /*
     * Description: Vertical container that displays all the questions & options in a vertical manner
     * */
    @FXML
    private VBox questionContainer;

    /*
     * Description: Data structure containing individual choices for each question
     * */
    private static ArrayList<HashMap<Integer, RadioButton>> questionOptions;   
    
    /*
     * Description: The textholders for each question
     * */
    private static ArrayList<Text> textHolders;                                 
    
    /*
     * Description: The area where each question is displayed
     * */
    private static ArrayList<TextArea> textAreas;                              

    /*
     * Description: Initializes the controller class - Loads data before window is displayed
     * 
     * @author - Murphy Lee
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Take user quiz from login information and use it to set up quiz data
        try {
            StudentQuiz quiz = LoginController.studentQuiz;
            runQuiz(quiz);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }    

    /*
     * Description: Displays quiz contents to the Scene
     * 
     * @author - Murphy Lee
     * @param studentsQuiz - The StudentQuiz object that holds quiz with questions
     * @throws Exception - Raised when trying to access FireBase data
     * */
    public static void runQuiz(StudentQuiz studentsQuiz) throws Exception {
        // Use FireBase service to obtain quiz from the quizID
        Quiz quiz = QuizService.getQuiz(studentsQuiz.quizId);

        // Create ArrayList (same size as the number of quiz questions) of HashMaps containing RadioButtons
        int length = quiz.questionList.size();
        questionOptions = new ArrayList<HashMap<Integer, RadioButton>>(length);
        // Jrraylists for java controls
        textHolders = new ArrayList<Text>(length);
        textAreas = new ArrayList<TextArea>(length);
        ArrayList<ToggleGroup> toggleGroups = new ArrayList<ToggleGroup>(length);

        displayQuestions(quiz, questionOptions, textHolders, textAreas, toggleGroups);
    }
    
    /*
     * Description: Adds JavaFX components to the scene according to the students test data
     * 
     * @author - Murphy Lee
     * @param quiz - The student-quiz data
     * @param options - ArrayList containing HashMaps of all the multiple choice options
     * @param texts - ArrayList containing all questions stored in a Text object
     * @param areas - ArrayList containing all textboxes for Text
     * @param toggles - ArrayList containing toggles for RadioButtons
     * */
    public static void displayQuestions(Quiz quiz, ArrayList<HashMap<Integer, RadioButton>> options, ArrayList<Text> texts, ArrayList<TextArea> areas, ArrayList<ToggleGroup> toggles) {
        int index = 0;     // Counter

        // Loop through the question list
        for (Question question : quiz.questionList) {
            // New Text - Feed the question and set the size of the text
            texts.add(new Text());
            texts.get(index).setText(question.questionText);
            
            // New TextArea to hold text and wrap to next line
            areas.add(new TextArea());
            areas.get(index).setPrefSize(600, 60);
            areas.get(index).setWrapText(true);
            areas.get(index).setEditable(false);   
            areas.get(index).setStyle("-fx-font-size: 16");

            // Bind the Text properties with the TextArea properties
            areas.get(index).textProperty().bind(texts.get(index).textProperty());
            
            // Create new HashMap in the ArrayList
            options.add(new HashMap<Integer, RadioButton>(question.choices.size()));
            toggles.add(new ToggleGroup());
            int innerIndex = 0;
            
            // Loop through the HashMaps from the array
            for (String choice : question.choices) {
                
                // Create new RadioButton
                options.get(index).put(innerIndex, new RadioButton(choice));

                // Add button to the current ToggleGroup
                options.get(index).get(innerIndex).setToggleGroup(toggles.get(index));
                innerIndex++;
            }
            index++;
        }  
    }

    /*
     * Description: Styles the TextAreas so that each area has a custom length according to the length of the question
     * 
     * @author - Murphy Lee
     * 
     * */
    public void alignText() {
        Region region;   // Space filler
        // Loop through the length of the text arrays, wrapping each text
        for (int i = 0; i < textAreas.size(); i++) {
            // Wrap the text 10 pixels before the TextArea wraps
            textHolders.get(i).setWrappingWidth(textAreas.get(i).getWidth() - 10);

            // Wrap the TextArea height to 20 pixels lower than the text for extra padding
            textAreas.get(i).setPrefHeight(textHolders.get(i).getLayoutBounds().getHeight() + 60);

            // Add text (questions) to the Parent VBox
            questionContainer.getChildren().add(textAreas.get(i));

            // Loop through RadioButtons, adding to the container
            for (int j = 0; j < questionOptions.get(i).size(); j++) {
                questionContainer.getChildren().add(questionOptions.get(i).get(j));
            }

            // Add empty region for spacing
            region = new Region();
            region.setPrefHeight(50);
            questionContainer.getChildren().add(region);
        }
    }

    /*
     * Description: Reads the selected RadioButtons when user presses submit
     * 
     * @author - Murphy Lee
     * @param e - ActionEvent triggered when user selects button
     * @throws Exception - Exception raised when trying to access the AnswerService class
     * */

    public void getQuizAnswers(ActionEvent e) throws Exception {
        String questionNum;   // The question number
        int answer;           // Stores an index equivalent to the students answer
        String quizId = LoginController.studentQuiz.quizId;
        String email = LoginController.studentQuiz.studentEmail;
        String name = LoginController.studentQuiz.studentName;

        // Confirm if the user wants to submit
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Submit");
        alert.setHeaderText("You're about to submit!");
        alert.setContentText("Are you sure you want to submit?");

        // If the user confirmed that they wanted to submit
        if (alert.showAndWait().get() == ButtonType.OK) {
            StudentAnswers studentAnswers = new StudentAnswers();   // Stores the student answer indexes
            try {
                // Loop through RadioButton data structure
                for (int i  = 0; i < questionOptions.size(); i++) {
                    answer = -2;   // Set to incorrect by default (in case student leaves question blank)
                    for (int index : questionOptions.get(i).keySet()) {
                        // Check if the RadioButton was selected
                        if (questionOptions.get(i).get(index).isSelected() == true) {
                            // Set the answer to the RadioButton index
                            answer = index;
                        }
                    }
                    questionNum = String.valueOf(i);
                    // Send the answer to the studentAnswers object
                    studentAnswers.answers.put(questionNum, answer);
                }
                // Save answers through FireBase
                AnswerService.saveAnswers(quizId, email, studentAnswers);

                // Go back to options menu
                FXMLLoader loader = new FXMLLoader(getClass().getResource("OptionsMenu.fxml"));
                Parent root = loader.load();

                // Create instance of the next scene controller - Display name in new scene
                OptionsMenuController optionsMenuController = loader.getController();
                optionsMenuController.greetStudent(name);

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
            // Thrown when the test has no questions
            catch (NullPointerException exception) {
                // Go back to login page
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

                // Create new scene with root node and add it to the stage
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                
                // Set the scene of the stage (Login page)
                stage.setScene(scene);
                stage.setResizable(true);
                stage.sizeToScene();
                stage.centerOnScreen();
                stage.setResizable(false);
                stage.show();
            }
        }
    }
}
