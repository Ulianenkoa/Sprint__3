package com.ya;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {
    private static final String ORDERS_PATH = "/api/v1/orders";

    @Step("Create order")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("Cancel order")
    public ValidatableResponse cancel(int trackNumber){
        return given()
                .spec(getBaseSpec())
                .body(trackNumber)
                .when()
                .put(ORDERS_PATH + "/cancel")
                .then();
    }

    @Step("Get the orders list")
    public static ValidatableResponse getAll() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH)
                .then();
    }
}
