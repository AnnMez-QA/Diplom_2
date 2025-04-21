package practikum.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import practikum.base.BaseConfig;

import java.util.Map;

public class UserSteps extends BaseConfig {
    private static final String REGISTER_URL = "/api/auth/register";
    private static final String LOGIN_URL = "/api/auth/login";
    private static final String AUTH_URL = "/api/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return spec()
                .and()
                .body(user)
                .when()
                .post(REGISTER_URL)
                .then().log().all();
    }

    @Step("Войти")
    public ValidatableResponse logIn(UserCreds creds, String token) {
        return spec()
                .header("Authorization", token)
                .and()
                .body(creds)
                .when()
                .post(LOGIN_URL)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return spec()
                .header("Authorization", token)
                .when()
                .delete(AUTH_URL)
                .then().log().all();

    }

    @Step("Обновление данных пользователя с авторизацией")
    public ValidatableResponse updateWithToken(Map<String, Object> updatedFields, String token) {
        return spec()
                .header("Authorization", token)
                .and()
                .body(updatedFields)
                .when()
                .patch(AUTH_URL)
                .then().log().all();
    }

    @Step("Обновление данных о пользователе без авторизации")
    public ValidatableResponse updateWithoutToken(Map<String, Object> updatedFields) {
        return spec()
                .and()
                .body(updatedFields)
                .when()
                .patch(AUTH_URL)
                .then().log().all();
    }


}
