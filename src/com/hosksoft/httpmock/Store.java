package com.hosksoft.httpmock;

import com.hosksoft.httpmock.expectations.HttpExchangeMatcher;
import com.hosksoft.httpmock.server.MatchersCollection;


public class Store {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private MatchersCollection withs;
	private Reply reply;
	private boolean actioned = false;
	private HttpMock parent;

	public Store(HttpMock parent) {
		this.parent = parent;
		this.withs = new MatchersCollection();
	}

	public Store and(HttpExchangeMatcher matcher) {
		return when(matcher);
	}


	public Store when(HttpExchangeMatcher matcher) {
		withs.add(matcher);
		return this;
	}

	public HttpMock replyWith(Reply reply) {
		this.reply = reply;
		return parent;
	}

	public Reply getReply() {
		return reply;
	}
	
	public MatchersCollection getMatchers() {
		return withs;
	}
	
	public void setActioned(boolean actioned) {
		this.actioned = actioned;
	}
	
	public boolean isActioned() {
		return actioned;
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("The following expectations were not met: ");
		b.append(LINE_SEPARATOR);
		if(!withs.isEmpty()) {
			b.append(withs.toString());
			b.append(LINE_SEPARATOR);
		}
		return b.toString();
	}
}