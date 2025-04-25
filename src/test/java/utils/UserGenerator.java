package utils;

import java.util.UUID;

public class UserGenerator {

    // Метод для генерации уникального email
    public static String generateUniqueEmail() {
        return "user_" + UUID.randomUUID() + "@yandex.com";
    }
}


