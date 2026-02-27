package ru.kuz.alchemy.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kuz.alchemy.driver.EmulatorHelper;
import ru.kuz.alchemy.pages.MainPage;
import ru.kuz.alchemy.pages.SettingsPage;

public class SettingsTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(SettingsTest.class);

    private MainPage mainPage;
    private SettingsPage settingsPage;


    @BeforeEach
    public void initPages() {
        mainPage = new MainPage();
        settingsPage = new SettingsPage();
    }

    @Test
    public void clickNightMode() {
        mainPage.waitForPageLoaded().tapSettings();
        settingsPage.clickNightMode();
        EmulatorHelper.goBack();
    }

}
