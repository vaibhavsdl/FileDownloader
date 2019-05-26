package com.agoda.service;

import com.agoda.constants.ApplicationConstants;
import com.agoda.model.Credential;
import com.agoda.util.StringUtility;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URI;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Base interface which all file downloader service which require credentails should implement
 */
public interface SecuredFileDownloader extends FileDownloader
{
    /**
     * Default implementation of getting credentials to access a file over secured channel
     *
     * @param fileUrl   URI of file to be downloaded
     * @return
     */
    default Credential getCredentialForFile(String fileUrl)
    {
        String userName = "anonymous";

        String password = "";

        return new Credential(userName, password);

    }

    default File downloadFile(String path, URI fileUri, Credential credential)
    {
        BufferedInputStream inputStream = null;
        Object connObject = null;

        __main:
        try
        {

            connObject = establishConnectionToRemoteServer(fileUri, credential);    //establishing connection to remote server

            if(connObject == null)
            {
                break __main;
            }

            inputStream = getBufferedInputStreamFromConnection(connObject, fileUri);     // getting InputStream from established connection

            if(inputStream != null) {
                String absFileName = StringUtility.cleanPathString(fileUri.getHost())
                        + StringUtility.cleanFileNameString(fileUri.getPath());
                File tempDownloadFile = new File(path, absFileName + ApplicationConstants.TEMP_FILE_EXTENSION);
                File downloadFile = new File(path, absFileName);

                FileWriterService fileWriterService = new FileWriterService(inputStream, tempDownloadFile, downloadFile);
                fileWriterService.writeStreamToFile();
                return downloadFile;
            }
        }
        catch (Exception ex)
        {
            ERROR_LOGGER.error(ex);
        }
        finally
        {
            if(inputStream != null) {
                closeInputStream(inputStream);
            }

            if(connObject != null) {
                try {
                    disconnectRemoteServer(connObject);
                }
                catch (Exception e)
                {
                    INFO_LOGGER.info("Unable to disconnect from server!");
                    ERROR_LOGGER.error(e);
                }
            }
        }
        return null;
    }
}
