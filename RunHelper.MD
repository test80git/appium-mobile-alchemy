# 🎮 RunHelper — Фабрика драйверов

## Зачем нужен?

Чтобы выбирать, на каком устройстве запускать тесты:

📱 Эмулятор

📲 Реальное устройство

☁️ BrowserStack (облако)

🚀 Selenoid

## Как работает?

```java
public class RunHelper {

    public Class<?> getDriverClass() {
        // Читаем из test.properties: deviceHost = emulator
        String deviceHost = ConfigReader.testConfig.deviceHost();

        switch (deviceHost) {
            case "browserstack":
                // return BrowserstackMobileDriver.class;  // для облака

            case "selenoid":
                // return SelenoidMobileDriver.class;      // для Selenoid

            case "emulator":
                return EmulatorDriver.class;  // ← ВОЗВРАЩАЕТ ЭМУЛЯТОР!

            case "real":
                // return RealMobileDriver.class;           // для реального устройства

            default:
                throw new RuntimeException("Неизвестный deviceHost: " + deviceHost);
        }
    }
}
```

## Где используется?

В настройках тестов:

```java
public class BaseTest {

    @BeforeEach
    public void setUp() {
        // Selenide будет использовать тот драйвер, который вернет RunHelper
        Configuration.browser = RunHelper.runHelper().getDriverClass().getName();
    }
}
```

## Как легко переключить устройство:

```properties
# В файле test.properties просто меняем одну строчку:
deviceHost=emulator     # тесты на эмуляторе
# deviceHost = real       # тесты на реальном телефоне
# deviceHost = browserstack # тесты в облаке
```
