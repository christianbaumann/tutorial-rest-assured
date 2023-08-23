package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dev.christianbaumann.entities.pojo.plain.Address;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void captureAndReuseBookingId() {

        Integer bookingId =
            given().
                queryParam("checkin", "2023-11-13").
            when().
                get("http://localhost:9876/booking").
            then().
                extract().
                path("bookingId");

        given().
            pathParam("bookingId", bookingId).
        when().
            get("http://localhost:9876/booking/{bookingId}").
        then().
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

    private ResponseSpecification responseSpec;

    @BeforeEach
    void createResponseSpec() {
        responseSpec = new ResponseSpecBuilder().
            expectStatusCode(200).
            expectContentType(ContentType.JSON).
            build();
    }

    @Test
    void useResponseSpec() {

        given().
            get("http://localhost:9876/booking/1").
        then().
            spec(responseSpec);
    }

    @Test
    void serializeAddressToJson() {

        Address myAddress = new Address("1 Bagshot Row", "123", "Hobbiton", "The Shire");

        given().
            body(myAddress).
            log().body().
        when().
            post("http://localhost:9876/address").
        then().
            assertThat().
            statusCode(201);
    }

    @Test
    void deserializeJsonToAddress() {

        Address myAddress =

            given().
            when().
                get("http://localhost:9876/address/1").
                as(Address.class);

        assertEquals("1 Bagshot Row", myAddress.getStreet());
        assertEquals("123", myAddress.getPostalCode());
        assertEquals("Hobbiton", myAddress.getCity());
        assertEquals("The Shire", myAddress.getCountry());
    }

}
