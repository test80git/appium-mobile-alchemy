package ru.kuz.alchemy.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage{
    private static final Logger log = LoggerFactory.getLogger(MainPage.class);

    private SelenideElement playButton = $(By.xpath("//android.widget.TextView[@text='Play']/.."));

    @Step("Ожидаем загрузки главного экрана")
    public MainPage waitForPageLoaded() {
        log.info("Ожидаем загрузки главного экрана");
        playButton.should(Condition.visible, Duration.ofSeconds(15));
        return this;
    }

    @Step("Нажимаем кнопку Play")
    public GamePage tapPlay() {
        log.info("Нажимаем Play");
        playButton.click();
        return new GamePage();
    }

}