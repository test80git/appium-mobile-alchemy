# 📱 Шпаргалка: appPackage и appActivity для Appium

## 📌 Коротко: для чего нужны
* appPackage — чтобы Appium знал, какое приложение запускать

* appActivity — чтобы Appium знал, с какого экрана начать

Без них Appium не сможет запустить приложение (только устанавливать APK)

## 🔍 Что это и зачем нужно

| Параметр     | 	Что это	                                                                                 | Зачем нужно                                             |
|--------------|-------------------------------------------------------------------------------------------|---------------------------------------------------------|
| appPackage   | 	Уникальный идентификатор приложения (как com.vk.vkvideo)	                                | Appium должен знать, какое приложение запускать         |
| appActivity	 | Конкретный экран/активность приложения (например, com.vk.video.screens.main.MainActivity) | 	Appium должен знать, какой именно экран открыть первым |

## 🛠 Все способы найти appPackage и appActivity

## 📋 Способ 1: Через ADB (самый надежный)
Для appPackage:
```bash
# Все установленные пакеты
adb shell pm list packages

# С фильтром по имени
adb shell pm list packages | findstr alchemy
# package:com.ilyin.alchemy

# Только сторонние приложения (не системные)
adb shell pm list packages -3 | findstr alchemy
```

Для appActivity (главная активность):
Метод А: Через текущее окно (быстрый)

```bash
# 1. Запусти приложение вручную на эмуляторе
# 2. Выполни:
adb shell dumpsys window windows | findstr mCurrentFocus
Пример вывода:
mCurrentFocus=Window{... com.vk.vkvideo/com.vk.video.screens.main.MainActivity}
```

Метод Б: Через logcat (подробный)

```bash
# Очисти логи
adb logcat -c

# Запусти приложение вручную
# Затем:
adb logcat -d | findstr Start
# 03-06 09:05:04.981   544   579 I ActivityManager: Start proc 8260:com.ilyin.alchemy:AppMetrica/u0a155 for service {com.ilyin.alchemy/io.appmetrica.analytics.internal.AppMetricaService}
```

Ищи строку с cmp= — там будет package/activity.

## 📦 Способ 2: Через APK файл (aapt)
Если у вас есть APK:

```bash
# Получить appPackage
aapt dump badging app.apk | findstr package

# Получить appActivity (launchable activity)
aapt dump badging app.apk | findstr launchable-activity
```
---

## 🏪 Способ 3: Через Google Play (если приложение установлено)
1. Открой Google Play на эмуляторе

2. Найди приложение

3. В URL страницы приложения будет package:
https://play.google.com/store/apps/details?id=com.vk.vkvideo
→ appPackage = com.vk.vkvideo

## 🔧 Способ 4: Через Android Studio (если есть исходники)
В файле AndroidManifest.xml:

```xml
<manifest package="com.vk.vkvideo">  <!-- это appPackage -->
<application>
<activity android:name=".MainActivity">  <!-- это appActivity -->
<intent-filter>
<action android:name="android.intent.action.MAIN" />
<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
</activity>
</application>
</manifest>
```

## ⚡ Самый быстрый способ для запущенного приложения
```bash
# 1. Запусти приложение на эмуляторе
# 2. Выполни:
adb shell dumpsys window windows | findstr mCurrentFocus
```

Из выхода:

```text
mCurrentFocus=Window{... com.vk.vkvideo/com.vk.video.screens.main.MainActivity}
```

* До / → appPackage = com.vk.vkvideo  
* После / → appActivity = com.vk.video.screens.main.MainActivity  

## ✅ Пример готовых настроек
```properties
# Если приложение НЕ установлено (ставится из APK)
appPackage=com.vk.vkvideo
appActivity=com.vk.video.screens.main.MainActivity
app=D:\\путь\\к\\vkvideo.apk

# Если приложение УЖЕ установлено на эмуляторе
appPackage=com.vk.vkvideo
appActivity=com.vk.video.screens.main.MainActivity
# app= (не указываем)
```


# ADB (Android Debug Bridge) — это консольная утилита для управления Android-устройствами с компьютера . Она входит в состав Android SDK Platform-Tools.

## 📥 Где взять и как установить
1. Скачай Platform-Tools:

* Официальная страница: developer.android.com/studio/releases/platform-tools

* Скачай zip-архив для Windows

2. Распакуй в удобную папку, например: C:\platform-tools

3. Добавь в PATH (чтобы работать из любой папки):

* Win + R → sysdm.cpl → Advanced → Environment Variables

* В Path добавь: C:\platform-tools

## 🚀 Как запускать
1. Подключи эмулятор (он должен быть запущен)

2. Открой командную строку (cmd) или PowerShell

3. Проверь связь:

```bash
adb devices
```

Должен появиться твой эмулятор (например, emulator-5554 device)

4. Выполняй нужные команды. Например, для получения package:

```bash
adb shell pm list packages | findstr vk
```

Вот и всё — ADB готов к работе!
