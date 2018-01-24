package com.router;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Files
{
    public void getFile(HttpExchange t) throws IOException
    {
        String path = t.getRequestURI().getPath();
        String[] pathArray = path.split("/");

        String response = "";

        switch(pathArray[1])
        {
            case "js":
                response = new String(java.nio.file.Files.readAllBytes(Paths.get("content/javascripts/" + pathArray[2])));
                break;

            case "css":
                response = new String(java.nio.file.Files.readAllBytes(Paths.get("content/stylesheets/" + pathArray[2])));
                break;

            case "logs":
                response = new String(java.nio.file.Files.readAllBytes(Paths.get("logs/" + pathArray[2])));
                break;
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
