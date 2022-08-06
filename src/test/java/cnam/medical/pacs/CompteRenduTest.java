package cnam.medical.pacs;

import javax.inject.Inject;
import io.vertx.core.json.JsonObject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cnam.medical.pacs.domain.dao.CompteRenduRepo;
import cnam.medical.pacs.domain.model.CompteRendu;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

@QuarkusTest
public class CompteRenduTest {

        Integer myLong;
        Integer myLongPatient;
        Integer myLongProtocole;
        Integer myLongExamen;

        JsonObject patient = new JsonObject();
        JsonObject protocole = new JsonObject();
        JsonObject examen = new JsonObject();
        JsonObject requestParams = new JsonObject();

        @Inject
        CompteRenduRepo myRepo;

        @BeforeEach
        @Transactional
        void init() {

                // Clear CompteRenduData
                myRepo.deleteAll();

                // Create Patient JSON
                patient.put("lastName", "Boufenara")
                                .put("firstName", "Anouar")
                                .put("birth", "1985-12-20")
                                .put("sex", "MALE");

                // Post Patient
                given()
                                .body(patient.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/patient")
                                .then()
                                .statusCode(201);

                // Get id of the patient
                List myList = given().when().get("/patient").body().jsonPath().get("id");
                myLongPatient = (Integer) myList.get(0);

                // Insert the id on the JSON Patient Object
                patient.put("id", myLongPatient);

                // Create Protocole JSON
                protocole.put("technique", "SCANNER")
                                .put("membre", "CRANE")
                                .put("type", "MOU")
                                .put("injection", "OUI");

                // POST Protocole
                given()
                                .body(protocole.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/protocole")
                                .then()
                                .statusCode(201);

                // Get the id of the Protocole
                myList = given().when().get("/protocole").body().jsonPath().get("id");
                myLongProtocole = (Integer) myList.get(0);

                // Insert the id on the JSON Protocole Object
                protocole.put("id", myLongProtocole);

                // Create the Exam JSON using the PatientJson and ProtocoleJson
                examen.put("patient", patient)
                                .put("protocole", protocole)
                                .put("medecinRadiologue", "medecinRadiologue1")
                                .put("manipulateur", "manipulateur1")
                                .put("date", "2005-12-20");

                // Post the Exam
                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/examen")
                                .then()
                                .statusCode(201);

                // Get the id of the Exam
                myList = given().when().get("/examen").body().jsonPath().get("id");
                myLongExamen = (Integer) myList.get(0);

                // Insert the id on the JSON Exam Object
                examen.put("id", myLongExamen);

                // Create the CR JSON usin the ExamJSON
                requestParams.put("secretaireLogin", "mySecretaire1")
                                .put("medecinLogin", "myDoc1")
                                .put("titre", "Titre du CR1")
                                .put("corp",
                                                "En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1")
                                .put("signature", "myDocSignature1")
                                .put("etat", "VALIDATED")
                                .put("examen", examen);

                // Post the first CR
                given()
                                .body(requestParams.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/compterendu")
                                .then()
                                .statusCode(201);

                // Get the id of the CR
                myList = given().when().get("/compterendu").body().jsonPath().get("id");
                myLong = (Integer) myList.get(0);

                // Create a new Exam
                // 1-> Remove the id of the previous ExamJson
                // 2-> Post the new Exam
                // 3-> Get the id of the new Exam
                // 4-> Put the new id on the new ExamJson
                examen.remove("id");// 1
                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/examen")
                                .then()
                                .statusCode(201);// 2

                myList = given().when().get("/examen").body().jsonPath().get("id");
                myLongExamen = (Integer) myList.get(1);// 3
                examen.put("id", myLongExamen);// 4

                // Create a new CRJson using the new ExamJson
                JsonObject requestParams2 = new JsonObject();
                requestParams2.put("secretaireLogin", "mySecretaire2")
                                .put("medecinLogin", "myDoc2")
                                .put("titre", "Titre du CR2")
                                .put("corp",
                                                "En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2")
                                .put("signature", "myDocSignature2")
                                .put("etat", "WRITTEN")
                                .put("examen", examen);

                // Post the second CR
                given()
                                .body(requestParams2.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/compterendu")
                                .then()
                                .statusCode(201);

                // Create a new Exam
                // 1-> Remove the id of the previous ExamJson
                // 2-> Post the new Exam
                // 3-> Get the id of the new Exam
                // 4-> Put the new id on the new ExamJson
                examen.remove("id");// 1
                given()
                                .body(examen.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/examen")
                                .then()
                                .statusCode(201);// 2
                myList = given().when().get("/examen").body().jsonPath().get("id");
                myLongExamen = (Integer) myList.get(2);// 3
                examen.put("id", myLongExamen);// 4

                // Create a new CRJson using the new ExamJson
                JsonObject requestParams3 = new JsonObject();
                requestParams3.put("secretaireLogin", "mySecretaire3")
                                .put("medecinLogin", "myDoc3")
                                .put("titre", "Titre du CR3")
                                .put("corp",
                                                "En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche3")
                                .put("signature", "myDocSignature3")
                                .put("etat", "NULL")
                                .put("examen", examen);

                // Post the third CR
                given()
                                .body(requestParams3.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/compterendu")
                                .then()
                                .statusCode(201);

        }

        @AfterEach
        @Transactional
        void tearDown() {

                myRepo.deleteAll();
        }

        @Test
        public void testGetCompteRendu() {

                given()
                                .when().get("/compterendu")
                                .then()
                                .statusCode(200)
                                .body("size()", is(3));

        }

        @Test
        public void testPostCompteRendu() {

                given()
                                .when().get("/compterendu")
                                .then()
                                .statusCode(200).body("$.size()", is(3),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"),

                                                "[1].secretaireLogin", is("mySecretaire2"),
                                                "[1].medecinLogin", is("myDoc2"),
                                                "[1].titre", is("Titre du CR2"),
                                                "[1].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[1].signature", is("myDocSignature2"),
                                                "[1].etat", is("WRITTEN"),

                                                "[2].secretaireLogin", is("mySecretaire3"),
                                                "[2].medecinLogin", is("myDoc3"),
                                                "[2].titre", is("Titre du CR3"),
                                                "[2].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche3"),
                                                "[2].signature", is("myDocSignature3"),
                                                "[2].etat", is("NULL"));

        }

        @Test
        @Transactional
        public void testPutCompteRendu() {
                
                CompteRenduRepo myrrepo = new CompteRenduRepo();
                CompteRendu cr = myrrepo.findById((long) myLong);
                JsonObject tempExam = new JsonObject();
                tempExam.put("id", cr.examen.id);

                requestParams.clear();
                requestParams.put("secretaireLogin", "NewmySecretaire1")
                                .put("medecinLogin", "NewmyDoc1")
                                .put("titre", "NewTitre du CR1")
                                .put("corp",
                                                "En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1")
                                .put("signature", "NewmyDocSignature1")
                                .put("etat", "WRITTEN")
                                .put("examen", tempExam);

                // Put
                given()
                                .body(requestParams.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/compterendu/" + (long) myLong)
                                .then()
                                .statusCode(200);

                // TEST Put result
                given()
                                .when().get("/compterendu/id/" + myLong)
                                .then()
                                .statusCode(200).body("id", is(myLong),
                                                "secretaireLogin", is("NewmySecretaire1"),
                                                "medecinLogin", is("NewmyDoc1"),
                                                "titre", is("NewTitre du CR1"),
                                                "corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "signature", is("NewmyDocSignature1"),
                                                "etat", is("WRITTEN"));

        }

        @Test
        public void testdeleteCompteRendu() {

                given()
                                .when().get("/compterendu")
                                .then()
                                .statusCode(200).body("$.size()", is(3),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"));

                // DELETE
                given()
                                .when().delete("/compterendu/" + myLong)
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/compterendu")
                                .then()
                                .statusCode(200).body("$.size()", is(2),
                                                "[0].secretaireLogin", is("mySecretaire2"),
                                                "[0].medecinLogin", is("myDoc2"),
                                                "[0].titre", is("Titre du CR2"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[0].signature", is("myDocSignature2"),
                                                "[0].etat", is("WRITTEN"));

        }

        @Test
        public void testgetCompteRenduById() {

                given()
                                .when().get("/compterendu/id/" + myLong)
                                .then()
                                .statusCode(200).body("id", is(myLong),
                                                "secretaireLogin", is("mySecretaire1"),
                                                "medecinLogin", is("myDoc1"),
                                                "titre", is("Titre du CR1"),
                                                "corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "signature", is("myDocSignature1"),
                                                "etat", is("VALIDATED"));
        }

        @Test
        public void testgetCompteRenduBySecretaire() {

                given()
                                .when().get("/compterendu/secretaireLogin/mySecretaire2")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire2"),
                                                "[0].medecinLogin", is("myDoc2"),
                                                "[0].titre", is("Titre du CR2"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[0].signature", is("myDocSignature2"),
                                                "[0].etat", is("WRITTEN"));

                given()
                                .when().get("/compterendu/secretaireLogin/mySecretaire1")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"));

        }

        @Test
        public void testgetCompteRenduByMedecin() {

                given()
                                .when().get("/compterendu/medecinLogin/myDoc2")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire2"),
                                                "[0].medecinLogin", is("myDoc2"),
                                                "[0].titre", is("Titre du CR2"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[0].signature", is("myDocSignature2"),
                                                "[0].etat", is("WRITTEN"));

                given()
                                .when().get("/compterendu/medecinLogin/myDoc1")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"));

        }

        @Test
        public void testgetCompteRenduByEtat() {

                given()
                                .when().get("/compterendu/etat/WRITTEN")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire2"),
                                                "[0].medecinLogin", is("myDoc2"),
                                                "[0].titre", is("Titre du CR2"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[0].signature", is("myDocSignature2"),
                                                "[0].etat", is("WRITTEN"));

                given()
                                .when().get("/compterendu/etat/VALIDATED")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"));

        }

        @Test
        public void testgetCompteRenduByMedecinANdEtat() {

                given()
                                .when().get("/compterendu/medecinLogin/myDoc2/WRITTEN")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire2"),
                                                "[0].medecinLogin", is("myDoc2"),
                                                "[0].titre", is("Titre du CR2"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[0].signature", is("myDocSignature2"),
                                                "[0].etat", is("WRITTEN"));

                given()
                                .when().get("/compterendu/medecinLogin/myDoc1/VALIDATED")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"));

                given()
                                .when().get("/compterendu/medecinLogin/myDoc1/WRITTEN")
                                .then()
                                .statusCode(200).body("$.size()", is(0));

                given()
                                .when().get("/compterendu/medecinLogin/myDoc2/VALIDATED")
                                .then()
                                .statusCode(200).body("$.size()", is(0));

        }

        @Test
        public void testgetCompteRenduBySecretaireAndEtat() {

                given()
                                .when().get("/compterendu/secretaireLogin/mySecretaire2/WRITTEN")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire2"),
                                                "[0].medecinLogin", is("myDoc2"),
                                                "[0].titre", is("Titre du CR2"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche2"),
                                                "[0].signature", is("myDocSignature2"),
                                                "[0].etat", is("WRITTEN"));

                given()
                                .when().get("/compterendu/secretaireLogin/mySecretaire1/VALIDATED")
                                .then()
                                .statusCode(200).body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire1"),
                                                "[0].medecinLogin", is("myDoc1"),
                                                "[0].titre", is("Titre du CR1"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche1"),
                                                "[0].signature", is("myDocSignature1"),
                                                "[0].etat", is("VALIDATED"));

                given()
                                .when().get("/compterendu/secretaireLogin/mySecretaire1/WRITTEN")
                                .then()
                                .statusCode(200).body("$.size()", is(0));

                given()
                                .when().get("/compterendu/secretaireLogin/mySecretaire2/VALIDATED")
                                .then()
                                .statusCode(200).body("$.size()", is(0));

        }



        @Test
        public void testgetCompteRenduByExamen() {

                patient.clear();
                patient.put("id", myLongExamen);

                given()
                .body(patient.toString())
                .header("Content-Type", "application/json")
                .when()
                .get("/compterendu/examen")
                .then()
                .statusCode(200)
                .body("$.size()", is(1),
                                                "[0].secretaireLogin", is("mySecretaire3"),
                                                "[0].medecinLogin", is("myDoc3"),
                                                "[0].titre", is("Titre du CR3"),
                                                "[0].corp",
                                                is("En effet, nous notons la présence de corps tumorale sur la partie inferieur du ganglion gauche3"),
                                                "[0].signature", is("myDocSignature3"),
                                                "[0].etat", is("NULL"));


        }

}
