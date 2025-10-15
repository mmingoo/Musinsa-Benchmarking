package com.avengers.musinsa.domain.search.entity;

import com.avengers.musinsa.domain.user.entity.User;
import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLog {
    private Long searchLogId;

    private User user;
    private Long userId;

    private String searchText;
    private Timestamp searchDateTime;
    private Integer searchCount;
}
