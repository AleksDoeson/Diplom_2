package order;

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

public class OrderCreateTest {

    private String token;
    private final String ingredients = "[\"61c0c5a71d1f82001bdaaa6d\", \"61c0c5a71d1f82001bdaaa6c\"]";

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
    @Step("Создание заказа с авторизацией")
    @Description("Создание заказа с авторизацией")
    public void createOrderWithAuthTest() {
        String orderDetails = "{\"ingredients\": " + ingredients + "}";
        Steps.createOrder(token, orderDetails)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Step("Попытка создания заказа без авторизации")
    @Description("Попытка создания заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        String orderDetails = "{\"ingredients\": " + ingredients + "}";
        Steps.createOrder("", orderDetails)
                .then()
                .log().all()  // <-- вот здесь
                .statusCode(401)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }


    @Test
    @Step("Создание заказа с ингредиентами")
    @Description("Создание заказа с ингредиентами")
    public void createOrderWithIngredientsTest() {
        String orderDetails = "{\"ingredients\": " + ingredients + "}";
        Steps.createOrder(token, orderDetails)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Step("Попытка создания заказа без ингредиентов")
    @Description("Попытка создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        String orderDetails = "{\"ingredients\": []}";
        Steps.createOrder(token, orderDetails)
                .then()
                .statusCode(400)
                .body("message", equalTo(Constants.EMPTY_INGREDIENTS));
    }

    @Test
    @Step("Попытка создания заказа с неверными ингредиентами")
    @Description("Попытка создания заказа без ингредиентов")
    public void createOrderWithInvalidIngredientsTest() {
        String invalidIngredients = "[\"invalidIngredientId\"]";
        String orderDetails = "{\"ingredients\": " + invalidIngredients + "}";
        Steps.createOrder(token, orderDetails)
                .then()
                .statusCode(500);
    }
}


