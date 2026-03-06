package ru.kuz.alchemy.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.File;

import io.qameta.allure.Description;
import ru.kuz.alchemy.pages.GamePage;
import ru.kuz.alchemy.pages.MainPage;

public class ScreenshotTests extends BaseTest {

    private TestInfo testInfo;

    private MainPage mainPage;
    private GamePage gamePage;


    @BeforeEach
    public void init(TestInfo info) {
        this.testInfo = info;
        mainPage = new MainPage();
        gamePage = new GamePage();
    }


    /**
     * Проверка верстки страницы Play
     * Перед первым запуском запускать так, чтобы сделать эталон скриншота
     * Надо чтобы в test.properties у updateScreenshots было значение true
     */
    @Test
    @Description("Позитивный тест: Проверка скриншота страницы Play")
    public void testGameScreenshot() {
        mainPage.waitForPageLoaded().tapPlay();

        File mainScreenScreenshot = gamePage
                .waitForScreenLoaded()
                .fullPageScreenshot();

        assertScreenshot(mainScreenScreenshot, testInfo.getDisplayName());
    }

    /**
     * Проверка верстки страницы Play
     * Перед первым запуском запускать так, чтобы сделать эталон скриншота
     * Надо чтобы в test.properties у updateScreenshots было значение true
     * Затем открыть в редакторе скриншот и добавить черту, например.
     */
    @Test
    @Description("Негативный тест: Проверка скриншота страницы Play")
    public void testGameScreenshotFail() {
        mainPage.waitForPageLoaded().tapPlay();

        File mainScreenScreenshot = gamePage
                .waitForScreenLoaded()
                .fullPageScreenshot();

        assertScreenshot(mainScreenScreenshot, "testGameScreenshotFail()");
    }

}
