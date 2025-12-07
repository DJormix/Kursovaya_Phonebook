package com.example.phonebook.model;

/**
 * Перечисление типов телефонного номера.
 * Используется для классификации номеров в контактной книге.
 * <p>Каждый тип имеет название, которое используется
 * в графическом интерфейсе.</p>
 */
public enum PhoneType {

    /** Мобильный телефон */
    MOBILE("Мобильный"),

    /** Домашний телефон */
    HOME("Домашний"),

    /** Рабочий телефон */
    WORK("Рабочий"),

    /** Факс */
    FAX("Факс");

    /**  Название типа */
    private final String displayName;

    /**
     * Создаёт тип телефона с указанным отображаемым именем.
     * @param displayName текст, который показывается пользователю
     */
    PhoneType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает название типа, отображаемое в интерфейсе.
     * @return строка для отображения
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Преобразует текстовое название в соответствующий тип телефона.
     * Используется при чтении данных из интерфейса.
     * <p>Если тип не найден или передан null — возвращается MOBILE.</p>
     * @param text строковое представление типа
     * @return соответствующий {@link PhoneType}
     */
    public static PhoneType fromDisplay(String text) {
        if (text == null) {
            return MOBILE;
        }
        String trimmed = text.trim();
        for (PhoneType t : values()) {
            if (t.displayName.equalsIgnoreCase(trimmed)) {
                return t;
            }
        }
        return MOBILE;
    }
}
