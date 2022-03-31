package com.ya;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class CreateOrderTest {

        final String[] color;
        OrderClient orderClient;
        Order order;
        int trackNumber;

        public CreateOrderTest(String[] color) {
            this.color = color;
        }

        @Parameterized.Parameters
        public static Object[][] getColorData() {
            return new Object[][]{
                    {new String[]{"Black"}},
                    {new String[]{"Grey"}},
                    {new String[]{"Black", "Grey"}},
                    {new String[]{"", ""}},
            };
        }

        @Before
        public void setUp() {
            order = Order.getDefault();
            orderClient = new OrderClient();
        }

        @After
        public void tearDown(){
            orderClient.cancel(trackNumber);
        }

        @Test
        @DisplayName("Order can be created")
        public void orderCanBeCreated(){
            order.setColor(color);
            ValidatableResponse createResponse = orderClient.create(order);
            int statusCode = createResponse.extract().statusCode();
            trackNumber = createResponse.extract().path("track");

            assertThat("Can't create order", statusCode, equalTo(SC_CREATED));
            assertThat("Empty number of order", trackNumber, is(not(0)));
        }
}
