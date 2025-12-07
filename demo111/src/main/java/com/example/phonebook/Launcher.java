package com.example.phonebook;

import javafx.application.Application;

/**
 * Точка входа в приложение.
 * <p>
 * Запускает основной класс {@link PhonebookApplication}.
 */
public class Launcher {
    /**
     * Запускает JavaFX-приложение.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Application.launch(PhonebookApplication.class, args);
    }
}
