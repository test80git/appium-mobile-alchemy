# 💻 DeviceHelper — Выполняет bash команды

## Зачем нужен?

Чтобы выполнять системные команды из Java (как в терминале)

## Два метода:

### Метод 1: executeSh (асинхронный)

```java
public static String executeSh(String command) throws IOException, ExecutionException, InterruptedException {
    Process p = Runtime.getRuntime().exec(command);  // Запускаем процесс

    FutureTask<String> future = new FutureTask<>(() -> {
        // Читаем результат в отдельном потоке
        return new BufferedReader(new InputStreamReader(p.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
    });

    new Thread(future).start();  // Запускаем поток для чтения
    return future.get();         // Ждем результат
}
```

### Метод 2: executeBash (синхронный)

```java
public static String executeBash(String command) {
    Process p = Runtime.getRuntime().exec(command);
    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

    StringBuilder output = new StringBuilder();
    String line;
    while ((line = input.readLine()) != null) {
        output.append(line).append("\n");  // Читаем построчно
    }
    p.waitFor();  // Ждем завершения процесса
    return output.toString();
}
```

## Примеры использования:

```java
// ApkInfoHelper использует:
apkInfo =DeviceHelper.

executeSh("C:\\Android\\aapt dumb badging app.apk");

// Можно использовать для других команд:
DeviceHelper.

executeBash("adb devices");  // список устройств
DeviceHelper.

executeBash("adb install app.apk");  // установить приложение
DeviceHelper.

executeBash("adb shell dumpsys battery");  // информация о батарее
```
