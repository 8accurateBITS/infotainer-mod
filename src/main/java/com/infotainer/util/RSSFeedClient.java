package com.infotainer.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.infotainer.config.ModConfig;

public class RSSFeedClient {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static List<RSSItem> cachedItems = new ArrayList<>();
    private static long lastUpdateTime = 0;
    
    public static class RSSItem {
        public String title;
        public String description;
        public String link;
        public String pubDate;
        
        public RSSItem(String title, String description, String link, String pubDate) {
            this.title = title;
            this.description = description;
            this.link = link;
            this.pubDate = pubDate;
        }
    }
    
    public static List<RSSItem> fetchFeed(String feedUrl) {
        ModConfig.ConfigData config = ModConfig.get();
        
        long now = System.currentTimeMillis();
        if (now - lastUpdateTime < config.rssUpdateInterval * 1000 && !cachedItems.isEmpty()) {
            return cachedItems;
        }
        lastUpdateTime = now;
        
        if (feedUrl == null || feedUrl.isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(feedUrl))
                .timeout(java.time.Duration.ofSeconds(10))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                cachedItems = parseFeed(response.body());
                return cachedItems;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new ArrayList<>();
    }
    
    private static List<RSSItem> parseFeed(String xmlContent) {
        List<RSSItem> items = new ArrayList<>();
        
        // Extract items from RSS feed
        Pattern itemPattern = Pattern.compile("<item>(.*?)</item>", Pattern.DOTALL);
        Matcher itemMatcher = itemPattern.matcher(xmlContent);
        
        while (itemMatcher.find()) {
            String itemContent = itemMatcher.group(1);
            
            String title = extractTag(itemContent, "title");
            String description = extractTag(itemContent, "description");
            String link = extractTag(itemContent, "link");
            String pubDate = extractTag(itemContent, "pubDate");
            
            if (!title.isEmpty()) {
                // Strip HTML tags from description
                description = description.replaceAll("<[^>]*>", "");
                if (description.length() > 100) {
                    description = description.substring(0, 100) + "...";
                }
                items.add(new RSSItem(title, description, link, pubDate));
            }
        }
        
        return items;
    }
    
    private static String extractTag(String content, String tagName) {
        Pattern pattern = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}
