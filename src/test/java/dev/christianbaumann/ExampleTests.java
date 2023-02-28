package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@WireMockTest(httpPort = 9876)
public class ExampleTests {

    @Test
    void aRestAssuredTest() {

        given().
            get("http://localhost:9876/booking/1").
        then().
            statusCode(200).
            body("firstname", equalTo("Frodo")).
            body("lastname", equalTo("Baggins"));
    }

    @Test
    void logRequest() {

        given().
            log().all().
        when().
            get("http://localhost:9876/booking/1");
    }

    @Test
    void logResponse() {

        given().
            get("http://localhost:9876/booking/1").
        then().
            log().all();
    }

}
