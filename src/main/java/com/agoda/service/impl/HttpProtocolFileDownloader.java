package com.agoda.service.impl;

import com.agoda.constants.ApplicationConstants;
import com.agoda.service.FileDownloader;
import com.agoda.util.StringUtility;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * Downloads files from http or https protocol to local folder
 */
public class HttpProtocolFileDownloader implements FileDownloader
{

    /**
     *
     * @param path  local folder where files should be downloaded
     * @param fileUri   URI of file to be downloaded
     * @return  File object of downloaded file
     */
    @Override
    public File downloadFile(String path, URI fileUri)
    {

        String absFileName = StringUtility.cleanPathString(fileUri.getHost())
                + StringUtility.cleanFileNameString(fileUri.getPath());

        File tempDownloadFile = new File(path, absFileName + ApplicationConstants.TEMP_FILE_EXTENSION);
        File downloadFile = new File(path, absFileName);


        try
        {
            INFO_LOGGER.info("Downloading file : " + fileUri.toString());

            FileUtils.copyURLToFile(new URL(fileUri.toString()), tempDownloadFile,
                    ApplicationConstants.CONNECTION_TIMEOUT_VALUE, ApplicationConstants.DATA_TIMEOUT_VALUE);

            tempDownloadFile.renameTo(downloadFile);

            INFO_LOGGER.info("File download complete!");

            return downloadFile;
        }
        catch (IOException ex)
        {
            INFO_LOGGER.info("File " + fileUri.toString() + " download failed");
            ERROR_LOGGER.error(ex);
            tempDownloadFile.delete();
        }

        return null;
    }
}
