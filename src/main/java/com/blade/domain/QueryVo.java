package com.blade.domain;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class QueryVo {
    //这里的变量名，要与页面传过来的变量名，保持一致
    private int page;
    private int rows;
    private String keyword;
}
