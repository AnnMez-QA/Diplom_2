package practikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import practikum.order.Order;
import practikum.order.OrderSteps;
import practikum.user.User;
import practikum.user.UserSteps;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static practikum.order.Order.*;

public class CreateOrderTest {
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    private String token;

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void createOrder() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        List<Order> orders = getOrderList();
        Order orderToCreate = orders.get(0);
        ValidatableResponse createOrderResponse = orderSteps.createOrderWithToken(orderToCreate, token);
        createOrderResponse.statusCode(HttpURLConnection.HTTP_OK);
        createOrderResponse.body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации и c ингредиентами")
    public void createOrderWithoutToken() {
        List<Order> orders = getOrderList();
        Order orderToCreate = orders.get(0);
        ValidatableResponse createOrderResponse = orderSteps.createOrderWithoutToken(orderToCreate);
        createOrderResponse.statusCode(HttpURLConnection.HTTP_OK);
        createOrderResponse.body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингредиентов")
    public void createOrderWithoutIngredients() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        ValidatableResponse createOrderResponse = orderSteps.createOrderWithoutIngredients(getEmptyIngredients(), token);
        createOrderResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        createOrderResponse.body(
                "success", equalTo(false),
                "message", equalTo("Ingredient ids must be provided")
        );
    }

    @Test
    @DisplayName("Создание заказа с невалидным хэшем")
    public void createOrderWithInvalidHash() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        ValidatableResponse createOrderResponse = orderSteps.createOrderWithToken(getInvalidHashIngredient(), token);
        createOrderResponse.statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);

    }

    @After
    public void deleteUser() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }


}
