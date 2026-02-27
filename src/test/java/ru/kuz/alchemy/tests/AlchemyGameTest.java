package ru.kuz.alchemy.tests;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kuz.alchemy.pages.MainPage;
import ru.kuz.alchemy.pages.GamePage;


public class AlchemyGameTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(AlchemyGameTest.class);

    private MainPage mainPage;
    private GamePage gamePage;

    @BeforeEach
    public void initPages() {
        mainPage = new MainPage();
        gamePage = new GamePage();
    }

    @Test
    @Description("Тест комбинации Water + Soil = Plant")
    public void testWaterAndSoilCreatePlant() {
        // 1. Открыть игру и нажать Play
        mainPage.waitForPageLoaded()
                .tapPlay();

        // 2. Подождать загрузки игрового экрана
        gamePage.waitForScreenLoaded();

        // 3. Запомнить начальный прогресс
        int initialProgress = gamePage.getCurrentProgress();
        log.info("Начальный прогресс: {}", initialProgress);

        // 4. Выбрать Water
        gamePage.onGameField().tapWater();

        // 5. Выбрать Soil
        gamePage.onGameField().tapSoil();

        // 6. Нажать кнопку Mix!
        gamePage.tapMix();

        // 7. Проверить что появился новый элемент Plant
        gamePage.onGameField().verifyElementExists("Plant");

        // 8. Проверить что прогресс увеличился на 1
        int newProgress = gamePage.getCurrentProgress();
        assert newProgress == initialProgress + 1 :
                "Прогресс не увеличился! Было: " + initialProgress + ", стало: " + newProgress;

        log.info("✅ Тест пройден! Создан элемент Plant");
    }
}
