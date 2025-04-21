package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import practikum.user.User;
import practikum.user.UserSteps;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;


public class CreateUserTest {
    UserSteps userSteps = new UserSteps();
    private String token;

    @Test
    @DisplayName("Создание уникального пользоватея")
    public void userCanBeCreated() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        createResponse.body("success", equalTo(true));
        assertNotNull(token);
    }

    @Test
    @DisplayName("Нельзя создать пользователя, который уже зарегистрирован")
    public void cannotCreateDuplicateUser() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        ValidatableResponse createResponseTwice = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        createResponseTwice.statusCode(HttpURLConnection.HTTP_FORBIDDEN);
        String errorMessage = createResponseTwice.extract().path("message");
        assertEquals("User already exists", errorMessage);

    }

    @Test
    @DisplayName("Если не передать почту, запрос возвращает ошибку")
    public void checkCannotCreateUserWithoutEmail() {
        User user = new User(null, "password123", "Debchik");
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_FORBIDDEN);
        createResponse.body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Если не передать пароль, запрос возвращает ошибку")
    public void checkCannotCreateUserWithoutPassword() {
        User user = new User("superpochtaforani@mail.meow", null, "Debchik");
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_FORBIDDEN);
        createResponse.body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("Если не передать имя, запрос возвращает ошибку")
    public void checkCannotCreateUserWithoutName() {
        User user = new User("superpochtaforani@mail.meow", "password123", null);
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_FORBIDDEN);
        createResponse.body("message", equalTo("Email, password and name are required fields"));

    }

    @After
    public void deleteUser() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }


}
