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
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


public class OrderUserHistoryTest {

    private String token;

    @Before
    public void setUp() {
        String email = UserGenerator.generateUniqueEmail();
        String password = UserGenerator.generateRandomPassword();
        String name = UserGenerator.generateRandomName();

        UserModel user = new UserModel(email, password, name);
        Response response = Steps.registerNewUser(user);
        response.then().statusCode(HttpStatus.SC_OK);
        token = response.then().extract().path("accessToken").toString().split(" ")[1];

        // Создаем заказ, чтобы у пользователя была история
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d");
        Steps.createOrder(token, new OrderModel(ingredients));
    }

    @After
    public void tearDown() {
        if (token != null) {
            Steps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Получение истории заказов авторизованного пользователя")
    @Description("Проверка успешного получения истории заказов авторизованного пользователя")
    public void getOrdersWithAuthTest() {
        Response response = Steps.getOrders(token);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0)); // Убедимся, что история не пуста
    }

    @Test
    @DisplayName("Попытка получения истории заказов без авторизации")
    @Description("Проверка, что без токена возвращается ошибка 401 Unauthorized")
    public void getOrdersWithoutAuthTest() {
        Response response = Steps.getOrders("");
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("message", equalTo(Constants.USER_NOTAUTHORISED_ERROR));
    }

    @Test
    @DisplayName("Получение истории заказов у нового пользователя без заказов")
    @Description("Проверка, что у нового пользователя без созданных заказов возвращается пустой список")
    public void getOrdersEmptyHistoryTest() {
        // Создаем нового пользователя без заказов
        String email = UserGenerator.generateUniqueEmail();
        String password = UserGenerator.generateRandomPassword();
        String name = UserGenerator.generateRandomName();

        UserModel newUser = new UserModel(email, password, name);
        Response registerResp = Steps.registerNewUser(newUser);
        registerResp.then().statusCode(HttpStatus.SC_OK);
        String newUserToken = registerResp.then().extract().path("accessToken").toString().split(" ")[1];

        // Запрос истории заказов для нового пользователя
        Response response = Steps.getOrders(newUserToken);
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("orders", is(empty()));

        // Удаляем созданного нового пользователя
        Steps.deleteUser(newUserToken);
    }
}





