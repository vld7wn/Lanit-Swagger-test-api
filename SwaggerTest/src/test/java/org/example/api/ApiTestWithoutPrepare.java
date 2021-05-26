package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

//В этом классе реализован пример отправки запроса GET без использования спецификации запроса new RequestSpecBuilder(),
//то есть все необходимые параметры переданы одинм методом
public class ApiTestWithoutPrepare {
    @Test
    public void testGet() throws IOException {
        // Читаем конфигурационный файл в System.properties
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));
        given()//ДАНО:
                .baseUri("https://petstore.swagger.io/v2/") // задаём базовый адрес каждого ресурса
                .header(new Header("api_key", System.getProperty("api.key")))// задаём заголовок с токеном для авторизации
                .accept(ContentType.JSON)// задаём заголовок accept
                .pathParam("petId", System.getProperty("petId"))// заранее задаём переменную petId
                                               //(так как это просто пример, нужно убедиться, что объект с таким Id существует)
                .log().all()//задаём логгирование запроса
            .when()//КОГДА:
                .get("/pet/{petId}") // переменная petId подставится в путь ресурса перед выполнением запроса GET
            .then()// ТОГДА:
                .statusCode(200) //проверка кода ответа
                .log().all(); //задаём логгирование ответа

    }
}
