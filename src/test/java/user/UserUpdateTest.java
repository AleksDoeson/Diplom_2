package user;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.TokenManager;
import utils.Steps;
import utils.UserGenerator;

import static org.hamcrest.Matchers.*;

public class UserUpdateTest {

    private String token;
    private final String newName = "UpdatedUserName";
    private String email;
    private final String password = Constants.DEFAULT_USER_PASSWORD;

    @Before
    @Step("Получение токена")
    @Description("Получение токена")
    public void setUp() {
        email = UserGenerator.generateUniqueEmail();
        String name = Constants.USER_NAME;
        Response response = Steps.registerNewUser(email, password, name);
        response.then().statusCode(200);
        token = response.path("accessToken").toString().replace("Bearer ", "");
    }

    @After
    @Step("Удаление пользователя после теста")
    @Description("Удаление пользователя после теста")
    public void cleanUp() {
        if (token != null) {
            Steps.deleteUser(token);
        }
    }

    @Test
    @Step("Обновление имени пользователя с авторизацией")
    @Description("Обновление имени пользователя с авторизацией")
    public void updateNameWithAuthTest() {
        Steps.updateUser(token, newName, null, null)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName));
    }

    @Test
    @Step("Обновление email пользователя с авторизацией")
    @Description("Обновление email пользователя с авторизацией")
    public void updateEmailWithAuthTest() {
        String newEmail = UserGenerator.generateUniqueEmail();
        Steps.updateUser(token, null, newEmail, null)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail.toLowerCase()));
    }

    @Test
    @Step("Обновление пароля пользователя с авторизацией")
    @Description("Обновление пароля пользователя с авторизацией")
    public void updatePasswordWithAuthTest() {
        String newPassword = "NewPassword123";
        Steps.updateUser(token, null, null, newPassword)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
        // Можно добавить логин с новым паролем для подтверждения
    }

    @Test
    @Step("Попытка обновления имени пользователя без авторизации")
    @Description("Попытка обновления имени пользователя без авторизации")
    public void updateNameWithoutAuthTest() {
        Steps.updateUser(null, newName, null, null)
                .then()
                .statusCode(401)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }

    @Test
    @Step("Попытка обновления email пользователя без авторизации")
    @Description("Попытка обновления email пользователя без авторизации")
    public void updateEmailWithoutAuthTest() {
        String newEmail = UserGenerator.generateUniqueEmail();
        Steps.updateUser(null, null, newEmail, null)
                .then()
                .statusCode(401)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }

    @Test
    @Step("Попытка обновления пароля пользователя без авторизации")
    @Description("Попытка обновления пароля пользователя без авторизации")
    public void updatePasswordWithoutAuthTest() {
        Steps.updateUser(null, null, null, "newPassword123")
                .then()
                .statusCode(401)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }
    @Test
    @Step("Попытка изменить email на уже зарегистрированный")
    @Description("Попытка изменить email на уже зарегистрированный")
    public void updateUserEmailToExistingOneTest() {
        // Регистрация второго пользователя
        String existingEmail = UserGenerator.generateUniqueEmail();
        Response secondUser = Steps.registerNewUser(existingEmail, password, "SecondUser");
        secondUser.then().statusCode(200);

        // Попытка обновить email текущего пользователя на существующий
        Steps.updateUser(token, null, existingEmail, null)
                .then()
                .statusCode(403)
                .body("message", equalTo(Constants.USER_ALREADY_EXISTS_ERROR));
    }


}


