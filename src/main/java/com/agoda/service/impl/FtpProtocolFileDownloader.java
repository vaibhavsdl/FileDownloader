package com.agoda.service.impl;

import com.agoda.constants.ApplicationConstants;
import com.agoda.model.Credential;
import com.agoda.service.SecuredFileDownloader;
import com.agoda.util.StringUtility;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.URI;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Downloads files from ftp server
 */
public class FtpProtocolFileDownloader implements SecuredFileDownloader
{

    @Override
    public File downloadFile(String path, URI fileUri)
    {
        //Get credentials to access file
        Credential credential = getCredentialForFile(fileUri.toString());

        return downloadFile(path, fileUri, credential);
    }


    /**
     * Downloads file from remote server to local folder
     *
     * @param path  folder where files should be downloaded
     * @param fileUri   URI of file to be downloaded
     * @param credential    Credentials to access file on remote server
     * @return
     */
    @Override
    public File downloadFile(String path, URI fileUri, Credential credential)
    {
        String server = fileUri.getHost();

        String remoteFile = fileUri.getPath().substring(1);

        String absFileName = StringUtility.cleanPathString(fileUri.getHost())
                + StringUtility.cleanFileNameString(fileUri.getPath());
        File tempDownloadFile = new File(path, absFileName + ApplicationConstants.TEMP_FILE_EXTENSION);
        File downloadFile = new File(path, absFileName);

        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;

        try
        (
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempDownloadFile));
        )
        {
            ftpClient.setConnectTimeout(ApplicationConstants.CONNECTION_TIMEOUT_VALUE);
            ftpClient.connect(server, getPort());
            ftpClient.setDefaultTimeout(ApplicationConstants.CONNECTION_TIMEOUT_VALUE);
            ftpClient.setDataTimeout(ApplicationConstants.DATA_TIMEOUT_VALUE);

            INFO_LOGGER.info("Attempting to connect to server : " + server + "...");

            boolean loginSuccessful = ftpClient.login(credential.getUserName(), credential.getPassword());

            if(loginSuccessful) {
                INFO_LOGGER.info("Connection successful !");

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                byte[] bytesArray = new byte[4096];
                int bytesRead;

                INFO_LOGGER.info("Downloading file : " + fileUri.toString());

                inputStream = ftpClient.retrieveFileStream(remoteFile);

                while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                    outputStream.write(bytesArray, 0, bytesRead);
                }

                boolean success = ftpClient.completePendingCommand();
                if (success) {
                    INFO_LOGGER.info("File : " + fileUri.toString() + " has been downloaded successfully.");
                }

                tempDownloadFile.renameTo(downloadFile);

                return downloadFile;
            }
            else {
                INFO_LOGGER.info("Login failed to server !");
            }
        }
        catch (Exception ex) {
            INFO_LOGGER.info(fileUri.toString() + " download failed!!");
            ERROR_LOGGER.error(ex);
        }
        finally {

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e)
            {
                ERROR_LOGGER.error(e);
            }

            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ERROR_LOGGER.error("Unable to disconnect from ftp server : " + server);
                ERROR_LOGGER.error(ex);
            }

            tempDownloadFile.delete();
        }

        return null;
    }

    /**
     * Returns port number of remote server
     *
     * @return port number of remote server
     */
    int getPort() {
        return 21;
    }
}
