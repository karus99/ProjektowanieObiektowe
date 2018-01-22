package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR
import com.sun.net.httpserver.HttpHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

interface IRoutingHandler extends HttpHandler
{
    void setNextHandler(HttpHandler next);
}

public abstract class RoutingHandler implements IRoutingHandler
{
    private ArrayList<Route> routes;
    HttpHandler chain;
    private static final Logger LOG = LogManager.getRootLogger();

    RoutingHandler(ArrayList<Route> routes)
    {
        this.routes = routes;
    }

    public void setNextHandler(HttpHandler next)
    {
        this.chain = next;
    }

    void handleRequest(String path, String method, HttpExchange t) throws IOException
    {
        LOG.debug(method + ", " + path);

        for(Route route : routes)
        {
            if(path.matches(route.path) && Objects.equals(method, route.method))
            {
                try
                {
                    route.handler.invoke(route.object, t);
                    return;
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    LOG.error("Routing Handler, handleRequest", e);
                }
            }
        }

        LOG.debug("not found in routing table");

        this.chain.handle(t);
    }
}
