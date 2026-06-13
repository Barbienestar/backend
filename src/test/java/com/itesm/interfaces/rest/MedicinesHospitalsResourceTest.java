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
    void getMonthlyReports_shouldReturn200WithValidHospitalId() {
        given().pathParam("idHospital", 1)
                .queryParam("first_date", "2026-06-01")
                .queryParam("second_date", "2026-06-01")
                .header("Authorization", "Bearer health-token")
                .when()
                .get("/medicines-hospitals/{idHospital}/monthly-reports")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test
    void getMonthlyReports_shouldReturn401WihoutToken() {
        given().pathParam("idHospital", 62)
                .queryParam("first_date", "2026-05-01")
                .queryParam("second_date", "2026-06-01")
                .when()
                .get("/medicines-hospitals/{idHospital}/monthly-reports")
                .then()
                .log()
                .all()
                .statusCode(401);
    }
}
