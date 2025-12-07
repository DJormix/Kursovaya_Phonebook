import org.gradle.jvm.tasks.Jar

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    // Логи
    implementation("org.apache.logging.log4j:log4j-api:2.24.1")
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")

    // Тесты
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
    useJUnitPlatform()
}

application {

    mainClass.set("com.example.phonebook.Launcher")
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}

/**
 * fat-jar со всеми зависимостями.

 */
tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Собирает fat-jar со всеми зависимостями"

    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }


    from(sourceSets.main.get().output)


    from(configurations.runtimeClasspath.get().map { file ->
        if (file.isDirectory) file else zipTree(file)
    })
}
