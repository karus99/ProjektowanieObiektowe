package com.router;

import com.sun.net.httpserver.Headers; //NOSONAR
import com.sun.net.httpserver.HttpExchange; //NOSONAR
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class API
{
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String TYPE = "application/json; charset=%s";

    private static final Logger LOG = LogManager.getRootLogger();

    public void insertElement(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        InputStreamReader isr =
                new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();

        int key = -1;

        if(query != null)
        {
            Map<String, Object> parameters = parseQuery(query);

            Database db = Database.getInstance();

            Element newElement = new Element(parameters.get("name").toString(), Integer.parseInt(parameters.get("qty").toString()));
            key = db.insert(newElement);
        }

        String response;
        if(key != -1)
        {
            response = "{\"status\": \"OK\", \"key\": " + key + "}";
        }
        else
        {
            response = "{\"status\": \"ERROR\", \"message\": \"Failed to insert new element!\"}";
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void getAllElements(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        String response = "[";

        Database db = Database.getInstance();

        List<Element> elements = db.getAll();
        for(Element element: elements)
        {
            response += "{\"key\":" + element.getKey() +", \"name\":\"" + element.getName() +"\", \"qty\":" + element.getQuantity() + "},";
        }
        response = response.substring(0, response.length() - 1);
        response += "]";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void deleteElement(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        InputStreamReader isr =
                new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();

        if(query != null)
        {
            Map<String, Object> parameters = parseQuery(query);

            Database db = Database.getInstance();

            Element element = new Element(Integer.parseInt(parameters.get("key").toString()));
            db.delete(element);
        }

        String response = "{\"status\": \"OK\"}";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void updateElement(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        InputStreamReader isr =
                new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();

        if(query != null)
        {
            Map<String, Object> parameters = parseQuery(query);

            Database db = Database.getInstance();

            Element element = new Element(Integer.parseInt(parameters.get("key").toString()));
            element.setName(parameters.get("name").toString());
            element.setQuantity(Integer.parseInt(parameters.get("qty").toString()));
            db.update(element);
        }

        String response = "{\"status\": \"OK\"}";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void insertLogEntry(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        InputStreamReader isr =
                new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();

        if(query != null)
        {
            Map<String, Object> parameters = parseQuery(query);

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date today = Calendar.getInstance().getTime();
            String reportDate = df.format(today);

            String buffer = reportDate + " " + parameters.get("ip").toString() + "\r\n";
            Path file = Paths.get("logs/entry.txt");
            java.nio.file.Files.write(file, buffer.getBytes(), StandardOpenOption.APPEND);
        }

        String response = "{\"status\": \"OK\"}";

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void returnMessage(HttpExchange t) throws IOException
    {
        Headers headers = t.getResponseHeaders();
        headers.set(HEADER_CONTENT_TYPE, String.format(TYPE, CHARSET));

        InputStreamReader isr =
                new InputStreamReader(t.getRequestBody(),"utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();

        String response = "";
        if(query != null)
        {
            Map<String, Object> parameters = parseQuery(query);

            response = "{\"status\": \"OK\", \"message\": \"" + parameters.get("message").toString() + "\"}";
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseQuery(String query) throws UnsupportedEncodingException
    {
        Map<String, Object> parameters = new HashMap<>();

        if (query == null)
        {
            return parameters;
        }

        String[] pairs = query.split("[&]");

        for (String pair : pairs)
        {
            String[] param = pair.split("[=]");

            String key = null;
            String value = null;
            if (param.length > 0)
            {
                key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
            }

            if (param.length > 1)
            {
                value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
            }

            if (parameters.containsKey(key))
            {
                Object obj = parameters.get(key);
                if(obj instanceof List<?>)
                {
                    List<String> values = (List<String>)obj;
                    values.add(value);
                }
                else if(obj instanceof String)
                {
                    List<String> values = new ArrayList<>();
                    values.add((String)obj);
                    values.add(value);
                    parameters.put(key, values);
                }
            }
            else
            {
                parameters.put(key, value);
            }
        }

        return parameters;
    }
}