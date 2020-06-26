package sample;

import controller.App;
import controller.ClassController;
import controller.ProgressController;
import controller.TaskController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Task;
import model.Class;


import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class Controller implements Initializable {
    @FXML
    private TextField hours;
    @FXML
    private TextField mins;
    @FXML
    private Spinner spMaxQuestions;
    @FXML
    private TextField txtTaskDesignation;
    @FXML
    private Spinner spExDones;
    @FXML
    private ProgressBar taskProgress;
    @FXML
    private ListView<Task> lstTasks;
    @FXML
    private ComboBox<Class> cmbClasses;
    @FXML
    private ListView<String> lstHistoric;
    @FXML
    private ProgressBar totalProgress;
    @FXML
    private TableView<Class> tblClasses;
    @FXML
    private TableColumn<Class, String> clExamDate;
    @FXML
    private TableColumn<Class, String> clDaysRemaining;
    @FXML
    private TableColumn<Class, String> clClassName;
    @FXML
    private TableColumn<Class, String> clPerHDone;
    @FXML
    private TextField txtClass;
    @FXML
    private DatePicker txtDatePicker;
    @FXML
    private ProgressBar classProgress;
    private ProgressController progressController;
    private TaskController taskController;
    private ClassController classController;
    private Class classSelected;
    private Timer skrt;
    private boolean taskHistoric;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressController = new ProgressController();
        taskController = new TaskController();
        classController = new ClassController();
        classSelected = null;
        clExamDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Class, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Class, String> classStringCellDataFeatures) {
                Calendar date = classStringCellDataFeatures.getValue().getExamDate();
                return new SimpleStringProperty(String.format("%d-%d-%d", date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR)));
            }
        });
        clDaysRemaining.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Class, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Class, String> classStringCellDataFeatures) {
                return new SimpleStringProperty((classController.getDaysRemaining(classStringCellDataFeatures.getValue().getExamDate())));
            }
        });
        clClassName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Class, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Class, String> classStringCellDataFeatures) {
                return new SimpleStringProperty(classStringCellDataFeatures.getValue().getName());
            }
        });
        clPerHDone.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Class, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Class, String> classStringCellDataFeatures) {
                return new SimpleStringProperty(String.format("%.2f", 100 * progressController.getClassProgress(classStringCellDataFeatures.getValue())));
            }
        });
        lstTasks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                taskProgress.setProgress(newValue != null ? progressController.getToDoProgress(newValue) : 0);
            }
        });

        Main.getTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                tblClasses.getItems().setAll(classController.getClassList());
            }
        }, 0, 1000);
        taskHistoric = true;
        clear();
        updateClasses();
        updateHistoric();
    }

    private void updateHistoric() {
        lstHistoric.getItems().setAll(progressController.getHistoric(this.taskHistoric));
    }

    private void updateTasks() {
        if (classSelected != null) {
            lstTasks.getSelectionModel().clearSelection();
            lstTasks.getItems().setAll(taskController.getTasks(classSelected));
            classProgress.setProgress(progressController.getClassProgress(classSelected));
            lstTasks.getItems().setAll(taskController.getTasks(classSelected));
        } else {
            lstTasks.getItems().clear();
        }
    }

    private void updateClasses() {
        totalProgress.setProgress(progressController.getTotalProgress());
        tblClasses.getItems().setAll(classController.getClassList());
        cmbClasses.getItems().setAll(classController.getClassList());
    }

    @FXML
    public void actSelectClass(ActionEvent actionEvent) {
        classSelected = cmbClasses.getValue();
        classProgress.setProgress(progressController.getClassProgress(classSelected));
        updateTasks();
        clear();
    }

    public void actAddTask(ActionEvent actionEvent) {
        try {
            taskController.addTask(classSelected, txtTaskDesignation.getText(), Integer.parseInt(spMaxQuestions.getEditor().getText()));
            updateTasks();
            updateClasses();
            clear();
        } catch (IllegalArgumentException | NullPointerException e) {
            Util.createAlert(Alert.AlertType.ERROR, Main.APP_NAME, "Error", e.getMessage()).show();
        }
    }

    public void actRemoveTask(ActionEvent actionEvent) {
        try {
            taskController.removeTask(classSelected, lstTasks.getSelectionModel().getSelectedItem());
            updateTasks();
            updateClasses();
            clear();
        } catch (NullPointerException e) {
            Util.createAlert(Alert.AlertType.ERROR, Main.APP_NAME, "Error", e.getMessage()).show();
        }
    }

    public void actDoneExercise(ActionEvent actionEvent) {
        try {
            taskController.doneExercises(lstTasks.getSelectionModel().getSelectedItem(), Integer.parseInt(spExDones.getEditor().getText()));
            updateHistoric();
            updateClasses();
            updateTasks();
            clear();
        } catch (IllegalArgumentException | NullPointerException e) {
            Util.createAlert(Alert.AlertType.ERROR, Main.APP_NAME, "Error", e.getMessage()).show();
        }
    }

    public void actRemoveExercise(ActionEvent actionEvent) {
        try {
            taskController.removeExercises(lstTasks.getSelectionModel().getSelectedItem(), Integer.parseInt(spExDones.getEditor().getText()));
            updateHistoric();
            updateClasses();
            updateTasks();
            clear();
        } catch (NullPointerException e) {
            Util.createAlert(Alert.AlertType.ERROR, Main.APP_NAME, "Error", e.getMessage()).show();
        }
    }


    public void actRemoveClass(ActionEvent actionEvent) {
        try {
            if (classSelected == tblClasses.getSelectionModel().getSelectedItem()) classSelected = null;
            classController.removeClass(tblClasses.getSelectionModel().getSelectedItem());
            clear();
            updateClasses();
            updateTasks();
        } catch (NullPointerException e) {
            Util.createAlert(Alert.AlertType.ERROR, Main.APP_NAME, "Error", e.getMessage()).show();
        }
    }

    public void actAddClass(ActionEvent actionEvent) {
        try {
            if (txtDatePicker.getEditor().getText().trim().isEmpty())
                throw new IllegalArgumentException("Please fill the date");
            classController.addClass(txtDatePicker.getValue(), hours.getText(), mins.getText(), txtClass.getText());
            updateClasses();
            updateTasks();
            clear();
        } catch (IllegalArgumentException e) {
            Util.createAlert(Alert.AlertType.ERROR, Main.APP_NAME, "Error", e.getMessage()).show();
        }
    }


    public void clear() {
        hours.clear();
        mins.clear();
        txtClass.clear();
        txtDatePicker.getEditor().clear();
        txtTaskDesignation.clear();
        spExDones.getValueFactory().setValue(0);
        spMaxQuestions.getValueFactory().setValue(1);
    }


    public void actClose(ActionEvent actionEvent) {
        Window window = lstTasks.getScene().getWindow();
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void actClear(ActionEvent actionEvent) {
        Alert alert = Util.createAlert(Alert.AlertType.CONFIRMATION, Main.APP_NAME, "Are you sure?", "You really sure??????");
        if (alert.showAndWait().get() == ButtonType.OK) {
            App.getInstance().clear();
            classSelected = null;
            progressController = new ProgressController();
            classController = new ClassController();
            taskController = new TaskController();
            clear();
            updateTasks();
            updateClasses();
            updateHistoric();
        }
    }

    public void SeeDaysProgress(ActionEvent actionEvent) {
        taskHistoric = false;
        updateHistoric();
    }

    public void SeeTaskProgress(ActionEvent actionEvent) {
        taskHistoric = true;
        updateHistoric();
    }
}
