package com.news.feed.newsfeed.service;

import com.news.feed.newsfeed.entity.News;
import com.news.feed.newsfeed.entity.NewsJob;
import com.news.feed.newsfeed.remote.bing.MicrosoftBingClient;
import com.news.feed.newsfeed.remote.bing.model.BingPayload;
import com.news.feed.newsfeed.remote.bing.model.BingSearchResponse;
import com.news.feed.newsfeed.repository.NewsRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service(value = "bingNewsService")
public class BingNewsServiceImpl implements NewsService {

    @Value("${news-job.bing.subscription-id}")
    private String subscriptionId;
    @Value("${news-job.bing.max-records-fetch}")
    private int count;

    @Autowired
    private MicrosoftBingClient microsoftBingClient;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UtilityService utilityService;

    @Override
    public void retrieveAndSaveNews(NewsJob newsJob) {
        try {
            BingSearchResponse response =  microsoftBingClient.getBingNewsSearch(subscriptionId, newsJob.getSearchKeyword(),
                    DATE, newsJob.getJobLastRunTime().toEpochSecond(),count, 0);
            log.info("Number of Records Fetched: {}",response.getTotalEstimatedMatches());
            processNews(response, newsJob);
        } catch (FeignException exception) {
            log.error("Error occurred in retrieveAndSaveNews ", exception);
        } catch (RuntimeException exception) {
            log.error("Runtime Exception in retrieveAndSaveNews ", exception);
        }

    }

    private void processNews(BingSearchResponse bingSearchResponse, NewsJob newsJob) {
        List<BingPayload> payloads = bingSearchResponse.getValue();
        if(!payloads.isEmpty()) {
            for(BingPayload payload: payloads) {
                var news = new News();
                news.setNewsDescription(payload.getDescription());
                String source = !payload.getProvider().isEmpty()? payload.getProvider().get(0).getName(): "";
                news.setSource(source);
                news.setTitle(payload.getName());
                news.setTitleHash(utilityService.generateHash(payload.getName()));
                news.setUrl(payload.getUrl());
                news.setDatePublished(OffsetDateTime.parse(payload.getDatePublished(), DateTimeFormatter.ISO_DATE_TIME));
                news.setStockName(newsJob.getStockName());
                news.setCreatedDateTime(OffsetDateTime.now());
                news.setLastUpdateTime(OffsetDateTime.now());
                saveNews(news);
            }
        }

    }
    private void saveNews(News news) {
        try {
            newsRepository.save(news);
        } catch (RuntimeException ex) {
            log.error("Exception occurred", ex);
        }
    }
    @Override
    public boolean isApplicable(String source) {
        return BING.equalsIgnoreCase(source);
    }
}
