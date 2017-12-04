package org.example.adapter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

interface DatabaseType
{
    Connection connect();
}

class SQLiteDatabase implements DatabaseType
{
    private static final Logger LOG = LogManager.getRootLogger();

    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:src\\test.sqlite");
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }

        return connection;
    }
}

class MySQLDatabase implements DatabaseType
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

class PostgresDatabase implements DatabaseType
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

interface Database
{
    int insert(Student student);
    void delete(Student student);
    Student getByKey(int key);
    List<Student> getAll();
    Student getOne();
    void update(Student student);
}

public class DatabaseAdapter implements Database
{
    static String type;
    DatabaseType dbInstance;
    Connection connection;
    Statement statement;
    static DatabaseAdapter instance = null;
    private static final Logger LOG = LogManager.getRootLogger();

    DatabaseAdapter(String type)
    {
        DatabaseAdapter.setType(type);
        if(type.equals("sqlite"))
        {
            dbInstance = new SQLiteDatabase();
            connection = dbInstance.connect();
        }
        else if(type.equals("mysql"))
        {
            dbInstance = new MySQLDatabase();
            connection = dbInstance.connect();
        }
        else if(type.equals("postgres"))
        {
            dbInstance = new PostgresDatabase();
            connection = dbInstance.connect();
        }

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

    public static void setType(String newType)
    {
        type = newType;
    }

    public static DatabaseAdapter getInstance(String newType)
    {
        if(instance == null || !type.equals(newType))
        {
            instance = new DatabaseAdapter(newType);
        }

        return instance;
    }

    public int insert(Student student)
    {
        ResultSet data = null;

        int id = -1;
        try
        {
            statement.executeUpdate("INSERT INTO students (firstname, surname) " +
                    "VALUES ('" + student.getFirstName() + "', '" + student.getSurname() + "')");

            data = statement.executeQuery("SELECT MAX(key) as highest FROM students");
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

    public void delete(Student student)
    {
        try
        {
            statement.executeUpdate("DELETE FROM students WHERE key = '" + student.getKey() + "'");
        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage());
        }
    }

    public Student getByKey(int key)
    {
        ResultSet data = null;
        Student result = null;
        PreparedStatement pstmt = null;
        try
        {
            String query = "SELECT firstName, surname FROM students WHERE key = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, key);  // Compliant; PreparedStatements escape their inputs.
            data = pstmt.executeQuery();

            while(data.next())
            {
                result = new Student(data.getString("firstName"), data.getString("surname"));
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

    public List<Student> getAll()
    {
        ResultSet data = null;
        ArrayList<Student> results = new ArrayList();

        try
        {
            data = statement.executeQuery("SELECT key FROM students");

            while(data.next())
            {
                Student temp = new Student(data.getInt("key"));
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

    public Student getOne()
    {
        ResultSet data = null;
        Student result = null;
        try
        {
            data = statement.executeQuery("SELECT key FROM students ORDER BY key DESC LIMIT 1");

            if(data.next())
            {
                result = new Student(data.getInt("key"));
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

    public void update(Student student)
    {
        if(student.getKey() == -1)
        {
            LOG.error("ERROR: Student wasn't inserted into database!");
            return;
        }

        try
        {
            statement.executeUpdate("UPDATE students " +
                    "SET firstName = '" + student.getFirstName() +"', surname = '" + student.getSurname() +"'" +
                    "WHERE key = " + student.getKey());
        }
        catch(SQLException e)
        {
            LOG.error(e.getMessage());
        }
    }
}
