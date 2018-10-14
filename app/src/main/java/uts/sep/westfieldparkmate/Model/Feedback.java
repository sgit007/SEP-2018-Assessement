package uts.sep.westfieldparkmate.Model;

public class Feedback {

    private String feeekback;
    private String user;

    public Feedback(String feeekback, String user) {
        this.feeekback = feeekback;
        this.user = user;
    }

    public Feedback() {
    }

    public String getFeeekback() {
        return feeekback;
    }

    public void setFeeekback(String feeekback) {
        this.feeekback = feeekback;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
