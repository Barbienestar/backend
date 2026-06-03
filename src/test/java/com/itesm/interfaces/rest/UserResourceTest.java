package com.itesm.interfaces.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class UserResourceTest {

    private static final String CITIZEN_BODY =
            "{\"name\":\"New\",\"last_name_1\":\"Citizen\",\"last_name_2\":\"Test\",\"age\":25,"
                    + "\"email\":\"citizen-new@test.com\",\"password\":\"Passw0rd\",\"role_id\":3,\"suburb_id\":1,"
                    + "\"hospital_ids\":[]}";

    private static final String CITIZEN_BODY_WITH_TOKEN =
            "{\"name\":\"New2\",\"last_name_1\":\"Citizen\",\"last_name_2\":\"Test\",\"age\":25,"
                    + "\"email\":\"citizen-new2@test.com\",\"password\":\"Passw0rd\",\"role_id\":3,\"suburb_id\":1,"
                    + "\"hospital_ids\":[]}";

    private static final String PRIVILEGED_BODY =
            "{\"name\":\"New\",\"last_name_1\":\"Admin\",\"email\":\"admin-new@test.com\","
                    + "\"password\":\"Passw0rd\",\"role_id\":1,\"suburb_id\":1,\"hospital_ids\":[]}";

    @Test
    void createCitizen_shouldReturn201WithoutToken() {
        given().contentType("application/json")
                .body(CITIZEN_BODY)
                .when()
                .post("/user/citizen")
                .then()
                .statusCode(201)
                .body("name", equalTo("New"))
                .body("last_name1", equalTo("Citizen"))
                .body("role", equalTo("citizen"));
    }

    @Test
    void createCitizen_shouldReturn201WithToken() {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body(CITIZEN_BODY_WITH_TOKEN)
                .when()
                .post("/user/citizen")
                .then()
                .statusCode(201);
    }

    @Test
    void createPrivileged_shouldReturn201AsAdmin() {
        given().contentType("application/json")
                .header("Authorization", "Bearer admin-token")
                .body(PRIVILEGED_BODY)
                .when()
                .post("/user/privileged")
                .then()
                .statusCode(201)
                .body("name", equalTo("New"))
                .body("role", equalTo("admin"));
    }

    @Test
    void createPrivileged_shouldReturn401WithoutToken() {
        given().contentType("application/json")
                .body(PRIVILEGED_BODY)
                .when()
                .post("/user/privileged")
                .then()
                .statusCode(401);
    }

    @Test
    void createPrivileged_shouldReturn403AsCitizen() {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body(PRIVILEGED_BODY)
                .when()
                .post("/user/privileged")
                .then()
                .statusCode(403);
    }

    @Test
    void createPrivileged_shouldReturn403AsHealth() {
        given().contentType("application/json")
                .header("Authorization", "Bearer health-token")
                .body(PRIVILEGED_BODY)
                .when()
                .post("/user/privileged")
                .then()
                .statusCode(403);
    }

    @Test
    void updateUser_shouldReturn200WithAllFields() {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body("{\"name\":\"NewName\",\"last_name1\":\"NewLast1\",\"last_name2\":\"NewLast2\",\"age\":35}")
                .when()
                .patch("/user")
                .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("name", equalTo("NewName"))
                .body("last_name1", equalTo("NewLast1"))
                .body("last_name2", equalTo("NewLast2"))
                .body("age", equalTo(35))
                .body("email", equalTo("citizen@test.com"));
    }

    @Test
    void updateUser_shouldReturn200WithPartialFields() {
        given().contentType("application/json")
                .header("Authorization", "Bearer citizen-token")
                .body("{\"name\":\"JustName\"}")
                .when()
                .patch("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo("JustName"));
    }

    @Test
    void updateUser_shouldReturn401WithNoToken() {
        given().contentType("application/json")
                .body("{\"name\":\"test\"}")
                .when()
                .patch("/user")
                .then()
                .statusCode(401);
    }
}
