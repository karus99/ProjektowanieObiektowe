package org.example.config;

class Student
{

    private String firstName = "";
    private String surname = "";
    private int key = -1;

    Student(String firstName, String surname)
    {
        this.firstName = firstName;
        this.surname = surname;
    }

    Student(int key)
    {
        this.key = key;
    }

    String getFirstName()
    {
        if(firstName.equals(""))
        {
            getStudentData();
        }

        return firstName;
    }

    void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    String getSurname()
    {
        if(surname.equals(""))
        {
            getStudentData();
        }

        return surname;
    }

    void setSurname(String surname)
    {
        this.surname = surname;
    }

    int getKey()
    {
        return key;
    }

    void setKey(int key)
    {
        this.key = key;
    }

    public String toString()
    {
        return getFirstName() + " " + getSurname();
    }

    String toStringRaw()
    {
        return "[KEY: " + getKey() + " | " + getFirstName() + " " + getSurname() + "]";
    }

    private void getStudentData()
    {
        ConfigReader configReader = new ConfigReader("test_sqlite.json");
        Config config = configReader.getConfig();
        DatabaseAdapter db = DatabaseAdapter.getInstance(config);
        Student temp = db.getByKey(key);

        this.firstName = temp.getFirstName();
        this.surname = temp.getSurname();
    }
}
