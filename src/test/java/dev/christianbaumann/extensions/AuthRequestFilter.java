package dev.christianbaumann.extensions;

import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilter;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class AuthRequestFilter extends StubRequestFilter {

    private String name;

    public AuthRequestFilter(String name) {
        this.name = name;
    }

    @Override
    public RequestFilterAction filter(Request request) {
        RequestFilterAction requestFilterAction;

        HttpHeader authHeader = request.header("Authorization");
        String authHeaderValue = authHeader.firstValue();

        switch (authHeaderValue) {
            case "Bearer myAuthentiactionToken":
            case "Basic dXNlcm5hbWU6cGFzc3dvcmQ=":
                requestFilterAction = RequestFilterAction.continueWith(request);
                break;
            default:
                requestFilterAction = RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
        }

        return requestFilterAction;
    }

    @Override
    public String getName() {
        return name;
    }
}
