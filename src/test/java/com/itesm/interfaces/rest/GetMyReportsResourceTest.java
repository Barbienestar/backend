package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

@QuarkusTest
class GetMyReportsResourceTest {

    @Test
    void getMyReports_shouldReturn200WithValidToken() {
        given()
                .header("Authorization", "Bearer test-token")
                .when()
                .get("/reports/me")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getMyReports_shouldReturn401WithNoToken() {
        given()
                .when()
                .get("/reports/me")
                .then()
                .statusCode(401);
    }
}
