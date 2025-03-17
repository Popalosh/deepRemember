package org.example;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Gson gson = new Gson();
    private static final ContextStorage contextStorage = new ContextStorage(new FileHandlerImpl());

    public static void mainMenu() {
        while (true) {
            System.out.println("\n=== Меню ===");
            System.out.println("1. Создать новый контекст");
            System.out.println("2. Загрузить контекст из JSON-файла");
            System.out.println("3. Восстановить контекст по UID");
            System.out.println("4. Показать все UID с комментариями");
            System.out.println("5. Сбросить все данные");
            System.out.println("6. Выйти");
            System.out.print("Выберите действие: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Очистка буфера после nextInt()

                switch (choice) {
                    case 1:
                        createNewContext();
                        break;
                    case 2:
                        loadContextFromJsonFile();
                        break;
                    case 3:
                        restoreContext();
                        break;
                    case 4:
                        showAllUIDsWithComments();
                        break;
                    case 5:
                        resetStorage();
                        break;
                    case 6:
                        System.out.println("Выход из программы.");
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Неверный ввод. Пожалуйста, введите число.");
                scanner.nextLine(); // Очистка буфера в случае ошибки
            }
        }
    }

    private static void createNewContext() {
        System.out.println("\n=== Создание нового контекста ===");

        System.out.print("Введите тему: ");
        String topic = scanner.nextLine();

        System.out.print("Введите цель: ");
        String goal = scanner.nextLine();

        System.out.print("Введите шаги (через запятую): ");
        List<String> steps = Arrays.asList(scanner.nextLine().split(","));

        System.out.print("Введите текущий шаг: ");
        String currentStep = scanner.nextLine();

        System.out.print("Введите код (если есть): ");
        String code = scanner.nextLine();

        System.out.print("Введите заметки: ");
        String notes = scanner.nextLine();

        // Создаём контекст
        Context context = new Context(topic, goal, steps, currentStep, code, notes);

        // Создаём экземпляр ContextManager через фабричный метод
        ContextManager contextManager = ContextManager.create();

        // Генерируем UID
        String uid = contextManager.generateUID(context);
        System.out.println("\nСгенерированный UID: " + uid);

        // Сохраняем контекст
        try {
            contextStorage.saveContext(uid, context);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении контекста: " + e.getMessage());
            return;
        }

        // Запрашиваем комментарий для UID
        System.out.print("Введите комментарий для UID: ");
        String comment = scanner.nextLine();
        try {
            contextStorage.saveUidWithComment(uid, comment);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении комментария: " + e.getMessage());
            return;
        }

        System.out.println("Контекст успешно сохранён.");
    }

    private static void loadContextFromJsonFile() {
        System.out.println("\n=== Загрузка контекста из JSON-файла ===");

        System.out.print("Введите путь к JSON-файлу: ");
        String filePath = scanner.nextLine();

        try (FileReader reader = new FileReader(filePath)) {
            Context context = gson.fromJson(reader, Context.class);

            // Создаём экземпляр ContextManager через фабричный метод
            ContextManager contextManager = ContextManager.create();

            // Генерируем UID
            String uid = contextManager.generateUID(context);
            System.out.println("\nСгенерированный UID: " + uid);

            // Сохраняем контекст
            contextStorage.saveContext(uid, context);

            // Запрашиваем комментарий для UID
            System.out.print("Введите комментарий для UID: ");
            String comment = scanner.nextLine();
            contextStorage.saveUidWithComment(uid, comment);

            System.out.println("Контекст успешно загружен и сохранён.");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private static void restoreContext() {
        System.out.println("\n=== Восстановление контекста ===");

        System.out.print("Введите UID: ");
        String uid = scanner.nextLine();

        // Восстанавливаем контекст
        try {
            Context context = contextStorage.loadContext(uid);
            if (context != null) {
                System.out.println("\nКонтекст восстановлен:");
                System.out.println("Тема: " + context.getTopic());
                System.out.println("Цель: " + context.getGoal());
                System.out.println("Шаги: " + String.join(", ", context.getSteps()));
                System.out.println("Текущий шаг: " + context.getCurrentStep());
                System.out.println("Код: " + (context.getCode() != null ? context.getCode() : "отсутствует"));
                System.out.println("Заметки: " + context.getNotes());

                // Сохраняем восстановленный контекст в JSON
                contextStorage.saveRestoredContextToJson(uid, context);
                System.out.println("Контекст сохранён в файл restored_context_" + uid + ".json");
            } else {
                System.out.println("Контекст с UID " + uid + " не найден.");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при восстановлении контекста: " + e.getMessage());
        }
    }

    private static void showAllUIDsWithComments() {
        System.out.println("\n=== Все UID с комментариями ===");
        try {
            List<String[]> uidsWithComments = contextStorage.getAllUIDsWithComments();
            if (uidsWithComments.isEmpty()) {
                System.out.println("Сохранённых UID нет.");
            } else {
                for (String[] uidWithComment : uidsWithComments) {
                    System.out.println("UID: " + uidWithComment[0] + " | Комментарий: " + uidWithComment[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении UID: " + e.getMessage());
        }
    }

    private static void resetStorage() {
        System.out.println("\n=== Сброс всех данных ===");
        System.out.print("Вы уверены, что хотите удалить все данные? (да/нет): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("да")) {
            try {
                contextStorage.resetStorage();
                System.out.println("Все данные успешно удалены.");
            } catch (IOException e) {
                System.out.println("Ошибка при сбросе данных: " + e.getMessage());
            }
        } else {
            System.out.println("Сброс отменён.");
        }
    }

    public static void main(String[] args) {
        mainMenu();
    }
}