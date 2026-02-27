package ru.kuz.alchemy.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class GameField {
    private static final Logger log = LoggerFactory.getLogger(GameField.class);

    private SelenideElement waterElement = $(By.xpath("//android.widget.TextView[@text='Water']/.."));
    private SelenideElement airElement = $(By.xpath("//android.widget.TextView[@text='Air']/.."));
    private SelenideElement fireElement = $(By.xpath("//android.widget.TextView[@text='Fire']/.."));
    private SelenideElement soilElement = $(By.xpath("//android.widget.TextView[@text='Soil']/.."));

    @Step("Нажимаем Water")
    public GameField tapWater() {
        log.info("Нажимаем Water");
        waterElement.click();
        return this;
    }

    @Step("Нажимаем Air")
    public GameField tapAir() {
        log.info("Нажимаем Air");
        airElement.click();
        return this;
    }

    @Step("Нажимаем Fire")
    public GameField tapFire() {
        log.info("Нажимаем Fire");
        fireElement.click();
        return this;
    }

    @Step("Нажимаем Soil")
    public GameField tapSoil() {
        log.info("Нажимаем Soil");
        soilElement.click();
        return this;
    }

    @Step("Проверяем элемент {name}")
    public GameField verifyElementExists(String name) {
        $(By.xpath("//android.widget.TextView[@text='" + name + "']"))
                .should(Condition.visible, Duration.ofSeconds(10));
        return this;
    }
}