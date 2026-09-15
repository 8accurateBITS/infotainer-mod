package com.infotainer.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infotainer.config.ModConfig;

public class TwitchApiClient {
    private static final String TWITCH_API_URL = "https://api.twitch.tv/helix";
    private static final String TWITCH_CLIENT_ID = "YOUR_CLIENT_ID_HERE"; // Set this to your app's client ID
    private static HttpClient httpClient;
    private static long lastFollowerCount = 0;
    private static long lastUpdateTime = 0;

    public static void initialize() {
        httpClient = HttpClient.newHttpClient();
    }

    public static long getFollowerCount(String channelName) {
        ModConfig.ConfigData config = ModConfig.get();
        
        // Check if we should update (based on updateInterval)
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime < config.updateInterval * 1000) {
            return lastFollowerCount;
        }
        lastUpdateTime = now;

        if (channelName.isEmpty() || config.twitchOAuthToken.isEmpty()) {
            return -1;
        }

        try {
            // Get user ID first
            String userId = getUserId(channelName);
            if (userId == null) {
                return -1;
            }

            // Get follower count
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TWITCH_API_URL + "/channels/followers?broadcaster_id=" + userId))
                .header("Authorization", "Bearer " + config.twitchOAuthToken)
                .header("Client-ID", TWITCH_CLIENT_ID)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                lastFollowerCount = json.get("total").getAsLong();
                return lastFollowerCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static String getUserId(String channelName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TWITCH_API_URL + "/users?login=" + channelName))
                .header("Authorization", "Bearer " + ModConfig.get().twitchOAuthToken)
                .header("Client-ID", TWITCH_CLIENT_ID)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                if (json.has("data") && json.getAsJsonArray("data").size() > 0) {
                    return json.getAsJsonArray("data").get(0).getAsJsonObject().get("id").getAsString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
