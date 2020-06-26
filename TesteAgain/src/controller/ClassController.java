package controller;

import controller.App;

import model.Class;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class ClassController {
    private App app;

    public ClassController() {
        app = App.getInstance();
    }

    public String getDaysRemaining(Calendar date) {
        Calendar currentDate = Calendar.getInstance();
        long examInMilis = date.getTimeInMillis();
        long currentDateMilis = currentDate.getTimeInMillis();
        int days = (int) ((examInMilis - currentDateMilis) / (24 * 60 * 60 * 1000));
        long daysMilis = days * (24 * 60 * 60 * 1000);
        int hours = (int) (examInMilis - currentDateMilis - daysMilis) / (60 * 60 * 1000);
        long hoursMilis = hours * 60 * 60 * 1000;
        int minutes = (int) (examInMilis - currentDateMilis - daysMilis - hoursMilis) / (60 * 1000);
        long minutesMilis = minutes * 60 * 1000;
        int seconds = (int) (examInMilis - currentDateMilis - daysMilis - hoursMilis - minutesMilis) / 1000;
        return String.format("%d:%d:%d:%d", days, hours, minutes, seconds);
    }

    public List<Class> getClassList() {
        return app.getClassList();
    }

    public void addClass(LocalDate examDate, String hours, String mins, String name) {
        if (name.trim().isEmpty()) throw new IllegalArgumentException("Please fill the name");
        int hoursInt = Integer.parseInt(hours);
        int minsInt = Integer.parseInt(mins);
        if (hoursInt < 0 || hoursInt > 23 || minsInt < 0 || minsInt > 59)
            throw new IllegalArgumentException("Invalid hour!");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, examDate.getDayOfMonth());
        calendar.set(Calendar.MONTH, examDate.getMonthValue() - 1);
        calendar.set(Calendar.YEAR, examDate.getYear());
        calendar.set(Calendar.HOUR_OF_DAY, hoursInt);
        calendar.set(Calendar.MINUTE, minsInt);
        System.out.println(calendar.getTime());
        Class cl = new Class(calendar, name);
        if (app.getClassList().contains(cl)) throw new IllegalArgumentException("The class already exists");
        app.getClassList().add(cl);
    }

    public void removeClass(Class cl) {
        if (cl == null) throw new NullPointerException("Select class!");
        app.getClassList().remove(cl);
    }
}
