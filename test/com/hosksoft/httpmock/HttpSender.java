package com.hosksoft.httpmock;

import com.hosksoft.httpmock.http.Header;
import com.hosksoft.httpmock.http.Method;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static com.hosksoft.httpmock.http.Method.GET;


public class HttpSender {


    public static class ResponseToClient {

        private final String body;
        private final int statusCode;
        private final URI uri;

        private ResponseToClient(String body, int statusCode, URI uri) {
            this.body = body;
            this.statusCode = statusCode;
            this.uri = uri;
        }

        public String getBody() {
            return body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public URI getUri() {
            return uri;
        }
    }

    public static ResponseToClient send(URL url, String payload, Method method) throws IOException, URISyntaxException {
        final HttpURLConnection connection = HttpURLConnection.class.cast(url.openConnection());
        final int responseCode;
        String reply;
        try {
            setDefaults(connection, payload, method);
            responseCode = connection.getResponseCode();
            reply = readReply(connection);
        } finally {
            connection.disconnect();
        }
        return new ResponseToClient(reply, responseCode, url.toURI());
    }

    public static ResponseToClient send(URL url, String payload, Header header, Method method) throws IOException, URISyntaxException {
        final HttpURLConnection connection = HttpURLConnection.class.cast(url.openConnection());
        final int responseCode;
        String reply;
        try {
            connection.setRequestProperty(header.name(), header.value());
            setDefaults(connection, payload, method);
            responseCode = connection.getResponseCode();
            reply = readReply(connection);
        } finally {
            connection.disconnect();
        }
        return new ResponseToClient(reply, responseCode, url.toURI());
    }

    private static String readReply(HttpURLConnection connection) throws IOException {
        final InputStream stream = connection.getInputStream();
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) > 0) {
            responseBody.write(buffer, 0, bytesRead);
        }
        return new String(responseBody.toByteArray());
    }

    private static void setDefaults(HttpURLConnection connection, String payload, Method method) throws IOException {
        connection.setRequestMethod(method.toString());
        if (method != GET && !"".equals(payload)) {
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
        }
    }

}
