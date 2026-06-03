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

    // GET /hospitals/{idHospital}/critical-medicines

    // Usuario con rol health obtiene 200 y la respuesta paginada con los campos esperados
    @Test
    void getCriticalMedicines_shouldReturn200WithHealthToken() {
        given().header("Authorization", "Bearer health-token")
                .when()
                .get("/hospitals/1/critical-medicines")
                .then()
                .statusCode(200)
                .body("hospital_id", notNullValue())
                .body("hospital_name", notNullValue())
                .body("critical_medicines", instanceOf(java.util.List.class))
                .body("total_elements", notNullValue())
                .body("total_pages", notNullValue())
                .body("current_page", equalTo(0));
    }

    // Paginación explícita: page=0&size=5 devuelve la primera página con tamaño correcto
    @Test
    void getCriticalMedicines_shouldReturnPagedResponseWithExplicitParams() {
        given().header("Authorization", "Bearer health-token")
                .queryParam("page", 0)
                .queryParam("size", 5)
                .when()
                .get("/hospitals/1/critical-medicines")
                .then()
                .statusCode(200)
                .body("current_page", equalTo(0))
                .body("critical_medicines", instanceOf(java.util.List.class))
                .body("critical_medicines.size()", lessThanOrEqualTo(5));
    }

    // Usuario con rol citizen no tiene permiso para este endpoint y recibe 403
    @Test
    void getCriticalMedicines_shouldReturn403WithCitizenToken() {
        given().header("Authorization", "Bearer citizen-token")
                .when()
                .get("/hospitals/1/critical-medicines")
                .then()
                .statusCode(403);
    }

    // Sin header Authorization el filtro rechaza la petición con 401
    @Test
    void getCriticalMedicines_shouldReturn401WithNoToken() {
        given().when().get("/hospitals/1/critical-medicines").then().statusCode(401);
    }
}
