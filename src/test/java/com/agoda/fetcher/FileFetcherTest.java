package com.agoda.fetcher;

import com.agoda.servers.MockFtpServer;
import com.agoda.servers.http.MockHttpServer;
import org.apache.ftpserver.FtpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFetcherTest {

    File expectedOutputFile1;
    File expectedOutputFile2;
    String downloadFolderPath = "/tmp";

    FtpServer ftpServer;
    MockHttpServer mockHttpServer;

    @Before
    public void setUp() throws IOException {
        expectedOutputFile1 = new File(downloadFolderPath, "localhost_static_http_image.jpg");

        expectedOutputFile2 = new File(downloadFolderPath, "localhost_ftp_image.jpg");

        ftpServer = MockFtpServer.createServer();
        MockFtpServer.startFTPServer(ftpServer);

        mockHttpServer = new MockHttpServer();
        mockHttpServer.start();
    }

    @Test
    public void downloadFilesFromUrls()
    {
        List<String> urlList = new ArrayList<>();

        urlList.add("ftp://localhost/ftp_image.jpg");
        urlList.add("http://localhost:8080/static/http_image.jpg");

        FileFetcher.downloadFilesFromUrls(urlList, downloadFolderPath);

        Assert.assertTrue("File Fetcher failed to download file : " + expectedOutputFile1.toString()
                , expectedOutputFile1.exists());
        Assert.assertTrue("File Fetcher failed to download file : " + expectedOutputFile2.toString()
                , expectedOutputFile2.exists());
    }

    @After
    public void tearDown()
    {
        expectedOutputFile1.delete();
        expectedOutputFile2.delete();

        MockFtpServer.stopFTPServer(ftpServer);
        mockHttpServer.stop();
    }
}