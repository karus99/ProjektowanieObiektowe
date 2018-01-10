package com.router;

import com.sun.net.httpserver.HttpServer; //NOSONAR

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;


/**
 * Created by kprzystalski on 13/12/2017.
 */
class Routing
{
    private ArrayList<Route> routes = null;

    Routing(String routesPath)
    {
        RoutesReader reader = new RoutesReader();
        routes = reader.readRoutingTable(routesPath);
    }

    void startServer(int port) throws IOException
    {
        MainHandler mainHandler = new MainHandler(routes);
        JSONRequestHandler jsonHandler = new JSONRequestHandler(routes);
        ErrorHandler errorHandler = new ErrorHandler();

        mainHandler.setNextHandler(jsonHandler);
        jsonHandler.setNextHandler(errorHandler);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", mainHandler);
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}