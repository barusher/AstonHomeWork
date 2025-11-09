package com.example.userservice;

import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.UserEntity;

import java.util.List;
import java.util.Scanner;

public class UserConsole {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDaoImpl();

    public static void main(String[] args) {
        System.out.println("Консольное приложение!");

        boolean isRunning = true;
        while (isRunning) {
            printMenu();
            String command = scanner.nextLine();

            switch (command) {
                case "1":
                    createUser();
                    break;
                case "2":
                    showAllUsers();
                    break;
                case "3":
                    getUserById();
                    break;
                case "4":
                    updateUser();
                    break;
                case "5":
                    deleteUser();
                    break;
                case "6": {
                    isRunning = false;
                    System.out.println("Cпасибо за проверку работы приложения :) ");
                    break;
                }
                default:
                    System.out.println("Неверная команда :( ");
            }
        }
    }

    private static void printMenu() {
        System.out.println("""
                1. Создать пользователя
                2. Показать всех пользователей
                3. Найти пользователя по ID
                4. Обновить пользователя
                5. Удалить пользователя
                6. Выйти
                Нажмите цифру для выбора:\s""");

    }

    private static void createUser() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.print("Введите email: ");
        String email = scanner.nextLine();

        System.out.print("Введите возраст: ");
        int age = Integer.parseInt(scanner.nextLine());

        UserEntity userEntity = new UserEntity(name, email, age);
        userDao.save(userEntity);
        System.out.println("Пользователь создан: " + userEntity);
    }

    private static void showAllUsers() {
        List<UserEntity> userEntities = userDao.getAll();
        if (userEntities == null || userEntities.isEmpty()) {
            System.out.println("Нет пользователей в базе.");
        } else {
            System.out.println("Список пользователей:");
            userEntities.forEach(System.out::println);
        }
    }

    private static void getUserById() {
        System.out.print("Введите ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        UserEntity userEntity = userDao.getById(id);
        if (userEntity != null)
            System.out.println("Найден: " + userEntity);
        else
            System.out.println("Пользователь с ID " + id + " не найден.");
    }

    private static void updateUser() {
        System.out.print("Введите ID пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());
        UserEntity userEntity = userDao.getById(id);

        if (userEntity == null) {
            System.out.println("Пользователь с таким ID не найден.");
            return;
        }

        System.out.print("Введите новое имя (" + userEntity.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) userEntity.setName(name);

        System.out.print("Введите новый email (" + userEntity.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) userEntity.setEmail(email);

        System.out.print("Введите новый возраст (" + userEntity.getAge() + "): ");
        String ageStr = scanner.nextLine();
        if (!ageStr.isEmpty()) userEntity.setAge(Integer.parseInt(ageStr));

        userDao.update(userEntity);
        System.out.println("Данные об пользователе обновлены: " + userEntity);
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = Long.parseLong(scanner.nextLine());
        userDao.delete(id);
    }
}
