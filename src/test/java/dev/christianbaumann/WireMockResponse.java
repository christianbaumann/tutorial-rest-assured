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

import org.apache.hc.core5.http.ClassicHttpResponse;

import static com.github.tomakehurst.wiremock.common.HttpClientUtils.getEntityAsByteArrayAndCloseStream;

public class WireMockResponse {

    private final ClassicHttpResponse httpResponse;
    private final byte[] content;

    public WireMockResponse(ClassicHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        content = getEntityAsByteArrayAndCloseStream(httpResponse);
    }

    public int statusCode() {
        return httpResponse.getCode();
    }
}
