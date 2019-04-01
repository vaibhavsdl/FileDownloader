package com.agoda.service.impl;

import com.agoda.constants.ApplicationConstants;
import com.agoda.model.Credential;
import com.agoda.service.SecuredFileDownloader;
import com.agoda.util.CredentialHandler;
import com.agoda.util.StringUtility;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Properties;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Downloads files from sftp protocol
 */
public class SftpProtocolFileDownloader implements SecuredFileDownloader
{
    /**
     *
     * @param path  local path where files should be downloaded
     * @param fileUrl URI of file to be downloaded
     * @return  File object of downloaded file
     */
    @Override
    public File downloadFile(String path, URI fileUrl) {

        // get credentials of file to be downloaded
        Credential credential = getCredentialForFile(fileUrl.toString());

        if(credential != null) {
            return downloadFile(path, fileUrl, credential);
        }
        return null;
    }

    /**
     *
     * @param path  local path where files should be downloaded
     * @param fileUri   URI of file to be downloaded
     * @param credential    Credentials of file to be downloaded
     * @return File object of downloaded file
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
        ChannelSftp sftp = null;
        Session sshSession = null;
        Channel channel = null;

        try
        (
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempDownloadFile));
        )
        {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(credential.getUserName(), server, getPort(fileUri));
            if(sshSession != null) {
                sshSession.setPassword(credential.getPassword());
                Properties sshConfig = new Properties();
                sshConfig.put("StrictHostKeyChecking", "no");
                sshSession.setTimeout(ApplicationConstants.DATA_TIMEOUT_VALUE);
                sshSession.setConfig(sshConfig);
                sshSession.connect();
                INFO_LOGGER.info("Session connected.");
                INFO_LOGGER.info("Opening Channel.");
                channel = sshSession.openChannel("sftp");
                channel.connect();
                sftp = (ChannelSftp) channel;
                INFO_LOGGER.info("Connected to " + server + ".");

                sftp.get(fileUri.getPath(), outputStream );

                tempDownloadFile.renameTo(downloadFile);

                INFO_LOGGER.info("File : " + fileUri.toString() + " has been downloaded successfully.");
                return downloadFile;
            }
            else {
                INFO_LOGGER.info("Failed to create session with server : " + fileUri.getHost());
            }

        } catch (Exception e) {
            INFO_LOGGER.info(fileUri.toString() + " download failed!!");
            ERROR_LOGGER.error(e);
        }
        finally {
            if(sftp != null && sftp.isConnected()) {
                sftp.disconnect();
            }
            if(sshSession != null && sshSession.isConnected())
            {
                sshSession.disconnect();
            }
            if(channel != null && channel.isConnected())
            {
                channel.disconnect();
            }
            tempDownloadFile.delete();
        }

        return null;
    }

    /**
     * Prompts user to provide username and password to download file
     *
     * @param fileUrl
     * @return  Credential object to access file
     */
    @Override
    public Credential getCredentialForFile(String fileUrl)
    {
        return CredentialHandler.getUserNameandPasswordFromUser(fileUrl);
    }

    /**
     *
     * get port of sftp server, default port is 22 unless provided in url
     * @param fileUri
     * @return
     */
    int getPort(URI fileUri) {
        return fileUri.getPort() != -1 ? fileUri.getPort() : 22;
    }


}
