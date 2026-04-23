package com.progresionnacion;

import com.progresionnacion.controller.MainController;
import com.progresionnacion.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("Progresión de marcas en natación");
        DatabaseManager.initializeDatabase();
        showLoginView();
        primaryStage.show();
    }

    public static void showLoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/progresionnacion/login-view.fxml"));
        Scene scene = new Scene(loader.load(), 500, 340);
        scene.getStylesheets().add(App.class.getResource("/com/progresionnacion/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
    }

    public static void showMainView(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/progresionnacion/main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1400, 860);
        scene.getStylesheets().add(App.class.getResource("/com/progresionnacion/styles.css").toExternalForm());

        MainController controller = loader.getController();
        controller.setLoggedUser(user);

        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
