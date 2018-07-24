package com.hosksoft.httpmock.server;

import com.hosksoft.httpmock.Reply;
import com.hosksoft.httpmock.Store;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Objects;

import static com.hosksoft.httpmock.Reply.reply;

public class HttpMockHandler implements HttpHandler {

    private LinkedList<Store> stores = new LinkedList<>();


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (stores.isEmpty()) {
            writeResponse(exchange, reply("", 500));
        }
        for (Store store : stores) {
            if (!store.isActioned()) {
                MatchersCollection expectations = store.getMatchers();
                final boolean actioned = expectations.matchesSafely(exchange);
                store.setActioned(actioned);
                Reply reply = store.getReply();
                writeResponse(exchange, Objects.requireNonNullElseGet(reply, () -> reply("", 200)));
            }
        }
    }

    private void writeResponse(HttpExchange exchange, Reply reply) throws IOException {
        byte[] bytes = reply.getBody().getBytes();
        exchange.sendResponseHeaders(reply.getStatusCode(), bytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(bytes);
        outputStream.close();
    }

    public void primeWith(LinkedList<Store> expected) {
        this.stores.addAll(expected);
    }

    public void validate() {
        boolean failed = false;
        StringBuilder b = new StringBuilder();
        for (Store expectedStore : stores) {
            if (!expectedStore.isActioned()) {
                b.append(expectedStore.toString());
                failed = true;
            }
        }
        if (failed) {
            throw new AssertionError(b.toString());
        }
    }
}
