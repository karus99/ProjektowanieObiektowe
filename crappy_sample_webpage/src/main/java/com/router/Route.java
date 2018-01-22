package com.router;

import com.sun.net.httpserver.HttpExchange; //NOSONAR
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

class Route
{
    String path;
    String method;
    Method handler;
    Object object;
    private static final Logger LOG = LogManager.getRootLogger();

    Route(String path, String method, String className, String methodName)
    {
        this.path = path;
        this.method = method;

        Class<?> c;
        try
        {
            c = Class.forName("com.router." + className);
            this.object = c.newInstance();
            this.handler = c.getDeclaredMethod(methodName, HttpExchange.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException e)
        {
            LOG.error(e.getMessage());
        }
    }
}