package model;

public class Patient {
    private int id;
    private String name;
    private String contact;
    private String medi_history;

    public Patient(int id, String name, String contact, String medi_history) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.medi_history = medi_history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMedi_history() {
        return medi_history;
    }

    public void setMedi_history(String medi_history) {
        this.medi_history = medi_history;
    }
}
