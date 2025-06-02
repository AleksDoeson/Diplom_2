package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {

    private String email;
    private String password;
    private String name;
    private String token;

    private final List<String> validIngredients = Arrays.asList(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa70"
    );

    @Before
    public void setUp() {
        email = UserGenerator.generateUniqueEmail();
        password = UserGenerator.generateRandomPassword();
        name = UserGenerator.generateRandomName();

        UserModel user = new UserModel(email, password, name);
        Response response = Steps.registerNewUser(user);
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
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Создание заказа с авторизацией и корректным списком ингредиентов")
    public void createOrderWithIngredientsTest() {
        OrderModel order = new OrderModel(validIngredients);
        Response response = Steps.createOrder(token, order);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Попытка создания заказа без авторизации")
    @Description("Создание заказа без авторизации должно вернуть 401 Unauthorized")
    public void createOrderWithoutAuthTest() {
        OrderModel order = new OrderModel(validIngredients);
        Response response = Steps.createOrder(null, order);
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }

    @Test
    @DisplayName("Попытка создания заказа без ингредиентов")
    @Description("Создание заказа с пустым списком ингредиентов должно вернуть 400 Bad Request")
    public void createOrderWithoutIngredientsTest() {
        OrderModel order = new OrderModel(Collections.emptyList());
        Response response = Steps.createOrder(token, order);
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", equalTo(Constants.EMPTY_INGREDIENTS));
    }

    @Test
    @DisplayName("Попытка создания заказа с неверными ингредиентами")
    @Description("Создание заказа с некорректным списком ингредиентов вызывает ошибку сервера")
    public void createOrderWithInvalidIngredientsTest() {
        OrderModel order = new OrderModel(Collections.singletonList("invalidIngredientId"));
        Response response = Steps.createOrder(token, order);
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}






