package org.anonymous.global.validators;

/**
 * 공통 비밀번호 검증
 *
 */
public interface PasswordValidator {

    /**
     * Alphabet 복잡성 체크
     * 1) 대소문자 각각 1개 이상 있는 경우
     * 2) 대소문자 구분 없이 Alphabet 1자 이상
     *
     * @param caseInsensitive : true = 2), false = 1)
     * @return
     */
    default boolean alphaCheck(String password, boolean caseInsensitive) {

        // 대소문자 구분 없이 Alphabet 1자 이상
        if (caseInsensitive) {
            // matches(String regex), regex = 정규 표현식
            // .* : 0이상 아무 문자 [a-zA-Z]+ : Alphabet 대소문자 상관없이 1자 이상
            return password.matches(".*[a-zA-Z]+.*");
        }

        // 소문자 1개이상 포함되어야 하는 패턴 && 대문자 1개이상 포함되어야 하는 패턴
        return password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*");
    }

    /**
     * 숫자 복잡성 체크
     *
     * [0-9] = \d
     * \\ 두개해야 \ 한개가 남음
     *
     * @param password
     * @return
     */
    default boolean numberCheck(String password) {

        // 숫자 1개 이상 포함
        return password.matches(".*\\d.*");
    }

    /**
     * 특수문자 복잡성 체크
     *
     * [^문자..] = 문자는 제외
     * [^\d] = 숫자 제외한 문자
     *
     * @param password
     * @return
     */
    default boolean specialCharscehk(String password) {

        // 숫자 & Alphabet & 한글 제외한 모든 문자 = 특수 문자
        String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣].*";

        return password.matches(pattern);
    }
}