package app;

import entities.Student;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import network.Network;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

public class Main extends Application {

    private final ObservableList<Student> studentsObservableList =
            FXCollections.observableArrayList();

    private int selectedIndex = -1;
    private TextField textFieldName = new TextField();
    private TextField textFieldSurname = new TextField();
    private Network network = new Network();
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private final Scene scene = new Scene(new Group());
    private Parent previousScreen;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Student list");
        stage.setWidth(650);
        stage.setHeight(550);

        progressIndicator.setPadding(new Insets(100, 100, 100, 100));
        progressIndicator.setCenterShape(true);

        Label nameLabel = new Label("Student list");
        nameLabel.setFont(new Font("Arial", 20));

        textFieldName.setPromptText("Name");
        textFieldSurname.setPromptText("Surname");

        ListView<Student> myListView = new ListView<>();
        myListView.setItems(studentsObservableList);

        myListView.setOnMouseClicked(event -> {
            Student selectedItem = myListView.getSelectionModel().getSelectedItem();
            selectedIndex = myListView.getSelectionModel().getSelectedIndex();
            updateEditableData(selectedItem);
        });

        HBox myHBox = new HBox();
        myHBox.getChildren().addAll(textFieldName, textFieldSurname);
        setupButtons(myHBox);
        myHBox.setSpacing(3);

        VBox myVBox = new VBox();
        myVBox.setSpacing(5);
        myVBox.setPadding(new Insets(10, 0, 0, 10));
        myVBox.getChildren().addAll(nameLabel, myListView, myHBox);

        ((Group) scene.getRoot()).getChildren().addAll(myVBox);

        stage.setScene(scene);
        stage.show();
        loadAllStudents();
    }

    private void updateEditableData(Student student) {
        textFieldName.setText(student.getName());
        textFieldSurname.setText(student.getSurname());
    }

    private void clearEditableData() {
        textFieldName.clear();
        textFieldSurname.clear();
    }

    private Student createStudentFromEditableData(Long oldId) {
        return new Student(
                oldId,
                textFieldName.getText(),
                textFieldSurname.getText()
        );
    }

    private void showProgress() {
        previousScreen = scene.getRoot();
        scene.setRoot(progressIndicator);
    }

    private void hideProgress() {
        if (previousScreen != null) {
            scene.setRoot(previousScreen);
        }
    }

    private void showError(Throwable throwable) {
        new Alert(Alert.AlertType.ERROR, throwable.getMessage()).show();
    }

    private void loadAllStudents() {
        network.getAllStudents()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .doOnSubscribe(it -> showProgress())
                .doFinally(this::hideProgress)
                .subscribe(students -> {
                    studentsObservableList.addAll(students);
                    clearEditableData();
                }, throwable -> {
                    showError(throwable);
                });
    }

    private void setupButtons(HBox hBox) {
        Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            network.createStudent(textFieldName.getText(), textFieldSurname.getText())
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doOnSubscribe(it -> showProgress())
                    .doFinally(this::hideProgress)
                    .subscribe(student -> {
                        studentsObservableList.add(student);
                        clearEditableData();
                    }, throwable -> {
                        showError(throwable);
                    });
        });
        Button updateBtn = new Button("Update");
        updateBtn.setOnAction((ActionEvent e) -> {
            network.updateStudent(createStudentFromEditableData(
                    studentsObservableList.get(selectedIndex).getId()
            ))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doOnSubscribe(it -> showProgress())
                    .doFinally(this::hideProgress)
                    .subscribe(student -> {
                        studentsObservableList.remove(selectedIndex);
                        studentsObservableList.add(selectedIndex, student);
                        clearEditableData();
                    }, throwable -> {
                        showError(throwable);
                    });
        });
        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction((ActionEvent e) -> {
            network.deleteStudent(studentsObservableList.get(selectedIndex))
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .doOnSubscribe(it -> showProgress())
                    .doFinally(this::hideProgress)
                    .subscribe(() -> {
                        studentsObservableList.remove(selectedIndex);
                        clearEditableData();
                    }, throwable -> {
                        showError(throwable);
                    });
        });
        hBox.getChildren().addAll(addButton, updateBtn, deleteBtn);

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}