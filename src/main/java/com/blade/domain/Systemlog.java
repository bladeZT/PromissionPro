package com.blade.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter@Setter@ToString
public class Systemlog {
    private Long id1;

    private Date optime1;

    private String ip1;

    private String function1;

    private String params1;


}