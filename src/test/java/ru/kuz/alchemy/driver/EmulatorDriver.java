package ru.kuz.alchemy.driver;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codeborne.selenide.WebDriverProvider;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nonnull;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import ru.kuz.alchemy.config.ConfigReader;
import ru.kuz.alchemy.helper.ApkInfoHelper;

public class EmulatorDriver implements WebDriverProvider {

    private static final String DEVICE_NAME = ConfigReader.emulatorConfig.deviceName();
    private static final String PLATFORM_NAME = ConfigReader.emulatorConfig.platformName();
    private static final String APP = ConfigReader.emulatorConfig.app();
    private static final String REMOTE_URL = ConfigReader.emulatorConfig.remoteURL();

    private String appPackage = ConfigReader.emulatorConfig.appPackage();
    private String appActivity = ConfigReader.emulatorConfig.appActivity();

    private URL getUrl() {
        try {
            return new URL(REMOTE_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAbsolutePath(String filePath) {
        File file = new File(filePath);
        assertTrue(file.exists(), filePath + " not found");
        return file.getAbsolutePath();
    }

    private void initPackageAndActivity() {
        if (appPackage == null || appPackage.isEmpty() ||
                appActivity == null || appActivity.isEmpty()) {
            ApkInfoHelper helper = new ApkInfoHelper();
            appPackage = helper.getAppPackageFromApk();
            appActivity = helper.getAppMainActivity();
        }
    }

    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull Capabilities capabilities) {
        initPackageAndActivity();

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        caps.setCapability("deviceName", DEVICE_NAME);
        caps.setCapability("platformName", PLATFORM_NAME);
        caps.setCapability("appPackage", appPackage);
        caps.setCapability("appActivity", appActivity);
        caps.setCapability("app", getAbsolutePath(APP));
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("newCommandTimeout", 300);
        caps.setCapability("noReset", true);

        return new AndroidDriver(getUrl(), caps);
    }

}
