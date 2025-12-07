package com.example.phonebook.service;

import com.example.phonebook.model.Contact;
import com.example.phonebook.model.PhoneNumber;
import com.example.phonebook.model.PhoneType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для класса {@link PhonebookService}.
 * <p>
 * Проверяются основные функции:
 * <ul>
 *     <li>добавление контактов;</li>
 *     <li>сортировка по имени;</li>
 *     <li>поиск по имени;</li>
 *     <li>поиск по номеру телефона;</li>
 *     <li>корректная работа с временным файлом БД;</li>
 * </ul>
 * Каждый тест выполняется в отдельной временной директории
 * благодаря аннотации {@link TempDir}.
 */
class PhonebookServiceTest {

    /**
     * Временная директория, создаваемая JUnit для тестов.
     * Фреймворк автоматически очищает её после завершения тестов.
     */
    @TempDir
    Path tempDir;

    private PhonebookService service;

    /**
     * Инициализация нового PhonebookService перед каждым тестом.
     * <p>
     * Используется отдельный временный файл, чтобы тесты не влияли
     * друг на друга и были полностью независимыми.
     */
    @BeforeEach
    void setUp() {
        Path file = tempDir.resolve("phonebook-test.bin");
        service = new PhonebookService(file);
    }

    /**
     * Вспомогательный метод для создания контактов в тестах.
     * @param name   ФИО контакта
     * @param number Номер телефона
     * @return созданный объект {@link Contact}
     */
    private Contact createContact(String name, String number) {
        Contact c = new Contact(name);
        c.addPhone(new PhoneNumber(number, PhoneType.MOBILE));
        return c;
    }

    /**
     * Проверяет корректность добавления контактов и получения полного списка.
     * <p>
     * Сценарий:
     * <ol>
     *     <li>создаём контакт;</li>
     *     <li>добавляем его в сервис;</li>
     *     <li>проверяем, что контакт появился в списке;</li>
     * </ol>
     */
    @Test
    void addContactAndGetAll() {
        Contact ivan = createContact("Иванов Иван Иванович", "+79319222322");

        service.addContact(ivan);

        List<Contact> all = service.getAllContacts();
        assertEquals(1, all.size(), "Должен быть один контакт");
        assertEquals("Иванов Иван Иванович", all.get(0).getFullName());
    }

    /**
     * Проверяет сортировку контактов по ФИО.
     * <p>
     * Ожидается, что:
     * <ul>
     *     <li>"Иванов Иван Иванович" будет первым;</li>
     *     <li>"Петров Пётр Петрович" будет вторым;</li>
     * </ul>
     */
    @Test
    void sortByName() {
        Contact petrov = createContact("Петров Пётр Петрович", "+79319222321");
        Contact ivanov = createContact("Иванов Иван Иванович", "+79319222322");

        service.addContact(petrov);
        service.addContact(ivanov);

        List<Contact> sorted = service.getSortedByName();

        assertEquals(2, sorted.size());
        assertEquals("Иванов Иван Иванович", sorted.get(0).getFullName());
        assertEquals("Петров Пётр Петрович", sorted.get(1).getFullName());
    }

    /**
     * Проверяет поиск по части имени.
     * <p>
     * Поиск по строке "Иван" должен вернуть только "Иванов Иван Иванович".
     */
    @Test
    void searchByName() {
        Contact petrov = createContact("Петров Пётр Петрович", "+79319222321");
        Contact ivanov = createContact("Иванов Иван Иванович", "+79319222322");

        service.addContact(petrov);
        service.addContact(ivanov);

        List<Contact> result = service.search("Иван");

        assertEquals(1, result.size());
        assertEquals("Иванов Иван Иванович", result.get(0).getFullName());
    }

    /**
     * Проверяет поиск по части номера телефона.
     * <p>
     * Строка "2322" содержится только в номере Иванова.
     */
    @Test
    void searchByPhone() {
        Contact petrov = createContact("Петров Пётр Петрович", "+79319222321");
        Contact ivanov = createContact("Иванов Иван Иванович", "+79319222322");

        service.addContact(petrov);
        service.addContact(ivanov);

        List<Contact> result = service.search("2322");

        assertEquals(1, result.size());
        assertEquals("Иванов Иван Иванович", result.get(0).getFullName());
    }
}
