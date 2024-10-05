package de.aimless.linkedroles.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class HttpClientWrapper {

    private final ObjectMapper objectMapper;

    public HttpClientWrapper() {
        this.objectMapper = new ObjectMapper();
    }

    private Request buildRequest(String url, String accessToken, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .method(Objects.nonNull(requestBody) ? "PUT" : "GET", requestBody)
                .build();
    }

    public Response makePutRequestWithAuthHeader(String url, String accessToken, Map<String, Object> bodyMap) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String body = objectMapper.writeValueAsString(bodyMap);
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));

        Request request = buildRequest(url, accessToken, requestBody);

        return client.newCall(request).execute();
    }

    public Response makeGetRequestWithAuthHeader(String url, String accessToken) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = buildRequest(url, accessToken, null);

        return client.newCall(request).execute();
    }
}
