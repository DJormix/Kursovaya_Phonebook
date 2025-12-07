package com.example.phonebook;

import com.example.phonebook.model.Contact;
import com.example.phonebook.model.PhoneNumber;
import com.example.phonebook.service.PhonebookService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

/**
 * Контроллер главного окна телефонного справочника.
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>отображение списка контактов</li>
 *     <li>поиск, сортировку и масштабирование</li>
 *     <li>отображение телефонных номеров выбранного контакта</li>
 *     <li>работу кнопок: добавить, изменить, удалить</li>
 *     <li>открытие окна редактора контактов</li>
 * </ul>
 */
public class PhonebookController {

    private PhonebookService service;

    /** Текущий размер шрифта списков. */
    private double fontSize = 12.0;

    private final double MIN_FONT = 10.0;
    private final double MAX_FONT = 24.0;
    private final double STEP_FONT = 2.0;

    /**
     * Устанавливает сервис телефонного справочника.
     * @param service экземпляр {@link PhonebookService}
     */
    public void setPhonebookService(PhonebookService service) {
        this.service = service;
        if (contactList != null) {
            refreshContactList(service.getAllContacts());
        }
    }

    @FXML private TextField searchField;
    @FXML private Button sortButton;
    @FXML private ListView<Contact> contactList;
    @FXML private ListView<String> phoneList;
    @FXML private Button zoomIn;
    @FXML private Button zoomOut;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    /**
     * Обновляет список контактов в левом ListView.
     * @param contacts список контактов
     */
    private void refreshContactList(List<Contact> contacts) {
        contactList.getItems().setAll(contacts);
    }

    /**
     * Открывает окно редактора контактов для добавления или изменения.
     * @param original контакт, который нужно изменить, либо {@code null} для нового
     * @return новый или изменённый контакт; {@code null} если действие отменено
     */
    private Contact openEditor(Contact original) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    PhonebookApplication.class.getResource("contact-editor.fxml")
            );

            Parent root = loader.load();
            ContactEditorController controller = loader.getController();

            if (original != null) {
                controller.load(original);
            }

            Stage dialog = new Stage();
            dialog.setTitle(original == null ? "Добавить контакт" : "Изменить контакт");
            dialog.setScene(new Scene(root));
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(contactList.getScene().getWindow());
            dialog.showAndWait();

            return controller.getResult();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Отображает телефоны выбранного контакта в правом ListView.
     * @param contact выбранный контакт
     */
    private void showPhones(Contact contact) {
        phoneList.getItems().clear();
        if (contact == null) return;

        for (PhoneNumber pn : contact.getPhones()) {
            String line = String.format("%s: %s",
                    pn.getType().getDisplayName(),
                    pn.getNumber());
            phoneList.getItems().add(line);
        }
    }

    /**
     * Применяет текущий размер шрифта ко всем спискам.
     */
    private void applyFontSize() {
        String style = String.format("-fx-font-size: %.0fpx;", fontSize);
        contactList.setStyle(style);
        phoneList.setStyle(style);
    }

    /**
     * Сортирует контакты по ФИО.
     */
    @FXML
    private void onSortButtonClick() {
        if (service == null) return;
        List<Contact> sorted = service.getSortedByName();
        refreshContactList(sorted);
        phoneList.getItems().clear();
    }

    /**
     * Инициализирует обработчики событий интерфейса.
     * Вызывается автоматически при загрузке FXML.
     */
    @FXML
    private void initialize() {

        contactList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldC, newC) -> showPhones(newC));

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (service == null) return;
            List<Contact> filtered = service.search(newText);
            refreshContactList(filtered);
            phoneList.getItems().clear();
        });

        addButton.setOnAction(e -> {
            Contact c = openEditor(null);
            if (c != null) {
                service.addContact(c);
                refreshContactList(service.getAllContacts());
            }
        });

        editButton.setOnAction(e -> {
            Contact selected = contactList.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            Contact updated = openEditor(selected);
            if (updated != null) {
                service.updateContact(selected, updated);
                refreshContactList(service.getAllContacts());
            }
        });

        deleteButton.setOnAction(e -> {
            Contact selected = contactList.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            service.removeContact(selected);
            refreshContactList(service.getAllContacts());
            phoneList.getItems().clear();
        });

        zoomIn.setOnAction(e -> {
            if (fontSize < MAX_FONT) {
                fontSize += STEP_FONT;
                applyFontSize();
            }
        });

        zoomOut.setOnAction(e -> {
            if (fontSize > MIN_FONT) {
                fontSize -= STEP_FONT;
                applyFontSize();
            }
        });

        applyFontSize();
    }
}
