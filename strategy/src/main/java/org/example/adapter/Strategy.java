package org.example.adapter;

import java.util.List;

public class Strategy
{
    public static void main(String[] args)
    {
        //Database db = DatabaseAdapter.getInstance("sqlite");
        Database db = new Database();
        db.setDatabaseStrategy(new SQLiteDatabase());

        Student newStudent = new Student("Karol", "Drwila");
        newStudent.setKey(db.insert(newStudent));

        db.insert(new Student("Jan", "Kowalski"));
        db.insert(new Student("Anna", "Nowak"));

        System.out.println(db.getOne().toStringRaw());

        System.out.println("==== STUDENTS LIST ====");
        List<Student> students = db.getAll();
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
