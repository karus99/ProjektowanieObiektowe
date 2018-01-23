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

    String build(int decoratorType);
}

class HTTPTemplateBuilder implements TemplateBuilder
{
    static final int USE_NORMAL = 0;
    static final int USE_SIMPLE = 1;
    static final int USE_NONE = 2;

    private String configFileName = "";
    private String templateFileName = "";
    private HashMap<String, Object> values = null;

    HTTPTemplateBuilder(String configFileName, String templateFileName)
    {
        this.configFileName = configFileName;
        this.templateFileName = templateFileName;
    }

    HTTPTemplateBuilder(HashMap<String, Object> values, String templateFileName)
    {
        this.templateFileName = templateFileName;
        this.values = values;
    }

    public String build(int decoratorType)
    {
        if(values == null)
        {
            TemplateConfigReader tcr = new TemplateConfigReader(configFileName);
            values = tcr.getConfig();
        }

        Template t;

        switch(decoratorType)
        {
            case USE_NORMAL:
                t = new NormalTemplateDecorator(new HTTPTemplate(templateFileName, values), values);
                break;

            case USE_SIMPLE:
                t = new SimpleTemplateDecorator(new HTTPTemplate(templateFileName, values), values);
                break;

            case USE_NONE:
            default:
                t = new HTTPTemplate(templateFileName, values);
                break;
        }
        return t.returnTemplate();
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

    @Override
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
            if(value != null)
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
        if(filePath == null)
        {
            return new HashMap<>();
        }

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