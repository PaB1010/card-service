package org.anonymous.train.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainData {

    @Id
    @GeneratedValue
    private Long seq;

    // 특성
    private double item1;
    private double item2;
    private double item3;
    private double item4;
    private double item5;

    // 정답
    private int target;

    /**
     * 정답데이터에 대한 설명
     *
     * 정수 || 문자열
     *
     * 정수 : 카드 번호 등
     * 문자열 : 카드 이름, 설명 등
     */
    @Column(length = 40)
    private String targetDescription;

    /**
     * 훈련 완료 여부
     * true - 훈련 완료
     * false - 훈련 예정
     */
    private boolean done;
}