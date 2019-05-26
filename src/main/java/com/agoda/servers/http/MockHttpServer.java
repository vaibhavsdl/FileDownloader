package com.agoda.servers.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import static com.agoda.constants.LoggingConstants.INFO_LOGGER;

public class MockHttpServer {

    private static final String BASEDIR = "src/test/resources";

    private static final int PORT = 8080;

    private HttpServer server;

    /*public static void main(String[] args) throws Exception {
        MockHttpServer server = new MockHttpServer();
        server.start();
    }*/

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/static", new StaticFileHandler(BASEDIR));

        server.start();
        INFO_LOGGER.info("http server started at localhost:" + PORT);
    }

    public void stop() {
        INFO_LOGGER.info("Stopping http server at localhost:" + PORT);
        server.stop(0);
    }
}