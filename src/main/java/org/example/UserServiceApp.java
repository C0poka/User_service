package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.model.User;

import java.util.List;
import java.util.Scanner;

public class UserServiceApp {

    private static final UserDao userDao = new UserDaoImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            printMenu();
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> listUsers();
                    case 3 -> updateUser();
                    case 4 -> deleteUser();
                    case 5 -> {
                        exit = true;
                        System.out.println("Выход из программы.");
                    }
                    default -> System.out.println("Неверный выбор, попробуйте еще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите число от 1 до 5.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Меню пользователя ---");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Показать всех пользователей");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Выход");
        System.out.print("Выберите действие: ");
    }

    private static void createUser() {
        try {
            System.out.print("Имя: ");
            String name = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);

            userDao.save(user);
            System.out.println("Пользователь успешно создан:\n" + user);
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    private static void listUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Пользователи не найдены.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private static void updateUser() {
        try {
            System.out.print("Введите ID пользователя для обновления: ");
            Long id = Long.parseLong(scanner.nextLine());

            User user = userDao.findById(id);
            if (user == null) {
                System.out.println("Пользователь с таким ID не найден.");
                return;
            }

            System.out.print("Новое имя (оставьте пустым, чтобы не менять) [" + user.getName() + "]: ");
            String name = scanner.nextLine();

            System.out.print("Новый email (оставьте пустым, чтобы не менять) [" + user.getEmail() + "]: ");
            String email = scanner.nextLine();

            System.out.print("Новый возраст (оставьте пустым, чтобы не менять) [" + user.getAge() + "]: ");
            String ageStr = scanner.nextLine();

            if (!name.isBlank()) user.setName(name);
            if (!email.isBlank()) user.setEmail(email);
            if (!ageStr.isBlank()) user.setAge(Integer.parseInt(ageStr));

            userDao.update(user);
            System.out.println("Пользователь успешно обновлен:\n" + user);
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try {
            System.out.print("Введите ID пользователя для удаления: ");
            Long id = Long.parseLong(scanner.nextLine());

            User user = userDao.findById(id);
            if (user == null) {
                System.out.println("Пользователь с таким ID не найден.");
                return;
            }

            userDao.delete(user);
            System.out.println("Пользователь удалён.");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }
}