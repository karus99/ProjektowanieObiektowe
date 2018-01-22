package com.router;

class Element
{

    private String name = "";
    private int qty = -1;
    private int key = -1;

    Element(String name, int qty)
    {
        this.name = name;
        this.qty = qty;
    }

    Element(int key)
    {
        this.key = key;
    }

    String getName()
    {
        if(name.equals(""))
        {
            getElementData();
        }

        return name;
    }

    void setFirstName(String name)
    {
        this.name = name;
    }

    int getQuantity()
    {
        if(qty == -1)
        {
            getElementData();
        }

        return qty;
    }

    void setSurname(int qty)
    {
        this.qty = qty;
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
        return getName() + " " + getQuantity();
    }

    String toStringRaw()
    {
        return "[KEY: " + getKey() + " | " + getName() + " " + getQuantity() + "]";
    }

    private void getElementData()
    {
        Database db = new Database();
        db.setDatabaseStrategy(new SQLiteDatabase());
        Element temp = db.getByKey(key);

        this.name = temp.getName();
        this.qty = temp.getQuantity();
    }
}
