package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GoogleAuthResourceTest {

    @Test
    void googleSignIn_shouldReturn200_forCitizenToken() {
        given().header("Authorization", "Bearer citizen-token")
                .when()
                .get("/auth/google")
                .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("name", equalTo("Citizen"))
                .body("last_name1", equalTo("User"))
                .body("role", equalTo("citizen"))
                .body("email", equalTo("citizen@test.com"));
    }

    @Test
    void googleSignIn_shouldReturn403_forAdminToken() {
        given().header("Authorization", "Bearer admin-token")
                .when()
                .get("/auth/google")
                .then()
                .statusCode(403);
    }

    @Test
    void googleSignIn_shouldReturn403_forHealthToken() {
        given().header("Authorization", "Bearer health-token")
                .when()
                .get("/auth/google")
                .then()
                .statusCode(403);
    }

    @Test
    void googleSignIn_shouldReturn401_withoutToken() {
        given().when()
                .get("/auth/google")
                .then()
                .statusCode(401);
    }

    @Test
    void googleSignIn_shouldReturn401_withInvalidToken() {
        given().header("Authorization", "Bearer nonexistent-token")
                .when()
                .get("/auth/google")
                .then()
                .statusCode(401);
    }
}
