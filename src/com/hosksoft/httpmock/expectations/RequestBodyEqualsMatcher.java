package com.hosksoft.httpmock.expectations;

import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.Description;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class RequestBodyEqualsMatcher extends HttpExchangeMatcher {

    private final String expected;
    private String actual;

    public RequestBodyEqualsMatcher(String expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(HttpExchange httpExchange) {
        actual = readAll(httpExchange.getRequestBody());
        if (expected == null || !expected.equals(actual)) {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        String reply = "Expected string was: " + expected;
        if (actual != null) {
            reply += ", Actual string was: " + actual;
        }
        description.appendText(reply);
    }

    private String readAll(InputStream requestBody) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(requestBody, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return textBuilder.toString();
    }

}
