package com.quizkit.api.sendgrid;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: Connects to Sendgrid to send quiz codes to students. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class EmailConnection {

    /**
     * Stores a reference to the EmailSettings when it loaded from the json settings file. 
     */
    EmailSettings settings;

    /**
     * Reads in the API key for SendGrid and adds it to the EmailSettings object. 
     * 
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public EmailConnection() throws IOException {

        // Constructs a Gson object with default configuration.
        Gson gson = new Gson();

        // Reads in the API key for SendGrid and adds it to the EmailSettings object, in the form of a Gson object.  
        this.settings = gson.fromJson(new FileReader(".\\sendgridSettings.json"), EmailSettings.class);
    }

    /**
     * Sends quiz code emails to students. 
     * 
     * @param name Student's name.
     * @param email Student's email.
     * @param studentCode Quiz Code.
     * @return Status code, which signals whether or not an email has been sent. 
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    public int Send(String name, String email, String studentCode) throws IOException {
        
        // Construct a new Mail object.
        Mail mail = new Mail();

        // Construct a new Email object. 
        Email from = new Email();

        // Sets the name of who the email is from.
        from.setName(settings.fromName);

        // Sets the name of who the email is sent from. 
        from.setEmail(settings.fromEmail);

        // Sets what email address the email is sent from. 
        mail.setFrom(from);

        // Construct a new Email object. 
        Email to = new Email();

        // Sets the email address of who the email sends to.  
        to.setEmail(email);

        // Sets the name of who the email sends to. 
        to.setName(name);

        // Constructs a new personalization object. 
        Personalization personalization = new Personalization();
        
        // Add the person that needs to be sent the email to that personalization object. 
        personalization.addTo(to);

        // Pass in the name of the student and the quiz code to personalization object. 
        personalization.addDynamicTemplateData("unique_name",studentCode);

        // Adds the personalization object to the mail object. 
        mail.addPersonalization(personalization);

        // Sets the template ID for the mail object. 
        mail.setTemplateId(settings.templateId);

        // Constructs a new Twilio SendGrid API wrapper with the API key.  
        SendGrid sg = new SendGrid(settings.apiKey);

        // Creates a new request object. 
        Request request = new Request();
        
        // Try/Catch statement to catch IOExceptions.
        try {
            
            // Sending request to SendGrid API.
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            
            // Prints the status code, body, and headers.
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            
            // Returns response status code to confirm that email has been sent. 
            return response.getStatusCode();
        } catch (IOException ex) {
            
            // Prints out error message.
            System.out.println(ex.getMessage());
            
            // Returns 0 to let program know that the email failed to send. 
            return 0;
        }
    }
}