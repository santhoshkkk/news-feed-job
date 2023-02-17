package com.news.feed.newsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.news.feed")
public class NewsFeedJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsFeedJobApplication.class, args);
	}

}
