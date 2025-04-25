package order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;
import utils.Steps;
import utils.TokenManager;
import utils.UserGenerator;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OrderUserHistoryTest {

    private String token;
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
    @Step("Получение заказов с авторизацией")
    @Description("Получение заказов с авторизацией")
    public void getOrdersWithAuthTest() {
        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Step("Попытка получения заказов без авторизации")
    @Description("Попытка получения заказов без авторизации")
    public void getOrdersWithoutAuthTest() {
        given()
                .when()
                .get("https://stellarburgers.nomoreparties.site/api/orders")
                .then()
                .statusCode(401)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }
}

