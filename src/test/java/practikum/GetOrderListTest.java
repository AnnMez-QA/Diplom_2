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
import static practikum.order.Order.getOrderList;

public class GetOrderListTest {
    UserSteps userSteps = new UserSteps();
    OrderSteps orderSteps = new OrderSteps();
    private String token;


    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrderForAuthUser() {
        User user = User.random();
        ValidatableResponse createResponse = userSteps.createUser(user);
        createResponse.statusCode(HttpURLConnection.HTTP_OK);
        token = createResponse.extract().path("accessToken");
        List<Order> orders = getOrderList();
        Order orderToCreate = orders.get(0);
        ValidatableResponse createOrderResponse = orderSteps.createOrderWithToken(orderToCreate, token);
        createOrderResponse.statusCode(HttpURLConnection.HTTP_OK);
        ValidatableResponse getOrderResponse = orderSteps.getOrderListWithToken(token);
        getOrderResponse.statusCode(HttpURLConnection.HTTP_OK);
        getOrderResponse.body("success", equalTo(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrderForNotAuthUser() {
        ValidatableResponse getOrderResponse = orderSteps.getOrderListWithoutToken();
        getOrderResponse.statusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        getOrderResponse.body(
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
