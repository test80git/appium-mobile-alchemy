package ru.kuz.alchemy.tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import ru.kuz.alchemy.helper.RunHelper;
import ru.kuz.alchemy.listeners.AllureListener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;


import static ru.kuz.alchemy.helper.Constants.SCREENSHOT_TO_SAVE_FOLDER;
import static ru.kuz.alchemy.helper.DeviceHelper.executeBash;

/**
 * Базовый тестовый класс
 */
@ExtendWith(AllureListener.class)
public class BaseTest {

    @BeforeAll
    public static void setup() {
        // Логирование для Allure
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        // Папка для скриншотов
        Configuration.reportsFolder = SCREENSHOT_TO_SAVE_FOLDER;

        // Инициализация драйвера через RunHelper
        Configuration.browser = RunHelper.runHelper().getDriverClass().getName();
        Configuration.browserSize = null;
        Configuration.timeout = 10000;

        // Отключение анимаций
        disableAnimationOnEmulator();
    }

    @BeforeEach
    public void startDriver() {
        Allure.step("Открыть приложение", () -> Selenide.open());
    }

    @AfterEach
    public void afterEach() {
        Allure.step("Закрыть приложение", Selenide::closeWebDriver);
    }

    /**
     * Отключение анимаций на эмуляторе чтобы не лагало
     */
    private static void disableAnimationOnEmulator() {
        String adbCommand = getAdbCommand();
        executeBash(adbCommand + " shell settings put global transition_animation_scale 0.0");
        executeBash(adbCommand + " shell settings put global window_animation_scale 0.0");
        executeBash(adbCommand + " shell settings put global animator_duration_scale 0.0");
    }

    private static String getAdbCommand() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "adb.exe";  // Windows
        } else {
            return "adb";      // Linux/Mac
        }
    }

}
