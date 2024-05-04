package com.quizkit.api.sendgrid;

/**
 * Date: June 4, 2021
 * Teacher: Mr. Ho
 * Description: Model for the email settings. This object is deserialized from the sendgridSettings.json file. 
 * 
 * @author Ibrahim Rahman <341169092@gapps.yrdsb.ca>
 */
public class EmailSettings {
    
    /**
     * String of the API key for SendGrid. 
     */
    String apiKey;

    /**
     * String of the email template ID in SendGrid.
     */
    String templateId;

    /**
     * String of the name that the email is sent from. Used in the title of the email  
     */ 
    String fromName;

    /**
     *  String of the email address that the email is sent from.
     */  
    String fromEmail;
}