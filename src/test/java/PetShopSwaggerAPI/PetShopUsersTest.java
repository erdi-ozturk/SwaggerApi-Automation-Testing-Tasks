package PetShopSwaggerAPI;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class PetShopUsersTest {
    Faker faker = new Faker();
    RequestSpecification reqSpec;

    String userName;
    String userPassword;

    String firstName;

    // int userID;

    @BeforeClass
    public void Setup() {
        baseURI = "https://petstore.swagger.io/v2";
        reqSpec = new RequestSpecBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createUser() {
        String randomUserName = faker.name().username();
        firstName = faker.name().firstName();
        String randomLastName = faker.name().lastName();
        String randomEmail = faker.internet().emailAddress();
        String randomPassword = faker.internet().password();
        String randomPhone = faker.phoneNumber().cellPhone();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("username", randomUserName);
        newUser.put("firstName", firstName);
        newUser.put("lastName", randomLastName);
        newUser.put("email", randomEmail);
        newUser.put("password", randomPassword);
        newUser.put("phone", randomPhone);

        Response response =
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        .log().uri()
                        .log().body()
                        .when()
                        .post("/user")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().response();
        userName = newUser.get("username");
        userPassword = newUser.get("password");
    }

    @Test(dependsOnMethods = "createUser")
    public void loginUser() {
        given()
                .param("username", userName)
                .param("password", userPassword)
                .spec(reqSpec)
                .when()
                .get("/user/login")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);

    }

    @Test(dependsOnMethods = "loginUser")
    public void updateUser() {
        Map<String, String> updateUser = new HashMap<>();
        firstName = "Erdi";
        updateUser.put("firstName", firstName);
        given()
                .spec(reqSpec)
                .body(updateUser)
                .when()
                .put("/user/" + userName)
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "updateUser")
    public void getUser() {
        given()
                .spec(reqSpec)
                .when()
                .get("/user/" + userName)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }


    @Test(dependsOnMethods = "getUser")
    public void deleteUser() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/user/" + userName)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("message", userName)
        ;
    }

    @Test(dependsOnMethods = "deleteUser")
    public void logout() {
        System.out.println("Executing logout method");
        given()
                .spec(reqSpec)
                .when()
                .get("/user/logout")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
}
