package utils;

public class Constants {

    // Основной URL API
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/api";

    // URL для регистрации
    public static final String REGISTER_URL = BASE_URL + "/auth/register";

    // URL для логина
    public static final String LOGIN_URL = BASE_URL + "/auth/login";

    // URL для обновления данных пользователя
    public static final String UPDATE_USER_URL = BASE_URL + "/auth/user";

    // URL для создания заказа
    public static final String CREATE_ORDER_URL = BASE_URL + "/orders";

    //URL для удаления заказа
    public static final String USER_DELETE_URL = BASE_URL + "/auth/user";

    // Данные для логина
    public static final String VALID_EMAIL = "aleks.doeson@yandex.ru";
    public static final String VALID_PASSWORD = "Kdepapa1?";
    public static final String USER_NAME = "TestUser";
    public static final String DEFAULT_USER_PASSWORD = "password123";


    // Множество сообщений об ошибках
    public static final String USER_NOTAUTHORISED_ERROR = "You should be authorised";
    public static final String INVALID_CREDENTIALS_ERROR = "email or password are incorrect";
    public static final String USER_EXISTS_ERROR = "User already exists";
    public static final String EMPTY_FIELDS_ERROR = "Email, password and name are required fields";
    public static final String EMPTY_INGREDIENTS = "Ingredient ids must be provided";
    public static final String USER_ALREADY_EXISTS_ERROR = "User with such email already exists";

}

