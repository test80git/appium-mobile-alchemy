# 📦 ApkInfoHelper — Читает информацию из APK

## Зачем нужен?

Чтобы автоматически извлекать из APK-файла то, что нужно для запуска приложения:

* package — идентификатор приложения (com.example.game)

* main activity — какой экран открывается первым

## Как работает?

```java
public class ApkInfoHelper {
    private String apkInfo;  // Содержит весь AndroidManifest.xml в виде текста

    public ApkInfoHelper() {
        String app = ConfigReader.emulatorConfig.app(); // путь к APK

        // ВЫЗЫВАЕТ КОМАНДУ: aapt dumb badging app.apk
        // Это утилита из Android SDK, которая читает манифест из APK
        apkInfo = executeSh(pathToAapt + "aapt dumb badging " + app);
    }

    // Вытаскивает package с помощью регулярки
    public String getAppPackageFromApk() {
        // Ищет строку: package: name='com.alchemy.game'
        return findGroup1ValueFromString(apkInfo, "package: name='\\s*([^']+?)\\s*'");
    }

    // Вытаскивает main activity с помощью регулярки
    public String getAppMainActivity() {
        // Ищет строку: launchable-activity: name='.MainActivity'
        return findGroup1ValueFromString(apkInfo, "launchable-activity: name='\\s*([^']+?)\\s*'");
    }
}
```

## Что внутри apkInfo после выполнения команды:

```text
package: name='com.alchemy.game' versionCode='1' versionName='1.0'
...
launchable-activity: name='com.alchemy.game.MainActivity' label='' icon=''
```

## Где используется?

В EmulatorDriver.initPackageAndActivity():

```java
if(appPackage ==null||appPackage.

isEmpty()){
ApkInfoHelper helper = new ApkInfoHelper();
appPackage =helper.

getAppPackageFromApk();  // "com.alchemy.game"

appActivity =helper.

getAppMainActivity();   // ".MainActivity"
}
```
