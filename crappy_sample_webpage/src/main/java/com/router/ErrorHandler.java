package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class ErrorHandler implements HttpHandler
{
    private static final Logger LOG = LogManager.getRootLogger();

    public void handle(HttpExchange t) throws IOException
    {
        LOG.debug("Error handler");
        HTTPTemplateBuilder tb = new HTTPTemplateBuilder("content/variables/404.json", "content/templates/404.html");
        String response = tb.build();

        t.sendResponseHeaders(404, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}