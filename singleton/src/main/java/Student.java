class Student
{

    String firstName = "";
    String surname = "";
    int key = -1;

    Student(String firstName, String surname)
    {
        this.firstName = firstName;
        this.surname = surname;
    }

    Student(int key)
    {
        this.key = key;
    }

    public String getFirstName()
    {
        if(firstName.equals(""))
        {
            getStudentData();
        }

        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSurname()
    {
        if(surname.equals(""))
        {
            getStudentData();
        }

        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public String toString()
    {
        return getFirstName() + " " + getSurname();
    }

    public String toStringRaw()
    {
        return "[KEY: " + getKey() + " | " + getFirstName() + " " + getSurname() + "]";
    }

    private void getStudentData()
    {
        DatabaseHandler db = DatabaseHandler.getInstance("sqlite");
        Student temp = db.getByKey(key);

        this.firstName = temp.getFirstName();
        this.surname = temp.getSurname();
    }
}
