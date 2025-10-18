package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.search.dto.SearchSaveDto;
import com.avengers.musinsa.domain.search.entity.SearchLog;
import com.avengers.musinsa.domain.search.response.SearchKeywordResponseDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchMapper {

    List<SearchKeywordResponseDTO> getRecentSearches(@Param("user_id") Long userId);

    void deleteRecentSearches(@Param("userId") Long userId);

    void deleteRecentSearchKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);

    //검색 로그 저장 - 자동생성된 ID를 엔티티에 설정
    void insertSearchLog(SearchLog searchLog);

    void insertSearchBrandLog(SearchSaveDto.searchBrandLogSaveDto searchBrandLogSaveDto);

    void insertSearchKeywordLog(SearchSaveDto.searchKeywordLogSaveDto keywordLogSaveDto);
}
