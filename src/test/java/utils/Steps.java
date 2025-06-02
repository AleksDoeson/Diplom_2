package utils;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Steps {

    static {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    private static RequestSpecification getSpec() {
        return given().header("Content-type", "application/json");
    }

    @Step("Регистрация нового пользователя")
    public static Response registerNewUser(UserModel user) {
        return getSpec()
                .body(user)
                .when()
                .post(Constants.REGISTER_URL.replace(Constants.BASE_URL, ""));
    }

    @Step("Авторизация пользователя")
    public static Response loginUser(UserModel user) {
        return getSpec()
                .body(user)
                .when()
                .post(Constants.LOGIN_URL.replace(Constants.BASE_URL, ""));
    }

    @Step("Удаление пользователя")
    public static Response deleteUser(String token) {
        return getSpec()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(Constants.USER_DELETE_URL.replace(Constants.BASE_URL, ""));
    }

    @Step("Создание заказа")
    public static Response createOrder(String token, OrderModel order) {
        return getSpec()
                .header("Authorization", "Bearer " + token)
                .body(order)
                .when()
                .post(Constants.CREATE_ORDER_URL.replace(Constants.BASE_URL, ""));
    }

    @Step("Получение заказов пользователя")
    public static Response getOrders(String token) {
        return getSpec()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(Constants.CREATE_ORDER_URL.replace(Constants.BASE_URL, ""));
    }

    @Step("Обновление данных пользователя")
    public static Response updateUser(String token, UserModel user) {
        return getSpec()
                .header("Authorization", "Bearer " + token)
                .body(user)
                .when()
                .patch(Constants.UPDATE_USER_URL.replace(Constants.BASE_URL, ""));
    }

}









