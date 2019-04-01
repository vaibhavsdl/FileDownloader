package com.agoda.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtilityTest {

    List<String> fileUrlList;
    List<String> httpUrlList;
    List<String> ftpUrlList;

    Map<String, List<URI>> fileUrlsGroupMap;

    @Before
    public void setUp()
    {
        fileUrlList = new ArrayList<>();

        fileUrlList.add("ftp://speedtest.tele2.net/1MB.zip");
        fileUrlList.add("http://speedtest.ftp.otenet.gr/files/test1Mb.db");
        fileUrlList.add("http://speedtest.ftp.otenet.gr/files/test10Mb.db");

        fileUrlsGroupMap = FileUtility.groupFilesByProtocol(fileUrlList);
    }

    @Test
    public void testGroupCount()
    {
        Assert.assertTrue("Unexpected group count ", fileUrlsGroupMap.size() == 2);
    }

    @Test
    public void testProtocolIdentification()
    {
        Assert.assertTrue("http protocol group not created", fileUrlsGroupMap.containsKey("http"));

        Assert.assertTrue("ftp protocol group not created", fileUrlsGroupMap.containsKey("ftp"));
    }

    @Test
    public void testGroupContent()
    {
        Assert.assertTrue("http url count mismatch", fileUrlsGroupMap.get("http").size() == 2);
    }
}