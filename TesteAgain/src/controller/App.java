package controller;

import javafx.util.Pair;
import model.Class;
import model.Task;

import java.io.*;
import java.util.*;

public class App {
    private final List<Class> classList;
    private final List<Pair<String, Calendar>> historicApp;

    private App() {
        Object[] p = readObjects();
        if (p == null) {
            classList = new ArrayList<>();
            historicApp = new ArrayList<>();
        } else {
            classList = (List<Class>) p[0];
            historicApp = (List<Pair<String, Calendar>>) p[1];
        }
    }


    public List<Class> getClassList() {
        return classList;
    }

    private static App singleton = null;

    public static App getInstance() {
        if (singleton == null) {
            singleton = new App();
        }
        return singleton;
    }

    public List<String> getHistoricTask() {
        List<String> temp = new ArrayList<>();
        Collections.sort(historicApp, byDate.reversed());
        for (Pair<String, Calendar> p : historicApp) {
            temp.add(String.format("%s, %s:%s  %d-%d-%d ;", p.getKey(), p.getValue().get(Calendar.HOUR_OF_DAY) > 10 ? Integer.toString(p.getValue().get(Calendar.HOUR_OF_DAY)) : "0" + p.getValue().get(Calendar.HOUR),
                    p.getValue().get(Calendar.MINUTE) > 10 ? Integer.toString(p.getValue().get(Calendar.MINUTE)) : "0" + p.getValue().get(Calendar.MINUTE), p.getValue().get(Calendar.DAY_OF_MONTH), p.getValue().get(Calendar.MONTH), p.getValue().get(Calendar.YEAR)));
        }
        return temp;
    }


    Comparator<Pair<String, Calendar>> byDate = new Comparator<Pair<String, Calendar>>() {
        @Override
        public int compare(Pair<String, Calendar> o1, Pair<String, Calendar> o2) {
            return o1.getValue().getTime().compareTo(o2.getValue().getTime());
        }
    };


    public void addActivityToHistoric(Task task, int numberOfQuestionsDone) {
        Calendar calendar = Calendar.getInstance();
        historicApp.add(new Pair<String, Calendar>(String.format("%s: %d", task.getDesignation(), numberOfQuestionsDone), calendar));
    }

    public void clear() {
        singleton = null;
        File skrt = new File("app.dat");
        if(skrt.exists())skrt.delete();
    }


    private Object[] readObjects() {
        Object[] p = new Object[2];
        try {
            FileInputStream in = new FileInputStream("app.dat");
            ObjectInputStream ois = new ObjectInputStream(in);
            try {
                p[0] = ois.readObject();
                p[1] = ois.readObject();
                return p;
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            } finally {
                ois.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void close() {
        try {
            FileOutputStream out = new FileOutputStream("app.dat");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(classList);
            oos.writeObject(historicApp);
            oos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getHistoricDays() {
        List<String> temp1 = new ArrayList<>();
        Collections.sort(historicApp, byDate.reversed());
        String date = "";
        int total = 0;
        int totalDone = 0;
        if (!historicApp.isEmpty()) date = historicApp.get(0).getValue().get(Calendar.DAY_OF_MONTH) + "-" +
                historicApp.get(0).getValue().get(Calendar.MONTH) + "-" +
                historicApp.get(0).getValue().get(Calendar.YEAR);
        for (Pair<String, Calendar> p : historicApp) {
            Calendar calendar = p.getValue();
            String datePair = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
            if (date.equals(datePair)) {
                totalDone += Integer.parseInt(p.getKey().split(":")[1].trim());
            } else {
                total += totalDone;
                temp1.add(String.format("%s , %d;", date, totalDone));
                date = datePair;
                totalDone = Integer.parseInt(p.getKey().split(":")[1].trim());
            }
        }
        total += totalDone;
        temp1.add(String.format("%s , %d;", date, totalDone));
        List<String> temp = new ArrayList<>();
        for (String s : temp1) {
            String[] splited = s.split("[,;]");
            String graph = "[";
            int totalDay = Integer.parseInt(splited[1].trim());
            int perCentage = (int) (((double) totalDay / total)*33);
            if(perCentage<0) perCentage =0;
            if (perCentage>33) perCentage = 33;
            for (int i = 0; i < perCentage ; i++) {
                graph += "+";
            }
            for (int i = 0; i < 33 - perCentage; i++) {
                graph += "...";
            }
            graph += "]";
            temp.add(String.format("%s, Impact: %.2f%s\n%s", splited[0], ((double) totalDay / total) * 100, "%", graph));
        }
        return temp;
    }
}
