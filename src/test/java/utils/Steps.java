package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.Map;
import java.util.HashMap;


import static io.restassured.RestAssured.given;

public class Steps {

    // Регистрация нового пользователя
    public static Response registerNewUser(String email, String password, String name) {
        String userData = "{\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"name\": \"" + name + "\"\n" +
                "}";

        return given()
                .contentType("application/json")
                .body(userData)
                .when()
                .post(Constants.REGISTER_URL);
    }

    // Логин пользователя
    public static Response loginUser(String email, String password) {
        String loginData = "{\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\"\n" +
                "}";

        return given()
                .contentType("application/json")
                .body(loginData)
                .when()
                .post(Constants.LOGIN_URL);
    }

    // Обновление данных пользователя
    public static Response updateUser(String token, String name, String email, String password) {
        Map<String, Object> body = new HashMap<>();
        if (name != null) body.put("name", name);
        if (email != null) body.put("email", email);
        if (password != null) body.put("password", password);

        // Проверка на null для токена
        if (token == null) {
            return given()
                    .header("Content-type", "application/json")
                    .body(body)
                    .when()
                    .patch(Constants.UPDATE_USER_URL); // Проводим запрос без авторизации
        }

        // Если токен есть, добавляем его в запрос
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .body(body)
                .when()
                .patch(Constants.UPDATE_USER_URL);
    }



    // Создание заказа
    public static Response createOrder(String token, String orderDetails) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .body(orderDetails)
                .when()
                .post(Constants.CREATE_ORDER_URL);
    }

    // Метод для удаления пользователя
    public static void deleteUser(String token) {
        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(Constants.USER_DELETE_URL) // Используем константу для URL удаления
                .then()
                .statusCode(202);  // Ожидаем успешный ответ с кодом 200
    }
}


