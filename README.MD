## Тестовое задание

### Стек: Appium, Java 22, Selenide, Android Studio (Android 11 / api 30), Maven.

#### Написать автотест на мобильное приложение:

Алхимия:Головоломка com.ilyin.alchemy.  
https://play.google.com/store/apps/details?id=com.ilyin.alchemy  
Шаги прохождения теста:

1. Нажать на кнопку &quot;Играть&quot; в главном меню.
2. Нажать на добавление подсказок.
3. Получить подсказку за просмотр рекламы.
4. Тест завершается после проверки на то что количество
   подсказок было увеличено и значение равно 4.

# 🧪 Мобильное тестирование на Java + Appium + Selenide

Проект для автоматизации тестирования мобильного приложения (APK) на Android с использованием Java,
Appium, Selenide и Allure Reports.

### 📋 Содержание

* Технологии
* Структура проекта
* Предварительные требования
* Настройка конфигурации
* Запуск тестов
* Где смотреть результаты
* Важные особенности

### 🛠 Технологии

| Компонент        | 	Версия	 | Назначение                         |
|------------------|----------|------------------------------------|
| Java	            | 11+	     | Язык программирования              |
| Appium	          | 8.3.0	   | Автоматизация мобильных устройств  |
| Selenide	        | 6.19.1   | 	Удобные обертки над WebDriver     |
| JUnit 5	         | 5.8.2	   | Тестовый фреймворк                 |
| Allure           | 	2.29.0	 | Формирование отчетов               |
| Owner            | 	1.0.12	 | Работа с конфигурационными файлами |
| Image Comparison | 	4.4.0	  | Сравнение скриншотов               |

### 📁 Структура проекта

```text
src/
├── test/
│   ├── java/
│   │   └── ru/kuz/alchemy/
│   │       ├── config/          # Конфигурационные классы (чтение .properties)
│   │       │   ├── ConfigReader.java
│   │       │   ├── EmulatorConfig.java
│   │       │   └── TestConfig.java
│   │       │
│   │       ├── driver/           # Классы для работы с драйверами
│   │       │   ├── EmulatorDriver.java    # Создание AndroidDriver
│   │       │   └── EmulatorHelper.java    # Вспомогательные методы (back, скролл, клавиатура)
│   │       │
│   │       ├── helper/            # Вспомогательные утилиты
│   │       │   ├── ApkInfoHelper.java     # Чтение package/activity из APK
│   │       │   ├── Constants.java         # Константы (пути к папкам)
│   │       │   ├── DeviceHelper.java      # Выполнение bash команд
│   │       │   └── RunHelper.java         # Фабрика драйверов (выбор устройства)
│   │       │
│   │       └── listeners/         # Кастомные JUnit listeners
│   │           └── AllureListener.java    # Скриншоты при падении тестов
│   │
│   └── resources/
│       ├── app/                   # Мобильное приложение
│       │   └── alhimija.apk        # Тестируемое APK
│       └── configs/                # Конфигурационные файлы
│           ├── emulator.properties  # Настройки для эмулятора
│           └── test.properties      # Общие настройки тестов
└── build.gradle                     # Сборка проекта
```

## 🔧 Предварительные требования

1. Установленное ПО

* Java 11+
* Android Studio (для эмулятора)
* Appium Server (версия 2.x)
* Node.js (для Appium)

2. Настройка окружения

```bash
# Проверить версии
java -version
appium -v
adb --version
```

3. Создать эмулятор  
* Открыть Android Studio → Device Manager  
* Создать эмулятор с именем Pixel 4 (или изменить в emulator.properties)  
* Запустить эмулятор  

4. Запустить Appium сервер
```bash
appium --address 127.0.0.1 --port 4808
# или через Appium Desktop
```

