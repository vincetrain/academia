/*
 * Date: June 1st, 2021
 * Author: Murphy Lee
 * Description: StudentResults class whose properties can be stored in an Observable list and transferred into TableColumns
 * */

package com.quizkit.gui.teacher.Services;

/*
 * Description: Represents a students data for his test, accessed by TableView of the teacher/TeacherMenu.java program
 * 
 * @author - Murphy Lee
 * */
public class StudentResults {
    /*
     * Description: String containing the students email
     * */
    private String studentEmail;

    /*
     * Description: String containing the students mark for the a quiz
     * */
    private String studentMark;

    /*
     * Description: Constructor method for an instance of a students data
     * 
     * @author - Murphy Lee
     * @param email - String containing the users email
     * @param mark - Object containing the users mark for the quiz, or -1 if incomplete
     * */
    public StudentResults(String email, Object mark) {
        studentEmail = email;
        if (String.valueOf(mark).equals("-1")) {
            studentMark = "Incomplete";
        }
        else {
            studentMark = String.valueOf(mark) + "%";
        }
    }

    /*
     * Description: Getter method for TableColumn data
     * 
     * @author - Murphy Lee
     * @return studentEmail - The student's email
     * */
    public String getStudentEmail() {
        return studentEmail;
    }

    /*
     * Description: Getter method for TableColumn data
     * 
     * @author - Murphy Lee
     * @return studentMark - The student's quiz mark
     * */
    public String getStudentMark() {
        return studentMark;
    }
}
