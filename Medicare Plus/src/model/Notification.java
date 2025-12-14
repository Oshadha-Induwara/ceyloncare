package model;

public class Notification {
    private int id;
    private int patientId;
    private String message;
    private String date;
    private boolean sent;

    public Notification(int id, int patientId, String message, String date, boolean sent) {
        this.id = id;
        this.patientId = patientId;
        this.message = message;
        this.date = date;
        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
