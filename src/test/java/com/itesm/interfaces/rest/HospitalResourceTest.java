package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class HospitalResourceTest {

    // GET /hospitals

    @Test
    void getAll_shouldReturn200WithoutToken() {
        given().when().get("/hospitals").then().statusCode(200).body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getAll_shouldReturn200WithCitizenToken() {
        given().header("Authorization", "Bearer citizen-token")
                .when()
                .get("/hospitals")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    void getAll_shouldReturn200WithAdminToken() {
        given().header("Authorization", "Bearer admin-token")
                .when()
                .get("/hospitals")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    // GET /hospitals/my-hospitals

    // Usuario autenticado con hospitales asignados obtiene 200 y lista no vacía
    @Test
    void getMyHospitals_shouldReturn200WithValidToken() {
        given().header("Authorization", "Bearer health-token")
                .when()
                .get("/hospitals/my-hospitals")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class))
                .body("$.size()", greaterThan(0));
    }

    // Sin header Authorization el filtro rechaza la petición con 401
    @Test
    void getMyHospitals_shouldReturn401WithNoToken() {
        given().when().get("/hospitals/my-hospitals").then().statusCode(401);
    }

    // Token que no corresponde a ningún usuario en BD retorna 401
    @Test
    void getMyHospitals_shouldReturn401WithInvalidToken() {
        given().header("Authorization", "Bearer invalid-token-xyz")
                .when()
                .get("/hospitals/my-hospitals")
                .then()
                .statusCode(401);
    }

    // Usuario autenticado sin rol health obtiene 403
    @Test
    void getMyHospitals_shouldReturn403WithCitizenToken() {
        given().header("Authorization", "Bearer citizen-token")
                .when()
                .get("/hospitals/my-hospitals")
                .then()
                .statusCode(403);
    }

    // GET /hospitals/critical-medicines

    // Usuario con rol health obtiene 200 y la lista de hospitales con medicamentos críticos
    @Test
    void getCriticalMedicines_shouldReturn200WithHealthToken() {
        given().header("Authorization", "Bearer health-token")
                .when()
                .get("/hospitals/critical-medicines")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    // Usuario con rol citizen no tiene permiso para este endpoint y recibe 403
    @Test
    void getCriticalMedicines_shouldReturn403WithCitizenToken() {
        given().header("Authorization", "Bearer citizen-token")
                .when()
                .get("/hospitals/critical-medicines")
                .then()
                .statusCode(403);
    }

    // Sin header Authorization el filtro rechaza la petición con 401
    @Test
    void getCriticalMedicines_shouldReturn401WithNoToken() {
        given().when().get("/hospitals/critical-medicines").then().statusCode(401);
    }
}
