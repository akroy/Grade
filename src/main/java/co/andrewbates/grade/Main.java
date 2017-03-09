package co.andrewbates.grade;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static final StudentController students = new StudentController();
    public static GradePreferences preferences;

    @Override
    public void start(Stage primaryStage) throws Exception {
        preferences = new GradePreferences(GradePreferences.dataDirectory().resolve("preferences.properties").toFile());
        preferences.bindWindow("Main", primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/andrewbates/grade/fxml/Main.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
