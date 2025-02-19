package ru.frigesty.tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ApiTests {

    @BeforeEach
    public void beforeEach() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

    @Test
    public void getUserTest() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.email", equalTo("janet.weaver@reqres.in"))
                .body("data.first_name", equalTo("Janet"))
                .body("data.last_name", equalTo("Weaver"))
                .body("data.avatar", equalTo("https://reqres.in/img/faces/2-image.jpg"));
    }

    @Test
    public void userNotFoundTest() {
        given()
                .when()
                .get("/users/900")
                .then()
                .statusCode(404);
    }

    @Test
    public void getUnknownResourcesTest() {
        given()
                .when()
                .get("/unknown")
                .then()
                .statusCode(200)
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .body("data[0].id", equalTo(1))
                .body("data[0].name", equalTo("cerulean"))
                .body("data[0].year", equalTo(2000))
                .body("data[0].color", equalTo("#98B2D1"))
                .body("data[0].pantone_value", equalTo("15-4020"));
    }

    @Test
    public void getResourceByIdTest() {
        given()
                .when()
                .get("/unknown/2")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(2))
                .body("data.name", equalTo("fuchsia rose"))
                .body("data.year", equalTo(2001))
                .body("data.color", equalTo("#C74375"))
                .body("data.pantone_value", equalTo("17-2031"));
    }

    @Test
    public void getNonExistingResourceTest() {
        given()
                .when()
                .get("/unknown/999")
                .then()
                .statusCode(404);
    }

    @Test
    public void createUserTest() {
        String requestBody = """
        {
            "name": "morpheus",
            "job": "leader"
        }
        """;

        given()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    public void updateUserTest() {
        String requestBody = """
        {
            "name": "morpheus",
            "job": "zion resident"
        }
    """;

        given()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"))
                .body("updatedAt", notNullValue());
    }

    @Test
    public void deleteUserTest() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }
}