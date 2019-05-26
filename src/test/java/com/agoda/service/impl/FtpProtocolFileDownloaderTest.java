package com.agoda.service.impl;

import com.agoda.servers.MockFtpServer;
import org.apache.ftpserver.FtpServer;
import org.junit.*;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;


public class FtpProtocolFileDownloaderTest {

    static FtpServer ftpServer;
    File downloadFile;

    @Before
    public void setup()
    {
        ftpServer = MockFtpServer.createServer();
        MockFtpServer.startFTPServer(ftpServer);
    }

    public File downloadFile(URI uri, String path)
    {
        FtpProtocolFileDownloader ftpProtocolFileDownloader = new FtpProtocolFileDownloader();

        return ftpProtocolFileDownloader.downloadFile(path, uri);
    }

    @Test
    public void downloadSmallFile()
    {
        try {
            URI uri = new URI("ftp://localhost/file_284KB.csv");
            File file = Paths.get(getClass().getResource("/file_284KB.csv").toURI()).toFile();

            downloadFile = downloadFile(uri, "/tmp");

            Assert.assertTrue("Unable to download file : " + downloadFile.toString(), downloadFile.exists());
            Assert.assertTrue("File not downloaded completely", file.length() == downloadFile.length());
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void downloadLargeFile()
    {
        try {
            URI uri = new URI("ftp://localhost/file_10MB.zip");
            File file = Paths.get(getClass().getResource("/file_10MB.zip").toURI()).toFile();

            downloadFile = downloadFile(uri, "/tmp");

            Assert.assertTrue("Unable to download file : " + downloadFile.toString(), downloadFile.exists());
            Assert.assertTrue("File not downloaded completely", file.length() == downloadFile.length());
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void teardown()
    {
        MockFtpServer.stopFTPServer(ftpServer);
        downloadFile.delete();
    }
}