package com.example.phonebook.service;

import com.example.phonebook.model.Contact;
import com.example.phonebook.storage.PhonebookStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис телефонного справочника.
 * <p> Хранит список контактов в оперативной памяти,
 * а также обеспечивает:
 * <ul>
 *     <li>Загрузку данных из бинарного файла</li>
 *     <li>Добавление, обновление и удаление контактов</li>
 *     <li>Поиск по ФИО или номеру телефона</li>
 *     <li>Сортировку контактов</li>
 *     <li>Сохранение данных в файл через {@link PhonebookStorage}</li>
 * </ul>
 * <p>Сервис является основным для логики приложения.</p>
 */
public class PhonebookService {

    private static final Logger log = LogManager.getLogger(PhonebookService.class);

    private final PhonebookStorage storage;
    private final List<Contact> contacts = new ArrayList<>();

    /**
     * Создаёт сервис телефонного справочника и загружает данные из файла.
     * @param filePath путь к бинарному файлу-хранилищу
     */
    public PhonebookService(Path filePath) {
        this.storage = new PhonebookStorage(filePath);
        log.info("Инициализация PhonebookService. Файл: {}", filePath.toAbsolutePath());
        contacts.addAll(storage.load());
        log.info("Загрузка завершена. Количество контактов: {}", contacts.size());
    }

    /**
     * Возвращает список всех контактов.
     * @return неизменяемый список контактов
     */
    public List<Contact> getAllContacts() {
        return Collections.unmodifiableList(contacts);
    }

    /**
     * Добавляет новый контакт и сохраняет изменения в файл.
     * @param contact контакт для добавления
     */
    public void addContact(Contact contact) {
        contacts.add(contact);
        log.info("Добавлен контакт: {}", contact.getFullName());
        save();
    }

    /**
     * Удаляет контакт из списка и сохраняет изменения.
     * @param contact контакт для удаления
     */
    public void removeContact(Contact contact) {
        contacts.remove(contact);
        log.info("Удалён контакт: {}", contact.getFullName());
        save();
    }

    /**
     * Обновляет существующий контакт.
     * @param oldContact     старый вариант контакта (для поиска в списке)
     * @param updatedContact новый вариант
     */
    public void updateContact(Contact oldContact, Contact updatedContact) {
        int index = contacts.indexOf(oldContact);
        if (index >= 0) {
            contacts.set(index, updatedContact);
            log.info("Обновлён контакт: {} -> {}",
                    oldContact.getFullName(), updatedContact.getFullName());
            save();
        } else {
            log.warn("Попытка обновить контакт, которого нет в списке: {}",
                    oldContact.getFullName());
        }
    }

    /**
     * Выполняет поиск по ФИО или по номеру телефона.
     * Метод объединяет оба типа поиска в один.
     * @param query строка поиска
     * @return список подходящих контактов
     */
    public List<Contact> search(String query) {
        if (query == null || query.isBlank()) {
            return getAllContacts();
        }

        String q = query.toLowerCase(Locale.ROOT).replace(" ", "");

        return contacts.stream()
                .filter(c -> {
                    boolean nameMatch =
                            c.getFullName() != null &&
                                    c.getFullName().toLowerCase(Locale.ROOT).contains(query.toLowerCase());

                    boolean phoneMatch =
                            c.getPhones().stream().anyMatch(
                                    p -> p.getNumber()
                                            .toLowerCase(Locale.ROOT)
                                            .replace(" ", "")
                                            .contains(q)
                            );

                    return nameMatch || phoneMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список контактов, отсортированных по ФИО по алфавиту.
     * @return отсортированный список
     */
    public List<Contact> getSortedByName() {
        return contacts.stream()
                .sorted(Comparator.comparing(
                        Contact::getFullName,
                        Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toList());
    }

    /**
     * Сохраняет текущее состояние списка контактов в бинарный файл.
     */
    public void save() {
        storage.save(contacts);
    }
}
