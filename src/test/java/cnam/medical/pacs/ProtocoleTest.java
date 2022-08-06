package cnam.medical.pacs;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cnam.medical.pacs.domain.dao.CompteRenduRepo;
import cnam.medical.pacs.domain.dao.ExamenRepo;
import cnam.medical.pacs.domain.dao.ProtocoleRepo;
import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

@QuarkusTest
public class ProtocoleTest {

    Integer myLong;

    @Inject
    ProtocoleRepo myRepo;

    @Inject
    ExamenRepo myExamenRepo;

    @Inject
    CompteRenduRepo myCRRepo;

    @BeforeEach
    @Transactional
    void init() {


        myCRRepo.deleteAll();
        myExamenRepo.deleteAll();
        myRepo.deleteAll();

        // POST1
        given()
                .body("{\"technique\": \"SCANNER\", \"membre\": \"CRANE\", \"type\": \"MOU\", \"injection\": \"OUI\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/protocole")
                .then()
                .statusCode(201);

        List myList = given().when().get("/protocole").body().jsonPath().get("id");
        myLong = (Integer) myList.get(0);

        // POST2
        given()
                .body("{\"technique\": \"RADIO\", \"membre\": \"TRONC\", \"type\": \"OS\", \"injection\": \"NON\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/protocole")
                .then()
                .statusCode(201);

        // POST3
        given()
                .body("{\"technique\": \"MEDECINE_NUCLEAIRE\", \"membre\": \"BRAS\", \"type\": \"MOU\", \"injection\": \"OUI\"}")
                .header("Content-Type", "application/json")
                .when()
                .post("/protocole")
                .then()
                .statusCode(201);

    }

    @AfterEach
    @Transactional
    void tearDown() {


        myCRRepo.deleteAll();
        myExamenRepo.deleteAll();
        myRepo.deleteAll();
    }

    @Test
    public void testGetProtocoles() {

        given()
                .when().get("/protocole")
                .then()
                .statusCode(200)
                .body("size()", is(3));

    }

