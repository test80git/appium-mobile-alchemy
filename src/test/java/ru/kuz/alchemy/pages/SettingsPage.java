package ru.kuz.alchemy.pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;

public class SettingsPage {
    private static final Logger log = LoggerFactory.getLogger(SettingsPage.class);

    private SelenideElement nightMode = $(By.xpath("//android.widget.TextView[@text=\"Night Mode\"]/.."));
    SelenideElement nightModeToggle = $(By.xpath("//android.widget.TextView[@text='Night Mode']/following-sibling::android.view.View"));

    @Step
    public void clickNightMode() {

        String checked = nightModeToggle.getAttribute("checked");
        log.info("checked - {}", checked);

        log.info("Нажимаем на NightMode бокс");
        nightMode.should(Condition.visible).click();
        sleep(1000);
        String checked2 = nightModeToggle.getAttribute("checked");
        log.info("checked2 - {}", checked2);

        Assertions.assertNotEquals(checked, checked2, "Должно было произойти переключение темы");

    }

}
