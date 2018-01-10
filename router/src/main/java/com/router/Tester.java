package com.router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Tester
{
    private static final Logger LOG = LogManager.getRootLogger();

    public static void main(String[] args) throws IOException
    {
        Routing routing = new Routing("routes.csv");
        routing.startServer(9000);
    }
}
