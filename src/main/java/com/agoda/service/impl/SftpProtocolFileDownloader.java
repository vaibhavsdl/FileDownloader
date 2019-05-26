package com.agoda.service.impl;

import com.agoda.constants.ApplicationConstants;
import com.agoda.model.Credential;
import com.agoda.service.SecuredFileDownloader;
import com.agoda.util.CredentialHandler;
import com.jcraft.jsch.*;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Downloads files from sftp protocol
 */
public class SftpProtocolFileDownloader implements SecuredFileDownloader
{
    @Override
    public Object establishConnectionToRemoteServer(URI fileUri) throws JSchException {
        Credential credential = getCredentialForFile(fileUri.toString());

        return establishConnectionToRemoteServer(fileUri, credential);
    }

    @Override
    public Object establishConnectionToRemoteServer(URI fileUri, Credential credential) throws JSchException {
        String server = fileUri.getHost();
        JSch jsch = new JSch();
        Session sshSession = jsch.getSession(credential.getUserName(), server, getPort(fileUri));
        if(sshSession != null) {
            sshSession.setPassword(credential.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setTimeout(ApplicationConstants.DATA_TIMEOUT_VALUE);
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            INFO_LOGGER.info("Session connected.");
            INFO_LOGGER.info("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;

            Map<String, Object> connectionEntityMap = new HashMap<>();
            connectionEntityMap.put("sftp", sftp);
            connectionEntityMap.put("session", sshSession);

            return connectionEntityMap;
        }
        else
        {
            return null;
        }
    }

    @Override
    public BufferedInputStream getBufferedInputStreamFromConnection(Object connObject, URI fileUri) throws SftpException {
        Map<String, Object> connectionEntityMap = (Map<String, Object>) connObject;

        ChannelSftp channelSftp = (ChannelSftp) connectionEntityMap.get("sftp");

        return new BufferedInputStream(channelSftp.get(fileUri.getPath()));
    }

    @Override
    public void disconnectRemoteServer(Object connObject) {
        Map<String, Object> connectionEntityMap = (Map<String, Object>) connObject;

        ChannelSftp channelSftp = (ChannelSftp) connectionEntityMap.get("sftp");
        channelSftp.disconnect();

        Session sshSession = (Session) connectionEntityMap.get("session");
        sshSession.disconnect();
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
