## 📱 **Что делает EmulatorDriver?**

**EmulatorDriver** — это фабрика для создания подключения к эмулятору Android через Appium. Он реализует интерфейс `WebDriverProvider` от Selenide и отвечает за:

---

## 🎯 **Основные задачи**

### 1. **Создает и настраивает AndroidDriver**
```java
return new AndroidDriver(getUrl(), caps);  // Создает сессию с эмулятором
```

### 2. **Читает конфигурацию из emulator.properties**
```java
private static final String DEVICE_NAME = ConfigReader.emulatorConfig.deviceName();  // "Pixel 4"
private static final String PLATFORM_NAME = ConfigReader.emulatorConfig.platformName(); // "android"
private static final String APP = ConfigReader.emulatorConfig.app();  // путь к APK
private static final String REMOTE_URL = ConfigReader.emulatorConfig.remoteURL();  // http://127.0.0.1:4808/wd/hub
```

### 3. **Определяет package и activity приложения**
```java
private void initPackageAndActivity() {
    if (appPackage == null || appPackage.isEmpty() ||
        appActivity == null || appActivity.isEmpty()) {
        ApkInfoHelper helper = new ApkInfoHelper();  // Читает из APK!
        appPackage = helper.getAppPackageFromApk();   // например: "com.alchemy.game"
        appActivity = helper.getAppMainActivity();    // например: ".MainActivity"
    }
}
```

### 4. **Настраивает DesiredCapabilities для Appium**
```java
DesiredCapabilities caps = new DesiredCapabilities();
caps.setCapability("deviceName", DEVICE_NAME);           // Имя устройства
caps.setCapability("platformName", PLATFORM_NAME);       // Android
caps.setCapability("appPackage", appPackage);            // Пакет приложения
caps.setCapability("appActivity", appActivity);          // Главная Activity
caps.setCapability("app", getAbsolutePath(APP));         // Путь к APK
caps.setCapability("automationName", "UiAutomator2");    // Драйвер автоматизации
caps.setCapability("autoGrantPermissions", true);        // Автоматически разрешать права
caps.setCapability("noReset", true);                      // Не сбрасывать данные приложения
```

### 5. **Проверяет существование APK файла**
```java
private String getAbsolutePath(String filePath) {
    File file = new File(filePath);
    assertTrue(file.exists(), filePath + " not found");  // Если файла нет - тест упадет
    return file.getAbsolutePath();
}
```

---

## 🔄 **Процесс работы EmulatorDriver**

```
1. Запуск теста
   ↓
2. Selenide вызывает createDriver()
   ↓
3. Чтение настроек из emulator.properties
   ↓
4. Проверка существования APK
   ↓
5. Если package/activity не указаны → извлечь из APK через aapt
   ↓
6. Сформировать DesiredCapabilities
   ↓
7. Создать AndroidDriver (подключиться к Appium)
   ↓
8. Вернуть драйвер в Selenide
   ↓
9. Тест выполняется на эмуляторе!
```

---

## 📊 **Что происходит в каждой строке:**

```java
@Nonnull
@Override
public WebDriver createDriver(@Nonnull Capabilities capabilities) {
    // 1. Получаем package и activity (из APK или пропертей)
    initPackageAndActivity();

    // 2. Создаем капабилитис
    DesiredCapabilities caps = new DesiredCapabilities();
    
    // 3. Разрешать все запросы прав автоматически
    caps.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
    
    // 4. Имя устройства (должно совпадать с созданным в AVD)
    caps.setCapability("deviceName", DEVICE_NAME);
    
    // 5. Платформа (android)
    caps.setCapability("platformName", PLATFORM_NAME);
    
    // 6. Пакет приложения (например, com.example.game)
    caps.setCapability("appPackage", appPackage);
    
    // 7. Главная активити (например, .MainActivity)
    caps.setCapability("appActivity", appActivity);
    
    // 8. Путь к APK файлу (абсолютный)
    caps.setCapability("app", getAbsolutePath(APP));
    
    // 9. Используем UiAutomator2 (современный драйвер для Android)
    caps.setCapability("automationName", "UiAutomator2");
    
    // 10. Таймаут на новые команды (5 минут)
    caps.setCapability("newCommandTimeout", 300);
    
    // 11. Не сбрасывать данные приложения между сессиями
    caps.setCapability("noReset", true);

    // 12. Создаем драйвер и подключаемся к Appium
    return new AndroidDriver(getUrl(), caps);
}
```

---

## 💡 **Почему это удобно?**

### ✅ **Автоматическое определение package/activity**
Не нужно искать в манифесте - программа сама извлечет из APK!

### ✅ **Гибкая конфигурация**
Все параметры в одном файле `emulator.properties`

### ✅ **Проверка ошибок**
Если APK не найден - тест упадет с понятным сообщением

### ✅ **Готовые капабилитис**
Все необходимые настройки уже прописаны

### ✅ **Интеграция с Selenide**
Selenide сам управляет жизненным циклом драйвера

---

## 🔧 **Пример использования в тесте:**

```java
public class AlchemyTest {
    
    @BeforeEach
    public void setUp() {
        // Selenide использует EmulatorDriver для создания драйвера
        Configuration.browser = RunHelper.runHelper().getDriverClass().getName();
    }
    
    @Test
    public void testAppLaunch() {
        // Драйвер уже создан, приложение запущено!
        $(By.id("com.alchemy.game:id/startButton")).click();
        // ...
    }
}
```

---

## 🎯 **Коротко: EmulatorDriver - это "мост" между тестами и эмулятором**

Он берет на себя всю рутину:
- 📍 Где лежит APK?
- 📍 Как называется приложение?
- 📍 Какая главная Activity?
- 📍 Как подключиться к Appium?
- 📍 Какие параметры передать?

И отдает **готовый драйвер** для работы с приложением!