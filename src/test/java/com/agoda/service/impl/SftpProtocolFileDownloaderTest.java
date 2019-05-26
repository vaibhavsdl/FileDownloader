package com.agoda.service.impl;

import com.agoda.model.Credential;
//import com.agoda.servers.CustomSftpServer;
import com.github.stefanbirkner.fakesftpserver.rule.FakeSftpServerRule;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class SftpProtocolFileDownloaderTest
{

    File downloadFile;

    @Rule
    public final FakeSftpServerRule sftpServer = new FakeSftpServerRule()
            .setPort(2222)
            .addUser("user", "password");

    @Test
    public void downloadFile() throws IOException
    {
        InputStream is = getClass().getResourceAsStream("/log4j2.xml");
        sftpServer.putFile("/directory/file.bin", is);
        is.close();

        SftpProtocolFileDownloader sftpProtocolFileDownloader = new SftpProtocolFileDownloader();

        try {
            URI uri = new URI("sftp://user@localhost:2222/directory/file.bin");

            Credential credential = new Credential("user", "password");

            downloadFile = sftpProtocolFileDownloader.downloadFile("/tmp/", uri, credential);

            Assert.assertTrue("Unable to download file : " + downloadFile.toString(), downloadFile.exists());
        }
        catch (Exception e)
        {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void tearDown()
    {
        downloadFile.delete();
    }
}