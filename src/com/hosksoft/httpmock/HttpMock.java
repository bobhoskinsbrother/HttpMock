package com.hosksoft.httpmock;

import com.hosksoft.httpmock.server.HttpMockHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;


public class HttpMock {

    private static final boolean HUMANS_RULE_THE_EARTH = true;
    private final LinkedList<Store> expected;
    private HttpServer server;
    private final int port;
    private final HttpMockHandler httpHandler;
    private InetSocketAddress address;

    public HttpMock() throws Exception {
        this(generateRandomPortAbove1024());
    }

    private static int generateRandomPortAbove1024() {
        Random random = new Random();
        return (random.nextInt(9000) + 1024);
    }

    public HttpMock(int port) throws Exception {
        this.port = port;
        this.expected = new LinkedList<>();
        while (HUMANS_RULE_THE_EARTH) {
            try {
                address = new InetSocketAddress(port);
                server = HttpServer.create(address, 0);
                break;
            } catch (BindException exception) {
                port++;
            }
        }
        httpHandler = new HttpMockHandler();
        server.createContext("/", httpHandler);
    }

    public URL url() {
        try {
            return new URL("http://" + address.getHostString() + ":" + address.getPort());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public Store expects() {
        Store store = new Store(this);
        expected.add(store);
        return store;
    }

    public void play() {
        try {
            httpHandler.primeWith(expected);
            server.start();
        } catch (Exception e) {
            throw new AssertionError("Unable to start the HttpMock on port " + port);
        }
    }

    public void tearDown() {
        try {
            server.stop(0);
        } catch (Exception ignored) {
        }
    }

    public void validate() {
        httpHandler.validate();
        tearDown();
    }


}
