package org.anonymous.global.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 목록 Data
 *
 * /list?page=1&limit=100
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListData<T> {

    // 목록 Data
    private List<T> items;

    // Paging 기초 Data
    private Pagination pagination;
}