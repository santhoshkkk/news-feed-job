package com.news.feed.newsfeed.service;

import com.news.feed.newsfeed.entity.NewsJob;

public interface NewsService {
    String BING = "bing";
    String DATE = "date";
    String DAY = "Day";
    void retrieveAndSaveNews(NewsJob newsJob);

    boolean isApplicable(String source);
}
