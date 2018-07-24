package com.hosksoft.httpmock.expectations;

import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.Description;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class PathStartsWithMatcher extends HttpExchangeMatcher {

    private final String expectedPath;
    private String actualPath;

    public PathStartsWithMatcher(String expectedPath) {
        this.expectedPath = expectedPath;
    }

    @Override
    protected boolean matchesSafely(HttpExchange httpExchange) {
        actualPath = httpExchange.getRequestURI().getPath();
        return actualPath.startsWith(expectedPath);
    }

    @Override
    public void describeTo(Description description) {
        String reply = "Expected path was: " + expectedPath;
        if (actualPath != null) {
            reply += ", Actual path was: " + actualPath;
        }
        description.appendText(reply);
    }

}
