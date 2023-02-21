package com.news.feed.newsfeed.schduler;

import com.news.feed.newsfeed.entity.NewsJob;
import com.news.feed.newsfeed.repository.NewsJobRepository;
import com.news.feed.newsfeed.scheduler.NewsJobScheduler;
import com.news.feed.newsfeed.service.BingNewsServiceImpl;
import com.news.feed.newsfeed.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsJobScheduleTest {

    private NewsJobScheduler newsJobScheduler;

    @Mock
    private NewsJobRepository newsJobRepository;

    @Mock
    private List<NewsService> newsServices;

    @Mock
    private BingNewsServiceImpl bingNewsService;

    @BeforeEach
    void configure() {
        newsServices = new ArrayList<>();
        newsServices.add(bingNewsService);
        newsJobScheduler = new NewsJobScheduler(newsServices, newsJobRepository,60);
    }

    @Test
    void should_run_scheduler_when_scheduled() {
        NewsJob newsJob = new NewsJob();
        newsJob.setSource("bing");
        List<NewsJob> newsJobs = new ArrayList<>();
        newsJobs.add(newsJob);
        when(newsJobRepository.findByJobNextRunTimeLessThanEqual(any())).thenReturn(newsJobs);
        when(newsJobRepository.save(any())).thenReturn(new NewsJob());
        when(bingNewsService.isApplicable(anyString())).thenReturn(true);
        doNothing().when(bingNewsService).retrieveAndSaveNews(any());
        newsJobScheduler.runNewsJobScheduler();
        verify(newsJobRepository).findByJobNextRunTimeLessThanEqual(any());
        verify(newsJobRepository).save(any());
        verify(bingNewsService).isApplicable(anyString());
        verify(bingNewsService).retrieveAndSaveNews(any());
    }
}
