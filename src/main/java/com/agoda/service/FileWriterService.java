package com.agoda.service;

import java.io.*;

import static com.agoda.constants.LoggingConstants.ERROR_LOGGER;
import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

public class FileWriterService
{

    InputStream inputStream;
    File tempDestinationFile;
    File destinationFile;

    public FileWriterService(InputStream inputStream, File destinationFile)
    {
        this(inputStream, destinationFile, destinationFile);
    }

    public FileWriterService(InputStream inputStream, File tempDestinationFile, File destinationFile)
    {
        this.inputStream = inputStream;
        this.tempDestinationFile = tempDestinationFile;
        this.destinationFile = destinationFile;
    }

    public void writeStreamToFile() throws Exception
    {
        try {
            byte[] bytesArray = new byte[4096];

            int bytesRead;
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempDestinationFile));

            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                bufferedOutputStream.write(bytesArray, 0, bytesRead);
            }

            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            tempDestinationFile.renameTo(destinationFile);

            INFO_LOGGER.info("Stream written successfully into file");
        }
        catch (Exception e)
        {
            ERROR_LOGGER.error("Failed to write stream into file.");
            ERROR_LOGGER.error(e);
            throw e;
        }
    }
}
