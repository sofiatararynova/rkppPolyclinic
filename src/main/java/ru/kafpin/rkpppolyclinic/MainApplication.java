package ru.kafpin.rkpppolyclinic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class MainApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        logger.info("Приложение запущено");

        while (true) {
            Optional<LoginResult> result = showLoginDialog();
            if (result.isEmpty()) {
                logger.info("Пользователь отменил вход");
                Platform.exit();
                return;
            }
            try {
                DBHelper.initConnection(result.get().getUsername(), result.get().getPassword());
                logger.info("Успешный вход: {}", result.get().getUsername());
                break;
            } catch (SQLException e) {
                logger.error("Ошибка подключения: {}", e.getMessage());
                showError("Неверный логин/пароль или БД недоступна");
            }
        }
        loadMainWindow();
    }

    private Optional<LoginResult> showLoginDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(loader.load()));
            dialogStage.setTitle("Авторизация");
            LoginDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            if (controller.isOkClicked()) {
                return Optional.of(new LoginResult(controller.getUsername(), controller.getPassword()));
            }
        } catch (IOException e) {
            logger.error("Не удалось загрузить login-dialog.fxml", e);
        }
        return Optional.empty();
    }

    private void loadMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            primaryStage.setScene(new Scene(loader.load(), 800, 400));
            primaryStage.setTitle("Поликлиника");
            primaryStage.show();
            logger.debug("Главное окно открыто");
        } catch (IOException e) {
            logger.error("Ошибка загрузки main-view.fxml", e);
            showErrorAndExit("Не удалось загрузить интерфейс");
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showErrorAndExit(String msg) {
        showError(msg);
        Platform.exit();
    }

    @Override
    public void stop() {
        DBHelper.closeConnection();
        logger.info("Приложение остановлено");
    }

    // public static Stage getPrimaryStage() { return primaryStage; }

    public static Stage getStage() {
        return primaryStage;
    }

    private static class LoginResult {
        private final String username, password;
        LoginResult(String u, String p) { username = u; password = p; }
        String getUsername() { return username; }
        String getPassword() { return password; }
    }

    public static void main(String[] args) {
        launch();
    }
}