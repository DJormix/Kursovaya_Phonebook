package com.example.phonebook;

import com.example.phonebook.model.Contact;
import com.example.phonebook.model.PhoneNumber;
import com.example.phonebook.model.PhoneType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Контроллер окна редактирования/создания контакта.
 * <p>
 * Отвечает за:
 * <ul>
 *     <li>ввод имени абонента</li>
 *     <li>добавление и удаление телефонов</li>
 *     <li>выбор типа номера (мобильный, домашний и т.д.)</li>
 *     <li>сбор введённых данных и возврат результата в вызывающий контроллер</li>
 * </ul>
 *
 * Контроллер работает внутри модального окна, отображаемого методом
 * {@link com.example.phonebook.PhonebookController#openEditor(Contact)}.
 */
public class ContactEditorController {

    /** Поле ввода имени контакта */
    @FXML
    private TextField nameField;

    /** Список телефонов в формате: “Тип: номер” */
    @FXML
    private ListView<String> phoneList;

    /** Кнопка добавления нового телефона */
    @FXML
    private Button addPhoneBtn;

    /** Кнопка удаления выбранного телефона */
    @FXML
    private Button removePhoneBtn;

    /** Кнопка сохранения изменений */
    @FXML
    private Button saveBtn;

    /** Кнопка отмены */
    @FXML
    private Button cancelBtn;

    /**
     * Результат работы окна.
     * Если пользователь нажал «Отмена» — остаётся {@code null}.
     */
    private Contact result = null;

    /**
     * Возвращает созданный или изменённый контакт.
     * @return новый объект Contact или null, если пользователь закрыл окно без сохранения
     */
    public Contact getResult() {
        return result;
    }

    /**
     * Инициализация контроллера:
     * назначение обработчиков кнопок.
     */
    @FXML
    private void initialize() {
        addPhoneBtn.setOnAction(e -> addPhone());
        removePhoneBtn.setOnAction(e -> removeSelectedPhone());
        saveBtn.setOnAction(e -> onSave());
        cancelBtn.setOnAction(e -> nameField.getScene().getWindow().hide());
    }

    /**
     * Открывает диалоговое окно создания номера телефона,
     * затем окно выбора типа телефона.
     * Если пользователь ввёл данные корректно — номер добавляется в список.
     */
    private void addPhone() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Добавить номер");
        dialog.setHeaderText("Введите номер телефона:");
        dialog.setContentText("Номер:");

        String number = dialog.showAndWait().orElse(null);
        if (number == null || number.isBlank())
            return;

        ChoiceDialog<PhoneType> typeDialog =
                new ChoiceDialog<>(PhoneType.MOBILE, PhoneType.values());
        typeDialog.setTitle("Тип телефона");
        typeDialog.setHeaderText("Выберите тип номера");

        PhoneType type = typeDialog.showAndWait().orElse(null);
        if (type == null) return;

        phoneList.getItems().add(type.getDisplayName() + ": " + number);
    }

    /**
     * Удаляет выбранный элемент из списка телефонов.
     * Если номер не выбран — ничего не происходит.
     */
    private void removeSelectedPhone() {
        String selected = phoneList.getSelectionModel().getSelectedItem();
        if (selected != null)
            phoneList.getItems().remove(selected);
    }

    /**
     * Создаёт объект {@link Contact}, заполняя его данными из интерфейса.
     * <p>
     * Выполняет:
     * <ul>
     *     <li>проверку, что имя не пустое</li>
     *     <li>поиск номеров вида “Тип: Номер”</li>
     *     <li>создание PhoneNumber для каждого номера</li>
     * </ul>
     * После успешного сохранения окно закрывается.
     */
    private void onSave() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "ФИО не может быть пустым").show();
            return;
        }

        Contact contact = new Contact(name);

        for (String s : phoneList.getItems()) {
            String[] parts = s.split(": ");
            PhoneType type = PhoneType.fromDisplay(parts[0]);
            String number = parts[1];
            contact.addPhone(new PhoneNumber(number, type));
        }

        this.result = contact;
        nameField.getScene().getWindow().hide();
    }

    /**
     * Загружает данные существующего контакта в поля формы.
     * Используется при редактировании.
     * @param contact контакт, который требуется показать пользователю
     */
    public void load(Contact contact) {
        nameField.setText(contact.getFullName());
        phoneList.getItems().clear();

        for (PhoneNumber pn : contact.getPhones()) {
            phoneList.getItems().add(
                    pn.getType().getDisplayName() + ": " + pn.getNumber()
            );
        }
    }
}
