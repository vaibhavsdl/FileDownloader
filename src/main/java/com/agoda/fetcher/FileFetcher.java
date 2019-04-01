package com.agoda.fetcher;

import com.agoda.service.FileDownloader;
import com.agoda.util.FileUtility;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.agoda.constants.ApplicationConstants.SUPPORTED_PROTOCOL_MAP;
import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

/**
 * The base class which controls all file downloading as per the service associated with the protocol
 */
public class FileFetcher
{
    /**
     * Downloads files to local directory, uses corresponding service to each protocol file
     *
     * @param urlList   list of urls which have to be downloaded
     * @param downloadFolderPath    the local folder where files needs to be downloaded
     */
    public static void downloadFilesFromUrls(List<String> urlList, String downloadFolderPath)
    {
        Map<String, List<URI>> fileProtocolMap = FileUtility.groupFilesByProtocol(urlList);

        for(Map.Entry<String, List<URI>> entry : fileProtocolMap.entrySet())
        {
            String protocol = entry.getKey();
            List<URI> fileUrlList = entry.getValue();

            if(SUPPORTED_PROTOCOL_MAP.containsKey(protocol))
            {
                INFO_LOGGER.info("Downloading file from protocol : " + protocol);

                try
                {
                    Class<FileDownloader> protocolClass = (Class<FileDownloader>) Class.forName(SUPPORTED_PROTOCOL_MAP.get(protocol));

                    FileDownloader fileDownloader = protocolClass.newInstance();

                    for(URI fileUrl : fileUrlList)
                    {
                        fileDownloader.downloadFile(downloadFolderPath, fileUrl);
                    }
                }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
                {
                    ERROR_LOGGER.error(ex);
                }
            }
            else
            {
                INFO_LOGGER.info("Protocol " + protocol + " is not supported!");
            }
        }
    }
}
