package PetShopSwaggerAPI;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetShopPetsTest {
    RequestSpecification reqSpec;
    int petId;

    String petStatus="sold";

    Faker faker=new Faker();

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
    public void addNewPetStore(){
        Map<String,Object> category=new HashMap<>();
        category.put("id",faker.number().randomDigitNotZero());
        category.put("name",faker.lorem().word());

        List<Map<String,Object>> tags=new ArrayList<>();
        Map<String,Object> tag=new HashMap<>();
        tag.put("id",faker.number().randomDigitNotZero());
        tag.put("name",faker.lorem().word());
        tags.add(tag);

        Map<String,Object> pet=new HashMap<>();
        pet.put("id",faker.number().randomDigitNotZero());
        pet.put("category",category);
        pet.put("name",faker.name().firstName());
        pet.put("photoUrls",List.of("https://www.bil-jac.com/media/qsgk35e1/factsaboutdoggiedodo.jpg?anchor=center&mode=crop&width=1024&height=512"));
        pet.put("tags",tags);
        pet.put("status",petStatus);

        Response response=
                given()
                        .spec(reqSpec)
                        .body(pet)
                        .when()
                        .post("/pet")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("name", equalTo(pet.get("name")))
                        .extract().response();

        petId=response.path("id");
        System.out.println("petId = " + petId);

    }
    @Test(dependsOnMethods = "addNewPetStore")
    public void updateExistingPet(){

        Map<String,Object> pet=new HashMap<>();
        pet.put("name","bakky");
        pet.put("status",petStatus);

                given()
                        .spec(reqSpec)
                        .body(pet)
                        .when()
                        .put("/pet")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("name", equalTo(pet.get("name")))
                        .extract().response();

    }

    @Test(dependsOnMethods = "updateExistingPet")
    public void updatePetWithFormData(){
        Map<String,Object> petFormData=new HashMap<>();
        petFormData.put("name","chukky");
        petFormData.put("status","available");

        given()
                .spec(reqSpec)
                .contentType("application/x-www-form-urlencoded")
                .formParams(petFormData)
                .when()
                .post("/pet/"+petId)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("message", String.valueOf(equalTo(petId)))
        ;
    }

    @Test(dependsOnMethods = "updateExistingPet")
    public void uploadPetImage(){

        String filePath="/Users/erdiozturk/Desktop/Doggie.jpeg";
        Response response=
        given()
                .spec(reqSpec)
                .contentType("multipart/form-data")
                .multiPart("file",new File(filePath),"image/jpeg")
                .multiPart("additionalMetadata","Test metadata")
                .when()
                .post("/pet/"+petId+"/uploadImage")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();
        String fileUrl=response.getBody().asString();
        System.out.println("Uploaded file Url = " + fileUrl);
    }

    @Test(dependsOnMethods = "uploadPetImage")
    public void getPetById(){


        given()
                .spec(reqSpec)
                .when()
                .get("/pet/"+petId)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }
    @Test(dependsOnMethods = "getPetById")
    public void deletePet(){
        given()
                .spec(reqSpec)
                .when()
                .delete("/pet/"+petId)
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("message", String.valueOf(equalTo(petId)))
        ;
    }

    @Test(dependsOnMethods = "deletePet")
    public void findByStatus(){

        given()
                .spec(reqSpec)
                .param("status",petStatus)
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
        ;
    }
}
