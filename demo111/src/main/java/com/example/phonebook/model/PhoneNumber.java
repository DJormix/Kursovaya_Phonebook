package com.example.phonebook.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Представляет один телефонный номер в контактной книге.
 * Каждый номер имеет строковое значение
 * и тип {@link PhoneType}, который описывает его назначение:
 * мобильный, домашний, рабочий.
 * <p>Класс реализует {@link Serializable}, что позволяет сохранять
 * данные в бинарный файл вместе с остальными объектами модели.</p>
 */
public class PhoneNumber implements Serializable {

    private String number;
    private PhoneType type;

    /**
     * Создаёт телефонный номер.
     * @param number строка номера телефона
     * @param type   тип телефонного номера
     */
    public PhoneNumber(String number, PhoneType type) {
        this.number = number;
        this.type = type;
    }

    /**
     * Возвращает строковое значение номера телефона.
     * @return номер телефона
     */
    public String getNumber() {
        return number;
    }

    /**
     * Устанавливает новое значение номера телефона.
     * @param number номер телефона
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Возвращает тип телефонного номера (мобильный, рабочий, домашний и т.п.)
     * @return тип номера {@link PhoneType}
     */
    public PhoneType getType() {
        return type;
    }

    /**
     * Устанавливает новый тип телефонного номера.
     * @param type тип номера
     */
    public void setType(PhoneType type) {
        this.type = type;
    }

    /**
     * Возвращает строковое представление номера
     */
    @Override
    public String toString() {
        return type + ": " + number;
    }

    /**
     * Два номера считаются равными, если совпадают и строка номера,
     * и тип телефонного номера.
     * @param o объект для сравнения
     * @return true, если номера совпадают
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber that)) return false;
        return Objects.equals(number, that.number) && type == that.type;
    }

    /**
     * Хэш-код строится на основе номера и его типа.
     */
    @Override
    public int hashCode() {
        return Objects.hash(number, type);
    }
}
