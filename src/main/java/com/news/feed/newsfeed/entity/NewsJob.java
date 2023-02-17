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
@Table(name = "newsjob")
public class NewsJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String stockName;
    private String searchKeyword;
    private String source;
    private OffsetDateTime jobLastRunTime;
    private OffsetDateTime jobNextRunTime;
    private OffsetDateTime createdDateTime;
    private OffsetDateTime lastUpdateTime;

}
