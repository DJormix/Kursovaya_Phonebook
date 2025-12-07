package com.example.phonebook;

import com.example.phonebook.service.PhonebookService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Главный класс «Телефонный справочник».
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>инициализацию службы работы со справочником ({@link PhonebookService})</li>
 *     <li>загрузку главного интерфейса из FXML</li>
 *     <li>передачу сервиса контроллеру</li>
 *     <li>отображение основного окна</li>
 * </ul>
 */
public class PhonebookApplication extends Application {

    /**
     * Запускает графическое приложение.
     * @param stage главное окно приложения
     * @throws IOException если FXML не удалось загрузить
     */
    @Override
    public void start(Stage stage) throws IOException {
        Path storagePath = Path.of("data", "phonebook.bin");
        PhonebookService service = new PhonebookService(storagePath);

        FXMLLoader loader = new FXMLLoader(
                PhonebookApplication.class.getResource("main-view.fxml")
        );

        Parent root = loader.load();

        PhonebookController controller = loader.getController();
        controller.setPhonebookService(service);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Телефонный справочник");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Запуск JavaFX-приложения.
     * @param args параметры командной строки
     */
    public static void main(String[] args) {
        launch();
    }
}
