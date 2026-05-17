package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import com.itesm.application.usecase.CreateReportUseCase;

import io.quarkus.test.junit.QuarkusTest;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

/** ReportResourceTest */
@QuarkusTest
class ReportResourceTest {
    @Inject CreateReportUseCase createReportUseCase;

    @Test
    void createReport_shouldReturn201WithValidData() throws Exception {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body(
                        "{\"description\":\"Test"
                            + " description\",\"image_url\":\"https://example.com/image.png\",\"medicine_id\":1,\"hospital_id\":1}")
                .when()
                .post("/reports")
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .body("description", equalTo("Test description"))
                .body("medicine_name", equalTo("Paracetamol"))
                .body("hospital_name", equalTo("Hospital name"))
                .body("created_at", notNullValue());
    }

    @Test
    void createReport_shouldReturn401WithNoToken() throws Exception {
        given().contentType("application/json")
                .body(
                        "{\"description\":\"Test"
                            + " description\",\"imageUrl\":\"https://example.com/image.png\",\"medicineId\":1,\"hospitalId\":1}")
                .when()
                .post("/reports")
                .then()
                .statusCode(401);
    }

    @Test
    void createReport_shouldReturn400WithIncompleteBody() throws Exception {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body(
                        "{ \"description\",\"imageUrl\":\"https://example.com/image.png\",\"medicineId\":1,\"hospitalId\":1}")
                .when()
                .post("/reports")
                .then()
                .statusCode(400);
    }
}
