package practikum.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import practikum.base.BaseConfig;


public class OrderSteps extends BaseConfig {
    private final String ORDER_URL = "/api/orders";

    @Step("Создание заказа с авторизацией и ингредиентами")
    public ValidatableResponse createOrderWithToken(Order order, String token) {
        return spec()
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(ORDER_URL)
                .then().log().all();
    }

    @Step("Создание заказа без авторизации и ингредиентами")
    public ValidatableResponse createOrderWithoutToken(Order order) {
        return spec()
                .and()
                .body(order)
                .when()
                .post(ORDER_URL)
                .then().log().all();
    }


    @Step("Создание заказа без ингредиентов и с авторизацией")
    public ValidatableResponse createOrderWithoutIngredients(Order order, String token) {
        return spec()
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(ORDER_URL)
                .then().log().all();
    }

    @Step("Получение заказов авторизованного пользователя")
    public ValidatableResponse getOrderListWithToken(String token) {
        return spec()
                .header("Authorization", token)
                .when()
                .get(ORDER_URL)
                .then().log().all();

    }

    @Step("Получение заказов неавторизованным пользователем")
    public ValidatableResponse getOrderListWithoutToken() {
        return spec()
                .when()
                .get(ORDER_URL)
                .then().log().all();


    }
}

