package com.agoda.service.impl;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;

public class HttpProtocolFileDownloaderTest {

    @Test
    public void downloadFile()
    {
        HttpProtocolFileDownloader httpProtocolFileDownloader = new HttpProtocolFileDownloader();

        try {
            URI uri = new URI("http://speedtest.ftp.otenet.gr/files/test10Mb.db");

            File file = httpProtocolFileDownloader.downloadFile("/tmp/", uri);

            Assert.assertTrue("Unable to download file : " + file.toString(), file.exists());
        }
        catch (Exception e)
        {
            Assert.fail(String.valueOf(e.getStackTrace()));
        }
    }
}