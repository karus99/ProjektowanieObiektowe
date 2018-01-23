package com.router;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface DatabaseStrategy
{
    Connection connect();
}

class SQLiteDatabase implements DatabaseStrategy
{
    private static final Logger LOG = LogManager.getRootLogger();

    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\_karus\\coding\\java\\projektowanie_obiektowe\\crappy_sample_webpage\\src\\database.sqlite");
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }

        return connection;
    }
}

class MySQLDatabase implements DatabaseStrategy
{
    private static final Logger LOG = LogManager.getRootLogger();

    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306");
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }

        return connection;
    }
}

class PostgresDatabase implements DatabaseStrategy
{
    private static final Logger LOG = LogManager.getRootLogger();

    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres");
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }

        return connection;
    }
}

class Database
{
    public static Database instance = null;
    private DatabaseStrategy strategy;
    Connection connection;
    Statement statement;
    private static final Logger LOG = LogManager.getRootLogger();

    public static Database getInstance()
    {
        if(instance == null)
        {
            instance = new Database();
        }

        return instance;
    }

    public void setDatabaseStrategy(DatabaseStrategy strategy)
    {
        this.strategy = strategy;
        connection = this.strategy.connect();

        try
        {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
    }

    public int insert(Element element)
    {
        ResultSet data = null;

        int id = -1;
        try
        {
            statement.executeUpdate("INSERT INTO elements (name, qty) " +
                    "VALUES ('" + element.getName() + "', '" + element.getQuantity() + "')");

            data = statement.executeQuery("SELECT MAX(key) as highest FROM elements");
            while(data.next())
            {
                id = data.getInt("highest");
            }
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
        finally
        {
            try
            {
                if(data != null)
                    data.close();
            }
            catch(SQLException e)
            {
                LOG.error(e.getMessage());
            }
        }

        return id;
    }

    public void delete(Element element)
    {
        try
        {
            statement.executeUpdate("DELETE FROM elements WHERE key = '" + element.getKey() + "'");
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
    }

    public Element getByKey(int key)
    {
        ResultSet data = null;
        Element result = null;
        PreparedStatement pstmt = null;
        try
        {
            String query = "SELECT name, qty FROM elements WHERE key = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, key);  // Compliant; PreparedStatements escape their inputs.
            data = pstmt.executeQuery();

            while(data.next())
            {
                result = new Element(data.getString("name"), data.getInt("qty"));
                result.setKey(key);
            }
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }
        finally
        {
            try
            {
                if(data != null)
                    data.close();
            }
            catch(SQLException e)
            {
                LOG.error(e.getMessage());
            }

            try
            {
                if(pstmt != null)
                    pstmt.close();
            }
            catch(SQLException e)
            {
                LOG.error(e.getMessage());
            }
        }

        return result;
    }

    public List<Element> getAll()
    {
        ResultSet data = null;
        ArrayList<Element> results = new ArrayList();

        try
        {
            data = statement.executeQuery("SELECT key FROM elements");

            while(data.next())
            {
                Element temp = new Element(data.getInt("key"));
                results.add(temp);
            }
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
        finally
        {
            try
            {
                if (data != null)
                {
                    data.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error(e.getMessage());
            }
        }

        return results;
    }

    public Element getOne()
    {
        ResultSet data = null;
        Element result = null;
        try
        {
            data = statement.executeQuery("SELECT key FROM elements ORDER BY key DESC LIMIT 1");

            if(data.next())
            {
                result = new Element(data.getInt("key"));
            }
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
        finally
        {
            try
            {
                if (data != null)
                {
                    data.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error(e.getMessage());
            }
        }

        return result;
    }

    public Element getFirst()
    {
        ResultSet data = null;
        Element result = null;
        try
        {
            data = statement.executeQuery("SELECT key FROM elements ORDER BY key ASC LIMIT 1");

            if(data.next())
            {
                result = new Element(data.getInt("key"));
            }
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
        finally
        {
            try
            {
                if (data != null)
                {
                    data.close();
                }
            }
            catch (SQLException e)
            {
                LOG.error(e.getMessage());
            }
        }

        return result;
    }

    public void update(Element element)
    {
        if(element.getKey() == -1)
        {
            LOG.error("ERROR: Element wasn't inserted into database!");
            return;
        }

        try
        {
            statement.executeUpdate("UPDATE elements " +
                    "SET name = '" + element.getName() +"', qty = '" + element.getQuantity() +"'" +
                    "WHERE key = " + element.getKey());
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }
    }
}