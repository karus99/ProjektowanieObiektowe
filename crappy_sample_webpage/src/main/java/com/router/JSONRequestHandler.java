package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class JSONRequestHandler extends RoutingHandler
{
    ArrayList<Route> routes;
    private static final Logger LOG = LogManager.getRootLogger();

    JSONRequestHandler(ArrayList<Route> routes)
    {
        super(routes);
    }

    public void handle(HttpExchange t) throws IOException
    {
        LOG.debug("JSON handler");

        String method = t.getRequestMethod();
        String path = t.getRequestURI().getPath();

        handleRequest(path, method, t);
    }
}