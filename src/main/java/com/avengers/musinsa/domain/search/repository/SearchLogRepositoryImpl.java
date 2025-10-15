package com.avengers.musinsa.domain.search.repository;

import com.avengers.musinsa.domain.search.dto.SearchSaveDto;
import com.avengers.musinsa.domain.search.entity.SearchLog;
import com.avengers.musinsa.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class SearchLogRepositoryImpl implements SearchLogRepository {

    //SearchLogMapper 주입
    private final SearchMapper searchMapper;

    // 검색 로그 저장 후 생성된 logId 반환
    @Override
    public Long save(SearchLog searchLog) {
        searchMapper.insertSearchLog(searchLog); // 검색 로그 저장
        return searchLog.getSearchLogId(); // 엔티티의 logId 반환
    }

    @Override
    public Long saveSearchBrandLog(SearchSaveDto.searchBrandLogSaveDto brandLogSaveDto) {
        searchMapper.insertSearchBrandLog(brandLogSaveDto); // 검색 로그 저장
        return brandLogSaveDto.getSearchBrandLogSaveId(); // 엔티티의 logId 반환
    }

    @Override
    public Long saveSearchKeywordLog(SearchSaveDto.searchKeywordLogSaveDto keywordLogSaveDto) {
        searchMapper.insertSearchKeywordLog(keywordLogSaveDto); // 검색 로그 저장
        return keywordLogSaveDto.getSearchKeywordLogSaveId(); // 엔티티의 logId 반환
    }


}
