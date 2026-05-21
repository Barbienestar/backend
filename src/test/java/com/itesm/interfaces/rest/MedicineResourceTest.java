package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class MedicineResourceTest {

    private static final String CSV_HEADER = "generic_name,dosage_form,strength,presentation,stock\n";

    // GET /medicines

    @Test
    void getAll_shouldReturn200WithoutToken() {
        given()
                .when()
                .get("/medicines")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getAll_shouldReturn200WithQueryParam() {
        given()
                .queryParam("q", "Paracetamol")
                .when()
                .get("/medicines")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getAll_shouldReturn200WithBlankQueryParam() {
        given()
                .queryParam("q", "   ")
                .when()
                .get("/medicines")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getAll_shouldReturn200WithAdminToken() {
        given()
                .header("Authorization", "Bearer admin-token")
                .when()
                .get("/medicines")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    // POST /medicines/upload-stock/{idHospital}

    @Test
    void uploadStock_shouldReturn200_withNewMedicine() {
        String csv = CSV_HEADER + "Ibuprofeno,Tablet,400mg,Box,50\n";

        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.csv", csv.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(200)
                .body("inserted", equalTo(1))
                .body("errors", empty());
    }

    // CSV con medicamento ya existente en DB reutiliza y retorna inserted=1
    @Test
    void uploadStock_shouldReturn200_withExistingMedicine() {
        String csv = CSV_HEADER + "Paracetamol,Tablet,500mg,Box,20\n";

        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.csv", csv.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(200)
                .body("inserted", equalTo(1))
                .body("errors", empty());
    }

    @Test
    void uploadStock_shouldReturn200_withNegativeStock_andErrorInList() {
        String csv = CSV_HEADER + "Ibuprofeno,Tablet,400mg,Box,-5\n";

        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.csv", csv.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(200)
                .body("inserted", equalTo(0))
                .body("errors", hasSize(1));
    }

    @Test
    void uploadStock_shouldReturn400_whenContentTypeIsInvalid() {
        String csv = CSV_HEADER + "Ibuprofeno,Tablet,400mg,Box,50\n";

        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.pdf", csv.getBytes(), "application/pdf")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(400);
    }
    @Test
    void uploadStock_shouldReturn400_whenNoFileProvided() {
        given()
                .header("Authorization", "Bearer health-token")
                .contentType("multipart/form-data")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(400);
    }

    @Test
    void uploadStock_shouldReturn500_whenRowIsIncomplete() {
        String csv = CSV_HEADER + "Ibuprofeno,Tablet\n";

        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.csv", csv.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(500);
    }

    @Test
    void uploadStock_shouldReturn500_whenStockIsNotNumeric() {
        String csv = CSV_HEADER + "Ibuprofeno,Tablet,400mg,Box,abc\n";

        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.csv", csv.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(500);
    }

    @Test
    void uploadStock_shouldReturn401_whenNoToken() {
        String csv = CSV_HEADER + "Ibuprofeno,Tablet,400mg,Box,50\n";

        given()
                .multiPart("file", "stock.csv", csv.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(401);
    }
}