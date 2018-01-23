package com.router;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class MainPage
{

    public void getIndex(HttpExchange t) throws IOException
    {
        TemplateConfigReader tcr = new TemplateConfigReader("content/variables/index.json");
        HashMap<String, Object> values = tcr.getConfig();

        Database db = Database.getInstance();

        String elementsString = "";
        List<Element> elements = db.getAll();
        boolean isFirst = true;

        for(Element element: elements)
        {
            if(isFirst)
            {
                isFirst = false;
                values.put("GET4_FIRST_KEY", String.valueOf(element.getKey()));
            }

            elementsString += "<option value=\"" + element.getKey() +"\">" + element.getName() + "</option>";
        }
        values.put("GET4_ELEMENTS", elementsString);
        values.put("DELETE1_ELEMENTS", elementsString);

        HTTPTemplateBuilder tb = new HTTPTemplateBuilder(values, "content/templates/index.html");
        String response = tb.build(HTTPTemplateBuilder.USE_NORMAL);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void getAllElements(HttpExchange t) throws IOException
    {
        TemplateConfigReader tcr = new TemplateConfigReader("content/variables/all.json");
        HashMap<String, Object> values = tcr.getConfig();

        Database db = Database.getInstance();

        String elementsString = "";
        List<Element> elements = db.getAll();
        for(Element element: elements)
        {
            HashMap<String, Object> innerValues = new HashMap<>();
            innerValues.put("KEY", String.valueOf(element.getKey()));
            innerValues.put("NAME", element.getName());
            innerValues.put("QTY", String.valueOf(element.getQuantity()));

            HTTPTemplateBuilder tb = new HTTPTemplateBuilder(innerValues, "content/templates/all_element.html");
            elementsString += tb.build(HTTPTemplateBuilder.USE_NONE);
        }

        values.put("ELEMENTS", elementsString);

        HTTPTemplateBuilder tb = new HTTPTemplateBuilder(values, "content/templates/all.html");
        String response = tb.build(HTTPTemplateBuilder.USE_NORMAL);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void getFirstElement(HttpExchange t) throws IOException
    {
        TemplateConfigReader tcr = new TemplateConfigReader("content/variables/first.json");
        HashMap<String, Object> values = tcr.getConfig();

        Database db = Database.getInstance();

        Element element = db.getFirst();

        values.put("KEY", String.valueOf(element.getKey()));
        values.put("NAME", element.getName());
        values.put("QTY", String.valueOf(element.getQuantity()));

        HTTPTemplateBuilder tb = new HTTPTemplateBuilder(values, "content/templates/first.html");
        String response = tb.build(HTTPTemplateBuilder.USE_NORMAL);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public void getElement(HttpExchange t) throws IOException
    {
        TemplateConfigReader tcr = new TemplateConfigReader("content/variables/element.json");
        HashMap<String, Object> values = tcr.getConfig();

        Database db = Database.getInstance();

        String path = t.getRequestURI().getPath();
        int key = Integer.parseInt(path.substring(9));

        Element element = db.getByKey(key);

        values.put("TITLE", element.getName() + values.get("TITLE"));
        values.put("KEY", String.valueOf(key));
        values.put("NAME", element.getName());
        values.put("QTY", String.valueOf(element.getQuantity()));

        HTTPTemplateBuilder tb = new HTTPTemplateBuilder(values, "content/templates/element.html");
        String response = tb.build(HTTPTemplateBuilder.USE_NORMAL);
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
