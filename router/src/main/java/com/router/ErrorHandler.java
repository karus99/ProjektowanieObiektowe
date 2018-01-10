package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ErrorHandler implements HttpHandler
{
    public void handle(HttpExchange t) throws IOException
    {
        HTTPTemplateBuilder tb = new HTTPTemplateBuilder("blank.json", "templates/404.html");
        String response = tb.build();
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}