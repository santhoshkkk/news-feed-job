package com.news.feed.newsfeed.service;

import com.news.feed.newsfeed.entity.News;
import com.news.feed.newsfeed.entity.NewsJob;
import com.news.feed.newsfeed.remote.bing.MicrosoftBingClient;
import com.news.feed.newsfeed.remote.bing.model.BingPayload;
import com.news.feed.newsfeed.remote.bing.model.BingSearchResponse;
import com.news.feed.newsfeed.remote.bing.model.Provider;
import com.news.feed.newsfeed.repository.NewsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BingNewsServiceImplTest {

    @Mock
    private MicrosoftBingClient microsoftBingClient;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UtilityService utilityService;

    @InjectMocks
    private BingNewsServiceImpl bingNewsService;

    @Test
    void should_fetch_news_and_save_when_valid_source_is_passed() {
        NewsJob newsJob = new NewsJob();
        newsJob.setSource("bing");
        newsJob.setJobLastRunTime(OffsetDateTime.now());
        BingSearchResponse bingSearchResponse = buildBingSearchResponse();
        when(microsoftBingClient.getBingNewsSearch(any(),any(),anyString(), anyString(),anyInt(),anyInt())).thenReturn(bingSearchResponse);
        when(utilityService.generateHash(anyString())).thenReturn("test hash");
        when(newsRepository.save(any())).thenReturn(new News());
        bingNewsService.retrieveAndSaveNews(newsJob);
        verify(microsoftBingClient).getBingNewsSearch(any(),any(),anyString(), anyString(),anyInt(),anyInt());
        verify(newsRepository).save(any());
    }

    @Test
    void should_return_true_when_source_is_bing() {
        assertTrue(bingNewsService.isApplicable("bing"));
    }

    @Test
    void should_return_false_when_source_is_not_bing() {
        assertFalse(bingNewsService.isApplicable("google"));
    }
    private BingSearchResponse buildBingSearchResponse() {
        BingSearchResponse bingSearchResponse = new BingSearchResponse();
        bingSearchResponse.setTotalEstimatedMatches(1);
        List<BingPayload> value = new ArrayList<>();
        BingPayload bingPayload = new BingPayload();
        bingPayload.setName("Reliance Industries Ltd.");
        bingPayload.setUrl("https://www.ndtv.com/business/stock/reliance-industries-ltd_reliance");
        bingPayload.setDescription("Reliance Industries plans to invest an additional Rs 75,000 crores in the next four years across Uttar Pradesh.");
        bingPayload.setDatePublished("2023-02-15T07:55:00.0000000Z");
        List<Provider> providers = new ArrayList<>();
        Provider provider = new Provider();
        provider.setName("MSN");
        providers.add(provider);
        bingPayload.setProvider(providers);
        value.add(bingPayload);
        bingSearchResponse.setValue(value);
        return bingSearchResponse;
    }
}
