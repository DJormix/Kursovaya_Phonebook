package com.example.phonebook.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Модель контакта телефонного справочника.
 * Хранит ФИО абонента и список его телефонных номеров.
 * <p>Класс реализует {@link Serializable}, чтобы его можно было
 * сохранять в бинарный файл и загружать при следующем запуске приложения.</p>
 */
public class Contact implements Serializable {

    private String fullName;
    private final List<PhoneNumber> phones = new ArrayList<>();

    /**
     * Создаёт новый контакт с указанным ФИО.
     * @param fullName ФИО абонента
     */
    public Contact(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Возвращает ФИО контакта.
     * @return строка ФИО
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Обновляет ФИО контакта.
     * @param fullName новое ФИО
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Возвращает список телефонных номеров контакта.
     * Список изменяемый — можно добавлять и удалять номера.
     * @return список {@link PhoneNumber}
     */
    public List<PhoneNumber> getPhones() {
        return phones;
    }

    /**
     * Добавляет телефонный номер к контакту.
     * @param phoneNumber объект {@link PhoneNumber}, который нужно добавить
     */
    public void addPhone(PhoneNumber phoneNumber) {
        phones.add(phoneNumber);
    }

    /**
     * Удаляет телефонный номер из контакта.
     * @param phoneNumber объект {@link PhoneNumber}, который нужно удалить
     */
    public void removePhone(PhoneNumber phoneNumber) {
        phones.remove(phoneNumber);
    }

    /**
     * Возвращает телефоны контакта в виде одной строки,
     * разделённой точкой с запятой.
     * @return строка вида "Мобильный: 123; Рабочий: 456"
     */
    public String phonesAsString() {
        return phones.stream()
                .map(PhoneNumber::toString)
                .reduce((a, b) -> a + "; " + b)
                .orElse("");
    }

    /**
     * Возвращает строковое представление контакта
     * @return строка вида "Иван Иванов (Мобильный: 123; Домашний: 456)"
     */
    @Override
    public String toString() {
        return fullName + " (" + phonesAsString() + ")";
    }

    /**
     * Контакты считаются равными, если совпадают их ФИО.
     * @param o объект для сравнения
     * @return true, если ФИО совпадает
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact contact)) return false;
        return Objects.equals(fullName, contact.fullName);
    }

    /**
     * Хэш-код основан на ФИО.
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }
}
