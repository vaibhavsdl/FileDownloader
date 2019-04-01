package com.agoda.service.impl;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;


public class FtpProtocolFileDownloaderTest {

    @Test
    public void downloadFile()
    {
        FtpProtocolFileDownloader ftpProtocolFileDownloader = new FtpProtocolFileDownloader();

        try {
            URI uri = new URI("ftp://ftp.cs.brown.edu/pub/aard.sol.tar.Z");

            File file = ftpProtocolFileDownloader.downloadFile("/tmp/", uri);

            Assert.assertTrue("Unable to download file : " + file.toString(), file.exists());
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}