## ⚙️ Настройка конфигурации
📄 Файл: src/test/resources/configs/emulator.properties
```properties
deviceName=Pixel 4                # Название эмулятора
platformName=android              # Платформа
remoteURL=http://127.0.0.1:4808/wd/hub  # Адрес Appium сервера
app=D:\\path\\to\\alhimija.apk    # АБСОЛЮТНЫЙ путь к APK

# Можно оставить пустыми - будут извлечены из APK автоматически
appPackage=
appActivity=
```
📄 Файл: src/test/resources/configs/test.properties
```properties
updateScreenshots = false          # true - обновить эталонные скриншоты
deviceHost = emulator              # emulator / browserstack / selenoid / real
```

## 🚀 Запуск тестов
Способ 1: Через терминал
```bash
# Запустить все тесты
./gradlew clean test

# Запустить конкретный тест-класс
./gradlew test --tests *ClassName*

# Запустить с обновлением скриншотов
./gradlew test -DupdateScreenshots=true
```

Способ 2: Через IntelliJ IDEA
1 Открыть проект  
2 Найти тестовый класс (в src/test/java)  
3 Нажать зеленый треугольник рядом с классом или методом  

Способ 3: Через Android Studio  
1 Открыть терминал внутри IDE  
2 Выполнить gradle команды  

## 📊 Где смотреть результаты
### 1. Allure отчет
После запуска тестов:
```bash
# Сгенерировать и открыть отчет
./gradlew allureServe
```

Отчет откроется в браузере: http://localhost:8080  
В отчете будут:  
✅ Успешные/упавшие тесты  
📸 Скриншоты упавших тестов  
📝 Логи выполнения  

### 2. Видео выполнения
Если настроено видео, будет в папке:
```text
build/videos/
```

### 3. Скриншоты

Актуальные: screenshots/actual/  
Эталонные: screenshots/expected/ (если используется сравнение)

### 4. Логи
```bash
# Логи выполнения в консоли
# Логи Appium сервера
# Логи в build/reports/tests/test/index.html
```

## 🎯 Важные особенности

Автоматическое определение package/activity
```java
// Если не указаны в .properties, извлекаются из APK через aapt
ApkInfoHelper helper = new ApkInfoHelper();
String appPackage = helper.getAppPackageFromApk();     // com.example.app
String appActivity = helper.getAppMainActivity();      // .MainActivity
```

Переключение между устройствами
В test.properties можно изменить:
```properties
deviceHost = emulator     # тесты на эмуляторе
deviceHost = real         # на реальном устройстве
deviceHost = browserstack # в облаке
```

Скриншоты при падении  
Автоматически прикрепляются к Allure отчету  
Сохраняются локально в screenshots/actual/  

Гибкая конфигурация  
Все настройки вынесены в .properties файлы  
Легко менять устройство, приложение, параметры  

## 🔍 Проверка подключения
Перед запуском тестов выполнить:

1. Проверить эмулятор:
```bash
adb devices
# Должен отобразиться: emulator-5554 device
```
2. Проверить Appium:
```bash
curl http://127.0.0.1:4808/status
# Должен вернуть JSON с "ready": true
```

3. Проверить путь к APK:

```bash
# Файл должен существовать по указанному пути
ls -la "D:\\path\\to\\alhimija.apk"
```

⚠️ Возможные проблемы и решения  

|Проблема|	Решение|
|--|--|
|aapt: command not found|	Указать правильный путь к build-tools в ApkInfoHelper|
|Appium не подключается|	Проверить порт 4808, запущен ли сервер|
|APK не найден	|Использовать абсолютный путь в emulator.properties|
|Эмулятор не виден	|adb kill-server и adb start-server|
|Тесты падают на скролле|	Проверить наличие элемента на экране|

## 📝 Пример Page Object
```java
public class MainPage {
    private static final SelenideElement searchField = $(By.id("com.example:id/search"));
    private static final SelenideElement searchButton = $(By.xpath("//*[@text='Поиск']"));
    
    public static void search(String query) {
        searchField.sendKeys(query);
        EmulatorHelper.closeKeyBoard();
        searchButton.click();
    }
}
```

# Happy Testing! 🚀