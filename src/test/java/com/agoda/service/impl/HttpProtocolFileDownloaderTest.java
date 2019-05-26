package com.agoda.service.impl;

import com.agoda.fetcher.FileFetcher;
import com.agoda.servers.MockFtpServer;
import com.agoda.servers.http.MockHttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class HttpProtocolFileDownloaderTest {

    File expectedOutputFile1 = null;
    MockHttpServer mockHttpServer;
    String downloadFolderPath = "/tmp";

    @Before
    public void setUp() throws IOException {
        expectedOutputFile1 = new File(downloadFolderPath, "localhost_static_http_image.jpg");
        mockHttpServer = new MockHttpServer();
        mockHttpServer.start();
    }

    @Test
    public void downloadFile()
    {
        List<String> urlList = new ArrayList<>();

        urlList.add("http://localhost:8080/static/http_image.jpg");

        FileFetcher.downloadFilesFromUrls(urlList, downloadFolderPath);

        Assert.assertTrue("File Fetcher failed to download file : " + expectedOutputFile1.toString()
                , expectedOutputFile1.exists());
    }

    @After
    public void tearDown()
    {
        expectedOutputFile1.delete();
        mockHttpServer.stop();
    }
}