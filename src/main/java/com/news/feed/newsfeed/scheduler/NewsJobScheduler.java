package com.news.feed.newsfeed.scheduler;

import com.news.feed.newsfeed.entity.NewsJob;
import com.news.feed.newsfeed.repository.NewsJobRepository;
import com.news.feed.newsfeed.service.NewsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableScheduling
@Configuration
public class NewsJobScheduler {


    private List<NewsService> newsServices;
    private NewsJobRepository newsJobRepository;
    private int nextExecutionMinutes;

    public NewsJobScheduler(List<NewsService> newsServices,
                            NewsJobRepository newsJobRepository,
                            @Value("${news-job.next-execution.minutes:1440}") int nextExecutionMinutes) {
        this.newsServices = newsServices;
        this.newsJobRepository = newsJobRepository;
        this.nextExecutionMinutes = nextExecutionMinutes;
    }

    @Scheduled(fixedRateString = "${news-job.execution-interval.minutes:1440}", timeUnit = TimeUnit.MINUTES)
    public void runNewsJobScheduler() {
        log.info("News Job Scheduler Begin");

        List<NewsJob> newsJobs = newsJobRepository.findByJobNextRunTimeLessThanEqual(OffsetDateTime.now());
        log.info("Number of records : {}", newsJobs.size());
        for(NewsJob newsJob: newsJobs) {
            for(NewsService newsService: newsServices) {
                if(newsService.isApplicable(newsJob.getSource())) {
                    newsService.retrieveAndSaveNews(newsJob);
                    newsJob.setJobLastRunTime(OffsetDateTime.now());
                    newsJob.setLastUpdateTime(OffsetDateTime.now());
                    newsJob.setJobNextRunTime(OffsetDateTime.now().plusMinutes(nextExecutionMinutes));
                    newsJobRepository.save(newsJob);
                }
            }
        }
        log.info("News Job Scheduler End");
    }
}
