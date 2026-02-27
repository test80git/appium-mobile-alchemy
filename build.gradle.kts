plugins {
    id("java")
//    id("io.qameta.allure") version "2.11.2"
    id("io.qameta.allure") version "2.12.0"
}

group = "ru.kuz"
version = "1.0"

val allureVersion = "2.29.0"
val junitVersion = "5.8.2"
val slf4jVersion = "1.7.32"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("com.github.romankh3:image-comparison:4.4.0")
    testImplementation("io.qameta.allure", "allure-junit5", "$allureVersion")
    testImplementation("io.qameta.allure", "allure-selenide", "$allureVersion")
//    testImplementation("com.codeborne:selenide-appium:5.25.0")
//    testImplementation("com.codeborne:selenide:5.25.0")
    testImplementation("com.codeborne:selenide:6.19.1")

//    testImplementation("io.appium:java-client:9.2.2")
    testImplementation("io.appium:java-client:8.3.0")
    testImplementation("org.aeonbits.owner:owner:1.0.12") /*для чтения конфигураций*/
    testImplementation("io.rest-assured:rest-assured:4.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")

    testImplementation("org.slf4j:slf4j-api:${slf4jVersion}")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.4.6")
    testRuntimeOnly("ch.qos.logback:logback-core:1.4.6")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
    useJUnitPlatform()
    systemProperty("allure.results.directory", "build/allure-results")
}

