package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class MedicineResourceTest {

    private static final String CSV_HEADER = "generic_name,dosage_form,strength,presentation,stock\n";

    // Test 1: CSV válido con medicamento nuevo → 200, inserted=1, errors vacío
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

    // Test 2: CSV con medicamento ya existente en DB → reutiliza y retorna inserted=1
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

    // Test 5: Content-type no permitido (application/pdf) → 400 Bad Request
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

    // Test 6: Sin bearer token → 401 Unauthorized
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

    // Test 8: CSV solo con encabezado, sin filas de datos → 200, inserted=0, errors vacío
    @Test
    void uploadStock_shouldReturn200_withOnlyHeader() {
        given()
                .header("Authorization", "Bearer health-token")
                .multiPart("file", "stock.csv", CSV_HEADER.getBytes(), "text/csv")
                .when()
                .post("/medicines/upload-stock/1")
                .then()
                .statusCode(200)
                .body("inserted", equalTo(0))
                .body("errors", empty());
    }

    // Test 9: Fila con menos de 5 columnas → CsvParser lanza RuntimeException → 500
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

    // Test 10: Stock negativo → use case captura el error por fila → 200, inserted=0, errors con 1 entrada
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

    // Test 11: Stock no numérico → CsvParser lanza RuntimeException → 500
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
}
