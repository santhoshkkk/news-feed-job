package com.news.feed.newsfeed.remote.bing.model;

import lombok.Data;

import java.util.List;

@Data
public class BingPayload {
    private String name;
    private String url;
    private String description;
    private String datePublished;
    private List<Provider> provider;

}
