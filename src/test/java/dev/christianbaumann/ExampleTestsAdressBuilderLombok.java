package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dev.christianbaumann.entities.lombok.builder.Address;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@WireMockTest(httpPort = 9876)
public class ExampleTestsAdressBuilderLombok {

    @Test
    void adressBuilderStandardAddress() {

        Address standardAddress = Address.builder().
            street("Smithy").
            postalCode("666").
            city("Lounely Mountain").
            country("Mordor").
            build();

        given().
            body(standardAddress).
            log().body().
        when().
            post("http://localhost:9876/address").
        then().
            assertThat().
            statusCode(201);
    }

    @Test
    void adressBuilderCustomizedAddress() {

        Address standardAddress = Address.builder().
            street("Smithy").
            postalCode("666").
            city("Lounely Mountain").
            country("Mordor").
            build();

        Address.AddressBuilder addressBuilder = standardAddress.toBuilder();

        Address newAddress = addressBuilder.
                street("1 Bagshot Row").
                postalCode("123").
                city("Hobbiton").
                country("The Shire").
                build();

        given().
            body(newAddress).
            log().body().
        when().
            post("http://localhost:9876/address").
        then().
            assertThat().
            statusCode(201);
    }
}
