package com.agoda.util;

import com.agoda.model.Credential;

import java.io.IOException;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Handles credentials object creation from user inputs
 */
public class CredentialHandler
{
    public static Credential getUserNameandPasswordFromUser(String fileUrl)
    {
        try
        {
            IOUtility.printToConsole("Enter access credentials for file : " + fileUrl);

            IOUtility.printToConsole("Enter username : ");
            String userName = IOUtility.readFromConsole();

            IOUtility.printToConsole("Enter password : ");
            String password = IOUtility.readFromConsole();

            return new Credential(userName, password);

        }
        catch (IOException exception)
        {
            INFO_LOGGER.info("Unable to get credentials for file : " + fileUrl);
            ERROR_LOGGER.error(exception);
        }

        return null;
    }
}
