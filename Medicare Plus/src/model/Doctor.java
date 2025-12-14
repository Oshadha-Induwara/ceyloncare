package model;

public class Doctor {
    private int id;
    private String name;
    private String speciality;
    private String available_time;

    public Doctor(int id, String name, String speciality, String available_time) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
        this.available_time = available_time;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAvailable_time() {
        return available_time;
    }

    public void setAvailable_time(String available_time) {
        this.available_time = available_time;
    }
}
