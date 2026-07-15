package com.orion.mdd.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyResponseDto {

    private String message;
    private String code;
    private Map<String, Object> data;

}
