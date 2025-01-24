package org.anonymous.global.rests;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 모든 JSON Data 형식 통일
 * Error 여부 상관 X
 *
 */
@Data
@NoArgsConstructor
public class JSONData {

    // 어떤 응답인지 알려주기 위해 항상 필요
    // OK가 대체적으로 많으므로 편의상 기본 값 OK 설정
    private HttpStatus status = HttpStatus.OK;

    // true = 성공, false = error
    // 성공이 대체적으로 많으므로 편의상 기본 값 true 설정
    private boolean success = true;

    // 단순한 message 형태의 문자열일수도 있지만
    // 중첩된 필드와 메세지일 수도 있으므로
    // Object 자료형 사용
    // 실패시 Error message;
    private Object message;

    // 성공시 Data
    private Object data;

    // 기본 생성자
    public JSONData(Object data) {

        this.data = data;
    }
}