package com.ya;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
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
    final String courierLogin = RandomStringUtils.randomAlphabetic(10);
    final String courierPassword = RandomStringUtils.randomAlphabetic(10);

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
           CourierCredentials courierCredentials= CourierCredentials.builder()
                   .login(courier.getLogin())
                   .password(courier.getPassword())
                   .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertThat("Courier can login", statusCode, equalTo(SC_OK));
        assertThat("Courier id is correct", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Courier cannot login with empty password")
    public void courierCannotLoginWithoutPassword() {
       CourierCredentials courierCredentials= CourierCredentials.builder()
               .login(courier.getLogin())
               .password("")
               .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login",statusCode, equalTo(400));
        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier cannot login with empty login")
    public void courierCannotLoginWithoutLogin() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .login("")
                .password(courier.getPassword())
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(400));
        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier cannot login with incorrect login")
    public void courierCannotLoginWithIncorrectLogin() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .login("test")
                .password(courier.getPassword())
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(404));
        assertThat("Message if cannot login", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier cannot login with incorrect password")
    public void courierCannotLoginWithIncorrectPassword() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .login(courier.getLogin())
                .password("test")
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(404));
        assertThat("Message if cannot login", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier cannot login if hasn't created")
    public void courierCannotLoginIfNotCreated() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .login("test")
                .password("test")
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat("Courier cannot login", statusCode, equalTo(404));
        assertThat("Message if cannot login", message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login field is required")
    @Description("The courier cannot log in if the login field is missing in the request")
    public void courierCannotLoginIfLoginFieldMissing() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .password(courier.getPassword())
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();

        assertThat("Courier cannot login", statusCode, equalTo(400));

        String message = loginResponse.extract().path("message");

        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Password field is required")
    @Description("The courier cannot log in if the password field is missing in the request")
    public void courierCannotLoginIfPasswordFieldMissing() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .login(courier.getLogin())
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier cannot login", statusCode, equalTo(400));
        String message = loginResponse.extract().path("message");
        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Request body is required")
    @Description("The courier cannot log in if the request body is missing")
    public void courierCannotLoginIfRequestBodyMissing() {
        CourierCredentials courierCredentials= CourierCredentials.builder()
                .build();
        ValidatableResponse loginResponse = courierClient.login(courierCredentials);
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Courier cannot login", statusCode, equalTo(400));
        String message = loginResponse.extract().path("message");
        assertThat("Message if cannot login", message, equalTo("Недостаточно данных для входа"));
    }
}
