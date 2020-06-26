package model;

import java.io.Serializable;

public class Task implements Serializable {
    private String designation;
    private int maxQuestions;
    private int questionsDone;

    public Task(String designation, int maxQuestions) {
        this.designation = designation;
        this.maxQuestions = maxQuestions;
        questionsDone = 0;
    }

    public String getDesignation() {
        return designation;
    }

    public int getMaxQuestions() {
        return maxQuestions;
    }

    public int getQuestionsDone() {
        return questionsDone;
    }

    public void changeQuestionsDone(int numberQuestions) {
        if (questionsDone + numberQuestions < 0) {
            questionsDone = 0;
        } else if (questionsDone + numberQuestions > maxQuestions) {
            questionsDone = maxQuestions;
        } else {
            questionsDone += numberQuestions;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Task)) return false;
        Task t = (Task) obj;
        return t.designation.trim().equals(designation.trim());
    }

    @Override
    public String toString() {
        return String.format("%s, %d / %d", designation, questionsDone, maxQuestions);
    }
}
