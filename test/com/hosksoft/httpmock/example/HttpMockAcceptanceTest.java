package com.hosksoft.httpmock.example;

import com.hosksoft.httpmock.HttpMock;
import com.hosksoft.httpmock.HttpSender.ResponseToClient;
import org.junit.Test;

import java.net.URL;

import static com.hosksoft.httpmock.HttpSender.send;
import static com.hosksoft.httpmock.Reply.reply;
import static com.hosksoft.httpmock.expectations.Expectations.*;
import static com.hosksoft.httpmock.http.Header.header;
import static com.hosksoft.httpmock.http.Method.GET;
import static com.hosksoft.httpmock.http.Method.POST;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HttpMockAcceptanceTest {

    @Test
    public void willFailWhenNotAPost() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(methodEq(GET))
                .replyWith(reply("{}", 200));

        httpMock.play();

        send(httpMock.url(), "{\"id\":\"1234567890\"}", POST);
        try {
            httpMock.validate();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(
                    "The following expectations were not met: \n" +
                            "Expected methods do not match.  Method expected was: GET, Actual method was: POST\n\n" ));
        }
    }

    @Test
    public void willPassWhenAGet() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(methodEq(GET))
                .replyWith(reply("success", 200));

        httpMock.play();

        final URL url = httpMock.url();
        final ResponseToClient response = send(url, "{\"id\":\"1234567890\"}", GET);

        httpMock.validate();
        assertThat(response.getBody(), is("success"));
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getUri().toString(), is(url.toString()));
    }

    @Test
    public void willFailWhenWrongPath() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(pathStartsWith("/green/brick/road"))
                .replyWith(reply("success", 200));

        httpMock.play();

        final URL url = new URL(httpMock.url().toString()+"/yellow/brick/road/with/the/tinman");
        send(url, "", GET);

        try {
            httpMock.validate();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(
                    "The following expectations were not met: \n" +
                            "Expected path was: /green/brick/road, Actual path was: /yellow/brick/road/with/the/tinman\n"));
        }
    }

    @Test
    public void willPassWhenCorrectPath() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(pathStartsWith("/yellow/brick/road"))
                .replyWith(reply("success", 200));

        httpMock.play();

        final URL url = new URL(httpMock.url().toString()+"/yellow/brick/road/with/the/tinman");
        final ResponseToClient response = send(url, "", GET);

        httpMock.validate();
        assertThat(response.getBody(), is("success"));
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getUri().toString(), is(url.toString()));
    }

    @Test
    public void willFailWhenWrongPayload() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(payloadEq("{\"\"}"))
                .replyWith(reply("{}", 200));

        httpMock.play();

        send(httpMock.url(), "{\"id\":\"1234567890\"}", POST);

        try {
            httpMock.validate();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), is(
                    "The following expectations were not met: \n" +
                            "Expected string was: {\"\"}, Actual string was: {\"id\":\"1234567890\"}\n"));
        }
    }

    @Test
    public void willPassWhenCorrectPayload() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(payloadEq("{\"id\":\"1234567890\"}"))
                .replyWith(reply("{\"success\":true}", 200));

        httpMock.play();

        final URL url = httpMock.url();
        final ResponseToClient response = send(url, "{\"id\":\"1234567890\"}", POST);

        httpMock.validate();
        assertThat(response.getBody(), is("{\"success\":true}"));
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getUri().toString(), is(url.toString()));
    }

    @Test
    public void willFailWhenWrongHeader() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(headersEq(header("Accept", "text/html", "text/plain")))
                .replyWith(reply("{}", 200));

        httpMock.play();

        send(httpMock.url(), "{\"id\":\"1234567890\"}", header("Accept", "application/json"), POST);

        try {
            httpMock.validate();
        } catch (AssertionError e) {
            assertThat(e.getMessage(), containsString(
                    "The following expectations were not met: \n" +
                            "Expected headers were Accept:text/html, Accept:text/plain, " +
                            "Actual headers were Accept:application/json"));
        }
    }

    @Test
    public void willPassWhenCorrectHeader() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(headersEq(header("Accept", "text/plain")))
                .replyWith(reply("success", 200));

        httpMock.play();

        final URL url = httpMock.url();
        ResponseToClient response = send(url, "anything", header("Accept", "text/plain"), POST);

        httpMock.validate();
        assertThat(response.getBody(), is("success"));
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getUri().toString(), is(url.toString()));
    }

    @Test
    public void willReturnWithASuccessfulReplyWhenEvokedProperly() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(payloadEq("{\"id\":\"1234567890\"}"))
                .and(pathStartsWith("/yellow/brick/road"))
                .and(methodEq(POST))
                .and(headersEq(header("Accept", "application/json")))
            .replyWith(reply("{\"success\":true}", 200));

        httpMock.play();

        final URL url = new URL(httpMock.url().toString()+"/yellow/brick/road/with/the/tinman");

        ResponseToClient response = send(url, "{\"id\":\"1234567890\"}", header("Accept", "application/json"), POST);
        httpMock.validate();
        assertThat(response.getBody(), is("{\"success\":true}"));
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getUri().toString(), is(url.toString()));
    }

    @Test
    public void willReturnTwoSuccessfulReplies() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(payloadEq("{\"id\":\"1234567890\"}"))
                .and(pathStartsWith("/yellow/brick/road"))
                .and(methodEq(POST))
                .and(headersEq(header("Accept", "application/json")))
            .replyWith(reply("{\"success\":true}", 200))
        .expects()
                .when(pathStartsWith("/red/brick/road"))
                .and(methodEq(GET))
                .and(headersEq(header("Accept", "text/plain")))
            .replyWith(reply("{\"success\":\"also_true\"}", 200));

        httpMock.play();

        final URL firstUrl = new URL(httpMock.url().toString()+"/yellow/brick/road/with/the/tinman");
        final URL secondUrl = new URL(httpMock.url().toString()+"/red/brick/road/with/the/lion");

        ResponseToClient firstResponse = send(firstUrl, "{\"id\":\"1234567890\"}", header("Accept", "application/json"), POST);
        ResponseToClient secondResponse = send(secondUrl, "{\"id\":\"0987654321\"}", header("Accept", "text/plain"), GET);

        httpMock.validate();

        assertThat(firstResponse.getBody(), is("{\"success\":true}"));
        assertThat(firstResponse.getStatusCode(), is(200));
        assertThat(firstResponse.getUri().toString(), is(firstUrl.toString()));

        assertThat(secondResponse.getBody(), is("{\"success\":\"also_true\"}"));
        assertThat(secondResponse.getStatusCode(), is(200));
        assertThat(secondResponse.getUri().toString(), is(secondUrl.toString()));

    }

    @Test
    public void willReturnFirstExpectationWhenTwoMatchersIdentical() throws Exception {
        HttpMock httpMock = new HttpMock();
        httpMock.expects()
                .when(payloadEq("{\"id\":\"1234567890\"}"))
            .replyWith(reply("{\"success\":true}", 200))
        .expects()
                .when(payloadEq("{\"id\":\"1234567890\"}"))
            .replyWith(reply("{\"success\":\"also_true\"}", 200));

        httpMock.play();

        final URL url = new URL(httpMock.url().toString()+"/yellow/brick/road/with/the/tinman");

        ResponseToClient response = send(url, "{\"id\":\"1234567890\"}", POST);

        assertThat(response.getBody(), is("{\"success\":true}"));
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getUri().toString(), is(url.toString()));
    }

}