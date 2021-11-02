package quarkus.world.tour;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@QuarkusTest
class RockBandResourceTest {

    @Test
    void theBandsAreInThePlace() {
        final Response response = given()
                .when().get("/rock")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().response();
        final List<String> names = response.jsonPath().getList("name");
        assertThat("The bands are in the place!", names,
                hasItems("AC/DC", "The Beatles", "Guns N Roses"));
    }

    @Test
    void aliveAreAlive() {
        final Response response = given()
                .when().get("/rock/alive")
                .then()
                .statusCode(200)
                .extract().response();
        final List<String> names = response.jsonPath().getList("name");
        assertThat("Alive are alive", names, hasItems("AC/DC", "Guns N Roses"));
        assertThat("Dead are rip", names, not(hasItems("The Beatles")));
    }

    @Test
    void myFavoriteBand() {
        given()
                .when().get("/rock/2")
                .then()
                .statusCode(200)
                .body("name", is("Guns N Roses"));
    }

    @Test
    void is404ABand() {
        given()
                .when().get("/rock/10")
                .then()
                .body(containsString("no band found with id: 10"))
                .statusCode(404);
    }

    @Test
    void mayWeHaveANewBand() {
        final Band b = new Band();
        b.name = "Red Hot Chili Peppers";
        b.alive = true;
        b.creationYear = 1983;
        b.terminationYear = -1;
        final String path = given()
                .body(b)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when().post("/rock/")
                .then()
                .statusCode(201)
                .extract().header("location");
        given()
                .when().get(path)
                .then()
                .statusCode(200)
                .body("name", is("Red Hot Chili Peppers"))
                .body("alive", is(true))
                .body("creationYear", is(1983))
                .body("terminationYear", is(-1));

    }

}