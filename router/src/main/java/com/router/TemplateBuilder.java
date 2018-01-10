package com.router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface TemplateBuilder
{
    String build();
}

class HTTPTemplateBuilder implements TemplateBuilder
{
    private String configFileName = "";
    private String templateFileName = "";

    HTTPTemplateBuilder(String configFileName, String templateFileName)
    {
        this.configFileName = configFileName;
        this.templateFileName = templateFileName;
    }

    public String build()
    {
        TemplateConfigReader tcr = new TemplateConfigReader(configFileName);
        HashMap<String, Object> values = tcr.getConfig();

        HTTPTemplate ht = new HTTPTemplate(templateFileName, values);
        return ht.returnTemplate();
    }
}

interface Template
{
    String returnTemplate();
}

class HTTPTemplate implements Template
{
    private String templateContent;
    private static final Logger LOG = LogManager.getRootLogger();

    HTTPTemplate(String templateFileName, HashMap<String, Object> values)
    {
        try
        {
            templateContent = new String(Files.readAllBytes(Paths.get(templateFileName)));
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage());
        }

        TemplateEngine te = new TemplateEngine(templateContent, values);
        templateContent = te.parseTemplate();
    }

    public String returnTemplate()
    {
        return templateContent;
    }
}

class TemplateEngine
{
    private String templateContent;
    private HashMap<String, Object> values;

    TemplateEngine(String templateContent, HashMap<String, Object> values)
    {
        this.templateContent = templateContent;
        this.values = values;
    }

    private List<String> getVariables()
    {
        ArrayList<String> results = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher m = pattern.matcher(templateContent);
        while (m.find())
        {
            results.add(m.group(1));
        }

        return results;
    }

    String parseTemplate()
    {
        List<String> variables = getVariables();

        for (Object key : variables)
        {
            String value = (String) values.get(key);
            templateContent = templateContent.replace("{{" + key + "}}", value);
        }

        return templateContent;
    }
}

class TemplateConfigReader
{
    private String filePath = "";
    private static final Logger LOG = LogManager.getRootLogger();

    TemplateConfigReader(String filePath)
    {
        this.filePath = filePath;
    }

    HashMap<String, Object> getConfig()
    {
        try
        {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(content);

            HashMap<String, Object> result = new HashMap<>();

            for (Object key : config.keySet()) //NOSONAR
                result.put((String) key, config.get(key));

            return result;
        }
        catch (IOException | ParseException e)
        {
            LOG.error(e.getMessage());
        }

        return null;
    }
}