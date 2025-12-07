package com.example.phonebook.storage;

import com.example.phonebook.model.Contact;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за хранение телефонного справочника.
 * <p>
 * Основные функции:
 * <ul>
 *     <li>Сохранение списка контактов в бинарный файл</li>
 *     <li>Загрузка списка контактов из файла</li>
 *     <li>Создание директории при необходимости</li>
 *     <li>Обработка ошибок ввода/вывода с логированием</li>
 * </ul>
 */
public class PhonebookStorage {

    private static final Logger log = LogManager.getLogger(PhonebookStorage.class);

    /** Путь к бинарному файлу с сохранёнными контактами */
    private final Path filePath;

    /**
     * Создаёт объект хранилища для указанного файла.
     * @param filePath путь к файлу формата .bin
     */
    public PhonebookStorage(Path filePath) {
        this.filePath = filePath;
        log.info("Создан PhonebookStorage с файлом: {}", filePath.toAbsolutePath());
    }

    /**
     * Сохраняет список контактов в бинарный файл.
     * Файл будет создан, если отсутствует.
     * Родительская директория создаётся автоматически.
     * @param contacts список контактов для сохранения
     */
    public void save(List<Contact> contacts) {
        try {
            Files.createDirectories(filePath.getParent());

            try (ObjectOutputStream oos =
                         new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(contacts);
            }

            log.info("Успешно сохранены контакты. Количество: {}", contacts.size());
        } catch (IOException e) {
            log.error("Ошибка при сохранении контактов в файл {}", filePath, e);
        }
    }

    /**
     * Загружает контакты из бинарного файла.
     * Если файл отсутствует или содержит некорректные данные — возвращается пустой список.
     * @return список контактов, загруженных из файла
     */
    @SuppressWarnings("unchecked")
    public List<Contact> load() {
        if (!Files.exists(filePath)) {
            log.warn("Файл {} не найден. Возвращаю пустой список контактов.", filePath);
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(Files.newInputStream(filePath))) {

            Object obj = ois.readObject();

            if (obj instanceof List<?>) {
                List<Contact> contacts = (List<Contact>) obj;
                log.info("Успешно загружены контакты. Количество: {}", contacts.size());
                return contacts;
            } else {
                log.error("Некорректный формат данных в файле {}", filePath);
                return new ArrayList<>();
            }

        } catch (IOException | ClassNotFoundException e) {
            log.error("Ошибка при загрузке контактов из файла {}", filePath, e);
            return new ArrayList<>();
        }
    }
}
