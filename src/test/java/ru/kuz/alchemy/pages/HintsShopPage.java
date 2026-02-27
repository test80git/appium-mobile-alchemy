package ru.kuz.alchemy.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class HintsShopPage {
    private static final Logger log = LoggerFactory.getLogger(HintsShopPage.class);

    // Кнопка Cancel в Google Play Games (если вылезет)
    private SelenideElement cancelButton = $(By.id("com.google.android.gms.optional_games:id/signin_cancel_button"));

    // Первый блок подсказок (быстрый, 2 hints)
    private SelenideElement fastHintBlock = $(By.xpath("//android.view.View[@bounds='[44,402][946,666]']"));
    private SelenideElement loadingText = $(By.xpath("//android.widget.TextView[@text='Loading']"));
    private SelenideElement noAdsText = $(By.xpath("//android.widget.TextView[@text='No ads :(']"));

    // Второй блок подсказок (медленный, 2 hints)
    private SelenideElement slowHintBlock = $(By.xpath("//android.view.View[@bounds='[44,688][946,1084]']"));
    private SelenideElement claimButton = $(By.xpath("//android.widget.TextView[@text='Claim!']/.."));

    @Step("Закрываем окно входа в Google Play Games если оно появилось")
    public HintsShopPage closeGoogleSignInIfPresent() {
        try {
            if (cancelButton.exists()) {
                log.info("Закрываем окно Google Play Games");
                cancelButton.click();
                sleep(2000);
            }
        } catch (Exception e) {
            log.info("Окно Google Play Games не появилось");
        }
        return this;
    }

    @Step("Проверяем доступен ли быстрый блок подсказок")
    public boolean isFastHintAvailable() {
        return fastHintBlock.exists() && !noAdsText.exists();
    }

    @Step("Ожидаем загрузки быстрого блока")
    public HintsShopPage waitForFastBlockToLoad() {
        log.info("Ожидаем загрузки быстрого блока...");
        loadingText.should(Condition.disappear, Duration.ofSeconds(30));
        return this;
    }

    @Step("Проверяем видимость кнопки Claim в быстром блоке")
    public boolean isClaimInFastBlockVisible() {
        SelenideElement claimInFast = fastHintBlock
                .find(By.xpath(".//android.widget.TextView[@text='Claim!']/.."));
        return claimInFast.exists();
    }

    @Step("Нажимаем Claim в быстром блоке")
    public HintsShopPage clickClaimInFastBlock() {
        log.info("Нажимаем Claim в быстром блоке");
        SelenideElement claimInFast = fastHintBlock
                .find(By.xpath(".//android.widget.TextView[@text='Claim!']/.."));
        claimInFast.should(Condition.visible).click();
        sleep(3000);
        return this;
    }

    @Step("Используем быстрый блок (максимум 2 минуты)")
    public HintsShopPage useFastHint() {
        log.info("Используем быстрый блок подсказок");

        int maxWaitSeconds = 120; // 2 минуты
        long startTime = System.currentTimeMillis();
        boolean loadingSeen = false;
        boolean noAdsSeen = false;

        log.info("⏳ Ожидаем появления кнопки Claim! (максимум {} секунд)", maxWaitSeconds);

        while (System.currentTimeMillis() - startTime < maxWaitSeconds * 1000L) {

            // Проверяем наличие Loading
            if (!loadingSeen && loadingText.exists()) {
                loadingSeen = true;
                log.info("   🔄 Появился Loading... ждем загрузки");
            }

            // Проверяем наличие No ads :(
            if (!noAdsSeen && noAdsText.exists()) {
                noAdsSeen = true;
                log.info("   😕 Появился 'No ads :(' - реклама недоступна");
            }

            // Проверяем кнопку Claim
            SelenideElement claimInFast = fastHintBlock
                    .find(By.xpath(".//android.widget.TextView[@text='Claim!']/.."));

            if (claimInFast.exists()) {
                log.info("✅ Кнопка Claim! появилась!");
                log.info("   Статусы: Loading был: {}, No ads был: {}", loadingSeen, noAdsSeen);
                claimInFast.click();
                sleep(3000);
                return this;
            }

            sleep(2000); // проверяем каждые 2 секунды
        }

        log.warn("⚠️ Кнопка Claim не появилась за {} секунд", maxWaitSeconds);
        log.warn("   Видели Loading: {}, видели No ads: {}", loadingSeen, noAdsSeen);

        // Если Claim не появился, но был No ads - пробуем медленный блок
        if (noAdsSeen) {
            log.info("🔄 Был 'No ads', переключаемся на медленный блок");
            return useSlowHint();
        }

        throw new RuntimeException("❌ Кнопка Claim! не появилась за " + maxWaitSeconds + " секунд");
    }

    @Step("Используем медленный блок (ждем появления Claim!)")
    public HintsShopPage useSlowHint() {
        log.info("Используем медленный блок подсказок");

        int maxWaitMinutes = 7;
        long startTime = System.currentTimeMillis();

        log.info("⏳ Ожидаем появления кнопки Claim! (максимум {} минут)", maxWaitMinutes);

        for (int minute = 0; minute < maxWaitMinutes; minute++) {
            for (int half = 0; half < 2; half++) {
                if (claimButton.exists()) {
                    log.info("✅ Кнопка Claim! появилась!");
                    claimButton.click();
                    sleep(3000);
                    return this;
                }
                log.info("   Кнопка еще не появилась, ждем 30 секунд...");
                sleep(30000);
            }
            log.info("Прошло {} минут", minute + 1);
        }

        if (claimButton.exists()) {
            log.info("✅ Кнопка Claim! появилась!");
            claimButton.click();
            sleep(3000);
        } else {
            throw new RuntimeException("❌ Кнопка Claim! не появилась за " + maxWaitMinutes + " минут");
        }
        return this;
    }
}