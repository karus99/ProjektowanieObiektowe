package org.example.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Adapter
{
    private static final Logger LOG = LogManager.getRootLogger();

    public static void main(String[] args)
    {
        ConfigReader configReader = new ConfigReader("test_sqlite.json");
        Config config = configReader.getConfig();

        Database db = DatabaseAdapter.getInstance(config);

        Student newStudent = new Student("Karol", "Drwila");
        newStudent.setKey(db.insert(newStudent));

        db.insert(new Student("Jan", "Kowalski"));
        db.insert(new Student("Anna", "Nowak"));

        LOG.debug(db.getOne().toStringRaw());

        LOG.debug("==== STUDENTS LIST ====");
        List<Student> students = db.getAll();
        for(Student student: students)
        {
            LOG.debug(student.toStringRaw());
        }

        newStudent.setFirstName("Tobjorn");
        newStudent.setSurname("Rudobrody");

        db.update(newStudent);

        LOG.debug("==== STUDENTS LIST ====");
        students = db.getAll();
        for(Student student: students)
        {
            LOG.debug(student.toStringRaw());
        }

        db.delete(newStudent);

        LOG.debug("==== STUDENTS LIST ====");
        students = db.getAll();
        for(Student student: students)
        {
            LOG.debug(student.toStringRaw());

            db.delete(student);
        }
    }
}
