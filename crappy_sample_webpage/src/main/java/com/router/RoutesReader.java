package com.router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class RoutesReader
{
    private static final Logger LOG = LogManager.getRootLogger();

    ArrayList<Route> readRoutingTable(String filePath)
    {
        ArrayList<Route> result = new ArrayList<>();
        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            while ((line = br.readLine()) != null)
            {
                String[] route = line.split(",");
                result.add(new Route(route[1], route[0], route[2], route[3]));
            }
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage());
        }

        return result;
    }
}
