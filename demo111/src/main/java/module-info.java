module com.example.phonebook {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Log4j2
    requires org.apache.logging.log4j;

    opens com.example.phonebook to javafx.fxml;
    opens com.example.phonebook.model to javafx.base;
    opens com.example.phonebook.storage to javafx.base;
    opens com.example.phonebook.service to javafx.base;


    exports com.example.phonebook;
    exports com.example.phonebook.model;
    exports com.example.phonebook.storage;
    exports com.example.phonebook.service;
}
