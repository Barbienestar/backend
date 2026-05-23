package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class MedicinesHospitalsResourceTest {

    @Test
    void getByMedicine_shouldReturn200WithValidMedicineName() {
        given().queryParam("medicine_name", "Paracetamol")
                .when()
                .get("/medicines-hospitals/stock")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getByMedicine_shouldReturn400WhenMedicineNameIsMissing() {
        given().when()
                .get("/medicines-hospitals/stock")
                .then()
                .statusCode(400)
                .body("error", equalTo("medicine_name is required"));
    }

    @Test
    void getByMedicine_shouldReturn200WithoutToken() {
        given().queryParam("medicine_name", "Ibuprofeno")
                .when()
                .get("/medicines-hospitals/stock")
                .then()
                .statusCode(200);
    }

    @Test
    void getPeriodReports_shouldReturn200WithHealthToken() {
        given().header("Authorization", "Bearer health-token")
                .queryParam("start-date", "2024-01-01")
                .queryParam("end-date", "2024-01-31")
                .when()
                .get("/medicines-hospitals/period/1")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class))
                .body("size()", equalTo(3))
                .body("[0].report_date", equalTo("2024-01-05"))
                .body("[0].total_accepted_reports", equalTo(10));
    }

    @Test
    void getPeriodReports_shouldReturn401WithoutToken() {
        given().queryParam("start-date", "2024-01-01")
                .queryParam("end-date", "2024-01-31")
                .when()
                .get("/medicines-hospitals/period/1")
                .then()
                .statusCode(401);
    }

    @Test
    void getPeriodReports_shouldReturn403WithWrongRole() {
        given().header("Authorization", "Bearer citizen-token")
                .queryParam("start-date", "2024-01-01")
                .queryParam("end-date", "2024-01-31")
                .when()
                .get("/medicines-hospitals/period/1")
                .then()
                .statusCode(403);
    }

    @Test
    void getPeriodReports_shouldReturn400WhenParamsMissing() {
        given().header("Authorization", "Bearer health-token")
                .when()
                .get("/medicines-hospitals/period/1")
                .then()
                .statusCode(400);
    }
}
