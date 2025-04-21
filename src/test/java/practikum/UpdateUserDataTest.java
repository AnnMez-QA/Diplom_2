package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import practikum.user.User;
import practikum.user.UserSteps;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.equalTo;


public class UpdateUserDataTest {
    UserSteps userSteps = new UserSteps();
    private String token;
    int suffix = ThreadLocalRandom.current().nextInt(100, 100_000);


    @Test
    @DisplayName("Изменение почты пользователя с авторизацией")
    public void userEmailCanBeUpdatedWithToken() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("email", suffix + "updated_pochta@mail.ru");
        ValidatableResponse updateResponse = userSteps.updateWithToken(updatedFields, token);
        updateResponse.statusCode(HttpURLConnection.HTTP_OK);
        updateResponse.body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void userNameCanBeUpdatedWithToken() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("name", "Bobik");
        ValidatableResponse updateResponse = userSteps.updateWithToken(updatedFields, token);
        updateResponse.statusCode(HttpURLConnection.HTTP_OK);
        updateResponse.body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение почты пользователя без авторизации")
    public void userEmailCanNotBeUpdatedWithoutToken() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("email", suffix + "updated_pochta@mail.ru");
        ValidatableResponse updateResponse = userSteps.updateWithoutToken(updatedFields);
        updateResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        updateResponse.body(
                "success", equalTo(false),
                "message", equalTo("You should be authorised")
        );
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void userNameCanNotBeUpdatedWithoutToken() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("name", "Bobik");
        ValidatableResponse updateResponse = userSteps.updateWithoutToken(updatedFields);
        updateResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        updateResponse.body(
                "success", equalTo(false),
                "message", equalTo("You should be authorised")
        );
    }

    @After
    public void deleteUser() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }

}
