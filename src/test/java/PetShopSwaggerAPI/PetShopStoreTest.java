package PetShopSwaggerAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.BeforeClass;

import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class PetShopStoreTest {
    RequestSpecification reqSpec;
    int orderId;

    Faker faker=new Faker();

    @BeforeClass
    public void Setup() {
        baseURI = "https://petstore.swagger.io/v2/store";
        reqSpec = new RequestSpecBuilder()
                .addHeader("accept", "application/json")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test()
    public void getInventory() {
        given()
                .spec(reqSpec)
                .when()
                .get("/inventory")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test()
    public void toOrder() throws JsonProcessingException {
        int rndmOrderId=faker.number().numberBetween(1,10);
        int rndmPetId=faker.number().numberBetween(1,3);
        int rndmQuantity=faker.number().numberBetween(1,3);
        boolean complete=true;
        LocalDateTime localDateTime=LocalDateTime.now();

        String shipDate= String.valueOf(localDateTime);

        Map<String, Object> order = new HashMap<>();
        order.put("id",rndmOrderId);
        order.put("petId",rndmPetId);
        order.put("quantity",rndmQuantity);
        order.put("complete",complete);
        order.put("shipDate",shipDate);

        ObjectMapper objectMapper=new ObjectMapper();
        String orderPayload=objectMapper.writeValueAsString(order);
        orderId=
        given()
                .spec(reqSpec)
                .body(orderPayload)
                .when()
                .post("/order")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("id"); // I got order ID to execute getOrder() with orderID

        System.out.println("orderId = " + orderId);
    }
    @Test(dependsOnMethods = "toOrder")
    public void getOrder(){
        given()
                .spec(reqSpec)
                .when()
                .get("/order/"+orderId)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
    @Test(dependsOnMethods = "getOrder")
    public void deleteOrder(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/order/"+orderId)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test(dependsOnMethods = "deleteOrder")
    public void deleteOrderNegative(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/order/"+orderId)
                .then()
                .log().all()
                .statusCode(404) // Order Not Found
                .contentType(ContentType.JSON);

    }
}
