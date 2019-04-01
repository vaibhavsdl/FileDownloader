package com.agoda.util;

import com.agoda.constants.ApplicationConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class StringUtilityTest {

    @Test
    public void splitStringToList()
    {
        List<String> urlList = new ArrayList<>();
        urlList.add("http://www.google.com/test1.html");
        urlList.add("http://www.google.com/test2.html");

        String urlString = "http://www.google.com/test1.html, http://www.google.com/test2.html";

        Assert.assertEquals(StringUtility.splitStringToList(urlString, ApplicationConstants.URL_SPLIT_CHAR), urlList);
    }

    @Test
    public void cleanPathString()
    {
        String cleanUrl = "http_www_google_com_test1_html";
        Assert.assertEquals("clean string not matching", cleanUrl,
                StringUtility.cleanPathString("http://www.google.com/test1.html"));
    }

    @Test
    public void cleanFileNameString()
    {
        String cleanUrl = "folder1_folder2_test1.html";
        Assert.assertEquals("clean file namestring not matching", cleanUrl,
                StringUtility.cleanFileNameString("folder1/folder2/test1.html"));
    }
}