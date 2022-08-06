package cnam.medical.pacs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cnam.medical.pacs.domain.dao.CompteRenduRepo;
import cnam.medical.pacs.domain.dao.DocumentRepo;
import cnam.medical.pacs.domain.dao.ExamenRepo;
import cnam.medical.pacs.domain.dao.PatientRepo;
import io.quarkus.test.junit.QuarkusTest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import javax.transaction.Transactional;
import javax.inject.Inject;

@QuarkusTest
public class PatientTest {

        Integer myLong;
        
        
        @Inject
        PatientRepo myrepo;
        @Inject
        ExamenRepo myExamenRepo;
        @Inject
        CompteRenduRepo myCRepo;
        @Inject
        DocumentRepo myDocRepo;

        @BeforeEach
        @Transactional
        void init() {


                myDocRepo.deleteAll();
                myCRepo.deleteAll();
                myExamenRepo.deleteAll();
                myrepo.deleteAll();

                // POST1
                given()
                                .body("{\"lastName\": \"Boufenara\", \"firstName\": \"Anouar\", \"birth\": \"1985-12-20\", \"sex\": \"MALE\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/patient")
                                .then()
                                .statusCode(201);

                List myList = given().when().get("/patient").body().jsonPath().get("id");
                myLong = (Integer) myList.get(0);

                // POST2
                given()
                                .body("{\"lastName\": \"testFemale\", \"firstName\": \"testFemale\", \"birth\": \"1985-12-20\", \"sex\": \"FEMALE\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/patient")
                                .then()
                                .statusCode(201);

                // POST3
                given()
                                .body("{\"lastName\": \"testMale\", \"firstName\": \"testMale\", \"birth\": \"2005-12-20\", \"sex\": \"MALE\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/patient")
                                .then()
                                .statusCode(201);

        }

        @AfterEach
        @Transactional
        void tearDown() {

                myDocRepo.deleteAll();
                myCRepo.deleteAll();
                myExamenRepo.deleteAll();
                myrepo.deleteAll();

        }

        @Test
        public void testGetPatients() {

                given()
                                .when().get("/patient")
                                .then()
                                .statusCode(200);

        }

        @Test
        public void testPostPatient() {

                // VERIFICATION
                given()
                                .when().get("/patient")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].lastName", is("Boufenara"),
                                                "[0].firstName", is("Anouar"),
                                                "[0].birth", is("1985-12-20"),
                                                "[0].sex", is("MALE"),
                                                "[1].lastName", is("testFemale"),
                                                "[1].firstName", is("testFemale"),
                                                "[1].birth", is("1985-12-20"),
                                                "[1].sex", is("FEMALE"),
                                                "[2].lastName", is("testMale"),
                                                "[2].firstName", is("testMale"),
                                                "[2].birth", is("2005-12-20"),
                                                "[2].sex", is("MALE")

                                );
        }

        @Test
        public void testPutPatient() {

                // VERIFICATION
                given()
                                .when().get("/patient")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].lastName", is("Boufenara"),
                                                "[0].firstName", is("Anouar"),
                                                "[0].birth", is("1985-12-20"),
                                                "[0].sex", is("MALE"),
                                                "[1].lastName", is("testFemale"),
                                                "[1].firstName", is("testFemale"),
                                                "[1].birth", is("1985-12-20"),
                                                "[1].sex", is("FEMALE"),
                                                "[2].lastName", is("testMale"),
                                                "[2].firstName", is("testMale"),
                                                "[2].birth", is("2005-12-20"),
                                                "[2].sex", is("MALE")

                                );

                // PUT
                given()
                                .body("{\"id\": " + myLong
                                                + " , \"lastName\": \"testUpdated\", \"firstName\": \"testUpdated\", \"birth\": \"1985-12-21\", \"sex\": \"FEMALE\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/patient/" + myLong)
                                .then()
                                .statusCode(200);

                // VERIFICATION2
                given()
                                .when().get("/patient")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].lastName", is("testUpdated"),
                                                "[0].firstName", is("testUpdated"),
                                                "[0].birth", is("1985-12-21"),
                                                "[0].sex", is("FEMALE"),
                                                "[1].lastName", is("testFemale"),
                                                "[1].firstName", is("testFemale"),
                                                "[1].birth", is("1985-12-20"),
                                                "[1].sex", is("FEMALE"),
                                                "[2].lastName", is("testMale"),
                                                "[2].firstName", is("testMale"),
                                                "[2].birth", is("2005-12-20"),
                                                "[2].sex", is("MALE")

                                );
        }

        @Test
        public void testFindPatientByBirth() {

                // VERIFICATION1
                given()
                                .when().get("/patient/birth/1985-12-20")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(2),
                                                "[0].lastName", is("Boufenara"),
                                                "[0].firstName", is("Anouar"),
                                                "[0].birth", is("1985-12-20"),
                                                "[0].sex", is("MALE"),

                                                "[1].lastName", is("testFemale"),
                                                "[1].firstName", is("testFemale"),
                                                "[1].birth", is("1985-12-20"),
                                                "[1].sex", is("FEMALE")

                                );

        }

        @Test
        public void testFindPatientByID() {

                // VERIFICATION1
                given().when().get("/patient/id/" + myLong).then()
                                .statusCode(200)
                                .body("id", is(myLong),
                                                "lastName", is("Boufenara"),
                                                "firstName", is("Anouar"),
                                                "birth", is("1985-12-20"),
                                                "sex", is("MALE"));

        }

        @Test
        public void testFindPatientBySex() {

                // VERIFICATION1
                given()
                                .when().get("/patient/sex/MALE")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(2),
                                                "[0].lastName", is("Boufenara"),
                                                "[0].firstName", is("Anouar"),
                                                "[0].birth", is("1985-12-20"),
                                                "[0].sex", is("MALE"),

                                                "[1].lastName", is("testMale"),
                                                "[1].firstName", is("testMale"),
                                                "[1].birth", is("2005-12-20"),
                                                "[1].sex", is("MALE")

                                );

        }

        @Test
        public void testDeletePatient() {

                // VERIFICATION1
                given()
                                .when().get("/patient")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].lastName", is("Boufenara"),
                                                "[0].firstName", is("Anouar"),
                                                "[0].birth", is("1985-12-20"),
                                                "[0].sex", is("MALE"),

                                                "[1].lastName", is("testFemale"),
                                                "[1].firstName", is("testFemale"),
                                                "[1].birth", is("1985-12-20"),
                                                "[1].sex", is("FEMALE")

                                );

                given()
                                .when().delete("/patient/" + myLong)
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/patient")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(2),

                                                "[0].lastName", is("testFemale"),
                                                "[0].firstName", is("testFemale"),
                                                "[0].birth", is("1985-12-20"),
                                                "[0].sex", is("FEMALE")

                                );

        }

}
