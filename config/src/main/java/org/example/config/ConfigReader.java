package org.example.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class ConfigReader
{
    private String filePath = "";
    private static final Logger LOG = LogManager.getRootLogger();

    ConfigReader(String filePath)
    {
        this.filePath = filePath;
    }

    Config getConfig()
    {
        try
        {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(content);

            Config obj = new Config();
            obj.setDatabase((String) config.get("database"));
            obj.setUrl((String) config.get("url"));
            obj.setUsername((String) config.get("username"));
            obj.setPassword((String) config.get("password"));
            obj.setPort((Long) config.get("port"));

            return obj;
        }
        catch (IOException | ParseException e)
        {
            LOG.error(e.getMessage());
        }

        return null;
    }
}

class Config
{
    private String url = "";
    private long port = -1;
    private String username = "";
    private String password;
    private String database = "";

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public long getPort() {
        return port;
    }

    void setPort(long port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    void setDatabase(String database) {
        this.database = database;
    }

    String guessType()
    {
        if(url.length() <= 0)
        {
            return "sqlite";
        }

        if(port == 3306)
        {
            return "mysql";
        }

        if(port == 5432)
        {
            return "postgres";
        }

        return "undefined";
    }
}