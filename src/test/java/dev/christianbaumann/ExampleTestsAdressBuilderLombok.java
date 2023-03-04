package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import dev.christianbaumann.entities.lombok.builder.Address;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@WireMockTest(httpPort = 9876)
public class ExampleTestsAdressBuilderLombok {

    @Test
    void adressBuilderStandardAddress() {

        private String street = "1 Bagshot Row";
        private String postalCode = "123";
        private String city = "Hobbiton";
        private String country = "The Shire";

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

//    @Test
//    void adressBuilderCustomizedAddress() {
//
//        Address myAddress = new AddressBuilder().
//            street("Smithy").
//            postalCode("666").
//            city("Lounely Mountain").
//            country("Mordor").
//            build();
//
//        given().
//            body(myAddress).
//            log().body().
//        when().
//            post("http://localhost:9876/address").
//        then().
//            assertThat().
//            statusCode(201);
//    }
}
