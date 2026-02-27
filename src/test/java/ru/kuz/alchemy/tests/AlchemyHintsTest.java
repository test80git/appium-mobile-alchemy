package ru.kuz.alchemy.tests;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kuz.alchemy.pages.MainPage;
import ru.kuz.alchemy.pages.GamePage;
import ru.kuz.alchemy.pages.HintsShopPage;


public class AlchemyHintsTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(AlchemyHintsTest.class);

    private MainPage mainPage;
    private GamePage gamePage;

    @BeforeEach
    public void initPages() {
        mainPage = new MainPage();
        gamePage = new GamePage();
    }

    @Test
    @Description("Тест получения подсказок (быстрый или медленный блок)")
    public void testHintsIncrease() {
        // 1. Нажать Play
        GamePage gamePage = mainPage.waitForPageLoaded()
                .tapPlay();

        // 2. Ждем загрузки игрового экрана
        gamePage.waitForScreenLoaded();

        // 3. Запоминаем начальное количество подсказок
        int initialHints = gamePage.getHintsCount();
        log.info("Начальное количество подсказок: {}", initialHints);

        // 4. Открываем магазин подсказок
        HintsShopPage shop = gamePage.openHintsShop();

        // 5. Закрываем Google Play Games если вылезло
        shop.closeGoogleSignInIfPresent();

        // 6. Получаем подсказку (автоматически выберет быстрый или медленный блок)
        shop.useFastHint(); // внутри логика переключения на медленный если надо

        // 7. Возвращаемся на игровой экран и проверяем
        gamePage.waitForScreenLoaded()
                .verifyHintsIncreasedBy2(initialHints);
    }

}
