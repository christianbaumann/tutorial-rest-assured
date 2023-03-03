package dev.christianbaumann.extensions;

import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilter;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class OauthRequestFilter extends StubRequestFilter {

    @Override
    public RequestFilterAction filter(Request request) {
        HttpHeader authorizationHeader = request.header("Authorization");
        if (authorizationHeader.firstValue().equals("Bearer myAuthentiactionToken")) {
            return RequestFilterAction.continueWith(request);
        }

        return RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
    }

    @Override
    public String getName() {
        return "oauth";
    }
}
