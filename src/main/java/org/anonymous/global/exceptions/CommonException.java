package org.anonymous.global.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter @Setter
public class CommonException extends RuntimeException {

    // HttpStatus = Enum Class
    private HttpStatus status;

    // 에러 코드가 맞나 체크
    private boolean errorCode;

    private Map<String, List<String>> errorMessages;

    public CommonException(String message, HttpStatus status) {

        super(message);

        // status = null일 경우 HttpStatus.INTERNAL_SERVER_ERROR로 대체
        this.status = Objects.requireNonNullElse(status, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @RestController 에서 커맨드 객체 검증 실패시
     *
     * Error Message 정보 가공해서 set 하기 위한 생성자 메서드
     *
     * @param errorMessages
     * @param status
     */
    // 같은 Field 에 메세지가 여러개인 경우도 있고 하나인 경우도 있어서 <Object>
    public CommonException(Map<String, List<String>> errorMessages, HttpStatus status) {

        this.errorMessages = errorMessages;
        this.status = status;
    }
}