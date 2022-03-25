package com.ya;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class LoginCourierTest {
    CourierClient courierClient;
    Courier courier;
    int courierId;
    String courierLogin = "LogCurier";
    String courierPassword = "PasCurier";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier(courierLogin, courierPassword, "FirstName");
        courierClient.create(courier);
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Courier can login with valid credentials")
    public void courierCanLoginWithValidCredentials() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertThat("Courier can login", statusCode, equalTo(SC_OK));
        assertThat("Courier id is correct", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Courier cannot login without password")
    public void courierCannotLoginWithoutPassword() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), ""));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login",statusCode, equalTo(400));
        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier cannot login without login")
    public void courierCannotLoginWithoutLogin() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("", courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(400));
        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier cannot login with incorrect login")
    public void courierCannotLoginWithIncorrectLogin() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("test", courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(404));
        assertThat("Message if cannot login", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier cannot login with incorrect password")
    public void courierCannotLoginWithIncorrectPassword() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), "test"));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(404));
        assertThat("Message if cannot login", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier cannot login if hasn't created")
    public void courierCannotLoginIfNotCreated() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("test", "test"));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(404));
        assertThat("Message if cannot login", message, equalTo("Учетная запись не найдена"));
    }
}