    @Test
    public void testPostProtocole() {

        given()
                .when().get("/protocole")
                .then()
                .statusCode(200).body("$.size()", is(3),
                        "[0].technique", is("SCANNER"),
                        "[0].membre", is("CRANE"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"),
                        "[1].technique", is("RADIO"),
                        "[1].membre", is("TRONC"),
                        "[1].type", is("OS"),
                        "[1].injection", is("NON"),
                        "[2].technique", is("MEDECINE_NUCLEAIRE"),
                        "[2].membre", is("BRAS"),
                        "[2].type", is("MOU"),
                        "[2].injection", is("OUI"));
    }

    @Test
    public void testPutPatient() {

        // VERIFICATION
        given()
                .when().get("/protocole")
                .then()
                .statusCode(200).body("$.size()", is(3),
                        "[0].technique", is("SCANNER"),
                        "[0].membre", is("CRANE"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"),
                        "[1].technique", is("RADIO"),
                        "[1].membre", is("TRONC"),
                        "[1].type", is("OS"),
                        "[1].injection", is("NON"),
                        "[2].technique", is("MEDECINE_NUCLEAIRE"),
                        "[2].membre", is("BRAS"),
                        "[2].type", is("MOU"),
                        "[2].injection", is("OUI"));

        // PUT
        given()
                .body("{\"id\": " + myLong
                        + " , \"technique\": \"RADIO\", \"membre\": \"TRONC\", \"type\": \"OS\", \"injection\": \"NON\", \"date\": \"2022-07-19T07:41:06.491Z[UTC]\"}")
                .header("Content-Type", "application/json")
                .when()
                .put("/protocole/" + myLong)
                .then()
                .statusCode(200);

        // VERIFICATION2
        given()
                .when().get("/protocole")
                .then()
                .statusCode(200).body("$.size()", is(3),
                        "[0].technique", is("RADIO"),
                        "[0].membre", is("TRONC"),
                        "[0].type", is("OS"),
                        "[0].injection", is("NON"),
                        "[1].technique", is("RADIO"),
                        "[1].membre", is("TRONC"),
                        "[1].type", is("OS"),
                        "[1].injection", is("NON"),
                        "[2].technique", is("MEDECINE_NUCLEAIRE"),
                        "[2].membre", is("BRAS"),
                        "[2].type", is("MOU"),
                        "[2].injection", is("OUI"));
    }

    @Test
    public void testDeleteProtocole() {

        given()
                .when().get("/protocole")
                .then()
                .statusCode(200).body("$.size()", is(3),
                        "[0].technique", is("SCANNER"),
                        "[0].membre", is("CRANE"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"),
                        "[1].technique", is("RADIO"),
                        "[1].membre", is("TRONC"),
                        "[1].type", is("OS"),
                        "[1].injection", is("NON"),
                        "[2].technique", is("MEDECINE_NUCLEAIRE"),
                        "[2].membre", is("BRAS"),
                        "[2].type", is("MOU"),
                        "[2].injection", is("OUI"));

        given().when().delete("/protocole/" + myLong).then().statusCode(200);

        given()
                .when().get("/protocole")
                .then()
                .statusCode(200)
                .body("size()", is(2),
                        "[0].technique", is("RADIO"),
                        "[0].membre", is("TRONC"),
                        "[0].type", is("OS"),
                        "[0].injection", is("NON"),
                        "[1].technique", is("MEDECINE_NUCLEAIRE"),
                        "[1].membre", is("BRAS"),
                        "[1].type", is("MOU"),
                        "[1].injection", is("OUI"));

    }

    @Test
    public void testGetProtocoleById() {

        given()
                .when().get("/protocole/id/" + myLong)
                .then()
                .statusCode(200)
                .body("id", is(myLong),
                        "technique", is("SCANNER"),
                        "membre", is("CRANE"),
                        "type", is("MOU"),
                        "injection", is("OUI"));
    }

    @Test
    public void testGetProtocoleByTechnique() {

        given()
                .when().get("/protocole/technique/RADIO")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].technique", is("RADIO"),
                        "[0].membre", is("TRONC"),
                        "[0].type", is("OS"),
                        "[0].injection", is("NON"));

        given()
                .when().get("/protocole/technique/MEDECINE_NUCLEAIRE")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].technique", is("MEDECINE_NUCLEAIRE"),
                        "[0].membre", is("BRAS"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"));
    }

    @Test
    public void testGetProtocoleByMembre() {

        given()
                .when().get("/protocole/membre/CRANE")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].technique", is("SCANNER"),
                        "[0].membre", is("CRANE"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"));

        given()
                .when().get("/protocole/membre/BRAS")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].technique", is("MEDECINE_NUCLEAIRE"),
                        "[0].membre", is("BRAS"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"));

    }

    @Test
    public void testGetProtocoleByType() {

        given()
                .when().get("/protocole/type/MOU")
                .then()
                .statusCode(200)
                .body("size()", is(2),
                        "[0].technique", is("SCANNER"),
                        "[0].membre", is("CRANE"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"));

        given()
                .when().get("/protocole/type/OS")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].technique", is("RADIO"),
                        "[0].membre", is("TRONC"),
                        "[0].type", is("OS"),
                        "[0].injection", is("NON"));

    }

    @Test
    public void testGetProtocoleByInjection() {

        given()
                .when().get("/protocole/injection/OUI")
                .then()
                .statusCode(200)
                .body("size()", is(2),
                        "[0].technique", is("SCANNER"),
                        "[0].membre", is("CRANE"),
                        "[0].type", is("MOU"),
                        "[0].injection", is("OUI"));

        given()
                .when().get("/protocole/injection/NON")
                .then()
                .statusCode(200)
                .body("size()", is(1),
                        "[0].technique", is("RADIO"),
                        "[0].membre", is("TRONC"),
                        "[0].type", is("OS"),
                        "[0].injection", is("NON"));
    }

}
