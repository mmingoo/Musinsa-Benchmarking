package com.avengers.musinsa.global.util.uuid;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Uuid /* extends BaseEntity -> 추후 추가 시 주석 제거 */  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uuid_seq")
    @SequenceGenerator(name = "uuid_seq", sequenceName = "uuid_sequence", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Builder
    public Uuid(String uuid) {
        this.uuid = uuid;
    }
}
