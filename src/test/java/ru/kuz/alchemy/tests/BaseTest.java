package ru.kuz.alchemy.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import ru.kuz.alchemy.config.ConfigReader;
import ru.kuz.alchemy.helper.Constants;
import ru.kuz.alchemy.helper.RunHelper;
import ru.kuz.alchemy.listeners.AllureListener;
import ru.kuz.alchemy.pages.BasePage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static ru.kuz.alchemy.helper.Constants.SCREENSHOT_TO_SAVE_FOLDER;
import static ru.kuz.alchemy.helper.DeviceHelper.executeBash;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

/**
 * Базовый тестовый класс
 */
@ExtendWith(AllureListener.class)
public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

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

    /**
     * Проверка скриншота с эталоном для проверки верстки
     * @param actualScreenshot актуальный скриншот
     * @param expectedFileName название файла для сравнений
     */
    public void assertScreenshot(File actualScreenshot, String expectedFileName) {
        expectedFileName = expectedFileName.replace("()", ".png");

        if (ConfigReader.testConfig.isScreenshotsNeedToUpdate()) {
            try {
                Files.createDirectories(new File(Constants.EXPECTED_SCREENSHOT_TO_SAVE_FOLDER).toPath());

                // РЕЖИМ ОБНОВЛЕНИЯ: копирует текущий скриншот как эталон
                Files.move(actualScreenshot.toPath(),
                        new File(Constants.EXPECTED_SCREENSHOT_TO_SAVE_FOLDER + expectedFileName).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                log.info("✅ Скриншот сохранен как эталон: " + expectedFileName);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось сохранить эталонный скриншот", e);
            }
            return;
        }

        // РЕЖИМ СРАВНЕНИЯ: ищет эталон и сравнивает
        try {
            File expectedFile = new File(Constants.EXPECTED_SCREENSHOT_TO_SAVE_FOLDER + expectedFileName);
            if (!expectedFile.exists()) {
                throw new RuntimeException("Эталонный скриншот не найден: " + expectedFile.getAbsolutePath() +
                        "\nЗапусти тест с update.screenshots=true для создания эталона");
            }

            BufferedImage expectedImage = ImageComparisonUtil
                    .readImageFromResources(Constants.EXPECTED_SCREENSHOT_TO_SAVE_FOLDER + expectedFileName);

            BufferedImage actualImage = ImageIO.read(actualScreenshot);
            if (actualImage == null) {
                throw new RuntimeException("Не удалось прочитать актуальный скриншот: " + actualScreenshot.getAbsolutePath());
            }

            File diffDir = new File("build/diff");
            diffDir.mkdirs();
            File resultDestination = new File(diffDir, "diff_" + expectedFileName);

            // Сравниваем
            ImageComparison imageComparison = new ImageComparison(expectedImage, actualImage, resultDestination);
            ImageComparisonResult imageComparisonResult = imageComparison.compareImages();

            // Если скриншоты отличаются
            if (imageComparisonResult.getImageComparisonState() == ImageComparisonState.MISMATCH) {
                // Добавляем скриншот с отличиями в Allure
                if (resultDestination.exists()) {
                    byte[] diffImageBytes = Files.readAllBytes(resultDestination.toPath());
                    AllureListener.saveScreenshot(diffImageBytes);
                }

                log.info("❌ Найдены различия в скриншотах!");
                log.info("   Ожидаемый: " + expectedFile.getAbsolutePath());
                log.info("   Актуальный: " + actualScreenshot.getAbsolutePath());
                log.info("   Diff: " + resultDestination.getAbsolutePath());
            } else {
                log.info("✅ Скриншоты совпадают");
            }

            assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сравнении скриншотов", e);
        }
    }

}
