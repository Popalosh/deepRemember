# Context Manager

Проект для управления контекстами с использованием UID и JSON. Позволяет сохранять, восстанавливать и передавать контексты между диалогами.

## Описание

Проект включает следующие функции:
1. **Создание контекста**: Пользователь вводит данные (тема, цель, шаги, код, заметки), и программа генерирует уникальный UID.
2. **Сохранение контекста**: Контекст сохраняется в сжатом файле (`context_storage.gz`), а UID с комментарием — в текстовом файле (`uids.txt`).
3. **Восстановление контекста**: По UID можно восстановить контекст и сохранить его в JSON-файл.
4. **Просмотр всех UID**: Пользователь может просмотреть все сохранённые UID с комментариями.
5. **Сброс данных**: Возможность удалить все сохранённые данные.

## Требования

- Java 17 или выше.
- Maven для сборки проекта.
- Библиотека Gson для работы с JSON.

## Установка

1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/ваш-username/context-manager.git
    ```
2. Перейдите в директорию проекта:

    ```bash
    cd context-manager
    ```
3. Соберите проект с помощью Maven:

    ```bash
    mvn clean package
    ```
## Использование

Запустите программу:
```bash
java -jar target/deepRemember-1.0-SNAPSHOT.jar
```
Следуйте инструкциям в консольном меню:

1. **Создать новый контекст**.
2. **Загрузить контекст из JSON-файла**.
3. **Восстановить контекст по UID**.
4. **Показать все UID с комментариями**.
5. **Сбросить все данные**.
6. **Выйти**.

## Пример JSON-шаблона для запроса контекста

```json
{
  "uid": "a1b2c3d4",
  "comment": "Необходимо продолжить обсуждение разработки алгоритма сортировки."
}
```
## Пример работы программы
```
=== Меню ===
1. Создать новый контекст
2. Загрузить контекст из JSON-файла
3. Восстановить контекст по UID
4. Показать все UID
5. Выйти
Выберите действие: 1

=== Создание нового контекста ===
Введите тему: Разработка алгоритма
Введите цель: Создать эффективный алгоритм сортировки
Введите шаги (через запятую): Анализ задачи, Написание кода, Тестирование
Введите текущий шаг: Написание кода
Введите код (если есть): public void sort(int[] arr) { ... }
Введите заметки: Нужно оптимизировать время выполнения.

Сгенерированный UID: a1b2c3d4
Введите комментарий для UID: Первый контекст
Контекст успешно сохранён.
```

## Структура проекта
```
context-manager/
├── pom.xml
├── README.md
├── .gitignore
└── src/
    ├── main/
    │   └── java/
    │       └── org/
    │           └── example/
    │               ├── ConsoleUI.java
    │               ├── Context.java
    │               ├── ContextManager.java
    │               ├── ContextStorage.java
    │               ├── FileHandler.java
    │               ├── FileHandlerImpl.java
    │               └── Main.java
    └── test/
        └── java/
            └── org/
                └── example/
                    ├── ContextManagerTest.java
                    ├── ContextStorageTest.java
                    └── FileHandlerTest.java
```
## Зависимости

Проект использует следующие зависимости:
- **Gson** для работы с JSON.
- **JUnit 5** для тестирования.
- **Mockito** для мокирования в тестах.

## Лицензия

Этот проект распространяется под лицензией MIT. Подробности см. в файле [LICENSE](LICENSE).