package cnam.medical.pacs;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cnam.medical.pacs.domain.dao.DocumentRepo;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

@QuarkusTest
public class DocumentTest {

        // Declarations
        String myDirectory = System.getProperty("user.dir") + "/src/main/resources/TestResources/";
        String JPEG = "CNI.jpeg";
        String PDF = "PetstoreToCloudPres_2022.pdf";
        String txt = "Texte.txt";        Integer myLong;

        @Inject
        DocumentRepo myRepo;

        @BeforeEach
        @Transactional
        void init() throws IOException {

                myRepo.deleteAll();

                // Encode base64
                File myFile = new File(myDirectory + "/" + JPEG);
                byte[] myBytes = Base64.getEncoder().encode(Files.readAllBytes(myFile.toPath()));
                String s = new String(myBytes);

                // Prepare Json request
                JsonObject patient1 = new JsonObject();
                patient1.put("id", 1);
                JsonObject requestParams = new JsonObject();
                requestParams.put("typeDocument", "Identity").put("nameFile", "CNI.jpeg")
                                .put("localedate", "2022-07-24").put("myfileBase64", s)
                                .put("patient", patient1);

                // Upload 1

                // POST 1 Patient
                given()
                                .body("{\"lastName\": \"Boufenara\", \"firstName\": \"Anouar\", \"birth\": \"1985-12-20\", \"sex\": \"MALE\"}")
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/patient")
                                .then()
                                .statusCode(201);

                // POST 1 Document
                given()
                                .body(requestParams.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/document")
                                .then()
                                .statusCode(201);

                List myList = given().when().get("/document").body().jsonPath().get("id");
                myLong = (Integer) myList.get(0);
               
               
                // POST 2
                myFile = new File(myDirectory + "/" + PDF);
                myBytes = Base64.getEncoder().encode(Files.readAllBytes(myFile.toPath()));
                s = new String(myBytes);

                requestParams = new JsonObject();
                requestParams.put("typeDocument", "Prescription").put("nameFile", "PetstoreToCloudPres_2022.pdf")
                                .put("localedate", "2021-07-24").put("myfileBase64", s)
                                .put("patient", patient1);

                given()
                                .body(requestParams.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/document")
                                .then()
                                .statusCode(201);

                // POST 3

                myFile = new File(myDirectory + "/" + txt);
                myBytes = Base64.getEncoder().encode(Files.readAllBytes(myFile.toPath()));
                s = new String(myBytes);

                requestParams = new JsonObject();
                requestParams.put("typeDocument", "Other").put("nameFile", "Texte.txt")
                                .put("localedate", "2020-07-24").put("myfileBase64", s)
                                .put("patient", patient1);

                given()
                                .body(requestParams.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .post("/document")
                                .then()
                                .statusCode(201);

        }

        @AfterEach
        @Transactional
        void tearDown() {

                myRepo.deleteAll();
        }

        @Test
        public void testGetDocument() {

                given()
                                .when().get("/document")
                                .then()
                                .statusCode(200)
                                .body("size()", is(3));

        }

        @Test
        public void testPostDocument() throws IOException {

                File myFile1 = new File(myDirectory + "/" + JPEG);
                byte[] myBytes1 = Base64.getEncoder().encode(Files.readAllBytes(myFile1.toPath()));
                String s1 = new String(myBytes1);

                File myFile2 = new File(myDirectory + "/" + PDF);
                byte[] myBytes2 = Base64.getEncoder().encode(Files.readAllBytes(myFile2.toPath()));
                String s2 = new String(myBytes2);

                File myFile3 = new File(myDirectory + "/" + this.txt);
                byte[] myBytes3 = Base64.getEncoder().encode(Files.readAllBytes(myFile3.toPath()));
                String s3 = new String(myBytes3);

                given()
                                .when().get("/document")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3),
                                                "[0].typeDocument", is("Identity"),
                                                "[0].nameFile", is("CNI.jpeg"),
                                                "[0].localedate", is("2022-07-24"),
                                                "[0].myfileBase64", is(s1),
                                                "[0].patient.id", is(1),
                                                "[1].typeDocument", is("Prescription"),
                                                "[1].nameFile", is("PetstoreToCloudPres_2022.pdf"),
                                                "[1].localedate", is("2021-07-24"),
                                                "[1].myfileBase64", is(s2),
                                                "[1].patient.id", is(1),
                                                "[2].typeDocument", is("Other"),
                                                "[2].nameFile", is("Texte.txt"),
                                                "[2].localedate", is("2020-07-24"),
                                                "[2].myfileBase64", is(s3),
                                                "[2].patient.id", is(1)

                                );

        }

        @Test
        public void testgetDocumentById() {

                given()
                                .when().get("/document/id/" + myLong)
                                .then()
                                .statusCode(200)
                                .body("id", is(myLong),
                                                "typeDocument", is("Identity"),
                                                "nameFile", is("CNI.jpeg"),
                                                "localedate", is("2022-07-24"),
                                                "patient.id", is(1));
        }

        @Test
        public void testgetDocumentByPatientID() {

                given()
                                .when().get("/document/patient/1")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(3));

        }

        @Test
        public void testgetDocumentByType() {

                given()
                                .when().get("/document/type/Identity")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(1),
                                                "[0].typeDocument", is("Identity"),
                                                "[0].nameFile", is("CNI.jpeg"),
                                                "[0].localedate", is("2022-07-24"),
                                                "[0].patient.id", is(1));

        }

        @Test
        public void testgetDocumentByDate() {

                given()
                                .when().get("/document/date/2022-07-24")
                                .then()
                                .statusCode(200)
                                .body("$.size()", is(1),
                                                "[0].typeDocument", is("Identity"),
                                                "[0].nameFile", is("CNI.jpeg"),
                                                "[0].localedate", is("2022-07-24"),
                                                "[0].patient.id", is(1));

        }

        @Test
        public void testUpdateDocument() throws IOException {

                File myFile = new File(myDirectory + "/" + JPEG);
                byte[] myBytes = Base64.getEncoder().encode(Files.readAllBytes(myFile.toPath()));
                String s = new String(myBytes);

                JsonObject patient1 = new JsonObject();
                patient1.put("id", 1);
                JsonObject requestParams = new JsonObject();
                requestParams.put("typeDocument", "Identity").put("nameFile", "CNINEW.jpeg")
                                .put("localedate", "2021-07-24").put("myfileBase64", s)
                                .put("patient", patient1);

                // POST 1 Document
                given()
                                .body(requestParams.toString())
                                .header("Content-Type", "application/json")
                                .when()
                                .put("/document/" + myLong)
                                .then()
                                .statusCode(200)
                                .body("id", is(myLong),
                                                "typeDocument", is("Identity"),
                                                "nameFile", is("CNINEW.jpeg"),
                                                "localedate", is("2021-07-24"),
                                                "patient.id", is(1));

        }

        @Test
        public void testDeleteDocument() {

                given()

                                .header("Content-Type", "application/json")
                                .when()
                                .delete("/document/" + myLong)
                                .then()
                                .statusCode(200);

                given()
                                .when().get("/document")
                                .then()
                                .statusCode(200)
                                .body("size()", is(2));

        }
}
