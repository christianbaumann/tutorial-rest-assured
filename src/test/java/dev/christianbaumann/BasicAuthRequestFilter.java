package dev.christianbaumann;

import com.github.tomakehurst.wiremock.extension.requestfilter.RequestFilterAction;
import com.github.tomakehurst.wiremock.extension.requestfilter.StubRequestFilter;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

public class BasicAuthRequestFilter extends StubRequestFilter {

    /***
     * Implement this request filter so that it inspects the
     * 'Authorization' header in the request.
     * - If the header value (you can retrieve this using
     *   request.header("Authorization").firstValue() is equal to
     *   'Basic dXNlcm5hbWU6cGFzc3dvcmQ=', the request should
     *   be processed normally
     * - In all other cases, we should filter out the request and
     *   return an HTTP 401 Unauthorized. You can use the notAuthorised()
     *   method in the ResponseDefinition class for this.
     */

    @Override
    public RequestFilterAction filter(Request request) {
        if (request.header("Authorization").firstValue().equals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=")) {
            return RequestFilterAction.continueWith(request);
        }

        return RequestFilterAction.stopWith(ResponseDefinition.notAuthorised());
    }

    @Override
    public String getName() {
        return "basic-auth";
    }
}