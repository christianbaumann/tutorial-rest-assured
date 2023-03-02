package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @Test
    void usePathParameters() {

        given().
            pathParam("bookingId", 1).
        when().
            get("http://localhost:9876/booking/{bookingId}").
        then().
            assertThat().
            body("firstname", equalTo("Frodo")).
            body("lastname", equalTo("Baggins"));
    }

    @Test
    void useQueryParameters() {

        given().
            queryParam("checkin", "2023-11-13").
        when().
            get("http://localhost:9876/booking").
        then().
            assertThat().
            body("bookingdates.checkin", equalTo("2023-11-13"));
    }

    @ParameterizedTest
    @CsvSource({
            "1, Frodo, Baggins",
            "2, Gandalf, The White",
            "3, Aragorn, Elessar"
    })
    void aDataDrivenTest(int bookingId, String firstName, String lastname){

        given().
            pathParam("bookingId", bookingId).
        when().
            get("http://localhost:9876/booking/{bookingId}").
        then().
            assertThat().
            body("firstname", equalTo(firstName)).
            body("lastname", equalTo(lastname));
    }

    @Test
    void useBasicAuthentication() {

        // TODO add response for not matching template, see: https://stackoverflow.com/questions/75614289/wiremock-json-template-basicauth-and-doesnotmatch-not-working

        given().
            auth().
            preemptive().
            basic("username", "password").
        when().
            get("http://localhost:9876/basicAuth").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    void useOauthAuthentication() {

        // build JSON response template

        given().
            auth().
            oauth2("myAuthentiactionToken").
        when().
            get("http://localhost:9876/oAuth").
        then().
            assertThat().
            statusCode(200);
    }

    private RequestSpecification requestSpec;

    @BeforeEach
    void createRequestSpec() {
        requestSpec = new RequestSpecBuilder().
            setBaseUri("http://localhost").
            setPort(9876).
            build();
    }

    @Test
    void useRequestSpec() {

        given().
            spec(requestSpec).
        when().
            get("/booking/1").
        then().
            assertThat().
            statusCode(200);
    }

}
