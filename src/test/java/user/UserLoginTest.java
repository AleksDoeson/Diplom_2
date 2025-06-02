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

public class UserLoginTest {

    private UserModel user;
    private String token;

    @Before
    public void setUp() {
        user = UserGenerator.generateRandomUser();
        Steps.registerNewUser(user).then().statusCode(HttpStatus.SC_OK);
        token = TokenManager.getToken(user);
    }

    @After
    public void tearDown() {
        if (token != null) {
            Steps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Успешная авторизация с валидными данными")
    @Description("Проверка успешной авторизации пользователя с корректными email и паролем")
    public void loginWithValidData() {
        Response response = Steps.loginUser(user);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("accessToken", notNullValue())
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Авторизация с невалидным email")
    @Description("Проверка ошибки авторизации при использовании невалидного email")
    public void loginWithInvalidEmail() {
        UserModel invalidEmailUser = new UserModel(
                "invalid" + user.getEmail(),
                user.getPassword(),
                user.getName()
        );
        Steps.loginUser(invalidEmailUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(Constants.INVALID_CREDENTIALS_ERROR));
    }

    @Test
    @DisplayName("Авторизация с невалидным паролем")
    @Description("Проверка ошибки авторизации при использовании невалидного пароля")
    public void loginWithInvalidPassword() {
        UserModel invalidPasswordUser = new UserModel(
                user.getEmail(),
                "wrongPassword123",
                user.getName()
        );
        Steps.loginUser(invalidPasswordUser)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(Constants.INVALID_CREDENTIALS_ERROR));
    }
}







