package com.news.feed.newsfeed.remote.bing.model;

import lombok.Data;

import java.util.List;

@Data
public class BingSearchResponse {
    private int totalEstimatedMatches;
    private List<BingPayload> value;
}
