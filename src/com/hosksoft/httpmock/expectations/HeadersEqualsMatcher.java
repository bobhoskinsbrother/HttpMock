package com.hosksoft.httpmock.expectations;

import com.hosksoft.httpmock.http.Header;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.Description;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class HeadersEqualsMatcher extends HttpExchangeMatcher {

    private final List<Header> expected;
    private List<Header> actuals;

    public HeadersEqualsMatcher(Header... headers) {
        this.expected = Arrays.asList(headers);
    }


    @Override
    protected boolean matchesSafely(HttpExchange exchange) {
        actuals = findAll(exchange);
        for (Header header : expected) {
            for (Header actual : actuals) {
                if(header.equals(actual)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void describeTo(Description description) {
        StringBuilder b = new StringBuilder();
        b.append("Expected headers were ");
        for (Header header : expected) {
            b.append(header);
            b.append(", ");
        }
        if (actuals != null) {
            b.append("Actual headers were ");

            for (Header actual : actuals) {
                b.append(actual);
                b.append(", ");
            }
        }
        description.appendText(b.toString());
    }

    private List<Header> findAll(HttpExchange exchange) {
        List<Header> reply = new LinkedList<>();
        Headers headers = exchange.getRequestHeaders();
        Iterator<String> headerNames = headers.keySet().iterator();
        headerNames.forEachRemaining(name -> reply.add(new Header(name, headers.get(name))));
        return reply;
    }
}
