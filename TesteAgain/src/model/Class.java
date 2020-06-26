package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Class implements Serializable {
    private Calendar examDate;
    private String name;
    private List<Task> taskList;

    public Class(Calendar examDate, String name) {
        this.examDate = examDate;
        this.name = name;
        taskList = new ArrayList<>();
    }

    public Calendar getExamDate() {
        return examDate;
    }

    public String getName() {
        return name;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Class)) return false;
        Class t = (Class) obj;
        return t.getName().trim().equals(name.trim());
    }

    @Override
    public String toString() {
        return name;
    }
}
