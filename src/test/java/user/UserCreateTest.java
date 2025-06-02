package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.*;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {

    private String token;
    private UserModel existingUser;

    @Before
    public void setUp() {
        // Создаем существующего пользователя с генераторами
        existingUser = new UserModel(
                UserGenerator.generateUniqueEmail(),
                UserGenerator.generateRandomPassword(),
                UserGenerator.generateRandomName()
        );

        Response response = Steps.registerNewUser(existingUser);
        response.then().statusCode(HttpStatus.SC_OK);
        token = response.then().extract().path("accessToken").toString().split(" ")[1];
    }

    @After
    public void tearDown() {
        if (token != null) {
            Steps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    @Description("Проверка успешного создания пользователя с автоматически сгенерированными корректными данными")
    public void createUserWithValidData() {
        UserModel newUser = new UserModel(
                UserGenerator.generateUniqueEmail(),
                UserGenerator.generateRandomPassword(),
                UserGenerator.generateRandomName()
        );

        Steps.registerNewUser(newUser)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя с уже зарегистрированным email")
    @Description("Проверка ошибки при создании пользователя с существующим email")
    public void createUserWithExistingEmail() {
        Steps.registerNewUser(existingUser)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(Constants.USER_EXISTS_ERROR));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка ошибки при создании пользователя без email")
    public void createUserWithoutEmail() {
        UserModel user = new UserModel(null, UserGenerator.generateRandomPassword(), UserGenerator.generateRandomName());
        Steps.registerNewUser(user)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(Constants.EMPTY_FIELDS_ERROR));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка ошибки при создании пользователя без пароля")
    public void createUserWithoutPassword() {
        UserModel user = new UserModel(UserGenerator.generateUniqueEmail(), "", UserGenerator.generateRandomName());
        Steps.registerNewUser(user)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(Constants.EMPTY_FIELDS_ERROR));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка ошибки при создании пользователя без имени")
    public void createUserWithoutName() {
        UserModel user = new UserModel(UserGenerator.generateUniqueEmail(), UserGenerator.generateRandomPassword(), "");
        Steps.registerNewUser(user)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("message", equalTo(Constants.EMPTY_FIELDS_ERROR));

    }
}














