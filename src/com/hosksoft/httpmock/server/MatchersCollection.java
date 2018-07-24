package com.hosksoft.httpmock.server;

import com.hosksoft.httpmock.expectations.HttpExchangeMatcher;
import com.sun.net.httpserver.HttpExchange;
import org.hamcrest.Description;

import java.util.LinkedList;


public class MatchersCollection extends HttpExchangeMatcher {

	private LinkedList<HttpExchangeMatcher> expectations;

	public MatchersCollection() {
		this.expectations = new LinkedList<>();
	}

	@Override
	protected boolean matchesSafely(HttpExchange httpExchange) {
		for (HttpExchangeMatcher expectation : expectations) {
			if(!expectation.matches(httpExchange)){
				return false;
			}
		}
		return true;
	}

	public void add(HttpExchangeMatcher expectation) {
		expectations.add(expectation);
	}

	public boolean isEmpty() {
		return expectations.isEmpty();
	}
	
	@Override
	public void describeTo(Description description) {
		StringBuilder b = new StringBuilder();
		for (HttpExchangeMatcher expectation : expectations) {
			b.append(expectation.toString());
		}
		description.appendText(b.toString());
	}
}
