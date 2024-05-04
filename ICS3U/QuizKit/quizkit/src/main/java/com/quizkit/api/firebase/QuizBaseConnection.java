package com.quizkit.api.firebase;

import com.google.firebase.cloud.FirestoreClient;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.*;
import java.io.FileInputStream;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: Creates a connection with Firebase Database using an account key JSON file. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class QuizBaseConnection {

    /**
     * Holds the Firestore instance associated with the Firebase Database.
     */
    private static Firestore db;

    /**
     * Establishes a connection with the Firebase database. 
     * 
     * @return Returns the Firestore instance associated with the Firebase Database. 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static Firestore GetConnection() throws Exception {

        // Checks if the Firestore instance associated with the Firebase Database is null. 
        if (db == null) {
            
            // Obtains the Firestore keys from an account key JSON file using FileInputStream. 
            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

            // Gets Google credentials from the stream that contains a Service Account key file in JSON format.
            GoogleCredentials cred = GoogleCredentials.fromStream(serviceAccount);

            // Creates an empty builder, then sets the GoogleCredentials to authenticate the SDK. Finally, it 
            // builds the FirebaseOptions instance from the previously set options.
            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(cred).build();

            // Initializes the default FirebaseApp using the given options. 
            FirebaseApp app = FirebaseApp.initializeApp(options);

            // Assigns the Firestore instance associated with the specified Firebase app.
            db = FirestoreClient.getFirestore(app);
        }

        // Returns the Firestore instance associated with the Firebase Database. 
        return db;
    }

    /**
     * Closes the previously established connection with the Firebase Database. 
     * 
     * @throws Exception Indicates conditions that need to be caught.
     */
    public static void CloseConnection() throws Exception {
        
        // If the database connection is not null (there's a connection), close the connection. 
        if (db != null)

            // Closes the connection with the database. 
            db.close();
    }
}