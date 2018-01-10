package com.router;

import com.sun.net.httpserver.Headers; // NOSONAR
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestResponse
{
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String TYPE = "application/json; charset=%s";

    public void getTestPage(HttpExchange t) throws IOException
    {
        HTTPTemplateBuilder tb = new HTTPTemplateBuilder("test.json", "templates/test.html");
        String response = tb.build();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void postTestPage(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        String response = "{action: \"post\", value: \"test\", num_value: 1}";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void putTestPage(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        String response = "{action: \"put\", value: \"test\", num_value: 1}";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void deleteTestPage(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        String response = "{action: \"delete\", value: \"test\", num_value: 1}";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
