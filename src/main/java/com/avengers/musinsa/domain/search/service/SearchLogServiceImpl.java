package com.avengers.musinsa.domain.search.service;

import com.avengers.musinsa.domain.brand.dto.response.BrandResponse;
import com.avengers.musinsa.domain.search.dto.SearchLogResponseDTO;
import com.avengers.musinsa.domain.search.dto.SearchLogRequestDTO;
import com.avengers.musinsa.domain.search.dto.SearchSaveDto;
import com.avengers.musinsa.domain.search.entity.SearchLog;
import com.avengers.musinsa.domain.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchLogServiceImpl implements SearchLogService{
    private final SearchLogRepository searchLogRepository; // 검색 로그 저장소 주입

    // 검색 로그 저장
    @Override
    public SearchLogResponseDTO saveSearchLog(SearchLogRequestDTO requestDTO) {
        try {
            // DTO를 엔티티로 변환
            SearchLog searchLog = SearchLog.builder()
                    .searchText(requestDTO.getSearchText())    // 검색어 설정
                    .userId(requestDTO.getUserId())            // 사용자 ID 설정
                    .searchCount(requestDTO.getSearchCount())
                    .build();



            // 저장소를 통해 저장하고 생성된 ID 반환
            Long logId = searchLogRepository.save(searchLog);

            // 성공 응답 반환 - JSON -> success : success
            return SearchLogResponseDTO.builder()
                    .logId(logId)
                    .success(true)
                    .build();

        } catch (Exception e) {
            // 에러 발생 시 실패 응답 반환 - JSON -> success : fail
            return SearchLogResponseDTO.builder()
                    .logId(null)
                    .success(false)
                    .build();
        }
    }


    @Override
    public void saveSearchKeywordLog(String keyword, Long userId) {
        try{
            SearchSaveDto.searchKeywordLogSaveDto brandLogSaveDto = SearchSaveDto.searchKeywordLogSaveDto.builder()
                    .userId(userId)
                    .searchText(keyword)
                    .build();

            Long logId = searchLogRepository.saveSearchKeywordLog(brandLogSaveDto);
            log.info("검색어 로그 저장 id = "+logId);

        }catch (Exception e){

            log.info("검색어 로그 저장 실패");

        }
    }

    @Override
    public void saveSearchBrandLog(BrandResponse brandResponse, Long userId) {
        try{

            SearchSaveDto.searchBrandLogSaveDto brandLogSaveDto = SearchSaveDto.searchBrandLogSaveDto.builder()
                    .userId(userId)
                    .brandId(brandResponse.getBrandId())
                    .build();

            Long logId = searchLogRepository.saveSearchBrandLog(brandLogSaveDto);
            log.info("브랜드 검색 로그 저장 id = "+logId);

        }catch (Exception e){

            log.info("브랜드 검색 로그 실패");

        }
    }

}
