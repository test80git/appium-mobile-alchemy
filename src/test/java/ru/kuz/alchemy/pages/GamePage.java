package ru.kuz.alchemy.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class GamePage extends BasePage{
    private static final Logger log = LoggerFactory.getLogger(GamePage.class);

    private GameField gameField = new GameField();
    private SelenideElement hintsCounter = $(By.xpath("//android.widget.TextView[starts-with(@text, 'Hints')]"));
    private SelenideElement freeHintsButton = $(By.xpath("//android.view.View[@content-desc='Free hints']"));

    @Step("Ожидаем загрузки игрового экрана")
    public GamePage waitForScreenLoaded() {
        log.info("Ожидаем загрузки игрового экрана");
        hintsCounter.should(Condition.visible, Duration.ofSeconds(15));
        return this;
    }

    @Step("Получаем текущее количество подсказок")
    public int getHintsCount() {
        String text = hintsCounter.should(Condition.visible).getText();
        Pattern pattern = Pattern.compile("Hints \\((\\d+)\\)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new RuntimeException("Не удалось распарсить: " + text);
    }

    @Step("Открываем магазин подсказок")
    public HintsShopPage openHintsShop() {
        log.info("Открываем магазин подсказок");
        freeHintsButton.should(Condition.visible).click();
        return new HintsShopPage();
    }

    @Step("Проверяем что подсказки увеличились на 2")
    public GamePage verifyHintsIncreasedBy2(int initialCount) {
        int actualCount = getHintsCount();
        if (actualCount != initialCount + 2) {
            throw new AssertionError("Ожидалось: " + (initialCount + 2) + ", фактически: " + actualCount);
        }
        log.info("✅ Подсказки увеличились на 2");
        return this;
    }

    @Step("Получаем текущий прогресс")
    public int getCurrentProgress() {
        String progressText = $(By.xpath("//android.widget.TextView[@text='Progress:']/following-sibling::android.widget.TextView[1]"))
                .should(Condition.visible).getText();
        return Integer.parseInt(progressText);
    }

    @Step("Нажимаем кнопку Mix!")
    public GamePage tapMix() {
        $(By.xpath("//android.view.View[@content-desc='Mix!']/.."))
                .should(Condition.visible).click();
        sleep(2000);
        return this;
    }

    public GameField onGameField() {
        return gameField;
    }
}