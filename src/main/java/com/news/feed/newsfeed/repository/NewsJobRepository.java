package com.news.feed.newsfeed.repository;

import com.news.feed.newsfeed.entity.NewsJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface NewsJobRepository extends JpaRepository<NewsJob, Long> {

    List<NewsJob> findByJobNextRunTimeLessThanEqual(OffsetDateTime currentTime);
}
