package com.hosksoft.httpmock.expectations;

import com.hosksoft.httpmock.http.Header;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HeadersEqualsMatcherTest {

    @Test
    public void doesEqual() {
        final HeadersEqualsMatcher unit = new HeadersEqualsMatcher(new Header("Kopff", "Fuss"));

        HttpExchange exchange = withResponseHeader("Kopff", "Fuss");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(true));
    }

    @Test
    public void doesNotEqual() {
        final HeadersEqualsMatcher unit = new HeadersEqualsMatcher(new Header("Kopff", "Fuss"));

        HttpExchange exchange = withResponseHeader("Kopff", "Hand");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(false));
    }

    @Test
    public void doesEqualWhenTwoExpectations() {
        final HeadersEqualsMatcher unit = new HeadersEqualsMatcher(new Header("Kopff", "Fuss"), new Header("Kopff", "Hand"));

        HttpExchange exchange = withResponseHeader("Kopff", "Hand");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(true));
    }









    private HttpExchange withResponseHeader(final String name, final String value) {
        return new HttpExchange() {
            @Override
            public Headers getRequestHeaders() {
                final Headers headers = new Headers();
                headers.add(name, value);
                return headers;
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
            public InputStream getRequestBody() {
                return null;
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
