package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class UserResourceTest {

    @Test
    void updateUser_shouldReturn200WithAllFields() {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body("{\"name\":\"NewName\",\"last_name_1\":\"NewLast1\",\"last_name_2\":\"NewLast2\",\"age\":35}")
                .when()
                .patch("/user")
                .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("name", equalTo("NewName"))
                .body("last_name_1", equalTo("NewLast1"))
                .body("last_name_2", equalTo("NewLast2"))
                .body("age", equalTo(35))
                .body("email", equalTo("citizen@test.com"));
    }

    @Test
    void updateUser_shouldReturn200WithPartialFields() {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body("{\"name\":\"JustName\"}")
                .when()
                .patch("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo("JustName"));
    }

    @Test
    void updateUser_shouldReturn401WithNoToken() {
        given().contentType("application/json")
                .body("{\"name\":\"test\"}")
                .when()
                .patch("/user")
                .then()
                .statusCode(401);
    }
}
