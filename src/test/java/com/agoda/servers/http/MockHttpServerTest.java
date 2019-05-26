package com.agoda.servers.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MockHttpServerTest {

    MockHttpServer mockHttpServer = new MockHttpServer();

    @Before
    public void setUp() throws Exception {
        mockHttpServer.start();
    }

    @After
    public void tearDown() {
        mockHttpServer.stop();
    }

    @Test
    public void testDownload() throws IOException {
        URL url = new URL("http://localhost:8080/static/http_image.jpg");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        in.lines().forEach(System.out::println);
        in.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testFilenNotFound() throws IOException {
        URL url = new URL("http://localhost:8080/static/not_found");
        url.openStream();
    }
}