package de.aimless.aimless_java_bot.http;

import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RoleConnectionWrapper {

    private static final String BOT_NAME = "Yogiri System";
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleConnectionWrapper.class);
    private static final String DISCORD_API_URL = "https://discord.com/api/";
    private final HttpClientWrapper httpClientWrapper;

    public RoleConnectionWrapper() {
        this.httpClientWrapper = new HttpClientWrapper();
    }

    public DataObject getCurrentUserApplicationRoleConnection(String applicationId, String accessToken) {
        String url = DISCORD_API_URL + ExtendedSelfRoute.GET_CURRENT_USER_APPLICATION_ROLE_CONNECTION.compile(applicationId).getCompiledRoute();
        try (Response response = httpClientWrapper.makeGetRequestWithAuthHeader(url, accessToken)) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            ResponseBody body = response.body();
            if (body != null) {
                return DataObject.fromJson(body.string());
            }
        } catch (IOException e) {
            LOGGER.warn("Error while getting current user application role connection", e);
        }

        // Return an empty DataObject if the request fails
        return DataObject.empty();
    }

    public void updateCurrentUserApplicationRoleConnection(String applicationId, String accessToken, Map<String, Object> metadata) {
        String url = DISCORD_API_URL + ExtendedSelfRoute.UPDATE_CURRENT_USER_APPLICATION_ROLE_CONNECTION.compile(applicationId).getCompiledRoute();

        Map<String, Object> bodyMap = Map.of("platform_name", BOT_NAME, "metadata", metadata);
        try (Response response = httpClientWrapper.makePutRequestWithAuthHeader(url, accessToken, bodyMap)) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            LOGGER.warn("Error while updating role connection", e);
        }
    }
}
