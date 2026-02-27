package ru.kuz.alchemy.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Set;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class MainPage {
    private static final Logger log = LoggerFactory.getLogger(MainPage.class);

    private SelenideElement playButton = $(By.xpath("//android.widget.TextView[@text='Play']/.."));
    private SelenideElement settingsButton = $(By.xpath("//android.widget.TextView[@text='Settings']/.."));

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

    @Step("Нажимаем кнопку Settings")
    public SettingsPage tapSettings() {
        log.info("Нажимаем Settings");
        settingsButton.click();
        return new SettingsPage();
    }

}