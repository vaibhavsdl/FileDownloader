package com.agoda.fetcher;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileFetcherTest {

    File expectedOutputFile1;
    File expectedOutputFile2;
    String downloadFolderPath = "/tmp";

    @Before
    public void setUp()
    {
        expectedOutputFile1 = new File(downloadFolderPath, "techslides_com_demos_samples_sample.pdf");

        expectedOutputFile2 = new File(downloadFolderPath, "speedtest_tele2_net_1Mb.zip");
    }

    @Test
    public void downloadFilesFromUrls()
    {
        List<String> urlList = new ArrayList<>();

        urlList.add("ftp://speedtest.tele2.net/1MB.zip");
        urlList.add("http://techslides.com/demos/samples/sample.pdf");

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
    }
}