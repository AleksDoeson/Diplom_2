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

public class UserCreateTest {

    private String uniqueEmail;
    private final String userPassword = Constants.DEFAULT_USER_PASSWORD;
    private final String userName = Constants.USER_NAME;
    private String token;

    @Before
    @Step("Получение токена")
    @Description("Получение токена")
    public void setUp() {
        uniqueEmail = UserGenerator.generateUniqueEmail();
        Response response = Steps.registerNewUser(uniqueEmail, userPassword, userName);
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
    @Step("Тест создания уникального пользователя")
    @Description("Тест создания уникального пользователя")
    public void createUniqueUserTest() {
        String newEmail = UserGenerator.generateUniqueEmail();
        Steps.registerNewUser(newEmail, userPassword, userName)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Step("Тест создания уже зарегистрированного пользователя")
    @Description("Тест создания уже зарегистрированного пользователя")
    public void createAlreadyRegisteredUserTest() {
        Steps.registerNewUser(uniqueEmail, userPassword, userName)
                .then()
                .statusCode(403)
                .body("message", equalTo(Constants.USER_EXISTS_ERROR));
    }

    @Test
    @Step("Тест создания пользователя с отсутствующим обязательным полем")
    @Description("Тест создания пользователя с отсутствующим обязательным полем")
    public void createUserMissingRequiredFieldTest() {
        String newEmail = UserGenerator.generateUniqueEmail(); // Генерация нового уникального email
        Steps.registerNewUser(newEmail, userPassword, "")
                .then()
                .statusCode(403)
                .body("message", equalTo(Constants.EMPTY_FIELDS_ERROR));
    }

}







