package com.agoda.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;

public class FileUtility
{
    /**
     * Groups urls by protocols
     *
     * @param urlList   List of urls
     * @return
     */
    public static Map<String, List<URI>> groupFilesByProtocol(List<String> urlList)
    {
        Map<String, List<URI>> urlProtocolMap = new HashMap<>();

        for (String urlString : urlList)
        {
            try
            {
                URI uri = new URI(urlString);
                String protocol = uri.getScheme();

                List<URI> urls = urlProtocolMap.getOrDefault(protocol, new ArrayList<>());

                urls.add(uri);

                urlProtocolMap.put(protocol, urls);
            }
            catch (Exception ex)
            {
                ERROR_LOGGER.error(ex);
            }
        }

        return urlProtocolMap;
    }


}
