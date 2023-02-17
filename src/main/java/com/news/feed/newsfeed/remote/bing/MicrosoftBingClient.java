package com.news.feed.newsfeed.remote.bing;

import com.news.feed.newsfeed.remote.bing.model.BingSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "MicrosoftBingClient", url = "${remote.bing.base-url}")
public interface MicrosoftBingClient {

    @GetMapping
    BingSearchResponse getBingNewsSearch(@RequestHeader(value = "Ocp-Apim-Subscription-Key") String subscriptionId,
                                         @RequestParam(value = "q") String query, @RequestParam(value = "count") int count,
                                         @RequestParam(value = "offset") int offset);
}
