package utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class TokenManager {

    @Step("Получение токена для валидного пользователя из Constants")
    public static String getValidToken() {
        UserModel user = new UserModel(Constants.VALID_EMAIL, Constants.VALID_PASSWORD, Constants.USER_NAME);
        return getToken(user);
    }

    @Step("Получение токена для пользователя")
    public static String getToken(UserModel user) {
        Response response = Steps.loginUser(user);
        String token = response.jsonPath().getString("accessToken");
        if (token != null) {
            return token.replace("Bearer ", "");
        } else {
            return null;
        }
    }
}





