package com.hosksoft.httpmock.expectations;

import com.hosksoft.httpmock.http.Header;
import com.hosksoft.httpmock.http.Method;

import java.util.Collections;
import java.util.List;


public class Expectations {

    public static PathStartsWithMatcher pathStartsWith(String expected) {
        return new PathStartsWithMatcher(expected);
    }

    public static RequestBodyEqualsMatcher payloadEq(String expected) {
        return new RequestBodyEqualsMatcher(expected);
    }

    public static MethodEqualsMatcher methodEq(Method method) {
        return new MethodEqualsMatcher(method);
    }

    public static HeadersEqualsMatcher headersEq(Header... headers) {
        return new HeadersEqualsMatcher(headers);
    }

}
