package com.ya;

import com.github.javafaker.Faker;

import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Data
@Builder
public class Order {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public static Order getDefault(){
        Faker faker = new Faker(new Locale("ru"));
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().streetAddress();
        String metroStation = faker.number().digits(1);
        String phone = faker.phoneNumber().phoneNumber();
        int rentTime = faker.number().numberBetween(1,8);
        String deliveryDate =  new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String comment = faker.name().title();
        String[] color = new String[2];

        return  new Order(firstName,lastName,address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}
