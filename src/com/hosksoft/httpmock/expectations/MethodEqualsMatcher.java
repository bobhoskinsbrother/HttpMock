package com.hosksoft.httpmock.expectations;

import com.hosksoft.httpmock.http.Method;
import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.Description;


public class MethodEqualsMatcher extends HttpExchangeMatcher {

    private final Method method;
    private String actualMethod;

    public MethodEqualsMatcher(Method method) {
        this.method = method;
    }

    @Override
    protected boolean matchesSafely(HttpExchange httpExchange) {
        actualMethod = httpExchange.getRequestMethod();
        return method.name().equalsIgnoreCase(actualMethod);
    }

    @Override
    public void describeTo(Description description) {
        String reply = "Expected methods do not match.  " +
                "Method expected was: " +
                method;
        if (actualMethod != null) {
            reply += ", Actual method was: " + actualMethod + "\n";
        }
        description.appendText(reply);
    }
}
