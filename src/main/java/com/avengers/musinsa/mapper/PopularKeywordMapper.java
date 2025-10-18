package com.avengers.musinsa.mapper;

import com.avengers.musinsa.domain.search.dto.KeywordCountDTO;
import com.avengers.musinsa.domain.search.dto.ResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PopularKeywordMapper {
    List<ResultDTO> selectTrendingKeywords();

    String selectTrendingKeywordsUpdatedAt();

    List<KeywordCountDTO> selectRecentKeywordCounts(@Param("from") LocalDateTime from,
                                                    @Param("to") LocalDateTime to);

    int insertKeyword(KeywordCountDTO keywordCountDTO);

    int updateKeyword(KeywordCountDTO keywordCountDTO);
}
