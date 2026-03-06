package ru.kuz.alchemy.pages;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import org.openqa.selenium.OutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

import javax.imageio.ImageIO;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import ru.kuz.alchemy.helper.Constants;

/**
 * Базовый тестовый класс для скриншотов
 */
public class BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePage.class);
    private SelenideElement content = $(AppiumBy.id("android:id/content"));

    /**
     * Делает скриншот экрана без статусбара
     *
     * @return файл скриншота
     */
    public File fullPageScreenshot() {
        log.info("Начинаю делать скриншот");
        try {
            AndroidDriver driver = (AndroidDriver) WebDriverRunner.getWebDriver();
            File fullScreenshot = driver.getScreenshotAs(OutputType.FILE);
            String fileName = "screenshot_" + System.currentTimeMillis() + ".png";
            File targetFile = new File(Constants.SCREENSHOT_TO_SAVE_FOLDER, fileName);
            boolean mkdirs = targetFile.getParentFile().mkdirs();

            File tempFile = new File(System.getProperty("java.io.tmpdir"), "temp_" + fileName);
            Files.copy(fullScreenshot.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            File croppedScreenshot = cropStatusBar(tempFile, targetFile);

            if (croppedScreenshot != null && croppedScreenshot.exists()) {
                log.info("Скриншот сохранен: {}", croppedScreenshot.getAbsolutePath());
                return croppedScreenshot;
            }

            Files.copy(fullScreenshot.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return targetFile;

        } catch (Exception e) {
            log.error("Ошибка при создании скриншота через Appium", e);
            return Screenshots.takeScreenShot(content);
        }
    }

    /**
     * Обрезает статус-бар сверху
     */
    private File cropStatusBar(File sourceFile, File targetFile) {
        try {
            BufferedImage originalImage = ImageIO.read(sourceFile);

            int statusBarHeight = getStatusBarHeight();

            if (statusBarHeight <= 0 || statusBarHeight >= originalImage.getHeight()) {
                return null;
            }
            // Обрезаем: оставляем только то, что ниже статус-бара
            BufferedImage croppedImage = originalImage.getSubimage(
                    0,                          // x
                    statusBarHeight,             // y (начинаем со статус-бара)
                    originalImage.getWidth(),    // width
                    originalImage.getHeight() - statusBarHeight  // height
            );

            ImageIO.write(croppedImage, "png", targetFile);
            return targetFile;
        } catch (IOException e) {
            log.error("Ошибка при обрезке скриншота", e);
            return null;
        }
    }

    /**
     * Получает высоту статус-бара
     */
    private int getStatusBarHeight() {
        try {
            AndroidDriver driver = (AndroidDriver) WebDriverRunner.getWebDriver();

            String result = driver.executeScript("mobile: shell",
                    Map.of("command", "dumpsys window displays")).toString();
            int barHeight = Integer.parseInt(result);
            return barHeight;

        } catch (Exception e) {
            log.warn("Не удалось получить высоту статус-бара, используем 150px");
            return 150;
        }

    }

}