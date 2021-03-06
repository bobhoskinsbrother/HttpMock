package com.hosksoft.httpmock.expectations;

import com.hosksoft.httpmock.http.Method;
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
import java.net.URISyntaxException;

import static com.hosksoft.httpmock.http.Method.GET;
import static com.hosksoft.httpmock.http.Method.POST;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PathStartsWithMatcherTest {

    @Test
    public void doesEqualWhenPathsAreIdentical() {
        final PathStartsWithMatcher unit = new PathStartsWithMatcher("/path/to/success");

        HttpExchange exchange = withPath("/path/to/success");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(true));
    }

    @Test
    public void doesEqualWhenActualPathIsMore() {
        final PathStartsWithMatcher unit = new PathStartsWithMatcher("/path/to/success");

        HttpExchange exchange = withPath("/path/to/success/is/fraught/with/peril");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(true));
    }

    @Test
    public void doesNotEqualWhenActualPathIsDifferent() {
        final PathStartsWithMatcher unit = new PathStartsWithMatcher("/path/to/success");

        HttpExchange exchange = withPath("/path/to/failure/is/not/always/clear");
        boolean reply = unit.matchesSafely(exchange);
        assertThat(reply, is(false));
    }










    private HttpExchange withPath(final String path) {
        return new HttpExchange() {
            @Override
            public URI getRequestURI() {

                try {
                    return new URI(path);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
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
