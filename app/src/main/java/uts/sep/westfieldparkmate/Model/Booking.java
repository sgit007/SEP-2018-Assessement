package uts.sep.westfieldparkmate.Model;

public class Booking {

    private String uid;
    private String pid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Booking(String uid, String pid) {
        this.uid = uid;
        this.pid = pid;
    }

    public Booking() {
    }
}
