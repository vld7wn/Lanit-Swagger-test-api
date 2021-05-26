package org.example.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Order;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class HomeTaskApiTest {
    @BeforeClass
    public void before() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", "api.key")
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    public void Save() {
        Order order = new Order();
        int id = new Random().nextInt(20) + 1;
        order.setId(id);
        order.setQuantity(new Random().nextInt(10) + 1);
        order.setStatus("placed");
        order.setComplete(true);

        given().body(order).when()
                .post("/store/order")
                .then().statusCode(200);

        Order actual = given().pathParam("orderId", id)
                .when()
                .get("/store/order/{orderId}")
                .then()
                .statusCode(200)
                .extract().body().as(Order.class);
        Assert.assertEquals(actual.getShipDate(),
                order.getShipDate());
    }

    @Test
    public void Delete() {
        Order order = new Order();
        int id = new Random().nextInt(20) + 1;
        order.setId(id);

        given().body(order).when()
                .post("/store/order")
                .then().statusCode(200);

        given().pathParam("orderId", id)
                .when().delete("/store/order/{orderId}")
                .then().statusCode(200);

        given().pathParam("orderId", id)
                .when().delete("/store/order/{orderId}")
                .then().statusCode(404);
    }

    @Test
    public void Inventory() {
        HashMap<String, Integer> inventory = new HashMap<>(
                given().when().get("/store/inventory")
                        .then().statusCode(200).extract().body()
                        .as(Map.class));
        Assert.assertTrue(inventory.containsKey("sold"));
        Assert.assertTrue(inventory.containsKey("pending"));
        Assert.assertTrue(inventory.containsKey("available"));
        Assert.assertTrue(inventory.containsKey("connector"));
        Assert.assertFalse(inventory.containsKey("id"));
    }
}
