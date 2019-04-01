package com.agoda;

import com.agoda.fetcher.FileFetcher;
import com.agoda.util.IOUtility;
import com.agoda.util.StringUtility;

import java.io.IOException;
import java.util.List;

import static com.agoda.constants.ApplicationConstants.URL_SPLIT_CHAR;
import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Main class to download files
 */
public class Application
{

    static String downloadFolderPath;

    public static void main(String[] args)
    {

        INFO_LOGGER.info("App started.");

        try {

            IOUtility.printToConsole( "Enter file urls (comma-seperated) : ");
            String urlString = IOUtility.readFromConsole();

            IOUtility.printToConsole( "Enter local folder to download files into : ");
            downloadFolderPath = IOUtility.readFromConsole();

            INFO_LOGGER.info("urls : " + urlString);

            List<String> urlList = StringUtility.splitStringToList(urlString.toLowerCase(), URL_SPLIT_CHAR);

            FileFetcher.downloadFilesFromUrls(urlList, downloadFolderPath);
        }
        catch (IOException ex)
        {
            ERROR_LOGGER.error(ex);
        }

        INFO_LOGGER.info("App ends.");
    }
}
