package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.*;

import static org.hamcrest.Matchers.*;

public class UserUpdateTest {

    private UserModel originalUser;
    private String token;

    @Before
    public void setUp() {
        originalUser = UserGenerator.generateRandomUser();
        Response response = Steps.registerNewUser(originalUser);
        response.then().statusCode(HttpStatus.SC_OK);

        token = response.then().extract().path("accessToken").toString();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

    }

    @After
    public void tearDown() {
        if (token != null) {
            Steps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Обновление имени пользователя с авторизацией")
    @Description("Проверка успешного обновления имени пользователя с токеном")
    public void updateNameWithAuthTest() {
        UserModel updatedUser = new UserModel(
                originalUser.getEmail(),  // текущий email обязательно
                null,
                UserGenerator.generateRandomName()
        );

        Steps.updateUser(token, updatedUser)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.name", equalTo(updatedUser.getName()));
    }

    @Test
    @DisplayName("Обновление email пользователя с авторизацией")
    @Description("Проверка успешного обновления email пользователя с токеном")
    public void updateEmailWithAuthTest() {
        UserModel updatedUser = new UserModel(
                UserGenerator.generateUniqueEmail(),
                "",
                ""
        );

        Steps.updateUser(token, updatedUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedUser.getEmail().toLowerCase()));
    }

    @Test
    @DisplayName("Обновление пароля пользователя с авторизацией")
    @Description("Проверка успешного обновления пароля пользователя с токеном")
    public void updatePasswordWithAuthTest() {
        UserModel updatedUser = new UserModel(
                originalUser.getEmail(),  // передаем текущий email обязательно
                UserGenerator.generateRandomPassword(),
                null
        );

        Steps.updateUser(token, updatedUser)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
        // Можно дополнительно залогиниться с новым паролем для проверки
    }

    @Test
    @DisplayName("Попытка обновления данных без авторизации")
    @Description("Проверка невозможности обновления данных без токена авторизации")
    public void updateWithoutAuthTest() {
        UserModel updatedUser = new UserModel(
                UserGenerator.generateUniqueEmail(),
                UserGenerator.generateRandomPassword(),
                UserGenerator.generateRandomName()
        );

        Steps.updateUser("", updatedUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }

    @Test
    @DisplayName("Попытка обновить email на уже зарегистрированный")
    @Description("Проверка ошибки при попытке изменить email на уже существующий")
    public void updateUserEmailToExistingOneTest() {
        // Регистрация второго пользователя
        UserModel secondUser = UserGenerator.generateRandomUser();
        Steps.registerNewUser(secondUser).then().statusCode(HttpStatus.SC_OK);

        // Попытка обновить email первого пользователя на email второго
        UserModel updatedUser = new UserModel(
                secondUser.getEmail(),
                null,
                null
        );

        Steps.updateUser(token, updatedUser)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(Constants.USER_ALREADY_EXISTS_ERROR));
    }
}





