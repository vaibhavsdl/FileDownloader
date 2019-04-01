package com.agoda.service;

import com.agoda.model.Credential;

import java.io.File;
import java.net.URI;

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

    /**
     *
     * @param path  Local folder to download files into
     * @param fileUri   URI of file to download
     * @param credential    Credentials to access file
     * @return  File object downloaded
     */
    File downloadFile(String path, URI fileUri, Credential credential);
}
