package ru.kuz.alchemy.driver;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import org.openqa.selenium.By;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

/**
 * Класс помощник для Page страниц
 */
public class EmulatorHelper {

    private static AndroidDriver getDriver() {
        return (AndroidDriver) WebDriverRunner.getWebDriver();
    }

    /**
     * Нажимает кнопку назад
     */
    public static void goBack() {
        getDriver().navigate().back();
    }

    /**
     * Листает к элементу по его тексту (обновленная версия для Appium 9.x)
     *
     * @param text текст на элементе
     */
    public static void androidScrollToAnElementByText(String text) {
        By selector = By.xpath("//*[@text='" + text + "']");
        getDriver().findElement(selector).click();
    }

    /**
     * Скролл с использованием UiAutomator2 (альтернативный метод)
     *
     * @param text текст элемента для скролла
     */
    public static void scrollToText(String text) {
        getDriver().findElement(By.xpath(
                "//*[contains(@text, '" + text + "')]"
        )).click();
    }

    /**
     * Закрывает клавиатуру если она есть
     */
    public static void closeKeyBoard() {
        if (getDriver().isKeyboardShown()) {
            getDriver().hideKeyboard();
        }
    }

    /**
     * Вводит текст и нажимает Enter
     *
     * @param element поле для ввода
     * @param text    текст
     */
    public static void sendKeysAndFind(SelenideElement element, String text) {
        element.sendKeys(text);
        getDriver().pressKey(new KeyEvent(AndroidKey.ENTER));
    }

}
