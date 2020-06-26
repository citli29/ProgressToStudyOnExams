package controller;

import model.Class;
import model.Task;

import java.util.List;

public class TaskController {
    public List<Task> getTasks(Class selectedItem) {
        return selectedItem != null ? selectedItem.getTaskList() : null;
    }

    public void addTask(Class cl, String taskDesignation, int maxQuestions) {
        if (cl == null) throw new NullPointerException("Select class!");
        if (taskDesignation.trim().isEmpty()) throw new IllegalArgumentException("Please fill the name section!");
        if (maxQuestions < 0) throw new IllegalArgumentException("Invalid number of questions!");
        Task task = new Task(taskDesignation, maxQuestions);
        if (cl.getTaskList().contains(task)) throw new IllegalArgumentException("Task already exists!");
        cl.getTaskList().add(task);
    }

    public void doneExercises(Task task, int numberOfQuestionsDone) {
        if (task == null) throw new NullPointerException("Select task!");
        if (numberOfQuestionsDone < 0) throw new IllegalArgumentException("Invalid number of questions!");
        int before = task.getQuestionsDone();
        task.changeQuestionsDone(numberOfQuestionsDone);
        int after = task.getQuestionsDone();
        if (after != before) App.getInstance().addActivityToHistoric(task, after - before);
    }

    public void removeExercises(Task task, int numberOfQuestionsDone) {
        if (task == null) throw new NullPointerException("Select task!");
        if (numberOfQuestionsDone < 0) throw new IllegalArgumentException("Invalid number of questions!");
        int before = task.getQuestionsDone();
        task.changeQuestionsDone(-numberOfQuestionsDone);
        int after = task.getQuestionsDone();
        if (after != before) App.getInstance().addActivityToHistoric(task, after - before);
    }

    public void removeTask(Class cl, Task task) {
        if (cl == null) throw new NullPointerException("Select class!");
        if (task == null) throw new NullPointerException("Select task!");
        cl.getTaskList().remove(task);
    }
}
