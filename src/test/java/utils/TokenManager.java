package utils;

import io.restassured.response.Response;

public class TokenManager {

    // Метод для получения валидного токена пользователя
    public static String getValidToken() {
        // Для тестов обычно отправляется запрос на логин, чтобы получить токен
        Response response = Steps.loginUser(Constants.VALID_EMAIL, Constants.VALID_PASSWORD);

        // Извлекаем токен из ответа
        return response.jsonPath().getString("accessToken");
    }
}


