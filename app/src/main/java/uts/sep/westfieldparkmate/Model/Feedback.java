package uts.sep.westfieldparkmate.Model;

public class Feedback {

    private String feedback;
    private String user;

    public Feedback(String feedback, String user) {
        this.feedback = feedback;
        this.user = user;
    }

    public Feedback() {
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String toString()
    {
        return feedback + " " + user;
    }
}
