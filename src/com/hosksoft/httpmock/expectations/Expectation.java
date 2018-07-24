package com.hosksoft.httpmock.expectations;

import com.sun.net.httpserver.HttpExchange;


public interface Expectation {
	void compute(HttpExchange request);
}
