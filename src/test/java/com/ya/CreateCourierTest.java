package com.ya;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateCourierTest {
    CourierClient courierClient;
    int courierId;
    private Courier courier;

    @Before
    public void SetUp() {
        courier = CourierGenerator.getRandom();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Courier must be created")
    public void courierMustBeCreated() {
        ValidatableResponse createResponse = courierClient.create(courier);
        int statusCode = createResponse.extract().statusCode();
        boolean ok = createResponse.extract().path("ok");

        assertThat("Courier has created", statusCode, equalTo(201));
        assertThat("State if courier has created", ok, equalTo(true));
    }

    @Test
    @DisplayName("Courier must be unique")
    public void courierMustBeUnique() {
        ValidatableResponse response = courierClient.create(courier);
        ValidatableResponse response1 = courierClient.create(courier);

        int statusCode = response1.extract().statusCode();
        String message = response1.extract().path("message");

        assertThat("Courier hasn't created", statusCode, equalTo(409));
        assertThat("Message if cannot create", message, equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Courier cannot created without password")
    public void courierCannotCreatedWithoutPassword() {
        ValidatableResponse createResponse = courierClient.create(new Courier(courier.getLogin(), "", courier.getFirstName() ));
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");

        assertThat("Courier hasn't created", statusCode, equalTo(400));
        assertThat("Message if cannot create", message, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Courier cannot created without login")
    public void courierCannotCreatedWithoutLogin() {
        ValidatableResponse createResponse = courierClient.create(new Courier("", courier.getPassword(), courier.getFirstName()));
        int statusCode = createResponse.extract().statusCode();
        String message = createResponse.extract().path("message");

        assertThat("Courier hasn't created", statusCode, equalTo(400));
        assertThat("Message if cannot create", message, equalTo("Недостаточно данных для создания учетной записи"));
    }
}

