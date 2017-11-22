import java.sql.*;
import java.util.ArrayList;

interface DatabaseType
{
    Connection connect();
}

class SQLiteDatabase implements DatabaseType
{
    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:src\\test.sqlite");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return connection;
    }
}

class MySQLDatabase implements DatabaseType
{
    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return connection;
    }
}

class PostgresDatabase implements DatabaseType
{
    public Connection connect()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return connection;
    }
}

interface Database
{
    int insert(Student student);
    void delete(Student student);
    Student getByKey(int key);
    ArrayList<Student> getAll();
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

    DatabaseAdapter(String type)
    {
        this.type = type;
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
            e.printStackTrace();
        }
    }

    public static DatabaseAdapter getInstance(String type_)
    {
        if(instance == null || !type.equals(type_))
        {
            instance = new DatabaseAdapter(type_);
        }

        return instance;
    };

    public int insert(Student student)
    {
        int id = -1;
        try
        {
            statement.executeUpdate("INSERT INTO students (firstname, surname) " +
                    "VALUES ('" + student.getFirstName() + "', '" + student.getSurname() + "')");

            ResultSet data = statement.executeQuery("SELECT MAX(key) as highest FROM students");
            while(data.next())
            {
                id = data.getInt("highest");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public Student getByKey(int key)
    {
        Student result = null;
        try
        {
            ResultSet data = statement.executeQuery("SELECT firstName, surname FROM students WHERE key = " + key);

            while(data.next())
            {
                result = new Student(data.getString("firstName"), data.getString("surname"));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<Student> getAll()
    {
        ArrayList<Student> results = new ArrayList();

        try
        {
            ResultSet data = statement.executeQuery("SELECT key FROM students");

            while(data.next())
            {
                Student temp = new Student(data.getInt("key"));
                results.add(temp);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return results;
    }

    public Student getOne()
    {
        Student result = null;
        try
        {
            ResultSet data = statement.executeQuery("SELECT key FROM students ORDER BY key DESC LIMIT 1");

            if(data.next())
            {
                result = new Student(data.getInt("key"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public void update(Student student)
    {
        if(student.getKey() == -1)
        {
            System.out.println("ERROR: Student wasn't inserted into database!");
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
            e.printStackTrace();
        }
    }
}
