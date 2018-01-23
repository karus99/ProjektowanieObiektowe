package com.router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class TemplateDecorator implements Template
{
    Template decoratedTemplate;
    HashMap<String, Object> values;

    TemplateDecorator(Template decoratedTemplate, HashMap<String, Object> values)
    {
        this.decoratedTemplate = decoratedTemplate;
        this.values = values;
    }

    public String returnTemplate()
    {
        return decoratedTemplate.returnTemplate();
    }
}

class NormalTemplateDecorator extends TemplateDecorator
{
    private String headerContent = "";
    private String footerContent = "";
    private static final Logger LOG = LogManager.getRootLogger();

    public NormalTemplateDecorator(Template decoratedTemplate, HashMap<String, Object> values)
    {
        super(decoratedTemplate, values);
    }

    @Override
    public String returnTemplate()
    {
        try
        {
            headerContent = new String(Files.readAllBytes(Paths.get("content/templates/header.html")));
            footerContent = new String(Files.readAllBytes(Paths.get("content/templates/footer.html")));
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage());
        }

        TemplateEngine te = new TemplateEngine(headerContent, values);
        headerContent = te.parseTemplate();

        te = new TemplateEngine(footerContent, values);
        footerContent = te.parseTemplate();

        return headerContent + decoratedTemplate.returnTemplate() + footerContent;
    }
}

class SimpleTemplateDecorator extends TemplateDecorator
{
    private String headerContent = "";
    private String footerContent = "";
    private static final Logger LOG = LogManager.getRootLogger();

    public SimpleTemplateDecorator(Template decoratedTemplate, HashMap<String, Object> values)
    {
        super(decoratedTemplate, values);
    }

    @Override
    public String returnTemplate()
    {
        try
        {
            headerContent = new String(Files.readAllBytes(Paths.get("content/templates/simple_header.html")));
            footerContent = new String(Files.readAllBytes(Paths.get("content/templates/simple_footer.html")));
        }
        catch (IOException e)
        {
            LOG.error(e.getMessage());
        }

        TemplateEngine te = new TemplateEngine(headerContent, values);
        headerContent = te.parseTemplate();

        te = new TemplateEngine(footerContent, values);
        footerContent = te.parseTemplate();

        return headerContent + decoratedTemplate.returnTemplate() + footerContent;
    }
}