package controller;

import model.Class;
import model.Task;

import java.util.List;

public class ProgressController {
    private App app;
    private List<Class> classList;

    public ProgressController() {
        app = App.getInstance();
        classList = app.getClassList();
    }

    public double getTotalProgress() {
        double total = 0;
        double done = 0;
        for (Class c : classList) {
            for (Task t : c.getTaskList()) {
                total += t.getMaxQuestions();
                done += t.getQuestionsDone();
            }
        }
        if (total == 0) return 1;
        return done / total;
    }

    public double getClassProgress(Class cl) {
        double total = 0;
        double done = 0;
        for (Task t : cl.getTaskList()) {
            total += t.getMaxQuestions();
            done += t.getQuestionsDone();
        }
        if (total == 0) return 1;
        return done / total;
    }

    public double getToDoProgress(Task task) {
        if (task.getMaxQuestions() == 0) return 1;
        return (double) task.getQuestionsDone() / task.getMaxQuestions();
    }

    public List<String> getHistoric(boolean taskHistoric) {
        if (taskHistoric) return App.getInstance().getHistoricTask();
        else return App.getInstance().getHistoricDays();
    }
}
