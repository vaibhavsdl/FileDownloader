package com.agoda.service;

import com.agoda.constants.ApplicationConstants;
import com.agoda.model.Credential;
import com.agoda.util.StringUtility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Base interface which all file downloader service should implement
 *
 * Each protocol implementation must implement the methods to achieve following tasks:-
 *      1. establish connection to remote server as per the protocol
 *      2. obtain input stream for the remote file via established connection object(s)
 *      3. close the session object(s) are file download is complete
 */
public interface FileDownloader
{
    /**
     * Default implementation for carrying out the download for any protocol
     *
     * @param path
     * @param fileUri
     * @return
     */
    default File downloadFile(String path, URI fileUri)
    {
        BufferedInputStream inputStream = null;
        Object connObject = null;

        __main:
        try
        {

            connObject = establishConnectionToRemoteServer(fileUri);    //establishing connection to remote server

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

    /**
     * Returns an object denoting the connection to the remote server
     * Can also be used to return a collection/map containing various connections(channels, sessions etc.) which have
     * been used to create a successful connection to a remote server
     *
     * @return
     * @param fileUri
     */
    Object establishConnectionToRemoteServer(URI fileUri) throws Exception;

    Object establishConnectionToRemoteServer(URI fileUri, Credential credential) throws Exception;

    /**
     * Get input stream from the corresponding connection entity of each protocol.
     * The input stream can be obtained from a channel, session, connection etc. depending on the protocol
     *
     * @param connObject
     * @param fileUri
     * @return
     */
    BufferedInputStream getBufferedInputStreamFromConnection(Object connObject, URI fileUri) throws Exception;

    /**
     * This function is used to close down the input stream created in the getBufferedInputStreamFromConnection() method
     *
     * @param inputStream
     */
    default void closeInputStream(BufferedInputStream inputStream)
    {
        try
        {
            inputStream.close();
        }
        catch (IOException e)
        {
            ERROR_LOGGER.error(e);
        }
    }

    /**
     * This function should shut down all sessions/connections which were made to the remote server
     * the object which was returned by establishConnectionToRemoteServer() should be given back to this method to
     * initiate shutdown of all remote entities
     *
     * @param connObject
     */
    void disconnectRemoteServer(Object connObject) throws IOException;
}
