package cnam.medical.pacs;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cnam.medical.pacs.domain.dao.CompteRenduRepo;
import cnam.medical.pacs.domain.dao.ExamenRepo;
import io.quarkus.test.junit.QuarkusTest;

import io.vertx.core.json.JsonObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;
import javax.inject.Inject;

@QuarkusTest
public class ExamenTest {

        // declarations
        @Inject
        ExamenRepo myRepo;
        @Inject
        CompteRenduRepo myCRRepo;

        Integer myLongPatient;
        Integer myLongProtocole;
        Integer myLongExamen;

        JsonObject patient = new JsonObject();
        JsonObject protocole = new JsonObject();

        @BeforeEach
        @Transactional
        void init() {

                // clear Exam Database
                myCRRepo.deleteAll();
                myRepo.deleteAll();

                JsonObject examen = new JsonObject();

                // Create the patient
                // 1->Create the PatientJson
                // 2->Post the patient
                // 3->Get the id
                // 4->Inject the id into the PatientJson
                patient.put("lastName", "Boufenara")
                                .put("firstName", "Anouar")
                                .put("birth", "1985-12-20")
                                .put("sex", "MALE");// 1
                given()
                                .body(patient.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/patient")
                                .then()
                                .statusCode(201);// 2
                List myList = given().when().get("/patient").body().jsonPath().get("id");
                myLongPatient = (Integer) myList.get(0);// 3
                patient.put("id", myLongPatient);// 4

                // Create the protocole
                // 1->Create the ProtocoleJson
                // 2->Post the Protocole
                // 3->Get the id
                // 4->Inject the id into the ProtocoleJson
                protocole.put("technique", "SCANNER")
                                .put("membre", "CRANE")
                                .put("type", "MOU")
                                .put("injection", "OUI");// 1
                given()
                                .body(protocole.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/protocole")
                                .then()
                                .statusCode(201);// 2
                myList = given().when().get("/protocole").body().jsonPath().get("id");
                myLongProtocole = (Integer) myList.get(0);// 3
                protocole.put("id", myLongProtocole);// 4

                // Create the exam
                // 1->Create the ExamJson
                // 2->Post the Exam
                // 3->Get the id
                examen.put("patient", patient)
                                .put("protocole", protocole)
                                .put("medecinRadiologue", "medecinRadiologue1")
                                .put("manipulateur", "manipulateur1")
                                .put("date", "2005-12-20");// 1
                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/examen")
                                .then()
                                .statusCode(201);// 2
                myList = given().when().get("/examen").body().jsonPath().get("id");
                myLongExamen = (Integer) myList.get(0);// 3

                // Create a new exam
                examen.remove("medecinRadiologue");
                examen.remove("manipulateur");
                examen.remove("date");
                examen.put("medecinRadiologue", "medecinRadiologue2")
                                .put("manipulateur", "manipulateur2")
                                .put("date", "2015-12-20");
                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/examen")
                                .then()
                                .statusCode(201);

                // Create a new exam
                examen.remove("medecinRadiologue");
                examen.remove("manipulateur");
                examen.remove("date");
                examen.put("medecinRadiologue", "medecinRadiologue3")
                                .put("manipulateur", "manipulateur3")
                                .put("date", "2025-12-20");
                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/examen")
                                .then()
                                .statusCode(201);
        }

        @AfterEach
        @Transactional
        void tearDown() {

                myCRRepo.deleteAll();
                myRepo.deleteAll();
        }

        @Test
        public void testGetExamens() {

                given()
                                .when().get("/examen")
                                .then()
                                .statusCode(200);
        }

        @Test
        public void testPostExamen() {

                // VERIFICATION
                given()
                                .when().get("/examen")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].patient.id", is(myLongPatient),
                                                "[0].protocole.id", is(myLongProtocole),
                                                "[0].medecinRadiologue", is("medecinRadiologue1"),
                                                "[0].manipulateur", is("manipulateur1"),
                                                "[0].date", is("2005-12-20"),
                                                "[1].patient.id", is(myLongPatient),
                                                "[1].protocole.id", is(myLongProtocole),
                                                "[1].medecinRadiologue", is("medecinRadiologue2"),
                                                "[1].manipulateur", is("manipulateur2"),
                                                "[1].date", is("2015-12-20"),
                                                "[2].patient.id", is(myLongPatient),
                                                "[2].protocole.id", is(myLongProtocole),
                                                "[2].medecinRadiologue", is("medecinRadiologue3"),
                                                "[2].manipulateur", is("manipulateur3"),
                                                "[2].date", is("2025-12-20")

                                );
        }

        @Test
        public void testPutExamen() {

                JsonObject examen = new JsonObject();

                examen.put("patient", patient)
                                .put("protocole", protocole)
                                .put("medecinRadiologue", "medecinRadiologue4")
                                .put("manipulateur", "manipulateur4")
                                .put("date", "2020-12-20");

                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/examen/" + myLongExamen)
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/examen")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].patient.id", is(myLongPatient),
                                                "[0].protocole.id", is(myLongProtocole),
                                                "[0].medecinRadiologue", is("medecinRadiologue4"),
                                                "[0].manipulateur", is("manipulateur4"),
                                                "[0].date", is("2020-12-20"),
                                                "[1].patient.id", is(myLongPatient),
                                                "[1].protocole.id", is(myLongProtocole),
                                                "[1].medecinRadiologue", is("medecinRadiologue2"),
                                                "[1].manipulateur", is("manipulateur2"),
                                                "[1].date", is("2015-12-20"),
                                                "[2].patient.id", is(myLongPatient),
                                                "[2].protocole.id", is(myLongProtocole),
                                                "[2].medecinRadiologue", is("medecinRadiologue3"),
                                                "[2].manipulateur", is("manipulateur3"),
                                                "[2].date", is("2025-12-20")

                                );

        }

        @Test
        public void testDeleteExamen() {

                given()
                                .when().get("/examen")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].patient.id", is(myLongPatient),
                                                "[0].protocole.id", is(myLongProtocole),
                                                "[0].medecinRadiologue", is("medecinRadiologue1"),
                                                "[0].manipulateur", is("manipulateur1"),
                                                "[0].date", is("2005-12-20"),
                                                "[1].patient.id", is(myLongPatient),
                                                "[1].protocole.id", is(myLongProtocole),
                                                "[1].medecinRadiologue", is("medecinRadiologue2"),
                                                "[1].manipulateur", is("manipulateur2"),
                                                "[1].date", is("2015-12-20"),
                                                "[2].patient.id", is(myLongPatient),
                                                "[2].protocole.id", is(myLongProtocole),
                                                "[2].medecinRadiologue", is("medecinRadiologue3"),
                                                "[2].manipulateur", is("manipulateur3"),
                                                "[2].date", is("2025-12-20")

                                );

                given()
                                .when().delete("/examen/" + myLongExamen)
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/examen")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(2),
                                                "[0].patient.id", is(myLongPatient),
                                                "[0].protocole.id", is(myLongProtocole),
                                                "[0].medecinRadiologue", is("medecinRadiologue2"),
                                                "[0].manipulateur", is("manipulateur2"),
                                                "[0].date", is("2015-12-20"),
                                                "[1].patient.id", is(myLongPatient),
                                                "[1].protocole.id", is(myLongProtocole),
                                                "[1].medecinRadiologue", is("medecinRadiologue3"),
                                                "[1].manipulateur", is("manipulateur3"),
                                                "[1].date", is("2025-12-20")

                                );

        }
}
