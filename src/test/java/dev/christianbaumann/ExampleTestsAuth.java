package dev.christianbaumann;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilter;
import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilter;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static dev.christianbaumann.TestHttpHeader.withHeader;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ExampleTestsAuth {

    private WireMockServer wm;
    private WireMockTestClient client;
    private String url;

    private void initialise(RequestFilter... filters) {
        wm = new WireMockServer(wireMockConfig().dynamicPort().extensions(filters));
        wm.start();
        client = new WireMockTestClient(wm.port());
    }

    public static class StubAuthenticatingFilter extends StubRequestFilter {

        @Override
        public RequestFilterAction filter(Request request) {
            HttpHeader authHeader = request.header("Authorization");
            if (!authHeader.isPresent() || !authHeader.firstValue().equals("Token 123")) {
                return RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
            }

            return RequestFilterAction.continueWith(request);
        }

        @Override
        public String getName() {
            return "stub-authenticator";
        }
    }

    @Test
    public void filterCanStopWithResponse() {
        initialise(new StubAuthenticatingFilter());
        url = "/hgiLo";

        wm.stubFor(get(url).willReturn(ok()));

        WireMockResponse good = client.get(url, withHeader("Authorization", "Token 123"));
        assertThat(good.statusCode(), is(200));

        WireMockResponse bad = client.get(url);
        assertThat(bad.statusCode(), is(401));
    }
}
