package com.agoda.service.impl;

import com.agoda.constants.ApplicationConstants;
import com.agoda.model.Credential;
import com.agoda.service.SecuredFileDownloader;
import com.agoda.util.StringUtility;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.net.URI;

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

    @Override
    public Object establishConnectionToRemoteServer(URI fileUri) throws Exception {

        Credential credential = getCredentialForFile(fileUri.toString());

        return establishConnectionToRemoteServer(fileUri, credential);
    }

    @Override
    public Object establishConnectionToRemoteServer(URI fileUri, Credential credential) throws Exception {
        FTPClient ftpClient = new FTPClient();
        String server = fileUri.getHost();

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

            return ftpClient;
        }
        INFO_LOGGER.info("Login failed to server !");
        return null;
    }

    @Override
    public BufferedInputStream getBufferedInputStreamFromConnection(Object connObject, URI fileUri) throws Exception
    {
        FTPClient ftpClient = (FTPClient) connObject;
        String remoteFile = fileUri.getPath().substring(1);
        return new BufferedInputStream(ftpClient.retrieveFileStream(remoteFile));
    }

    @Override
    public void disconnectRemoteServer(Object connObject) throws IOException
    {
        FTPClient ftpClient = (FTPClient) connObject;
        if (ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }
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
