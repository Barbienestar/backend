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

    @Test
    void changeReportStatus_shouldReturn200WithValidData() throws Exception {
        given().contentType("application/json")
                .header("Authorization", "Bearer admin-token")
                .when()
                .put("reports/1/status/2")
                .then()
                .statusCode(200);
    }

    @Test
    void changeReportStatus_shouldReturn403WithCitizenUser() throws Exception {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .when()
                .put("reports/1/status/2")
                .then()
                .statusCode(403);
    }

    @Test
    void changeReportStatus_shouldReturn404WithInvalidReport() throws Exception {
        given().contentType("application/json")
                .header("Authorization", "Bearer admin-token")
                .when()
                .put("reports/1982128182918293128/status/2")
                .then()
                .statusCode(404);
    }

    @Test
    void getReportsByStatus_shouldReturnPagedReportResult() throws Exception {
        given()
                .header("Authorization", "Bearer admin-token")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/reports/status/1")
                .then()
                .statusCode(200)
                .body("items", notNullValue())
                .body("page", equalTo(0))
                .body("page_size", equalTo(10))
                .body("total_items", equalTo(0))
                .body("total_pages", equalTo(0));
    }

    @Test
    void getReportsByStatus_shouldThrowWithUnauthorizedUser() throws Exception {
        given()
                .header("Authorization", "Bearer citizen-token")
                .when()
                .get("/reports/status/1")
                .then()
                .statusCode(403);
    }
}
