package com.ya;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CreateOrderListTest {

    @Test
    @DisplayName("Get list of orders")
    public void getReturnListOfOrders(){
        ValidatableResponse createResponse = OrderClient.getAll();
        int statusCode = createResponse.extract().statusCode();
        ArrayList<String> response = createResponse.extract().path("orders");

        assertThat("Can't create order", statusCode, equalTo(SC_OK));
        assertThat("Empty response", response, notNullValue());
    }
}
