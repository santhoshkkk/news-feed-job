package com.news.feed.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.OffsetDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockName;
    private OffsetDateTime datePublished;
    private String source;
    private String title;
    private String url;
    private String newsDescription;
    private OffsetDateTime createdDateTime;
    private OffsetDateTime lastUpdateTime;

}
