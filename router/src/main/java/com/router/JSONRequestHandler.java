package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR

import java.io.IOException;
import java.util.ArrayList;

public class JSONRequestHandler extends RoutingHandler
{
    ArrayList<Route> routes;

    JSONRequestHandler(ArrayList<Route> routes)
    {
        super(routes);
    }

    public void handle(HttpExchange t) throws IOException
    {
        String method = t.getRequestMethod();
        String path = t.getRequestURI().getPath();

        handleRequest(path, method, t);
    }
}