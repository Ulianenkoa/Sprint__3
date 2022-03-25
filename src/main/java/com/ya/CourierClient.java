package com.ya;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends ScooterRestClient{
    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Courier create")
    public ValidatableResponse create (Courier courier){
        String courierLogin = courier.getLogin();
        String courierPassword = courier.getPassword();
        String courierFirstName = courier.getFirstName();

        String registerRequestBody = "{\"login\":\"" + courierLogin + "\","
                + "\"password\":\"" + courierPassword + "\","
                + "\"firstName\":\"" + courierFirstName + "\"}";

        return given()
                .spec(getBaseSpec())
                .body(registerRequestBody)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Courier login")
    public ValidatableResponse login(CourierCredentials credentials){
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_PATH + "/login")
                .then();
    }

    @Step("Courier deleting")
    public void delete (int courierId){
        given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH +  "/" + courierId)
                .then();
    }
}
