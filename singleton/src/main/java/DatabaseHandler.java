import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler
{
    private static DatabaseHandler instance = null;
    private Connection connection = null;
    private Statement statement = null;

    protected DatabaseHandler()
    {
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\_karus\\coding\\java\\projektowanie\\src\\test.sqlite");
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        }
        catch (SQLException e)
        {
            System.out.println("SQL nie działa");
        }
    }

    public static DatabaseHandler getInstance()
    {
        if(instance == null)
        {
            instance = new DatabaseHandler();
        }

        return instance;
    }

    public void insertStudent(Student student)
    {
        try
        {
            statement.executeUpdate("INSERT INTO studenci (imie) VALUES ('" + student.getName() + "')");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void removeStudent(Student student)
    {
        try
        {
            statement.executeUpdate("DELETE FROM studenci WHERE imie = '" + student.getName() + "'");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Student> getAllStudents()
    {
        ResultSet data = null;
        ArrayList<Student> results = new ArrayList();

        try
        {
            data = statement.executeQuery("SELECT * FROM studenci");

            while(data.next())
            {
                Student temp = new Student(data.getString("imie"));
                results.add(temp);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return results;
    }

    public static void main(String args[])
    {
        DatabaseHandler db = DatabaseHandler.getInstance();

        Student newStudent = new Student("Karol");
        db.insertStudent(newStudent);

        ArrayList<Student> students = db.getAllStudents();
        for(Student student: students)
        {
            System.out.println("Imię: " + student.getName());

            if(student.getName().equals("Karol"))
            {
                db.removeStudent(student);
            }
        }

    }
}
