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
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
