package com.avengers.musinsa.domain.search.repository;

import com.avengers.musinsa.domain.search.dto.SearchSaveDto;
import com.avengers.musinsa.domain.search.entity.SearchLog;

public interface SearchLogRepository {
    Long save(SearchLog searchLog);

    Long saveSearchBrandLog(SearchSaveDto.searchBrandLogSaveDto brandLogSaveDto);

    Long saveSearchKeywordLog(SearchSaveDto.searchKeywordLogSaveDto brandLogSaveDto);
}
