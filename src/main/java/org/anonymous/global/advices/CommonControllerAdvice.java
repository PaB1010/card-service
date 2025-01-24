package org.anonymous.global.advices;

import lombok.RequiredArgsConstructor;
import org.anonymous.global.exceptions.CommonException;
import org.anonymous.global.libs.Utils;
import org.anonymous.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestControllerAdvice("org.anonymous")
public class CommonControllerAdvice {

    private final Utils utils;

    // Error 도 항상 동일한 형식(JSONData 형식)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData> errorHandler(Exception e) {

        // default Error Code = 500
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // CommonException에서 message를 String, Map으로 했으니 Object
        Object message = e.getMessage();

        // 정의한 예외일 경우 응답코드 get
        if (e instanceof CommonException commonException) {

            status = commonException.getStatus();

            /**
             * ApiFileController Class 의
             * if (errors.hasErrors()) {
             *     throw new BadRequestException(utils.getErrorMessages(errors));
             * }
             */
            Map<String, List<String>> errorMessages = commonException.getErrorMessages();

            if (errorMessages != null) {

                message = errorMessages;

            } else {
                // ErrorCode 형태 판별 후 message 조회해서 message 만 뺴내서 반환
                // message 형태일 경우 message 반환
                message = commonException.isErrorCode() ? utils.getMessage((String)message) : message;
            }
        }

        JSONData data = new JSONData();

        data.setSuccess(false);
        data.setStatus(status);
        data.setMessage(message);

        e.printStackTrace();

        // ★ 응답 Code & Body 상세 설정 ★
        return ResponseEntity.status(status).body(data);
    }
}