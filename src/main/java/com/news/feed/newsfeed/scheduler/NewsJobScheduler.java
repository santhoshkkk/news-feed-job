package com.news.feed.newsfeed.scheduler;

import com.news.feed.newsfeed.entity.NewsJob;
import com.news.feed.newsfeed.repository.NewsJobRepository;
import com.news.feed.newsfeed.service.NewsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        var currentTime = OffsetDateTime.now();
        List<NewsJob> newsJobs = newsJobRepository.findAll();
        if(CollectionUtils.isEmpty(newsJobs)) {
            log.warn("No Records Found");
            return;
        }
        log.info("Number of records Fetched : {}", newsJobs.size());
        newsJobs = newsJobs.stream().filter(newsJob -> newsJob.getJobLastRunTime().plusMinutes(nextExecutionMinutes).compareTo(OffsetDateTime.now()) < 0)
                .collect(Collectors.toList());
        log.info("Number of Records to be run : {}", newsJobs.size());
        for(NewsJob newsJob: newsJobs) {
            for(NewsService newsService: newsServices) {
                if(newsService.isApplicable(newsJob.getSource())) {
                    newsService.retrieveAndSaveNews(newsJob);
                    newsJob.setJobLastRunTime(currentTime);
                    newsJob.setLastUpdateTime(OffsetDateTime.now());
                    newsJobRepository.save(newsJob);
                }
            }
        }
        log.info("News Job Scheduler End");
    }
}
