package user;

import io.qameta.allure.Step;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.UserGenerator;
import utils.Steps;

import static org.hamcrest.Matchers.*;

public class UserLoginTest {

    private String email;
    private final String password = Constants.DEFAULT_USER_PASSWORD;
    private String token;

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
    @Step("Успешный логин с валидными данными")
    @Description("Успешный логин с валидными данными")
    public void loginWithValidCredentials() {
        Steps.loginUser(email, password)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @Step("Логин с невалидным email")
    @Description("Логин с невалидным email")
    public void loginWithInvalidEmail() {
        Steps.loginUser("invalid" + email, password)
                .then()
                .statusCode(401)
                .body("message", equalTo(Constants.INVALID_CREDENTIALS_ERROR));
    }

    @Test
    @Step("Логин с невалидным паролем")
    @Description("Логин с невалидным паролем")
    public void loginWithInvalidPassword() {
        Steps.loginUser(email, "wrongPassword")
                .then()
                .statusCode(401)
                .body("message", equalTo(Constants.INVALID_CREDENTIALS_ERROR));
    }
}



