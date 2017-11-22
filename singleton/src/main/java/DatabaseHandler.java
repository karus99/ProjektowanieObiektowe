import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler
{
    private static DatabaseHandler instance_sqlite = null;
    private static DatabaseHandler instance_mysql = null;
    private Connection connection = null;
    private Statement statement = null;

    protected DatabaseHandler(String type)
    {
        try
        {
            if(type.equals("sqlite"))
                connection = DriverManager.getConnection("jdbc:sqlite:src\\test.sqlite");
            else
                connection = DriverManager.getConnection("jdbc:mysql:src\\ADRES_DO_MYSQL");

            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        }
        catch (SQLException e)
        {
            System.out.println("ERROR: Cannot connect to SQL");
        }
    }

    public static DatabaseHandler getInstance(String type)
    {
        if(type.equals("sqlite"))
        {
            if(instance_sqlite == null)
            {
                instance_sqlite = new DatabaseHandler(type);
                return instance_sqlite;
            }
            return instance_sqlite;
        }
        else
        {
            if(instance_mysql == null)
            {
                instance_mysql = new DatabaseHandler(type);
                return instance_mysql;
            }
            return instance_mysql;
        }
    }

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

    public static void main(String args[])
    {
        DatabaseHandler db = DatabaseHandler.getInstance("sqlite");

        Student newStudent = new Student("Karol", "Drwila");
        newStudent.setKey(db.insert(newStudent));

        db.insert(new Student("Jan", "Kowalski"));
        db.insert(new Student("Anna", "Nowak"));

        System.out.println(db.getOne().toStringRaw());

        System.out.println("==== STUDENTS LIST ====");
        ArrayList<Student> students = db.getAll();
        for(Student student: students)
        {
            System.out.println(student.toStringRaw());
        }

        newStudent.setFirstName("Tobjorn");
        newStudent.setSurname("Rudobrody");

        db.update(newStudent);

        System.out.println("==== STUDENTS LIST ====");
        students = db.getAll();
        for(Student student: students)
        {
            System.out.println(student.toStringRaw());
        }

        db.delete(newStudent);

        System.out.println("==== STUDENTS LIST ====");
        students = db.getAll();
        for(Student student: students)
        {
            System.out.println(student.toStringRaw());

            db.delete(student);
        }
    }
}
