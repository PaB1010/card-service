package org.anonymous.global.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 모든 Entity 의 공통 속성화
 *
 * 추상 Class
 */
// 다른 Entity의 상위 Class임을 알려주는 Annotation, Table 순서는 변경 불가
// 해당 어노테이션 오류 날 시 build.gradle에서 jakarta.persistence-api의 버전을 설정 후 인텔리제이 재구동
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime modifiedAt;

    // 삭제일시, 삭제 표기만 해두고 추후 관리자가 복구하거나 DB 영구 삭제처리 하도록 할 예정
    @JsonFormat(pattern = "yyyy-MM-DD HH:mm:ss")
    private LocalDateTime deletedAt;
}