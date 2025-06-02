package utils;

import com.github.javafaker.Faker;

public class UserGenerator {
    private static final Faker faker = new Faker();

    public static String generateUniqueEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateRandomPassword() {
        return faker.internet().password(8, 16);
    }

    public static String generateRandomName() {
        return faker.name().firstName();
    }

    // Генерация полного пользователя
    public static UserModel generateRandomUser() {
        return new UserModel(
                generateUniqueEmail(),
                generateRandomPassword(),
                generateRandomName()
        );
    }
}





