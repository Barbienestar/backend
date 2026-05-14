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
                .header("Authorization", "Bearer 1289319831928")
                .body(
                        "{\"description\":\"Test"
                            + " description\",\"imageUrl\":\"https://example.com/image.png\",\"medicineId\":1,\"hospitalId\":1}")
                .when()
                .post("/reports/create")
                .then()
                .statusCode(201)
                .body("id", greaterThan(0))
                .body("description", equalTo("Test description"))
                .body("medicineName", equalTo("Paracetamol"))
                .body("hospitalName", equalTo("Hospital name"))
                .body("createdAt", notNullValue());
    }

    @Test
    void createReport_shouldReturn401WithNoToken() throws Exception {
        given().contentType("application/json")
                .body(
                        "{\"description\":\"Test"
                            + " description\",\"imageUrl\":\"https://example.com/image.png\",\"medicineId\":1,\"hospitalId\":1}")
                .when()
                .post("/reports/create")
                .then()
                .statusCode(401);
    }

    @Test
    void createReport_shouldReturn400WithIncompleteBody() throws Exception {
        given().contentType("application/json")
                .header("Authorization", "Bearer 1289319831928")
                .body(
                        "{ \"description\",\"imageUrl\":\"https://example.com/image.png\",\"medicineId\":1,\"hospitalId\":1}")
                .when()
                .post("/reports/create")
                .then()
                .statusCode(400);
    }
}
