package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainHandler extends RoutingHandler
{
    private static final Logger LOG = LogManager.getRootLogger();

    MainHandler(ArrayList<Route> routes)
    {
        super(routes);
    }

    @Override
    public void handle(HttpExchange t) throws IOException
    {
        LOG.debug("Main handler");
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