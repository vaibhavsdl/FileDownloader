package com.agoda.service.impl;

import com.agoda.model.Credential;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;


public class SftpProtocolFileDownloaderTest {

    @Test
    public void downloadFile()
    {
        SftpProtocolFileDownloader sftpProtocolFileDownloader = new SftpProtocolFileDownloader();

        try {
            URI uri = new URI("sftp://demo@test.rebex.net/pub/example/WinFormClient.png");

            Credential credential = new Credential("demo", "password");

            File file = sftpProtocolFileDownloader.downloadFile("/tmp/", uri, credential);

            Assert.assertTrue("Unable to download file : " + file.toString(), file.exists());
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }
}