package com.router;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MainPage
{
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String TYPE = "application/json; charset=%s";

    public void getIndex(HttpExchange t) throws IOException
    {
        HTTPTemplateBuilder tb = new HTTPTemplateBuilder("content/variables/index.json", "content/templates/index.html");
        String response = tb.build();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void getAllElements(HttpExchange t) throws IOException
    {
        HTTPTemplateBuilder tb = new HTTPTemplateBuilder("content/variables/all.json", "content/templates/all.html");
        String response = tb.build();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
