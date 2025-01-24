package org.anonymous.global.paging;

import lombok.Data;

/**
 * 공통 검색 항목 속성
 *
 */
@Data
public class CommonSearch {

    // 페이지 번호
    private int page = 1;

    // 페이지당 출력 개수
    private int limit = 20;

    // 검색 옵션
    private String sopt;

    // 검색 키워드
    private String skey;
}