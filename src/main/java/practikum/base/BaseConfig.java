package practikum.base;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseConfig {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    public RequestSpecification spec() {
        return given().log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .baseUri(BASE_URL);

    }
}
