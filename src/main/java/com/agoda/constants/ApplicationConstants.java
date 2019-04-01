package com.agoda.constants;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConstants
{
    public static final String URL_SPLIT_CHAR = ",";

    /**
     * Protocols supported by the application.
     * Maps protocol to respective downloader service
     */
    public static final Map<String, String> SUPPORTED_PROTOCOL_MAP = new HashMap<String, String>(){
        {
            put("http", "com.agoda.service.impl.HttpProtocolFileDownloader");
            put("https", "com.agoda.service.impl.HttpProtocolFileDownloader");
            put("ftp", "com.agoda.service.impl.FtpProtocolFileDownloader");
            put("sftp", "com.agoda.service.impl.SftpProtocolFileDownloader");
        }
    };

    /**
     * Extension to be assigned to files which are still downloading
     */
    public static final String TEMP_FILE_EXTENSION = ".apptemp";
    public static final int CONNECTION_TIMEOUT_VALUE = 100000;
    public static final int DATA_TIMEOUT_VALUE = 100000;
}
