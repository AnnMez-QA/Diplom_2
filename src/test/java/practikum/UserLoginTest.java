package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import practikum.user.User;
import practikum.user.UserCreds;
import practikum.user.UserSteps;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;


public class UserLoginTest {
    UserSteps userSteps = new UserSteps();
    private String token;


    @Test
    @DisplayName("Логин под существующим пользователем")
    public void userCanLogIn() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        var creds = UserCreds.fromUser(user);
        ValidatableResponse loginResponse = userSteps.logIn(creds, token);
        loginResponse.statusCode(HttpURLConnection.HTTP_OK);
        loginResponse.body("success", equalTo(true));
    }

    @Test
    @DisplayName("Вход c неправильным email")
    public void loginUserWithIncorrectEmail() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        token = createResponse.extract().path("accessToken");
        UserCreds correctCredentials = UserCreds.fromUser(user);
        UserCreds incorrectLoginCredentials = new UserCreds("wrong_email@mail.ru", correctCredentials.getPassword());
        ValidatableResponse loginResponse = userSteps.logIn(incorrectLoginCredentials, token);
        loginResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        loginResponse.body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Вход c неправильным паролем")
    public void loginUserWithIncorrectPassword() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        token = createResponse.extract().path("accessToken");
        UserCreds correctCredentials = UserCreds.fromUser(user);
        UserCreds incorrectLoginCredentials = new UserCreds(correctCredentials.getEmail(), "wrong_password");
        ValidatableResponse loginResponse = userSteps.logIn(incorrectLoginCredentials, token);
        loginResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        loginResponse.body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Вход без пароля")
    public void loginUserWithoutPassword() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        token = createResponse.extract().path("accessToken");
        UserCreds correctCredentials = UserCreds.fromUser(user);
        UserCreds incorrectLoginCredentials = new UserCreds(correctCredentials.getEmail(), "");
        ValidatableResponse loginResponse = userSteps.logIn(incorrectLoginCredentials, token);
        loginResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        loginResponse.body("message", equalTo("email or password are incorrect"));

    }

    @Test
    @DisplayName("Вход без email")
    public void loginUserWithoutEmail() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        token = createResponse.extract().path("accessToken");
        UserCreds correctCredentials = UserCreds.fromUser(user);
        UserCreds incorrectLoginCredentials = new UserCreds("", correctCredentials.getPassword());
        ValidatableResponse loginResponse = userSteps.logIn(incorrectLoginCredentials, token);
        loginResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        loginResponse.body("message", equalTo("email or password are incorrect"));

    }


    @After
    public void deleteUser() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }
}
