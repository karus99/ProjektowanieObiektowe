package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainHandler extends RoutingHandler
{
    MainHandler(ArrayList<Route> routes)
    {
        super(routes);
    }

    @Override
    public void handle(HttpExchange t) throws IOException
    {
        String method = t.getRequestMethod();
        String path = t.getRequestURI().getPath();

        if(!Objects.equals(method, "GET"))
        {
            this.chain.handle(t);
            return;
        }

        handleRequest(path, method, t);
    }
}