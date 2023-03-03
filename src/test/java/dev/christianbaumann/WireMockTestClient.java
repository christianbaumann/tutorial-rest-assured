/*
 * Copyright (C) 2011-2021 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.christianbaumann;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.config.CharCodingConfig;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.net.URI;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WireMockTestClient {

    private static final String LOCAL_WIREMOCK_ROOT = "http://%s:%d%s";

    private int port;
    private String address;

    public WireMockTestClient(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public WireMockTestClient(int port) {
        this(port, "localhost");
    }

    private String mockServiceUrlFor(String path) {
        return String.format(LOCAL_WIREMOCK_ROOT, address, port, path);
    }

    public WireMockResponse get(String url, TestHttpHeader... headers) {
        String actualUrl = URI.create(url).isAbsolute() ? url : mockServiceUrlFor(url);
        HttpUriRequest httpRequest = new HttpGet(actualUrl);
        return executeMethodAndConvertExceptions(httpRequest, headers);
    }

    private WireMockResponse executeMethodAndConvertExceptions(
            HttpUriRequest httpRequest, TestHttpHeader... headers) {
        try {
            for (TestHttpHeader header : headers) {
                httpRequest.addHeader(header.getName(), header.getValue());
            }
            ClassicHttpResponse httpResponse = httpClient().execute(httpRequest);
            return new WireMockResponse(httpResponse);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public WireMockResponse request(final String methodName, String url, TestHttpHeader... headers) {
        HttpUriRequest httpRequest =
                new HttpUriRequestBase(methodName, URI.create(mockServiceUrlFor(url)));
        return executeMethodAndConvertExceptions(httpRequest, headers);
    }

    public WireMockResponse request(final String methodName, String url, String body, TestHttpHeader... headers) {
        HttpUriRequest httpRequest =
                new HttpUriRequestBase(methodName, URI.create(mockServiceUrlFor(url)));
        httpRequest.setEntity(new StringEntity(body));
        return executeMethodAndConvertExceptions(httpRequest, headers);
    }

    private static CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .disableAuthCaching()
                .disableAutomaticRetries()
                .disableCookieManagement()
                .disableRedirectHandling()
                .disableContentCompression()
                .setConnectionManager(
                        PoolingHttpClientConnectionManagerBuilder.create()
                                .setConnectionFactory(
                                        new ManagedHttpClientConnectionFactory(
                                                null, CharCodingConfig.custom().setCharset(UTF_8).build(), null))
                                .build())
                .build();
    }
}
