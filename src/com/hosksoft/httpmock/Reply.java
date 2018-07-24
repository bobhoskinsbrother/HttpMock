package com.hosksoft.httpmock;

public class Reply {
    private String body;
    private int statusCode;

    private Reply(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }

    public static Reply reply(String body, int statusCode) {
        return new Reply(body, statusCode);
    }


    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
