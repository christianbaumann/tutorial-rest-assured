package dev.christianbaumann;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import dev.christianbaumann.extensions.AuthRequestFilter;
import dev.christianbaumann.extensions.BasicAuthRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;

public class ExampleTestsAuth {

    @RegisterExtension
    static WireMockExtension wiremockBasicAuth = WireMockExtension.newInstance().
        options(wireMockConfig().
            port(9876).
            extensions(new BasicAuthRequestFilter())
        ).build();

    @RegisterExtension
    static WireMockExtension wiremockAuth = WireMockExtension.newInstance().
        options(wireMockConfig().
            port(9878).
            extensions(new AuthRequestFilter("oAuth"))
        ).build();

    @BeforeEach
    public void stubForBasicAuth() {
        wiremockBasicAuth.stubFor(get(urlEqualTo("/basicAuth"))
            .willReturn(aResponse()
                .withStatus(200)
            ));
    }

    @BeforeEach
    public void stubForOauth() {
        wiremockAuth.stubFor(get(urlEqualTo("/oAuth"))
            .willReturn(aResponse()
                .withStatus(200)
            ));
    }

    @Test
    void useBasicAuthentication() {

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
    void useBasicAuthenticationWithWrongPassword() {

        given().
            auth().
            preemptive().
            basic("username", "password-incorrect").
        when().
            get("http://localhost:9876/basicAuth").
        then().
            assertThat().
            statusCode(401);
    }

    @Test
    void useOauthAuthentication() {

        given().
            auth().
            oauth2("myAuthentiactionToken").
        when().
            get("http://localhost:9878/oAuth").
        then().
            assertThat().
            statusCode(200);
    }

    @Test
    void useOauthAuthenticationWithWrongToken() {

        given().
            auth().
            oauth2("notMyAuthentiactionToken").
        when().
            get("http://localhost:9878/oAuth").
        then().
            assertThat().
            statusCode(401);
    }

    @Test
    void useOauthAuthentication_AuthRequestFilter() {

        given().
            auth().
            oauth2("myAuthentiactionToken123").
        when().
            get("http://localhost:9878/oAuth").
        then().
            assertThat().
            statusCode(401);
    }
}
