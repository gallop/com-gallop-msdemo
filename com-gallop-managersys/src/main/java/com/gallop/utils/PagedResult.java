package com.gallop.utils;

import lombok.Data;

import java.util.List;

/**
 * author gallop
 * date 2020-04-18 18:02
 * Description:
 * Modified By:
 */
@Data
public class PagedResult {
    private int page;			// 当前页数
    private int total;			// 总页数
    private long records;		// 总记录数
    private List<?> rows;		// 每行显示的内容
}
