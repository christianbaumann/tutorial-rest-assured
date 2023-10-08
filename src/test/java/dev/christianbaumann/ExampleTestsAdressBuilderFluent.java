package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dev.christianbaumann.entities.pojo.builder.fluent.AddressBuilder;
import dev.christianbaumann.entities.pojo.plain.Address;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@WireMockTest(httpPort = 9876)
public class ExampleTestsAdressBuilderFluent {

    @Test
    void adressBuilderStandardAddress() {

        Address myAddress = new AddressBuilder().build();

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
    void adressBuilderCustomizedAddress() {

        Address myAddress = new AddressBuilder().
                withStreet("Smithy").
                withPostalCode("666").
                withCity("Lounely Mountain").
                withCountry("Mordor").
            build();

        given().
            body(myAddress).
            log().body().
        when().
            post("http://localhost:9876/address").
        then().
            assertThat().
            statusCode(201);
    }
}
