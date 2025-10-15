package com.avengers.musinsa.domain.search.scheduler;

import com.avengers.musinsa.domain.search.service.PopularKeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PopularKeywordScheduler {

    private final PopularKeywordService popularKeywordService;

    @Scheduled(cron = "0 */30 * * * *")
    public void refreshPopularKeywords() {
        log.info("[PopularKeywordScheduler] 인기 검색어 리프레시 작업 시작");
        popularKeywordService.createPopularKeyword();
        log.info("[PopularKeywordScheduler] 인기 검색어 리프레시 작업 종료");
    }
}

