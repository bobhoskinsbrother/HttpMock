package com.hosksoft.httpmock.expectations;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestBodyEqualsMatcherTest {

    @Test
    public void doesEqual() {
        final RequestBodyEqualsMatcher unit = new RequestBodyEqualsMatcher("I am a body");

        HttpExchange exchange = withRequestBody("I am a body");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(true));
    }

    @Test
    public void doesNotEqual() {
        final RequestBodyEqualsMatcher unit = new RequestBodyEqualsMatcher("I am a body");

        HttpExchange exchange = withRequestBody("I am not your body");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(false));
    }









    private HttpExchange withRequestBody(final String body) {
        return new HttpExchange() {


            @Override
            public InputStream getRequestBody() {
                return new ByteArrayInputStream(body.getBytes());
            }

            @Override
            public Headers getRequestHeaders() {
                return null;
            }

            @Override
            public Headers getResponseHeaders() {
                return null;
            }

            @Override
            public URI getRequestURI() {
                return null;
            }

            @Override
            public String getRequestMethod() {
                return null;
            }

            @Override
            public HttpContext getHttpContext() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public OutputStream getResponseBody() {
                return null;
            }

            @Override
            public void sendResponseHeaders(int rCode, long responseLength) throws IOException {

            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public int getResponseCode() {
                return 0;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public Object getAttribute(String name) {
                return null;
            }

            @Override
            public void setAttribute(String name, Object value) {

            }

            @Override
            public void setStreams(InputStream i, OutputStream o) {

            }

            @Override
            public HttpPrincipal getPrincipal() {
                return null;
            }
        };
    }

}
